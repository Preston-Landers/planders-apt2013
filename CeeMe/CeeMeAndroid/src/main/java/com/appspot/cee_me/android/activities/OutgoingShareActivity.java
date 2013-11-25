package com.appspot.cee_me.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.SyncEndpointService;
import com.appspot.cee_me.register.model.Device;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.Message;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;

public class OutgoingShareActivity extends BaseActivity {
    private static final String TAG = CEEME + ".OutgoingShareActivity";

    private static final int REQUEST_DIRECTORY_LOOKUP = 23;

    private String toDeviceKey;
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

        String deviceDesc = device.getOwnerAccountName() + ": " + device.getPublicId() + " " + device.getName() ;
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

    private class SendMessageTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            String msgText = getMessageText();
            String urlData = getMessageURL();
            try {
                service = SyncEndpointService.getSyncService(getCredential());
                Sync.SendMessage sendMessage = service.sendMessage(deviceKey, toDeviceKey);
                // TODO: deal with media attachments here
                sendMessage.setText(msgText);
                sendMessage.setUrlData(urlData);
                message = sendMessage.execute();
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
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.outgoingShare_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            setStatusText("");
            if (querySuccess) {
                if (message != null) {
                    logSentMessageAndFinish(message);
                }
            } else {
                setStatusText("Error: couldn't send this message.");
                shortToast("Failed to send message :-(");
            }
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.outgoingShare_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            setStatusText("Sending, please wait...");
        }
    }
}

