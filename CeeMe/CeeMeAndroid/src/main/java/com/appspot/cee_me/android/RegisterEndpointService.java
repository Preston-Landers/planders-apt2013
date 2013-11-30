package com.appspot.cee_me.android;

import com.appspot.cee_me.register.Register;
import com.appspot.cee_me.register.RegisterRequest;
import com.appspot.cee_me.register.RegisterRequestInitializer;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * Endpoint service for device registration.
 */
public class RegisterEndpointService {
    public static Register getRegisterService(GoogleAccountCredential creds) {
        Register.Builder builder = new Register.Builder(
                // HttpTransport.getInstance().getHttpTransport(),
                new NetHttpTransport(),
                new AndroidJsonFactory(), creds);
        if (Config.LOCAL_APP_SERVER) {
            // Apparently gzip doesn't work in the dev server
            // possibly this https://code.google.com/p/googleappengine/issues/detail?id=9140
            builder.setGoogleClientRequestInitializer(new RegisterRequestInitializer() {
                protected void initializeRegisterRequest(RegisterRequest<?> request) {
                    request.setDisableGZipContent(true);
                }
            });
            builder.setRootUrl(Config.LOCAL_APP_SERVER_URL + "/_ah/api/"); // for localhost development
        }
        return builder.build();
    }
}


