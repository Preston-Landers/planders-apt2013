package com.appspot.cee_me.android;


/**
 * General static configuration strings and silly little utilities which should be split off into another class.
 */
public class Config {
    public static final String APPNAME = "Cee.me";
    public static final String PREFS_NAME = "Cee.me";
    public static final String PREF_ACCOUNT_NAME = "accountName";
    public static final String PREF_DEVICE_KEY = "deviceKey";

    /**
     * Used for Google APIs (Cloud Storage) calls along with the service secret key file in res/raw
     */
    public static final String SERVICE_ACCOUNT_EMAIL = "860742061992-sfafni5qmjudm67fhj5esvkna7nkrf4o@developer.gserviceaccount.com";
    // public static final String SERVICE_ACCOUNT_EMAIL = "860742061992-sfafni5qmjudm67fhj5esvkna7nkrf4o.apps.googleusercontent.com";


    /**
     * Sender ID for GCM, from G-API web console - Simple API Access - API Key
     */
    public static final String GOOGLE_PROJECT_NUMBER = "860742061992";

    /**
     * OAuth 2.0 "Web App" Client ID from G-API Console
     */
    public static final String AUDIENCE = "server:client_id:860742061992-hluiaap2cfsl9dp1io7bc3au26vks2m6.apps.googleusercontent.com";

    /**
     * The Google Cloud Storage bucket used for all uploads.
     */
    public static final String GCS_BUCKET = "ceeme_media";

    /**
     * Triggers various assertions and checks
     */
    public static final boolean DEVELOPER_MODE = false; // true;

    /**
     * Use the local App Engine dev server instead of the real one.
     * Address of server must be set in LOCAL_APP_SERVER_URL
     */
    public static final boolean LOCAL_APP_SERVER = false;

    // Would be nice to have this be automatic somehow
    // "http://10.0.2.2:8088"   // for localhost development (AVD)
    // public static final String LOCAL_APP_SERVER_URL = "http://192.168.56.1:8088"; // Genymotion
    public static final String LOCAL_APP_SERVER_URL = "http://192.168.1.99:8088"; // Local Area Network devices

    /**
     * how long will we wait for a location fix?
     */
    public static final int MAX_LOCATION_WAIT_SEC = 10;

    // Stuff for intents, could move somewhere else
    public static final String CEE_ME_ANDROID = "com.appspot.cee_me.android";

    public static final String MESSAGE_KEY = CEE_ME_ANDROID + ".MESSAGE_KEY";
    public static final String MESSAGE_TEXT = CEE_ME_ANDROID + ".MESSAGE_TEXT";
    public static final String MESSAGE_URL = CEE_ME_ANDROID + ".MESSAGE_URL";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            CEE_ME_ANDROID + "DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public static final String EXTRA_MESSAGE = "message";

}
