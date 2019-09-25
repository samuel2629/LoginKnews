package com.silhocompany.ideo.knews.Notification.Utilities;

import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.silhocompany.ideo.knews.Notification.Services.MyJobService;

/**
 * Created by Samuel on 30/05/2017.
 */

public class ReminderUtilities {

    private static final String FACT_REMINDER_JOB = "FACT_REMINDER_JOB";

    synchronized public static void scheduleFactReminder(Context context){
    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    Job myJob = dispatcher.newJobBuilder()
            .setService(MyJobService.class)
            .setTag(FACT_REMINDER_JOB)
            .setRecurring(true)
            .setLifetime(Lifetime.FOREVER)
            .setTrigger(Trigger.executionWindow(0, 86500))
            .setReplaceCurrent(false)
            .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
            .build();

        dispatcher.schedule(myJob);}
}
