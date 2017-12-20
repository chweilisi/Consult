package com.cheng.consult.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cheng.consult.R;
import com.cheng.consult.ui.view.LoginActivity;
import com.cheng.consult.ui.view.MyConsultQuestionActivity;
import com.cheng.consult.ui.view.MyLoveExpertListActivity;
import com.cheng.consult.ui.view.MyProfileActivity;
import com.cheng.consult.utils.PreUtils;

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
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        return view;
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
