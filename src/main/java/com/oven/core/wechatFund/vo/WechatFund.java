package com.oven.core.wechatFund.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * 微信基金
 *
 * @author Oven
 */
@Data
public class WechatFund {

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "data_date")
    private String dataDate;
    @Column(name = "money")
    private Double money;

}
