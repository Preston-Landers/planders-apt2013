package connexus.android;

/**
 * Created with IntelliJ IDEA.
 * User: Preston
 * Date: 10/4/13
 * Time: 9:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    public static final String PREFS_NAME = "Connex.us";
    public static final String PREF_AUTH_TOKEN = "authToken";
    public static final String PREF_ACCOUNT_NAME = "accountName";

    public static final String AUDIENCE = "server:client_id:771214203866-7btn313m3jrh49s1dhd837ssm1n1lrd0.apps.googleusercontent.com";
    public static final boolean DEVELOPER_MODE = false;

    // When doing the Nearby search, how long will we wait for a location fix?
    public static final int MAX_LOCATION_WAIT_SEC = 10;

    // Stuff for intents, could move somewhere else
    public static final String NAV_OFFSET = "connexus.android.NAV_OFFSET";
    public static final String NAV_LIMIT = "connexus.android.NAV_LIMIT";
    public static final String LOCATION_SEARCH = "connexus.android.LOCATION_SEARCH";
    public static final String SHOW_MY_SUBS = "connexus.android.SHOW_MY_SUBS";
    public static final String SEARCH_TERM = "connexus.android.SEARCH_TERM";
    public static final String STREAM_NAME = "connexus.android.STREAM_NAME";
    public static final String STREAM_ID = "connexus.android.STREAM_ID";
    public static final String STREAM_OWNER_ID = "connexus.android.STREAM_OWNER_ID";
    public static final String MY_ID = "connexus.android.MY_ID";
    public static final String IMAGES = "connexus.android.IMAGES";
    public static final String IMAGE_LABELS = "connexus.android.IMAGE_LABELS";
    public static final String IMAGE_POSITION = "connexus.android.IMAGE_POSITION";
    public static final String STREAM_UPLOAD_URL = "connexus.android.STREAM_UPLOAD_URL";
}
