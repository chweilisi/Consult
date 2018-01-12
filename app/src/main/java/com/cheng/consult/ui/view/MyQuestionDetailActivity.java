package com.cheng.consult.ui.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.ui.adapter.MyQuestionDetailAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class MyQuestionDetailActivity extends BaseActivity {

    private Subject mSubject;
    private List<SubjectItem> mQContent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyQuestionDetailAdapter mAdapter;
    private ImageView mSubjectIcon;
    private TextView mSubjectTitle;
    private TextView mSubjectDes;
    private ListView mListView;
    private String mSubjectData;
    private Button mBtnAskAgain;
    //private AnswerAdapter mLvAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_question_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSubjectTitle = (TextView)findViewById(R.id.tvTitle);
        mSubjectDes = (TextView)findViewById(R.id.tvDesc);
        //mListView = (ListView)findViewById(R.id.list_view);

        //获取activity传递数据
        mSubjectData = getIntent().getStringExtra("questiondetail");
        //int indexAnsweredBegin = mSubjectData.indexOf()
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        mSubject = gson.fromJson(mSubjectData, Subject.class);
        mQContent = mSubject.getItems();

        //设置问题标题 正文
        mSubjectTitle.setText(mSubject.getTitle());
        mSubjectDes.setText(mSubject.getContent());

//        mLvAdapter = new AnswerAdapter();
//
//        mListView.setAdapter(mLvAdapter);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);


        mQContent = mSubject.getItems();
        if(null != mQContent && mQContent.size() != 0) {
            mAdapter = new MyQuestionDetailAdapter(this, mQContent);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnAnswerItemClickListener(listener);
            mAdapter.notifyDataSetChanged();
        }

        //set footerview
        View footer = LayoutInflater.from(mContext).inflate(R.layout.question_detail_page_ask_again_layout, mRecyclerView, false);
        mBtnAskAgain = (Button)footer.findViewById(R.id.btn_question_detail_answer_again);
        mAdapter.setFooterView(footer);
        mAdapter.setHeaderView(null);
        mBtnAskAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyQuestionDetailActivity.this, AskExpertActivity.class);
                intent.putExtra("expertid", mSubject.getExpertId());
                startActivity(intent);
            }
        });
    }

    private MyQuestionDetailAdapter.onAnswerItemClickListener listener = new MyQuestionDetailAdapter.onAnswerItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, AnswerItemDetailActivity.class);
            intent.putExtra("answer_content", mSubject.getItems().get(position).getContent());
            intent.putExtra("item_type", mSubject.getItems().get(position).getItemType());
            startActivity(intent);
        }
    };
/*
    private class AnswerAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mQContent.size();
        }

        @Override
        public Object getItem(int position) {
            return mQContent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SubjectItem item = mQContent.get(position);
            View view;
            ViewHolder vh;

            if(null == convertView){
                view = LayoutInflater.from(mContext).inflate(R.layout.question_answer_item, null);
                vh = new ViewHolder();
                vh.tvAnswer = (TextView)view.findViewById(R.id.tvDesc);
                view.setTag(vh);
            } else {
                view = convertView;
                vh = (ViewHolder)view.getTag();
            }

            vh.tvAnswer.setText(mQContent.get(position).getContent());
            return view;
        }

        class ViewHolder{
            TextView tvAnswer;
        }
    }
*/
}
