package com.identitymanager.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.identitymanager.utilities.notifications.PushNotification;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        PushNotification pushNotification = new PushNotification(getApplicationContext());
        pushNotification.sendNotification("Your password need to be updated");
        return Result.success();
    }
}
