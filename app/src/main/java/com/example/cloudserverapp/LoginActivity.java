package com.example.cloudserverapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    Button signIn, createNewAccount;
    EditText email, password;
    UserPref userPref;
    CommonClass commonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //check for read write permission
        requestForStoragePermission();

        userPref = new UserPref(LoginActivity.this);
        commonClass = new CommonClass();
        //check login happened or not
        if (userPref.getAuthentication() != null) {
            // previously login done
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("new", userPref.getPreferences("new", 0) == 1);
            intent.putExtra("title", getResources().getString(R.string.app_name));
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }else {
            // when login not done
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .build();
            AndroidNetworking.initialize(getApplicationContext(), okHttpClient);

            email = findViewById(R.id.email_textbox);
            password = findViewById(R.id.password_textbox);
            createNewAccount = signIn = findViewById(R.id.createNewAccount);
            signIn = findViewById(R.id.signin_button);

            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        signIn(getWindow().getDecorView().getRootView());
                    }
                    return false;
                }
            });
        }
    }

    // Read/Write permission
    private void requestForStoragePermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void signIn(View view) {
        if (commonClass.validateInput(email, password)) {
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            commonClass.showProgress(LoginActivity.this, "", "Signing in...");
            Log.d(TAG,"Auth Token:: " + email.getText().toString());
            AndroidNetworking.post(Globals.SIGN_IN_API)
                    .addUrlEncodeFormBodyParameter("email", emailValue)
                    .addUrlEncodeFormBodyParameter("password", passwordValue)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // do anything with response
                            commonClass.cancelProgress();
                            try {
                                Log.d(TAG, "response Total: " + response.toString());
                                userPref.addAuthentication(
                                        emailValue,
                                        response.getString("token"),
                                        response.getString("regdate"),
                                        response.getString("name")
                                );
                                userPref.setPreferences("email", response.getString("email"));
                                userPref.setPreferences("name", response.getString("name"));
                                Log.d("TagLoginActivity", response.getString("node"));

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                authError();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            commonClass.cancelProgress();
                            try {
                                // handle error
                                if (error.getErrorCode() != 0) {
                                    JSONObject jsonObject = new JSONObject(error.getErrorBody());
                                    commonClass.showCustomDialog(CommonClass.find(error.getErrorCode() + ""),
                                            jsonObject.getString("message"),
                                            LoginActivity.this);
                                    return;
                                }
                            } catch (JSONException e) {
                                authError();
                                return;
                            }
                            Log.e(TAG, "LoginError : " + error.getErrorBody() + ", " + error.getMessage() + ", " + error.getErrorCode() + ", " + error.getErrorDetail());
                            commonClass.showConnectionError(LoginActivity.this);
                        }
                    });
        }
    }

    private void authError() {
        commonClass.cancelProgress();
        commonClass.showCustomDialog(null,
                "We can't authenticate you at this time, try again later." +
                        "\nIf problem persists please contact app administrator.",
                LoginActivity.this);
    }

    public void createNewAccount(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterAccountActivity.class));
//        overridePendingTransition(R.anim.right_to_left, R.anim.blank_anim);
    }
}