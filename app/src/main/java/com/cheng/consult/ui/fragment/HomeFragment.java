package com.cheng.consult.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cheng.consult.R;
import com.cheng.consult.ui.view.AskExpertActivity;
import com.cheng.consult.ui.view.ExpertCategoryActivity;
import com.cheng.consult.ui.view.MyConsultQuestionActivity;
import com.cheng.consult.ui.view.MyLoveExpertListActivity;
import com.cheng.consult.ui.view.SearchActivity;

/**
 * Created by cheng on 2017/11/13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private LinearLayout mQueryExp;
    private LinearLayout mQueryField;
    private LinearLayout mHistoryQuestion;
    private LinearLayout mEssense;
    private Button mQuickAskBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefrag_layout, null, false);
        LinearLayout searchView = (LinearLayout)view.findViewById(R.id.homeFragment_search);
        searchView.setOnClickListener(this);
        LinearLayout mQueryExp = (LinearLayout)view.findViewById(R.id.home_expert_icon);
        mQueryExp.setOnClickListener(this);
        LinearLayout mQueryField = (LinearLayout)view.findViewById(R.id.home_field_icon);
        mQueryField.setOnClickListener(this);
        LinearLayout mHistoryQuestion = (LinearLayout)view.findViewById(R.id.home_historyquest_icon);
        mHistoryQuestion.setOnClickListener(this);
        LinearLayout mEssense = (LinearLayout)view.findViewById(R.id.essence_question_icon);
        mEssense.setOnClickListener(this);
        LinearLayout mMyLoveExperts = (LinearLayout)view.findViewById(R.id.home_my_expert_icon);
        mMyLoveExperts.setOnClickListener(this);
        mQuickAskBtn = (Button)view.findViewById(R.id.expert_detail_ask_button);
        mQuickAskBtn.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeFragment_search:
            {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_expert_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), ExpertCategoryActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_field_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), ExpertCategoryActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_historyquest_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), MyConsultQuestionActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_my_expert_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), MyLoveExpertListActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.expert_detail_ask_button:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), AskExpertActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            default:
                break;
        }

    }
}
