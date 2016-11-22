package com.android.main.lib.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.main.lib.R;
import com.android.main.lib.util.BaseUtil;
import com.android.main.lib.util.SPUtils;
import com.android.main.lib.util.ScreenUtil;
import com.android.main.lib.util.SystemPropertyUtil;
import com.android.main.lib.util.VolleyRequestUtil;
import com.android.main.lib.util.permission.EasyPermissions;
import com.android.main.lib.util.permission.StatusBarCompat;
import com.android.main.lib.variable.Variable;
import com.android.main.lib.views.CustomToast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public class BaseLibActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
	protected String TAG = this.getClass().getSimpleName();

	protected SPUtils mSPUtils = null;
	protected RequestQueue queue;
	protected com.android.volley.toolbox.ImageLoader mImageLoader;
	protected Context mContext;
	protected Activity mActivity;
	protected Intent intent;
	protected InputMethodManager imm;

	/**
	 * 权限回调接口
	 */
	private CheckPermListener mListener;

	protected static final int RC_PERM = 123;

	protected static int reSting = R.string.ask_again;//默认提示语句


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mActivity = this;

		if (Variable.WIDTH == 0) {
			ScreenUtil.initScreenProperties(mContext);
		}

		if (!Variable.Loaded) {
			SystemPropertyUtil.initSystemProperties(this, null, R.raw.system, BuildConfig.DEBUG);
		}

		queue = getRequestQueue(mContext);
		mImageLoader = getImageLoader(mContext);

		mSPUtils = SPUtils.getInstance(mContext);

		imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (queue == null) {
			queue = getRequestQueue(mContext);
		}
		if (mImageLoader == null) {
			mImageLoader = getImageLoader(mContext);
		}
		if (mSPUtils == null) {
			mSPUtils = SPUtils.getInstance(mContext);
		}
	}

	@Override
	protected void onDestroy() {
		if (queue != null) {
			queue.cancelAll(TAG);
		}
		if (mImageLoader != null) {
			mImageLoader = null;
		}
		if (mSPUtils != null) {
			mSPUtils = null;
		}
		super.onDestroy();
	}

	public void right2Left() {
		BaseUtil.i("Base", "right2Left");
	}

	public void left2Right() {
		BaseUtil.i("Base", "left2Right");
		goBack();
	}

	public boolean myDispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("NewApi")
	public void goBack() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@SuppressLint("NewApi") public void goBackAnimBall() {
		finish();
		overridePendingTransition(R.anim.modal_in, R.anim.modal_out);
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	@SuppressLint("NewApi")
	@Override
	public void startActivity(Intent it) {
		super.startActivity(it);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@SuppressLint("NewApi")
	public void startActivityNoAnim(Intent it) {
		super.startActivity(it);
		overridePendingTransition(0, 0);
	}

	@SuppressLint("NewApi")
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * 弹出动画
	 * 
	 * @param intent
	 * @param requestCode
	 */
	@SuppressLint("NewApi") public void startActivityForResultAnimBall(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.modal_in, R.anim.modal_out);
	}

	public void startActivityNoAnimForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@SuppressLint("NewApi")
	public void startActivityB2T(Intent intent) {
		super.startActivity(intent);
		this.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_null);
	}

	@SuppressLint("NewApi") public void goBackT2B() {
		finish();
		overridePendingTransition(R.anim.slide_null, R.anim.anim_bottom_out);
	}

	public void showToast(final String msg) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomToast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void showToast(final String msg, final int time) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomToast.makeText(mContext, msg, time).show();
			}
		});
	}

	public void showToast(final int resId) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomToast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void showToast(final int resId, final int time) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				CustomToast.makeText(mContext, resId, time).show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("NewApi") @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 点击activity没有焦点的地方隐藏键盘
			if (mActivity.getCurrentFocus() != null) {
				if (mActivity.getCurrentFocus().getWindowToken() != null) {
					// 调用系统自带的隐藏软键盘
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public void initStatusBar(String strColor) {
		StatusBarCompat.setStatusBarColor(this, Color.parseColor(strColor));
		StatusBarCompat.translucentStatusBar(this);
	}

	public interface CheckPermListener {
		//权限通过后的回调方法
		void superPermission();
	}

	protected void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
		mListener = listener;
		if (EasyPermissions.hasPermissions(this, mPerms)) {
			if (mListener != null)
				mListener.superPermission();
		} else {
			EasyPermissions.requestPermissions(this, getString(resString), RC_PERM, mPerms);
		}
	}

	/**
	 * 用户权限处理,
	 * 如果全部获取, 则直接过.
	 * 如果权限缺失, 则提示Dialog.
	 *
	 * @param requestCode  请求码
	 * @param permissions  权限
	 * @param grantResults 结果
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onPermissionsGranted(int requestCode, List<String> perms) {
		//同意了某些权限可能不是全部
	}

	@Override
	public void onPermissionsAllGranted() {
		if (mListener != null)
			mListener.superPermission();//同意了全部权限的回调
	}

	@Override
	public void onPermissionsDenied(int requestCode, List<String> perms) {
		EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.perm_tip), R.string.setting, R.string.cancel, null, perms);
	}

	public String getTAG() {
		return TAG == null || TextUtils.isEmpty(TAG) ? "" :this.getClass().getSimpleName();
	}

	protected ImageLoader getImageLoader(Context mContext){
		return VolleyRequestUtil.getInstance(mContext).getImageLoader();
	}

	protected RequestQueue getRequestQueue(Context mContext){
		return VolleyRequestUtil.getInstance(mContext).getRequestQueue();
	}

}
