package com.example.whatstrending.utils;

import com.example.whatstrending.data.NewsApiWorker;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class WorkUtils {

    private static final String UNIQUE_WORK_GET_TOP_HEADLINES = "get-top-headlines";

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
                .addTag(UNIQUE_WORK_GET_TOP_HEADLINES)
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                UNIQUE_WORK_GET_TOP_HEADLINES,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }

    public static void oneTimeGetAllTopHeadlines() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NewsApiWorker.class)
                .addTag(WorkUtils.UNIQUE_WORK_GET_TOP_HEADLINES)
                .build();

        WorkManager.getInstance().enqueueUniqueWork(
                UNIQUE_WORK_GET_TOP_HEADLINES,
                ExistingWorkPolicy.KEEP,
                workRequest
        );
    }

    public static boolean workHasRunRecently() {
        //TODO: Monitor all WorkRequests and return true if a sync has occured in the last 15 minutes
        //TODO: Use this function to prevent starting an automatic work request if a recent manual refresh was made
        //TODO: Alternatively look into whether the swipe refresh could trigger the schedule work to perform immediately
        return true;
    }
}
