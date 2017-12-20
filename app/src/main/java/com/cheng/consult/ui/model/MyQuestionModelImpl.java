package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.ExpertJsonUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by cheng on 2017/12/5.
 */

public class MyQuestionModelImpl implements MyQuestionModel {
    @Override
    public void loadMyQuestion(String url, int pageIndex, final OnLoadMyQuestionsListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //List<Subject> newsBeanList = ExpertJsonUtils.readJsonNewsBeans(response, getID(type));
                //listener.onSuccess(newsBeanList);
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                //Gson gson = new Gson();
                List<Subject> subjects = gson.fromJson(response, new TypeToken<List<Subject>>() {}.getType());
                listener.onSuccess(subjects);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailed("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    private String getID(int type) {
        String id;
        switch (type) {
            case 0:
                id = Urls.TOP_ID;
                break;
            case 1:
                id = Urls.NBA_ID;
                break;
            case 2:
                id = Urls.CAR_ID;
                break;
            case 3:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }
}
