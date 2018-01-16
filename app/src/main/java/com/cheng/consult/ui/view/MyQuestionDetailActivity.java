package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.ui.adapter.MyQuestionDetailAdapter;
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
import java.util.List;

public class MyQuestionDetailActivity extends BaseActivity implements View.OnClickListener {

    private Subject mSubject;
    private List<SubjectItem> mQContent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyQuestionDetailAdapter mAdapter = null;
    private ImageView mSubjectIcon;
    private TextView mSubjectTitle;
    private TextView mSubjectDes;
    private ListView mListView;
    private String mSubjectData;
    private Button mBtnAskAgain;
    private ImageButton mBtnBottomBarAsk;
    private Gson gson;
    private int mPageIndex = 0;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_question_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("问题详情");

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSubjectTitle = (TextView)findViewById(R.id.tvTitle);
        mSubjectDes = (TextView)findViewById(R.id.tvDesc);

        //获取activity传递数据
        mSubjectData = getIntent().getStringExtra("questiondetail");

        mSubject = gson.fromJson(mSubjectData, Subject.class);
        mQContent = mSubject.getItems();

        //设置问题标题 正文
        mSubjectTitle.setText(mSubject.getTitle());
        mSubjectDes.setText(mSubject.getContent());
        mSubjectDes.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //mQContent = mSubject.getItems();
        if(null != mQContent && mQContent.size() != 0) {
            mAdapter = new MyQuestionDetailAdapter(this, mQContent);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnAnswerItemClickListener(listener);
            mAdapter.notifyDataSetChanged();
        }

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        //set footerview
        setViewerHeaderAndFooter();

        mBtnBottomBarAsk = (ImageButton)findViewById(R.id.btn_question_detail_bottombar_ask);
        mBtnBottomBarAsk.setOnClickListener(this);
    }

    private void setViewerHeaderAndFooter(){
        if(null != mAdapter){
            View footer = LayoutInflater.from(mContext).inflate(R.layout.question_detail_page_ask_again_layout, mRecyclerView, false);
            mBtnAskAgain = (Button)footer.findViewById(R.id.btn_question_detail_answer_again);
            mBtnAskAgain.setOnClickListener(this);
            mAdapter.setFooterView(footer);
            mAdapter.setHeaderView(null);
        }
    }
    private void refreshPage(Subject subject){
        mQContent.addAll(subject.getItems());
        if(null != mQContent && mQContent.size() != 0) {
            mAdapter.notifyDataSetChanged();
            setViewerHeaderAndFooter();
        }
    }

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
                mPageIndex = mPageIndex + 1;
                //mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);

                OkHttpUtils.ResultCallback<String> QuestionDetailResultCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                        boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                        if(issuccessed){
                            String questionStr = result.getResultJson().trim();
                            Subject tmpSubject = gson.fromJson(questionStr, Subject.class);
                            int size = tmpSubject.getItems().size();
                            if(0 < size){
                                refreshPage(tmpSubject);
                            }
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
                QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mSubject.getSubjectId()), String.valueOf(mPageIndex), "20");
                String strParam = gson.toJson(param);
                OkHttpUtils.postJson(url, QuestionDetailResultCallback, strParam);


            }
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

    private MyQuestionDetailAdapter.onAnswerItemClickListener listener = new MyQuestionDetailAdapter.onAnswerItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, AnswerItemDetailActivity.class);
            SubjectItem item = mSubject.getItems().get(position);
            String answerItem = gson.toJson(item);
            intent.putExtra("answer_item", answerItem);
//            intent.putExtra("answer_content", mSubject.getItems().get(position).getContent());
//            intent.putExtra("item_type", mSubject.getItems().get(position).getItemType());
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_question_detail_bottombar_ask:
            case R.id.btn_question_detail_answer_again:
            {
                Intent intent = new Intent(MyQuestionDetailActivity.this, AskExpertAgainActivity.class);
                intent.putExtra("expertid", mSubject.getExpertId());
                intent.putExtra("subjectid", mSubject.getSubjectId());
                startActivity(intent);
                break;
            }
            case R.id.tvDesc:
            {
                Intent intent = new Intent(mContext, AnswerItemDetailActivity.class);
                String questionDes = mSubject.getContent();
                intent.putExtra("question_description", questionDes);
                startActivity(intent);
                break;
            }
        }
    }
}
