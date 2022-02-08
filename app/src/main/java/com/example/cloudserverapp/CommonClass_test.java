package com.example.cloudserverapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.androidnetworking.error.ANError;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonClass_test {
//    private static ProgressDialog progress = null;
//    private Context context;
//
//    public CommonClass_test() {
//    }
//
//    public static String humanReadable(long n) {
//        String[] s = new String[5];
//        s[0] = " B";
//        s[1] = " KiB";
//        s[2] = " MiB";
//        s[3] = " GiB";
//        s[4] = " TiB";
//        StringBuilder sb = new StringBuilder();
//        int i = 0;
//        while (n > 1024) {
//            n = n / 1024;
//            i++;
//        }
//        sb.append(n);
//        sb.append(s[i]);
//        return sb.toString();
//    }
//
//    public static boolean validateFolderName(String folderName) {
//        if (folderName.length() > 255 || folderName.trim().length() == 0) {
//            return false;
//        }
//        if (folderName.contains("/") || folderName.contains("\\")) {
//            return false;
//        }
//        // folder name disallow /\:*?”<>|
//        Pattern pattern = Pattern.compile("[\\\\/:*?\"<>|]");
//        Matcher m = pattern.matcher(folderName);
//        return !m.matches();
//    }
//
//    public CommonClass_test(Context context) {
//        this.context = context;
//    }
//
//    public static String getOrdinalRank(int rank) {
//        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
//        switch (rank % 100) {
//            case 11:
//            case 12:
//            case 13:
//                return rank + "th";
//            default:
//                return rank + suffixes[rank % 10];
//        }
//    }
//
//
//    public static int pxToDp(int px) {
//        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
//    }
//
//    public static int dpToPx(int dp) {
//        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
//    }
//
//    public static String getErrorMessage(int code) {
//        if (code == 500) {
//            return "Internal Server Error\nSomething bad happened on our end, we usually track these issues automatically, if problem persist you can contact us.";
//        }
//        return "unknown";
//    }
//
//    public static String getAdminSubjectShort(String name) {
//        String[] split = name.split("\\(");
//        if (split.length > 1) {
//            return split[0];
//        } else {
//            return name;
//        }
//    }
//
//    public static String getAdminSubjectVeryShort(String name) {
//        String[] split = name.split("\\(");
//        if (split.length > 1) {
//            return split[1].replace(")", "").trim();
//        } else {
//            return name;
//        }
//    }
//
//    public static boolean validateInput(EditText email, EditText password) {
//        boolean check = true;
//        String emailText = email.getText().toString().replace(" ", "");
//        if (!isValidEmailAddress(emailText)) {
//            email.requestFocus();
//            email.setError("Email is invalid!");
//            check = false;
//        } else if (password != null) {
//            if (password.getText().toString().length() < 6) {
//                password.requestFocus();
//                password.setError("Minimum 6 characters are required");
//                check = false;
//            }
//        }
//        return check;
//    }
//
//    public static boolean validateEditText(EditText editText, String name) {
//        boolean check = true;
//        String text = editText.getText().toString().replace(" ", "");
//        if (name.equalsIgnoreCase("NAME")) {
//            String ePattern = "^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$";
//            Pattern p = Pattern.compile(ePattern);
//            Matcher m = p.matcher(text);
//            check = m.matches();
//            if (!check) {
//                editText.requestFocus();
//                editText.setError("Invalid person name");
//            }
//        } else if (TextUtils.isEmpty(editText.getText())) {
//            editText.requestFocus();
//            editText.setError(name + " is required");
//            check = false;
//        }
//        return check;
//    }
//
//    public boolean validatePhoneIndian(EditText mobile_) {
//        String mobile = mobile_.getText().toString();
//        String ePattern = "^[7-9][0-9]{9}$";
//        Pattern p = Pattern.compile(ePattern);
//        Matcher m = p.matcher(mobile);
//        if (m.matches())
//            return true;
//        else {
//            mobile_.setError("Invalid phone");
//            return false;
//        }
//    }
//
//    public static boolean isValidEmailAddress(String email) {
//        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
//        Pattern p = Pattern.compile(ePattern);
//        Matcher m = p.matcher(email);
//        return m.matches();
//    }
//
//    public static void commonClass.showCustomDialog(String title, String given, Context context) {
//        Log.d("TagCommonClass", title+" "+ given);
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(context);
//        builder.setTitle(title)
//                .setMessage(given)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .show();
//    }
//
//    public static void commonClass.showCustomDialog(int icon, String title, String given, Context context) {
//        AlertDialog.Builder builder;
//        builder = new AlertDialog.Builder(context);
//        builder.setIcon(context.getResources().getDrawable(icon));
//        builder.setTitle(title)
//                .setMessage(given)
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                })
//                .show();
//    }
//
//
//    public String getStringRes(Context context, int res) {
//        return context.getResources().getString(res);
//    }
//
//    public static void setTextProgress(String text) {
//        if (progress != null && progress.isShowing())
//            progress.setMessage(text);
//    }
//
//    public static String getOnError(ANError anError) {
//        if (anError.getErrorCode() != 0) {
//            try {
//                return "ERROR_CODE: " + anError.getErrorCode() + "\n"
//                        + new JSONObject(anError.getErrorBody()).getString("message");
//            } catch (Exception e) {
//                try {
//                    return "Something bad happened on our end. please try again\nERROR CODE: " + anError.getErrorCode() +
//                            "\nERROR DETAIL: " + find(anError.getErrorCode() + "");
//                } catch (Exception ignored) {
//                    return "Something bad happened on our end. please try again";
//                }
//            }
//        }
//        return "Request Time Out and destination host unreachable";
//    }
//
//    public static void showConnectionError(Context context) {
//        commonClass.showCustomDialog("Connection Error",
//                "Unable to reach host server, please check your internet connection and try requesting again.",
//                context);
//    }
//
//    public void showUnAuthorized(Context context) {
//        commonClass.showCustomDialog("Unauthorized request",
//                "We're unable to authorize generated request, try requesting again. If problem persists please login again to get new token.",
//                context);
//    }
//
//    public void showNotFoundError(Context context) {
//        commonClass.showCustomDialog("Not Found",
//                "This user doesn't exists anymore maybe it was deleted by administrator.\nPlease contact administrator for more details.",
//                context);
//    }
//
//    public static void showProgress(Context context, String title, String display) {
//        progress = new ProgressDialog(context);
//        progress.setTitle(title);
//        progress.setMessage(display);
//        progress.setCancelable(false);
//        progress.show();
//    }
//
//    public static void cancelProgress() {
//        try {
//            progress.dismiss();
//            progress.cancel();
//        } catch (Exception ignored) {
//        }
//    }
//
//    public String getCurrentDateAndTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("y/M/d H:m:s", Locale.getDefault());
//        return sdf.format(new Date());
//    }
//
//    static String find(String code) {
//        for (String[] codeNMsg : Globals.HTTP_CODES) {
//            if (code.equals(codeNMsg[0]))
//                return codeNMsg[1].split("\n")[0];
//        }
//        return null;
//    }
}
