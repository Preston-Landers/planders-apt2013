package connexus.android;

import android.app.Activity;
import android.view.MenuItem;

/**
 * Created with IntelliJ IDEA.
 * User: Preston
 * Date: 10/10/13
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
