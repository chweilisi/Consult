package com.cheng.consult.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import com.cheng.consult.R;
/**
 * Created by cheng on 2017/11/22.
 */

public class LeftRightView extends RelativeLayout {
    private static final String TAG = "LeftRightView";
    private TextView leftTextView;
    private TextView rightTextView;
    private ImageView iconImageView;
    private View divider;
    private View iconView;

    private int iconResId;
    private String leftText;
    private String rightText;
    private boolean showDivider;
    private boolean showIcon;
    private Drawable leftButtonDrawable;
    private Drawable rightButtonDrawable;
    private int dividerMarginLeft;
    private int leftTextTranslationX;

    public LeftRightView(Context context) {
        super(context);
        init(context, null);
    }

    public LeftRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LeftRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
        this.leftTextView.setText(this.leftText);
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        this.rightTextView.setText(this.rightText);
    }
    public void setRightText(String rightText,int textSize,String color) {
        this.rightText = rightText;
        this.rightTextView.setTextSize(textSize);
        this.rightTextView.setTextColor(Color.parseColor(color));
        this.rightTextView.setText(this.rightText);
    }

    public void setRightTextClearIcon(String rightText) {
        this.rightText = rightText;
        this.rightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        this.rightTextView.setText(this.rightText);
    }

    public void setRightText(String rightText, int resourceID, int padding) {
        this.rightText = rightText;
        this.rightTextView.setCompoundDrawablePadding(padding);
        this.rightTextView.setText(this.rightText);
        this.rightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(resourceID),
                null);
    }

    public View getLeftView() {
        if (leftTextView == null) {
            return null;
        }
        return leftTextView;
    }

    public View getRightTextView() {
        if (rightTextView != null) {
            return rightTextView;
        }
        return null;
    }

    public String getRightText() {
        return this.rightText;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setImageRes(int iconResId) {
        this.iconResId = iconResId;
        this.iconImageView.setImageResource(this.iconResId);
    }

    public void setImageVisibility(int visibility) {
        this.iconView.setVisibility(visibility);
        this.iconImageView.setVisibility(visibility);
    }


    public void setRightTextColor(int color) {
        this.rightTextView.setTextColor(color);
    }

    public void isShowDivider(boolean isShow) {
        this.showDivider = isShow;
        this.divider.setVisibility(this.showDivider ? View.VISIBLE : View.GONE);
        ViewHelper.setTranslationX(divider, dividerMarginLeft);
    }

    public void isShowIcon(boolean isShow) {
        this.showIcon = isShow;
        this.iconImageView.setVisibility(this.showIcon ? View.VISIBLE : View.GONE);
        ViewHelper.setTranslationX(iconImageView, dividerMarginLeft);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeftRightView);
            this.iconResId = a.getResourceId(R.styleable.LeftRightView_viewIcon, 0);
            this.leftText = a.getString(R.styleable.LeftRightView_leftText);
            this.rightText = a.getString(R.styleable.LeftRightView_rightText);
            this.showDivider = a.getBoolean(R.styleable.LeftRightView_showDivider, true);
            this.showIcon = a.getBoolean(R.styleable.LeftRightView_showIcon,true);
            this.leftButtonDrawable = a.getDrawable(R.styleable.LeftRightView_leftButtonDrawable);
            this.rightButtonDrawable = a.getDrawable(R.styleable.LeftRightView_rightButtonDrawable);
            dividerMarginLeft = a.getDimensionPixelSize(R.styleable.LeftRightView_dividerMarginLeft, 0);
            leftTextTranslationX = a.getDimensionPixelSize(R.styleable.LeftRightView_leftTextTranslationX, 0);
            a.recycle();
        }

        LayoutInflater.from(context).inflate(R.layout.view_shared_leftrightview, this, true);
        this.leftTextView = (TextView) this.findViewById(R.id.leftrightview_left_text);
        this.rightTextView = (TextView) this.findViewById(R.id.leftrightview_right_text);
        this.iconImageView = (ImageView) this.findViewById(R.id.leftrightview_icon);
        this.divider = this.findViewById(R.id.leftrightview_divider);
        this.iconView = this.findViewById(R.id.leftrightview_icon_contianer);
        if (this.iconResId > 0) {
            this.iconImageView.setImageResource(this.iconResId);
        }


        this.leftTextView.setText(this.leftText);
        this.rightTextView.setText(this.rightText);
        this.divider.setVisibility(this.showDivider ? View.VISIBLE : View.GONE);
        this.iconImageView.setVisibility(this.showIcon ? View.VISIBLE : View.GONE);
        this.leftTextView.setCompoundDrawablesWithIntrinsicBounds(this.leftButtonDrawable, null, null, null);
        this.rightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, this.rightButtonDrawable, null);
        ViewHelper.setTranslationX(leftTextView, leftTextTranslationX);
    }
}
