package com.android.main.lib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.android.main.lib.variable.Variable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SystemPropertyUtil {

	/**
	 * 初始化系统的配置文件
	 */
	public static void initSystemProperties(Context c, SPUtils sf, int rawRes, boolean isDebug) {
		if(TextUtils.isEmpty(Variable.AD_PATH)){
			Variable.AD_PATH = c.getDir("ad", 0).getAbsolutePath() + "/";
		}
		InputStream is = c.getResources().openRawResource(rawRes);
		Properties properties = new Properties();
		try {
			properties.load(is);
			Variable.IS_DEBUG = isDebug;
			Variable.CRASH_2_FILE = Boolean.parseBoolean(properties.getProperty("crash2file", "false"));

			Variable.DATABASE_VERSION = Integer.parseInt(properties.getProperty("dbversion", "1"));
			Variable.DATABASE_NAME = properties.getProperty("dbname");

			try {
				Variable.FILE_PATH = Environment.getExternalStorageDirectory() + properties.getProperty("filepath") + "/";
				Variable.LOG_PATH = Environment.getExternalStorageDirectory() + properties.getProperty("logpath") + "/";
			} catch (Exception e) {
				Variable.FILE_PATH = c.getCacheDir() + "/";
				Variable.LOG_PATH = c.getCacheDir() + "/";
				if (e != null) {
					BaseUtil.e("init", "init Variable.FILE_PAH,LOG_PAHT error ： " + e.getMessage());
				}
			}

			Variable.PACKAGE_NAME = c.getPackageName();
			Variable.APP_VERSION_CODE = String.valueOf(BaseUtil.getVersionCode(c));
			Variable.APP_VERSION_NAME = BaseUtil.getVersionName(c);
			
			Variable.APP_KEY = properties.getProperty("appkey");
			Variable.APP_ID = properties.getProperty("appid");

			Variable.Loaded = true;
		} catch (IOException e) {
			BaseUtil.e(e.getMessage());
		} finally {
			BaseUtil.closeStream(is);
		}
	}
}
 