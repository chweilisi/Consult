package com.cheng.consult.ui.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.google.gson.Gson;

public class AnswerItemDetailActivity extends BaseActivity {

    private TextView mAnswerDetail;
    private String mAnswer;
    private int mItemType;
    private Subject mSubject;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_item_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAnswerDetail = (TextView)findViewById(R.id.tv_answer_item_detail_page);
        mAnswer = getIntent().getStringExtra("answer_content");
        mItemType = getIntent().getIntExtra("item_type", -1);

        mAnswerDetail.setText(mAnswer);
        setSupportActionBar(toolbar);
        if(0 == mItemType){
            getSupportActionBar().setTitle("问题详情");
        } else {
            getSupportActionBar().setTitle("回答详情");
        }
    }

}
