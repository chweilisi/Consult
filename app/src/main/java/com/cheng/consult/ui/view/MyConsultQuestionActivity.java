package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectListItem;
import com.cheng.consult.ui.adapter.MyConsultQuestionAdapter;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.MyConsultQuestionPresenter;
import com.cheng.consult.ui.presenter.MyConsultQuestionPresenterImpl;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyConsultQuestionActivity extends BaseActivity implements IMyQuestionListView {

    private RecyclerView mRecyclerView;
    private List<SubjectListItem> mSubjects;
    private LinearLayoutManager mLinearLayoutManager;
    private MyConsultQuestionAdapter mAdapter;
    private MyConsultQuestionPresenter mQuestionPresenter;
    private int mPageIndex = 0;
    private int mUserId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_consult_question;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyConsultQuestionAdapter(this);
        mUserId = new Long(mApplication.mUserId).intValue();

        initView();
    }

    private void initView(){
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mQuestionPresenter = new MyConsultQuestionPresenterImpl(this);
        mAdapter.setOnQuestionListItemClickListener(listener);

        mRecyclerView.addOnScrollListener(mOnScrollListener);
        //mRecyclerView.setFocusableInTouchMode(false);

        //add data
        refreshView();
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                mPageIndex = mPageIndex + 1;
                mQuestionPresenter.loadMyQuestion(mUserId, mPageIndex);
            }
        }
    };

    private void refreshView(){
        if(null != mSubjects) {
            mSubjects.clear();
        }
        mQuestionPresenter.loadMyQuestion(mUserId, mPageIndex);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void addData(List<SubjectListItem> subjects) {
        if(null == mSubjects) {
            mSubjects = new ArrayList<SubjectListItem>();
        }
        mSubjects.addAll(subjects);
        if(mPageIndex == 0) {
            mAdapter.setData(mSubjects);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        //mPageIndex += Urls.PAZE_SIZE;
        //mPageIndex++;
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoadFailMsg() {

    }

    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    private MyConsultQuestionAdapter.onQuestionListItemClickListener listener = new MyConsultQuestionAdapter.onQuestionListItemClickListener() {
        @Override
        public void onItemClick(View view, final int position) {
            final int pos = position;
            OkHttpUtils.ResultCallback<String> QuestionDetailResultCallback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                    boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                    if(issuccessed){
                        Intent intent = new Intent(MyConsultQuestionActivity.this, MyQuestionDetailActivity.class);
                        String questionStr = result.getResultJson().trim();
                        intent.putExtra("questiondetail", questionStr);
                        startActivity(intent);
                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.question_detail_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.question_detail_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            };

            //json格式post参数
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = dateFormat.format(date).toString();

            String url = Urls.HOST_TEST + Urls.FORUM;
            PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "subjectView", mApplication.mAppSignature, dateStr, "9000");
            QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mSubjects.get(pos).getSubjectId()), "0", "20");
            String strParam = gson.toJson(param);
            OkHttpUtils.postJson(url, QuestionDetailResultCallback, strParam);
        }
    };

    class QuestionDetailPostBean{
        private PostCommonHead.HEAD head;
        private PostBody body;

        public QuestionDetailPostBean(PostCommonHead.HEAD head, String subjectId, String pageNum, String pageSize) {
            this.head = head;
            this.body = new PostBody(subjectId, pageNum, pageSize);
        }

        class PostBody{
            private String subjectId;
            private String pageNum;
            private String pageSize;

            public PostBody(String subjectId, String pageNum, String pageSize) {
                this.subjectId = subjectId;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }
}
