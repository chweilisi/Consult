package com.cheng.consult.db.table;

import java.io.Serializable;

/**
 * Created by cheng on 2017/11/27.
 */

public class Case implements Serializable {

    //@Id(autoincrement = true)
    private Long CaseId;//案例id

    private Long OwnerId;//所属专家id
    private Long OwnerUserId;//所属用户id
    private String CaseTime;//项目时间
    private String CorpArea;//企业所在区域
    private String CorpIndustry;//企业所处行业
    private String CorpNature;//企业性质
    private String CorpType;//企业经营类型
    private Long    CorpSaleAmout;//企业年销售额
    private String SaleArea;//销售区域
    private String SaleMode;//销售模式
    private int    CorpEmployeNum;//企业总人数
    private String QuestionType;//管理问题类类型
    private String QuestionDes;//管理问题简述
    private String ImpleEffort;//项目实施效果


}
