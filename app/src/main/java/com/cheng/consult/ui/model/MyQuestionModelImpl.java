package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.Subject;
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
 * Created by cheng on 2017/12/5.
 */

public class MyQuestionModelImpl implements MyQuestionModel {
    @Override
    public void loadMyQuestion(int userId, String url, int pageNum, int pageSize, final OnLoadMyQuestionsListener listener) {
        OkHttpUtils.ResultCallback<String> loadMyQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                List<Subject> subjects = gson.fromJson(response, new TypeToken<List<Subject>>() {}.getType());
                listener.onSuccess(subjects);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailed("load news list failure.", e);
            }
        };

        List<OkHttpUtils.Param> params = new ArrayList<>();
        try {
            OkHttpUtils.Param userid = new OkHttpUtils.Param("userId", String.valueOf(userId));
            OkHttpUtils.Param pagenum = new OkHttpUtils.Param("pagenum", Integer.toString(pageNum));
            OkHttpUtils.Param pagesize = new OkHttpUtils.Param("pagesize", Integer.toString(pageSize));
            OkHttpUtils.Param isanswer = new OkHttpUtils.Param("isAnswered", "");
            OkHttpUtils.Param ismy = new OkHttpUtils.Param("ismine", "1");
            OkHttpUtils.Param cateid = new OkHttpUtils.Param("cateId", "-1");
            OkHttpUtils.Param expid = new OkHttpUtils.Param("expertId", "-1");

            OkHttpUtils.Param mothed = new OkHttpUtils.Param("method","listUser");

            params.add(userid);
            params.add(pagenum);
            params.add(pagesize);
            params.add(isanswer);
            params.add(ismy);
            params.add(cateid);
            params.add(expid);
            params.add(mothed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpUtils.post(url, loadMyQuestionCallback, params);
    }
}
