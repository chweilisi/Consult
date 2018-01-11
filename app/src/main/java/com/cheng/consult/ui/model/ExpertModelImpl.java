package com.cheng.consult.ui.model;

import com.cheng.consult.app.App;
import com.cheng.consult.db.table.Expert;
import com.cheng.consult.db.table.ExpertListItem;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.ExpertJsonUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;
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

import static com.cheng.consult.app.App.getApplication;

/**
 * Created by cheng on 2017/11/28.
 */

public class ExpertModelImpl implements ExpertModel {

    private String mUserId;
    private PreUtils mPreUtils;

    public ExpertModelImpl() {
        int id = ((App) getApplication()).mUserId;
        if(-1 != id){
            mUserId = String.valueOf(id);
        } else {
            mPreUtils = PreUtils.getInstance(getApplication());
            mUserId = String.valueOf(mPreUtils.getUserId());
        }
    }

    Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

    @Override
    public void loadExpertList(String url, int userId, int pageNum, int pageSize, int cateId, final OnLoadExpertsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadExpertCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                //Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                //List<Expert> experts = gson.fromJson(response, new TypeToken<List<Expert>>() {}.getType());
                PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                //List<Expert> experts = gson.fromJson(result.getResultJson(), new TypeToken<List<Expert>>() {}.getType());
                List<ExpertListItem> expertList = gson.fromJson(result.getResultJson(), new TypeToken<List<ExpertListItem>>() {}.getType());
                listener.onSuccess(expertList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();

        //Gson gson = new Gson();
        String listMethod = (-1 == cateId) ? "relationList" : "fieldExpertList";
        String postParamJsonStr = "";
        PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", listMethod, ((App) getApplication()).mAppSignature, dateStr, "9000");

        if(-1 == cateId){//查询关注专家
            LoveExpertListParam param = new LoveExpertListParam(postHead, mUserId, String.valueOf(pageNum), String.valueOf(pageSize));
            postParamJsonStr = gson.toJson(param);
        } else {//查询领域专家
            ExpertListPostParam param = new ExpertListPostParam(postHead, String.valueOf(cateId), String.valueOf(pageNum), String.valueOf(pageSize));
            postParamJsonStr = gson.toJson(param);
        }

        OkHttpUtils.postJson(url, loadExpertCallback, postParamJsonStr);
    }

    class LoveExpertListParam{
        private PostCommonHead.HEAD head;
        private LoveBody body;

        public LoveExpertListParam(PostCommonHead.HEAD head, String userId, String pageNum, String pageSize) {
            this.head = head;
            this.body = new LoveBody(userId, pageNum, pageSize);
        }

        class LoveBody{
            private String userId;
            private String pageNum;
            private String pageSize;

            public LoveBody(String userId, String pageNum, String pageSize) {
                this.userId = userId;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }

    class ExpertListPostParam{
        private PostCommonHead.HEAD head;
        private BODY body;

        public ExpertListPostParam(PostCommonHead.HEAD head,
                                   String cateid, String pagenum, String pagesize) {
            this.head = head;
            this.body = new BODY(cateid, pagenum, pagesize);
        }

        class BODY{
            private String cateId;
            private String pagenum;
            private String pagesize;

            public BODY(String cateId, String pagenum, String pagesize) {
                this.cateId = cateId;
                this.pagenum = pagenum;
                this.pagesize = pagesize;
            }
        }
    }

}
