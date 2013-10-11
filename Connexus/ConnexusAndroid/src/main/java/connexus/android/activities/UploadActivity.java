package connexus.android.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import connexus.android.Account;
import connexus.android.Config;
import connexus.android.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadActivity extends BaseActivity {
    private static final String TAG = "UploadActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    LocationManager locationManager;
    LocationListener locationListener;
    Location currentBestLocation;

    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;

    Long streamId;
    Long streamOwnerId;
    String streamName;
    String uploadFormAction;

    // Our upload location
    public Uri uploadFileUri;

    // Where we saved camera results
    public Uri cameraFileUri;


    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_upload);

        Intent intent = getIntent();
        streamName = intent.getStringExtra(Config.STREAM_NAME);
        streamId = intent.getLongExtra(Config.STREAM_ID, 0);
        streamOwnerId = intent.getLongExtra(Config.STREAM_OWNER_ID, 0);
        uploadFormAction = intent.getStringExtra(Config.STREAM_UPLOAD_URL);

        // Dear God... there must be a better way to handle this!
        if (streamId == null || streamId == 0) {
            throw new IllegalArgumentException("You must specify a streamId!");
        }
        if (streamOwnerId == null || streamOwnerId == 0) {
            throw new IllegalArgumentException("You must specify a streamOwnerId!");
        }
        if (uploadFormAction == null) {
            throw new IllegalArgumentException("You must specify an upload URL!");
        }
        if (streamName == null) {
            streamName = "(no name)";
        }

        // Clears out the selected file text label
        setSelectedUploadUri(null);

        setTitle("Upload: " + streamName);

        //upload_stream_name
        TextView streamLabel = (TextView) findViewById(R.id.upload_stream_name);
        streamLabel.setText(streamName);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get user credentials for login
        credential = Account.getInstance().getCredential();
        if (credential != null) {
            signedIn = true;
        } else {
            signedIn = false;
        }

        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        setupLocationServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationServices();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupLocationServices();
    }

    public void useCamera(View view) {
//        Intent intent = new Intent(this, CameraActivity.class);
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        // create Intent to take a picture and return control to the calling application
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraFileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }



    private void makeUseOfNewLocation(Location location) {
        if (isBetterLocation(location, currentBestLocation)) {
            currentBestLocation = location;
            // String locStr = currentBestLocation.toString();
            // Toast.makeText(UploadActivity.this, locStr, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to cameraFileUri specified in the Intent
                String fileName = cameraFileUri.getPath();
                Toast.makeText(this, "Image saved to:\n" +
                        fileName, Toast.LENGTH_LONG).show();
                setSelectedUploadUri(cameraFileUri);
                ImageView imageView = (ImageView) findViewById(R.id.uploadPreviewImageView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(cameraFileUri.getPath()));

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(UploadActivity.this, "Take Photo was canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(UploadActivity.this, "Take Photo failed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOAD_IMAGE_ACTIVITY_REQUEST_CODE) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            setSelectedUploadUri(selectedImage);

            ImageView imageView = (ImageView) findViewById(R.id.uploadPreviewImageView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    /**
     * Choose a file URI to upload.
     * @param uploadUri
     */
    private void setSelectedUploadUri(Uri uploadUri) {
        uploadFileUri = uploadUri;
        TextView fileNameLabel = (TextView) findViewById(R.id.upload_file_name);
        Button uploadButton = (Button) findViewById(R.id.upload_now);

        if (uploadUri == null) {
            fileNameLabel.setText("(No file selected.)");
            uploadButton.setEnabled(false);
        } else {
            fileNameLabel.setText(uploadUri.getPath());
            uploadButton.setEnabled(true);
        }
    }

    public void chooseFromLibrary(View view) {
        String message = "(Not Implemented Yet)";
        Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void uploadNow(View view) {
        if (uploadFileUri == null) {
            String message = "Nothing to upload...";
            Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
            return;
        }
        String message = "Uploading: " + uploadFileUri.getPath();
        Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Connex.us"); /// XXX TODO: string
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Connex.us", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void setupLocationServices() {
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
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
        //To change body of created methods use File | Settings | File Templates.
        locationManager.removeUpdates(locationListener);
    }

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
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

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}

