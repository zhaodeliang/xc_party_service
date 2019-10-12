package com.daling.party.domain.constants;

public interface NewShopEnum {

	// 热配key
	String welfareTimeKey = "new_shop_welfare_time";// 福利开始-结束时间
	String welfareWinUrl = "new_shop_welfare_win_url";// 新店主福利 弹窗地址
	String firstOrderTitle = "new_shop_first_order_title";// 开单模块标题
	String firstOrderHotKey = "new_shop_first_order_hot_conf"; //开单热配
	String welfareTitleKey = "new_shop_welfare_title_module"; //标题模块
	String progressRate = "new_shop_welfare_progress_rate";//奖章模块
	String welfarePopupImg = "new_shop_welfare_popup_img";//奖章 弹层图
	String hotTaskList = "new_shop_welfare_task_list";//任务 列表
	String couponCode = "new_shop_welfare_coupon_code"; //券编码
	String couponQuantity = "new_shop_welfare_coupon_quantity"; // 券数量

	// redis
	String cacheBaseKey = "new_shop_welfare_base_"; // 新店主缓存 key
	String firstLogin = "first_login_"; // 首次登陆
	String welfareBase = "welfare_base_"; // 福利基本信息
	String taskList = "task_list_"; // 福利基本信息
	String buttonPrompt = "已完成"; // 任务完成的按键提示

	// 任务编码
	String sxyxskc = "daling_school"; // 商学院新手课程
	String haveVip = "have_vip"; // 拥有vip
	String registered = "registered"; // 注册即送
	String saleAnOrder = "sale_order"; // 售出订单
	String useSjjCoupon = "use_coupon"; // 使用399代金券
	String shareSelected = "share_selected"; // 分享精选
	String successRecruit = "success_recruit"; // 成功招募
}
