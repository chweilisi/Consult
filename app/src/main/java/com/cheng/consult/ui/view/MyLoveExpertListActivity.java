package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.ExpertListItem;
import com.cheng.consult.ui.adapter.LoveExpertListAdapter;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.presenter.ExpertListPresenter;
import com.cheng.consult.ui.presenter.ExpertListPresenterImpl;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyLoveExpertListActivity extends BaseActivity implements IExpertListView, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private LoveExpertListAdapter mAdapter;
    private List<ExpertListItem> mData;
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
                    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                    PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                    boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                    if(issuccessed){
                        Intent intent = new Intent(MyLoveExpertListActivity.this, ExpertDetailActivity.class);
                        String expertStr = result.getResultJson().trim();
                        if(!expertStr.isEmpty() && null != expertStr){
                            isFocused = new Gson().fromJson(expertStr, Expert.class).getIsFocused();

                            intent.putExtra("expert", expertStr);
                            intent.putExtra("isFocused", isFocused);
                            startActivity(intent);
                        }

                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.expert_detail_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                        Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                + getResources().getString(R.string.expert_detail_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(mContext, "查询专家详情失败", Toast.LENGTH_LONG).show();
                }
            };

            //json格式post参数
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = dateFormat.format(date).toString();

            String url = Urls.HOST_TEST + Urls.EXPERT;
            PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "expertView", mApplication.mAppSignature, dateStr, "9000");
            ExpertDetailPostBean postParam = new ExpertDetailPostBean(beanHead, String.valueOf(mApplication.mUserId), mData.get(pos).getExpertId());
            String param = new Gson().toJson(postParam);
            OkHttpUtils.postJson(url, checkCallback, param);
        }
    };

    class ExpertDetailPostBean{
        private PostCommonHead.HEAD head;
        private detailBody body;

        public ExpertDetailPostBean(PostCommonHead.HEAD head, String userId, String expertId) {
            this.head = head;
            this.body = new detailBody(userId, expertId);
        }

        class detailBody {
            private String userId;
            private String expertId;

            public detailBody(String userId, String expertId) {
                this.userId = userId;
                this.expertId = expertId;
            }
        }
    }

    @Override
    public void showProgress() {
        mSwipe.setRefreshing(true);
    }

    @Override
    public void addExperts(List<ExpertListItem> experts) {
        if(mData == null) {
            mData = new ArrayList<ExpertListItem>();
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
