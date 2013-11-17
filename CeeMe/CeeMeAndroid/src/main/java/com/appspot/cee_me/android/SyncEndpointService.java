package com.appspot.cee_me.android;

import com.appspot.cee_me.sync.Sync;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Endpoint service for message sync.
 */
public class SyncEndpointService {
    public static Sync getSyncService(GoogleAccountCredential creds) {
        Sync.Builder builder = new Sync.Builder(
                AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
        if (Config.LOCAL_APP_SERVER) {
            builder.setRootUrl(Config.LOCAL_APP_SERVER_URL + "/_ah/api/"); // for localhost development
        }
        return builder.build();
    }
}


