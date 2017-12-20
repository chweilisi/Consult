package com.cheng.consult.ui.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cheng.consult.R;
import com.cheng.consult.app.App;

public abstract class BaseActivity extends AppCompatActivity {

    public App mApplication;
    public Context mContext;
    //public MyReceiver receiver;

    protected abstract int getContentViewLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayoutId());
        mApplication = (App) getApplication();
        mApplication.addActivity(this);
        mApplication.addActivity_(this);
        mContext = this;

        //register broadcast receive
        //registerBroadcast();

        initViews(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
        mApplication.rmActivity_(this);
        mApplication.rmActivity(this);
    }

//    private void registerBroadcast() {
//        // 注册广播接收者
//        receiver = new MyReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("exit_app");
//        mContext.registerReceiver(receiver,filter);
//    }
//
//    class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals("logout_app")){
//                finish();
//            }
//        }
//    }
}
