package com.appspot.cee_me.android.activities;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class WelcomeActivity extends BaseActivity {
    private static final String TAG = "WelcomeActivity";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_ACCOUNT_PICKER = 1;
    static final int REQUEST_REGISTER_DEVICE = 2;

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

        registerReceiver(mHandleMessageReceiver,
                new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));

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
                        // onSignIn();
                    }
                }
                break;
            case REQUEST_REGISTER_DEVICE:
                if (resultCode == RegisterActivity.RESULT_CANCELED) {
                    Toast.makeText(WelcomeActivity.this, "Registration canceled.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WelcomeActivity.this, "Registration is required to use this app.", Toast.LENGTH_SHORT).show();
                    signOut();
                } else if (resultCode == RegisterActivity.RESULT_OK) {
                    Toast.makeText(WelcomeActivity.this, "Thanks for registering!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(WelcomeActivity.this, "Press Check Messages now.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RegisterActivity.RESULT_ERROR) {
                    Toast.makeText(WelcomeActivity.this, "Error during registration.", Toast.LENGTH_SHORT).show();
                    signOut();
                }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.appspot.cee_me.android.R.menu.main, menu);
        return true;
    }

    // When you click the Sign in with Google button
    public void loginButton(View view) {
        GoogleAccountCredential credential = getCredential();
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
        }
        signIn(view);
    }

    public void settingsButton(View view) {
        // Toast.makeText(WelcomeActivity.this, "Settings not implemented yet.", Toast.LENGTH_SHORT).show();

        // For now, this will sign out and forget any device registration.
        deregisterDevice();
        signOut();
        shortToast("De-registered device!");
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
            signOut();
        }
    }

    private void chooseAccount() {
        startActivityForResult(getCredential().newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void signOut() {
        super.signOut();

        // SharedPreferences.Editor editor2 = settings.edit();
        // editor2.remove(Config.PREF_AUTH_TOKEN);
        // editor2.commit();

        setSignInEnablement(true);
        setAccountLabel(getString(R.string.not_signed_in));
    }


    @Override
    protected void
    onSignIn() {
        super.onSignIn();
        setSignInEnablement(false);
        setAccountLabel(this.accountName);
        if (isSignedIn() && !isDeviceRegistered()) {
            // handle no device key
            promptForRegistration();
        }
    }


    private void setSignInEnablement(boolean state) {
        Button buttonLogin = (Button) findViewById(R.id.button_login);
        // TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
        Button buttonCheckMessages = (Button) findViewById(R.id.button_check_messages);

        if (state) {
            // textView.setText(" Signed out. ");
            buttonLogin.setText("Sign In");
            buttonCheckMessages.setVisibility(View.INVISIBLE);
        } else {
            // textView.setText(" Signed in. ");
            buttonLogin.setText("Sign Out");
            buttonCheckMessages.setVisibility(View.VISIBLE);
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

    protected void promptForRegistration() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                builder.setMessage(R.string.dialog_goto_registration)
                        .setPositiveButton(R.string.do_go_registration, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                doRegistration();
                            }
                        })
                        .setNegativeButton(R.string.do_cancel_registration, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                signOut();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    protected void doRegistration() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_REGISTER_DEVICE);
    }

    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
                    shortToast(newMessage + "\n");
                }
            };

}

