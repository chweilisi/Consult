package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public interface OnLoadMyQuestionsListener {
    public void onSuccess(List<Subject> subjects);
    public void onFailed(String msg, Exception e);
}
