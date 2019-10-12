package com.daling.party.domain.vo;

import com.daling.party.infrastructure.utils.AssertUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by lilindong on 2019/4/8.
 */
@Getter
@Setter
@NoArgsConstructor
public class PageResultVO {

    private int start; // 分页开始下标
    private boolean isHasNext; // 是否拥有下一页
    private List<?> data; // 列表数据

}
