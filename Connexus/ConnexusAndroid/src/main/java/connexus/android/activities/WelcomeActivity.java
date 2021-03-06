package connexus.android.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import connexus.android.Account;
import connexus.android.Config;
import connexus.android.R;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";

    // Not sure if these are just magic or what...?
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_ACCOUNT_PICKER = 1;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_main);
        super.onCreate(savedInstanceState);

//        // Get user credentials for login
//        settings = getSharedPreferences( Config.PREFS_NAME, 0);
//        credential = GoogleAccountCredential.usingAudience(this, Config.AUDIENCE);
//        setAccountName(settings.getString(Config.PREF_ACCOUNT_NAME, null));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkGooglePlayServicesAvailable()) {
            Log.e(TAG, "Can't get account from Google Play!.");
            // Maybe quit at this point? dialog to user? hide the login button?
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        setAccountName(accountName);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(Config.PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        onSignIn();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(connexus.android.R.menu.main, menu);
        return true;
    }

    // When you click the Sign in with Google button
    public void loginButton(View view) {
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
        }
        signIn(view);
    }

    // When you click the View Streams button
    public void ViewStreamsButton(View view) {

        Intent intent = new Intent(this, BrowseStreamsActivity.class);
        startActivity(intent);

     }


    /**
     * Handles logic for clicking the sign in button.
     * From the tictactoe sample app https://github.com/GoogleCloudPlatform/appengine-endpoints-tictactoe-android/blob/master/src/com/google/devrel/samples/ttt/TictactoeActivity.java
     *
     * @param v current view within the application, for rendering updates
     */
    public void signIn(View v) {
        if (!this.signedIn) {
            chooseAccount();
        } else {
            forgetAccount();
            setSignInEnablement(true);
            // setBoardEnablement(false);
            setAccountLabel("(not signed in)");
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    @Override
    protected void onSignIn() {
        super.onSignIn();
        setSignInEnablement(false);
        setAccountLabel(this.accountName);
    }

    private void forgetAccount() {
        setAccountName(null);
        SharedPreferences.Editor editor2 = settings.edit();
        editor2.remove(Config.PREF_AUTH_TOKEN);
        editor2.commit();
    }

    private void setSignInEnablement(boolean state) {
        Button button = (Button) findViewById(R.id.login_button);
        // TextView textView = (TextView) findViewById(R.id.welcome_status_textview);

        if (state) {
            // textView.setText(" Signed out. ");
            button.setText("Sign In");
        } else {
            // textView.setText(" Signed in. ");
            button.setText("Sign Out");
        }
    }

    private void setAccountLabel(String label) {
        TextView userLabel = (TextView) findViewById(R.id.tv_current_account);
        userLabel.setText("Signed in as: " + label);
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    /**
     * Called if the device does not have Google Play Services installed.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                            connectionStatusCode, WelcomeActivity.this, REQUEST_GOOGLE_PLAY_SERVICES);
                    dialog.show();
                } catch (Exception e) {
                    Log.e(TAG, "Unable to obtain Google Play!.", e);
                }

            }
        });
    }

}

