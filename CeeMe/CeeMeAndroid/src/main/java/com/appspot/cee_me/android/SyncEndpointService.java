package com.appspot.cee_me.android;

import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.SyncRequest;
import com.appspot.cee_me.sync.SyncRequestInitializer;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Endpoint service for message sync.
 */
public class SyncEndpointService {
    public static Sync getSyncService(GoogleAccountCredential creds) {
        Sync.Builder builder = new Sync.Builder(
                HttpTransport.getInstance().getHttpTransport(),
                new AndroidJsonFactory(), creds);
        if (Config.LOCAL_APP_SERVER) {
            // Apparently gzip doesn't work in the dev server
            // possibly this https://code.google.com/p/googleappengine/issues/detail?id=9140
            builder.setGoogleClientRequestInitializer(new SyncRequestInitializer() {
                protected void initializeSyncRequest(SyncRequest<?> request) {
                    request.setDisableGZipContent(true);
                }
            });

            builder.setRootUrl(Config.LOCAL_APP_SERVER_URL + "/_ah/api/"); // for localhost development
        }
        return builder.build();
    }
}


