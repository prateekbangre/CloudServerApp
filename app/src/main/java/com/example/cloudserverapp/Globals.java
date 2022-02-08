package com.example.cloudserverapp;

import android.util.Log;

public class Globals {

//    private static final String IP = "188.60.167.30";
    private static final String IP = "192.168.29.64";
    public static String API = "http://" + IP + "/android/api";

    public static String SIGN_UP_API = API + "/create";
    /* FORM Parameter
        Email,
        Password
     */
    public static String SIGN_IN_API = API + "/login";

    /*
        Explorer
     */
    public static String EXPLORER_API = API + "/explorer";

    /*
        DELETE
     */
    public static String DELETE_API = API + "/delete";

    /*
        CREATE
     */
    public static String CREATE_API = API + "/mkdir";

    /*
        UPLOAD
     */
    public static String UPLOAD_API = API + "/upload";

    static String[] WEB_EXT = ("html\n" +
            "htm\n" +
            "css\n" +
            "js\n" +
            "jsx\n" +
            "less\n" +
            "scss\n" +
            "wasm\n" +
            "php").split("\n");
    static String[] ARCH_EXT = ("7z\n" +
            "a\n" +
            "apk\n" +
            "ar\n" +
            "bz2\n" +
            "cab\n" +
            "cpio\n" +
            "deb\n" +
            "dmg\n" +
            "egg\n" +
            "gz\n" +
            "iso\n" +
            "jar\n" +
            "lha\n" +
            "mar\n" +
            "pea\n" +
            "rar\n" +
            "rpm\n" +
            "s7z\n" +
            "shar\n" +
            "tar\n" +
            "tbz2\n" +
            "tgz\n" +
            "tlz\n" +
            "war\n" +
            "whl\n" +
            "xpi\n" +
            "zip\n" +
            "zipx\n" +
            "xz\n" +
            "pak").split("\n");
    static String[] VIDEO_EXT = ("webm\n" +
            "mkv\n" +
            "flv\n" +
            "vob\n" +
            "ogv\n" +
            "ogg\n" +
            "rrc\n" +
            "gifv\n" +
            "mng\n" +
            "mov\n" +
            "avi\n" +
            "qt\n" +
            "wmv\n" +
            "yuv\n" +
            "rm\n" +
            "asf\n" +
            "amv\n" +
            "mp4\n" +
            "m4p\n" +
            "m4v\n" +
            "mpg\n" +
            "mp2\n" +
            "mpeg\n" +
            "mpe\n" +
            "mpv\n" +
            "m4v\n" +
            "svi\n" +
            "3gp\n" +
            "3g2\n" +
            "mxf\n" +
            "roq\n" +
            "nsv\n" +
            "flv\n" +
            "f4v\n" +
            "f4p\n" +
            "f4a\n" +
            "f4b").split("\n");
    static String[] IMG_EXT = ("3dm\n" +
            "3ds\n" +
            "max\n" +
            "bmp\n" +
            "dds\n" +
            "gif\n" +
            "jpg\n" +
            "jpeg\n" +
            "png\n" +
            "psd\n" +
            "xcf\n" +
            "tga\n" +
            "thm\n" +
            "tif\n" +
            "tiff\n" +
            "yuv\n" +
            "ai\n" +
            "eps\n" +
            "ps\n" +
            "svg\n" +
            "dwg\n" +
            "dxf\n" +
            "gpx\n" +
            "kml\n" +
            "kmz\n" +
            "webp").split("\n");
    static String[] TEXT_EXT = ("doc\n" +
            "docx\n" +
            "ebook\n" +
            "log\n" +
            "md\n" +
            "msg\n" +
            "odt\n" +
            "org\n" +
            "pages\n" +
            "pdf\n" +
            "rtf\n" +
            "rst\n" +
            "tex\n" +
            "txt\n" +
            "wpd\n" +
            "wps").split("\n");
    static String[] AUDIO_EXT = ("aac\n" +
            "aiff\n" +
            "ape\n" +
            "au\n" +
            "flac\n" +
            "gsm\n" +
            "it\n" +
            "m3u\n" +
            "m4a\n" +
            "mid\n" +
            "mod\n" +
            "mp3\n" +
            "mpa\n" +
            "pls\n" +
            "ra\n" +
            "s3m\n" +
            "sid\n" +
            "wav\n" +
            "wma\n" +
            "xm").split("\n");

    public static FILE_TYPE getType(String type) {
        Log.d("Compare", "InputType:" + type);
        type = type.replace(".", "");
        if (type.equalsIgnoreCase("dir"))
            return FILE_TYPE.FOLDER;

        for (String aud : Globals.AUDIO_EXT) {
            Log.d("Compare", "iter1:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.AUDIO;
        }
        for (String aud : Globals.TEXT_EXT) {
            Log.d("Compare", "iter2:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.TEXT;
        }
        for (String aud : Globals.IMG_EXT) {
            Log.d("Compare", "iter3:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.IMAGE;
        }
        for (String aud : Globals.VIDEO_EXT) {
            Log.d("Compare", "iter4:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.VIDEO;
        }
        for (String aud : Globals.ARCH_EXT) {
            Log.d("Compare", "iter5:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.ARCH;
        }
        for (String aud : Globals.WEB_EXT) {
            Log.d("Compare", "iter6:" + aud);
            if (type.equalsIgnoreCase(aud))
                return FILE_TYPE.WEB;
        }
        return FILE_TYPE.UNKNOWN;

    }

    public static String[][] HTTP_CODES = new String[][]{
            new String[]{"100", "Continue\nOnly a part of the request has been received by the server, but as long as it has not been rejected, the client should continue with the request."}, // 1x
            new String[]{"101", "Switching\nThe server switches protocol."}, // 1xx (Information)

            new String[]{"200", "OK\nThe request is OK."}, // 2xx (Successful)
            new String[]{"201", "Created\nThe request is complete, and a new resource is created ."}, // 2xx (Successful)
            new String[]{"202", "Accepted\nThe request is accepted for processing, but the processing is not complete."}, // 2xx (Successful)
            new String[]{"203", "Non-authoritative\nThe information in the entity header is from a local or third-party copy, not from the original server."}, // 2xx (Successful)
            new String[]{"204", "No\nA status code and a header are given in the response, but there is no entity-body in the reply."}, // 2xx (Successful)
            new String[]{"205", "Reset\nThe browser should clear the form used for this transaction for additional input."}, // 2xx (Successful)
            new String[]{"206", "Partial\nThe server is returning partial data of the size requested. Used in response to a request specifying a Range header. The server must specify the range included in the response with the Content-Range header."}, // 2xx (Successful)

            new String[]{"300", "Multiple\nA link list. The user can select a link and go to that location. Maximum five addresses  ."}, // 3xx: Redirection
            new String[]{"301", "Moved\nThe requested page has moved to a new url ."}, // 3xx: Redirection
            new String[]{"302", "Found\nThe requested page has moved temporarily to a new url ."}, // 3xx: Redirection
            new String[]{"303", "See\nThe requested page can be found under a different url ."}, // 3xx: Redirection
            new String[]{"304", "Not\nThis is the response code to an If-Modified-Since or If-None-Match header, where the URL has not been modified since the specified date."}, // 3xx: Redirection
            new String[]{"305", "Use\nThe requested URL must be accessed through the proxy mentioned in the Location header."}, // 3xx: Redirection
            new String[]{"306", "Unused\nThis code was used in a previous version. It is no longer used, but the code is reserved."}, // 3xx: Redirection
            new String[]{"307", "Temporary\nThe requested page has moved temporarily to a new url."}, // 3xx: Redirection

            new String[]{"400", "Bad Request\nThe server did not understand the request."}, // 4xx: Client Error
            new String[]{"401", "Unauthorized\nThe requested page needs a username and a password."}, // 4xx: Client Error
            new String[]{"402", "Payment required\nYou can not use this code yet."}, // 4xx: Client Error
            new String[]{"403", "Forbidden\nAccess is forbidden to the requested page."}, // 4xx: Client Error
            new String[]{"404", "Link not found\nThe server can not find the requested page."}, // 4xx: Client Error
            new String[]{"405", "Method\nThe method specified in the request is not allowed."}, // 4xx: Client Error
            new String[]{"406", "Not\nThe server can only generate a response that is not accepted by the client."}, // 4xx: Client Error
            new String[]{"407", "Proxy\nYou must authenticate with a proxy server before this request can be served."}, // 4xx: Client Error
            new String[]{"408", "Request\nThe request took longer than the server was prepared to wait."}, // 4xx: Client Error
            new String[]{"409", "Conflict\nThe request could not be completed because of a conflict."}, // 4xx: Client Error
            new String[]{"410", "Gone\nThe requested page is no longer available ."}, // 4xx: Client Error
            new String[]{"411", "Length\nThe \"Content-Length\" is not defined. The server will not accept the request without it ."}, // 4xx: Client Error
            new String[]{"412", "Precondition\nThe pre condition given in the request evaluated to false by the server."}, // 4xx: Client Error
            new String[]{"413", "Request\nThe server will not accept the request, because the request entity is too large."}, // 4xx: Client Error
            new String[]{"414", "Request-url\nThe server will not accept the request, because the url is too long. Occurs when you convert a \"post\" request to a \"get\" request with a long query information ."}, // 4xx: Client Error
            new String[]{"415", "Unsupported\nThe server will not accept the request, because the mediatype is not supported ."}, // 4xx: Client Error
            new String[]{"416", "Requested\nThe requested byte range is not available and is out of bounds."}, // 4xx: Client Error
            new String[]{"417", "Expectation\nThe expectation given in an Expect request-header field could not be met by this server."}, // 4xx: Client Error

            new String[]{"500", "Internal\nThe request was not completed. The server met an unexpected condition."}, // 5xx: Server Error
            new String[]{"501", "Not\nThe request was not completed. The server did not support the functionality required."}, // 5xx: Server Error
            new String[]{"502", "Bad\nThe request was not completed. The server received an invalid response from the upstream server."}, // 5xx: Server Error
            new String[]{"503", "Service\nThe request was not completed. The server is temporarily overloading or down."}, // 5xx: Server Error
            new String[]{"504", "Gateway\nThe gateway has timed out."}, // 5xx: Server Error
            new String[]{"505", "HTTP\nThe server does not support the \"http protocol\" version."}}; // 5xx: Server Error

}

