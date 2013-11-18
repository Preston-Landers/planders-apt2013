package com.appspot.cee_me.android;


import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * General static configuration strings and silly little utilities which should be split off into another class.
 */
public class Config {
    public static final String PREFS_NAME = "Cee.me";
    public static final String PREF_AUTH_TOKEN = "authToken";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String PREF_DEVICE_KEY = "deviceKey";

    /**
     * Sender ID for GCM, from G-API web console
     */
    public static final String GOOGLE_PROJECT_ID = "860742061992";

    /**
     * OAuth 2.0 "Web App" Client ID from G-API Console
     */
    public static final String AUDIENCE = "server:client_id:860742061992-hluiaap2cfsl9dp1io7bc3au26vks2m6.apps.googleusercontent.com";

    /**
     * Triggers various assertions and checks
     */
    public static final boolean DEVELOPER_MODE = true;

    /**
     * Use the local App Engine dev server instead of the real one.
     * Address of server must be set in LOCAL_APP_SERVER_URL
     */
    public static final boolean LOCAL_APP_SERVER = true;

    // "http://10.0.2.2:8088"   // for localhost development (AVD)
    // public static final String LOCAL_APP_SERVER_URL = "http://192.168.56.1:8088"; // Genymotion
    public static final String LOCAL_APP_SERVER_URL = "http://192.168.1.99:8088"; // Local Area Network devices

    /**
     * how long will we wait for a location fix?
     */
    public static final int MAX_LOCATION_WAIT_SEC = 10;

    // Stuff for intents, could move somewhere else
    public static final String CEE_ME_ANDROID = "com.appspot.cee_me.android";
    public static final String NAV_OFFSET = CEE_ME_ANDROID + ".NAV_OFFSET";
    public static final String NAV_LIMIT = CEE_ME_ANDROID + ".NAV_LIMIT";
    public static final String LOCATION_SEARCH = CEE_ME_ANDROID + ".LOCATION_SEARCH";
    public static final String SHOW_MY_SUBS = CEE_ME_ANDROID + ".SHOW_MY_SUBS";
    public static final String SEARCH_TERM = CEE_ME_ANDROID + ".SEARCH_TERM";
    public static final String STREAM_NAME = CEE_ME_ANDROID + ".STREAM_NAME";
    public static final String STREAM_ID = CEE_ME_ANDROID + ".STREAM_ID";
    public static final String STREAM_OWNER_ID = CEE_ME_ANDROID + ".STREAM_OWNER_ID";
    public static final String MY_ID = CEE_ME_ANDROID + ".MY_ID";
    public static final String IMAGES = CEE_ME_ANDROID + ".IMAGES";
    public static final String IMAGE_LABELS = CEE_ME_ANDROID + ".IMAGE_LABELS";
    public static final String IMAGE_POSITION = CEE_ME_ANDROID + ".IMAGE_POSITION";
    public static final String STREAM_UPLOAD_URL = CEE_ME_ANDROID + ".STREAM_UPLOAD_URL";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            CEE_ME_ANDROID + "DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

    // Extras for activity params/results
    public static final String EXTRAS_REGISTRATION_SUCCESS = CEE_ME_ANDROID + ".STREAM_UPLOAD_URL";

    /**
     * Returns a descriptive device name, e.g. Samsung GT-S5830L
     * From: http://stackoverflow.com/a/12707479/858289
     *
     * @return device description string
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    /**
     * Capitalize a string
     *
     * @param s string to capitalize
     * @return capitalized string
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
