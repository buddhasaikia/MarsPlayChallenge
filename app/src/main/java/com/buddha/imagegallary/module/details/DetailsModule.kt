package com.buddha.imagegallary.module.details

import com.buddha.imagegallary.di.scope.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun detailsFragment(): DetailsFragment
}
