
package com.appspot.cee_me.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;

/**
 *
 */
public class CeeMeApplication extends Application {
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onCreate() {
//        if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
//        }

        super.onCreate();

        // initImageLoader(getApplicationContext());

    }

    // http://stackoverflow.com/questions/11411395/how-to-get-current-foreground-activity-context-in-android
    private Activity mCurrentActivity = null;

    /**
     * Returns the foreground activity, if any, else null
     * @return foreground activity if there is one, otherwise null
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }
}