package connexus.android.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import connexus.android.Account;
import connexus.android.Config;
import connexus.android.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadActivity extends BaseActivity {
    private static final String TAG = "UploadActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int LOAD_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    Long streamId;
    Long streamOwnerId;
    Long myId;
    String streamName;
    String uploadFormAction;

    // Our upload location
    private Uri uploadFileUri;

    // Where we saved camera results
    public Uri cameraFileUri;

    // For the Preview
    protected ImageLoader imageLoader = ImageLoader.getInstance();

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
        myId = intent.getLongExtra(Config.MY_ID, 0);

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
        if (myId == null) {
            throw new IllegalArgumentException("You must specify your own user ID!");
        }
        if (streamName == null) {
            streamName = "(no name)";
        }

        if (uploadFormAction.toLowerCase().contains("/monolith:")) {
            uploadFormAction = uploadFormAction.replaceAll("(?i)[/]monolith[:]", "/192.168.56.1:");
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
        /// XX TODO: don't think we need here
        credential = Account.getInstance().getCredential();
        if (credential != null) {
            signedIn = true;
        } else {
            signedIn = false;
        }

        useLocation = true;
        startLocationServices();
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to cameraFileUri specified in the Intent
                String fileName = cameraFileUri.getPath();
//                Toast.makeText(this, "Image saved to:\n" +
//                        fileName, Toast.LENGTH_SHORT).show();
                setSelectedUploadUri(cameraFileUri);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(UploadActivity.this, "Take Photo was canceled", Toast.LENGTH_SHORT).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(UploadActivity.this, "Take Photo failed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOAD_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                setSelectedUploadUri(selectedImage);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(UploadActivity.this, "Load from Gallery was canceled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UploadActivity.this, "Load from Gallery failed", Toast.LENGTH_SHORT).show();
            }

        }
    }


    /**
     * Choose a file URI to upload.
     *
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

        setPreviewImage(uploadUri);

    }

    /**
     * After selecting an image for upload, load it in the preview area.
     * Uses the UIL library to load a smaller version of the image (fitting in the preview area)
     * since a camera image can be quite large.
     * @param imageUri
     */
    private void setPreviewImage(Uri imageUri) {
        final ImageView imageView = (ImageView) findViewById(R.id.uploadPreviewImageView);
        if (imageView == null) {
            return;
        }
        String imagePath = getPath(imageUri, true);
        ImageSize targetSize = new ImageSize(imageView.getWidth(), imageView.getHeight());
        imageLoader.loadImage(imagePath, targetSize, getDisplayOptions(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
            }
        });
    }

    /**
     * Start an intent to pick an image from the device image gallery.
     * @param view
     */
    public void chooseFromLibrary(View view) {
        Intent intent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, LOAD_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * This is run when you click the upload button.
     * @param view
     */
    public void uploadNow(View view) {
        if (uploadFileUri == null) {
            String message = "Nothing to upload...";
            Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
            return;
        }
        String message = "Uploading: " + uploadFileUri.getPath();
        Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();

        new UploadFileTask().execute();
    }

    private void uploadIsComplete() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void uploadFailed() {
        String message = "Upload failed, sorry!";
        try {
            Toast.makeText(UploadActivity.this, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "Can't Toast for some reason", e);
        }
        this.finish();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Connex.us"); /// XXX TODO: string
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Connex.us", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    private class UploadFileTask extends AsyncTask<Void, Void, Void> {
        private boolean uploadSucceeded = false;

        @Override
        protected Void doInBackground(Void... params) {
            EditText uploadComment = (EditText) findViewById(R.id.upload_comment);
            String comments = uploadComment.getText().toString();
            double latitude = 0.0;
            double longitude = 0.0;
            if (currentBestLocation != null) {
                latitude = currentBestLocation.getLatitude();
                longitude = currentBestLocation.getLongitude();
            }
            String streamUploadId = streamOwnerId + ":" + streamId;

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uploadFormAction);
            MultipartEntity httpEntity = new MultipartEntity();
            File uploadFile = new File(getPath(uploadFileUri, false));

            try {
                ContentBody contentBody = new FileBody(uploadFile);
                httpEntity.addPart("media", contentBody);
                httpEntity.addPart("v", new StringBody(streamUploadId));
                httpEntity.addPart("uu", new StringBody(myId.toString()));
                httpEntity.addPart("upload", new StringBody("1"));
                httpEntity.addPart("comments", new StringBody(comments));
                httpEntity.addPart("latitude", new StringBody(String.format("%f", latitude)));
                httpEntity.addPart("longitude", new StringBody(String.format("%f", longitude)));
                httpPost.setEntity(httpEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                uploadSucceeded = true;
                return null;

            } catch (Exception e) {
                Log.e(TAG, "Unable to upload", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.upload_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (uploadSucceeded) {
                uploadIsComplete();
            } else {
                uploadFailed();
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.upload_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Turn a content:// URI into a file:// URI
     * @param _uri a content:// URI
     * @param addFileProto if true, add the file:// protocol to the beginning
     * @return a file:// URI
     */
    public String getPath(Uri _uri, boolean addFileProto) {
        if (_uri == null) {
            return null;
        }
        String filePath = "";
        if (_uri != null && "content".equals(_uri.getScheme())) {
            Cursor cursor = this.getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
            cursor.moveToFirst();
            filePath = cursor.getString(0);
            cursor.close();
        }
        else {
            filePath = _uri.getPath();
        }
        if (addFileProto) {
            return "file://" + filePath;
        } else {
            return filePath;
        }
    }
}

