package com.cheng.consult.db.table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cheng on 2017/11/27.
 */

public class Expert implements Serializable {
    //@Id(autoincrement = true)
    private Long Id;//专家id

    private String LoginName;//登录用户名
    private String LoginPsw;//登录密码
    private String Name;//专家名字
    private String Sex;//专家性别
    private int    Age;//专家年龄
    private String Area;//所在区域
    private String ExpertTime;//从业时间
    private String GoodField;//擅长领域
    //private int AnswerCount;

    //@ToMany(referencedJoinProperty = "CaseId")
    private List<Case> Cases;//经典案例
    private String Des;//简介
    private String ImgSrc;//头像ip
    private String PhoneNum;//电话
    private String Weixin;//微信
    private String Qq;//qq
    private List<User> MyLoveUser;//我关注的企业
    private List<Subject> Subjects;//我回答的问题
    private int isFocused;//是否关注该专家，0：未关注；1：已关注

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getLoginPsw() {
        return LoginPsw;
    }

    public void setLoginPsw(String loginPsw) {
        LoginPsw = loginPsw;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getExpertTime() {
        return ExpertTime;
    }

    public void setExpertTime(String expertTime) {
        ExpertTime = expertTime;
    }

    public String getGoodField() {
        return GoodField;
    }

    public void setGoodField(String goodField) {
        GoodField = goodField;
    }

    public List<Case> getCases() {
        return Cases;
    }

    public void setCases(List<Case> cases) {
        Cases = cases;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public String getImgSrc() {
        return ImgSrc;
    }

    public void setImgSrc(String imgSrc) {
        ImgSrc = imgSrc;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getWeixin() {
        return Weixin;
    }

    public void setWeixin(String weixin) {
        Weixin = weixin;
    }

    public String getQq() {
        return Qq;
    }

    public void setQq(String qq) {
        Qq = qq;
    }

    public List<User> getMyLoveUser() {
        return MyLoveUser;
    }

    public void setMyLoveUser(List<User> myLoveUser) {
        MyLoveUser = myLoveUser;
    }

    public List<Subject> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        Subjects = subjects;
    }

    public int getIsFocused() {
        return isFocused;
    }

    public void setIsFocused(int isFocused) {
        this.isFocused = isFocused;
    }
}
