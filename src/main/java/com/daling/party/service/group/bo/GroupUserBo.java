package com.daling.party.service.group.bo;

import lombok.Data;

/**
 * @author jiwei.xue
 * @date 2019/4/10 12:23
 */
@Data
public class GroupUserBo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 参团人员头像
     */
    private String image;

    /**
     * 是否新
     */
    private Integer isNew;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户角色 1 团长 2 成员 3 插入者
     */
    private Integer userRole;

}
