package com.daling.party.infrastructure.interceptor;

import com.daling.party.controller.dto.UserDto;
import com.daling.party.infrastructure.enums.AppEnum;
import com.daling.party.infrastructure.enums.ClientTypeEnum;
import com.daling.party.infrastructure.model.ResultVO;
import com.daling.party.infrastructure.utils.WebUtil;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.enums.UserTypeEnum;
import com.daling.ucclient.pojo.HeaderConstant;
import com.daling.ucclient.pojo.Shop;
import com.daling.ucclient.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Component
public class UserArgumentResolver implements WebArgumentResolver {



    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (methodParameter != null && methodParameter.getParameterType().equals(UserDto.class)) {
            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            ResultVO<UserDto> userDtoResultVO = authUser(request);
            UserDto userDto = null;
            if (userDtoResultVO.isRetBool()) {
                userDto = userDtoResultVO.getT();
            }
            if (userDto != null) {
                log.debug("userDto: uid:{}, ivCode:{}, flCode:{}, hCode:{}", userDto.getId(), userDto.getInvitationCode(), userDto.getFollowerInviteCode(), userDto.getHeaderInviteCode());
            } else {
                log.debug("userDto={}", userDto);
            }
            return userDto;
        } else {
            log.debug("LoginUser=UNRESOLVED");
            return UNRESOLVED;
        }
    }
    public ResultVO<UserDto> authUser(HttpServletRequest request) {
        ResultVO<UserDto> resultVO = new ResultVO<>();
        do {
            String thirdSession = request.getHeader(HeaderConstant.UToken);
            String platform = request.getHeader(HeaderConstant.Platform);
            String app = request.getHeader(HeaderConstant.App);
            try {
                if (StringUtils.isBlank(request.getHeader(HeaderConstant.UID))) {
                    resultVO.format(false, "参数错误");
                    break;
                }
                Long uid = (Long) request.getAttribute(HeaderConstant.DecryptUID);
                log.info("DecryptUID uid "+uid);
                if (uid == null) {
                    uid = UserShopClient.decryptUid(request.getHeader(HeaderConstant.UID));
                }
                log.info("HeaderConstant uid "+uid);
                if (uid == null) {
                    resultVO.format(false, "用户不存在或者登陆已过期");
                    break;
                }
                User user = UserShopClient.getUser(uid);
                if (user == null) {
                    resultVO.format(false, "用户不存在或者登陆已过期");
                    break;
                }
                log.info("user uid "+user);
                UserDto userDto = new UserDto(user);
                if (StringUtils.isNotBlank(request.getHeader("province")) && StringUtils.isNotBlank(request.getHeader("city")) && StringUtils.isNotBlank(request.getHeader("district"))) {
                    if (!"undefined".equals(request.getHeader("province")) && !"undefined".equals(request.getHeader("city")) && !"undefined".equals(request.getHeader("district"))) {
                        userDto.setProvince(URLDecoder.decode(request.getHeader("province"), "UTF-8"));
                        userDto.setCity(URLDecoder.decode(request.getHeader("city"), "UTF-8"));
                        userDto.setDistrict(URLDecoder.decode(request.getHeader("district"), "UTF-8"));
                    }
                }
                userDto.setInBacklistYn(user.getInBacklistYn());
                userDto.setUserType(user.getUserType());
                userDto.setClientIp(WebUtil.getClientIp(request));
                if (user.getUserType() != null && user.getUserType() != UserTypeEnum.FANS) {
                    if (user.getUserType() == UserTypeEnum.SHOPKEEPER) {
                        Shop shop = UserShopClient.getShopByOwnerId(user.getId());
                        userDto.setHasShop(true);
                        userDto.setShopId(shop.getId());
                        userDto.setShopCode(shop.getCode());
                        userDto.setInvitationCode(shop.getInviteCode());
                    } else if(userDto.getUserType() == UserTypeEnum.VIP) {
                        Shop shop = UserShopClient.getShopByInviteCode(user.getFollowerInviteCode());
                        if (shop != null) {
                            userDto.setHasShop(false);
                            userDto.setFollowerShopId(shop.getId());
                            userDto.setFollowerShopCode(shop.getCode());
                            userDto.setFollowerShopName(shop.getName());
                        }
                    }
                }
                ClientTypeEnum clientTypeEnum = ClientTypeEnum.convertFromPlatform(platform);
                if (ClientTypeEnum.WxTouch.equals(clientTypeEnum)) {
                    userDto.setOpenid(user.getTouchOpenid());
                }
                if(StringUtils.isBlank(app)) {
                    userDto.setApp(AppEnum.APPSTORE.getCode());
                } else {
                    userDto.setApp(app);
                }
                userDto.setuToken(thirdSession);
                userDto.setPlatform(platform);
                userDto.setHeaderInviteCode(request.getParameter("inviteCode"));
                userDto.setHeaderUid(request.getHeader(HeaderConstant.UID));
                userDto.setHeaderDeviceid(request.getHeader(HeaderConstant.DeviceId));
                String strAppVersion = request.getHeader(HeaderConstant.AppVersion);
                if (StringUtils.isNotBlank(strAppVersion)) {
                    strAppVersion = StringUtils.trimToEmpty(strAppVersion);
                    int version = 0;
                    if (strAppVersion.matches("^\\d$")) {
                        version = Integer.parseInt(StringUtils.join(strAppVersion, ".0.0").replaceAll("\\.", ""));
                    } else if (strAppVersion.matches("^\\d\\.\\d$")) {
                        strAppVersion = StringUtils.join(strAppVersion, ".0").replaceAll("\\.", "");
                        version = Integer.parseInt(strAppVersion);
                    } else if (strAppVersion.matches("^\\d\\.\\d\\.\\d$")) {
                        strAppVersion = strAppVersion.replaceAll("\\.", "");
                        version = Integer.parseInt(strAppVersion);
                    }
                    userDto.setAppVersion(version);
                }
                resultVO.format(true, "获取成功", userDto);
            } catch (Exception e) {
                log.error("error", e);
                resultVO.format(false, "未获取到用户信息");
                break;
            }
        } while (false);

        return resultVO;
    }
}
