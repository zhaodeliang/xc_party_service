package com.daling.party.infrastructure.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by lilindong on 2019/4/10.
 */
public interface BaseDao<T> extends Mapper<T>, MySqlMapper<T> {
}
