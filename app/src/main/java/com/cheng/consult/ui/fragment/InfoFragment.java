package com.cheng.consult.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.view.LoginActivity;
import com.cheng.consult.ui.view.MyConsultQuestionActivity;
import com.cheng.consult.ui.view.MyLoveExpertListActivity;
import com.cheng.consult.ui.view.MyProfileActivity;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cheng.consult.app.App.mApp;

/**
 * Created by cheng on 2017/11/13.
 */

public class InfoFragment extends Fragment {
    private LinearLayout mMyConsultQuestion;
    private LinearLayout mMyLoveExpert;
    private LinearLayout mMyProfile;
    private Button mLogout;
    private PreUtils pre;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.infofrag_layout, null, false);
        mMyConsultQuestion = (LinearLayout)view.findViewById(R.id.my_question_parent);
        mMyLoveExpert = (LinearLayout)view.findViewById(R.id.my_expert_parent);
        mMyProfile = (LinearLayout)view.findViewById(R.id.my_profile_parent);

        pre = PreUtils.getInstance(getActivity());
        mLogout = (Button)view.findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("logout_app");
//                getContext().sendBroadcast(intent);
//                //jump to login
                //pre.setUserIsLogin(-1);
                mApp.rmAllActivity_();
                //restoreUserInfo();

                pre.clearUserInfo();
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentLogin);

//                Intent intent2 = new Intent(Intent.ACTION_MAIN);
//                intent2.addCategory(Intent.CATEGORY_HOME);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent2);
//                //android.os.Process.killProcess(android.os.Process.myPid());

            }
        });
        mMyLoveExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyLoveExpertListActivity.class);
                startActivity(intent);
            }
        });
        mMyConsultQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyConsultQuestionActivity.class);
                startActivity(intent);
            }
        });
        mMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.ResultCallback<String> myProfileResultCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        if(!response.isEmpty() && null != response){
                            Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                            PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                            if(null != result){
                                boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                                if(issuccessed){
                                    if(!result.getResultJson().trim().isEmpty() && null != result.getResultJson().trim()){
                                        String myProfile = result.getResultJson();
                                        Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                                        intent.putExtra("myProfile", myProfile);
                                        startActivity(intent);
                                    }
                                }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                                    Toast toast = Toast.makeText(getActivity(), "ErrorCode = "+ result.getResultCode() + " "
                                            + getResources().getString(R.string.login_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                                    Toast toast = Toast.makeText(getActivity(), "ErrorCode = "+ result.getResultCode() + " "
                                            + getResources().getString(R.string.login_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        String serverMessage = e.getMessage();
                        Toast.makeText(getActivity(), serverMessage + " " + getResources().getText(R.string.login_hint_net_error), Toast.LENGTH_LONG).show();
                    }
                };

                //json格式post参数
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateFormat.format(date).toString();

                String url = Urls.HOST_TEST + Urls.LOGIN;
                PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "getCompany", "wisegoo", dateStr, "9000");

                ProfilePostBean bean = new ProfilePostBean(beanHead, pre.getUserLoginId(), String.valueOf(pre.getUserId()));
                String postParamJsonStr = new Gson().toJson(bean);
                OkHttpUtils.postJson(url, myProfileResultCallback, postParamJsonStr);
            }
        });
        return view;
    }

    class ProfilePostBean{
        private PostCommonHead.HEAD head;
        private ProfileBody body;
        public ProfilePostBean(PostCommonHead.HEAD head, String loginId, String userId) {
            this.head = head;
            this.body = new ProfileBody(loginId, userId);
        }

        class ProfileBody{
            private String loginId;//登录id
            private String userId;//企业/用户id

            public ProfileBody(String loginId, String userId) {
                this.loginId = loginId;
                this.userId = userId;
            }

        }
    }

    private void restoreUserInfo(){
        pre.setUserName("");
        pre.setUserPhone("");
        pre.setUserArea("");
        pre.setUserCapital("");
        pre.setUserIndustry("");
        pre.setUserEmployeeNum("");
        pre.setUserProduction("");
        pre.setUserEstime("");
        pre.setUserSaleArea("");
        pre.setUserIsConsulted("");
        pre.setUserSaleMode("");
        pre.setUserLoginName("");
        pre.setUserLoginPsw("");
        pre.setUserId(0l);
        pre.setUserIsLogin(-1);
    }
}
