package com.daling.party.infrastructure.model;

import com.daling.common.hotconfig.client.ZKConfig;
import com.daling.common.hotconfig.client.ZKConfigBuilder;

public class CommonConstant {

	/**
	 * hotconfig
	 */
	public static ZKConfig zkconfig = ZKConfigBuilder.newBuilder().setAppPath("XC_PARTY").build();

	public static String getHotConfigValue(String key, String defaultValue) {
		if (zkconfig == null) {
			return defaultValue;
		}
		try {
			return zkconfig.getValue(key, defaultValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
}
