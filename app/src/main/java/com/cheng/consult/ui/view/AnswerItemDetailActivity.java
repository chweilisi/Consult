package com.cheng.consult.ui.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AnswerItemDetailActivity extends BaseActivity {

    private TextView mAnswerDetail;
    private String mAnswer;
    private int mItemType = -1;
    private Subject mSubject;
    private SubjectItem mAnswerItem;
    private String mQuestionDes;
    private String mAttachmentPath;
    private ImageView mAttachImg;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_item_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAttachImg = (ImageView)findViewById(R.id.answer_item_detail_attachment_img);

        mAnswerDetail = (TextView)findViewById(R.id.tv_answer_item_detail_page);

        String answerItem = getIntent().getStringExtra("answer_item");
        if(answerItem != null && !answerItem.isEmpty()){
            mAnswerItem = gson.fromJson(answerItem, SubjectItem.class);

            mAnswer = mAnswerItem.getContent();
            if(mAnswer != null && !mAnswer.isEmpty()){
                mAnswerDetail.setText(mAnswer);
            }

            mItemType = mAnswerItem.getItemType();
            if(0 == mItemType){
                getSupportActionBar().setTitle("问题详情");
            } else {
                getSupportActionBar().setTitle("回答详情");
            }
        }

        if(null == mAnswerItem.getFilePath()){
            mAttachImg.setVisibility(View.INVISIBLE);
        } else {

        }

        mQuestionDes = getIntent().getStringExtra("question_description");

        if(mQuestionDes != null && !mQuestionDes.isEmpty()){
            getSupportActionBar().setTitle("问题详情");
            mAnswerDetail.setText(mQuestionDes);
        }

    }

}
