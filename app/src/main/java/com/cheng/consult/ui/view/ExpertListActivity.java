package com.cheng.consult.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cheng.consult.R;
import com.cheng.consult.ui.adapter.ExpertListAdapter;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.ExpertListPresenter;
import com.cheng.consult.ui.presenter.ExpertListPresenterImpl;
import com.cheng.consult.utils.PreUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExpertListActivity extends BaseActivity implements IExpertListView, SwipeRefreshLayout.OnRefreshListener {

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
        mSwipe   = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipe.setOnRefreshListener(this);

        setSupportActionBar(mToolbar);

        mRecycleView = (RecyclerView)findViewById(R.id.recycle_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLinearLayoutManager);

        getSupportActionBar().setTitle(mToolbarTitle);

        mAdapter = new ExpertListAdapter(getApplicationContext());
        mRecycleView.setAdapter(mAdapter);

        mAdapter.setOnExpListItemClickListener(listener);

        mRecycleView.addOnScrollListener(mOnScrollListener);

        onRefresh();
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
            Expert expert = mAdapter.getExpItem(position);
            //String id = expert.getId();
            Intent intent = new Intent(ExpertListActivity.this, ExpertDetailActivity.class);
            Gson gson = new Gson();
            String data = gson.toJson(mData.get(position));
            intent.putExtra("expert", data);

            startActivity(intent);
//            View transitionView = view.findViewById(R.id.ivExpert);
//            ActivityOptionsCompat options =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(ExpertListActivity.this,
//                            transitionView, getString(R.string.transition_news_img));
//
//            ActivityCompat.startActivity(getApplicationContext(), intent, options.toBundle());
        }
    };

    @Override
    public void onRefresh() {
        if(mData != null) {
            mData.clear();
        }
        mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);
    }

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

        //mPageIndex += Urls.PAZE_SIZE;
        //mPageIndex++;
    }

    @Override
    public void hideProgress() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }
}
