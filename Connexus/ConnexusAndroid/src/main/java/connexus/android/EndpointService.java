package connexus.android;

import com.appspot.connexus_apt.streamlist.Streamlist;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class EndpointService {
    public static Streamlist getStreamlistService(GoogleAccountCredential creds) {
        Streamlist.Builder builder = new Streamlist.Builder(
                AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
        // builder.setRootUrl("http://192.168.56.1:8088/_ah/api/"); // for localhost development
        Streamlist service = builder.build();
        return service;
    }
}
