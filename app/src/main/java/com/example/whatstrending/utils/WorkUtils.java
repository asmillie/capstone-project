package com.example.whatstrending.utils;

import com.example.whatstrending.data.NewsApiWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class WorkUtils {

    private WorkUtils() {}

    public static void scheduleGetAllTopHeadlines() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NewsApiWorker.class, 30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(workRequest);
    }

    public static boolean workHasRunRecently() {
        //TODO: Monitor all WorkRequests and return true if a sync has occured in the last 15 minutes
        //TODO: Use this function to prevent starting an automatic work request if a recent manual refresh was made
        //TODO: Alternatively look into whether the swipe refresh could trigger the schedule work to perform immediately
        return true;
    }
}
