/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buddha.imagegallary.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.buddha.imagegallary.R;

public class ActivityUtils {

    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivityKeepHistory(@NonNull FragmentManager fragmentManager,
                                                        @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivityAnimUpDown(@NonNull FragmentManager fragmentManager,
                                                       @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up, R.animator.slide_out_up, R.animator.slide_out_up);
        transaction.replace(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivityKeepHistoryAnimUpDown(@NonNull FragmentManager fragmentManager,
                                                                  @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_up, R.animator.slide_out_up, R.animator.slide_out_down, R.animator.slide_in_down);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivityKeepHistoryAnimFadeInSlideDown(@NonNull FragmentManager fragmentManager,
                                                                           @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, R.animator.slide_out_down, R.animator.slide_in_down);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }
}
