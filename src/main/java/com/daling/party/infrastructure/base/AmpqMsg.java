package com.daling.party.infrastructure.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by lilindong on 2019/4/11.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmpqMsg {
    protected String track_id;
    protected Long event_time;
    protected String code;
    protected Object body;
    protected String client_ip;
}
