package com.cheng.consult.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng.consult.R;

import java.util.List;

/**
 * Created by cheng on 2017/11/22.
 */

public class ConsultCategoryListAdapter extends RecyclerView.Adapter {
    private String[] mData;
    private Context mContext;
    public categoryItemClickListener mCategoryItemClickListener;


    public ConsultCategoryListAdapter(Context context) {
        mContext = context;
    }

    public void setData(String[] data){
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.consult_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            String name = mData[position];
            if(name == null) return;

            setListIcon(holder, position);
            ((ViewHolder) holder).categoryName.setText(name);

        }

    }

    private void setListIcon(RecyclerView.ViewHolder holder, int position){
        switch (position){
            case 0:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_1));
                break;
            case 1:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_2));
                break;
            case 2:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_3));
                break;
            case 3:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_4));
                break;
            case 4:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_5));
                break;
            case 5:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_6));
                break;
            case 6:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_7));
                break;
            case 7:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_8));
                break;
            case 8:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_9));
                break;
            case 9:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_10));
                break;
            case 10:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_11));
                break;
            case 11:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_12));
                break;
            case 12:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_13));
                break;
            default:
                ((ViewHolder) holder).categoryIcon.setImageDrawable(mContext.getDrawable(R.drawable.icon_category_1));
        }
    }
    @Override
    public int getItemCount() {
        return mData.length;
    }

    public interface categoryItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnCategoryItemClickListener(categoryItemClickListener listener){
        mCategoryItemClickListener = listener;
    }
    private class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        private ImageView categoryIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView)itemView.findViewById(R.id.category_name);
            categoryIcon = (ImageView) itemView.findViewById(R.id.category_item_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCategoryItemClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
