package com.example.cloudserverapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

public class HomeModel {

    private String fileName, fileCreated, fileSize, link;
    FILE_TYPE type;
    public String type_ext;
    public String title;
    public String explorer;

    public HomeModel() {}

    public HomeModel(String fileName, String fileCreated, String fileSize, String link, String type) {
        Log.d("TagHomeModel", "HomeMode function");
        this.fileName = fileName;
        this.fileCreated = fileCreated;
        this.fileSize = fileSize;
        this.link = Globals.API + link;
        this.type = Globals.getType(type);
    }

    public HomeModel(JSONObject object) throws JSONException {
        this.type = Globals.getType(object.getString("type"));
        this.fileName = removeExtension(object.getString("name"));
        this.fileCreated = object.getString("cdate");
        this.fileSize = object.getString("size");
        this.link = Globals.API + object.getString("link");
        if (object.getString("link").split("/").length > 2) {
            this.title = "/" + URLDecoder.decode(object.getString("link").split("/")[object.getString("link").split("/").length - 1]);
            this.explorer = join(getItems(object.getString("link").split("/")), "/");
        } else
            this.title = "/";


        this.type_ext = object.getString("type");
    }

    public String removeExtension(String s) {
        if (this.type == FILE_TYPE.FOLDER)
            return s;
        int index = s.lastIndexOf(".");
        if (index == -1)
            return s;
        return s.substring(0, index);
    }


    public static String join(String[] arr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null)
                sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String[] getItems(String[] arr) {
        String[] items = new String[arr.length - 1];
        if (arr.length - 2 >= 0) System.arraycopy(arr, 2, items, 1, arr.length - 2);
        return items;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFileName() {
        Log.d("HomeModel", "getFilenam: " + fileName);
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileCreated() {
        return fileCreated;
    }

    public void setFileCreated(String fileCreated) {
        this.fileCreated = fileCreated;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public FILE_TYPE getType() {
        return type;
    }

    public void setType(FILE_TYPE type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HomeModel{" +
                "fileName='" + fileName + '\'' +
                ", fileCreated='" + fileCreated + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", link='" + link + '\'' +
                ", type=" + type +
                ", type_ext='" + type_ext + '\'' +
                ", title='" + title + '\'' +
                ", explorer='" + explorer + '\'' +
                '}';
    }
}
