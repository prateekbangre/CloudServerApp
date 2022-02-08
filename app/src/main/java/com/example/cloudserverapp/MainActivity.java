package com.example.cloudserverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int FILE_SELECT_CODE = 1000;

    static String prevExplorer = "";
    String explorer, title;
    UserPref userPref;

    private SwipeRefreshLayout refreshlayout;
    private TextView msgTextView;
    HomeAdapter homeAdapter;
    private final List<HomeModel> objects = new ArrayList<>();
    CommonClass commonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        //check login is done or not
        userPref = new UserPref(this);
        commonClass = new CommonClass(this);
        title = getString(R.string.app_name);


        if (getIntent() != null) {
            Intent intent = this.getIntent();
            explorer = intent.getStringExtra("link");
            if (explorer == null) {
                explorer = "";
            }else{
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            }
            title = intent.getStringExtra("title");
            Log.d(TAG, "Oncreate title " + title);
            Log.d(TAG, "Oncreate link " + explorer);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);

        msgTextView = findViewById(R.id.msg_text);
        refreshlayout = findViewById(R.id.refreshlayout);
        RecyclerView recyclerview = findViewById(R.id.recyclerview);
        homeAdapter = new HomeAdapter(MainActivity.this, objects);
        recyclerview.setAdapter(homeAdapter);

        refreshlayout.setOnRefreshListener(() -> {

//            if (refreshlayout.isRefreshing())
//                Log.d("TagHomeFragment", "onCreatedView 5");
            refreshlayout.setRefreshing(false);
            objects.clear();
            downloadDataFromNetworking(Globals.EXPLORER_API + explorer);
        });
        downloadDataFromNetworking(Globals.EXPLORER_API + explorer);

    }

    private void downloadDataFromNetworking(String link) {
        Log.d(TAG, link);
//        showLoading();
        String[] email_token = userPref.getAuthentication();
        Log.d(TAG, "email,token: " + email_token[0] + "," + email_token[1]);
        AndroidNetworking.get(link)
                .addHeaders("email", email_token[0])
                .addHeaders("authorization", email_token[1])
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "DownloadN");
                        readResponse(response);
                        // Log.d("TagHomeFragment Response", String.valueOf(response));
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("TagHomeFragment", "ANERROR: " + anError.getErrorCode() + ", " + anError.getErrorBody());
                        showInfo(commonClass.getOnError(anError), false, true);
                    }
                });
    }

    private void readResponse(JSONObject response) {
        Log.d("TagHomeFragment", "response 2 " + response.toString());
        try {
            if (!response.getString("message").equalsIgnoreCase("success")) {
                throw new JSONException("");
            }
            JSONArray array = response.getJSONArray("node");
            Log.d("HomeFragment", "resp: " + response.toString());
            if (array.length() == 0) {
                showInfo("This folder is empty", false, true);
                return;
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                objects.add(new HomeModel(obj));
            }
            // here we have the array containing the name of files
            Log.d("TagHomeFragmentOb", String.valueOf(objects.get(0).toString()));

            refreshlayout.setVisibility(View.VISIBLE);
            msgTextView.setVisibility(View.GONE);
            homeAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d("HomeFragment", "JSONEXCEP: " + e.getMessage());
            showInfo("Unknown error occur while parsing data", false, true);
        }
    }

    private void showInfo(String msg, boolean refreshView, boolean testView) {

        Log.d("TagHomeFragment", "info text:" + msg);
        if (refreshView){
            refreshlayout.setVisibility(View.VISIBLE);
        }else {
            refreshlayout.setVisibility(View.GONE);
        }
        if (testView){
            msgTextView.setVisibility(View.VISIBLE);
            msgTextView.setText(msg);
        }else {
            msgTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptions");
        switch (item.getItemId()){
            case R.id.action_create_folder:
                    showInput();
                return true;
            case R.id.action_upload_file:
                    showFileChooser();
                    return true;
            case R.id.action_logout:
                Log.d("MainActivity", "action_logout");
                userPref.clearAll();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInput(){
        LayoutInflater factory = LayoutInflater.from(getApplicationContext());
        final View customView = factory.inflate(R.layout.input_folder, null);
        Log.d("HomeFragment", "showing input");
        final EditText txtUrl = customView.findViewById(R.id.editTextTextPersonName);
        // Set the default text to a link of the Queen

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Create a directory")
                .setView(customView)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("HomeFragment", "cancel");
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = txtUrl.getText().toString().trim();
                if (CommonClass.validateFolderName(url)) {
                    Log.d("HomeFragment", "sending mkdir");
                    mkdir(url);
                    dialog.dismiss();
                    return;
                }
                txtUrl.setError("Invalid directory name");
            }
        });
    }

    private void mkdir(String name){
        Log.d("TagHomeFragment", "create file: " + (Globals.CREATE_API + (explorer == null ? "" : explorer)) + ":" + name);
        String[] email_token = userPref.getAuthentication();
        commonClass.showProgress(MainActivity.this, "", "creating directory");
        AndroidNetworking.get(Globals.CREATE_API + (explorer == null ? "" : explorer))
                .addHeaders("email", email_token[0])
                .addHeaders("authorization", email_token[1])
                .addHeaders("name", name)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        commonClass.cancelProgress();
                        Log.d("HomeFragment", "create file res: " + response.toString());
                        if (processResponse(response, false)) {
//                            HomeModel dirModel = new HomeModel(name, "just now", "",
//                                    "/" + name, "DIR");
//                            dirModel.type_ext = "DIR";
//                            dirModel.explorer = "/" + path.replace(" ", "%20");
//                            Log.d("HomeFragment", "Encode r" + dirModel.explorer);
//                            dirModel.title = "/" + name;

                            HomeModel dirModel = null;
                            try {
                                dirModel = new HomeModel(response);
                            } catch (JSONException e) {
                                Log.d(TAG, "JSONEXCEP: " + e.getMessage());
                                commonClass.showCustomDialog("issue while creating folder", "Unknown error occur while parsing data", getApplicationContext());
                            }

                            objects.add(0, dirModel);
                            refreshlayout.setVisibility(View.VISIBLE);
                            msgTextView.setVisibility(View.GONE);
                            homeAdapter.notifyItemInserted(0);

                            Toast.makeText(getApplicationContext(), name + " created successfully", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("HomeAdapter", "ANERROR: " + anError.getErrorCode() + ", " + anError.getErrorBody());
                        commonClass.showCustomDialog("Create error", commonClass.getOnError(anError), getApplicationContext());
                        commonClass.cancelProgress();
                    }
                });
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(intent, FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("HomeFragment", "CODE ;;;;" + requestCode + ";" + data);
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK && null != data) {
                Uri content_describer = data.getData();
                try {
                    PathUtil pathUtil = new PathUtil();
                    String path = pathUtil.getPath(getApplicationContext(), content_describer);
                    Log.d("HomeFragment", "path to picker: " + path);
                    uploadFile(path, getFileSize(content_describer));

                } catch (Exception e) {
                    e.printStackTrace();
                    commonClass.showCustomDialog("", "Unable to retrieve path, please try again.", getApplicationContext());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private long getFileSize(Uri fileUri) {
        Cursor returnCursor = getApplicationContext().getContentResolver().
                query(fileUri, null, null, null, null);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        return returnCursor.getLong(sizeIndex);
    }

    private void uploadFile(String path, long size) {
        Log.d("TagHomeFragment", "uploadFile");
        File f = new File(path);
        String name = f.getName();
        final String ext;
        int i = name.lastIndexOf('.');
        if (i > 0) {
            ext = name.substring(i + 1);
        } else {
            ext = "";
        }
        commonClass.showProgress(MainActivity.this, "", "Uploading " + path);
        String[] email_token = userPref.getAuthentication();
        Log.d("HomeFragment", "Upload fle to: " + Globals.UPLOAD_API + (explorer == null ? "" : explorer) + " >> " +
                path);
        AndroidNetworking.upload(Globals.UPLOAD_API + (explorer == null ? "" : explorer))
                .addMultipartFile("file", new File(path))
                .addHeaders("email", email_token[0])
                .addHeaders("authorization", email_token[1])
                .setPriority(Priority.MEDIUM)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        float progress = (bytesUploaded * 100f / totalBytes);
                        Log.d("HomeFragment", (bytesUploaded * 100 / totalBytes) + "");
                        commonClass.setTextProgress("Uploading file (" + (int) progress + "%)...");
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("HomeFragment", "upload complete " + response);
                        commonClass.cancelProgress();
                        if (processResponse(response, false)) {
                            HomeModel dirModel = new HomeModel(new HomeModel().removeExtension(name.replace(" ", "_")), "just now", CommonClass.humanReadable(size),
                                    "/explorer/" + explorer + "/" + name.replace(" ", "_"), "." + ext);
                            URL url = null;
                            try {
                                url = new URL(Globals.API + "/explorer" + explorer + "/" + name.replace(" ", "_"));
                                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                url = uri.toURL();
                                dirModel.setLink(url.toString());
                            } catch (MalformedURLException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                            dirModel.type_ext = "." + ext;
                            dirModel.explorer = explorer + "/" + name.replace(" ", "%20");
                            Log.d("HomeFragment", "Encode r" + dirModel);
                            dirModel.title = "/" + name;
                            Log.d("HomeFragment", dirModel.toString());
//                            objects.add(0, dirModel);
//                            initView();
//                            homeAdapter.notifyItemInserted(0);
                            objects.clear();
                           downloadDataFromNetworking(Globals.EXPLORER_API + explorer);

                            Toast.makeText(getApplicationContext(), name + " created successfully", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        commonClass.cancelProgress();
                        // handle error
                        commonClass.showCustomDialog("Upload error", commonClass.getOnError(error),
                                getApplicationContext());
                        Log.d("HomeFragment", error.getErrorBody() + "," + error.getErrorCode() + "," + error.getErrorDetail());
                        Log.d("HomeFragment", "errorrrr");
                    }
                });
    }

    private boolean processResponse(JSONObject response, boolean a) {
        Log.d("TagHomeFragment", "processResponse 1 " + response.toString());
        try {
            if (response.getString("message").equalsIgnoreCase("success")) {
                return true;
            }
            commonClass.showCustomDialog("File not deleted", response.getString("message"), getApplicationContext());
        } catch (JSONException e) {
            Log.d("HomeAdapter", "JSONEXCEP: " + e.getMessage());
            commonClass.showCustomDialog("File not deleted", "Unknown error occur while parsing data", getApplicationContext());
            return false;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}