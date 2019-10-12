package com.daling.party.controller.dto;

import com.daling.common.configuration.SysGlobalConfig;
import com.daling.ucclient.enums.UserTypeEnum;
import com.daling.ucclient.pojo.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class UserDto implements java.io.Serializable  {

    /** id */
    private Long id;

    /** 是否实名认证 */
    private Integer certificationYn;

    /** 真实姓名 */
    private String realName;

    /** 身份证号 */
    private String cardNo;

    /** 微信用户openid */
    private String openid;

    /** 小程序aped */
    private String appid;

    /** 微信用户unionid */
    private String unionid;

    /** 关注状态 */
    private Integer subscribeYn;

    /** 关注时间 */
    private Date subscribeTime;

    /** 取关时间 */
    private Date unsubscribeTime;

    /** 昵称 */
    private String nickName;

    /** country */
    private String country;

    /** province */
    private String province;

    /** city */
    private String city;

    /**district*/
    private String district;

    /** 头像 */
    private String headimgurl;

    /** 关联手机号 */
    private String mobile;

    /** 性别 */
    private Integer sex;

    /** created_date */
    private Date createdDate;

    /** creator_id */
    private Long creatorId;

    /** creator_name */
    private String creatorName;

    /** modi_date */
    private Date modiDate;

    /** modifier_id */
    private Long modifierId;

    /** modifier_name */
    private String modifierName;

    /** comments */
    private String comments;

    /** 用户是否有店铺*/
    private boolean hasShop;

    /** 用户店铺的ID */
    private Long shopId;

    /** 用户店铺Code*/
    private String shopCode;
    /** 用户店铺邀请码 */
    private String invitationCode;

    private String clientIp;

    /** 自己用的，并不会在鉴权的时候给你们 */
    private String uToken;

    /** 前端请求Header中的 invite-code */
    private String headerInviteCode;

    /** 客户端来源 **/
    private String platform;

    /** 请求header中传入的uid, 可能是混淆值**/
    private String headerUid;

    /** 设备Id **/
    private String headerDeviceid;

    /** App版本号 **/
    private int appVersion;
    /** 允许最大收货地址数: 默认值10*/
    private Integer maxReceiveAddressNum = 10;

    /**app使用客户端*/
    private String app;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    /**归属店铺信息*/
    private Long followerShopId;
    private String  followerShopCode;
    private String followerShopName;

    /** 受邀请码 */
    private String followerInviteCode;
    /**
     * 是否加入黑名单
     */
    private Integer inBacklistYn;
    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 版本兼容使用 版本递增
     */
    private String  version;


    public UserDto() {}

    public UserDto(User user) {
        if (user == null) {
            return;
        }
        this.id = user.getId();
        this.appid = user.getAppid();
        this.cardNo = user.getCardNo();
        this.certificationYn = user.getCertificationYn();
        this.city = user.getCity();
        this.comments = user.getComments();
        this.country = user.getCountry();
        this.createdDate = user.getCreatedDate();
        this.creatorId = user.getCreatorId();
        this.creatorName = user.getCreatorName();
        this.nickName = user.getNickName();
        this.followerInviteCode = user.getFollowerInviteCode();
        if (StringUtils.isBlank(user.getHeadimgurl())) {
            this.headimgurl = SysGlobalConfig.getInstance().getString("shop_index_default_img_url");
        } else {
            this.headimgurl = user.getHeadimgurl();
        }
        this.mobile = user.getMobile();
        this.openid = user.getOpenid();
        this.unionid = user.getUnionid();
        this.sex = user.getSex();
        this.modiDate = user.getModiDate();
        this.realName = user.getRealName();
        this.maxReceiveAddressNum = user.getMaxReceiveAddressNum();
        this.inBacklistYn = user.getInBacklistYn();
        this.status = user.getStatus();
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Integer getCertificationYn() {
        return certificationYn;
    }


    public void setCertificationYn(Integer certificationYn) {
        this.certificationYn = certificationYn;
    }


    public String getRealName() {
        return realName;
    }


    public void setRealName(String realName) {
        this.realName = realName;
    }


    public String getCardNo() {
        return cardNo;
    }


    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


    public String getOpenid() {
        return openid;
    }


    public void setOpenid(String openid) {
        this.openid = openid;
    }


    public String getAppid() {
        return appid;
    }


    public void setAppid(String appid) {
        this.appid = appid;
    }


    public String getUnionid() {
        return unionid;
    }


    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }


    public Integer getSubscribeYn() {
        return subscribeYn;
    }


    public void setSubscribeYn(Integer subscribeYn) {
        this.subscribeYn = subscribeYn;
    }


    public Date getSubscribeTime() {
        return subscribeTime;
    }


    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }


    public Date getUnsubscribeTime() {
        return unsubscribeTime;
    }


    public void setUnsubscribeTime(Date unsubscribeTime) {
        this.unsubscribeTime = unsubscribeTime;
    }


    public String getNickName() {
        return nickName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getProvince() {
        return province;
    }


    public void setProvince(String province) {
        this.province = province;
    }


    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getHeadimgurl() {
        return headimgurl;
    }


    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }


    public String getMobile() {
        return mobile;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public Integer getSex() {
        return sex;
    }


    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public String getFollowerInviteCode() {
        return followerInviteCode;
    }


    public void setFollowerInviteCode(String followerInviteCode) {
        this.followerInviteCode = followerInviteCode;
    }


    public Date getCreatedDate() {
        return createdDate;
    }


    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public Long getCreatorId() {
        return creatorId;
    }


    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public String getCreatorName() {
        return creatorName;
    }


    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }


    public Date getModiDate() {
        return modiDate;
    }


    public void setModiDate(Date modiDate) {
        this.modiDate = modiDate;
    }


    public Long getModifierId() {
        return modifierId;
    }


    public void setModifierId(Long modifierId) {
        this.modifierId = modifierId;
    }


    public String getModifierName() {
        return modifierName;
    }


    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }


    public String getComments() {
        return comments;
    }


    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public boolean isHasShop() {
        return hasShop;
    }

    public void setHasShop(boolean hasShop) {
        this.hasShop = hasShop;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getuToken() {
        return uToken;
    }

    public void setuToken(String uToken) {
        this.uToken = uToken;
    }

    public String getHeaderInviteCode() {
        return headerInviteCode;
    }

    public void setHeaderInviteCode(String headerInviteCode) {
        this.headerInviteCode = headerInviteCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHeaderUid() {
        return headerUid;
    }

    public void setHeaderUid(String headerUid) {
        this.headerUid = headerUid;
    }

    public String getHeaderDeviceid() {
        return headerDeviceid;
    }

    public void setHeaderDeviceid(String headerDeviceid) {
        this.headerDeviceid = headerDeviceid;
    }

    public Integer getMaxReceiveAddressNum() {
        return maxReceiveAddressNum;
    }

    public void setMaxReceiveAddressNum(Integer maxReceiveAddressNum) {
        this.maxReceiveAddressNum = maxReceiveAddressNum;
    }



    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }


    public int getAppVersion (){
        return this.appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public Long getFollowerShopId() {
        return followerShopId;
    }

    public void setFollowerShopId(Long followerShopId) {
        this.followerShopId = followerShopId;
    }

    public String getFollowerShopCode() {
        return followerShopCode;
    }

    public void setFollowerShopCode(String followerShopCode) {
        this.followerShopCode = followerShopCode;
    }

    public String getFollowerShopName() {
        return followerShopName;
    }

    public void setFollowerShopName(String followerShopName) {
        this.followerShopName = followerShopName;
    }

    public Integer getInBacklistYn() {
        return inBacklistYn;
    }

    public void setInBacklistYn(Integer inBacklistYn) {
        this.inBacklistYn = inBacklistYn;
    }


    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", inBacklistYn=" + inBacklistYn +
                ", certificationYn=" + certificationYn +
                ", realName='" + realName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", openid='" + openid + '\'' +
                ", appid='" + appid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", subscribeYn=" + subscribeYn +
                ", subscribeTime=" + subscribeTime +
                ", unsubscribeTime=" + unsubscribeTime +
                ", nickName='" + nickName + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sex=" + sex +
                ", followerInviteCode='" + followerInviteCode + '\'' +
                ", followerShopId='" + followerShopId + '\'' +
                ", followerShopCode='" + followerShopCode + '\'' +
                ", followerShopName='" + followerShopName + '\'' +
                ", createdDate=" + createdDate +
                ", creatorId=" + creatorId +
                ", creatorName='" + creatorName + '\'' +
                ", modiDate=" + modiDate +
                ", modifierId=" + modifierId +
                ", modifierName='" + modifierName + '\'' +
                ", comments='" + comments + '\'' +
                ", hasShop=" + hasShop +
                ", shopCode='" + shopCode + '\'' +
                ", invitationCode='" + invitationCode + '\'' +
                ", clientIp=" + clientIp +
                ", headerInviteCode=" + headerInviteCode +
                ", app=" + app +
                ", platform=" + platform +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
