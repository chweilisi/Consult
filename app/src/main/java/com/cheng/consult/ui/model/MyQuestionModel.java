package com.cheng.consult.ui.model;

/**
 * Created by cheng on 2017/12/5.
 */

public interface MyQuestionModel {
    public void loadMyQuestion(int userId, String url, int pageNum, int pageSize, OnLoadMyQuestionsListener listener);
}
