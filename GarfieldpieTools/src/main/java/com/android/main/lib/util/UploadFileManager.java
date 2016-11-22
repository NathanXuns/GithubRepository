package com.android.main.lib.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;

import com.android.main.lib.interfaces.ProgressListener;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <b>注意事项:</b>
 * <p>1,此类的String类型参数直接通过HttpParam传递过来，当然也可拼在url里，但不建议此类做法
 * <p>2,上传的文件类型数据需要通过startUploadFile()方法传递过来
 * <p>3,此类不支持多文件上传，如有需要可通过循环队列处理此问题
 * <p>4,如用法有所局限给大家带来不便，欢迎补充，谢谢：）
 * <p> <b>具体用法：</b>
 * <p> UploadFileManager uploadFileManager=new UploadFileManager(url);
 * <p> uploadFileManager.setOnProgressListener(...);
 * <p> uploadFileManager.startUploadFile("key", filepath, 480, 800);
 * @author Administrator
 *
 */

public class UploadFileManager {
	private OnProgressListener onProgressListener;
	
	// 参数，放入Part中
	private Map<String, String> partParams;
		
	// 头参，放入Header中
	private Map<String, String> headerParams;
	
	private String mUrl;
	
	private long totalSize=0;
	
	private File mCurFile;
	
	private int size = 0;
	
	public UploadFileManager(String url){
		this.mUrl = url;
		
	}
	
	public void setPartParams(Map<String, String> params){
		this.partParams = params;
	}
	
	public void setHeaderParams(Map<String, String> params) {
		this.headerParams = params;
	}
	
	/**
	 * 启动上传文件的方法, 如果是图片建议使用startUploadFile(String key, String filePath, int reqWidth, int reqHeight)方法
	 * <p>PS:如果用此方法上传图片，则表示上传原图/原文件
	 * @param key 上传文件的接口对应key值
	 * @param filePath 上传文件的本地路径名
	 */
	public void startUploadFile(String key, String filePath){
		UploadFileThread thread=new UploadFileThread(key, new File(filePath));
		thread.start();		
	}
	
	/**
	 * 启动上传图片的方法, 此方法会按照指定图片比例压缩图片，如果reqWidht,reqHeight均为0,那么将按照默认宽高(480*800)压缩
	 * @param key 上传文件的接口对应key值
	 * @param filePath 上传文件的本地路径名
	 */
	public void startUploadFile(String key, String filePath, int reqWidth, int reqHeight){
		new MySavePic().execute(key, filePath, String.valueOf(reqWidth), String.valueOf(reqHeight));		
	}
	
	/**
	 * 启动上传图片的方法, 此方法会按照指定图片比例压缩图片，如果reqWidht,reqHeight均为0,那么将按照默认宽高(480*800)压缩
	 * @param key 上传文件的接口对应key值
	 * @param filePath 上传文件的本地路径名
	 * @param size KB 指定压缩图片大小范围
	 */
	public void startUploadFile(String key, String filePath, int reqWidth, int reqHeight, int size){
		this.size = size;
		new MySavePic().execute(key, filePath, String.valueOf(reqWidth), String.valueOf(reqHeight));		
	}
		
	//上传文件的线程
	private class UploadFileThread extends Thread{
		public HttpClient httpClient;
		private String key;
		private File file;
		
		public UploadFileThread(String key, File file){
			this.key=key;
			this.file=file;
		}
		
		@Override
		public void run() {
			try {
				httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();

				HttpPost httpPost=new HttpPost(mUrl);
				if (headerParams != null) {
					for (Entry<String, String> entry : headerParams.entrySet()) {
						httpPost.addHeader(entry.getKey(), entry.getValue());
					}
				}

				CustomMultipartEntity multipartContent=new CustomMultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, new ProgressListener() {
					@Override
					public void transferred(long num) {
						if (onProgressListener != null) {
							onProgressListener.onProgress((int) ((num / (float) totalSize) * 100));
						}
					}
				});
				
				if(null != partParams){
					for(Entry<String, String> entry : partParams.entrySet()){
						multipartContent.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
					}
				}
				
				FileBody fileBody = new FileBody(file, FileHelper.getMimeType(file.getPath()));
				multipartContent.addPart(key, fileBody);
				totalSize = multipartContent.getContentLength();
				httpPost.setEntity(multipartContent);
				// 设置Header
				if (null != headerParams) {
					for (Entry<String, String> entry : headerParams.entrySet()) {
						httpPost.addHeader(entry.getKey(), entry.getValue());
					}
				}

				HttpResponse response = httpClient.execute(httpPost,httpContext);
				String serverResponse = EntityUtils.toString(response.getEntity());				
				// 服务器响应
				if (onProgressListener != null) {
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						onProgressListener.onFinished(serverResponse);
					} else {
						onProgressListener.onFailure(serverResponse);
					}
				}
				if(null!=mCurFile){
					mCurFile.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (onProgressListener != null) {
					onProgressListener.onFailure(e.getMessage());
				}
				if(null!=mCurFile){
					mCurFile.delete();
				}
			}finally{
				try {
					httpClient.getConnectionManager().shutdown();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	//压缩图片
	@SuppressLint("NewApi")
	private class MySavePic extends AsyncTask<String,String,String> {
		private String key;
		@Override
		protected String doInBackground(String... params) {
			key=params[0];
			String filePath=params[1];
			Bitmap bitmap;
			if("0".equals(params[2]) && "0".equals(params[3])){
				if(size == 0){
					bitmap=BaseUtil.getSmallBitmap(filePath);
				}else{
					bitmap=BaseUtil.getSmallBitmap(filePath, size);
				}
			}else{
				if(size == 0) {
					bitmap=BaseUtil.getSmallBitmap(filePath, Integer.parseInt(params[2]), Integer.parseInt(params[3]));
				}else{
					bitmap=BaseUtil.getSmallBitmap(filePath, Integer.parseInt(params[2]), Integer.parseInt(params[3]), size);
				}
			}

			String fileDir=BaseUtil.getPath();
			if(!new File(fileDir).exists()){
				new File(fileDir).mkdirs();
			}
			String filepath = fileDir + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+".jpg";
//			String filepath=fileName+".jpg";		
			mCurFile = new File(filepath);
			try{
				FileOutputStream fout=new FileOutputStream(mCurFile.getPath());
				bitmap.compress(CompressFormat.JPEG, 100, fout);
				fout.flush();
				fout.close();
				bitmap.recycle();
			}catch(Exception e){
				e.printStackTrace();
				return "Failure";
			}
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {
			//showToast(result);
			if(result.equals("Success")){
				//启动上传文件的线程
				UploadFileThread thread=new UploadFileThread(key, mCurFile);
				thread.start();							
			} else {
				if (onProgressListener != null) {
					onProgressListener.onFailure("Failed to process bitmap.");
				}
			}
		}
		
	}
	
	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

	public interface OnProgressListener{
		void onProgress(int progress);
		
		void onFinished(String response);
		
		void onFailure(String response);
	}
		
}
