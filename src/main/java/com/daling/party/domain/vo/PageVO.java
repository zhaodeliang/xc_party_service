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
public class PageVO extends PageResultVO {

    private int pageIndex; // 当前页码
    private int pageSize; // 每页条数

    public void buildPageData(List<?> data) {
        if (data != null && data.size() == pageSize) {
            this.setHasNext(true);
            data.remove(pageSize -1);
        }
        this.pageSize --;
        this.setData(data);
    }

    public PageVO(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public void checkPageVO () {
        AssertUtil.isTrue(pageIndex <= 0 || pageSize <= 0, "分页参数错误");

        this.setStart((pageIndex - 1) * pageSize);
        this.pageSize ++;
    }
}
