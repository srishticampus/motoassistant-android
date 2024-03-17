package com.project.motoassistant;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.application.isradeleon.notify.Notify;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class FireBaseCloudMessageService extends FirebaseMessagingService {


    private String category, subject, courseId, company, vacancy, placement, job, post, examCategory, name;
    String title, message, from, img;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i("Notification", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());
//            Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                // handleNow();
            }

        }


        try {
            issueNotification(remoteMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void scheduleJob() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void issueNotification(RemoteMessage remoteMessage) throws JSONException {


//        // make the channel. The method has been discussed before.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
//        }
//        // the check ensures that the channel will only be made
//        // if the device is running Android 8+
//
//        NotificationCompat.Builder notification =
//                new NotificationCompat.Builder(this, "CHANNEL_1");
//        // the second parameter is the channel id.
//        // it should be the same as passed to the makeNotificationChannel() method
//
//
//        try {
//            Map<String, String> params = remoteMessage.getData();
//            JSONObject object = new JSONObject(params);
//            //JSONObject Customer_details = object.getJSONObject("Customer_details");
//
//
//            title = object.getString("title");
//            message = object.getString("message");
//
//
//
//            Intent resultIntent = new Intent(this, LoginActivity.class);
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            stackBuilder.addNextIntentWithParentStack(resultIntent);
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
//                    R.drawable.cattle_icon);
//            notification
//                    .setLargeIcon(icon)
//                    .setSmallIcon(R.drawable.cattle_icon) // can use any other icon
//                    .setContentTitle("New Message Received ")
//                    //change this to order received.
//                    .setContentText("Title : " + title)
//                    //setContentText("ORder List")
//                    //.addAction(R.drawable.ic_launcher_background,"qwertyuio")
//                    .setContentIntent(resultPendingIntent)
//                    .setNumber(3) // this shows a number in the notification dots
//                    .setAutoCancel(true);
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//            assert notificationManager != null;
//            notificationManager.notify(1, notification.build());
//
//        }catch (Exception e){
//
//        }


        try {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            {

                title = "Request "+object.getString("status");
                message = "Staff Name " + object.getString("Staff_name");
                Notify.build(this)
                        .setTitle(title)
                        .setContent(message)
                        .setSmallIcon(R.drawable.notification_logo)
                        // .setLargeIcon(img)
                        .largeCircularIcon()
                        // .setPicture()
                        .setColor(R.color.login_bt_bg_color)
                        // .setAction(intent)
                        .show();
            }


        } catch (Exception e) {
            Log.e("ERROR IN PUSH", String.valueOf(e));
        }


    }


    @Override
    public void onNewToken(String token) {
        Log.i("TAG", "Refreshed token: " + token);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
        Log.i("token", token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

}
