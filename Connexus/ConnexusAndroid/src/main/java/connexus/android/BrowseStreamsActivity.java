package connexus.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.appspot.connexus_apt.streamlist.model.Stream;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import org.joda.time.DateTime;

import java.util.List;

public class BrowseStreamsActivity extends Activity {
    private static final String TAG = "BrowseStreamsActivity";
    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_streams);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get user credentials for login
        credential = Account.getInstance().getCredential();
        if (credential != null) {
            signedIn = true;
        } else {
            signedIn = false;
        }

        new BrowseStreamsTask().execute();
    }

     // figure out if we need this
    // http://developer.android.com/training/basics/firstapp/starting-activity.html
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void loginButton(View view) {
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
        }
    }

    // When you click the View Streams button
    public void ViewStreamsButton(View view) {

        Intent intent = new Intent(this, BrowseStreamsActivity.class);
        startActivity(intent);
     }


    // Combine this with the other task
    private class BrowseStreamsTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private List<Stream> streamList;
        @Override
        protected Void doInBackground(Void... params) {
            int limit = 16; // XXX TODO
            int offset = 0;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            Streamlist.Builder builder = new Streamlist.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
            service = builder.build();
            String rv = "<No result>";
            try {
                streamList = service.getStreams(limit, offset).setQuery("test").execute().getItems();

                querySuccess = true;
            } catch (GoogleAuthIOException e) {
                Log.e(TAG, "Browse Streams fail: ", e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Browse Streams failed.", e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
            DateTime now = new DateTime();
            if (querySuccess) {
                textView.setText("Server said: < " + " > at " + now);
            } else {
                textView.setText("ERROR: " + " time: " + now);
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

