package com.daling.party.feign.to;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jiwei.xue
 * @date 2019/4/12 14:27
 */
@Data
public class GoodDetailTo implements Serializable {

    private static final long serialVersionUID = -8334564855497673086L;

    /** id */
    private Long id;

    private String goodsId;

    private String goodsName;

    private String goodsShowName;

    private String goodsShowDesc;

    private String sku;

    private Map<String,Object> detail;

    private JSONObject treeInfo;

    private String image;

    /**
     * 缩略图
     */
    private String shareImage;
    /**
     * 新规格图1
     */
    private String specImage1;
    /**
     * 销售价
     */
    private BigDecimal salePrice;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 分润金额
     */
    private String benefitMoney;
    /**
     * 折扣率
     */
    private BigDecimal discount;

    private String deliveryCountry;
    /**
     * 允许售卖库存
     */
    private Long limitStock;
    /**
     * 库存信息
     */
    private String goodsStockMsg;

    /**
     * 限购数量
     */
    private Long buyLimitNum;
    private String buyLimitMsg;
    /**
     * 是否限购
     */
    private String isBuyLimit;

    /**直邮属性1 - 达令发货 2 - 海外直邮 3 - 代理商发货**/
    private Integer isForeignSupply;

    /**是否报税**/
    private Integer isInBond;

    private String userRole;

    private String goodsInviteCode;

    private String shopInviteCode;
    private String shopHadeImage;
    private String shopName;
    private String shopDesc;
    /**
     * 邀请人店铺名称及邀请码
     */
    private String followerInviteCode;
    private String followerShopName;
    /**税率*/
    private BigDecimal taxRate;

    /**税费*/
    private BigDecimal taxation;

    /**
     * 最大使用金币
     */
    private BigDecimal stunnerCurrency;
    private Integer isShowStunner;
    private List<GoodsLabelTo> labelList = Lists.newArrayList();
    /**
     * 开始时间
     */
    private long salesTime;
    private String salesTimeStr;
    private long salesTimeDiff;

    private Integer canReturn;
    private String skuType;
    private String skuTypeTwo;
    /**
     * 外部信息，苏宁商品活动结束时间
     */
    private long ext_end_time;
    private long ext_sub_time;
    /**
     * 是否深库存
     */
    private String is_deep_stock;
    /**
     * 结束时间
     */
    private long salesEndTime;
    private String salesEndTimeStr;
    private long salesEndTimeDiff;
    //增加是否下架的标志  "1" 已下架     "0"没下架
    private String flag="0";
    //新增spu 字段
    private String spu;

    /**是否预售*/
    private String preSaleYn;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 运费详情
     */
    private Map<String, Object> shipFeeDetail;


    private String vipCouponName;
    private BigDecimal vipCouponMoney;


    //新增分享标题
    private String shareTitle;
    //新增 分享描述
    private String shareDesc;

    private String bondedPayerSwitchOnYn;

    private String marketingActivityType;

    //新增 商品原图
    private String original_img;

    //销售量
    private Integer salesVolume;

    //浏览量
    private Integer browsingVolume;


    // 是否在时间轴中 0-不在； 1-在
    private Integer isInTimeline;

    // 是否是苏宁商品：0-不是；1-是
    //private Integer isSuningGoods = 0;

    // 活动类型 1：直降, 2：秒杀 3：阶梯满减
    private Integer activityType;

    // 活动状态  5：预热中；6：活动中；7：已结束
    private Integer activityStatus;

    // 活动规则：满XX减XX（如果活动类型为阶梯满减）
    private List<String> activityRule;

    //时间轴状态 0：未开始  1：预热中 2：抢购中 3：已删除  99：已结束
    private Integer timelineStatus;

    /**
     * 前端倒计时展示类型：1：即将开抢；2：限时特卖；3：限时秒杀；4：限时直降；5：满减
     */
    private Integer labelShowType;

    private String originalImg;

    //是否精选商品,1是0否
    private Integer choiceFlag;
    //是否允许作为普通商品售卖,1是0否
    private Integer canBeCommon;


    /**
     * 分页用商品下标数
     */
    private int index;

    /**
     * 促销活动短标题
     */
    private String shortTitle;

    /**
     * 促销活动编码
     */
    private String activityCode;

}
