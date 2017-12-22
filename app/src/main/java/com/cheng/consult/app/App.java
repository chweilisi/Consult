package com.cheng.consult.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.util.Log;

import com.cheng.consult.utils.PreUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cheng on 2017/12/6.
 */

public class App extends MultiDexApplication {
    public static App mApp;
    private DisplayMetrics mDisplayMetrics;
    private static float mDensity;
    private static float mScaledDensity;
    private static int mWidth;
    private static int mHeight;
    public static PreUtils mUserInfo;
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        mUserInfo = PreUtils.getInstance(getApplication());
        runningActivity = new ArrayList<WeakReference<Activity>>();
        activityList = new ArrayList<>();
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        mDisplayMetrics = getResources().getDisplayMetrics();
        mDensity = mDisplayMetrics.density;
        mScaledDensity = mDisplayMetrics.scaledDensity;
        mHeight = mDisplayMetrics.heightPixels;
        mWidth = mDisplayMetrics.widthPixels;

    }

    private List<WeakReference<Activity>> runningActivity;

    public void addActivity(Activity activity) {
        runningActivity.add(new WeakReference<Activity>(activity));
    }

    public void rmActivity(Activity activity) {
        Iterator<WeakReference<Activity>> iterator = runningActivity.iterator();
        while (iterator.hasNext()) {
            WeakReference<Activity> reference = iterator.next();
            if (activity.equals(reference.get())) {
                reference.clear();
                iterator.remove();
                break;
            }
        }
    }

    private List<Activity> activityList;

    public void addActivity_(Activity activity){
        activityList.add(activity);
    }

    public void rmActivity_(Activity activity){
        if(activityList.contains(activity)){
            activityList.remove(activity);
            activity.finish();
        }
    }

    public void rmAllActivity_(){
        for(Activity activity : activityList){
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static App getApplication() {
        return mApp;
    }

    public static int getWidth() {
        return mWidth;
    }

    public static int getHeight() {
        return mHeight;
    }

    public static float getDensity() {
        return mDensity;
    }

    public static float getScaledDensity() {
        return mScaledDensity;
    }
}
