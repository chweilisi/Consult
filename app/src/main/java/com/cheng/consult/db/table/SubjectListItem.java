package com.cheng.consult.db.table;

import java.util.Date;

/**
 * Created by cheng on 2018/1/11.
 */

public class SubjectListItem {
    private Long subjectId;//问题id
    private String ownerUserId;//提问用户id
    private String expertId;//回答专家id
    private String title;//问题title
    private String isAnswered;//是否已回答，0：未回答。1：已回答
    private String questionCateId;//问题所属领域
    private String answeredTime;//提问时间

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
    }

    public String getQuestionCateId() {
        return questionCateId;
    }

    public void setQuestionCateId(String questionCateId) {
        this.questionCateId = questionCateId;
    }

    public String getAnsweredTime() {
        return answeredTime;
    }

    public void setAnsweredTime(String answeredTime) {
        this.answeredTime = answeredTime;
    }
}
