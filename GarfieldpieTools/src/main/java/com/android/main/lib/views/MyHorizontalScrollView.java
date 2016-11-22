package com.android.main.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.android.main.lib.variable.Variable;

/**
 * 如果想让HorizontalScrollView获得弹性，建议横向ScrollView使用该类
 * @author Administrator
 *
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
	private RelativeLayout mParentView;
	private float mStartX;
	private float mLastX;

	//最大滚动偏移量
	private int MAX_SCROLL_OFFSET = (int)(50* Variable.DENSITY);

	public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public MyHorizontalScrollView(Context context) {
		super(context);

	}

	public RelativeLayout getmParentView() {
		return mParentView;
	}

	public void setmParentView(RelativeLayout mParentView) {
		this.mParentView = mParentView;
	}
	
	public void setMaxScrollOffset(int maxScrollOffset){
		this.MAX_SCROLL_OFFSET = maxScrollOffset;
	}

	// 处理手势冲突问题
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mParentView == null) {
			return super.onInterceptTouchEvent(ev);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mParentView.requestDisallowInterceptTouchEvent(true);
			mStartX = ev.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			mLastX = ev.getX();
			if (getScrollX() == 0 && (mLastX - mStartX > 5)) {
				mParentView.requestDisallowInterceptTouchEvent(false);
			} else {
				mParentView.requestDisallowInterceptTouchEvent(true);
			}

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mParentView.requestDisallowInterceptTouchEvent(true);

			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	//添加弹性效果
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		int newScrollX = scrollX + deltaX;
		int newScrollY = scrollY + deltaY;

		final int left = -maxOverScrollX - MAX_SCROLL_OFFSET;
		final int right = maxOverScrollX + scrollRangeX + MAX_SCROLL_OFFSET;
		final int top = -maxOverScrollY;
		final int bottom = maxOverScrollY + scrollRangeY;

		boolean clampedX = false;
		if (newScrollX > right) {
			newScrollX = right;
			clampedX = true;
		} else if (newScrollX < left) {
			newScrollX = left;
			clampedX = true;
		}

		boolean clampedY = false;
		if (newScrollY > bottom) {
			newScrollY = bottom;
			clampedY = true;
		} else if (newScrollY < top) {
			newScrollY = top;
			clampedY = true;
		}

		onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

		return clampedX || clampedY;
	}

}
