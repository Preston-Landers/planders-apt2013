package com.appspot.cee_me.android.activities;

import android.content.Intent;
import android.net.Uri;
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

public class OutgoingShareActivity extends BaseActivity {
    private static final String TAG = "OutgoingShareActivity";

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
        setContentView(R.layout.ac_outgoing_share);
        super.onCreate(savedInstanceState);
        requireSignIn();

        Intent intent = getIntent();

        String data = intent.getDataString();
        String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_SEND) && intent.hasExtra(Intent.EXTRA_TEXT)) {
            data = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        setStatusText("");
        setReceiverIdentity("<Select recipient...>");
        // setMessageText(messageText);
        setMessageURL(data);

    }

    private void setMessageText(String txt) {
        setText(txt, R.id.outgoingShare_message_editText);
    }

    private void setMessageURL(String txt) {
        setText(txt, R.id.outgoingShare_url_editText);
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

    /**
     * Load the text views and such to display the contents of a server Message
     *
     * @param message API model Message to display
     */
    private void displayMessageDetails(Message message) {
        setMessageText(message.getText());
        setMessageURL(message.getUrlData());
        setReceiverIdentity(message.getFromUser().getAccountName());
    }

    public void sendShareNow(View view) {
        shortToast("Not implemented yet.");
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

    private class SendMessageTask extends AsyncTask<Void, Void, Void> {
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
//                Log.e(TAG, "message send fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "message send failed: ", e);
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
                setStatusText("Error: couldn't send this message.");
                shortToast("Failed to send message :-(");
            }
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.incomingShare_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            setStatusText("Sending, please wait...");
        }
    }
}

