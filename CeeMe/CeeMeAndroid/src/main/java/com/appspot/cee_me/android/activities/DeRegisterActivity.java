package com.appspot.cee_me.android.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.RegisterEndpointService;
import com.appspot.cee_me.register.Register;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class DeRegisterActivity extends BaseActivity {
    private static final String TAG = CEEME + ".DeRegisterActivity";

    public static final int RESULT_ERROR = 42;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_deregister);
        super.onCreate(savedInstanceState);
        requireSignIn();

        deregisterDevice();

    }

    private void deregisterDevice() {
        // shortToast("Please wait for deregistration...");
        GCMRegistrar.unregister(this);
        new DeRegisterTask().execute();
    }

    private void afterServerDeregisterDevice() {
        deviceKey = "";
        setDeviceKeyPref(deviceKey);
        GCMRegistrar.setRegisteredOnServer(this, false);
        setResult(RESULT_OK);
        finish();
    }

    // effectively same as above but with the error return code
    private void afterFailedServerDeregisterDevice() {
        deviceKey = "";
        setDeviceKeyPref("");
        GCMRegistrar.setRegisteredOnServer(this, false);
        setResult(RESULT_ERROR);
        finish();
    }

    public static void unRegisterServiceCallback(Context context, String registrationId) {
        Log.i(TAG, "unRegisterServiceCallback() called");
    }

    private class DeRegisterTask extends AsyncTask<Void, Void, Void> {
        private boolean deregisterSuccess = false;
        String errMsg = getString(R.string.device_registration_failed);

        @Override
        protected Void doInBackground(Void... params) {
            deregisterSuccess = false;
            GoogleAccountCredential credential = getCredential();

            // These asserts are useless, replace with real checks
            assert(credential != null);
            assert(deviceKey != null);
            assert(!deviceKey.equals(""));

            try {
                Register service = RegisterEndpointService.getRegisterService(credential);
                Register.DeleteRegistration deleteRegistration = service.deleteRegistration(deviceKey);
                deleteRegistration.execute();
                deregisterSuccess = true;
            } catch (GoogleAuthIOException e) {
                errMsg = "Authentication error";
                Log.e(TAG, "deregister fail: " + e.getCause());
            } catch (GoogleJsonResponseException e) {
                if (e.getDetails() != null) {
                    errMsg = e.getDetails().getMessage();
                }
                else {
                    errMsg = e.toString();
                }
                Log.e(TAG, errMsg, e);
            } catch (Exception e) {
                Log.e(TAG, errMsg, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.deregister_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (deregisterSuccess) {
                afterServerDeregisterDevice();
            } else {
                longToast(errMsg);
                afterFailedServerDeregisterDevice();
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.deregister_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}

