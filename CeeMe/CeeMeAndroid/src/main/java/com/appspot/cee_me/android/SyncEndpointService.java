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
        // builder.setRootUrl("http://192.168.56.1:8088/_ah/api/"); // for localhost development (genymotion)
        // builder.setRootUrl("http://10.0.2.2:8088/_ah/api/"); // for localhost development (AVD)
        Sync service = builder.build();
        return service;
    }
}


