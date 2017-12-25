package com.cheng.consult.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AskExpertActivity extends BaseActivity {

    private EditText mQuestionDes;
    private EditText mQuestionTitle;
    private String qDes;
    private String qTitle;
    private Button mSubmit;
    private List<OkHttpUtils.Param> paramList;
    private Spinner cateSpinner;
    private int questionCate = -1;
    private boolean isFirstSelect = true;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cateSpinner = (Spinner)findViewById(R.id.cat_spinner);
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



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramList = new ArrayList<>();
                //add user id
                String id = String.valueOf(mApplication.mUserId);
                OkHttpUtils.Param userId = new OkHttpUtils.Param("userId", id);
                paramList.add(userId);
                //add post parameter question title
                qTitle = mQuestionTitle.getText().toString().trim();
                OkHttpUtils.Param param = new OkHttpUtils.Param("questionTitle", qTitle);
                paramList.add(param);
                //add post parameter question des
                qDes = mQuestionDes.getText().toString().trim();
                OkHttpUtils.Param param1 = new OkHttpUtils.Param("questionDes", qDes);
                paramList.add(param1);

                if(-1 == questionCate || 0 == questionCate){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_cate_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(qDes.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_des_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {
                    //TODO submit question
                    String url = Urls.HOST_TEST + Urls.QUESTION;
                    OkHttpUtils.Param qesCate = new OkHttpUtils.Param("questionCate", String.valueOf(questionCate));
                    OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","save");
                    paramList.add(qesCate);
                    paramList.add(mothed);
                    OkHttpUtils.post(url, null, paramList);
                    //finish self
                    finish();
                }

            }
        });



        List<String> spinnerRes = Arrays.asList(getResources().getStringArray(R.array.spinner_consult_category));

        ArrayAdapter<String> spinnerAadapter = new ArrayAdapter<String>(this,
                R.layout.custom_spiner_text_item, spinnerRes);

        spinnerAadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        cateSpinner.setAdapter(spinnerAadapter);

        cateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstSelect){
                    isFirstSelect = false;
                } else {
                    questionCate = position - 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_cate_error_toast), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }

}
