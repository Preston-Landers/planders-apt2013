package com.appspot.cee_me.android;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.appspot.cee_me.android.activities.IncomingShareActivity;
import com.appspot.cee_me.android.activities.RegisterActivity;
import com.google.android.gcm.GCMBaseIntentService;

import java.util.List;

import static com.appspot.cee_me.android.Config.displayMessage;

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(Config.GCM_SENDER_KEY);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, getString(R.string.gcm_registered,
                registrationId));
        RegisterActivity.registerServiceCallback(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        RegisterActivity.unRegisterServiceCallback(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message. Extras: " + intent.getExtras());
        IncomingMessageParams params = IncomingMessageParams.getMessageParamsFromExtras(intent.getExtras());

        // notifies user
        generateNotification(context, params);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        // generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     * TODO: why is this deprecated?
     */
    @SuppressWarnings("deprecation")
    private static void generateNotification(Context context, IncomingMessageParams messageParams) {

        String message = messageParams.getMsgText();
        String msgUrl = messageParams.getMsgUrl();
        String msgKey = messageParams.getMsgKey();

        Intent notificationIntent = new Intent(context, IncomingShareActivity.class);
        notificationIntent.putExtra(Config.MESSAGE_KEY, msgKey);
        notificationIntent.putExtra(Config.MESSAGE_TEXT, message);
        notificationIntent.putExtra(Config.MESSAGE_URL, msgUrl);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (isAppInForeground(context)) {
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(notificationIntent);
        } else {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = getNotification(context, message, notificationIntent);
            notificationManager.notify(0, notification);
        }

    }

    private static Notification getNotification(Context context, String message, Intent notificationIntent) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);

        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /**
     * Returns true if the app is currently in the foreground.
     * Requires GET_TASKS permission.
     * See: http://stackoverflow.com/questions/5504632/how-can-i-tell-if-android-app-is-running-in-the-foreground
     *
     * @param context application context
     * @return true if the app is currently in the foreground
     */
    static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        return isActivityFound;
    }


    /**
     * Handles extracting key bits of info from the GCM notification message
     */
    private static class IncomingMessageParams {
        public String msgKey;
        public String msgText;
        public String msgUrl;

        /**
         * Extract basic info from intent extras bundle.
         *
         * @param extras extras bundle from GCM message
         * @return new instance of this class
         */
        public static IncomingMessageParams getMessageParamsFromExtras(Bundle extras) {
            IncomingMessageParams params = new IncomingMessageParams();
            // mk, url, t
            params.msgKey = extras.getString("mk", null);
            params.msgText = extras.getString("t", null);
            params.msgUrl = extras.getString("url", null);
            return params;
        }

        public String getMsgText() {
            return msgText == null ? "" : msgText;
        }

        public String getMsgUrl() {
            return msgUrl == null ? "" : msgUrl;
        }

        public String getMsgKey() {
            return msgKey == null ? "" : msgKey;
        }
    }
}
