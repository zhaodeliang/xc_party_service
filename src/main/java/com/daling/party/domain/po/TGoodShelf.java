package com.daling.party.domain.po;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class TGoodShelf implements Serializable {

	private static final long serialVersionUID = 2967765056999401600L;

	private Long id;
	
	private BigDecimal salePrice;
	
	private BigDecimal marketPrice;
	
	private Long limitedStock;
	
	private BigDecimal benefitRate;
	
	private BigDecimal benefitMoney;
	
	private String status;
	
	private Date shelfOnTime;
	
	private Date shelfOffTime;
	
	private Long goodId;
	
	private String sku;

	private Integer useErpYn;
	
	private Date createdDate;
	
	private Long creatorId;
	
	private String creatorName;
	
	private String goodDesc;
	
	private String spu;
	
	private Date modiDate;
	
	private Long modifierId;
	
	private String modifierName;
	
	private String shipmentType;
	
	private String brandName;
	
	private Long index;
	
	private Long cateId1;
	
	private Long cateId2;
	
	private Long salesQty = new Long(0);

	private Long buyLimitNum;
	
	private String isBuyLimit;

	private Integer topYn;

	private BigDecimal totalWeight;
	
	/**
	 * 最大使用金币
	 */
	private BigDecimal stunnerCurrency;
	
	private Date salesTime;

	private Integer seckillYn;
	
	private String extInfo;
	
	private String stunnerInfo;

	private Date salesEndTime;

	private Integer specifiedAreaYn;
}

