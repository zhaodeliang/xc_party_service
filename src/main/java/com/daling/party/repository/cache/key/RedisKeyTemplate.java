package com.daling.party.repository.cache.key;

import org.apache.commons.lang3.StringUtils;

public class RedisKeyTemplate {

    public static final String PT_HANDLER_VALID_GROUP_MEMBER = "pt_handler_valid_group_member";

    public static final String PT_LOCK_NAME = "pt_send_remaining_message_task";

    /**
     * 参团key
     */
    public static final String PT_JOIN_GROUP_KEY = "pt_join_group_key:%s";

    /**
     * 创建分享团的key
     */
    public static final String PT_CREATE_SHARE_GROUP_KEY = "pt_create_share_group_key:%s_%s_%s";

    /**
     * 正在拼团的个数key
     *
     * @param userId
     * @return
     */
    public static String currentGroupListKey(Long userId) {
        return StringUtils.join("pt_current_list_", userId);
    }

    /**
     * group_list_activity_code
     */
    public static final String GROUP_LIST_ACTIVITY_CODE_KEY = "group_list_activity_code:%s-%s-%s-%s-%s";

    /**
     * 团key
     */
    public static final String PT_GROUP_INFO_KEY = "pt_group_info_key:%s";

    /**
     * 团key 不包含draft
     */
    public static final String PT_GROUP_INFO_NO_DRAFT_KEY = "pt_group_info_no_draft_key:%s";


    /**
     * 团成员列表key
     */
    public static final String PT_GROUP_MEMBER_LIST_KEY = "pt_group_member_list_key:%s:%s";


}
