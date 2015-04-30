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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.moralesf.masquerade.android.Chat.ChatActivity;
import com.moralesf.masquerade.android.List.ListActivity;
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
            String message = extras.getString("message");
            int mask_id = Integer.valueOf(extras.getString("mask_id"));
            int user_id = Integer.valueOf(extras.getString("user_id"));
            sendNotification(message, mask_id, user_id);
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, int mask_api_id, int user_id) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        ContentResolver contentResolver = this.getContentResolver();

        Cursor c = contentResolver.query(Uri.parse(MasqueradeContract.MaskEntry.CONTENT_URI + "/" + mask_api_id),
                null, null, null, null);
        c.moveToFirst();



        ContentValues values = new ContentValues();
        values.put(MasqueradeContract.ChatEntry.COLUMN_MASK_ID, c.getLong(0));
        values.put(MasqueradeContract.ChatEntry.COLUMN_MINE, 0);
        values.put(MasqueradeContract.ChatEntry.COLUMN_TEXT, msg);
        values.put(MasqueradeContract.ChatEntry.COLUMN_USER_ID, user_id);
        contentResolver.insert(MasqueradeContract.ChatEntry.CONTENT_URI, values);

        String title = c.getString(MasqueradeContract.MaskEntry.INDEX_COLUMN_TITLE);
        String key = c.getString(MasqueradeContract.MaskEntry.INDEX_COLUMN_KEYGEN);
        long mask_id = c.getLong(MasqueradeContract.MaskEntry.INDEX_COLUMN_ID);
        Intent intent = ChatActivity.createIntent(this, title, key, mask_id, mask_api_id);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if(!isForeground("com.moralesf.masquerade")){
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_masquerade_notification)
                            .setContentTitle(title)
                            .setColor(getResources().getColor(R.color.primary))
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