package com.saberrrc.cmy.common.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActManager {

    public static ArrayList<AppCompatActivity> listOfActivity = new ArrayList<>();

    private ActManager() {
    }
    private static ActManager actManager;

    public static ActManager getInstance() {
        if (actManager == null) {
            synchronized (ActManager.class) {
                if (actManager == null) {
                    actManager = new ActManager();
                }
            }
        }
        return actManager;
    }

    public void addActivity(AppCompatActivity a) {
        listOfActivity.add(a);
    }

    public void removeActivity(AppCompatActivity a) {
        listOfActivity.remove(a);
    }

    public void killActivity(Class<?> cls) {
        for (int i = 0; i < listOfActivity.size(); i++) {
            Activity activity = listOfActivity.get(i);
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    public void killFragment(Class<?>... cls) {
        if (cls == null || cls.length == 0) {
            return;
        }
        for (int k = 0; k < cls.length; k++) {
            for (int i = 0; i < listOfActivity.size(); i++) {
                AppCompatActivity activity = listOfActivity.get(i);
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                if (fragments == null || fragments.size() == 0) {
                    continue;
                }
                for (Fragment fragment : fragments) {
                    if (fragment == null) {
                        continue;
                    }
                    if (fragment.getClass().equals(cls[k])) {
                        activity.finish();
                    }
                }
            }
        }
    }
}