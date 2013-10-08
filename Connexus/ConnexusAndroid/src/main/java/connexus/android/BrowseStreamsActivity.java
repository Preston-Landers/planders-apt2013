package connexus.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
    Streamlist service;
    String accountName;
    GoogleAccountCredential credential;
    private boolean signedIn = false;
    String[] imageUrls;
    // AbsListView gridView;
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

        new BrowseStreamsTask().execute();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    private void loadImages(List<Stream> streamList) {
        imageUrls = new String[queryLimit];
        int i = 0;
        for (Stream stream: streamList) {
            String coverURL = stream.getCoverURL();
            if (coverURL != null && coverURL.length() > 0 && ! coverURL.startsWith("data:")) {
                imageUrls[i] = coverURL;
            } else {
                imageUrls[i] = ""; // is this necessary?
            }
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

        gridView = (GridView) findViewById(R.id.gridview);
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
        intent.putExtra(Config.IMAGE_POSITION, position);
        startActivity(intent);
    }

    // Combine this with the other task
    private class BrowseStreamsTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private List<Stream> streamList;
        @Override
        protected Void doInBackground(Void... params) {
            int limit = queryLimit;
            int offset = 2; // XXX TODO
            GoogleAccountCredential creds = null;
            if (signedIn) {
                creds = credential;
            }
            Streamlist.Builder builder = new Streamlist.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), creds);
            // builder.setRootUrl("http://192.168.56.1:8088/_ah/api/"); // for localhost development
            service = builder.build();
            String rv = "<No result>";
            try {
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
//            TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
//            DateTime now = new DateTime();
            if (querySuccess) {
//                textView.setText("Server said: < " + " > at " + now);
                if (streamList != null) {
                    loadImages(streamList);
                }
            } else {
//                textView.setText("ERROR: " + " time: " + now);
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
            if (convertView == null) {
                imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }

            imageLoader.displayImage(imageUrls[position], imageView, options);

            return imageView;
        }
    }
}

