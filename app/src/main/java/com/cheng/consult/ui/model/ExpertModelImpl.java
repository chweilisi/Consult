package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.ExpertJsonUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class ExpertModelImpl implements ExpertModel {
    @Override
    /*
    public void loadExpertList(String url, final int type, final OnLoadExpertsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<Expert> ExpertList = ExpertJsonUtils.readJsonExpertList(response);
                listener.onSuccess(ExpertList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }
    */
    public void loadExpertList(String url, int userId, final int cateId, final OnLoadExpertsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadExpertCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                List<Expert> experts = gson.fromJson(response, new TypeToken<List<Expert>>() {}.getType());
                //List<Expert> newsBeanList = ExpertJsonUtils.readJsonNewsBeans(response, getID(type));

                listener.onSuccess(experts);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param user = new OkHttpUtils.Param("userId", URLEncoder.encode(Integer.toString(userId), "UTF-8"));
            OkHttpUtils.Param expertCategory = new OkHttpUtils.Param("cateId", URLEncoder.encode(Integer.toString(cateId), "UTF-8"));

            params.add(user);
            params.add(expertCategory);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        OkHttpUtils.post(url, loadExpertCallback, params);
        //OkHttpUtils.get(url, loadExpertCallback);
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
