package com.android.main.lib.util;


import java.text.SimpleDateFormat;

public class DateFormatUtil {
	private static SimpleDateFormat mFormatter = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String getRefreshTime(long time) {
		if (time <= 0) {
			// time <= 0 说明数据异常，暂时显示成“很久很久前”
			// 否则会显示成1970-1-1 00:00
			return "很久很久前";
		}
		long cur_time = System.currentTimeMillis();
		String curT = mFormatter.format(cur_time).toString();
		int curY = Integer.valueOf(curT.substring(0, 4));
		int curM = Integer.valueOf(curT.substring(4, 6));
		int curD = Integer.valueOf(curT.substring(6, 8));
		String tempT = mFormatter.format(time).toString();
		int tempY = Integer.valueOf(tempT.substring(0, 4));
		int tempM = Integer.valueOf(tempT.substring(4, 6));
		int tempD = Integer.valueOf(tempT.substring(6, 8));
		int tempH = Integer.valueOf(tempT.substring(tempT.length() - 6,
				tempT.length() - 4));
		int tempm = Integer.valueOf(tempT.substring(tempT.length() - 4,
				tempT.length() - 2));
		if (curY > tempY) {
			// 当前年份大于发生时间年份 去年
			return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
					+ "日 " + formatInt(tempH) + ":" + formatInt(tempm);
		} else if (curY == tempY) {
			// 同年发生
			if (curM > tempM) {
				// 同年过去月份发生
				return formatInt(tempM) + "月" + formatInt(tempD) + "日 "
						+ formatInt(tempH) + ":" + formatInt(tempm);
			} else if (curM == tempM) {
				// 同年同月发生
				if (curD > tempD) {
					if ((curD - tempD) == 1) {
						// 昨天发生
						return "昨天 " + formatInt(tempH) + ":"
								+ formatInt(tempm);
					} else {
						// 本月过去天数发生
						return formatInt(tempM) + "月" + formatInt(tempD) + "日 "
								+ formatInt(tempH) + ":" + formatInt(tempm);
					}
				} else if (curD == tempD) {
					// 今天发生
					long del_time = cur_time - time;
					long sec = del_time / 1000;
					long min = sec / 60;
					if (min < 60) {
						if (min < 1) {
							return "刚刚";
						}
						return min + " 分钟前";
					} else {
						return formatInt(tempH) + ":" + formatInt(tempm);
					}
				} else {
					// 本月未来某天发生
					return tempY + "年" + formatInt(tempM) + "月"
							+ formatInt(tempD) + "日 " + formatInt(tempH) + ":"
							+ formatInt(tempm);
				}
			} else {
				// 同年未来月份
				return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
						+ "日 " + formatInt(tempH) + ":" + formatInt(tempm);
			}
		} else {
			// 未来发生 不存在
			return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
					+ "日 " + formatInt(tempH) + ":" + formatInt(tempm);
		}
	}

	public static String getHomeTaskRefreshTime(long time) {
		if (time <= 0) {
			// time <= 0 说明数据异常，暂时显示成“很久很久前”
			// 否则会显示成1970-1-1 00:00
			return "很久很久前";
		}
		long cur_time = System.currentTimeMillis();
		String curT = mFormatter.format(cur_time).toString();
		int curY = Integer.valueOf(curT.substring(0, 4));
		int curM = Integer.valueOf(curT.substring(4, 6));
		int curD = Integer.valueOf(curT.substring(6, 8));
		String tempT = mFormatter.format(time).toString();
		int tempY = Integer.valueOf(tempT.substring(0, 4));
		int tempM = Integer.valueOf(tempT.substring(4, 6));
		int tempD = Integer.valueOf(tempT.substring(6, 8));
		int tempH = Integer.valueOf(tempT.substring(tempT.length() - 6,
				tempT.length() - 4));
		int tempm = Integer.valueOf(tempT.substring(tempT.length() - 4,
				tempT.length() - 2));
		if (curY > tempY) {
			// 当前年份大于发生时间年份 去年
			return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
					+ "日 ";
		} else if (curY == tempY) {
			// 同年发生
			if (curM > tempM) {
				// 同年过去月份发生
				return formatInt(tempM) + "月" + formatInt(tempD) + "日 "
						+ formatInt(tempH) + ":" + formatInt(tempm);
			} else if (curM == tempM) {
				// 同年同月发生
				if (curD > tempD) {
					if ((curD - tempD) == 1) {
						// 昨天发生
						return "昨天 " + formatInt(tempH) + ":"
								+ formatInt(tempm);
					} else {
						// 本月过去天数发生
						return formatInt(tempM) + "月" + formatInt(tempD) + "日 "
								+ formatInt(tempH) + ":" + formatInt(tempm);
					}
				} else if (curD == tempD) {
					// 今天发生
					long del_time = cur_time - time;
					long sec = del_time / 1000;
					long min = sec / 60;
					if (min < 60) {
						if (min < 1) {
							return "刚刚";
						}
						return min + " 分钟前";
					} else {
						return formatInt(tempH) + ":" + formatInt(tempm);
					}
				} else {
					// 本月未来某天发生
					return tempY + "年" + formatInt(tempM) + "月"
							+ formatInt(tempD) + "日 " + formatInt(tempH) + ":"
							+ formatInt(tempm);
				}
			} else {
				// 同年未来月份
				return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
						+ "日 " + formatInt(tempH) + ":" + formatInt(tempm);
			}
		} else {
			// 未来发生 不存在
			return tempY + "年" + formatInt(tempM) + "月" + formatInt(tempD)
					+ "日 " + formatInt(tempH) + ":" + formatInt(tempm);
		}
	}

	public static String formatInt(int digital){
		if(String.valueOf(digital).length() < 2){
			return "0"+ String.valueOf(digital);
		}
		return String.valueOf(digital);
	}
	
	public static String getLastTime(long time) {
		if (time <= 0) {
			// time <= 0 说明数据异常，暂时显示成“很久很久前”
			// 否则会显示成1970-1-1 00:00
			return "很久很久后";
		}
		long cur_time = System.currentTimeMillis();
		long del_time = time - cur_time;
		long sec = del_time / 1000;
		long min = sec / 60;
		if (min < 60) {
			if (min < 1) {
				return (int) del_time / 1000 + "秒";
			}
			return min + " 分钟";
		} else {
			long h = min / 60;
			if (h < 24) {
				return h + " 小时";
			}
			long day = h / 24;
			if (day >= 1) {
				return day + " 天";
			} else {
				return h + " 小时";
			}
		}
	}
}
