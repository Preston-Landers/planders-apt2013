package connexus.android.activities;

import android.content.Intent;
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
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to cameraFileUri specified in the Intent
                String fileName = cameraFileUri.getPath();
                Toast.makeText(this, "Image saved to:\n" +
                        fileName, Toast.LENGTH_LONG).show();
                setSelectedUploadUri(cameraFileUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(UploadActivity.this, "Take Photo was canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(UploadActivity.this, "Take Photo failed", Toast.LENGTH_SHORT).show();
            }
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
}

