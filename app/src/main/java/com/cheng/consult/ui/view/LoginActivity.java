package com.cheng.consult.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
        if(1 == pre.getUserIsLogin()){
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

    private void loginPassport(){
        //TODO: 暂时不做网络登录，直接打开mainactivity
/*
        OkHttpUtils.ResultCallback<String> loginCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                if(null != user){
                    pre.setUserLoginName(user.getName());
                    pre.setUserLoginPsw(user.getPassword());
                    pre.setUserId(user.getId());
                    pre.setUserIsLogin(1);

                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
            }
        };
        //Map<String, Object> params = new HashMap<String, Object>();
        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param userName = new OkHttpUtils.Param("username", URLEncoder.encode(strUserName, "UTF-8"));
            OkHttpUtils.Param passWord = new OkHttpUtils.Param("password", URLEncoder.encode(strPassword, "UTF-8"));

            params.add(userName);
            params.add(passWord);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OkHttpUtils.post(Urls.HOST + Urls.LOGIN, loginCallback, params);
*/
        //TODO: for tmp use, login directlly
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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
}
