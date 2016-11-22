package com.android.main.lib.views;


import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.main.lib.R;


public class CustomToast {

	private static Toast toast;

	public static Toast makeText(Context context, CharSequence msg,
			int duration) {
		return getToast(context, msg, duration);
	}
	
	public static Toast makeText(Context context, int resId,
			int duration) {
		return getToast(context, context.getString(resId), duration);
	}
	
	private static Toast getToast(Context context, CharSequence msg, int duration){
		if(toast!=null){
			toast.cancel();
		}
		
		LinearLayout ll = (LinearLayout) LinearLayout.inflate(context,
				R.layout.custom_toast, null);
		TextView textView = (TextView) ll.findViewById(R.id.message);
		textView.setText(msg);
		toast = new Toast(context);
		toast.setView(ll);
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		return toast;
	}

}
