package com.appspot.cee_me.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.appspot.cee_me.android.Config;
import com.appspot.cee_me.android.R;
import com.appspot.cee_me.android.SyncEndpointService;
import com.appspot.cee_me.android.adapters.MessageListAdapter;
import com.appspot.cee_me.sync.Sync;
import com.appspot.cee_me.sync.model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CheckMessagesActivity extends BaseActivity
        implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String TAG = CEEME + ".CheckMessagesActivity";
    private static final int REQUEST_SHOW_MESSAGE = 82;

    private SearchView searchView;
    private MessageListAdapter listAdapter;
    private ListView listView;

    // query parameters for the server
    private final static int defaultQueryLimit = 10;
    private int queryLimit = defaultQueryLimit;
    private int queryOffset = 0;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ac_check_messages);
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        requireSignIn();

        searchView = (SearchView) findViewById(R.id.checkMessages_searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        listAdapter = new MessageListAdapter(this, R.layout.item_message_result, new ArrayList<Message>());

        listView = (ListView) findViewById(R.id.checkMessages_listView);
        listView.setAdapter(listAdapter);

        showResults("");
        // new CheckMessagesTask().execute();
    }

    public void checkMessagesCancelButton(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void checkMessagesOpenButton(View view) {
        shortToast("Not implemented yet");
        setResult(RESULT_OK);
        finish();
    }

    private void showResults(String query) {
        // String[] params = { query };
        new CheckMessagesTask().execute();
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
        // showResults("");
        return false;
    }

    private void loadMessages(final List<Message> messageList) {
        listAdapter.setMessageList(messageList);
        final Context thisContext = this;

        // Define the on-click listener for the list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // shortToast("Clicked: " + position + " id: " + id);
                Message message = messageList.get(position);
                Log.i(TAG, "click message : " + message);

                Intent intent = new Intent(thisContext, IncomingShareActivity.class);
                intent.putExtra(Config.MESSAGE_KEY, message.getMessageKey());
                intent.putExtra(Config.MESSAGE_TEXT, message.getText());
                intent.putExtra(Config.MESSAGE_URL, message.getUrlData());
                startActivityForResult(intent, REQUEST_SHOW_MESSAGE);

            }
        });

    }

    private class CheckMessagesTask extends AsyncTask<Void, Void, Void> {
        private boolean querySuccess = false;
        private Sync service;
        private List<Message> messageList;

        @Override
        protected Void doInBackground(Void... params) {
            querySuccess = false;
            int limit = queryLimit;
            int offset = queryOffset;
            try {
                service = SyncEndpointService.getSyncService(getCredential());
                Sync.GetMessages getMessages = service.getMessages(deviceKey);
                getMessages.setLimit(limit);
                getMessages.setOffset(offset);

                messageList = getMessages.execute().getMessageList();
                querySuccess = true;
//            } catch (GoogleAuthIOException e) {
//                Log.e(TAG, "Check messages fail: " + e.getCause());
            } catch (Exception e) {
                Log.e(TAG, "Check messages failed.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void rv) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.checkMessages_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            if (querySuccess) {
                if (messageList != null) {
                    loadMessages(messageList);
                }
            } else {
                shortToast("Failed to load messages :-(");
            }

        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.checkMessages_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    // Don't really need to do anything with the result here.
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SHOW_MESSAGE:
                if (resultCode == RESULT_CANCELED) {
                    shortToast(getString(R.string.canceled));
                }
                break;
        }

    }
*/
}

