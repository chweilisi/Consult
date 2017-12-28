package com.cheng.consult.ui.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.ui.adapter.MyQuestionDetailAdapter;
import com.google.gson.Gson;

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
    private AnswerAdapter mLvAdapter;

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
        String data = getIntent().getStringExtra("subject");
        Gson gson = new Gson();
        mSubject = gson.fromJson(data, Subject.class);
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
            mAdapter.notifyDataSetChanged();
        }
        //mRecyclerView.setFocusableInTouchMode(false);
        //mRecyclerView.setNestedScrollingEnabled(false);
    }

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

}
