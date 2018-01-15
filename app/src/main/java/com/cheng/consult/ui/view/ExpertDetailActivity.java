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
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            mFocusBtn.setImageDrawable(getDrawable(R.drawable.btn_love_n));
        }else {//关注
            mFocusBtn.setImageDrawable(getDrawable(R.drawable.btn_love_d));
        }


        final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        mExpert = gson.fromJson(data, Expert.class);

        //上传关注专家id
        mFocusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpUtils.ResultCallback<String> focusCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                        if(result.getResultCode().trim().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS)){
                            if(isFocused == 0){
                                mFocusBtn.setImageDrawable(getDrawable(R.drawable.btn_love_n));
                                Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_cancel_focus_expert_toast), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }else {
                                mFocusBtn.setImageDrawable(getDrawable(R.drawable.btn_love_d));
                                Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_focus_expert_toast), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                            Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                    + getResources().getString(R.string.expert_love_opertion_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                            Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                    + getResources().getString(R.string.expert_love_opertion_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(mContext, "关注专家失败，请检查网络。", Toast.LENGTH_LONG).show();
                    }
                };

                if(isFocused == 0){//点击前没有关注，则点击为加关注
                    isFocused = 1;
                }else {//点击前已关注，则点击为取消关注
                    isFocused = 0;
                }

                //json格式post参数
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateFormat.format(date).toString();

                PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "relation", mApplication.mAppSignature, dateStr, "9000");
                LoveOperationPostParam postParam = new LoveOperationPostParam(postHead, mExpert.getUserId(), String.valueOf(mUserId), String.valueOf(isFocused));
                String strParam = gson.toJson(postParam);

                String sUrl = Urls.HOST_TEST + Urls.EXPERT;
                OkHttpUtils.postJson(sUrl, focusCallback, strParam);
            }
        });

        //set data
        mExpertName.setText(mExpert.getName());
        mExpDetailDes.setText(mExpert.getDes());

        String[] good = getResources().getStringArray(R.array.consult_category);
        StringBuilder sb = new StringBuilder();
        String expertGoodat = mExpert.getGoodField();

        for (int i = 10; i < 23; i++){
            if(expertGoodat.indexOf(String.valueOf(i)) >= 0){
                sb.append(good[i - 10]).append("  ");
            }
        }

        mExpDetailGoodAt.setText(sb.toString());
        mWorkYear.setText(mExpert.getExpertTime() + "年");
        mAnswerCount.setText("10个");


        mAskButton = (Button)findViewById(R.id.expert_detail_ask_button);
        mAskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpertDetailActivity.this, AskExpertActivity.class);
                intent.putExtra("expertid", mExpert.getUserId());
                startActivity(intent);
            }
        });
    }

    class LoveOperationPostParam{
        private PostCommonHead.HEAD head;
        PostBody body;

        public LoveOperationPostParam(PostCommonHead.HEAD head, String expertId, String userId, String isFocused) {
            this.head = head;
            this.body = new PostBody(expertId, userId, isFocused);
        }

        class PostBody{
            private String expertId;
            private String userId;
            private String isFocused;

            public PostBody(String expertId, String userId, String isFocused) {
                this.expertId = expertId;
                this.userId = userId;
                this.isFocused = isFocused;
            }
        }
    }

}
