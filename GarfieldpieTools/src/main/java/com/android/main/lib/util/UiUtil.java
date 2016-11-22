package com.android.main.lib.util;

import com.android.main.lib.variable.Variable;

/**
 * Ui设置类
 * 
 * @author 徐宁生
 * 
 */
public class UiUtil {
	/**
	 * 记录上次点击时间
	 */
	private static long lastClickTime;

	/**
	 * 判断是不是短时间内连续点击两次
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (timeD >= 0 && timeD <= Variable.DOUBLE_CLICK_INTERVAL) {
			return true;
		} else {
			lastClickTime = time;
			return false;
		}
	}
}
