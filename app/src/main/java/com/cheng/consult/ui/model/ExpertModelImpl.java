package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.ExpertJsonUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public void loadExpertList(String url, int userId, int pageNum, int pageSize, int cateId, final OnLoadExpertsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadExpertCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                List<Expert> experts = gson.fromJson(response, new TypeToken<List<Expert>>() {}.getType());
                listener.onSuccess(experts);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param user = new OkHttpUtils.Param("userId", Integer.toString(userId));
            OkHttpUtils.Param expertCategory = new OkHttpUtils.Param("cateId", Integer.toString(cateId));
            OkHttpUtils.Param pagenum = new OkHttpUtils.Param("pagenum", Integer.toString(pageNum));
            OkHttpUtils.Param pagesize = new OkHttpUtils.Param("pagesize", Integer.toString(pageSize));
            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","list");

            params.add(user);
            params.add(expertCategory);
            params.add(pagenum);
            params.add(pagesize);
            params.add(mothed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.post(url, loadExpertCallback, params);
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
