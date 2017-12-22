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

    //每次必传userid，根据expertCate来区别是查询什么专家，如果expertCate为-1，就是查询对应领域专家
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
        mModel.loadExpertList(url, pageIndex, Urls.PAZE_SIZE, userId, expertCate, loadNewsListener);
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
        return Urls.HOST_TEST + Urls.LOVEEXPERT;
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
    /*
    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case 0:
                sb.append(Urls.STRATEGYCONSULTING).append(Urls.END_URL);
                break;
            case 1:
                sb.append(Urls.TRANSFORMUPGRADING).append(Urls.END_URL);
                break;
            case 2:
                sb.append(Urls.CAPITALOPERATION).append(Urls.END_URL);
                break;
            case 3:
                sb.append(Urls.SYSTEMMANAGMENT).append(Urls.END_URL);
            case 4:
                sb.append(Urls.MARKETING).append(Urls.END_URL);
                break;
            case 5:
                sb.append(Urls.FINEPRODUCTION).append(Urls.END_URL);
                break;
            case 6:
                sb.append(Urls.QUALITYIMPROVEMENT).append(Urls.END_URL);
                break;
            case 7:
                sb.append(Urls.COSTCONTROL).append(Urls.END_URL);
                break;
            case 8:
                sb.append(Urls.FINANCIALOPERATION).append(Urls.END_URL);
                break;
            case 9:
                sb.append(Urls.SALARYPERFORMANCE).append(Urls.END_URL);
                break;
            case 10:
                sb.append(Urls.TEAMBUILDING).append(Urls.END_URL);
                break;
            case 11:
                sb.append(Urls.CORPORATECURLTURE).append(Urls.END_URL);
                break;
            case 12:
                sb.append(Urls.AMOEBAMODE).append(Urls.END_URL);
                break;
            default:
                sb.append(Urls.END_URL).append(Urls.END_URL);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
    */
    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case 0:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case 1:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case 2:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case 3:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
}
