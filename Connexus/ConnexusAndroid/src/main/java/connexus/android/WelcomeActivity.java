package connexus.android;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import org.joda.time.DateTime;

import com.appspot.connexus_apt.helloworld.Helloworld;

public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";
    Helloworld service;
    SharedPreferences settings;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user credentials for login
        settings = getSharedPreferences( Config.PREFS_NAME, 0);
        credential = GoogleAccountCredential.usingAudience(this, Config.AUDIENCE);
        setAccountName(settings.getString(Config.PREF_ACCOUNT_NAME, null));
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
        // TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
            // new ViewStreamsTask().execute();
        } else {
        }
        signIn(view);
    }

    // When you click the View Streams button
    public void ViewStreamsButton(View view) {
        TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
        DateTime now = new DateTime();
        textView.setText("starting task at: " + now);

        new ViewStreamsTask().execute();
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

    private void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        this.accountName = accountName;
        if (accountName != null) {
            this.signedIn = true;
            setAccountLabel(accountName);
            setSignInEnablement(false);
        }

    }

    private void onSignIn() {
        this.signedIn = true;
        // this.waitingForMove = true;
        setSignInEnablement(false);
        // setBoardEnablement(true);
        setAccountLabel(this.accountName);
        // queryScores();
        // new ViewStreamsTask().execute();
    }

    private void forgetAccount() {
        this.signedIn = false;
        SharedPreferences.Editor editor2 = settings.edit();
        editor2.remove(Config.PREF_AUTH_TOKEN);
        editor2.commit();
    }

    private void setSignInEnablement(boolean state) {
        Button button = (Button) findViewById(R.id.login_button);
        TextView textView = (TextView) findViewById(R.id.welcome_status_textview);

        if (state) {
            textView.setText(" Signed out. ");
            button.setText("Sign In");
        } else {
            textView.setText(" Signed in. ");
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

    // Combine this with the other task
    private class ViewStreamsTask extends AsyncTask<Void, Void, String> {
        private boolean loginSuccess = false;
        @Override
        protected String doInBackground(Void... params) {
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            Helloworld.Builder builder = new Helloworld.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
            service = builder.build();
            String rv = "<No result>";
            try {
                if (signedIn) {
                    rv = service.greetings().authed().execute().getMessage();
                } else {
                    rv = service.greetings().getGreeting(0).execute().getMessage();
                }

                loginSuccess = true;
            } catch (GoogleAuthIOException e) {
                Log.e(TAG, "View Streams fail: ", e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "View Streams failed.", e);
            }
            return rv;
        }
        @Override
        protected void onPostExecute(String result) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
            DateTime now = new DateTime();
            if (loginSuccess) {
                textView.setText("Server said: < " + result + " > at " + now);
            } else {
                textView.setText("ERROR: " + result + " time: " + now);
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}

