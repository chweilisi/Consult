package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.ui.adapter.MyConsultQuestionAdapter;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.MyConsultQuestionPresenter;
import com.cheng.consult.ui.presenter.MyConsultQuestionPresenterImpl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MyConsultQuestionActivity extends BaseActivity implements IMyQuestionListView {

    private RecyclerView mRecyclerView;
    private List<Subject> mSubjects;
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
        mUserId = new Long(mApplication.mUserInfo.getUserId()).intValue();

        initView();
    }

    private void initView(){
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mQuestionPresenter = new MyConsultQuestionPresenterImpl(this);
        mAdapter.setOnQuestionListItemClickListener(listener);

        mRecyclerView.addOnScrollListener(mOnScrollListener);

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
    public void addData(List<Subject> subjects) {
        if(null == mSubjects) {
            mSubjects = new ArrayList<Subject>();
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

    private MyConsultQuestionAdapter.onQuestionListItemClickListener listener = new MyConsultQuestionAdapter.onQuestionListItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(MyConsultQuestionActivity.this, MyQuestionDetailActivity.class);
            Gson gson = new Gson();
            String data = gson.toJson(mSubjects.get(position));
            intent.putExtra("subject", data);
            startActivity(intent);
        }
    };
}
