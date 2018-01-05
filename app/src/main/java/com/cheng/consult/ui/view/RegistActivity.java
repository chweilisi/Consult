package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.MainActivity;
import com.cheng.consult.R;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.cheng.consult.widget.PwdShowLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistActivity extends BaseActivity implements View.OnClickListener {
    private EditText userName;
    private PwdShowLayout passWord;
    private String mUserName;
    private String mPassword;
    private Button mbtnRegist;
    private TextView mToastTip;
    private PreUtils pre;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        pre = PreUtils.getInstance(mContext);
        userName = (EditText)findViewById(R.id.regist_username);
        passWord = (PwdShowLayout)findViewById(R.id.regist_password_layout);
        mToastTip = (TextView) findViewById(R.id.toast_tip);
        mbtnRegist = (Button)findViewById(R.id.regist_btn);
        mbtnRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist_btn:
            {
                mUserName = userName.getText().toString();
                mPassword = passWord.getPwdText();
                if (TextUtils.isEmpty(mUserName)) {
                    updateStateTV(R.string.login_hint_phonenumber);
                    return;
                }else if(TextUtils.isEmpty(mPassword)){
                    updateStateTV(R.string.register_tip_password);
                    return;
                }
                registUser();
                break;
            }
        }
    }

    String userType = "";
    boolean isLoginSuccess = false;
    private void registUser(){
        OkHttpUtils.ResultCallback<String> registCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //format return json
                PostResponseBodyJson result;
                registResultBean registResult;
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                if(null != response && !response.isEmpty()){
                    result = gson.fromJson(response, PostResponseBodyJson.class);
                    String status = result.getResultJson();
                    if((null != status) && (!status.isEmpty())){
                        registResult = gson.fromJson(status, registResultBean.class);

                        userType = registResult.getUserType();
                        isLoginSuccess = result.getResultCode().equalsIgnoreCase("200") ? true : false;
                        if(100 == Integer.parseInt(userType) && isLoginSuccess) {
                            //save regist user info to local
                            pre.setUserLoginName(registResult.getLoginName());
                            pre.setUserLoginPsw(registResult.getLoginPsw());
                            pre.setLoginId(Long.parseLong(registResult.getLoginId()));
                            pre.setUserType(userType);
                            pre.setUserIsLogin(1);

                            //start mainactivity
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mContext, getResources().getText(R.string.login_hint_net_error), Toast.LENGTH_LONG).show();
            }
        };

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();

        //init request parameter
        registPostParam registJsonBean = new registPostParam("1", "register",  mApplication.mAppSignature, dateStr, "9000", mUserName, mPassword, "100");
        String registParam = new Gson().toJson(registJsonBean);

        //request regist
        OkHttpUtils.postJson(Urls.HOST_TEST + Urls.LOGIN, registCallback, registParam);
    }

    class registResultBean{
        private String loginName;
        private String loginPsw;
        private String loginId;
        private String userId;
        private String userType;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getLoginPsw() {
            return loginPsw;
        }

        public void setLoginPsw(String loginPsw) {
            this.loginPsw = loginPsw;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }
    }

    class registPostParam{
        private PostCommonHead.HEAD head;
        private registBody body;

        public registPostParam(String signkey, String method, String signature, String requesttime, String appid,
                               String loginname, String loginpsw, String usertype) {
            this.head = new PostCommonHead.HEAD(signkey, method, signature, requesttime, appid);
            this.body = new registBody(loginname, loginpsw, usertype);
        }

        class registBody{
            private String loginName;
            private String loginPass;
            private String userType;

            public registBody(String loginName, String loginPass, String userType) {
                this.loginName = loginName;
                this.loginPass = loginPass;
                this.userType = userType;
            }
        }
    }

    private void updateStateTV(int strID) {
        if (strID <= 0) {
            mToastTip.setVisibility(View.INVISIBLE);
        } else {
            mToastTip.setVisibility(View.VISIBLE);
            mToastTip.setText(strID);
        }
    }
}
