package com.cheng.consult.ui.presenter;

/**
 * Created by cheng on 2017/11/28.
 */
//根据userId expertCate来区别是查询什么专家，两者必有一个为-1，谁不是-1就是查询什么专家
public interface ExpertListPresenter {
    public void loadExpertList(int userId, int expertCate, int page);
}
