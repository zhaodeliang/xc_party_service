package com.daling.party.repository.dao;


import com.daling.party.domain.entity.NewShopTask;
import com.daling.party.domain.entity.NewShopWelfare;
import com.daling.party.infrastructure.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component
public interface NewShopTaskDao extends BaseDao<NewShopTask> {


	/**
	 * 按照完成时间排序
	 * @param shopId
	 * @return
	 */
	List<NewShopTask> queryNewShopTask(@Param("shopId") Long shopId);

	/**
	 * 更新任务列表
	 * @param newShopTask
	 * @return
	 */
	int updateTask(NewShopTask newShopTask) throws SQLException;

	/**
	 * 插入任务数据
	 * @param task
	 * @return
	 */
	long insertTask(NewShopTask task);
}
