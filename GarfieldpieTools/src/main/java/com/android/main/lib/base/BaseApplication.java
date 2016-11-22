package com.android.main.lib.base;

import android.app.Application;
import com.android.main.lib.util.SPUtils;

/**
 * 此类为基类，不足部分，需自行添加，在项目的Application中继承
 * @author Administrator
 *
 */
public class BaseApplication extends Application {
	protected static BaseApplication mInstance = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
	    mInstance=this;
	}

	public static BaseApplication getInstance(){
		return mInstance;
	}
	
	public SPUtils getSPUtils(){
		return SPUtils.getInstance(mInstance);
	}


}
