package com.cheng.consult.ui.view;

import com.cheng.consult.db.table.Expert;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public interface IExpertListView {
    void showProgress();

    void addExperts(List<Expert> newsList);

    void hideProgress();

    void showLoadFailMsg();
}
