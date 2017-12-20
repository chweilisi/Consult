package com.cheng.consult.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.cheng.consult.R;

/**
 * Created by cheng on 2017/12/6.
 */

public class PwdShowLayout extends FrameLayout {
    Context mContext;
    EditText pwdEdit;
    CheckBox pwdShowCb;

    public PwdShowLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PwdShowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public PwdShowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.password_layout, this);
        pwdEdit = (EditText) findViewById(R.id.pwd_edit);
        pwdEdit.setTypeface(Typeface.SANS_SERIF);
        pwdShowCb = (CheckBox) findViewById(R.id.pwd_show_toggle);
        if (isInEditMode()) {
            return;
        }
        initListener();
    }

    public void setCheckBoxGone(boolean isGone) {
        if (isGone) {
            pwdShowCb.setVisibility(View.GONE);
            pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }
    }

    private void initListener() {
        pwdShowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                pwdEdit.setSelection(pwdEdit.getText().toString().length());
            }
        });
    }

    public String getPwdText() {
        return pwdEdit.getText().toString();
    }

    public void setPwdText(String input) {
        pwdEdit.setText(input);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        pwdEdit.addTextChangedListener(watcher);
    }

    public void setKeyListener(KeyListener input) {
        pwdEdit.setKeyListener(input);
    }

    public void setPwdHintText(int resId) {
        pwdEdit.setHint(resId);
    }

    public EditText getPwdView() {
        return pwdEdit;
    }
}
