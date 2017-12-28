package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.adapter.LoveExpertListAdapter;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.ExpertListPresenter;
import com.cheng.consult.ui.presenter.ExpertListPresenterImpl;
import com.cheng.consult.utils.OkHttpUtils;
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
    private int mUserId = -1;
    private int mExpertCat = -1;
    private int mPageIndex = 0;
    private int isFocused = -1;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_love_expert_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mUserId = new Long(mApplication.mUserId).intValue();

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
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mAdapter.setOnExpListItemClickListener(listener);

        initRecycler();
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
                //关注的专家，expertCate 要传-1
                mPageIndex = mPageIndex + 1;
                mPresenter.loadExpertList(mUserId, mExpertCat, mPageIndex);
            }
        }
    };

    private LoveExpertListAdapter.onExpListItemClickListener listener = new LoveExpertListAdapter.onExpListItemClickListener() {
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
                    Intent intent = new Intent(MyLoveExpertListActivity.this, ExpertDetailActivity.class);
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


//            Expert expert = mAdapter.getExpItem(position);
//            Long id = expert.getId();
//
//            Intent intent = new Intent(MyLoveExpertListActivity.this, ExpertDetailActivity.class);
//            //intent.putExtra("id", id);
//
//            Gson gson = new Gson();
//            String data = gson.toJson(mData.get(position));
//            intent.putExtra("expert", data);
//            intent.putExtra("isFocused", 1);
//
//            startActivity(intent);
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

        //mPageIndex += Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
        mSwipe.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {

    }

    @Override
    public void onResume(){
        super.onResume();
        //initRecycler();
    }
    @Override
    public void onRefresh() {
//        if(mData != null) {
//            mData.clear();
//        }
        //关注的专家，expertCate 要传-1
        mPresenter.loadExpertList(mUserId, mExpertCat, mPageIndex);
    }

    public void initRecycler() {
        if(mData != null) {
            mData.clear();
        }
        //关注的专家，expertCate 要传-1
        mPresenter.loadExpertList(mUserId, mExpertCat, mPageIndex);
    }
}
