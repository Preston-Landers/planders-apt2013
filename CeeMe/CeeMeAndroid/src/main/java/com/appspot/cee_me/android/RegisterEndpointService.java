package com.appspot.cee_me.android;

import com.appspot.cee_me.register.Register;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

/**
 * Endpoint service for message sync.
 */
public class RegisterEndpointService {
    public static Register getRegisterService(GoogleAccountCredential creds) {
        Register.Builder builder = new Register.Builder(
                AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
        // builder.setRootUrl("http://192.168.56.1:8088/_ah/api/"); // for localhost development (genymotion)
        // builder.setRootUrl("http://10.0.2.2:8088/_ah/api/"); // for localhost development (AVD)
        return builder.build();
    }
}


