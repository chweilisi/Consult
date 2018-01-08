package com.cheng.consult.ui.view;

import android.content.Context;
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
import com.cheng.consult.db.table.User;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.cheng.consult.utils.StringUtils;
import com.cheng.consult.widget.PwdShowLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mLoginBtn;
    private TextView mToastTip;
    private EditText mUserName;
    private PwdShowLayout mPassword;

    private String strUserName;
    private String strPassword;
    private PreUtils pre;
    private TextView mTvRegist;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        pre = PreUtils.getInstance(mContext);
        if(1 == pre.getUserIsLogin()){
            strUserName = pre.getUserLoginName();
            strPassword = pre.getUserLoginPsw();
            loginPassport();
        } else {
            mTvRegist = (TextView)findViewById(R.id.tv_regist_user);
            mTvRegist.setOnClickListener(this);

            mLoginBtn = (Button)findViewById(R.id.sign_in_btn);
            mToastTip = (TextView) findViewById(R.id.toast_tip);

            mUserName = (EditText) findViewById(R.id.login_username);
            String userName = pre.getUserLoginName();
            if (!StringUtils.isEmpty(userName)) {
                mUserName.setText(userName);
            }
            mPassword = (PwdShowLayout) findViewById(R.id.login_password_layout);
            String pswd = pre.getUserLoginPsw();
            if (!StringUtils.isEmpty(pswd)) {
                mPassword.setPwdText(pswd);
            }

            mLoginBtn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        updateStateTV(-1);
        View focusView = null;
        boolean cancel = false;
        switch (v.getId()){
            case R.id.sign_in_btn:
            {
                updateStateTV(-1);
                //规范性检测
                strUserName = mUserName.getText().toString();
                strPassword = mPassword.getPwdText();
                if (TextUtils.isEmpty(strUserName)) {
                    updateStateTV(R.string.login_hint_phonenumber);
                    focusView = mUserName;
                    cancel = true;
                    return;
                }else if(TextUtils.isEmpty(strPassword)){
                    updateStateTV(R.string.login_tip_password);
                    return;
                }
                //登录
                loginPassport();
                break;
            }
            case R.id.tv_regist_user:
            {
                Intent intent = new Intent();
                intent.setClass(mContext, RegistActivity.class);
                startActivity(intent);
                finish();
                break;
            }

        }
    }

    String userType = "";
    boolean isLoginSuccess = false;
    private void loginPassport(){

        OkHttpUtils.ResultCallback<String> loginCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //format return json
                PostResponseBodyJson result;
                LoginResultJsonBean loginStatus;
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                if(null != response && !response.isEmpty()){
                    result = gson.fromJson(response, PostResponseBodyJson.class);
                    if((result.getResultCode().trim().equalsIgnoreCase("200") && null != result.getResultJson()) && (!result.getResultJson().isEmpty())){
                        loginStatus = gson.fromJson(result.getResultJson(), LoginResultJsonBean.class);

                        userType = loginStatus.getUserType();
                        isLoginSuccess = result.getResultCode().trim().equalsIgnoreCase("200") ? true : false;
                        if(100 != Integer.parseInt(userType) || !isLoginSuccess) {
                            Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.login_hint_error_user), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            //save login user info
                            pre.setUserLoginName(loginStatus.getLoginName());
                            pre.setUserLoginPsw(loginStatus.getLoginPsw());
                            pre.setUserLoginId(loginStatus.getLoginId());
                            pre.setUserIsManager(loginStatus.getIsManager());
                            pre.setUserIsLogin(1);
                            pre.setUserType(userType);
                            //set userid to App, so, other activity can user it
                            if(!loginStatus.getUserId().trim().isEmpty() && !loginStatus.getUserId().trim().equalsIgnoreCase("-1")){
                                mApplication.mUserId = Integer.parseInt(loginStatus.getUserId());
                                pre.setUserId(Long.parseLong(loginStatus.getUserId()));
                            }

                            //start mainactivity
                            Intent intent = new Intent();
                            intent.setClass(mContext, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else if (result.getResultCode().trim().equalsIgnoreCase("100")){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if (result.getResultCode().trim().equalsIgnoreCase("500")){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mContext, getResources().getText(R.string.login_hint_net_error), Toast.LENGTH_LONG).show();
            }
        };
        //Map<String, Object> params1 = new HashMap<String, Object>();
//        List<OkHttpUtils.Param> params = new ArrayList<>();
//        try {
//
//            //params1.put("username", URLEncoder.encode(strUserName, "UTF-8"));
//            //params1.put("password", URLEncoder.encode(strPassword, "UTF-8"));
//            //params1.put("format", "json");
//
//            OkHttpUtils.Param userName = new OkHttpUtils.Param("username", URLEncoder.encode(strUserName, "UTF-8"));
//            OkHttpUtils.Param passWord = new OkHttpUtils.Param("password", URLEncoder.encode(strPassword, "UTF-8"));
//            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","login");
//            //OkHttpUtils.Param id = new OkHttpUtils.Param("id","");
//
//            params.add(userName);
//            params.add(passWord);
//            params.add(mothed);
//            //params.add(id);
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        //OkHttpUtils.post(Urls.HOST_TEST + Urls.USER, loginCallback, params);

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();
        LoginPostParam loginJson = new LoginPostParam("1", "login", mApplication.mAppSignature, dateStr, "9000",
                String.valueOf(pre.getUserId()), strUserName, strPassword);
        String loginParam = new Gson().toJson(loginJson);
        //请求数据
        OkHttpUtils.postJson(Urls.HOST_TEST + Urls.LOGIN, loginCallback, loginParam);
    }

    class LoginPostParam{
        private PostCommonHead.HEAD head;
        private BODY body;
        public LoginPostParam(String signkey, String method, String signature, String requesttime, String appid,
                              String loginid, String loginname, String loginpass) {
            this.head = new PostCommonHead.HEAD(signkey, method, signature, requesttime, appid);
            this.body = new BODY(loginid, loginname, loginpass);
        }

        class BODY{
            private String loginid;
            private String loginname;
            private String loginpass;

            public BODY(String loginid, String loginname, String loginpass) {
                this.loginid = loginid;
                this.loginname = loginname;
                this.loginpass = loginpass;
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

    private void updateStateTV(String str) {
        if (TextUtils.isEmpty(str)) {
            mToastTip.setVisibility(View.INVISIBLE);
        } else {
            mToastTip.setVisibility(View.VISIBLE);
            mToastTip.setText(str);
        }
    }

    class LoginResultJsonBean{
        private String isManager;
        private String loginName;
        private String loginId;
        private String loginPsw;
        private String userType;
        private String userId;

        public String getIsManager() {
            return isManager;
        }

        public void setIsManager(String isManager) {
            this.isManager = isManager;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getLoginPsw() {
            return loginPsw;
        }

        public void setLoginPsw(String loginPsw) {
            this.loginPsw = loginPsw;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    class loginResultBean{
        String LoginName;
        String Password;
        String Id;
        boolean LoginSuccess;
        String UserType;

        public String getLoginName() {
            return LoginName;
        }

        public void setLoginName(String loginName) {
            LoginName = loginName;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public boolean isLoginSuccess() {
            return LoginSuccess;
        }

        public void setLoginSuccess(boolean loginSuccess) {
            LoginSuccess = loginSuccess;
        }

        public String getUserType() {
            return UserType;
        }

        public void setUserType(String userType) {
            UserType = userType;
        }
    }
}
