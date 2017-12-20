package com.cheng.consult.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.google.gson.Gson;

public class ExpertDetailActivity extends BaseActivity {
    private Button mAskButton;
    private Expert mExpert;
    private TextView mExpertName;
    private TextView mExpDes;
    private TextView mExpGoodAt;
    private TextView mWorkYear;
    private TextView mAnswerCount;
    private TextView mExpDetailDes;
    private TextView mExpDetailGoodAt;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mExpertName = (TextView)findViewById(R.id.expert_name);
        mExpDes = (TextView)findViewById(R.id.expert_des);
        mExpGoodAt = (TextView)findViewById(R.id.expert_goodat);
        mWorkYear = (TextView)findViewById(R.id.expert_year);
        mAnswerCount = (TextView)findViewById(R.id.answer_number);
        mExpDetailDes = (TextView)findViewById(R.id.expert_detail_des);
        mExpDetailGoodAt = (TextView)findViewById(R.id.expert_detail_goodat);

        //获取activity传递数据
        String data = getIntent().getStringExtra("expert");
        Gson gson = new Gson();
        mExpert = gson.fromJson(data, Expert.class);

        //set data
        mExpertName.setText(mExpert.getName());
        mExpDetailDes.setText(mExpert.getDes());
        mExpDetailGoodAt.setText(mExpert.getGoodField());
        mWorkYear.setText(mExpert.getExpertTime() + "年");
        mAnswerCount.setText("10个");


        mAskButton = (Button)findViewById(R.id.expert_detail_ask_button);
        mAskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpertDetailActivity.this, AskExpertActivity.class);
                startActivity(intent);
            }
        });
    }

}
