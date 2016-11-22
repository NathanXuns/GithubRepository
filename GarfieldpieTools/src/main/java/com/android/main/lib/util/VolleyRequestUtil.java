package com.android.main.lib.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BitmapCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 此类通过单例模式创建了两个对象，一个是ImageLoader，还有一个是RequestQueue
 * 
 * @author Administrator
 * 
 */
public class VolleyRequestUtil {
	private String TAG = "VolleyRequestUtil";
	private static VolleyRequestUtil mInstance;
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Context mCtx;

	private VolleyRequestUtil(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();

		try {
			RequestQueue requestQueue = Volley.newRequestQueueInDisk(context, BaseUtil.getPath(), null);
			mImageLoader = new ImageLoader(requestQueue, BitmapCache.getInstance(((FragmentActivity) context).getSupportFragmentManager()));
		} catch (Exception e) {
			e.printStackTrace();
			BaseUtil.d(TAG, "传进来的context不是一个FragmentActivity实例");
		}
	}

	/**
	 * 请注意：此处的context必须是一个FragmentActivity实例
	 * 
	 */
	public static synchronized VolleyRequestUtil getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new VolleyRequestUtil(context);
		}
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() 是很关键的一步, 它将保证我们在整个应用中的Context是一致的
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
