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
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private Long mExpertId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mExpertId = getIntent().getLongExtra("expertid", -1);
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
                OkHttpUtils.ResultCallback<String> submitQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };

                qTitle = mQuestionTitle.getText().toString().trim();
                qDes = mQuestionDes.getText().toString().trim();

                if(-1 == questionCate){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_cate_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(qTitle.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_title_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(qDes.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_des_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //json格式post参数
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateFormat.format(date).toString();

                    PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubject", mApplication.mAppSignature, dateStr, "9000");
                    QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), String.valueOf(mExpertId),
                            String.valueOf(questionCate), qTitle, qDes);

                    String param = new Gson().toJson(postParam);
                    String url = Urls.HOST_TEST + Urls.FORUM;
                    OkHttpUtils.postJson(url, submitQuestionCallback, param);
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

    class QuestionSubmitParam{
        private PostCommonHead.HEAD head;
        private QuestionBody body;

        public QuestionSubmitParam(PostCommonHead.HEAD head,
                                   String userId, String expertId, String questionCate, String questionTitle, String questionDes) {
            this.head = head;
            this.body = new QuestionBody(userId, expertId, questionCate, questionTitle, questionDes);
        }

        class QuestionBody{
            private String userId;
            private String expertId;
            private String questionCate;
            private String questionTitle;
            private String questionDes;

            public QuestionBody(String userId, String expertId, String questionCate, String questionTitle, String questionDes) {
                this.userId = userId;
                this.expertId = expertId;
                this.questionCate = questionCate;
                this.questionTitle = questionTitle;
                this.questionDes = questionDes;
            }
        }
    }

}
