package com.daling.party.service.group;

import com.daling.party.infrastructure.model.ResultVO;

import java.util.concurrent.ExecutionException;

/**
 * Created by lilindong on 2019/4/11.
 *
 */
public interface GroupTaskService {

    /**
     * 发送截止前2小时消息
     * @return
     */
    ResultVO<String> sendRemainingMessage();

    /**
     * 发货、退款处理
     * @Author dl.zhao
     * @return
     */
    ResultVO handlerValidGroupMember();

}
