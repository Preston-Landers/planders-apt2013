package com.appspot.cee_me.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.appspot.cee_me.android.R;

/**
 * Cee.me preferences activity
 */
public class SettingsActivity extends BaseActivity {

    private final static int REQUEST_DEREGISTER_DEVICE = 1;
    public final static int RESULT_DEREGISTERED = 2;
    public final static int RESULT_DEREGISTERED_ERROR = 3;

    public final static String KEY_PREF_FORCE_APP_OPEN_CHOOSE = "pref_force_app_open_choose";
    public final static String KEY_PREF_AUTO_OPEN_INCOMING = "pref_auto_open_incoming";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_preferences);

        Button deregisterButton = (Button) findViewById(R.id.preferences_deregister_button);
        if (!isSignedIn()) {
            deregisterButton.setVisibility(View.INVISIBLE);
        }

    }

    @SuppressWarnings("UnusedParameters")
    public void deregisterButton(View view) {
        Intent intent = new Intent(this, DeRegisterActivity.class);
        startActivityForResult(intent, REQUEST_DEREGISTER_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DEREGISTER_DEVICE:
                if (resultCode == DeRegisterActivity.RESULT_CANCELED) {
                    shortToast("De-registration was canceled.");
                } else if (resultCode == DeRegisterActivity.RESULT_OK) {
                    setResult(RESULT_DEREGISTERED);
                    finish();
                } else if (resultCode == DeRegisterActivity.RESULT_ERROR) {
                    setResult(RESULT_DEREGISTERED_ERROR);
                    finish();
                }
                break;
        }
    }

}
