package com.daling.party.infrastructure.base;

import com.daling.party.domain.group.enums.GroupJoinStatusEnum;
import com.daling.party.infrastructure.utils.XcHeadWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
public class BaseController {

    @Autowired
    protected HttpServletRequest request;

    public <T> XcHeadWrapper<T> initJsonHeaderWrapper() {
        String trackId = StringUtils.isBlank(request.getParameter("trackId")) ? UUID.randomUUID().toString() : request.getParameter("trackId");
        return this.initJsonHeaderWrapper(trackId);
    }

    public <T> XcHeadWrapper<T> initJsonHeaderWrapper(String trackId) {
        XcHeadWrapper<T> jsonWrapper = new XcHeadWrapper<>();
        jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Success.getCode());
        jsonWrapper.setErrorMsg(XcHeadWrapper.StatusEnum.Success.getDesc());
        if (StringUtils.isNotBlank(trackId))
            jsonWrapper.setTrackId(trackId);

        return jsonWrapper;
    }


    public <T> XcHeadWrapper<T> initJsonHeaderWrapper(String trackId, String defaultTrackId) {
        XcHeadWrapper<T> jsonWrapper = new XcHeadWrapper<>();
        jsonWrapper.setStatus(XcHeadWrapper.StatusEnum.Success.getCode());
        jsonWrapper.setErrorMsg(XcHeadWrapper.StatusEnum.Success.getDesc());
        if (StringUtils.isNotBlank(trackId))
            jsonWrapper.setTrackId(trackId);
        else
            jsonWrapper.setTrackId(defaultTrackId);

        return jsonWrapper;
    }

    protected  <T> XcHeadWrapper<T> buildResponse(T i) {
        XcHeadWrapper<T> xcHeadWrapper = initJsonHeaderWrapper();
        xcHeadWrapper.success(i);
        return xcHeadWrapper;
    }

    protected  <T> XcHeadWrapper<T> buildResponse() {
        XcHeadWrapper<T> xcHeadWrapper = initJsonHeaderWrapper();
        xcHeadWrapper.success(null);
        return xcHeadWrapper;
    }

    protected  <T> XcHeadWrapper<T> buildFailureResponse() {
        XcHeadWrapper<T> xcHeadWrapper = initJsonHeaderWrapper();
        xcHeadWrapper.setStatus(XcHeadWrapper.StatusEnum.UnknownOther.getCode());
        xcHeadWrapper.setErrorMsg(XcHeadWrapper.StatusEnum.UnknownOther.getDesc());
        return xcHeadWrapper;
    }

    protected  <T> XcHeadWrapper<T> buildAsyResponse() {
        XcHeadWrapper<T> xcHeadWrapper = initJsonHeaderWrapper();
        xcHeadWrapper.setStatus(XcHeadWrapper.StatusEnum.AsyDelayProblem.getCode());
        xcHeadWrapper.setErrorMsg("请重新加载一下~");
        return xcHeadWrapper;
    }

    protected  <T> XcHeadWrapper<T> buildFailJoinResponse() {
        XcHeadWrapper<T> xcHeadWrapper = initJsonHeaderWrapper();
        xcHeadWrapper.setStatus(GroupJoinStatusEnum.JOIN_GROUP_FAIL.getCode());
        xcHeadWrapper.setErrorMsg("拼团失败");
        return xcHeadWrapper;
    }

}
