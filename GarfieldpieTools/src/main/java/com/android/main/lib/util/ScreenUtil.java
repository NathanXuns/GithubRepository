package com.android.main.lib.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.android.main.lib.variable.Variable;

public class ScreenUtil {

	/**
	 * 初始化屏幕的大小密度等参数
	 */
	public static void initScreenProperties(Context mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		Variable.DENSITY = dm.density;
		Variable.WIDTH = dm.widthPixels;
		Variable.HEIGHT = dm.heightPixels;

		Configuration mConfiguration = mContext.getResources().getConfiguration();
		Variable.ScreenOrientation = mConfiguration.orientation;
	}

	/**
	 * 获取状态栏的高度
	 */
	public static int getStatusBarHeight(Context mContext){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		Rect frame = new Rect();
		((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	/**
	 * 是否显示状态栏
     */
	public static void setStatusBarShowOrHide(Activity mActivity,boolean isShow) {
		if (isShow) {
			WindowManager.LayoutParams attr = mActivity.getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			mActivity.getWindow().setAttributes(attr);
			mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			mActivity.getWindow().setAttributes(lp);
			mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 判断是不是竖屏显示
     */
	public static boolean isVerticalScreen(Context mContext){
		if(getScreenOrientation(mContext) == Configuration.ORIENTATION_PORTRAIT){
			return true;
		}
			return false;
	}

	/**
	 * 判断是不是横屏显示
     */
	public static boolean isHorizontalScreen(Context mContext){
		if(getScreenOrientation(mContext) == Configuration.ORIENTATION_LANDSCAPE){
			return true;
		}
			return false;
	}

	/**
	 * 获取当前屏幕方向（横竖屏）
     */
	public static int getScreenOrientation(Context mContext){
		return mContext.getResources().getConfiguration().orientation;
	}

	/**
	 * 设置屏幕竖屏显示
     */
	public static void setVerticalScreen(Activity mActivity){
		mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
	}

	/**
	 * 设置屏幕横屏显示
     */
	public static void setHorizontalScreen(Activity mActivity){
		mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
	}
}
 