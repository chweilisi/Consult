package com.cheng.consult.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheng.consult.R;
import com.cheng.consult.ui.view.ExpertListActivity;
import com.cheng.consult.ui.adapter.ConsultCategoryListAdapter;

/**
 * Created by cheng on 2017/11/13.
 */

public class ConsultFragment extends Fragment {
    private RecyclerView mRecycleView;
    private LinearLayoutManager mLayoutManager;
    //private TextView mEnterCategory;
    private ConsultCategoryListAdapter mAdapter;
    private String[] mConsultCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultfrag_layout, null, false);
        //mEnterCategory = (TextView)view.findViewById(R.id.focus_button);

        mRecycleView = (RecyclerView)view.findViewById(R.id.recycle_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new ConsultCategoryListAdapter(getActivity());
        //mRecycleView.setAdapter(mAdapter);
        mAdapter.setOnCategoryItemClickListener(listener);
        initView();
        return view;
    }

    private void initView(){
        mConsultCategory = getActivity().getResources().getStringArray(R.array.consult_category);
        mAdapter.setData(mConsultCategory);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private ConsultCategoryListAdapter.categoryItemClickListener listener = new ConsultCategoryListAdapter.categoryItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            //TODO enter category

            Intent intent = new Intent(getContext(),ExpertListActivity.class);
            intent.putExtra("cat",mConsultCategory[position]);
            intent.putExtra("position",position);
            startActivity(intent);
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
            }
        }
    };
}
