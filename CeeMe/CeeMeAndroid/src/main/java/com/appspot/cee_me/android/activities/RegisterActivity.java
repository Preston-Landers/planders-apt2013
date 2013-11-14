package com.appspot.cee_me.android.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.RegisterEndpointService;
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
        requireSignIn();

        loadHardwareDescription();

    }

    private void loadHardwareDescription() {
        TextView hwTextView = (TextView) findViewById(R.id.textViewRegisterHardwareDesc);
        String hardwareDescription = Config.getDeviceName();
        hwTextView.setText(hardwareDescription);

        EditText deviceNameEditText = (EditText) findViewById(R.id.registerDeviceName);
        String suggestedDeviceName = "My " + Build.MODEL;
        deviceNameEditText.setText(suggestedDeviceName);
    }

    public void cancelButton(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void registerButton(View view) {
        Toast.makeText(RegisterActivity.this, "Please wait.", Toast.LENGTH_SHORT);
        setResult(RESULT_OK);
        this.finish();
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
                // service = RegisterEndpointService.getRegisterService();
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

