package com.appspot.cee_me.android.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.appspot.cee_me.android.CeeMeApplication;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.RegisterEndpointService;
import com.appspot.cee_me.register.Register;
import com.appspot.cee_me.register.model.Device;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    public static final int RESULT_ERROR = 42;
    public static final String EXTRA_DEREGISTER = CEEME_PACKAGE + ".extra.deregister";

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

        Intent intent = getIntent();
        final boolean doDeregister = intent.getBooleanExtra(EXTRA_DEREGISTER, false);

        if (doDeregister) {
            deregisterDevice();
        } else {
            loadHardwareDescription();
        }

    }

    private void deregisterDevice() {
        shortToast("Please wait for deregistration...");
        new DeRegisterTask().execute();
    }

    private void afterServerDeregisterDevice() {
        GCMRegistrar.setRegisteredOnServer(this, false);
        GCMRegistrar.unregister(this);
        deviceKey = "";
        setDeviceKeyPref(deviceKey);
        setResult(RESULT_OK);
        finish();
    }

    public static void unRegisterServiceCallback(Context context, String registrationId) {
        Log.i(TAG, "unRegisterServiceCallback() called");
    }


    private void loadHardwareDescription() {
        TextView hwTextView = (TextView) findViewById(R.id.textViewRegisterHardwareDesc);
        String hardwareDescription = Config.getDeviceName();
        hwTextView.setText(hardwareDescription);

        EditText deviceNameEditText = (EditText) findViewById(R.id.registerDeviceName);
        String suggestedDeviceName = "My " + Build.MODEL;
        deviceNameEditText.setText(suggestedDeviceName);
    }

    @SuppressWarnings("UnusedParameters")
    public void cancelButton(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    @SuppressWarnings("UnusedParameters")
    public void registerButton(View view) {
        Toast.makeText(RegisterActivity.this, "Please wait.", Toast.LENGTH_SHORT);
        doRegisterWithGCM();
    }

    public static void registerServiceCallback(Context context, final String registrationId) {
        Log.i(TAG, "registerServiceCallback() called");
        Activity currentActivity = ((CeeMeApplication)context.getApplicationContext()).getCurrentActivity();
        if (currentActivity.getClass().equals(RegisterActivity.class)) {
            RegisterActivity registerActivity = (RegisterActivity) currentActivity;
            registerActivity.register(registrationId);
        }

    }

    public void register(final String registrationId) {
        runOnUiThread(new Runnable() {
            public void run() {
                gcmRegistrationId = registrationId;
                new RegisterTask().execute();
            }
        });
    }

    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        private boolean registerSuccess = false;
        String deviceKey;
        String errMsg = getString(R.string.device_registration_failed);

        @Override
        protected Void doInBackground(Void... params) {
            deviceKey = null;
            registerSuccess = false;
            GoogleAccountCredential credential = getCredential();
            assert(credential != null);
            TextView hwTextView = (TextView) findViewById(R.id.textViewRegisterHardwareDesc);
            EditText deviceNameEditText = (EditText) findViewById(R.id.registerDeviceName);
            String hwDesc = hwTextView.getText().toString();
            String deviceName = deviceNameEditText.getText().toString();
            String gcmId = gcmRegistrationId;

            try {
                Register service = RegisterEndpointService.getRegisterService(credential);
                Register.RegisterDevice registerDevice = service.registerDevice(deviceName, hwDesc, gcmId);
                Device device = registerDevice.execute();
                deviceKey = device.getDeviceKey();
                GCMRegistrar.setRegisteredOnServer(RegisterActivity.this, true);
                registerSuccess = true;
            } catch (GoogleAuthIOException e) {
                errMsg = "Authentication error";
                Log.e(TAG, "Browse Streams fail: " + e.getCause());
            } catch (GoogleJsonResponseException e) {
                errMsg = e.getDetails().getMessage();
                Log.e(TAG, errMsg, e);
            } catch (Exception e) {
                Log.e(TAG, errMsg, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (registerSuccess) {
                setDeviceKeyPref(deviceKey);
                setResult(RESULT_OK);
                finish();
            } else {
                shortToast(errMsg);
                setDeviceKeyPref("");
                setResult(RESULT_ERROR);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void doRegisterWithGCM() {
        if (gcmRegistrationId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, Config.GCM_SENDER_KEY);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                Log.i(TAG, getString(R.string.already_registered) + "\n");
                shortToast("Already registered...?");
            } else {
                new RegisterTask().execute();
            }
        }

    }

    private class DeRegisterTask extends AsyncTask<Void, Void, Void> {
        private boolean deregisterSuccess = false;
        String errMsg = getString(R.string.device_registration_failed);

        @Override
        protected Void doInBackground(Void... params) {
            deregisterSuccess = false;
            GoogleAccountCredential credential = getCredential();
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
                errMsg = e.getDetails().getMessage();
                Log.e(TAG, errMsg, e);
            } catch (Exception e) {
                Log.e(TAG, errMsg, e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (deregisterSuccess) {
                afterServerDeregisterDevice();
            } else {
                shortToast(errMsg);
                setResult(RESULT_ERROR);
                finish();
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}

