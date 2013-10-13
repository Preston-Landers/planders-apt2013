package connexus.android.activities;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.appspot.connexus_apt.streamlist.model.Media;
import com.appspot.connexus_apt.streamlist.model.NearbyResult;
import com.appspot.connexus_apt.streamlist.model.StreamResult;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.nostra13.universalimageloader.core.ImageLoader;
import connexus.android.*;

import java.util.List;

public class ViewStreamActivity extends BaseActivity {
    private static final String TAG = "ViewStreamsActivity";
    private static final int DO_UPLOAD_ACTIVITY_REQUEST_CODE = 100;
    private final int queryLimit = 9;
    private int queryOffset = 0;
    GoogleAccountCredential credential;
    private boolean signedIn = false;

    Streamlist service;
    String accountName;
    StreamResult streamResult;
    NearbyResult nearbyResult;
    Long streamId;
    Long streamOwnerId;
    String streamName;

    GridView gridView;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean doingLocationSearch = false;

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
        doingLocationSearch = intent.getBooleanExtra(Config.LOCATION_SEARCH, false);

        // Dear God... there must be a better way to handle this!
        if (!doingLocationSearch) {
            if (streamId == null || streamId == 0) {
                throw new IllegalArgumentException("You must specify a streamId!");
            }
            if (streamOwnerId == null || streamOwnerId == 0) {
                throw new IllegalArgumentException("You must specify a streamOwnerId!");
            }
        }
        if (streamName == null) {
            streamName = "(Loading...)";
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

        setStatusText("Please wait, loading...");
        if (doingLocationSearch) {
            useLocation = true;
            startLocationServices();
            setTitle("Nearby Images");
        } else {
            setTitle(streamName);
        }

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
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra(Config.STREAM_NAME, streamName);
        intent.putExtra(Config.STREAM_ID, streamId);
        intent.putExtra(Config.STREAM_OWNER_ID, streamOwnerId);
        intent.putExtra(Config.MY_ID, streamResult.getMyId());

        intent.putExtra(Config.STREAM_UPLOAD_URL, streamResult.getUploadUrl());
        startActivityForResult(intent, DO_UPLOAD_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DO_UPLOAD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // re-run a new query so we can see the upload.
                String message = "Upload complete!";
                Toast.makeText(ViewStreamActivity.this, message, Toast.LENGTH_SHORT).show();
                new ViewStreamTask().execute();
            }
        }
    }

    private void loadStreamResults() {
        if (streamResult == null) {
            Toast.makeText(ViewStreamActivity.this, "Can't load stream.", Toast.LENGTH_SHORT).show();
            return;
        }
        streamName = streamResult.getStreamName();
        setTitle(streamName);
        setStatusText("Stream: " + streamName + " from: " + streamResult.getStreamOwnerName());
        loadImages();
    }

    private void loadNearbyResults() {
        if (nearbyResult == null) {
            Toast.makeText(ViewStreamActivity.this, "Can't load nearby results.", Toast.LENGTH_SHORT).show();
            return;
        }
        setStatusText("Photos near: " + getNearbySearchDescription(nearbyResult));
        loadImages();
    }

    private static String getNearbySearchDescription(NearbyResult nearbyResult) {
        return getNearbySearchDescription(nearbyResult.getSearchLatitude(), nearbyResult.getSearchLongitude());
    }

    private static String getNearbySearchDescription(Double latitude, Double longitude) {
        // return latitude + " , " + longitude;
        return String.format("%1$,.5f , %2$,.5f", latitude, longitude);
    }

    private void loadImages() {
        checkNavButtonState();

        gridView = (GridView) findViewById(R.id.vs_gridview);
        ((GridView) gridView).setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (nearbyResult != null) {
                    startNearbyImageSelectActivity(position);
                } else {
                    startImagePagerActivity(position);
                }
            }
        });
    }

    private void setStatusText(String message) {
        TextView statusTextView = (TextView) findViewById(R.id.view_stream_statustext);
        statusTextView.setText(message);
    }

    private void startNearbyImageSelectActivity(int position) {
        Media media = nearbyResult.getMediaList().get(position);
        Intent intent = new Intent(this, ViewStreamActivity.class);
        intent.putExtra(Config.STREAM_ID, media.getStreamId());
        intent.putExtra(Config.STREAM_OWNER_ID, media.getStreamOwnerId());
        startActivity(intent);

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
        for (Media media : mediaList) {
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
        // private StreamResult streamResult;

        @Override
        protected Void doInBackground(Void... params) {
            int limit = queryLimit;
            int offset = queryOffset;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }

            try {
                if (doingLocationSearch) {
                    // If doing a location search, wait until we have a location
                    // for a short amount of time.
                    Location location = waitForLocation();
                    if (location == null) {
                        // Maybe do a Toast here?
                        Log.e(TAG, "Tried to do a location search but couldn't get location within time limit");
                        Toast.makeText(ViewStreamActivity.this, "ERROR: Can't find location", Toast.LENGTH_SHORT).show();
                        return null;
                    } else {
                        // Location search is like a "Virtual Stream" but has a different return value on the API call
                        Double qLat = new Double(location.getLatitude());
                        Double qLong = new Double(location.getLongitude());
                        service = EndpointService.getStreamlistService(creds);
                        Streamlist.GetNearby request = service.getNearby(new Integer(limit), new Integer(offset), qLat, qLong);
                        nearbyResult = request.execute();
                        streamResult = null;
                        querySuccess = true;
                    }
                } else {
                    // Normal stream view
                    service = EndpointService.getStreamlistService(creds);
                    streamResult = service.getMedia(streamId, streamOwnerId, limit, offset)
                            .execute();
                    nearbyResult = null;

                    querySuccess = true;
                }

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
                    loadStreamResults();
                } else if (nearbyResult != null) {
                    loadNearbyResults();
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
            if (streamResult != null) {
                return streamResult.getResultSize();
            } else if (nearbyResult != null) {
                return nearbyResult.getResultSize();
            }
            return 0;
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
            if (streamResult != null) {
                List<Media> mediaList = streamResult.getMediaList();
                Media media = mediaList.get(position);

                textView.setText(media.getComments());
                imageLoader.displayImage(media.getUrl(), imageView, getDisplayOptions());
            } else if (nearbyResult != null) {
                List<Media> mediaList = nearbyResult.getMediaList();
                Media media = mediaList.get(position);

                String imageLabel = String.format("%s : %.3f%n km", media.getComments(), (media.getMetersToSearchPoint() / 1000));
                textView.setText(imageLabel);
//                textView.setText(media.getComments() + " @ " +
//                        getNearbySearchDescription(media.getLatitude(), media.getLongitude()));
                imageLoader.displayImage(media.getUrl(), imageView, getDisplayOptions());
            }
            return imgContainerRL;
        }
    }
}

