package org.net5ijy.util;

import java.io.UnsupportedEncodingException;

/**
 * 字符串工具类
 * 
 * @author 创建人：xuguofeng
 * @version 创建于：2018年7月3日 上午11:48:36
 */
public class StringUtil {

	/**
	 * 首字母转大写
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年7月3日 上午11:49:02
	 * @param str
	 * @return
	 */
	public static String captureName(String str) {
		if (isNullOrEmpty(str)) {
			return str;
		}
		char[] cs = str.toCharArray();
		if (cs[0] > 'z' || cs[0] < 'a') {
			return str;
		}
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	/**
	 * 验证字符串是否为null或长度为0，此方法不会对字符串进行trim操作
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年5月19日 下午1:03:03
	 * @param str
	 * @return true - 为空<br />
	 *         false - 不为空
	 */
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 获取UTF-8编码方式的字符串
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年6月28日 下午12:56:45
	 * @param text
	 * @return
	 */
	public static String getUtf8Text(String text) {
		try {
			return new String(text.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return text;
	}

	/**
	 * 把字符串转为Integer
	 * 
	 * @author 创建人：xuguofeng
	 * @version 创建于：2018年7月4日 上午10:44:06
	 * @param str
	 *            - 字符串
	 * @param defaultVal
	 *            - 默认值，在数值转换失败时使用
	 * @return
	 */
	public static Integer getInteger(String str, Integer defaultVal) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return defaultVal;
	}
}
