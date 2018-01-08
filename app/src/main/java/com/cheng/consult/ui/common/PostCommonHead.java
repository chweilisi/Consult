package com.cheng.consult.ui.common;

/**
 * Created by cheng on 2018/1/4.
 */

public class PostCommonHead {
    private HEAD head;

    public PostCommonHead(String signkey, String method, String signature, String requesttime, String appid) {
        this.head = new HEAD(signkey, method, signature, requesttime, appid);
    }

    public static class HEAD{
        private String signkey;
        private String method;
        private String signature;
        private String requesttime;
        private String appid;

        public HEAD(String signkey, String method, String signature, String requesttime, String appid) {
            this.signkey = signkey;
            this.method = method;
            this.signature = signature;
            this.requesttime = requesttime;
            this.appid = appid;

        }

        public String getSignkey() {
            return signkey;
        }

        public void setSignkey(String signkey) {
            this.signkey = signkey;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getRequesttime() {
            return requesttime;
        }

        public void setRequesttime(String requesttime) {
            this.requesttime = requesttime;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
