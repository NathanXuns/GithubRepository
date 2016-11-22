package com.android.main.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  <b>使用实例:</b>
 *  <p>AsyncImageLoader loader=new AsyncImageLoader(getActivity(), 300);
 *  <p>loader.setPercentCallBack(....);
 *  <p>loader.loadImageView("http://hot.dahangzhou.com/image/xihu6/027.jpg", imageView);
 * @author Administrator
 *
 */
public class AsyncImageLoader {
	/** The Constant DEFAULT_THREAD_POOL. */
	private static final int DEFAULT_THREAD_POOL = 3;
	
	private String path=null;
	private loadPercentCallBack mPercentCallBack;
	private ExecutorService _service=Executors.newFixedThreadPool(DEFAULT_THREAD_POOL);
	private Handler handler=new Handler();
	
	private Animation fade_in_anim;
	private boolean isShowAnim;

	private int reqWidth = 480;
	private int reqHeight = 800;
	
	public AsyncImageLoader (Context context){
		path=BaseUtil.getPath();
	}
	
	public AsyncImageLoader (Context context, int duration){
		path=BaseUtil.getPath();
		isShowAnim=true;
		
		fade_in_anim=new AlphaAnimation(0, 1);
		fade_in_anim.setDuration(duration);
		fade_in_anim.setFillAfter(true);
		fade_in_anim.setInterpolator(new AccelerateDecelerateInterpolator());
		
	}
	
	public void setPercentCallBack(loadPercentCallBack mPercentCallBack) {
		this.mPercentCallBack = mPercentCallBack;
	}

	public void loadLocalImage(final String filePath, final ImageView imageView, final int reqWidth, final int reqHeight, final int size){
		if(null==imageView){
			return ;
		}
		_service.submit(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap=BaseUtil.getSmallBitmap(filePath, reqWidth, reqHeight, size);
				if(bitmap != null){
					handler.post(new Runnable() {		
						@Override
						public void run() {
							if (imageView instanceof NetworkImageView) {
								((NetworkImageView) imageView).setLocalImageBitmap(bitmap);
							}else{
								imageView.setImageBitmap(bitmap);
							}
							if(isShowAnim){
								imageView.startAnimation(fade_in_anim);
							}
							if(null != mPercentCallBack){
								mPercentCallBack.setPercent(100);
							}
						}
					});
				}else{
					if(null != mPercentCallBack){
						mPercentCallBack.onFailure();
					}
				}
				
			}
		});
		
	}
	
	private void setBitmapToImageView(final ImageView imageView,String imgUrl){
		if(null==imageView){
			return ;
		}
		final Bitmap bitmap=BaseUtil.getSmallBitmap(path+BaseUtil.md5(imgUrl), reqWidth, reqHeight);
		handler.post(new Runnable() {		
			@Override
			public void run() {
				imageView.setImageBitmap(bitmap);
				if(isShowAnim){
					imageView.startAnimation(fade_in_anim);
				}
			}
		});
	}
	
	public interface loadPercentCallBack{
		void setPercent(int percent);
		void onFailure();
	}

}


