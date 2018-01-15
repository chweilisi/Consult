package com.cheng.consult.db.table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by cheng on 2017/12/1.
 */

public class SubjectItem implements Serializable {
    //@Id(autoincrement = true)
    private Long SubjectItemId;//内容id
    private int expertId;//此条回答专家的id
    private int ItemType;//0:问题   1:回答
    private Long OwnerSubjectId;//内容所属问题id 关联Subject表的SubjectId
    private Date Date;//日期
    private String Content;//问题

    public Long getSubjectItemId() {
        return SubjectItemId;
    }

    public void setSubjectItemId(Long subjectItemId) {
        SubjectItemId = subjectItemId;
    }

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public Long getOwnerSubjectId() {
        return OwnerSubjectId;
    }

    public void setOwnerSubjectId(Long ownerSubjectId) {
        OwnerSubjectId = ownerSubjectId;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
