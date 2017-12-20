package com.cheng.consult.ui.model;

/**
 * Created by cheng on 2017/11/28.
 */

public interface ExpertModel {
    void loadExpertList(String url, int userId, int cateId, OnLoadExpertsListListener listener);

}
