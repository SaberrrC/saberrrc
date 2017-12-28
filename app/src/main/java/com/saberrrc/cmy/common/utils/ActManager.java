package com.saberrrc.cmy.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.saberrrc.cmy.App;

import java.util.List;
import java.util.Stack;

public class ActManager {

    private static Stack<AppCompatActivity> activityStack;
    private static ActManager               instance;

    private ActManager() {
    }

    public static ActManager getInstance() {
        if (instance == null) {
            instance = new ActManager();
        }
        return instance;
    }

    public void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?>... cls) {
        if (cls == null || cls.length == 0) {
            return;
        }
        for (AppCompatActivity activity : activityStack) {
            for (Class<?> cl : cls) {
                if (activity.getClass().equals(cl)) {
                    finishActivity(activity);
                }
            }
        }

    }

    public void killFragment(Class<?>... cls) {
        if (cls == null || cls.length == 0) {
            return;
        }
        for (Class<?> cl : cls) {
            for (AppCompatActivity activity : activityStack) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
                if (fragments == null || fragments.size() == 0) {
                    continue;
                }
                for (Fragment fragment : fragments) {
                    if (fragment == null) {
                        continue;
                    }
                    if (fragment.getClass().equals(cl)) {
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束承܉Activity
     */
    public void finishAllActivity() {
        for (AppCompatActivity appCompatActivity : activityStack) {
            if (appCompatActivity != null) {
                appCompatActivity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(App.getInstance().getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}