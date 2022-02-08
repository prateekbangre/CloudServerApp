package com.example.cloudserverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAccountActivity extends AppCompatActivity {

    public static String TAG = "RegisterAccount";
    Button signIn;
    EditText name, email, password, confirmPassword;
    CommonClass commonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        name = findViewById(R.id.name_textbox_register);
        email = findViewById(R.id.email_textbox_register);
        password = findViewById(R.id.password_textbox_register);
        confirmPassword = findViewById(R.id.confirm_password_textbox_register);
        signIn = findViewById(R.id.signin_button_register);

        commonClass = new CommonClass();

        confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    createAccount(getWindow().getDecorView().getRootView());
                }
                return false;
            }
        });
    }

    public boolean isPasswordMatch() {
        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Password not match");
            return false;
        } else
            return true;
    }

    public void createAccount(View view) {

        if (commonClass.validateEditText(name, "name") && commonClass.validateInput(email, password) && isPasswordMatch()) {
            commonClass.showProgress(RegisterAccountActivity.this, null, "Creating an account for you, please wait...");
            Log.d(TAG, "SignUp: " + Globals.SIGN_UP_API + ";" +
                    "email:" + email.getText().toString() + ";" +
                    "name:" + name.getText().toString() + ";" +
                    "pass:" + password.getText().toString());
            AndroidNetworking.post(Globals.SIGN_UP_API)
                    .addUrlEncodeFormBodyParameter("email", email.getText().toString())
                    .addUrlEncodeFormBodyParameter("name", name.getText().toString())
                    .addUrlEncodeFormBodyParameter("password", password.getText().toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "resp: " + response.toString());
                            commonClass.cancelProgress();
                            // do anything with response
                            try {
                                String msg = response.getString("message");
                                commonClass.showCustomDialog("Account Registered",
                                        "Congratulations " + name.getText().toString() + "!\nYour account has been registered successfully.\n\nYou can now login to drive.",
                                        RegisterAccountActivity.this);
                            } catch (JSONException e) {

                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d(TAG, "Error: " + error.getErrorBody() + ";" + error.getErrorDetail() + ";" + error.getErrorCode());
                            commonClass.cancelProgress();
                            try {
                                // handle error
                                if (error.getErrorCode() != 0) {
                                    JSONObject jsonObject = new JSONObject(error.getErrorBody());
                                    commonClass.showCustomDialog(
                                            CommonClass.find(error.getErrorCode() + ""),
                                            jsonObject.getString("message"),
                                            RegisterAccountActivity.this);
                                    return;
                                }
                            } catch (JSONException e) {
                                commonClass.showCustomDialog(
                                        "",
                                        "Something bad happened on our end, please try again later.",
                                        RegisterAccountActivity.this);
                                return;
                            }
                            Log.e(TAG, "LoginError : " + error.getErrorBody() + ", " + error.getMessage() + ", " + error.getErrorCode() + ", " + error.getErrorDetail());
                            commonClass.showConnectionError(RegisterAccountActivity.this);
                        }
                    });
        }
    }
}