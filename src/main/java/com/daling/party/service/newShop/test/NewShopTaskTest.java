package com.daling.party.service.newShop.test;

import com.alibaba.fastjson.JSONObject;
import com.daling.party.domain.constants.NewShopEnum;
import com.daling.party.domain.vo.newShop.childVo.ProgressRateVo;
import com.daling.party.domain.vo.newShop.childVo.TaskVo;
import com.daling.party.utils.SmartDateUtil;

import java.util.*;

public class NewShopTaskTest {


	public static String getStr(String trackId, String key) {
		String value = "";
		if(!"asdf".equals(trackId)){
			return "";
		}
		if (NewShopEnum.welfareTimeKey.equals(key)) {
			value="2017-07-30 00:00:00~2019-08-30 00:00:00";
		}
		if (NewShopEnum.welfareWinUrl.equals(key)) {
			value="";
		}
		if (NewShopEnum.welfareTitleKey.equals(key)) {
			value="{\"backColor\":\"#2D8BEF\",\"backImg\":\"http://daling.com/abc.jpg\",\"fontColor\":\"#2D8BEF\",\"titleName\":\"欢迎回家\"}";
		}
		if (NewShopEnum.firstOrderTitle.equals(key)) {
			value="欢迎福利.开单送代金券";
		}
		if (NewShopEnum.firstOrderHotKey.equals(key)) {
			value="[{\"backColor\":\"#2D8BEF\",\"desc\":\"奖励以5元无门槛代金券发放\",\"fillColor\":\"#2D8BEF\",\"fontColor\":\"#2D8BEF\",\"imgUrl\":\"http://daling.com/abc.jpg\",\"priceColor\":\"#2D8BEF\",\"title\":\"自购或者销售任何普通商品\"},{\"backColor\":\"#2D8BEF\",\"desc\":\"奖励以10元无门槛代金券发放\",\"fillColor\":\"#2D8BEF\",\"fontColor\":\"#2D8BEF\",\"imgUrl\":\"http://daling.com/abc.jpg\",\"priceColor\":\"#2D8BEF\",\"title\":\"自购或者销售任何普通商品\"}]";
		}
		if (NewShopEnum.progressRate.equals(key)) {
			value = "{\"backColor\":\"#2D8BEF\",\"backImg\":\"http://daling.com/abc.jpg\",\"desc\":\"继续加油哦，奖品马上就到手了\",\"descFontColor\":\"#2D8BEF\",\"medalName\":\"奖章\",\"medalUnit\":\"枚\",\"prizeImg\":\"http://daling.com/abc.jpg\",\"title\":\"暖心福利.集齐奖章送奖品\",\"titleFontColor\":\"#2D8BEF\"}";
		}
		if (NewShopEnum.welfarePopupImg.equals(key)) {
			value = "http://daling.com/abc.jpg";
		}
		if (NewShopEnum.hotTaskList.equals(key)) {
			value = getHotStrTaskList();
		}
		if (NewShopEnum.couponCode.equals(key)) {
			value = "123456";
		}
		if (NewShopEnum.couponQuantity.equals(key)) {
			value = "2";
		}


		return value;
	}

	public static void main(String[] args) {
		getHotStrProgressRate();
		getHotStrTaskList();

	}


	public static void getHotStrProgressRate() {
		ProgressRateVo vo = new ProgressRateVo();
		vo.setTitle("暖心福利.集齐奖章送奖品");
		vo.setDesc("继续加油哦，奖品马上就到手了");
		vo.setMedalName("奖章");
		vo.setMedalUnit("枚");
		// vo.setBackImg("http://daling.com/abc.jpg");
		vo.setPrizeImg("http://daling.com/abc.jpg");
		vo.setBackColor("#2D8BEF");
		vo.setTitleFontColor("#2D8BEF");
		vo.setDescFontColor("#2D8BEF");
		String s = JSONObject.toJSONString(vo);
		System.out.println(s);
	}

	public static String getHotStrTaskList() {
		Map<String, List<TaskVo>> map = new HashMap<>();
		List<TaskVo> list7 = new ArrayList<>();
		TaskVo vo7 = new TaskVo();
		vo7.setTaskCode(NewShopEnum.successRecruit); // 成功招募
		vo7.setOrderNum(1);
		vo7.setTaskDescUp("成功邀请获百元奖励");
		vo7.setTaskDescDown("确认收货后可领取五枚奖章");
		vo7.setMedalNum(5);
		vo7.setMedalUnit("枚");
		vo7.setMedalName("奖章");
		vo7.setMedalDesc("5枚奖章");
		vo7.setButtonPrompt("立即前往");
		vo7.setJumpUrl("");
		setDate(vo7);
		setColor(vo7);
		list7.add(vo7);
		map.put(NewShopEnum.successRecruit, list7);
		List<TaskVo> list6 = new ArrayList<>();
		TaskVo vo6 = new TaskVo();
		vo6.setTaskCode(NewShopEnum.shareSelected);
		vo6.setOrderNum(2);
		vo6.setTaskDescUp("邀请亲朋一同回家");
		vo6.setTaskDescDown("分享精选商品");
		vo6.setMedalNum(1);
		vo6.setMedalUnit("枚");
		vo6.setMedalName("奖章");
		vo6.setMedalDesc("1枚奖章");
		vo6.setButtonPrompt("前往分享");
		vo6.setJumpUrl("");
		setDate(vo6);
		setColor(vo6);
		list6.add(vo6);
		map.put(NewShopEnum.shareSelected, list6);
		List<TaskVo> list5 = new ArrayList<>();
		TaskVo vo5 = new TaskVo();
		vo5.setTaskCode(NewShopEnum.saleAnOrder);
		vo5.setOrderNum(4);
		vo5.setTaskDescUp("店铺开张了");
		vo5.setTaskDescDown("成功售出一笔订单");
		vo5.setMedalNum(3);
		vo5.setMedalUnit("枚");
		vo5.setMedalName("奖章");
		vo5.setMedalDesc("3枚奖章");
		vo5.setButtonPrompt("前往专题");
		vo5.setJumpUrl("https://djia.daling.com/subject/e8b134f");
		setDate(vo5);
		setColor(vo5);
		list5.add(vo5);
		map.put(NewShopEnum.saleAnOrder, list5);
		List<TaskVo> list4 = new ArrayList<>();
		TaskVo vo4 = new TaskVo();
		vo4.setTaskCode(NewShopEnum.haveVip);
		vo4.setOrderNum(5);
		vo4.setTaskDescUp("顾客上门了");
		vo4.setTaskDescDown("拥有一位店铺VIP");
		vo4.setMedalNum(1);
		vo4.setMedalUnit("枚");
		vo4.setMedalName("奖章");
		vo4.setMedalDesc("1枚奖章");
		vo4.setButtonPrompt("前往邀请");
		vo4.setJumpUrl("");
		setDate(vo4);
		setColor(vo4);
		list4.add(vo4);
		map.put(NewShopEnum.haveVip, list4);
		List<TaskVo> list3 = new ArrayList<>();
		TaskVo vo3 = new TaskVo();
		vo3.setTaskCode(NewShopEnum.sxyxskc);
		vo3.setOrderNum(6);
		vo3.setTaskDescUp("学习使我成长");
		vo3.setTaskDescDown("商学院完成新手课程");
		vo3.setMedalNum(1);
		vo3.setMedalUnit("枚");
		vo3.setMedalName("奖章");
		vo3.setMedalDesc("1枚奖章");
		vo3.setButtonPrompt("立即前往");
		vo3.setJumpUrl("");
		setDate(vo3);
		setColor(vo3);
		list3.add(vo3);
		map.put(NewShopEnum.sxyxskc, list3);
		List<TaskVo> list1 = new ArrayList<>();
		TaskVo vo = new TaskVo();
		vo.setTaskCode(NewShopEnum.registered);
		vo.setOrderNum(3);
		vo.setTaskDescUp("欢迎加入达令家");
		vo.setTaskDescDown("注册即送");
		vo.setMedalNum(1);
		vo.setMedalUnit("枚");
		vo.setMedalName("奖章");
		vo.setMedalDesc("1枚奖章");
		vo.setButtonPrompt("前往领取");
		vo.setJumpUrl("");
		setDate(vo);
		setColor(vo);
		list1.add(vo);
		map.put(NewShopEnum.registered, list1);
		List<TaskVo> list2 = new ArrayList<>();
		TaskVo vo1 = new TaskVo();
		vo1.setTaskCode(NewShopEnum.useSjjCoupon);
		vo1.setOrderNum(6);
		vo1.setTaskDescUp("顾家省钱小能手");
		vo1.setTaskDescDown("使用399代金券购买一件商品");
		vo1.setMedalNum(3);
		vo1.setMedalUnit("枚");
		vo1.setMedalName("奖章");
		vo1.setMedalDesc("3枚奖章");
		vo1.setButtonPrompt("立即前往");
		vo1.setJumpUrl("https://djia.daling.com/subject/e8b134f");
		setDate(vo1);
		setColor(vo1);
		list2.add(vo1);
		map.put(NewShopEnum.useSjjCoupon, list2);
		String s = JSONObject.toJSONString(map);
		return s;
	}

	private static void setDate(TaskVo vo){
		vo.setBegTime(SmartDateUtil.getAddDayBeginTime(-1000,new Date()));
		vo.setEndTime(SmartDateUtil.getAddDayBeginTime(2,new Date()));
	}

	private static void setColor(TaskVo vo){
		vo.setBackColor("#DC143C");
		vo.setButtonFillColor("#708090");
		vo.setButtonFontColor("#000000");
		vo.setDescUpFontColor("#000000");
		vo.setDescDownFontColor("#000000");
	}
}
