package connexus.android.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.appspot.connexus_apt.streamlist.Streamlist;
import com.appspot.connexus_apt.streamlist.model.Stream;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException;
import com.nostra13.universalimageloader.core.ImageLoader;
import connexus.android.*;

import java.util.List;

public class BrowseStreamsActivity extends BaseActivity {
    private static final String TAG = "BrowseStreamsActivity";

    private boolean signedIn = false;
    GridView gridView;

    // services 'n things
    Streamlist service;
    GoogleAccountCredential credential;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    // query parameters for the server
    private final static int defaultQueryLimit = 9;
    private int queryLimit = defaultQueryLimit;
    private int queryOffset = 0;
    private boolean showMySubs = false;
    private String searchTerm = null;
    List<Stream> streamList; // query results

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

        // Blank out the status text
        setStatusText("");

        Intent intent = getIntent();
        queryOffset = intent.getIntExtra(Config.NAV_OFFSET, 0);
        queryLimit = intent.getIntExtra(Config.NAV_LIMIT, defaultQueryLimit);
        showMySubs = intent.getBooleanExtra(Config.SHOW_MY_SUBS, false);
        searchTerm = intent.getStringExtra(Config.SEARCH_TERM);

        if (showMySubs) {
            setTitle("My Subscriptions");
        }

        // Handles the action bar search term
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchTerm = intent.getStringExtra(SearchManager.QUERY);
            setTitle("Search: " + searchTerm);
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

        new BrowseStreamsTask().execute();
    }

    /**
     * Figure out correct state of buttons
     */
    private void checkNavButtonState() {
        ImageButton browseLeftButton = (ImageButton) findViewById(R.id.browseLeftButton);
        ImageButton browseRightButton = (ImageButton) findViewById(R.id.browseRightButton);

        if (queryOffset > 0) {
            browseLeftButton.setVisibility(View.VISIBLE);
        } else {
            browseLeftButton.setVisibility(View.INVISIBLE);
        }

        if (streamList != null) {
            if (streamList.size() < queryLimit) {
                browseRightButton.setVisibility(View.INVISIBLE);
            } else {
                browseRightButton.setVisibility(View.VISIBLE);
            }

        }

        // Hide the "show my subs" button if we're there.
        Button showMySubsButton = (Button) findViewById(R.id.mysubs_button);
        showMySubsButton.setVisibility(showMySubs ? View.INVISIBLE : View.VISIBLE);

    }

    private void setStatusText(String message) {
        TextView statusTextView = (TextView) findViewById(R.id.browse_status_textview);
        statusTextView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean rv = super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ac_browse_streams_actions, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return rv;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
//            case R.id.action_search:
//                openSearch();
//                return true;
            case R.id.action_location_search:
                doLocationSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loginButton(View view) {
        if (credential.getSelectedAccountName() != null) {
            Log.i(TAG, "You are logged into android as: " + credential.getSelectedAccountName());
        }
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

    public void MySubsButton(View view) {
        Intent intent = new Intent(this, BrowseStreamsActivity.class);
        intent.putExtra(Config.SHOW_MY_SUBS, true);
        intent.putExtra(Config.NAV_OFFSET, 0);
        intent.putExtra(Config.NAV_LIMIT, queryLimit);

        startActivity(intent);
    }

    private void doLocationSearch() {
        Intent intent = new Intent(this, ViewStreamActivity.class);
        intent.putExtra(Config.LOCATION_SEARCH, true);
        intent.putExtra(Config.NAV_OFFSET, 0);
        intent.putExtra(Config.NAV_LIMIT, queryLimit);

        startActivity(intent);
    }


    private void loadImages(List<Stream> streamList) {
        this.streamList = streamList;

        checkNavButtonState();

        if (searchTerm != null) {
            setStatusText(streamList.size() + " search results for: " + searchTerm);
        }

        gridView = (GridView) findViewById(R.id.gridview);
        ((GridView) gridView).setAdapter(new ImageAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startViewStreamActivity(position);
            }
        });
    }

    private void startViewStreamActivity(int position) {
        Stream stream = streamList.get(position);
        if (stream == null) {
            Log.e(TAG, "can't find stream to view at position: " + position);
            return;
        }

        Intent intent = new Intent(this, ViewStreamActivity.class);
        intent.putExtra(Config.STREAM_NAME, stream.getName());
        intent.putExtra(Config.STREAM_ID, stream.getId());
        intent.putExtra(Config.STREAM_OWNER_ID, stream.getOwnerId());
        startActivity(intent);

    }

    private class BrowseStreamsTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private List<Stream> streamList;
        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            int limit = queryLimit;
            int offset = queryOffset;
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            try {
                service = EndpointService.getStreamlistService(creds);
                Streamlist.GetStreams getStreams = service.getStreams(limit, offset);

                // Set various search criteria
                if (searchTerm != null) {
                    getStreams.setQuery(searchTerm);
                }
                getStreams.setMySubs(showMySubs);

                streamList = getStreams
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
            return streamList.size();
            // return imageUrls.length;
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

            Stream stream = streamList.get(position);

            textView.setText(stream.getName());
            imageLoader.displayImage(stream.getCoverURL(), imageView, getDisplayOptions());
            return imgContainerRL;
        }
    }
}

