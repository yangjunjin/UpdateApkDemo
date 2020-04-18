package com.heima.updateapkdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;


/**
 * @author yangjunjin
 * @date 2020/4/18
 */
public class DownloadNotifyUtil {
    private static NotificationManager notificationManager;
    private static NotificationCompat.Builder builder;
    private static DownloadNotifyUtil instance;
    private static final int PROGRESS_MAX = 100;
    private static final int notifyId = 2020092710;

    public static DownloadNotifyUtil getInstance() {
        if (instance == null)
            instance = new DownloadNotifyUtil();
        return instance;
    }

    private DownloadNotifyUtil(){
        builder = new NotificationCompat.Builder(App.getContext(), "default_notification_channel_id");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "default_notification_channel_id";
            String description = "default_notification_channel_name";
            int importance = NotificationManager.IMPORTANCE_LOW;
            notificationManager = App.getContext().getSystemService(NotificationManager.class);
            NotificationChannel channel;
            channel = new NotificationChannel("default_notification_channel_id", name, importance);
            channel.setDescription(description);
            channel.setSound(null,null);
            notificationManager.createNotificationChannel(channel);
        }else {
            notificationManager = (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        builder.setContentTitle("Title")
                .setContentText("content")
                .setSound(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW);
    }

    public void showDownloadProgress(int progress){
        builder.setProgress(PROGRESS_MAX, progress, false);
        notificationManager.notify(notifyId, builder.build());
    }

    public void hideDownloadProgress(){
        notificationManager.cancel(notifyId);
    }
}
