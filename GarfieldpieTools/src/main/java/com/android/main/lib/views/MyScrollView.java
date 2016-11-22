package com.android.main.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.android.main.lib.variable.Variable;

/**
 * 如果想让ScrollView获得弹性，建议ScrollView使用该类
 * @author Administrator
 *
 */
public class MyScrollView extends ScrollView {
	//最大滚动偏移量
	private int MAX_SCROLL_OFFSET = (int)(125* Variable.DENSITY);
	
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public MyScrollView(Context context) {
		super(context);
		initWithContext(context);
	}

	private void initWithContext(Context context){
		setOverScrollMode(OVER_SCROLL_NEVER);
	}
	
	public void setMaxScrollOffset(int maxScrollOffset){
		this.MAX_SCROLL_OFFSET = maxScrollOffset;
	}
	
	//添加弹性效果
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		int newScrollX = scrollX + deltaX;
		int newScrollY = scrollY + deltaY;

		final int left = -maxOverScrollX;
		final int right = maxOverScrollX + scrollRangeX;
		final int top = -maxOverScrollY - MAX_SCROLL_OFFSET;
		final int bottom = maxOverScrollY + scrollRangeY + MAX_SCROLL_OFFSET;

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
