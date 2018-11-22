package com.buddha.imagegallary.module.upload


import com.buddha.imagegallary.di.scope.FragmentScoped

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UploadModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun uploadFragment(): UploadFragment
}
