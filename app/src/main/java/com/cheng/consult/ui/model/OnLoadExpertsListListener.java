package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.ExpertListItem;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public interface OnLoadExpertsListListener {
    //void onSuccess(List<Expert> list);
    void onSuccess(List<ExpertListItem> list);
    void onFailure(String msg, Exception e);
}
