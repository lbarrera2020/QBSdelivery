package com.example.proyectoelectiva3;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

public class QbsAppFireBaseOffline extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
