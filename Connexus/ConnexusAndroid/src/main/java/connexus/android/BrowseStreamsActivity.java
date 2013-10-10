package connexus.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.appspot.connexus_apt.streamlist.model.Stream;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class BrowseStreamsActivity extends Activity {
    private static final String TAG = "BrowseStreamsActivity";
    private final int queryLimit = 9;
    private int queryOffset = 0;
    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;
    List<Stream> streamList;
    String[] imageUrls;
    String[] imageLabels;
    GridView gridView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

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
        setContentView(R.layout.ac_browse_streams);

        Intent intent = getIntent();
        queryOffset = intent.getIntExtra(Config.NAV_OFFSET, 0);

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
        checkNavButtonState();
        new BrowseStreamsTask().execute();
    }

    /**
     * Figure out if we should be showing each nav left/right button
     */
    private void checkNavButtonState() {
        ImageButton browseLeftButton = (ImageButton) findViewById(R.id.browseLeftButton);

        if (queryOffset > 0) {
            browseLeftButton.setVisibility(View.VISIBLE);
        } else {
            browseLeftButton.setVisibility(View.INVISIBLE);
       }

    }

    // figure out if we need this
    // http://developer.android.com/training/basics/firstapp/starting-activity.html
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ac_browse_streams_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void loginButton(View view) {
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
        }
    }

    // When you click the View Streams button
    public void ViewStreamsButton(View view) {
        Intent intent = new Intent(this, BrowseStreamsActivity.class);
        startActivity(intent);
     }

    public void GoRightButton(View view) {
        int newOffset = queryOffset + queryLimit;
        queryOffset = newOffset;
        new BrowseStreamsTask().execute();
    }

    public void GoLeftButton(View view) {
        int newOffset = queryOffset - queryLimit;
        if (newOffset < 0) {
            newOffset = 0;
        }
        queryOffset = newOffset;
        new BrowseStreamsTask().execute();

    }


    private void loadImages(List<Stream> streamList) {
        this.streamList = streamList;
        imageUrls = new String[queryLimit];
        imageLabels = new String[queryLimit];
        int i = 0;
        for (Stream stream: streamList) {
            String coverURL = stream.getCoverURL();
            if (coverURL == null || coverURL.equals("")  || coverURL.startsWith("data:")) {
                coverURL = "";
                coverURL = null;
            }
            imageUrls[i] = coverURL;
            String streamName = stream.getName();
            if (streamName == null || streamName.equals("")) {
                streamName = "(no name)";
            }
            imageLabels[i] = streamName;
            i++;
        }
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                // .cacheOnDisc(true)
                // .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        checkNavButtonState();

        gridView = (GridView) findViewById(R.id.gridview);
        ((GridView) gridView).setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startViewStreamActivity(position);
            }
        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(BrowseStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void startViewStreamActivity(int position) {
        Stream stream = streamList.get(position);
        if (stream == null) {
            Log.e(TAG, "can't find stream to view at position: " + position);
            return;
        }

        Intent intent = new Intent(this, ViewStreamActivity.class);
        intent.putExtra(Config.STREAM_ID, stream.getId());
        intent.putExtra(Config.STREAM_OWNER_ID, stream.getOwnerId());
        startActivity(intent);

    }

    private class BrowseStreamsTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private List<Stream> streamList;
        @Override
        protected Void doInBackground(Void... params) {
            int limit = queryLimit;
            int offset = queryOffset;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            String rv = "<No result>";
            try {
                service = EndpointService.getStreamlistService(creds);
                streamList = service.getStreams(limit, offset)
                        .setQuery("test")
                        .execute()
                        .getItems();

                querySuccess = true;
            } catch (GoogleAuthIOException e) {
                Log.e(TAG, "Browse Streams fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Browse Streams failed.", e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.browse_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (streamList != null) {
                    loadImages(streamList);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.browse_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imageUrls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            RelativeLayout imgContainerRL;
            imgContainerRL = (RelativeLayout) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
            if (convertView == null) {
                imageView = (ImageView) imgContainerRL.getChildAt(0);
            } else {
                imgContainerRL = (RelativeLayout) convertView;
                imageView = (ImageView) imgContainerRL.getChildAt(0);
            }
            TextView textView = (TextView) imgContainerRL.getChildAt(1);
            textView.setText(imageLabels[position]);
            imageLoader.displayImage(imageUrls[position], imageView, options);
            return imgContainerRL;
        }
    }
}

