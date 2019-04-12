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

    private static final String UNIQUE_PERIODIC_WORK_GET_TOP_HEADLINES = "get-top-headlines";
    private static final String UNIQUE_ONE_TIME_WORK_GET_TOP_HEADLINES = "get-top-headlines-once";

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

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                UNIQUE_PERIODIC_WORK_GET_TOP_HEADLINES,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
        );
    }

    public static void oneTimeGetAllTopHeadlines() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NewsApiWorker.class)
                .build();

        WorkManager.getInstance().enqueueUniqueWork(
                UNIQUE_ONE_TIME_WORK_GET_TOP_HEADLINES,
                ExistingWorkPolicy.KEEP,
                workRequest
        );
    }
}
