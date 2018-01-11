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
 * Created by cheng on 2017/11/28.
 */

public class ExpertListAdapter extends RecyclerView.Adapter {

    private onExpListItemClickListener mItemClickListener;
    private List<ExpertListItem> mData;
    private Context mContext;

    public ExpertListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ExpertListItem> data){
        this.mData = data;
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
            ExpertListItem exp = mData.get(position);
            if(exp == null) return;

            ((ItemViewHolder) holder).mTitle.setText(exp.getExpertName());
            ((ItemViewHolder) holder).mDesc.setText(exp.getExpertDes());
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), exp.getImgSrc());
        }

    }

    @Override
    public int getItemCount() {
        if(null != mData){
            return mData.size();
        }
        return 0;
    }

    public ExpertListItem getExpItem(int position){
        return (null == mData) ? null : mData.get(position);
    }

    public interface onExpListItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnExpListItemClickListener(onExpListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImg;
        public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //mImg = (ImageView)itemView.findViewById(R.id.ivExpert);
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
