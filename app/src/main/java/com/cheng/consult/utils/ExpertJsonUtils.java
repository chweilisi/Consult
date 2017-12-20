package com.cheng.consult.utils;

import com.cheng.consult.db.table.Expert;
import com.cheng.consult.ui.common.NewsBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheng on 2017/11/28.
 */

public class ExpertJsonUtils {

    private static final String TAG = "ExpertJsonUtils";
    private Gson mGson;

    public static List<Expert> readJsonExpertList(String res) {
        List<Expert> beans = new ArrayList<Expert>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();

            if(jsonObj == null) {
                return null;
            }
            JsonArray jsonArray = jsonObj.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                Expert expert = JsonUtils.deserialize(jo, Expert.class);
                beans.add(expert);

            }
        } catch (Exception e) {
            LogUtils.e(TAG, "readJsonExpertBeans error" , e);
        }
        return beans;
    }

    public static List<Expert> readJsonNewsBeans(String res, String value) {

        List<Expert> expert = new ArrayList<>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if(jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if (!jo.has("imgextra")) {
                    NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                    Expert exp = new Expert();
                    exp.setImgSrc(news.getImgsrc());
                    exp.setName(news.getTitle());
                    exp.setDes(news.getDigest());
                    expert.add(exp);
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "readJsonNewsBeans error" , e);
        }
        return expert;
    }

}
