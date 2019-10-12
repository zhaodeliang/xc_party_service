package com.daling.party.infrastructure.utils;

import com.daling.party.controller.dto.UserDto;
import com.daling.ucclient.clients.UserShopClient;
import com.daling.ucclient.pojo.Shop;
import com.daling.ucclient.pojo.User;


public class GenUserUtil {
	public static UserDto gen(String trackId, UserDto user, String mobile) {
		if ("asdf".equals(trackId)) {
			GetUserInfoTestClass g = new GetUserInfoTestClass();
			user = gen(mobile);
		}
		return user;
	}

	public static UserDto gen(String trackId, UserDto user) {
		if ("asdf".equals(trackId)) {
			GetUserInfoTestClass g = new GetUserInfoTestClass();
			user = g.getUserInfo();
		} else {
			String[] split = trackId.split("~");
			if ("asdf".equals(split[0])) {
				GetUserInfoTestClass g = new GetUserInfoTestClass();
				user = g.getUserInfo(split[1]);
			}
		}

		return user;
	}

	private static UserDto gen(String mobile) {
		GetUserInfoTestClass g = new GetUserInfoTestClass();
		UserDto userInfo = g.getUserInfo(mobile);
		return userInfo;
	}

	private static UserDto gen(Long userId) {
		GetUserInfoTestClass g = new GetUserInfoTestClass();
		UserDto userInfo = g.getUserInfo(userId);
		return userInfo;
	}

	private static UserDto gen(UserDto userDto) {
		if (null != userDto) {
			return userDto;
		}
		GetUserInfoTestClass g = new GetUserInfoTestClass();
		UserDto userInfo = g.getUserInfo(userDto.getMobile());
		return userInfo;
	}

}

class GetUserInfoTestClass {
	private static UserDto userDto = null;

	public UserDto getUserInfo() {
		if (null == userDto) {
			userDto = gen();
		}
		return userDto;
	}

	public UserDto getUserInfo(String mobile) {
		UserDto userDto = gen(mobile);
		return userDto;
	}

	public UserDto getUserInfo(Long userId) {
		UserDto userDto = null;
		try {
			User userByMobile = UserShopClient.getUser(userId);
			userDto = new UserDto(userByMobile);
			Shop shopByOwnerId = UserShopClient.getShopByOwnerId(userByMobile.getId());
			if (null != shopByOwnerId) {
				userDto.setShopId(shopByOwnerId.getId());
				userDto.setHasShop(true);
				userDto.setShopCode(shopByOwnerId.getInviteCode());
			} else {
				userDto.setHasShop(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDto;
	}

	private UserDto gen(String mobile) {
		return initGen(mobile);
	}

	private UserDto gen() {
		return initGen("13401157249");
	}

	private UserDto initGen(String mobile) {
		UserDto userDto = null;
		try {
			User userByMobile = UserShopClient.getUserByMobile(mobile);
			userDto = new UserDto(userByMobile);
			Shop shopByOwnerId = UserShopClient.getShopByOwnerId(userByMobile.getId());
			if (null != shopByOwnerId) {
				userDto.setShopId(shopByOwnerId.getId());
				userDto.setHasShop(true);
				userDto.setShopCode(shopByOwnerId.getInviteCode());
			} else {
				userDto.setHasShop(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDto;
	}
}