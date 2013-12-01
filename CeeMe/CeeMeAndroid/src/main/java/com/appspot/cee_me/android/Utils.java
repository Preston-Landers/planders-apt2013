package com.appspot.cee_me.android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * General utilities and misc. code...
 */
public class Utils {
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
        Intent intent = new Intent(Config.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(Config.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
