package com.cheng.consult.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
    private ImageButton mFocusBtn;
    private int mUserId;
    //private static int mclickNum = 0;
    //private boolean isFocusExpert = false;
    private int isFocused = 0;//从列表传过来的查询服务器结果，是否是关注的专家

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mUserId = new Long(mApplication.mUserId).intValue();
        mExpertName = (TextView)findViewById(R.id.expert_name);
        mExpDes = (TextView)findViewById(R.id.expert_des);
        mExpGoodAt = (TextView)findViewById(R.id.expert_goodat);
        mWorkYear = (TextView)findViewById(R.id.expert_year);
        mAnswerCount = (TextView)findViewById(R.id.answer_number);
        mExpDetailDes = (TextView)findViewById(R.id.expert_detail_des);
        mExpDetailGoodAt = (TextView)findViewById(R.id.expert_detail_goodat);

        //获取activity传递数据
        String data = getIntent().getStringExtra("expert");
        isFocused = getIntent().getIntExtra("isFocused", 0);

        mFocusBtn = (ImageButton)findViewById(R.id.button_focus);
        //初始化是否关注图标
        if(isFocused == 0){//没有关注
            mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_normal));
        }else {//关注
            mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_pressed));
        }


        Gson gson = new Gson();
        mExpert = gson.fromJson(data, Expert.class);

        //上传关注专家id
        mFocusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpUtils.ResultCallback<String> focusCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        if(isFocused == 0){
                            mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_normal));
                            Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_cancel_focus_expert_toast), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else {
                            mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_pressed));
                            Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_focus_expert_toast), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };
                //mclickNum = mclickNum + 1;
                if(isFocused == 0){//点击前没有关注，则点击为加关注
                    //isFocusExpert = false;
                    isFocused = 1;
//                    mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_pressed));
//                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_focus_expert_toast), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }else {//点击前已关注，则点击为取消关注
                    //isFocusExpert = true;
                    isFocused = 0;
//                    mFocusBtn.setImageDrawable(getDrawable(R.drawable.follow_btn_normal));
//                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_cancel_focus_expert_toast), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
                }

                List<OkHttpUtils.Param> params = new ArrayList<>();
                try {
                    Long expId = mExpert.getId();
                    OkHttpUtils.Param expid = new OkHttpUtils.Param("focusExpertId", String.valueOf(expId));
                    OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","relation");
                    OkHttpUtils.Param userid = new OkHttpUtils.Param("userid",String.valueOf(mUserId));

                    if(isFocused == 1){
                        OkHttpUtils.Param isfocus = new OkHttpUtils.Param("isfocused", "1");
                        params.add(isfocus);
                    }else {
                        OkHttpUtils.Param isfocus = new OkHttpUtils.Param("isfocused","0");
                        params.add(isfocus);
                    }
                    params.add(userid);
                    params.add(expid);
                    params.add(mothed);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //更新关注状态
                String sUrl = Urls.HOST_TEST + Urls.EXPERT;
                OkHttpUtils.post(sUrl, focusCallback, params);
            }
        });

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
