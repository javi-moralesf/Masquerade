package com.moralesf.masquerade;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.moralesf.masquerade.android.chat.ChatActivity;
import com.moralesf.masquerade.android.data.MasqueradeContract;

import java.util.List;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty() && GoogleCloudMessaging.
                MESSAGE_TYPE_MESSAGE.equals(messageType)) {

            // Post notification of received message.
            String type = extras.getString("type");

            if(type.equals("notify_mask_users")){
                String key = extras.getString("key");
                Integer api_id = Integer.valueOf(extras.getString("mask_id"));
                String users_string = extras.getString("users");
                String[] users = users_string.split(",");

                updateApiId(key, api_id);

                sendNotification(getString(R.string.chat_user_added), key, 0, MasqueradeContract.ChatEntry.CHAT_TYPE_SYSTEM);
                for (String user_id : users){
                    sendNotification(getString(R.string.chat_initial_message), key, Integer.valueOf(user_id), 0);
                }

            }
            else if(type.equals("chat")){
                String message = extras.getString("message");
                String key = extras.getString("key");
                int user_id = Integer.valueOf(extras.getString("user_id"));

                sendNotification(message, key, user_id, 0);
            }


        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void updateApiId(String key, int api_id) {
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.MaskEntry.COLUMN_API_ID, api_id);

        String where = MasqueradeContract.MaskEntry.TABLE_NAME+"."+MasqueradeContract.MaskEntry.COLUMN_KEYGEN+"='"+key+"'";
        contentResolver.update(MasqueradeContract.MaskEntry.CONTENT_URI, values, where, null);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String key, int user_id, int chat_type) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        ContentResolver contentResolver = this.getContentResolver();

        Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/key/" + key),
                null, null, null, null);
        c.moveToFirst();

        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, c.getLong(0));
        values.put(MasqueradeContract.ChatEntry.COLUMN_TYPE, chat_type);
        values.put(MasqueradeContract.ChatEntry.COLUMN_TEXT, msg);
        values.put(MasqueradeContract.ChatEntry.COLUMN_USER_ID, user_id);
        values.put(MasqueradeContract.ChatEntry.COLUMN_SYNC, 1);
        contentResolver.insert(MasqueradeContract.ChatEntry.CONTENT_URI, values);

        String title = c.getString(MasqueradeContract.MaskEntry.INDEX_COLUMN_TITLE);
        long mask_id = c.getLong(MasqueradeContract.MaskEntry.INDEX_COLUMN_ID);
        int mask_api_id = c.getInt(MasqueradeContract.MaskEntry.INDEX_COLUMN_API_ID);
        Intent intent = ChatActivity.createIntent(this, title, key, mask_id, mask_api_id);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();

        if(!isForeground("com.moralesf.masquerade") || !isScreenOn){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_masquerade_notification)
                            .setContentTitle(title)
                            .setColor(getResources().getColor(R.color.primary))
                            .setSound(alarmSound)
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                            .setContentText(msg);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    public boolean isForeground(String myPackage) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        return componentInfo.getPackageName().equals(myPackage);
    }

}