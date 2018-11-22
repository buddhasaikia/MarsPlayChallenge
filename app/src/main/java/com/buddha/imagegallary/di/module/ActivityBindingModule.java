package com.buddha.imagegallary.di.module;


import com.buddha.imagegallary.di.scope.PerActivity;
import com.buddha.imagegallary.module.details.DetailsActivity;
import com.buddha.imagegallary.module.details.DetailsModule;
import com.buddha.imagegallary.module.home.HomeActivity;
import com.buddha.imagegallary.module.home.HomeModule;
import com.buddha.imagegallary.module.upload.UploadActivity;
import com.buddha.imagegallary.module.upload.UploadModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @PerActivity
    @ContributesAndroidInjector(modules = HomeModule.class)
    abstract HomeActivity homeActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = DetailsModule.class)
    abstract DetailsActivity detailsActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = UploadModule.class)
    abstract UploadActivity uploadActivity();
}
