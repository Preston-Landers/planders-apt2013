package com.appspot.cee_me.android.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.MessageQuery;

public class IncomingShareActivity extends BaseActivity {
    private static final String TAG = "CheckMessagesActivity";

    private String messageKey;
    private String messageText;
    private String messageUrl;

    private Sync service;
    private MessageQuery messageQuery;

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

        if (messageText != null) {
            setMessageText(messageText);
        }
        if (messageUrl != null) {
            setMessageURL(messageUrl);
        }


        new LoadMessageTask().execute();
    }

    private void setMessageText(String messageText) {
        TextView streamLabel = (TextView) findViewById(R.id.incomingShare_text_tv);
        streamLabel.setText(messageText);
    }

    private void setMessageURL(String messageUrl) {
        TextView streamLabel = (TextView) findViewById(R.id.incomingShare_url_tv);
        streamLabel.setText(messageUrl);
    }

    private void setStatusText(String message) {
        TextView streamLabel = (TextView) findViewById(R.id.incomingShare_status_textview);
        streamLabel.setText(message);
    }

    private class LoadMessageTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            try {
                /*
                service = SyncEndpointService.getSyncService();
                Sync.GetMessages getMessages = service.getMessages(deviceKey);


                messageQuery = getMessages
                        .execute();
                */
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "Browse Streams fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Browse Streams failed.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            setStatusText("");
            if (querySuccess) {
                if (messageQuery != null) {
                    // loadImages(messageQuery);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            setStatusText("Please wait while I load the content.");
        }
    }
}

