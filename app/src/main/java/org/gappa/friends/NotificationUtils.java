package org.gappa.friends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

public class NotificationUtils {
    private static int TEST_NOTIFICATION_ID = 1234;
    private static final String TEST_NOTIFICATION_CHANNEL_ID = "test_notification_channel";

    public static void notify(Context context, String text) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    TEST_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        String notificationTitle = context.getString(R.string.app_name);

        /*
         * NotificationCompat Builder is a very convenient way to build backward-compatible
         * notifications. In order to use it, we provide a context and specify a color for the
         * notification, a couple of different icons, the title for the notification, and
         * finally the text of the notification, which in our case in a summary of today's
         * forecast.
         */
    //          COMPLETED (2) Use NotificationCompat.Builder to begin building the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, TEST_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_stat_name)
//                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(text)
                .setAutoCancel(true);

        //          COMPLETED (3) Create an Intent with the proper URI to start the DetailActivity
        /*
         * This Intent will be triggered when the user clicks the notification. In our case,
         * we want to open Sunshine to the DetailActivity to display the newly updated weather.
         */
        Intent detailIntentForToday = new Intent(context, MainActivity.class);

        //          COMPLETED (4) Use TaskStackBuilder to create the proper PendingINtent
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
        PendingIntent resultPendingIntent = taskStackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    //          COMPLETED (5) Set the content Intent of the NotificationBuilder
                notificationBuilder.setContentIntent(resultPendingIntent);

        //          COMPLETED (6) Get a reference to the NotificationManager

    //          COMPLETED (7) Notify the user with the ID WEATHER_NOTIFICATION_ID
        /* WEATHER_NOTIFICATION_ID allows you to update or cancel the notification later on */
                notificationManager.notify(TEST_NOTIFICATION_ID, notificationBuilder.build());
    }
}
