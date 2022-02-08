package com.example.cloudserverapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "HomeAdapter";
    private List<HomeModel> objects;

    private Context context;
    UserPref userPref;
    private LayoutInflater layoutInflater;
    String[] email_token;
    CommonClass commonClass = new CommonClass();

    public HomeAdapter(Context context, List<HomeModel> objects) {
        Log.d(TAG, "HomeAdapter function");
        this.context = context;
        this.objects = objects;
        userPref = new UserPref(context);
        email_token = userPref.getAuthentication();
        this.layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ((ViewHolder) holder).fileTitle.setText(objects.get(position).getFileName());
        ((ViewHolder) holder).fileCreated.setText(objects.get(position).getFileCreated());
        if (objects.get(position).getType() == FILE_TYPE.FOLDER) {
            ((ViewHolder) holder).fileSize.setVisibility(View.GONE);
            ((ViewHolder) holder).textView7.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).fileSize.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).fileSize.setText(objects.get(position).getFileSize());
            ((ViewHolder) holder).textView7.setVisibility(View.VISIBLE);
        }
        switch (objects.get(position).getType()) {
            case TEXT:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_notepad_svgrepo_com);
                break;
            case FOLDER:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_icons8_folder);
                break;
            case VIDEO:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_iconmonstr_video_8);
                break;
            case IMAGE:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_image_svgrepo_com);
                break;
            case AUDIO:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_audio_svgrepo_com);
                break;
            case ARCH:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_zip_file);
                break;
            default:
                ((ViewHolder) holder).fileIcon.setBackgroundResource(R.drawable.ic_unknown);
        }

        if (objects.get(position).getType() != FILE_TYPE.FOLDER) {
            ((ViewHolder) holder).goNext.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).goNext.setVisibility(View.VISIBLE);
        }

        // MORE MENU BUTTON
        final HomeModel objj = objects.get(position);
        ((ViewHolder) holder).downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadFile(objj.getFileName() + objj.type_ext.toLowerCase(),
                        Globals.EXPLORER_API + objj.explorer);
            }
        });

        ((ViewHolder) holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile(objj);
            }
        });

        ((ViewHolder) holder).goNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFolder(objj);
            }
        });

        if (objects.get(position).getType() != FILE_TYPE.FOLDER) {
            String n = objects.get(position).getFileName() + objects.get((position)).type_ext.toLowerCase();
            String l = Globals.EXPLORER_API + objects.get(position).explorer;
            ((ViewHolder) holder).customFileCard.setOnClickListener(view ->
                    downloadFile(n, l)
            );
        } else {
            Uri uri;
//            Bundle arguments = new Bundle();
//            arguments.putString("link", objects.get(position).explorer);
//            arguments.putString("title", objects.get(position).title);
//            ((ViewHolder) holder).customFileCard.setOnClickListener(view -> Navigation.findNavController(view).navigate(
//                    R.id.nav_home, arguments));

//            Intent intent = new Intent(context, MainActivity.class);
//            intent.putExtra("link", objects.get(position).explorer);
//            intent.putExtra("title", objects.get(position).title);
//            context.startActivity(intent);
            Log.d(TAG, "onBindViewHolder: else");
//            ((ViewHolder) holder).customFileCard.setOnClickListener(view -> Navigation.findNavController(view).navigate(
//                    R.id.nav_home, arguments));
            ((ViewHolder) holder).customFileCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFolder(objj);
                }
            });
        }
    }

    private void openFolder(HomeModel object) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("link", object.explorer);
        intent.putExtra("title", object.title);
//        MainActivity.prevExplorer = "";
        Log.d(TAG, "openFolder: link: "+object.explorer);
        Log.d(TAG, "openFolder: title: "+object.title);
        context.startActivity(intent);
    }

//    private void menu(final View view, final HomeModel obj) {
//        PopupMenu popup = new PopupMenu(context, view);
//        popup.getMenuInflater()
//                .inflate(R.menu.popup_menu, popup.getMenu());
//        popup.getMenu().getItem(0).setVisible(obj.getType() != FILE_TYPE.FOLDER);
//
//        //registering popup with OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.action_delete) {
//                    deleteFile(obj);
//                } else if (item.getItemId() == R.id.action_download) {
//                    downloadFile(obj.getFileName() + obj.type_ext.toLowerCase(),
//                            Globals.EXPLORER_API + obj.explorer);
//                }
//                return true;
//            }
//        });
//        popup.show(); //showing popup menu
//    }

    private void deleteFile(final HomeModel obj) {
        commonClass.showProgress(context, "", "deleting directory...");
        Log.d("HomeAdapter", "delete file: " + Globals.DELETE_API + obj.explorer);
        AndroidNetworking.get(Globals.DELETE_API + obj.explorer)
                .addHeaders("email", email_token[0])
                .addHeaders("authorization", email_token[1])
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        commonClass.cancelProgress();
                        Log.d("HomeAdapter", "delete file res: " + response.toString());
                        if (processResponse(response)) {
                            int pos = objects.indexOf(obj);
                            objects.remove(obj);
                            notifyItemRemoved(pos);
                            Toast.makeText(context, obj.getFileName() + " deleted successfully", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        commonClass.cancelProgress();
                        Log.d("HomeAdapter", "ANERROR: " + anError.getErrorCode() + ", " + anError.getErrorBody());
                        commonClass.showCustomDialog("Delete error", commonClass.getOnError(anError),
                                context);
                    }
                });
    }

    private boolean processResponse(JSONObject response) {
        try {
            if (response.getString("message").equalsIgnoreCase("success")) {
                return true;
            }
            commonClass.showCustomDialog("File not deleted", response.getString("message"), context);
        } catch (JSONException e) {
            Log.d("HomeAdapter", "JSONEXCEP: " + e.getMessage());
            commonClass.showCustomDialog("File not deleted", "Unknown error occur while parsing data", context);
            return false;
        }
        return false;
    }

    private void downloadFile(String title, String url) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                commonClass.showCustomDialog("Permission denied", "Please allow the permission in settings to work app properly.", context);
//                return;
//            }
//        }
        commonClass.showProgress(context, title, "Downloading file...");
        // this is executed during download
        String external = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        Log.d("HomeAdapter", "Download task: " + external
                + "; title: " + title + "; url: " + url);
        File f = new File(external + "/" + title);
        //Log.d("HomeAdapter", HomeModel);
        if (f.exists()) {
        }

        AndroidNetworking.download(url,
                        external,
                        title)
                .setTag("downloadTest")
                .addHeaders("email", email_token[0])
                .addHeaders("authorization", email_token[1])
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        float progress = (bytesDownloaded * 100f) / totalBytes;
                        Log.d("HomeAdapter", "prgoress: " + progress);
                        commonClass.setTextProgress("Downloading file (" + (int) progress + "%)...");
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        commonClass.cancelProgress();
                        commonClass.showCustomDialog(title, "File has been successfuly saved to path \"" + external + "\"", context);
                    }

                    @Override
                    public void onError(ANError error) {
                        commonClass.cancelProgress();
                        Log.d("HomeAdapter", "ANERROR: " + error.getErrorCode() + ", " + error.getErrorBody() + "," +
                                error.getMessage()  + ";" + error.getErrorDetail()  + "," + error.getResponse());
                        commonClass.showCustomDialog("", commonClass.getOnError(error), context);
                    }
                });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private void initializeViews(HomeModel object, ViewHolder holder) {
        //TODO implement
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout customFileCard;
        private ImageView fileIcon, goNext;
        private TextView fileTitle;
        private ImageButton downloadBtn;
        private ImageButton deleteBtn;
        private TextView fileCreated;
        private TextView textView7;
        private TextView fileSize;

        public ViewHolder(@NonNull View view) {
            super(view);
            customFileCard = (ConstraintLayout) view.findViewById(R.id.custom_file_card);
            fileIcon = (ImageView) view.findViewById(R.id.file_icon);
            goNext = (ImageView) view.findViewById(R.id.next_btn);
            fileTitle = (TextView) view.findViewById(R.id.file_title);
            downloadBtn = (ImageButton) view.findViewById(R.id.download_btn);
            deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);
            fileCreated = (TextView) view.findViewById(R.id.file_created);
            textView7 = (TextView) view.findViewById(R.id.textView7);
            fileSize = (TextView) view.findViewById(R.id.file_size);
        }
    }
}
