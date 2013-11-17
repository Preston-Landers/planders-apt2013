package com.appspot.cee_me.android;

import com.appspot.cee_me.register.Register;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Endpoint service for device registration.
 */
public class RegisterEndpointService {
    public static Register getRegisterService(GoogleAccountCredential creds) {
        Register.Builder builder = new Register.Builder(
                AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
        if (Config.LOCAL_APP_SERVER) {
            builder.setRootUrl(Config.LOCAL_APP_SERVER_URL + "/_ah/api/"); // for localhost development
        }
        return builder.build();
    }
}


