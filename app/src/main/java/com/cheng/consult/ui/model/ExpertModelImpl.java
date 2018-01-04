package com.cheng.consult.ui.model;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.ExpertJsonUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class ExpertModelImpl implements ExpertModel {

    class ExpertListStatus{
        String resultJson;
        String resultMess;
        String resultCode;
        String resultDate;

        public String getResultJson() {
            return resultJson;
        }

        public void setResultJson(String resultJson) {
            this.resultJson = resultJson;
        }

        public String getResultMess() {
            return resultMess;
        }

        public void setResultMess(String resultMess) {
            this.resultMess = resultMess;
        }

        public String getResultCode() {
            return resultCode;
        }

        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultDate() {
            return resultDate;
        }

        public void setResultDate(String resultDate) {
            this.resultDate = resultDate;
        }
    }

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
                //List<Expert> experts = gson.fromJson(response, new TypeToken<List<Expert>>() {}.getType());
                PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                List<Expert> experts = gson.fromJson(result.getResultJson(), new TypeToken<List<Expert>>() {}.getType());
                listener.onSuccess(experts);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

/*
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
*/
        //json格式post参数测试
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();
        ExpertListPostParam param = new ExpertListPostParam("1", "list", "jerry", dateStr, "9000",
                Integer.toString(userId), Integer.toString(cateId), Integer.toString(pageNum), Integer.toString(pageSize)/*, "list"*/);
        String postParamJsonStr = new Gson().toJson(param);
        OkHttpUtils.postJson(url, loadExpertCallback, postParamJsonStr);
    }

    class ExpertListPostParam{
        PostCommonHead.HEAD head;
        BODY body;

        public ExpertListPostParam(String signkey, String method, String signature, String requesttime, String appid,
                                   String userid, String cateid, String pagenum, String pagesize/*, String bodymethod*/) {
            this.head = new PostCommonHead.HEAD(signkey, method, signature, requesttime, appid);
            this.body = new BODY(userid, cateid, pagenum, pagesize/*, bodymethod*/);
        }

        class BODY{
            private String userId;
            private String cateId;
            private String pagenum;
            private String pagesize;
            //private String method;

            public BODY(String userId, String cateId, String pagenum, String pagesize/*, String bodymethod*/) {
                this.userId = userId;
                this.cateId = cateId;
                this.pagenum = pagenum;
                this.pagesize = pagesize;
                //this.method = bodymethod;
            }
        }
    }

}
