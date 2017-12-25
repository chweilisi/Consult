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
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.cheng.consult.utils.StringUtils;
import com.cheng.consult.widget.PwdShowLayout;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        pre = PreUtils.getInstance(mContext);
        if(false/*1 == pre.getUserIsLogin()*/){
            strUserName = pre.getUserLoginName();
            strPassword = pre.getUserLoginPsw();
            loginPassport();
        } else {
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
                updateStateTV(-1);
                //规范性检测
                strUserName = mUserName.getText().toString();
                strPassword = mPassword.getPwdText();
                if (TextUtils.isEmpty(strUserName)) {
                    updateStateTV(R.string.login_hint_phonenumber);
                    focusView = mUserName;
                    cancel = true;
                    break;
                }
                //登录
                loginPassport();
        }
    }

    String userType = "";
    boolean isLoginSuccess = false;
    private void loginPassport(){
        //TODO: 暂时不做网络登录，直接打开mainactivity

        OkHttpUtils.ResultCallback<String> loginCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                loginResultBean user = gson.fromJson(response, loginResultBean.class);

                userType = user.getUserType();
                isLoginSuccess = user.isLoginSuccess();
                if(null != user){
                    //TODO: for tmp use, login directlly
                    if(100 != Integer.parseInt(userType) || !isLoginSuccess){
                        Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.login_hint_errorusertype), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        //save login user info
                        pre.setUserLoginName(user.getLoginName());
                        pre.setUserLoginPsw(user.getPassword());
                        pre.setUserId(Long.parseLong(user.getId()));
                        pre.setUserIsLogin(1);
                        pre.setUserType(userType);
                        //set userid
                        mApplication.mUserId = Integer.parseInt(user.getId());

                        //start mainactivity
                        Intent intent = new Intent();//new Intent(this, MainActivity.class);
                        intent.setClass(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
            }
        };
        //Map<String, Object> params1 = new HashMap<String, Object>();
        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {

            //params1.put("username", URLEncoder.encode(strUserName, "UTF-8"));
            //params1.put("password", URLEncoder.encode(strPassword, "UTF-8"));
            //params1.put("format", "json");

            OkHttpUtils.Param userName = new OkHttpUtils.Param("username", URLEncoder.encode(strUserName, "UTF-8"));
            OkHttpUtils.Param passWord = new OkHttpUtils.Param("password", URLEncoder.encode(strPassword, "UTF-8"));
            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","login");
            //OkHttpUtils.Param id = new OkHttpUtils.Param("id","");

            params.add(userName);
            params.add(passWord);
            params.add(mothed);
            //params.add(id);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post(Urls.HOST_TEST + Urls.USER, loginCallback, params);



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
