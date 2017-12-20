package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.adapter.LoveExpertListAdapter;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.ExpertListPresenter;
import com.cheng.consult.ui.presenter.ExpertListPresenterImpl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MyLoveExpertListActivity extends BaseActivity implements IExpertListView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private LoveExpertListAdapter mAdapter;
    private List<Expert> mData;
    private ExpertListPresenter mPresenter;

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipe;

    //应该从登陆信息中读取，临时赋值
    private int mUserId = 1;
    private int mPageIndex = 0;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_love_expert_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipe.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myfragment_my_expert_tip));
        mPresenter = new ExpertListPresenterImpl(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new LoveExpertListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(mOnScrollListener);
        mAdapter.setOnExpListItemClickListener(listener);

        onRefresh();
    }

//    private void initView(){
//        if(mData != null) {
//            mData.clear();
//        }
//        //TODO http or db to query data
//        mPresenter.loadExpertList(mUserId, -1, mPageIndex);
//    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {

                mPresenter.loadExpertList(mUserId, -1, mPageIndex + Urls.PAZE_SIZE);
            }
        }
    };

    private LoveExpertListAdapter.onExpListItemClickListener listener = new LoveExpertListAdapter.onExpListItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            Expert expert = mAdapter.getExpItem(position);
            Long id = expert.getId();



            Intent intent = new Intent(MyLoveExpertListActivity.this, ExpertDetailActivity.class);
            //intent.putExtra("id", id);

            Gson gson = new Gson();
            String data = gson.toJson(mData.get(position));
            intent.putExtra("expert", data);

            startActivity(intent);
        }
    };

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addExperts(List<Expert> experts) {
        if(mData == null) {
            mData = new ArrayList<Expert>();
        }
        mData.addAll(experts);
        if(mPageIndex == 0) {
            mAdapter.setData(mData);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mPageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void onRefresh() {
        if(mData != null) {
            mData.clear();
        }
        //TODO http or db to query data
        mPresenter.loadExpertList(mUserId, -1, mPageIndex);
    }
}
