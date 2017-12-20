package com.cheng.consult.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.cheng.consult.R;
import com.cheng.consult.utils.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

public class AskExpertActivity extends BaseActivity {

    private EditText mQuestionDes;
    private EditText mQuestionTitle;
    private String qDes;
    private String qTitle;
    private Button mSubmit;
    private List<OkHttpUtils.Param> paramList;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mQuestionDes = (EditText)findViewById(R.id.question_des);
        mQuestionTitle = (EditText)findViewById(R.id.question_title);
        mQuestionDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mQuestionDes, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        mSubmit = (Button)findViewById(R.id.submit);

        paramList = new ArrayList<>();
        //add post parameter question title
        qTitle = mQuestionTitle.getText().toString();
        OkHttpUtils.Param param = new OkHttpUtils.Param("questionTitle", qTitle);
        paramList.add(param);
        //add post parameter question des
        qDes = mQuestionDes.getText().toString();
        OkHttpUtils.Param param1 = new OkHttpUtils.Param("questionDes", qDes);
        paramList.add(param1);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO submit question
                OkHttpUtils.post("http://101.200.40.228:8080/public/api/case", null, paramList);
                //finish self
                finish();
            }
        });
    }

}
