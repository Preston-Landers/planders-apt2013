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
import com.appspot.cee_me.android.SyncEndpointService;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.Message;

public class IncomingShareActivity extends BaseActivity {
    private static final String TAG = "CheckMessagesActivity";

    private String messageKey;
    private String messageText;
    private String messageUrl;

    private Sync service;
    private Message message;

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
     * @param message API model Message to display
     */
    private void displayMessageDetails(Message message) {
        setMessageText(message.getText());
        setMessageURL(message.getUrlData());
        setSenderIdentity(message.getFromUser().getAccountName());
    }

    public void openIncomingShareButton(View view) {

    }

    public void cancelIncomingShareButton(View view) {
        shortToast("Ignoring this message.");

    }

    private class LoadMessageTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            try {
                service = SyncEndpointService.getSyncService(getCredential());
                Sync.GetMessage getMessage = service.getMessage(messageKey);
                message = getMessage.execute();
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "message retrieval fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "message retrieval failed: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
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
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            setStatusText("Please wait while I load the content.");
        }
    }
}

