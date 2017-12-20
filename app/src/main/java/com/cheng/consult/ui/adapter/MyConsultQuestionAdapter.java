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
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyConsultQuestionAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Subject> mDatas;

    public MyConsultQuestionAdapter(Context context){
        this.mContext = context;
    }
    private onQuestionListItemClickListener mItemClickListener;

    public void setData(List<Subject> experts){
        this.mDatas = experts;
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consult_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            Subject item = mDatas.get(position);
            if(item == null) return;

            //TODO exactlly data is not thi
            List<SubjectItem> subItem = item.getItems();

            //(((ItemViewHolder) holder).mImg).setBackgroundResource(R.drawable.icon_category_6);
            ((ItemViewHolder) holder).mDesc.setText(item.getTitle());

            //((ItemViewHolder) holder).mDesc.setText(item.getItems().get(position).getContent());
            //ImageLoaderUtils.display(mContext, (((ItemViewHolder) holder).mImg), item.getIconSrc());
        }

    }

    @Override
    public int getItemCount() {
        if(null != mDatas){
            return mDatas.size();
        }
        return 0;
    }

    public Subject getExpItem(int position){
        return (null == mDatas) ? null : mDatas.get(position);
    }

    public interface onQuestionListItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnQuestionListItemClickListener(onQuestionListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImg;
        //public TextView mTitle;
        public TextView mDesc;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView)itemView.findViewById(R.id.ivSubject);
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
