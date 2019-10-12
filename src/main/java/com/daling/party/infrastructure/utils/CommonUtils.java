package com.daling.party.infrastructure.utils;

import com.daling.common.idutils.IDGener;
import com.daling.common.servicelocate.ServerDefIdUtil;
import com.daling.ucclient.tools.Jackson2Helper;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;


public class CommonUtils {
    public static Map<String, String> constructionMap(Map<String, String[]> paramsMap) {
        Map<String, String> params = new HashMap<>();
        for (Iterator iter = paramsMap.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = paramsMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
            params.put(name, valueStr);
        }
        return params;
    }

    public static void writerJson(HttpServletResponse response,XcHeadWrapper jsonHeaderWrapper){
        try {
            response.setContentType("application/json;charset=UTF-8");

            response.setCharacterEncoding("UTF-8");
            PrintWriter pw = response.getWriter();
            pw.write(Jackson2Helper.toJsonString(jsonHeaderWrapper));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去除重复
     * @param list
     */
    public static void removeDuplicateWithOrder(List<Long> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        list.clear();
        list.addAll(newList);
    }

    public static String arrayStrConvert(Array array){
        //todo 暂时这么处理sql集合,还没找到其他方法
        return array.toString().replace("{","").replace("}","");
    }
    
    
    /**
     * 将类似 memberName 转换为 member_name
     * @param name
     * @return
     */
    public static String underscoreName(String name) {
    	if (name.startsWith("_")) {
			name = name.substring(1);
		}
	    StringBuilder result = new StringBuilder();
	    if (name != null && name.length() > 0) {
	        // 将第一个字符处理成大写
	        result.append(name.substring(0, 1).toUpperCase());
	        // 循环处理其余字符
	        for (int i = 1; i < name.length(); i++) {
	            String s = name.substring(i, i + 1);
	            // 在大写字母前添加下划线
	            if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
	                result.append("_");
	            }
	            // 其他字符直接转成大写
	            result.append(s.toUpperCase());
	        }
	    }
	    return result.toString();
	}
    
    /**
     * 将 Date 类转换为 cron 格式
     * @param date
     * @return
     */
    public static String convertDateToCron(Date date){
        String dateFormat="ss mm HH dd MM ? yyyy"; 
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;  
    }

    /**
     * 把原图改成想要的图片(格式如2015/12/03/32FC3F262HI21O21Y_0.7_S.jpg_150x150.jpg)
     * @param original 原图
     * @param thumbSuffix 缩略图
     * @param size 尺寸大小
     * @return
     */
    public static String assembleImage(String original,String thumbSuffix,String size){
    	if (StringUtils.isBlank(original)) return original;
        String[] strArray = StringUtils.split(original, ".");
        String prefix = strArray[0];
        String suffix = strArray[1];
        return prefix + thumbSuffix + "." + suffix + size + "." + suffix;
    }

    /**
     * 把原图改成想要的图片(格式如2015/12/03/32FC3F262HI21O21Y_0.7_S.jpg)
     * @param original 原图
     * @param thumbSuffix 缩略图
     * @return
     */
    public static String assembleImage(String original,String thumbSuffix){
    	if (StringUtils.isBlank(original)) return original;
        String[] strArray = StringUtils.split(original, ".");
        String prefix = strArray[0];
        String suffix = strArray[1];
        return prefix + thumbSuffix + "." + suffix;
    }

    /**
     * 用户头像(格式如2015/12/03/32FC3F262HI21O21Y.jpg_150x150.jpg)
     * @param original 原图
     * @return
     */
    public static String assembleHeanImage(String original,String imgSize){
        if (StringUtils.isBlank(original)) return original;
        String suffix = StringUtils.substring(original, StringUtils.lastIndexOf(original,"."),original.length());
        return original + imgSize + suffix;
    }

    /**
     * 用户头像尺寸替换
     * @param original 原图
     * @param originalSize 原图大小
     * @param replaceSize  替换图
     * @return
     */
    public static String headImageReplace(String original, String originalSize,String replaceSize) {
        if (StringUtils.isBlank(original)) return original;
        return original.replace(originalSize, replaceSize);
    }

    /**
     * objectIsNotEmpty:(判断Object不为空). <br/>
     *
     * @return 为空返回true 不为空返回false
     * @since JDK 1.7
     * @author zhaodeliang
     */
    public static boolean objectIsNotEmpty(Object obj) {
        return !objectIsEmpty(obj);
    }

    /**
     * objectIsEmpty:(判断Object为空). <br/>
     *
     * @param obj
     * @return 为空返回true 不为空返回false
     * @since JDK 1.8
     * @author 赵得良
     */
    public static boolean objectIsEmpty(Object obj) {

        if (obj == null) {
            return true;
        }
        if ("null".equals(obj)) {
            return true;
        }
        if ("".equals(obj)) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).isEmpty();
        }
        if ((obj instanceof Map)) {
            return ((Map) obj).isEmpty();
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        if ((obj instanceof Set)) {
            return ((Set) obj).isEmpty();
        }
        return false;
    }

    /**
     * 获取唯一流水号
     * @return
     */
    public static String getFlowNo() {
        return new StringBuffer("C").append(ServerDefIdUtil.getInstance().getId()).append(IDGener.genOID()).toString();
    }
}
