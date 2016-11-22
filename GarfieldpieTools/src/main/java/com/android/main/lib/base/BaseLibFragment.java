package com.android.main.lib.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.BuildConfig;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.main.lib.R;
import com.android.main.lib.util.BaseUtil;
import com.android.main.lib.util.SPUtils;
import com.android.main.lib.util.ScreenUtil;
import com.android.main.lib.util.SystemPropertyUtil;
import com.android.main.lib.util.VolleyRequestUtil;
import com.android.main.lib.util.permission.EasyPermissions;
import com.android.main.lib.variable.Variable;
import com.android.main.lib.views.CustomToast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

public abstract class BaseLibFragment extends Fragment implements OnClickListener, EasyPermissions.PermissionCallbacks {
	protected String TAG = this.getClass().getSimpleName();
	protected Context mContext;
	protected Activity mActivity;
	protected SPUtils mSPUtils = null;
	protected RequestQueue queue;
	protected com.android.volley.toolbox.ImageLoader mImageLoader;
	protected LayoutInflater mInflater;
	protected View mContentView;

	protected Intent intent;

	public BaseLibFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
		mContext = getActivity();

		if (Variable.WIDTH == 0) {
			ScreenUtil.initScreenProperties(mContext);
		}

		if (!Variable.Loaded) {
			SystemPropertyUtil.initSystemProperties(mContext, null, R.raw.system, BuildConfig.DEBUG);
		}

		queue = getRequestQueue(mContext);
		mImageLoader = getImageLoader(mContext);
		mSPUtils = SPUtils.getInstance(mContext);
	}

	@Override
	public void onResume() {
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
	public void onDestroy() {
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

	@SuppressLint("NewApi")
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void startActivityB2TForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_null);
	}

	public void startActivityNoAnimForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void startActivityB2T(Intent intent) {
		super.startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_null);
	}

	public void startActivityNoAnim(Intent it) {
		super.startActivity(it);
		getActivity().overridePendingTransition(0, 0);
	}

	public void showToast(String msg) {
		CustomToast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	public void showToast(String msg, int time) {
		CustomToast.makeText(mContext, msg, time).show();
	}

	public void showToast(int resId) {
		CustomToast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int resId, int time) {
		CustomToast.makeText(mContext, resId, time).show();
	}

	/**
	 * 随手势而产生的事件，向左的事件()
	 * 
	 * 根据不同的页面，在各自模块中重写
	 */
	public void right2Left() {
		BaseUtil.i("Base", "right2Left");
	}

	/**
	 * 随手势而产生的事件，向右的事件
	 * 
	 * 默认是goback事件
	 */
	public void left2Right() {
		((BaseLibActivity) mContext).goBack();
	}

	@Override
	public void onClick(View view) {

	}

	protected ImageLoader getImageLoader(Context mContext){
		return VolleyRequestUtil.getInstance(mContext).getImageLoader();
	}

	protected RequestQueue getRequestQueue(Context mContext){
		return VolleyRequestUtil.getInstance(mContext).getRequestQueue();
	}
// ----------------------------------------- 权限先关 Begin---------------------------------------------- //
	/**
	 * 权限回调接口
	 */
	private CheckPermListener mListener;
	protected static final int RC_PERM = 123;

	public interface CheckPermListener {
		//权限通过后的回调方法
		void superPermission();
	}

	protected void checkPermission(CheckPermListener listener, int resString, String... mPerms) {
		mListener = listener;
		if (EasyPermissions.hasPermissions(this.getContext(), mPerms)) {
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
// ----------------------------------------- 权限先关 End---------------------------------------------- //
}
