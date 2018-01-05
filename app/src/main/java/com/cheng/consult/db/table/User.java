package com.cheng.consult.db.table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cheng on 2017/12/4.
 */

public class User implements Serializable {
    //@Id(autoincrement = true)
    private String UserId;//企业会员id

    private String LoginName;
    private String Password;//登录密码
    private String LoginId;//用户登录id
    private String PhoneNumber;//电话

    private String Name;//企业名称
    private int Industry;//所属行业
    private String LeiBie;//经营类别
    private String Area;//所在区域
    private String StablishDate;//成立时间
    private Long RegistCapital;//注册资金
    private Long CapitalTotal;//资产总额
    private String BussRange;//业务领域
    private String Production;//主要产品
    private Long LastYearSum;//上年总产量
    private String SalesArea;//销售区域 外销内销
    private String SalesMode;//销售模式 代理商/经销商、终端客户、电商
    private Long LastYearSaleCount;//上年销售额
    private boolean IsConsulted;//有无咨询经历
    private int EmployeNum;//雇员数目

    //@ToMany(referencedJoinProperty = "OwnerUserId")
    private List<Subject> Subjects;//提问问题

    //@ToMany(referencedJoinProperty = "Id")
    private List<Expert> LoveExperts;//关注的专家

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIndustry() {
        return Industry;
    }

    public void setIndustry(int industry) {
        Industry = industry;
    }

    public String getLeiBie() {
        return LeiBie;
    }

    public void setLeiBie(String leiBie) {
        LeiBie = leiBie;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getStablishDate() {
        return StablishDate;
    }

    public void setStablishDate(String stablishDate) {
        StablishDate = stablishDate;
    }

    public Long getRegistCapital() {
        return RegistCapital;
    }

    public void setRegistCapital(Long registCapital) {
        RegistCapital = registCapital;
    }

    public Long getCapitalTotal() {
        return CapitalTotal;
    }

    public void setCapitalTotal(Long capitalTotal) {
        CapitalTotal = capitalTotal;
    }

    public String getBussRange() {
        return BussRange;
    }

    public void setBussRange(String bussRange) {
        BussRange = bussRange;
    }

    public String getProduction() {
        return Production;
    }

    public void setProduction(String production) {
        Production = production;
    }

    public Long getLastYearSum() {
        return LastYearSum;
    }

    public void setLastYearSum(Long lastYearSum) {
        LastYearSum = lastYearSum;
    }

    public String getSalesArea() {
        return SalesArea;
    }

    public void setSalesArea(String salesArea) {
        SalesArea = salesArea;
    }

    public String getSalesMode() {
        return SalesMode;
    }

    public void setSalesMode(String salesMode) {
        SalesMode = salesMode;
    }

    public Long getLastYearSaleCount() {
        return LastYearSaleCount;
    }

    public void setLastYearSaleCount(Long lastYearSaleCount) {
        LastYearSaleCount = lastYearSaleCount;
    }

    public boolean isConsulted() {
        return IsConsulted;
    }

    public void setConsulted(boolean consulted) {
        IsConsulted = consulted;
    }

    public int getEmployeNum() {
        return EmployeNum;
    }

    public void setEmployeNum(int employeNum) {
        EmployeNum = employeNum;
    }

    public List<Subject> getSubjects() {
        return Subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        Subjects = subjects;
    }

    public List<Expert> getLoveExperts() {
        return LoveExperts;
    }

    public void setLoveExperts(List<Expert> loveExperts) {
        LoveExperts = loveExperts;
    }
}
