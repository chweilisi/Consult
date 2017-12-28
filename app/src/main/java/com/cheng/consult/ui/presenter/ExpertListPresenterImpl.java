package com.cheng.consult.ui.presenter;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.model.ExpertModel;
import com.cheng.consult.ui.model.ExpertModelImpl;
import com.cheng.consult.ui.model.OnLoadExpertsListListener;
import com.cheng.consult.ui.view.IExpertListView;

import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class ExpertListPresenterImpl implements ExpertListPresenter {

    private IExpertListView mIExpView;
    private ExpertModel mModel;

    public ExpertListPresenterImpl(IExpertListView mIExpView) {
        this.mIExpView = mIExpView;
        mModel = new ExpertModelImpl();
    }

    //每次必传userid，根据expertCate来区别是查询什么专家，如果expertCate不为-1，就是查询对应领域专家
    @Override
    public void loadExpertList(int userId, int expertCate, int pageIndex) {
        String url = (-1 == expertCate) ? (getLoveExpUrl(userId)) : (getCategoryExpUrl(expertCate));

        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 0) {
            mIExpView.showProgress();
        }
        //other method to implement
        //mNewsModel.loadNews(url, type, this);
        //replace implement class interface
        mModel.loadExpertList(url, userId, pageIndex, Urls.PAZE_SIZE, expertCate, loadNewsListener);
    }

    OnLoadExpertsListListener loadNewsListener = new OnLoadExpertsListListener(){
        @Override
        public void onSuccess(List<Expert> list) {
            mIExpView.hideProgress();
            mIExpView.addExperts(list);
        }

        @Override
        public void onFailure(String msg, Exception e) {
            mIExpView.hideProgress();
            mIExpView.showLoadFailMsg();
        }
    };

    //如果userId不为-1，则为查询我关注的专家url
    private String getLoveExpUrl(int userId){
        return Urls.HOST_TEST + Urls.EXPERT;
    }

    //如果expertCate不为-1，则为查询领域专家url
    private String getCategoryExpUrl(int CateId){
        return Urls.HOST_TEST + Urls.EXPERT;
    }
    /**
     * 根据类别和页面索引创建url
     * @param type
     * @param pageIndex
     * @return
     */

}
