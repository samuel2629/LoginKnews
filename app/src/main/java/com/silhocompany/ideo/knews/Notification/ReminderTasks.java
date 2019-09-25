package com.silhocompany.ideo.knews.Notification;

import android.content.Context;

/**
 * Created by Samuel on 30/05/2017.
 */

public class ReminderTasks {


    public static void executeTask(Context context, String action){
        if (NotifcationsUtils.ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotifcationsUtils.clearAllNotifications(context);
        } else if(NotifcationsUtils.ACTION_SHARE_FACT.equals(action)){
            NotifcationsUtils.shareFactNotification(context);
            NotifcationsUtils.clearAllNotifications(context);
        }
    }
}