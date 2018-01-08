package com.cheng.consult.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by cheng on 2017/12/6.
 */

public class PreUtils {

    private static PreUtils instance;

    private SharedPreferences mPre;

    public static PreUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreUtils(context);
        }
        return instance;
    }

    private PreUtils(Context context) {
        mPre = context.getSharedPreferences("consult_config", 0);
    }

/*
    public void setLoginStatus(boolean status) {
        mPre.edit().putBoolean("consult_login_status", status).commit();
    }


    public boolean getLoginStatus() {
        return mPre.getBoolean("conslut_login_status", false);
    }


    public void setCookie(String cookie) {
        mPre.edit().putString("cookie", cookie).commit();
    }


    public String getCookie() {
        return mPre.getString("cookie", "");
    }

    //这个只有登录之后才能使用 发帖用到

    public void setFormhash(String formhash) {
        mPre.edit().putString("formhash", formhash).commit();
    }

     //只有登录之后才能使用 发帖用到

    public String getFormhash() {
        return mPre.getString("formhash", null);
    }

    //用于缓存登录信息

    public void setLoginUid(String uid) {
        mPre.edit().putString("login_uid", uid).commit();
    }

    //获取当前登录的用户信息

    public String getloginUid() {
        return mPre.getString("login_uid", "0");
    }


    //用于缓存登录信息saltKey
    public void setSaltKey(String saltKey) {
        mPre.edit().putString("login_saltKey", saltKey).commit();
    }


     //获取当前登录的用户信息saltKey

    public String getSaltKey() {
        return mPre.getString("login_saltKey", "");
    }

    public void setLoginUserName(String member_username) {
        mPre.edit().putString("login_username", member_username).commit();
    }

    public void setLoginToken(String token) {
        mPre.edit().putString("login_token", token).commit();
    }

    public String getLoginToken() {
        return mPre.getString("login_token", "");
    }

    public String getLoninUserName() {
        return mPre.getString("login_username", "");
    }

    public void setAvator(String avator) {
        mPre.edit().putString("avator_", avator).commit();
    }

    public String getAvator() {
        return mPre.getString("avator_", "");
    }

    public void setAvatorStatus(int status) {
        mPre.edit().putInt("avator_status_" + getloginUid(), status).commit();
    }

    public int getAvatorStatus() {
        return mPre.getInt("avator_status_" + getloginUid(), -1);
    }


    public void setLoignAccount(String account) {
        mPre.edit().putString("account", account).commit();
    }

    public String getLoginAccount() {
        return mPre.getString("account", null);
    }


    public void setLoginPswd(String loginPswd) {
        mPre.edit().putString("loginPswd", loginPswd).commit();
    }

    public String getLoginPswd() {
        return mPre.getString("loginPswd", null);
    }


    public void setNickName(String account) {
        mPre.edit().putString("nickname", account).commit();
    }

    public String getNickName() {
        return mPre.getString("nickname", null);
    }

    public void setId(Long id){
        mPre.edit().putLong("id", id);
    }

    public Long getId(){
        return mPre.getLong("id", -1);
    }
*/
    //user profile
    public void setUserName(String name){
        mPre.edit().putString("username", name).commit();
    }
    public String getUserName(){
        return mPre.getString("username", "");
    }

    public void setUserPhone(String num){
        mPre.edit().putString("phonenum", num).commit();
    }
    public String getUserPhone(){
        return mPre.getString("phonenum", "");
    }

    public void setUserArea(String area){
        mPre.edit().putString("area", area).commit();
    }
    public String getUserArea(){
        return mPre.getString("area", "");
    }

    public void setUserCapital(String cap){
        mPre.edit().putString("capital", cap).commit();
    }
    public String getUserCapital(){
        return mPre.getString("capital", "");
    }

    public void setUserIndustry(String ind){
        mPre.edit().putString("industry", ind).commit();
    }
    public String getUserIndustry(){
        return mPre.getString("industry", "");
    }

    public void setUserEmployeeNum(String num){
        mPre.edit().putString("employee", num).commit();
    }
    public String getUserEmployeeNum(){
        return mPre.getString("employee", "");
    }

    public void setUserProduction(String production){
        mPre.edit().putString("production", production).commit();
    }
    public String getUserProduction(){
        return mPre.getString("production", "");
    }

    public void setUserCapitalTotal(String production){
        mPre.edit().putString("usercapitaltotal", production).commit();
    }
    public String getUserCapitalTotal(){
        return mPre.getString("usercapitaltotal", "0");
    }

    public void setUserLastYearSum(String production){
        mPre.edit().putString("userlastyearsum", production).commit();
    }
    public String getUserLastYearSum(){
        return mPre.getString("userlastyearsum", "0");
    }

    public void setUserEstime(String time){
        mPre.edit().putString("estime", time).commit();
    }
    public String getUserEstime(){
        return mPre.getString("estime", "2000-1");
    }

    public void setUserSaleArea(String area){
        mPre.edit().putString("saleArea", area).commit();
    }
    public String getUserSaleArea(){
        return mPre.getString("saleArea", "");
    }

    public void setUserIsConsulted(String con){
        mPre.edit().putString("mIsConsulted", con).commit();
    }
    public String getUserIsConsulted(){
        return mPre.getString("mIsConsulted", "false");
    }

    public void setUserSaleMode(String mode){
        mPre.edit().putString("saleMode", mode).commit();
    }
    public String getUserSaleMode(){
        return mPre.getString("saleMode", "1");
    }

    //login & regist associated
    public void setUserLoginName(String id){
        mPre.edit().putString("loginusername", id).commit();
    }
    public String getUserLoginName(){
        return mPre.getString("loginusername", "");
    }

    public void setUserLoginPsw(String psw){
        mPre.edit().putString("userpsw", psw).commit();
    }
    public String getUserLoginPsw(){
        return mPre.getString("userpsw", "");
    }

    public void setUserLoginId(String id){
        mPre.edit().putString("userloginid", id).commit();
    }
    public String getUserLoginId(){
        return mPre.getString("userloginid", "-1");
    }

    public void setUserId(Long id){
        mPre.edit().putLong("userid", id).commit();
    }
    public Long getUserId(){
        return mPre.getLong("userid", -1);
    }

    public void setUserIsLogin(int login){
        mPre.edit().putInt("islogined", login).commit();
    }
    public int getUserIsLogin(){
        return mPre.getInt("islogined", -1);
    }

    public void setUserType(String userType){
        mPre.edit().putString("usertype", userType).commit();
    }
    public String getUserType(){
        return mPre.getString("usertype", "");
    }

    public void setUserIsManager(String isManager){
        mPre.edit().putString("isManager", isManager).commit();
    }
    public String getUserIsManager(){
        return mPre.getString("isManager", "-1");
    }

    public void clearUserInfo(){
        mPre.edit().clear().commit();
    }
}
