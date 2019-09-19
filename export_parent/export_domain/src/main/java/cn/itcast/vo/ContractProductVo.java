package cn.itcast.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class ContractProductVo implements Serializable {
    private String customName;
    private String contractNo;
    private String productNo;
    private Integer cnumber;
    private String factoryName;
    private Date deliveryPeriod;
    private Date shipTime;
    private String tradeTerms;
}
