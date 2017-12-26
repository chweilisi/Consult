package com.cheng.consult.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consult.MainActivity;
import com.cheng.consult.R;
import com.cheng.consult.ui.adapter.ExpertListAdapter;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.ExpertListPresenter;
import com.cheng.consult.ui.presenter.ExpertListPresenterImpl;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExpertListActivity extends BaseActivity implements IExpertListView/*, SwipeRefreshLayout.OnRefreshListener*/ {

    private String mToolbarTitle;
    private int    mExpertCategory;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLinearLayoutManager;
    private SwipeRefreshLayout mSwipe;
    private ExpertListAdapter mAdapter;
    private List<Expert> mData;
    private int mPageIndex = 0;
    private ExpertListPresenter mPresenter;
    private PreUtils pre;
    private int mUserId;
    private int isFocused = -1;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mUserId = new Long(mApplication.mUserId).intValue();
        mToolbarTitle = getIntent().getStringExtra("cat");
        mExpertCategory     = getIntent().getIntExtra("position", 0);

        mPresenter = new ExpertListPresenterImpl(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mSwipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
//        mSwipe.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);

        mRecycleView = (RecyclerView)findViewById(R.id.recycle_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);

        getSupportActionBar().setTitle(mToolbarTitle);

        mAdapter = new ExpertListAdapter(getApplicationContext());
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnExpListItemClickListener(listener);

        mRecycleView.addOnScrollListener(mOnScrollListener);

        initRecycler();
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
                mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);
            }
        }
    };

    private ExpertListAdapter.onExpListItemClickListener listener = new ExpertListAdapter.onExpListItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }

            final int pos = position;
            OkHttpUtils.ResultCallback<String> checkCallback = new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onSuccess(String response) {
                    Gson gson = new Gson();
                    Expert user = gson.fromJson(response, Expert.class);

                    isFocused = user.getIsFocused();

                    //Expert expert = mAdapter.getExpItem(pos);
                    //String id = expert.getId();
                    Intent intent = new Intent(ExpertListActivity.this, ExpertDetailActivity.class);
                    Gson gson2 = new Gson();
                    String data = gson2.toJson(mData.get(pos));
                    intent.putExtra("expert", data);
                    intent.putExtra("isFocused", isFocused);

                    startActivity(intent);

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(mContext, "查询专家详情失败", Toast.LENGTH_LONG).show();
                }
            };

            List<OkHttpUtils.Param> params = new ArrayList<>();
            try {
                Long expId = mData.get(position).getId();

                OkHttpUtils.Param expid = new OkHttpUtils.Param("focusExpertId", String.valueOf(expId));
                OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","detail");
                OkHttpUtils.Param userid = new OkHttpUtils.Param("userid",String.valueOf(mUserId));

                params.add(userid);
                params.add(expid);
                params.add(mothed);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //查询是否关注专家
            String checkFocusUrl = Urls.HOST_TEST + Urls.EXPERT;
            OkHttpUtils.post(checkFocusUrl, checkCallback, params);



            //Expert expert = mAdapter.getExpItem(position);
            //String id = expert.getId();
//            Intent intent = new Intent(ExpertListActivity.this, ExpertDetailActivity.class);
//            Gson gson = new Gson();
//            String data = gson.toJson(mData.get(position));
//            int isFocused = mData.get(position).getIsFocused();
//            intent.putExtra("expert", data);
//            intent.putExtra("isFocused", isFocused);
//
//            startActivity(intent);

        }
    };

    class QueryFocusExpert{
        int isFocused;//0：没有关注，1：已关注

        public int getIsFocused() {
            return isFocused;
        }

        public void setIsFocused(int isFocused) {
            this.isFocused = isFocused;
        }
    }

//    @Override
//    public void onRefresh() {
//        if(mData != null) {
//            mData.clear();
//        }
//        mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);
//    }

    private void initRecycler(){
        if(mData != null) {
            mData.clear();
        }
        mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);
    }

    @Override
    public void showProgress() {
//        mSwipe.setRefreshing(true);
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

        //mPageIndex += Urls.PAZE_SIZE;
        //mPageIndex++;
    }

    @Override
    public void hideProgress() {
//        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }
}
