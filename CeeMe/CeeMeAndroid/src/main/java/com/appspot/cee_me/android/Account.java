package connexus.android;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * A singleton within the application to hold the Google Account credential
 */
public class Account {
    private Account() { }

    GoogleAccountCredential credential;

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        public static final Account INSTANCE = new Account();
    }

    public static Account getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void setCredential(GoogleAccountCredential credential) {
        this.credential = credential;
    }


}
