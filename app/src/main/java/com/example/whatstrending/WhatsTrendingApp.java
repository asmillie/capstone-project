package com.example.whatstrending;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class WhatsTrendingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
