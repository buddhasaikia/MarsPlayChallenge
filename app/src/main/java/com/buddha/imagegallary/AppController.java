package com.buddha.imagegallary;

import com.buddha.imagegallary.di.components.DaggerAppComponent;
import com.cloudinary.android.MediaManager;
import com.facebook.stetho.Stetho;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class AppController extends DaggerApplication {
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
        MediaManager.init(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}
