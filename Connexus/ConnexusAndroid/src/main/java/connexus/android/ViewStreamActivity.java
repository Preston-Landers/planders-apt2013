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
import com.appspot.connexus_apt.streamlist.model.Stream;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
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
    String[] imageUrls;
    String[] imageLabels;
    GridView gridView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;

    Long streamId;
    Long streamOwnerId;

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
        streamId = intent.getLongExtra(Config.STREAM_ID, 0);
        streamOwnerId = intent.getLongExtra(Config.STREAM_OWNER_ID, 0);

        // Dear God... there must be a better way to handle this!
        if (streamId == null || streamId == 0) {
            throw new IllegalArgumentException("You must specify a streamId!");
        }
        if (streamOwnerId == null || streamOwnerId == 0) {
            throw new IllegalArgumentException("You must specify a streamOwnerId!");
        }

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


    private void loadImages(List<Media> mediaList) {
        imageUrls = new String[queryLimit];
        imageLabels = new String[queryLimit];
        int i = 0;
        for (Media media: mediaList) {
            String mediaUrl = media.getUrl();
            if (mediaUrl == null || mediaUrl.equals("")  || mediaUrl.startsWith("data:")) {
                mediaUrl = "";
            }
            imageUrls[i] = mediaUrl;
            String mediaComment = media.getComments();
            if (mediaComment == null || mediaComment.equals("")) {
                mediaComment = "(none)";
            }
            imageLabels[i] = mediaComment;
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
        intent.putExtra(Config.IMAGES, imageUrls);
        intent.putExtra(Config.IMAGE_LABELS, imageLabels);
        intent.putExtra(Config.IMAGE_POSITION, position);
        startActivity(intent);
    }

    private class ViewStreamTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private List<Media> mediaList;
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
                mediaList = service.getMedia(streamId, streamOwnerId, limit, offset)
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
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.viewstream_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (mediaList != null) {
                    loadImages(mediaList);
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

