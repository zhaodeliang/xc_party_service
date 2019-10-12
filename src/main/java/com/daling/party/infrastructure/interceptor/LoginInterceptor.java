package com.daling.party.infrastructure.interceptor;

import com.alibaba.fastjson.JSON;
import com.daling.party.infrastructure.utils.IPAddressUtil;
import com.daling.party.infrastructure.utils.WebUtil;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.pojo.AuthInfo;
import com.daling.ucclient.pojo.HeaderConstant;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.testng.collections.Lists;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Log
public class LoginInterceptor implements HandlerInterceptor {

    //不检查的url
    @Value("${party.uncheck.url.prefix}")
    private String uncheckedPrefix;
    //配置不拦截登陆校验的url前缀
    private List<String> uncheckedUrlPrefix=Lists.newArrayList();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!StringUtils.equals(request.getRequestURI(), "/xc_party/health/check")) {
            String requestMsg = String.format("请求信息：{路径:\"%s\", 参数:%s, uToken:\"%s\", ip: %s}", request.getRequestURI(),
                    JSON.toJSONString(request.getParameterMap()), request.getHeader(HeaderConstant.UToken), IPAddressUtil.getClientIPAddress(request));
            log.info(requestMsg);
        }
        if(!uncheckedUrlPrefix.isEmpty()){
            String realUri = WebUtil.getNoAppNamedRequestURI(request);
            for(String s:uncheckedUrlPrefix){
                if (realUri.startsWith(s.trim())) {
                    return true;
                }
            }
        }
        return authImpl(request, response, false);

    }

    private boolean authImpl(HttpServletRequest request, HttpServletResponse response, boolean bMngURI) throws Exception {
        AuthInfo authInfo;
        String uToken = request.getHeader(HeaderConstant.UToken);
        if (StringUtils.isBlank(uToken) || StringUtils.equals(uToken, "null") || StringUtils.equals(uToken, "undefined")) {
            authInfo = new AuthInfo();
            authInfo.setbAuthSuccess(Boolean.FALSE);
        } else if (bMngURI) {
            authInfo = UserShopClient.authGJ(request);
        } else {
            authInfo = UserShopClient.auth(request);
        }
        if (authInfo == null || !authInfo.isbAuthSuccess()) {
            return false;
        }
        request.setAttribute(HeaderConstant.DecryptUID, authInfo.getId());
        return true;
    }
    @PostConstruct
    private void initUnCheckUrl(){
        //理论上第一次请求会进行初始化赋值
        if(StringUtils.isNotBlank(uncheckedPrefix)){
            String[] str=uncheckedPrefix.split(",");
            uncheckedUrlPrefix= Arrays.asList(str);
        }
    }
}
