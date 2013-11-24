package com.appspot.cee_me.android.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.RegisterEndpointService;
import com.appspot.cee_me.android.adapters.DirectoryListAdapter;
import com.appspot.cee_me.register.Register;
import com.appspot.cee_me.register.model.Device;

import java.util.List;

public class DirectoryActivity extends BaseActivity
        implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = "DirectoryActivity";
    private SearchView searchView;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_directory);
        super.onCreate(savedInstanceState);

        searchView = (SearchView) findViewById(R.id.directory_searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        requireSignIn();

        // TODO: use location here!

        Log.i(TAG, "DirectoryActivity started.");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mDbHelper  != null) {
//            mDbHelper.close();
//        }
    }

    public boolean onQueryTextChange(String newText) {
        showResults(newText);
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        showResults(query);
        return false;
    }

    public boolean onClose() {
        showResults("");
        return false;
    }

    private void showResults(String query) {
        String[] params = { query };
        new SearchDirectoryTask().execute(params);
    }

    private void loadSearchResults(List<Device> deviceList) {
        shortToast("Got " + deviceList.size() + " results!");
        DirectoryListAdapter listAdapter = new DirectoryListAdapter(this, R.layout.item_directory_result, deviceList);
        ListView listView = (ListView) findViewById(R.id.directory_listView);

        View header = getLayoutInflater().inflate(R.layout.item_directory_header_row, null);
        listView.addHeaderView(header);

        listView.setAdapter(listAdapter);
    }

    @SuppressWarnings("UnusedParameters")
    public void cancelDirectoryButton(View view) {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    public void chooseRecipientDirectoryButton(View view) {
        // TODO: set data value!
        setResult(RESULT_OK);
        this.finish();
    }

    private class SearchDirectoryTask extends AsyncTask<String, Void, Void> {
        private boolean querySuccess = false;
        private Register service;
        private List<Device> deviceList;
        private final int limit = 100;
        private final int offset = 0;

        @Override
        protected Void doInBackground(String... params) {
            querySuccess = false;
            deviceList = null;
            String query = params[0];
            try {
                service = RegisterEndpointService.getRegisterService(getCredential());
                Register.GetDeviceDirectory getDeviceDirectory = service.getDeviceDirectory(limit, offset);
                getDeviceDirectory.setQuery(query);
                deviceList = getDeviceDirectory.execute().getItems();
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "directory retrieval fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "directory retrieval failed: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.directory_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (deviceList != null) {
                    loadSearchResults(deviceList);
                }
            } else {
                shortToast("Failed to load directory info :-(");
            }
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.directory_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}

