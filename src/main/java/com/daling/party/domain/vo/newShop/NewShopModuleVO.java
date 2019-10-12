package com.daling.party.domain.vo.newShop;

import com.daling.party.domain.vo.newShop.childVo.FirstOrderVo;
import com.daling.party.domain.vo.newShop.childVo.ProgressRateVo;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.domain.vo.newShop.childVo.TitleVo;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther : wangheming
 * @Date : 2019/07/23 10:31
 * @Description :新店主福利页 模块ABC vo
 */
public class NewShopModuleVO implements Serializable {
    private static final long serialVersionUID = -8347856955506193069L;

    /**
     * 标题模块
     */
    private TitleVo titleVo;

//    /**
//     * 开单奖励模块标题
//     */
//    private String firstOrderTitle;

    /**
     * 开单奖励下沉标志 0下沉 1:展示
     */
    private Integer partMove;

    /**
     * 开单奖励模块
     */
    private List<FirstOrderVo> firstOrderVos;

    /**
     * 进度模块
     */
    private ProgressRateVo progressRateVo;

    /**
     * 任务模块
     */
    private List<TaskVo> taskVos;

    /**
     * 奖励图
     */
    private String popupImgUrl;

    public Integer getPartMove() {
        return partMove;
    }

    public void setPartMove(Integer partMove) {
        this.partMove = partMove;
    }

    public TitleVo getTitleVo() {
        return titleVo;
    }

    public void setTitleVo(TitleVo titleVo) {
        this.titleVo = titleVo;
    }

    public List<FirstOrderVo> getFirstOrderVos() {
        return firstOrderVos;
    }

    public void setFirstOrderVos(List<FirstOrderVo> firstOrderVos) {
        this.firstOrderVos = firstOrderVos;
    }

    public ProgressRateVo getProgressRateVo() {
        return progressRateVo;
    }

    public void setProgressRateVo(ProgressRateVo progressRateVo) {
        this.progressRateVo = progressRateVo;
    }

    public List<TaskVo> getTaskVos() {
        return taskVos;
    }

    public void setTaskVos(List<TaskVo> taskVos) {
        this.taskVos = taskVos;
    }

    public String getPopupImgUrl() {
        return popupImgUrl;
    }

    public void setPopupImgUrl(String popupImgUrl) {
        this.popupImgUrl = popupImgUrl;
    }
}
