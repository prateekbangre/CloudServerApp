package com.example.cloudserverapp;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.io.File;

public class UserPref {

    Context context;
    SharedPreferences preferences;

    public UserPref(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("Pref", Context.MODE_PRIVATE);
    }

    public UserPref() {
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void setPreferences(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPreferences(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void addAuthentication(String email, String token, String regDate, String name) {
        setPreferences("email", email);
        setPreferences("token", token);
        setPreferences("name", name);
        setPreferences("regDate", regDate);
    }

    public void clearPref(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearAll() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    public String[] getAuthentication() {
        String email = getPreferences("email", "");
        String token = getPreferences("token", "");
        if (email.equals("") || token.equals(""))
            return null;
        else
            return new String[]{email, token};
    }

    public void logout() {
        try {
            clearAll();

            deleteCache(context);
        } catch (Exception e) {
        }
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = context.getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getPreferences(String key, String def) {
        return preferences.getString(key, def);
    }

    public int getPreferences(String key, int def) {
        return preferences.getInt(key, def);
    }

}
