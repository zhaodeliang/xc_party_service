package com.daling.party.infrastructure.utils;

import com.daling.common.ssoclient.util.DMonitorAgent;
import com.daling.party.infrastructure.model.ResultVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class XcHeadWrapper<T> {
    @JsonIgnore
    private static final ObjectMapper mapper = new ObjectMapper();
    @JsonIgnore
    private static final SimpleDateFormat DAT_FMT = new SimpleDateFormat("yyMMdd\'T\'HHmmss.SSS\'Z\'");
    private String version = "1.0";
    private String timestamp = DateTime.now().toString("yyMMdd\'T\'HHmmss.SSS\'Z\'");
    private int status;
    private String errorMsg;
    private long elapsed;
    private String trackId;
    private T data;
    @JsonIgnore
    private long stopWatch = System.currentTimeMillis();

    public XcHeadWrapper() {
        this.status = XcHeadWrapper.StatusEnum.UnknownOther.getCode();
        this.errorMsg = "";
        this.elapsed = 0L;
        this.data = null;
    }

    public static XcHeadWrapper<?> parsingObject(String jsonString) throws JsonParseException, JsonMappingException, IOException {
        return null == jsonString?null:(XcHeadWrapper)mapper.readValue(jsonString, XcHeadWrapper.class);
    }

    public String toJsonString() {
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(DAT_FMT);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "toJsonString error ";
        }
    }

    public long timeWatchStop() {
        this.elapsed = System.currentTimeMillis() - this.stopWatch;
        return this.elapsed;
    }

    public String getVersion() {
        return this.version;
    }

    public String getTimestamp() {
        return this.timestamp;
    }


    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getElapsed() {
        return this.elapsed;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public XcHeadWrapper<T> success (T data) {
        this.setStatus(StatusEnum.Success.getCode());
        this.setData(data);
        return this;
    }

    public XcHeadWrapper<T> error (int code, String message) {
        this.setStatus(code);
        this.setErrorMsg(message);
        return this;
    }

    public static enum StatusEnum {
        Success(0, "全部成功"),
        Success4M(200, "全部成功"),
        PartialSuccess(202, "库存数不足"),
        AuthFailed(403, "走神了，再来一次吧"),
        AuthNotFound(444, "未能找到的用户"),
        Timeout(405, "接口超时返回"),
        OverFlowCtrl(406, "客户流量超量"),
        Failed(500, "全部失败"),
        ParamError(505, "传入参数错误"),
        Forbidden(503, "权限不足"),
        UnknownOther(599, "未知错误,系统错误"),
        AsyDelayProblem(600, "异步延迟问题");


        private int code;
        private String desc;

        public int getCode() {
            return this.code;
        }

        public void setCode(int value) {
            this.code = value;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private StatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


}
