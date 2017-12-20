package com.cheng.consult;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cheng.consult.ui.fragment.ConsultFragment;
import com.cheng.consult.ui.fragment.HomeFragment;
import com.cheng.consult.ui.fragment.InfoFragment;
import com.cheng.consult.ui.view.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initContent();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
                    return true;
                case R.id.navigation_consult:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                    return true;
                case R.id.navigation_info:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new InfoFragment()).commit();
                    return true;
            }
            return false;
        }

    };

    private void initContent(){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new HomeFragment()).commit();
    }

}
