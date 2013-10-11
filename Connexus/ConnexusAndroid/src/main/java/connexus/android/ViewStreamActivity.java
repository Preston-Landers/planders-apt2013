package connexus.android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.appspot.connexus_apt.streamlist.model.Media;
import com.appspot.connexus_apt.streamlist.model.StreamResult;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ViewStreamActivity extends Activity {
    private static final String TAG = "BrowseStreamsActivity";
    private final int queryLimit = 9;
    private int queryOffset = 0;
    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;
    StreamResult streamResult;
    GridView gridView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

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
        setContentView(R.layout.ac_view_stream);

        Intent intent = getIntent();
        queryOffset = intent.getIntExtra(Config.NAV_OFFSET, 0);
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

        setTitle(streamName);

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
        new ViewStreamTask().execute();
    }

    /**
     * Should we be showing each nav left/right button?
     */
    private void checkNavButtonState() {
        ImageButton browseLeftButton = (ImageButton) findViewById(R.id.viewStreamLeftButton);
        ImageButton browseRightButton = (ImageButton) findViewById(R.id.viewStreamRightButton);

        if (queryOffset > 0) {
            browseLeftButton.setVisibility(View.VISIBLE);
        } else {
            browseLeftButton.setVisibility(View.INVISIBLE);
       }
        if (streamResult != null) {
            if (streamResult.getResultSize() < queryLimit) {
                browseRightButton.setVisibility(View.INVISIBLE);
            } else {
                browseRightButton.setVisibility(View.VISIBLE);
            }
            Button uploadButton = (Button) findViewById(R.id.upload_button);
            if (streamResult.getCanUpload()) {
                uploadButton.setVisibility(View.VISIBLE);
            } else {
                uploadButton.setVisibility(View.INVISIBLE);
            }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.ac_browse_streams_actions, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void GoRightButton(View view) {
        int newOffset = queryOffset + queryLimit;
        queryOffset = newOffset;
        new ViewStreamTask().execute();

    }

    public void GoLeftButton(View view) {
        int newOffset = queryOffset - queryLimit;
        if (newOffset < 0) {
            newOffset = 0;
        }
        queryOffset = newOffset;
        new ViewStreamTask().execute();
    }

    public void UploadButton(View view) {

    }

    private void loadImages(StreamResult streamResult) {
        this.streamResult = streamResult;
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                // .cacheOnDisc(true)
                // .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        checkNavButtonState();

        gridView = (GridView) findViewById(R.id.vs_gridview);
        ((GridView) gridView).setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Toast.makeText(BrowseStreamsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        if (streamResult == null) {
            Log.e(TAG, "No media loaded, can't start image pager");
            return;
        }
        List<Media> mediaList = streamResult.getMediaList();
        if (mediaList == null) {
            Log.e(TAG, "No media loaded, can't start image pager");
            return;
        }
        String[] imageUrls = new String[mediaList.size()];
        String[] imageLabels = new String[mediaList.size()];
        int i = 0;
        for (Media media: mediaList) {
            imageUrls[i] = media.getUrl();
            imageLabels[i] = media.getComments();
            i++;
        }
        intent.putExtra(Config.IMAGES, imageUrls);
        intent.putExtra(Config.STREAM_NAME, streamName);
        intent.putExtra(Config.IMAGE_LABELS, imageLabels);
        intent.putExtra(Config.IMAGE_POSITION, position);
        startActivity(intent);
    }

    private class ViewStreamTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private StreamResult streamResult;

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
                streamResult = service.getMedia(streamId, streamOwnerId, limit, offset)
                        .execute();

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
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.viewstream_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (streamResult != null) {
                    loadImages(streamResult);
                }
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.viewstream_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return streamResult.getResultSize();
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
            List<Media> mediaList = streamResult.getMediaList();
            Media media = mediaList.get(position);

            textView.setText(media.getComments());
            imageLoader.displayImage(media.getUrl(), imageView, options);
            return imgContainerRL;
        }
    }
}

