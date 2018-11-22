package com.buddha.imagegallary.module.home


import com.buddha.imagegallary.di.scope.FragmentScoped

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun mainFragment(): HomeFragment
}
