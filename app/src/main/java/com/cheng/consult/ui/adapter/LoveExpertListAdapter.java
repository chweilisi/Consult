package com.cheng.consult.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.ExpertListItem;
import com.cheng.consult.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class LoveExpertListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ExpertListItem> mDatas;
    private onExpListItemClickListener mItemClickListener;

    public LoveExpertListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<ExpertListItem> experts){
        this.mDatas = experts;
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ExpertListItem exp = mDatas.get(position);
            if(exp == null) return;

            ((ItemViewHolder) holder).mTitle.setText(exp.getExpertName());
            ((ItemViewHolder) holder).mDesc.setText(exp.getExpertDes());
            //((ItemViewHolder) holder).mImg.setImageDrawable(mContext.getDrawable(R.drawable.mypage_concerned_teacher_icon));
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
        }

    }

    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }
        return 0;
    }

    public ExpertListItem getExpItem(int position){
        return (null == mDatas) ? null : mDatas.get(position);
    }

    public interface onExpListItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnExpListItemClickListener(onExpListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        //public com.cheng.consult.widget.CircularImage mImg;
        public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //mImg = (com.cheng.consult.widget.CircularImage)itemView.findViewById(R.id.ivExpert);
            mTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            mDesc = (TextView)itemView.findViewById(R.id.tvDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });

        }
    }
}
