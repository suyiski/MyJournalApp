package com.suizexpressions.myjournalapp;

import android.app.Application;

import com.firebase.client.Firebase;

public class MyJournalApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
