package com.appspot.cee_me.android.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.widget.Toast;
import com.appspot.cee_me.android.Account;
import com.appspot.cee_me.android.CeeMeApplication;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.google.android.gcm.GCMRegistrar;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;


/**
 * Base activity for all activities. Has code for location and account services.
 * <p/>
 * Handles location services - disabled by default, set useLocation in subclass onCreate.
 */
public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    public static final String CEEME_PACKAGE = "com.appspot.cee_me.android";
    protected CeeMeApplication mMyApp;

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentBestLocation;
    protected boolean useLocation = false;

    protected SharedPreferences settings;
    protected String accountName;
    private GoogleAccountCredential credential;
    protected boolean signedIn = false;
    protected String deviceKey = "";
    protected String gcmRegistrationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (CeeMeApplication)this.getApplicationContext();

        // Get user credentials for login
        credential = GoogleAccountCredential.usingAudience(this, Config.AUDIENCE);
        settings = getSharedPreferences(Config.PREFS_NAME, 0);
        setAccountName(settings.getString(Config.PREF_ACCOUNT_NAME, null));

        // Check for GCM (cloud notifications) device registration ID
        // will return empty string if not registered yet
        gcmRegistrationId = getGCMRegistration();
    }

    protected void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        credential.setSelectedAccountName(accountName);
        Account.getInstance().setCredential(credential);
        this.accountName = accountName;
        if (accountName != null) {
            this.signedIn = true;
            onSignIn();
        } else {
            signOutExceptAccount();
        }
    }

    /**
     * subclass can override to take action upon gaining account credentials
     * but should call superclass version
     */
    protected void onSignIn() {
        loadDeviceKey();
    }

    protected void signOutExceptAccount() {
        signedIn = false;
        deviceKey = "";
    }

    protected void signOut() {
        // signOutExceptAccount();
        setAccountName(null);
    }

    protected boolean isSignedIn() {
        return signedIn;
    }

    protected void requireSignIn() {
        if (signedIn) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
                builder.setMessage(R.string.dialog_signin_required)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    protected boolean isDeviceRegistered() {
        return deviceKey != null && !deviceKey.equals("");
    }

    /**
     * @return Returns the current Google Account credential object.
     */
    protected GoogleAccountCredential getCredential() {
        // Uses the singleton instead of our private instance, in case 'somewhere outside' changes the singleton
        return Account.getInstance().getCredential();
    }

    /**
     * Loads the GCM registration ID. If the device is not currently registered,
     * this will return an empty string.
     */
    private String getGCMRegistration() {
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        if (Config.DEVELOPER_MODE) {
            GCMRegistrar.checkManifest(this);
        }
        return GCMRegistrar.getRegistrationId(this);
    }

    /**
     * Ensures correct "Back" navigation on action bar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean rv = super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return rv;
        }
        return rv;
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
        stopLocationServices();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
            mMyApp.setCurrentActivity(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationServices();
        mMyApp.setCurrentActivity(this);
    }

    protected void loadDeviceKey() {
        deviceKey = null;
        if (credential != null) {
            deviceKey = getDeviceKeyPref();
        }
    }

    private void makeUseOfNewLocation(Location location) {
        if (isBetterLocation(location, currentBestLocation)) {
            currentBestLocation = location;
            // String locStr = currentBestLocation.toString();
            // Toast.makeText(UploadActivity.this, locStr, Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationServices() {
        if (!useLocation) {
            return;
        }
        if (locationManager == null) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        // CAN DO BOTH
        long minTimeMS = 500; // don't need frequent updates
        float minDistanceMeters = 0; // can update w/o moving

        String locationProvider = LocationManager.GPS_PROVIDER; // LocationManager.NETWORK_PROVIDER
        // locationManager.requestLocationUpdates(locationProvider, minTimeMS, minDistanceMeters, locationListener);
        locationManager.requestLocationUpdates(locationProvider, minTimeMS, minDistanceMeters, locationListener);
        currentBestLocation = locationManager.getLastKnownLocation(locationProvider);
    }

    private void stopLocationServices() {
        if (!useLocation || locationListener == null || locationManager == null) {
            return;
        }
        //To change body of created methods use File | Settings | File Templates.
        locationManager.removeUpdates(locationListener);
    }

    public Location waitForLocation() {
        int maxSleepCycle = Config.MAX_LOCATION_WAIT_SEC;
        int i = 0;
        while (true) {
            if (currentBestLocation != null) {
                return currentBestLocation;
            }
            if (i >= maxSleepCycle) {
                return null;
            }
            SystemClock.sleep(1000);
            i++;
        }
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public void shortToast(String msg) {
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    String getDeviceKeyPrefName() {
        return Config.PREF_DEVICE_KEY + accountName;
    }

    void setDeviceKeyPref(String keyStr) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(getDeviceKeyPrefName(), keyStr);
        editor.commit();
    }

    String getDeviceKeyPref() {
        return settings.getString(getDeviceKeyPrefName(), null);
    }

}
