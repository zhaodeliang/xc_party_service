package com.daling.party.infrastructure.exception;

import com.daling.common.ssoclient.util.DMonitorAgent;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionAdvices {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public XcHeadWrapper exceptionHandler(Exception ex) {
        XcHeadWrapper headerWrapper = new XcHeadWrapper();
        if (ex instanceof GenericBusinessException) {
            GenericBusinessException e = (GenericBusinessException) ex;
            log.warn("code:{},message:{}", e.getErrorCode(), e.getMessage());
            headerWrapper = headerWrapper.error(XcHeadWrapper.StatusEnum.UnknownOther.getCode(), e.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            String message = validationError(e);
            log.warn("message:{}", e.getMessage());
            headerWrapper = headerWrapper.error(XcHeadWrapper.StatusEnum.ParamError.getCode(), message);
        } else {
            log.warn("message:{},e:{}", ex.getMessage(), ex);
            headerWrapper = headerWrapper.error(XcHeadWrapper.StatusEnum.UnknownOther.getCode(), XcHeadWrapper.StatusEnum.UnknownOther.getDesc());
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.error("exceptionHandler requestURI={}",request.getRequestURI());
        DMonitorAgent.recordOne(request.getRequestURI() + "_error");
        return headerWrapper;
    }

    /**
     * 获取参数message
     *
     * @param ex
     * @return
     */
    private String validationError(MethodArgumentNotValidException ex) {
        log.error("raised MethodArgumentNotValidException : " + ex);
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors)) {
            return "参数异常";
        }
        return fieldErrors.get(0).getDefaultMessage();
    }
}