package com.cheng.consult.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.ui.adapter.ExpertCategoryActivityAdapter;


public class ExpertCategoryActivity extends BaseActivity {
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    private ExpertCategoryActivityAdapter mAdapter;
    private String[] mConsultCategory;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_expert_category;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mRecycleView = (RecyclerView)findViewById(R.id.recycle_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new ExpertCategoryActivityAdapter(this);
        //mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnCategoryItemClickListener(listener);
        initView();
    }

    private void initView(){
        mConsultCategory = getResources().getStringArray(R.array.consult_category);
        mAdapter.setData(mConsultCategory);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private ExpertCategoryActivityAdapter.categoryItemClickListener listener = new ExpertCategoryActivityAdapter.categoryItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //TODO enter category

//            Intent intent = new Intent(ExpertCategoryActivity.class, ExpertListActivity.class);
            Intent intent = new Intent(ExpertCategoryActivity.this, ExpertListActivity.class);
            intent.putExtra("cat",mConsultCategory[position]);
            intent.putExtra("position",position);
            startActivity(intent);
//            if (getContext() instanceof Activity) {
//                ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
//            }
        }
    };

}
