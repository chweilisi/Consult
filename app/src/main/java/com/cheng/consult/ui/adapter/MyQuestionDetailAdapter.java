package com.cheng.consult.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by cheng on 2017/12/7.
 */

public class MyQuestionDetailAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<SubjectItem> mData;
    private onAnswerItemClickListener mItemClickListener;

    public static final int TYPE_QUESTION = 0;  //问题
    public static final int TYPE_ANSWER = 1;  //回答
    public static final int TYPE_HEADER = 3;  //说明是带有Header的
    public static final int TYPE_FOOTER = 4;  //说明是带有Footer的

    private View mHeaderView = null;
    private View mFooterView = null;

    public MyQuestionDetailAdapter(Context context, List<SubjectItem> data) {
        this.mContext = context;
        this.mData = data;
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        if(null != headerView){
            mHeaderView = headerView;
            notifyItemInserted(0);
        }
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        if(null != footerView){
            mFooterView = footerView;
            notifyItemInserted(getItemCount()-1);
        }
    }

    @Override
    public int getItemViewType(int position) {


        int count = getItemCount();
        if (mHeaderView == null && mFooterView == null){
            SubjectItem item = mData.get(position);
            if(0 == item.getItemType()){
                return TYPE_QUESTION;
            } else if(1 == item.getItemType()){
                return TYPE_ANSWER;
            }
        }
        //判断第一和最后一个类型
        if (position == 0){
            if(null != mHeaderView){
                return TYPE_HEADER;
            } else {
                SubjectItem item = mData.get(position);
                if(0 == item.getItemType()){
                    return TYPE_QUESTION;
                } else if(1 == item.getItemType()){
                    return TYPE_ANSWER;
                }
            }
        }

        if (position == count - 1){
            if(null != mFooterView){
                return TYPE_FOOTER;
            }else {
                SubjectItem item = mData.get(position);
                if(0 == item.getItemType()){
                    return TYPE_QUESTION;
                } else if(1 == item.getItemType()){
                    return TYPE_ANSWER;
                }
            }
        }

        if (mHeaderView == null && mFooterView != null){
            if((position != 0) && (position != count - 1)){
                SubjectItem item = mData.get(position);
                if(0 == item.getItemType()){
                    return TYPE_QUESTION;
                } else if(1 == item.getItemType()){
                    return TYPE_ANSWER;
                }
            }
        }

        return TYPE_ANSWER;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView, viewType);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ItemViewHolder(mFooterView, viewType);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_item, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if((getItemViewType(position) != TYPE_HEADER) && ((getItemViewType(position) != TYPE_FOOTER))){
            if(holder instanceof ItemViewHolder){
                SubjectItem item = mData.get(position);
                if(TYPE_QUESTION == item.getItemType()){
                    //((ItemViewHolder) holder).mQuestionLayout.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).mQuestionDesc.setText(item.getContent());
                    String attach = item.getFilePath();
                    if(null != attach){
                        ((ItemViewHolder) holder).mQuestionAttachment.setVisibility(View.VISIBLE);
                    } else {
                        ((ItemViewHolder) holder).mQuestionAttachment.setVisibility(View.INVISIBLE);
                    }
                } else if(TYPE_ANSWER == item.getItemType()){
                    //((ItemViewHolder) holder).mAnswerLayout.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).mAnswerDesc.setText(item.getContent());
                }
            }
        }

    }

    public interface onAnswerItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnAnswerItemClickListener(onAnswerItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mData.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mData.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mData.size() + 1;
        }else {
            return mData.size() + 2;
        }
        //return ((mData.size() == 0) ? 1 : mData.size());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout mQuestionLayout;
        public ImageView mQuestionImg;
        public TextView mQuestionDesc;
        public ImageView mQuestionAttachment;
        public LinearLayout mAnswerLayout;
        public ImageView mAnswerImg;
        public TextView mAnswerDesc;
        public ItemViewHolder(final View itemView, int viewType) {
            super(itemView);

            mQuestionLayout = (LinearLayout)itemView.findViewById(R.id.question_parent);
            mAnswerLayout = (LinearLayout)itemView.findViewById(R.id.answer_parent);
            if(TYPE_QUESTION == viewType){
                mQuestionLayout.setVisibility(View.VISIBLE);
                mAnswerLayout.setVisibility(View.GONE);
                mQuestionImg = (ImageView)itemView.findViewById(R.id.ivQuestionSubject);
                mQuestionDesc = (TextView)itemView.findViewById(R.id.tvQuestionDesc);
                mQuestionAttachment = (ImageView)itemView.findViewById(R.id.img_attachment);
            } else if(TYPE_ANSWER == viewType){
                mAnswerLayout.setVisibility(View.VISIBLE);
                mQuestionLayout.setVisibility(View.GONE);
                mAnswerImg = (ImageView)itemView.findViewById(R.id.ivAnswerSubject);
                mAnswerDesc = (TextView)itemView.findViewById(R.id.tvAnswerDesc);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
        }
    }
}
