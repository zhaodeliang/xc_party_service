package com.daling.party.infrastructure.interceptor;

import com.daling.party.infrastructure.utils.CommonUtils;
import com.daling.party.infrastructure.utils.IPAddressUtil;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * FileName: LogbackFilter
 *
 * @author: 赵得良
 * Date:     2019/6/10 0010 11:57
 * Description:
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*")
public class LogbackFilter implements Filter {


    private static final String UNIQUE_ID = "sessionTokenId";
    private final static String IP = "removeIP";
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        boolean bInsertMDC = insertMDC();
        try {
            chain.doFilter(request, response);
        } finally {
            if(bInsertMDC) {
                MDC.remove(UNIQUE_ID);
                MDC.remove(IP);
            }
        }
    }

    private boolean insertMDC() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MDC.put(IP, IPAddressUtil.getClientIPAddress(request));
        MDC.put(UNIQUE_ID, CommonUtils.getFlowNo());
        return true;
    }

    @Override
    public void destroy() {

    }

}
