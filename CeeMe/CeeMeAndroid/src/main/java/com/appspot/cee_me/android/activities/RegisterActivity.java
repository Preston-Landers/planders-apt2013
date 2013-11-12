package com.appspot.cee_me.android.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.register.Register;
import com.appspot.cee_me.register.model.Device;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";


    private Register service;
    private Device device;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_register);
        super.onCreate(savedInstanceState);

        loadHardwareDescription();

//        // Get user credentials for login
//        settings = getSharedPreferences( Config.PREFS_NAME, 0);
//        credential = GoogleAccountCredential.usingAudience(this, Config.AUDIENCE);
//        setAccountName(settings.getString(Config.PREF_ACCOUNT_NAME, null));

        // new CheckMessagesTask().execute();
    }

    private void loadHardwareDescription() {
        TextView hwTextView = (TextView) findViewById(R.id.textViewRegisterHardwareDesc);
        hwTextView.setText("CrappyVision 9000");
    }

    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        private boolean registerSuccess = false;

        @Override
        protected Void doInBackground(Void... params) {
            registerSuccess = false;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            try {
                /*
                service = SyncEndpointService.getSyncService();
                Sync.GetMessages getMessages = service.getMessages(deviceKey);


                messageQuery = getMessages
                        .execute();
                */
                registerSuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "Browse Streams fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Browse Streams failed.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.browse_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (registerSuccess) {
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.browse_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

