package com.daling.party.repository.dao;


import com.daling.party.domain.entity.NewShopWelfare;
import com.daling.party.infrastructure.base.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public interface NewShopWelfareDao extends BaseDao<NewShopWelfare> {


	/**
	 * 根据shopId获取福利信息
	 *
	 * @param shopId
	 * @return
	 */
	NewShopWelfare getWelfare(@Param("shopId") Long shopId);

	/**
	 * 初始化福利信息
	 *
	 * @param welfare
	 */
	long initWelfare(NewShopWelfare welfare);

	/**
	 * 更新福利信息
	 *
	 * @param welfare
	 * @return
	 */
	int updateWelfare(NewShopWelfare welfare);

}
