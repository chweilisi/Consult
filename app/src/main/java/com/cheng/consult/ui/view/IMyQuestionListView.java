package com.cheng.consult.ui.view;

import com.cheng.consult.db.table.Subject;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public interface IMyQuestionListView {
    void showProgress();

    void addData(List<Subject> subjects);

    void hideProgress();

    void showLoadFailMsg();
}
