package com.appspot.cee_me.android.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.MessageQuery;
import com.appspot.cee_me.android.SyncEndpointService;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;

public class CheckMessagesActivity extends BaseActivity {
    private static final String TAG = CEEME + ".CheckMessagesActivity";

    // query parameters for the server
    private final static int defaultQueryLimit = 9;
    private int queryLimit = defaultQueryLimit;
    private int queryOffset = 0;

    private Sync service;
    private MessageQuery messageQuery;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_check_messages);
        super.onCreate(savedInstanceState);

//        // Get user credentials for login
//        settings = getSharedPreferences( Config.PREFS_NAME, 0);
//        credential = GoogleAccountCredential.usingAudience(this, Config.AUDIENCE);
//        setAccountName(settings.getString(Config.PREF_ACCOUNT_NAME, null));

        new CheckMessagesTask().execute();
    }


    private class CheckMessagesTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            int limit = queryLimit;
            int offset = queryOffset;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = getCredential();
            }
            try {
                /*
                service = SyncEndpointService.getSyncService();
                Sync.GetMessages getMessages = service.getMessages(deviceKey);


                messageQuery = getMessages
                        .execute();
                */
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "Browse Streams fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Browse Streams failed.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            // TODO: wrong progress
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.button_check_messages);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (messageQuery != null) {
                    // loadImages(messageQuery);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO: wrong progress
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.button_check_messages);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

