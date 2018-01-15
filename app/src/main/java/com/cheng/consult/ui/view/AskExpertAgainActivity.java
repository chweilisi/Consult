package com.cheng.consult.ui.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AskExpertAgainActivity extends BaseActivity {
    private EditText mQuestionDes;
    private Button mSubmit;
    private String qDes;
    private int mExpertId;
    private Long mSubjectId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_again;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mExpertId = getIntent().getIntExtra("expertid", -1);
        mSubjectId = getIntent().getLongExtra("subjectid", -1);

        mQuestionDes = (EditText)findViewById(R.id.question_des);
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



        final Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.ResultCallback<String> submitQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                        boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                        if(issuccessed){
                            Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.submit_success), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(mContext, "提交失败，请检查网络", Toast.LENGTH_LONG).show();
                    }
                };

                qDes = mQuestionDes.getText().toString().trim();

                if(qDes.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_des_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //json格式post参数
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateFormat.format(date).toString();

                    PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubjectItem", mApplication.mAppSignature, dateStr, "9000");
                    QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), String.valueOf(mSubjectId), "0", qDes, "0", "20");

                    String param = gson.toJson(postParam);
                    String url = Urls.HOST_TEST + Urls.FORUM;
                    OkHttpUtils.postJson(url, submitQuestionCallback, param);
                    finish();
                }
            }
        });
    }

    class QuestionSubmitParam{
        private PostCommonHead.HEAD head;
        private QuestionBody body;

        public QuestionSubmitParam(PostCommonHead.HEAD head,
                                   String relationId, String subjectId, String isType, String questionDes, String pageNum, String pageSize) {
            this.head = head;
            this.body = new QuestionBody(relationId, subjectId, isType, questionDes, pageNum, pageSize);
        }

        class QuestionBody{
            private String relationId;//追问就是用户id，专家回答就是专家id
            private String subjectId;
            private String isType;
            private String questionDes;
            private String pageNum;
            private String pageSize;

            public QuestionBody(String relationId, String subjectId, String isType, String questionDes, String pageNum, String pageSize) {

                this.relationId = relationId;
                this.subjectId = subjectId;
                this.isType = isType;
                this.questionDes = questionDes;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }

}
