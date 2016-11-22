package com.android.main.lib.util;

import android.content.Context;

import com.android.main.lib.variable.Variable;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 此类在项目的欢迎界面进行初始化，直接调用InitUtil.init()即可完成初始化
 * <p>欢迎界面最好根据项目不同重新编写
 * <p><b>PS:当然更牛的方法还是直接用叮当，虽然用法复杂，本人写此Lib的作用在于针对定制化特别强的用户</b>
 * @author Administrator
 *
 */
public class InitUtil {

	public static void init(Context c, int statusBarDrawableRes, int umeng_channel, int rawRes, boolean isDebug) {
		Variable.AD_PATH = c.getDir("ad", 0).getAbsolutePath() + "/";
		
		SPUtils sf = SPUtils.getInstance(c);
		// 初始化系统配置参数
		SystemPropertyUtil.initSystemProperties(c, sf, rawRes, isDebug);
		// 初始化屏幕参数
		ScreenUtil.initScreenProperties(c);
		// 初始化模块参数
		//FileHelper.initModule(c);
		// 初始化app自动更新
		initAppUpdate(c,sf);
		// 初始化用户中心
		//initUser(c);
		// 初始化位置信息
		// 初始化手机相关信息
		initMobile_client(c, sf, umeng_channel);
	}
	
	public static void initMobile_client(Context c, SPUtils sf , int umeng_channel) {
		Map<String, String> map = new HashMap<>();
		try {
			map.put("system", BaseUtil.enCodeUtf8(BaseUtil.getSystem()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("program_name", BaseUtil.enCodeUtf8(BaseUtil.getProgramName(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("types", BaseUtil.enCodeUtf8(BaseUtil.getTypes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("debug", BaseUtil.enCodeUtf8(BaseUtil.getDebug()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("program_version", BaseUtil.enCodeUtf8(BaseUtil.getVersionName(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("device_token", BaseUtil.enCodeUtf8(BaseUtil.getDeviceToken(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("long", BaseUtil.enCodeUtf8(Variable.LNG));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			map.put("lat", BaseUtil.enCodeUtf8(Variable.LAT));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("imei", BaseUtil.enCodeUtf8(BaseUtil.getIMEI(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("imsi", BaseUtil.enCodeUtf8(BaseUtil.getIMSI(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("iccid", BaseUtil.enCodeUtf8(BaseUtil.getICCID(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(Variable.IS_FIRST_OPEN){
			try {
				map.put("insta", BaseUtil.enCodeUtf8("1"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		try {
			map.put("phone_num", BaseUtil.enCodeUtf8(BaseUtil.getPhoneNum(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("umeng_channel",BaseUtil.enCodeUtf8(c.getResources().getString(umeng_channel)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	

	// 校验Tag Alias 只能是数字和英文字母
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}


	private static void initAppUpdate(Context c,SPUtils sf) {
		String versionName = sf.get("APP_VERSION_NAME", "0.0.0");
		if(!versionName.equals(Variable.APP_VERSION_NAME)){
			Variable.IS_FIRST_OPEN = true;
			sf.put("APP_VERSION_NAME", Variable.APP_VERSION_NAME);
		}else {
			Variable.IS_FIRST_OPEN = false;
		}
	}

}
