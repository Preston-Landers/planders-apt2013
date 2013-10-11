package connexus.android.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import connexus.android.Account;
import connexus.android.Config;
import connexus.android.R;

public class UploadActivity extends BaseActivity {
    private static final String TAG = "UploadActivity";
    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;

    Long streamId;
    Long streamOwnerId;
    String streamName;

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

        // Dear God... there must be a better way to handle this!
        if (streamId == null || streamId == 0) {
            throw new IllegalArgumentException("You must specify a streamId!");
        }
        if (streamOwnerId == null || streamOwnerId == 0) {
            throw new IllegalArgumentException("You must specify a streamOwnerId!");
        }
        if (streamName == null) {
            streamName = "(no name)";
        }

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
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void chooseFromLibrary(View view) {
        String message = "(Not Implemented Yet)";
        Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void uploadNow(View view) {
        String message = "(Not Implemented Yet)";
        Toast.makeText(UploadActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}

