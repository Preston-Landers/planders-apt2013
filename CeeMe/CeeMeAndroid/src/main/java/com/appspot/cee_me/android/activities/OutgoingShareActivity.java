package com.appspot.cee_me.android.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.cee_me.android.*;
import com.appspot.cee_me.register.model.Device;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.Media;
import com.appspot.cee_me.sync.model.Message;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.JsonFactory;

import java.io.IOException;
import java.util.UUID;

public class OutgoingShareActivity extends BaseActivity {
    private static final String TAG = CEEME + ".OutgoingShareActivity";

    public static final int REQUEST_AUTHORIZE_API = 19;

    private static final int REQUEST_DIRECTORY_LOOKUP = 23;

    private String toDeviceKey;
    private Sync service;
    private Message message;

    private GoogleAccountCredential gapiCredential; // for Cloud Storage calls
    private boolean usingMediaAttachment = false;
    private Uri mediaUri = null;
    private ContentResolver contentResolver;


    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_outgoing_share);
        super.onCreate(savedInstanceState);
        requireSignIn();
        contentResolver = this.getContentResolver();


        Intent intent = getIntent();

        String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_SEND) && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String textData = intent.getStringExtra(Intent.EXTRA_TEXT);
            setMessageURL(textData);
        }

        ClipData clipData = intent.getClipData();
        if (clipData != null) {
            ClipData.Item item = clipData.getItemAt(0);

            mediaUri = item.getUri();
            usingMediaAttachment = true;
            String mimeType = FileUtils.getMimeType(mediaUri, contentResolver);
            String filePath = FileUtils.getPath(mediaUri, contentResolver, true);

            Log.i(TAG, "Got uri: " + mediaUri + "\nmimeType: " + mimeType + "\n" + filePath + "\n");
            setMessageURL(filePath);
        }

        setStatusText("");
        setReceiverIdentity("<Select recipient...>");
        // setMessageText(messageText);

        // getCloudStorageCredential();
        getAndUseAuthTokenInAsyncTask();

    }

    void getAndUseAuthTokenInAsyncTask() {
        new LoadGoogleAPIAuthorizationTask().execute();
    }

    private void getCloudStorageCredential() {

        // Test our Google API credential - we may require user authorization
        gapiCredential = getGoogleAPICredential(CloudStorage.getStorageScopes());
        try {
            String token = gapiCredential.getToken();
            Log.i(TAG, "Got API token: " + token);
        } catch (GooglePlayServicesAvailabilityException playEx) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                    playEx.getConnectionStatusCode(),
                    this,
                    REQUEST_AUTHORIZE_API);
            // Use the dialog to present to the user.
            dialog.show();
        } catch (UserRecoverableAuthException recoverableException) {
            Intent recoveryIntent = recoverableException.getIntent();
            // Use the intent in a custom dialog or just startActivityForResult.
            startActivityForResult(recoveryIntent, REQUEST_AUTHORIZE_API);
        } catch (IOException e) {
            // The next two exceptions are likely non-recoverable...
            Log.e(TAG, "Failed to get Google API credential.", e);
            gapiCredential = null;
        } catch (GoogleAuthException e) {
            Log.e(TAG, "Failed to get Google API credential.", e);
            gapiCredential = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_DIRECTORY_LOOKUP:
                if (resultCode == DirectoryActivity.RESULT_CANCELED) {
                    shortToast("Directory lookup canceled.");
                } else if (resultCode == DirectoryActivity.RESULT_OK) {
                    handleDirectoryLookup(data);
                } else if (resultCode == DirectoryActivity.RESULT_ERROR) {
                    shortToast("Error during directory lookup.");
                }
                break;
            case REQUEST_AUTHORIZE_API:
                if (resultCode == RESULT_OK) {
                    getAndUseAuthTokenInAsyncTask();
                }
        }
    }

    private void handleDirectoryLookup(Intent data) {
        String deviceStr = data.getStringExtra(DirectoryActivity.EXTRA_DEVICE_JSON);
        // Log.i(TAG, "Got device from directory: " + deviceStr);

        // Reconstitute the device
//        Gson gson = new Gson();
//        Device device = gson.fromJson(deviceStr, Device.class);
        Device device;
        try {
            JsonFactory factory = new AndroidJsonFactory();
            device = factory.fromString(deviceStr, Device.class);
            Log.i(TAG, "Got device from directory: " + device);
        } catch (Exception e) {
            Log.e(TAG, "Unable to deserialize device from lookup: " + deviceStr, e);
            return;
        }

        String deviceDesc = device.getOwnerAccountName() + ": " + device.getPublicId() + " " + device.getName();
        setText(deviceDesc, R.id.outgoingShare_to_tv);
        toDeviceKey = device.getDeviceKey();
    }

    private void logSentMessageAndFinish(Message message) {
        Log.i(TAG, "Successfully sent message: " + message);
        setResult(RESULT_OK);
        finish();
    }

    private void setMessageURL(String txt) {
        setText(txt, R.id.outgoingShare_url_editText);
    }

    private String getMessageURL() {
        TextView textView = (TextView) findViewById(R.id.outgoingShare_url_editText);
        return textView.getText().toString();
    }

    private String getMessageText() {
        TextView textView = (TextView) findViewById(R.id.outgoingShare_message_editText);
        return textView.getText().toString();
    }


    private void setStatusText(String txt) {
        setText(txt, R.id.outgoingShare_status_textview);
    }

    private void setReceiverIdentity(String txt) {
        setText(txt, R.id.outgoingShare_to_tv);
    }

    private void setText(String txt, int viewId) {
        if (txt == null) {
            txt = "";
        }
        TextView streamLabel = (TextView) findViewById(viewId);
        streamLabel.setText(txt);
    }

    public void chooseMessageRecipient(View view) {
        Intent intent = new Intent(this, DirectoryActivity.class);
        startActivityForResult(intent, REQUEST_DIRECTORY_LOOKUP);
    }

    public void sendShareNow(View view) {
        // Validate that we can send the message.
        if (toDeviceKey == null || toDeviceKey.equals("")) {
            shortToast("You must select a recipient first.");
            return;
        }
        new SendMessageTask().execute();
    }

    public void cancelOutgoingShare(View view) {
        shortToast("Ignoring this message.");
        setResult(RESULT_CANCELED);
        finish();
    }

    private void openExternalURL(String theURL) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(theURL));
        startActivity(i);
    }

    private class LoadGoogleAPIAuthorizationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getCloudStorageCredential();
            return null;
        }
        @Override
        protected void onPostExecute(Void rv) {
            return;
        }
    }

    private class SendMessageTask extends AsyncTask<Void, ProgressParams, Void> {
        private boolean querySuccess = false;
        private String mimeType;
        private String filePath;
        private Long fileSize;
        private String GCSFilename;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            String msgText = getMessageText();
            String urlData = getMessageURL();
            Media media = null;
            try {
                if (usingMediaAttachment) {
                    media = uploadMedia();
                    if (media == null) {
                        throw new IOException("Unable to upload file attachment.");
                    }
                }
                service = SyncEndpointService.getSyncService(getCredential());
                Sync.SendMessage sendMessage = service.sendMessage(deviceKey, toDeviceKey);
                sendMessage.setText(msgText);
                if (usingMediaAttachment && media != null) {
                    sendMessage.setMediaKey(media.getMediaKey());
                } else if (!usingMediaAttachment && urlData != null && !urlData.equals("")) {
                    // urlData is a useless local file URI if we have an upload attachment
                    // so only set this param if we actually shared a URL
                    sendMessage.setUrlData(urlData);
                }
                message = sendMessage.execute();
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "message send fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "message send failed: ", e);
            }
            return null;
        }

        /**
         * Upload the file to Google Cloud Storage, then create a Media record on the Cee.me server and return it.
         *
         * @return new Media record
         */
        private Media uploadMedia() {
            mimeType = FileUtils.getMimeType(mediaUri, contentResolver);
            filePath = FileUtils.getPath(mediaUri, contentResolver, false);
            fileSize = FileUtils.getFileSize(mediaUri, contentResolver);
            GCSFilename = getNewGCSFilename(deviceKey, filePath);
            String logDesc = filePath + " - gcs filename: " + GCSFilename;

            // Upload the content to the app's GCS bucket.
            try {
                uploadFileToGCS();
            } catch (IOException e) {
                Log.e(TAG, "Unable to upload file to GCS: " + logDesc, e);
                return null;
            }

            // Create the media record on Cee.me app server and return it.
            return createMediaRecord(GCSFilename);
        }

        private void uploadFileToGCS() throws IOException {
            // Create a progress updater.
            IOProgress ioProgress = new IOProgress() {
                @Override
                public void setProgress(int progress, long bytesTransferred, long totalBytes) {
                    publishProgress(new ProgressParams(progress, bytesTransferred, totalBytes));
                }

                @Override
                public void started() {
                }

                @Override
                public void initiationCompleted() {
                }

                @Override
                public void completed() {
                }
            };
            if (gapiCredential == null) {
                throw new IllegalArgumentException("Could not get Google Cloud Storage authorization");
            }
            CloudStorage cloudStorage = new CloudStorage(gapiCredential);
            // CloudStorage cloudStorage = new CloudStorage(getCredential());
            cloudStorage.uploadFile(Config.GCS_BUCKET, filePath, GCSFilename, ioProgress);

        }

        private Media createMediaRecord(String GCSFilename) {

            service = SyncEndpointService.getSyncService(getCredential());
            try {
                Sync.CreateMedia createMedia = service.createMedia(
                        GCSFilename,
                        FileUtils.getBaseFilenameFromPath(filePath),
                        mimeType,
                        fileSize
                );
                return createMedia.execute();
            } catch (Exception e) {
                Log.e(TAG, "Failed to create media description object.", e);
                shortToast("The file upload failed. :-(");
                return null;
            }
        }

        /**
         * Decides the name within Google Cloud Storage for this file. Incorporates the sender's deviceKey,
         * a random UUID, and the original filename.
         *
         * @param deviceKey
         * @return
         */
        private String getNewGCSFilename(String deviceKey, String filePath) {
            UUID newUUID = UUID.randomUUID();
            String baseFilename = FileUtils.getBaseFilenameFromPath(filePath);
            return "s/" + deviceKey + "/" + newUUID + "/" + baseFilename;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.outgoingShare_progressBar);
            TextView progressText = (TextView) findViewById(R.id.outgoingShare_progress_textView);
            progressBar.setVisibility(View.INVISIBLE);
            setStatusText("");
            if (querySuccess) {
                if (message != null) {
                    progressText.setText("Finished!");
                    logSentMessageAndFinish(message);
                }
            } else {
                progressText.setText("Upload failed.");
                setStatusText("Error: couldn't send this message.");
                shortToast("Failed to send message :-(");
            }
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.outgoingShare_progressBar);
            progressBar.setMax(100);
            progressBar.setVisibility(View.VISIBLE);
            TextView progressText = (TextView) findViewById(R.id.outgoingShare_progress_textView);
            progressText.setVisibility(View.VISIBLE);
            progressText.setText("Starting upload.");
            setStatusText("Sending, please wait...");
        }

        @Override
        protected void onProgressUpdate(ProgressParams... progressParams) {
            ProgressParams params = progressParams[0];
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.outgoingShare_progressBar);
            progressBar.setProgress(params.progress);
            TextView progressText = (TextView) findViewById(R.id.outgoingShare_progress_textView);
            String progressString = params.bytesSent + " sent / " + params.totalBytes + " total";
            progressText.setText(progressString);
        }
    }

    /**
     * packages the items we need to update the upload progress indicators
     */
    private static class ProgressParams {
        public int progress;
        public long bytesSent;
        public long totalBytes;

        public ProgressParams(int progress, long bytesSent, long totalBytes) {
            this.progress = progress;
            this.bytesSent = bytesSent;
            this.totalBytes = totalBytes;
        }
    }
}

