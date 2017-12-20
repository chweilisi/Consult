package com.cheng.consult.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.cheng.consult.R;
import com.cheng.consult.utils.DisplayUtils;

/**
 * Created by cheng on 2017/11/20.
 */

public class EditTextWithDel extends EditText {
    private Drawable imgInable;
    private Drawable imgAble;
    private Context mContext;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        int height = mContext.getResources().getDimensionPixelOffset(R.dimen.college_tag_padding);
        this.setPadding(0,0,height,0);
        imgAble = mContext.getResources().getDrawable(R.drawable.search_delete_btn_selector);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    // 设置删除图片
    private void setDrawable() {
        if (length() > 0)
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void setDrawable(int resId) {
        if (resId > 0) {
            imgAble = mContext.getResources().getDrawable(resId);
        } else {
            imgAble = null;
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            //Rect rect = new Rect();
            int[] postion = new int[2];
            getLocationOnScreen(postion);
            //rect.right = postion[0]+getWidth();
            //rect.top = postion[1];
            //rect.bottom = postion[1]+getHeight();
            //getGlobalVisibleRect(rect);
            //rect.left = rect.right - 50;
            //if (rect.contains(eventX, eventY))
            int right = postion[0]+getWidth();
            int left = right- DisplayUtils.dpToPx(mContext, 25);
            if(eventX >= left && eventX <= right) {
                setText("");
                //发送点删除动作event
//                EventBus.getDefault().post(new HotSearchDelEvent().build(true));
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
