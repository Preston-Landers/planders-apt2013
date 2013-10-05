package connexus.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import org.joda.time.DateTime;

import com.appspot.connexus_apt.helloworld.Helloworld;



public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";
    Helloworld service;
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(connexus.android.R.menu.main, menu);
        return true;
    }

    public void loginButton(View view) {
        // EditText editText = (EditText) findViewById(R.id.edit_message);
        TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
        DateTime now = new DateTime();
        textView.setText("starting task at: " + now);

        new WelcomeTask().execute("hello");

    }


    private class WelcomeTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            Helloworld.Builder builder = new Helloworld.Builder(
                    AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null);
            service = builder.build();
            String rv = "<No result>";
            try {
                String serverMsg = service.greetings().getGreeting(1).execute().getMessage();
                rv = serverMsg;
//            } catch (IOException e) {
//                Log.e(TAG, "hello greeting failed.", e);
            } catch (Exception e) {
                Log.e(TAG, "hello greeting failed.", e);
            }
            return rv;
        }
        @Override
        protected void onPostExecute(String result) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.INVISIBLE);
            TextView textView = (TextView) findViewById(R.id.welcome_status_textview);
            textView.setText("[ " + result + " ] " + new DateTime().now());
        }

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

}

