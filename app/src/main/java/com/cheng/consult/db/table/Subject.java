package com.cheng.consult.db.table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by cheng on 2017/12/1.
 */

public class Subject implements Serializable {
    //@Id(autoincrement = true)
    private Long SubjectId;//问题id
    private int OwnerUserId;//提问用户id
    private int ExpertId;//回答专家id
    private String IconSrc;//问题图标ip
    private String Title;//问题title
    private String Content;//问题正文
    private String filePath = null;
    private Date date;//问题日期
    private boolean IsAnswered;//是否已回答
    private int QuestionCate;//问题所属领域

    //@ToMany(referencedJoinProperty = "OwnerSubjectId")
    private List<SubjectItem> Items;//问题内容
    //private String Question;//
    //private String Answer;//

    public Long getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(Long subjectId) {
        SubjectId = subjectId;
    }

    public int getOwnerUserId() {
        return OwnerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        OwnerUserId = ownerUserId;
    }

    public int getExpertId() {
        return ExpertId;
    }

    public void setExpertId(int expertId) {
        ExpertId = expertId;
    }

    public String getIconSrc() {
        return IconSrc;
    }

    public void setIconSrc(String iconSrc) {
        IconSrc = iconSrc;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAnswered() {
        return IsAnswered;
    }

    public void setAnswered(boolean answered) {
        IsAnswered = answered;
    }

    public int getQuestionCate() {
        return QuestionCate;
    }

    public void setQuestionCate(int questionCate) {
        QuestionCate = questionCate;
    }

    public List<SubjectItem> getItems() {
        return Items;
    }

    public void setItems(List<SubjectItem> items) {
        Items = items;
    }
}
