package com.android.main.lib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.main.lib.R;

/**
 * 状态显示不同的View
 * 
 * @author gaojianming
 * @date 2014年9月29日 上午10:02:45
 */
public class StatusSwitchLayout extends RelativeLayout {
	private View vContentView;
	private LinearLayout vRequestLayout;
	private LinearLayout vFailureLayout;
	private LinearLayout vNoDataLayout;
	
	private ImageView vRequestImg;
	private ImageView vFailureImg;
	private ImageView vNoDataImg;
	private TextView vNoDataTxt;
	private Button vNoDataBtn;
	private Button vFailureBtn;
	
	public StatusSwitchLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	public StatusSwitchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public StatusSwitchLayout(Context context) {
		super(context);
		initWithContext(context);
	}
	
	private void initWithContext(Context context){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.status_switch_layout, this);
		
		vRequestLayout = (LinearLayout)findViewById(R.id.request_layout);
		vFailureLayout = (LinearLayout)findViewById(R.id.loading_failure_layout);
		vNoDataLayout = (LinearLayout)findViewById(R.id.no_data_layout);
		
		vRequestImg = (ImageView)findViewById(R.id.request_loading_img);
		vFailureImg = (ImageView)findViewById(R.id.loading_failure_img);
		vNoDataTxt = (TextView) findViewById(R.id.no_data_txt);
		vNoDataImg = (ImageView)findViewById(R.id.no_data_img);
		vNoDataBtn = (Button)findViewById(R.id.other_operate_button);
		vFailureBtn = (Button)findViewById(R.id.loading_failure_button);
		
	}
	
	public View getContentView() {
		return vContentView;
	}
	
	/**
	 * 设置content layout 便于统一控制显示哪个layout
	 * @param vContentView
	 */
	public void setContentView(View vContentView) {
		this.vContentView = vContentView;
	}

	public LinearLayout getRequestLayout() {
		return vRequestLayout;
	}

	public LinearLayout getFailureLayout() {
		return vFailureLayout;
	}

	public LinearLayout getNoDataLayout() {
		return vNoDataLayout;
	}

	public ImageView getRequestImg() {
		return vRequestImg;
	}

	public ImageView getFailureImg() {
		return vFailureImg;
	}

	public TextView getNodatatxt(){
		return vNoDataTxt;
	}
	
	public ImageView getNoDataImg() {
		return vNoDataImg;
	}

	public Button getNoDataBtn() {
		return vNoDataBtn;
	}
	
	public Button getFailureBtn(){
		return vFailureBtn;
	}

	public void setRequestImgRes(int resId){
		vRequestImg.setImageResource(resId);
	}
	
	public void setFailureImgRes(int resId){
		vFailureImg.setImageResource(resId);
	}
	
	public void setNodataText(int resid){
		vNoDataTxt.setText(resid);
	}
	
	public void setNodataText(String str){
		vNoDataTxt.setText(str);
	}
	
	public void setNoDataImgRes(int resId){
		vNoDataImg.setImageResource(resId);
	}
	
	public void setNoDataBtnBg(int resId){
		vNoDataBtn.setBackgroundResource(resId);
	}
	
	public void setFailureBtnBg(int resId){
		vFailureBtn.setBackgroundResource(resId);
	}
	
	public void showContentLayout(){
		showWhichLayout(0);
	}
	
	public void showNoDataLayout(){
		showWhichLayout(1);
	}
	
	public void showRequestLayout(){
		showWhichLayout(2);
	}
	
	public void showFailureLayout(){
		showWhichLayout(3);
	}
	
	public void dismissAll(){
		showWhichLayout(4);
	}
	
	/**
	 * 0.代表显示content layout,1.代表显示无数据layout,2.代表显示请求layout,3.代表显示失败layout, 4.代表均不显示(预留显示其他可能的布局)
	 * @param index
	 */
	private void showWhichLayout(int index){
		switch (index) {
		case 0:
			if(null != vContentView && vContentView.getVisibility() == View.GONE){
				vContentView.setVisibility(View.VISIBLE);
			}
			if(vNoDataLayout.getVisibility() == View.VISIBLE){
				vNoDataLayout.setVisibility(View.GONE);
			}
			if(vRequestLayout.getVisibility() == View.VISIBLE){
				vRequestLayout.setVisibility(View.GONE);
			}
			if(vFailureLayout.getVisibility() == View.VISIBLE){
				vFailureLayout.setVisibility(View.GONE);
			}
			break;
		case 1:
			if(null != vContentView && vContentView.getVisibility() == View.VISIBLE){
				vContentView.setVisibility(View.GONE);
			}
			if(vNoDataLayout.getVisibility() == View.GONE){
				vNoDataLayout.setVisibility(View.VISIBLE);
			}
			if(vRequestLayout.getVisibility() == View.VISIBLE){
				vRequestLayout.setVisibility(View.GONE);
			}
			if(vFailureLayout.getVisibility() == View.VISIBLE){
				vFailureLayout.setVisibility(View.GONE);
			}
			break;
		case 2:
			if(null != vContentView && vContentView.getVisibility() == View.VISIBLE){
				vContentView.setVisibility(View.GONE);
			}
			if(vNoDataLayout.getVisibility() == View.VISIBLE){
				vNoDataLayout.setVisibility(View.GONE);
			}
			if(vRequestLayout.getVisibility() == View.GONE){
				vRequestLayout.setVisibility(View.VISIBLE);
			}
			if(vFailureLayout.getVisibility() == View.VISIBLE){
				vFailureLayout.setVisibility(View.GONE);
			}
			break;
		case 3:
			if(null != vContentView && vContentView.getVisibility() == View.VISIBLE){
				vContentView.setVisibility(View.GONE);
			}
			if(vNoDataLayout.getVisibility() == View.VISIBLE){
				vNoDataLayout.setVisibility(View.GONE);
			}
			if(vRequestLayout.getVisibility() == View.VISIBLE){
				vRequestLayout.setVisibility(View.GONE);
			}
			if(vFailureLayout.getVisibility() == View.GONE){
				vFailureLayout.setVisibility(View.VISIBLE);
			}
			break;
		case 4:
			if(null != vContentView && vContentView.getVisibility() == View.VISIBLE){
				vContentView.setVisibility(View.GONE);
			}
			if(vNoDataLayout.getVisibility() == View.VISIBLE){
				vNoDataLayout.setVisibility(View.GONE);
			}
			if(vRequestLayout.getVisibility() == View.VISIBLE){
				vRequestLayout.setVisibility(View.GONE);
			}
			if(vFailureLayout.getVisibility() == View.VISIBLE){
				vFailureLayout.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}
	
}
