package com.silhocompany.ideo.knews.Notification;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Samuel on 30/05/2017.
 */

public class OldFactReminderIntentService extends IntentService {


    public OldFactReminderIntentService() {
        super("OldFactReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);}
    }
}