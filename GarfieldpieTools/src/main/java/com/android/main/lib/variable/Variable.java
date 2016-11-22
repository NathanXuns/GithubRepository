package com.android.main.lib.variable;

import android.support.v7.appcompat.BuildConfig;

public class Variable {

	/**
	 * 按钮两次点击间隔
	 */
	public static final long DOUBLE_CLICK_INTERVAL = 1000;

	/**
	 * 是否已加载过配置
	 */
	public static boolean Loaded = false;

	public static String APP_KEY = "";
	public static String APP_ID = "";

	/**
	 * 数据库名称 （默认）
	 */
	public static String DATABASE_NAME = "";

	/**
	 * 数据库版本号 （默认）
	 */
	public static int DATABASE_VERSION = 1;

	/**
	 * SDK版本 确定设备是2.x;3.x;4.x
	 */
	public static int SDK_VERSION = 0;

	/**
	 * 是否开启日志 false : 不打印日志信息，true : 打印日志信息
	 */
	public static boolean IS_DEBUG = BuildConfig.DEBUG;

	/**
	 * 是否保存crash日志到文件中
	 */
	public static boolean CRASH_2_FILE = true;

	/**
	 * 日志的默认TAG
	 */
	public static String DEFAULT_LOG_TAG = "ActionBarLib";

	/**
	 * 该应用程序的版本号
	 */
	public static String APP_VERSION_NAME = "0.0.0";

	/**
	 * 该应用程序的version_code
	 */
	public static String APP_VERSION_CODE = "0";

	/**
	 * 文件缓存到sdk中路径
	 */
	public static String FILE_PATH = "";

	/**
	 * log缓存到sdk中路径
	 */
	public static String LOG_PATH = "";

	/**
	 * 应用程序包名
	 */
	public static String PACKAGE_NAME = "";

	/**
	 * 设备屏幕参数
	 */
	public static float DENSITY = 1f;
	public static int WIDTH = 0;
	public static int HEIGHT = 0;
	public static int ScreenOrientation = 1; // 屏幕方向：默认竖屏（1）


	/**
	 * 默认城市
	 */
	public static String CITY_NAME = "南京";

	/**
	 * 经纬度 以及 定位城市
	 */
	public static String LAT = "";
	public static String LNG = "";
	public static String LOC_CITY = "";

	/**
	 * 是否是首次启动
	 */
	public static boolean IS_FIRST_OPEN = true;

	/**
	 * launch图片（广告图）的保持路径
	 */
	public static String AD_PATH = null;

	/**
	 * 是否接受服务器通知；默认是接受通知
	 */
	public static boolean IS_RECEIVE_NOTIFY = true;
	public static String RECEIVE_NOTIFY_TAG = "is_receive_notify";

	/**
	 * 设置一个常量表示滑动屏幕多少距离即退出此界面
	 */
	public static int LEFT_RIGHT_DISTANCE = 200;

	/**
	 * 用于标记应用是否在运行状态
	 */
	public static boolean APP_RUNNING = false;
}
