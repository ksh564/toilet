package org.androidtown.findrest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.androidtown.findrest.other.watch_member;

/**
 * Created by 김승훈 on 2017-03-22.
 */
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    static String user_id, event_enroll_time;
    static int caseby;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
//        String click_action = remoteMessage.getNotification().getClickAction();


        System.out.println("데이터수신전");
        user_id = remoteMessage.getData().get("user_id");
        event_enroll_time = remoteMessage.getData().get("event_time");
        System.out.println("데이터수신후" + user_id);
        System.out.println("데이터수신후2" + remoteMessage.getFrom());
        System.out.println("데이터수신후3" + remoteMessage.getData().get("event_time"));
        System.out.println("데이터수신후4" + remoteMessage.getData().get("case"));
        String caseto= remoteMessage.getData().get("case");
        caseby= Integer.parseInt(caseto);

        // 이벤트 등록할때 이뤄지는것

        sendNotification(remoteMessage.getData().get("message"), user_id, event_enroll_time,caseby);
        //이벤트 수락시 이벤트

        //이벤트 거절시 이벤트


    }

    private void sendNotification(String messageBody, String user_id, String event_enroll_time,int caseby) {

        if(caseby==0){
                Intent intent = new Intent(this, watch_event.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("event_enroll_time", event_enroll_time);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("이벤트 알리미")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }else if(caseby==1){
                Intent intent = new Intent(this,watch_member.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("event_enroll_time", event_enroll_time);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("이벤트 알리미")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }else if(caseby==2){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("event_enroll_time", event_enroll_time);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("이벤트 알리미")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }

        }



    }

