package com.silhocompany.ideo.knews.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Activities.OldFactActivity;


/**
 * Created by Samuel on 30/05/2017.
 */

public class NotifcationsUtils {

    private static final int FACT_REMINDER = 123;
    private static final int NOTIFICATION_FACT_ID = 122;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 121;
    private static final int ACTION_SHARE_PENDING_INTENT_ID = 120;

    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss";
    public static final String ACTION_SHARE_FACT = "share";

    private static String mFact;
    private static String mDate;

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void reminderFact(Context context, String fact, String date){

        mFact = fact;
        mDate = date;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_hourglass_full_black_24dp)
                .setContentTitle(context.getString(R.string.was_today_label))
                .setStyle(new NotificationCompat.BigTextStyle().bigText("In " + date + " " + fact))
                .setContentText("In " + date + " " + fact)
                .addAction(ignore(context))
                .addAction(share(context))
                .addAction(more(context))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_FACT_ID, builder.build());
    }

    private static NotificationCompat.Action more(Context context){
        Intent moreIntent = new Intent(context, OldFactActivity.class);
        PendingIntent morePendingItnent = PendingIntent.getActivity(context, FACT_REMINDER, moreIntent ,0);
        NotificationCompat.Action moreAction = new NotificationCompat.Action(R.drawable.ic_filter_9_plus_black_24dp,
                context.getString(R.string.more), morePendingItnent);
        clearAllNotifications(context);
        return moreAction;
    }

    private static NotificationCompat.Action ignore(Context context){
        Intent ignoreItent = new Intent(context, OldFactReminderIntentService.class);
        ignoreItent.setAction(ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignorePendingIntent = PendingIntent.getService(
                context, ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreItent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreAction = new NotificationCompat.Action(R.drawable.ic_delete_black_24dp,
                context.getString(R.string.ignore), ignorePendingIntent);
        return ignoreAction;
    }

    private static NotificationCompat.Action share(Context context){
        Intent shareIntent = new Intent(context, OldFactReminderIntentService.class);
        shareIntent.setAction(ACTION_SHARE_FACT);
        PendingIntent sharePendingIntent = PendingIntent.getService(
                context, ACTION_SHARE_PENDING_INTENT_ID,
                shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action shareAction = new NotificationCompat
                .Action(R.drawable.ic_menu_share, context.getString(R.string.share), sharePendingIntent);
        return shareAction;

    }

    public static void shareFactNotification(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.today_in) +
                mDate + context.getString(R.string.was) + mFact);
        shareIntent.setType("text/plain");
        Intent intentChooser = Intent.createChooser(shareIntent, context.getString(R.string.send_to));
        intentChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentChooser);
        clearAllNotifications(context);
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
