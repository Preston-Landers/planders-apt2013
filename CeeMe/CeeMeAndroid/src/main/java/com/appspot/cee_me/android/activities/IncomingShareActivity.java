package com.appspot.cee_me.android.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.cee_me.android.*;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.Media;
import com.appspot.cee_me.sync.model.Message;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class IncomingShareActivity extends BaseActivity {
    private static final String TAG = CEEME + ".IncomingShareActivity";

    private String messageKey;
    private String messageText;
    private String messageUrl;

    private Sync service;
    private Message message;
    private Media media;
    private File localFile;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_incoming_share);
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        messageKey = intent.getStringExtra(Config.MESSAGE_KEY);
        messageText = intent.getStringExtra(Config.MESSAGE_TEXT);
        messageUrl = intent.getStringExtra(Config.MESSAGE_URL);

        // There must be a better way to handle this...?
        if (messageKey == null) {
            Log.i(TAG, "Null messageKey on " + this.getClass().getName());
            throw new IllegalArgumentException("You must specify a message key!");

        }
        setStatusText("");
        setSenderIdentity("<Loading...>");
        setMessageText(messageText);
        setMessageURL(messageUrl);

        new LoadMessageTask().execute();
    }

    private void setMessageText(String txt) {
        setText(txt, R.id.incomingShare_text_tv);
    }

    private void setMessageURL(String txt) {
        setText(txt, R.id.incomingShare_url_tv);
    }

    private void setStatusText(String txt) {
        setText(txt, R.id.incomingShare_status_textview);
    }

    private void setSenderIdentity(String txt) {
        setText(txt, R.id.incomingShare_from_tv);
    }

    private void setText(String txt, int viewId) {
        if (txt == null) {
            txt = "";
        }
        TextView streamLabel = (TextView) findViewById(viewId);
        streamLabel.setText(txt);
    }

    /**
     * Load the text views and such to display the contents of a server Message
     *
     * @param message API model Message to display
     */
    private void displayMessageDetails(Message message) {
        setMessageText(message.getText());
        if (localFile != null) {
            setMessageURL(localFile.getPath());
        } else {
            setMessageURL(message.getUrlData());
        }
        setSenderIdentity(message.getFromUser().getAccountName());
    }

    public void openIncomingShareURL(View view) {
        openIncomingShareButton(view);
    }

    public void openIncomingShareButton(View view) {
        if (localFile != null && media != null) {
            openExternalFile(localFile, media.getMimeType());
        } else {
            String url = message.getUrlData();
            if (url == null || url.length() == 0) {
                shortToast("Can't open URL.");
                return;
            }
            openExternalURL(url);
        }
    }

    public void cancelIncomingShareButton(View view) {
        // shortToast("Ignoring this message.");
        setResult(RESULT_CANCELED);
        finish();
    }

    private void openExternalURL(String theURL) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(theURL));
        startActivity(i);
    }

    private void openExternalFile(File localFile, String mimeType) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(localFile), mimeType);
        try {
            startActivity(i);
        } catch (ActivityNotFoundException notFound) {
            shortToast("Can't find an app to view this file.");
        }
    }

    private class LoadMessageTask extends AsyncTask<Void, ProgressParams, Void> {
        private boolean querySuccess = false;
        private double rate = 0;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            try {
                service = SyncEndpointService.getSyncService(getCredential());
                Sync.GetMessage getMessage = service.getMessage(messageKey);
                message = getMessage.execute();

                media = message.getMedia();
                if (media != null) {
                    loadMedia(media);
                }
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "message retrieval fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "message retrieval failed: ", e);
                localFile = null;
                media = null;
                message = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            String rateTxt = "";
            if (rate != 0) {
                rateTxt = " " + rate + " bytes/sec";
                shortToast("Transfer rate: " + rateTxt);
            }
            setStatusText("");
            if (querySuccess) {
                if (message != null) {
                    displayMessageDetails(message);
                }
            } else {
                setStatusText("Error: couldn't load this message.");
                shortToast("Failed to load message :-(");
            }
        }

        @Override
        protected void onPreExecute() {
            TextView progressText = (TextView) findViewById(R.id.incomingShare_progress_textView);
            progressText.setVisibility(View.VISIBLE);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setText("Starting download.");
            setStatusText("Please wait while I load the content.");
        }

        private IOProgress getAsyncTaskIOProgress() {
            // Create a progress updater.
            return new IOProgress() {
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

                @Override
                public void setCurrentRate(double thisRate) {
                    rate = thisRate;
                }
            };
        }

        private void loadMedia(Media media) throws IOException, GeneralSecurityException {
            if (media == null) {
                Log.e(TAG, "Error: passed null media to loadMedia");
                return;
            }
            // See if we already have the file

            if (!FileUtils.isStorageWritable()) {
                Log.e(TAG, "Can't write to storage in order to download media!");
                shortToast("Error: can't write to storage media.");
                return;
            }
            localFile = new File(getExternalFilesDir(null) + "/" + media.getGcsFilename());
            if (localFile.exists()) {
                Log.d(TAG, "Local file already exists: " + localFile);
            } else {
                FileUtils.ensureDirectory(localFile);
                CloudStorage cloudStorage = getCloudStorage();
                cloudStorage.downloadFile(Config.GCS_BUCKET, media.getGcsFilename(), localFile, getAsyncTaskIOProgress());
                Log.d(TAG, "Media download complete.");
            }
        }

        @Override
        protected void onProgressUpdate(ProgressParams... progressParams) {
            ProgressParams params = progressParams[0];
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setProgress(params.progress);
            TextView progressText = (TextView) findViewById(R.id.incomingShare_progress_textView);
            String progressString = params.getProgressString();
            progressText.setText(progressString);
        }

    }
}

