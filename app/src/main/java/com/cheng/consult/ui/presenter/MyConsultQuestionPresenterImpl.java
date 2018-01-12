package com.cheng.consult.ui.presenter;

import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectListItem;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.ui.model.MyQuestionModel;
import com.cheng.consult.ui.model.MyQuestionModelImpl;
import com.cheng.consult.ui.model.OnLoadMyQuestionsListener;
import com.cheng.consult.ui.view.IMyQuestionListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyConsultQuestionPresenterImpl implements MyConsultQuestionPresenter {
    private MyQuestionModel model;
    private IMyQuestionListView questionListView;

    public MyConsultQuestionPresenterImpl(IMyQuestionListView view){
        questionListView = view;
        model = new MyQuestionModelImpl();
    }

    @Override
    public void loadMyQuestion(int userId, int pageIndex) {
        //String url = Urls.HOST_TEST + Urls.QUESTION;
        String url = Urls.HOST_TEST + Urls.FORUM;

        model.loadMyQuestion(userId, url, pageIndex, Urls.PAZE_SIZE, listener);
    }

    private OnLoadMyQuestionsListener listener = new OnLoadMyQuestionsListener() {
        @Override
        public void onSuccess(List<SubjectListItem> subjects) {
            questionListView.addData(subjects);
        }

        @Override
        public void onFailed(String msg, Exception e) {

        }
    };

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
