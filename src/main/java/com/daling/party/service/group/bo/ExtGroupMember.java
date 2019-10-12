package com.daling.party.service.group.bo;

import com.daling.party.domain.entity.GroupMember;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExtGroupMember extends GroupMember {

    /**团编码*/
    private String groupCode;

    /**团编码*/
    private Integer groupStatus;

    /**团编码*/
    private Date endTime;

    /**剩余的数量（lower_limit每次参团-1直到为0）*/
    private Integer surplusNum;

    @Override
    public String toString() {
        return "ExtGroupMember{" +
                "id='" + super.getId() + '\'' +
                ",groupCode='" + groupCode + '\'' +
                ",userId='" + super.getUserId() + '\'' +
                ",groupId='" + super.getGroupId() + '\'' +
                ",orderNo='" + super.getOrderNo() + '\'' +
                ",groupRole='" + super.getGroupRole() + '\'' +
                ", groupStatus=" + groupStatus +
                '}';
    }
}
