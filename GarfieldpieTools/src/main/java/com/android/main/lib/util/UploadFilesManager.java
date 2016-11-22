package com.android.main.lib.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.android.main.lib.interfaces.ProgressListener;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UploadFilesManager {
	public static final double DOUBLE = 17.;
	private String TAG = "UploadFilesManager";
	private OnUpProgressListener onProgressListener;

	private String mUrl;

	private long totalSize = 0;

	private List<Map<String, Pair<String, byte[]>>> fileBytesMap = new ArrayList<>();
	private List<String> mFilePaths = null;

	// 参数，放入Part中
	private Map<String, String> partParams;
	private List<Map> partParamsList;

	// 头参，放入Header中
	private Map<String, String> headerParams;

	private int width = 0;
	private int height = 0;

	private int size = 0;
	private int currentUpFilesIndex = -1; // 当前上传文件下标

	public UploadFilesManager(String url) {
		this.mUrl = url;
	}

	public void setPartParams(Map<String, String> params) {
		this.partParams = params;
	}

	public void setPartParamsList(List<Map> partParamsList){
		this.partParamsList = partParamsList;
	}

	public void setHeaderParams(Map<String, String> params) {
		this.headerParams = params;
	}
	
	/**
	 * 启动上传图片的方法, 此方法会按照指定图片比例压缩图片，如果reqWidht,reqHeight均为0,那么将按照默认宽高(480*800)压缩
	 * 
	 * @param key
	 *            上传文件的接口对应key值
	 * @param filePaths
	 *            上传文件的本地路径名
	 */
	public void startUploadFile(String key, List<String> filePaths, int reqWidth, int reqHeight) {
		this.mFilePaths = filePaths;
		this.width = reqWidth;
		this.height = reqHeight;
		new MySavePic().execute(key);
	}

	/**
	 * 上传指定范围大小的图片
	 * 
	 * @param key
	 * @param filePaths
	 * @param reqWidth
	 * @param reqHeight
	 * @param size
	 *            单位 KB
	 */
	public void startUploadFile(String key, List<String> filePaths, int reqWidth, int reqHeight, int size) {
		this.mFilePaths = filePaths;
		this.width = reqWidth;
		this.height = reqHeight;
		this.size = size;
		new MySavePic().execute(key);
	}

	// 上传文件的线程
	private class UploadFileThread extends Thread {
		public HttpClient httpClient;
		private String key;
		private String userId;
		private List<Map<String, Pair<String, byte[]>>> fileBytesMap;

		public UploadFileThread(String key, List<Map<String, Pair<String, byte[]>>> fileBytesMap) {
			this.key = key;
			this.fileBytesMap = fileBytesMap;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			try {
				httpClient = new DefaultHttpClient();
				HttpContext httpContext = new BasicHttpContext();

				HttpPost httpPost = new HttpPost(mUrl);
				CustomMultipartEntity multipartContent = new CustomMultipartEntity(new ProgressListener() {
					@Override
					public void transferred(long num) {
						onProgressListener.onProgress((int) ((num / (float) totalSize) * 100));
					}
				});
				if (null != partParams) {
					for (Entry<String, String> entry : partParams.entrySet()) {
						multipartContent.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
					}
				}else if (null != partParamsList && partParamsList.size()>0){
					for (int i = 0; i < mFilePaths.size(); i++) {
						if(i < partParamsList.size()) {
							for (Entry<String, String> entry : ((Map<String, String>) partParamsList.get(i)).entrySet()) {
								multipartContent.addPart(entry.getKey(), new StringBody(entry.getValue(), Charset.forName("UTF-8")));
							}
						}
					}
				}

				onProgressListener.onAddPart(multipartContent , currentUpFilesIndex);

				for (Map<String, Pair<String, byte[]>> map : fileBytesMap) {
					for (Entry<String, Pair<String, byte[]>> entry : map.entrySet()) {
						ByteArrayBody fileByte = new ByteArrayBody(entry.getValue().second, entry.getValue().first, entry.getKey());
						multipartContent.addPart(entry.getKey(), fileByte);
					}
				}
				totalSize = multipartContent.getContentLength();
				httpPost.setEntity(multipartContent);
				
				// 设置Header
				if (null != headerParams) {
					for (Entry<String, String> entry : headerParams.entrySet()) {
						httpPost.addHeader(entry.getKey(), entry.getValue());
					}
				}

				HttpResponse response = httpClient.execute(httpPost, httpContext);
				String serverResponse = EntityUtils.toString(response.getEntity());
				System.out.println("serverResponse-->" + serverResponse);
				// 服务器响应
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					onProgressListener.onFinished(serverResponse);
				} else {
					onProgressListener.onFailure(serverResponse);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onProgressListener.onFailure(e.getMessage());
			} finally {
				try {
					httpClient.getConnectionManager().shutdown();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		}
	}

	// 压缩图片
	@SuppressLint("NewApi")
	private class MySavePic extends AsyncTask<String, String, String> {
		private String key;

		@Override
		protected String doInBackground(String... params) {
			key = params[0];
			for (int i = 0; i < mFilePaths.size(); i++) {
				currentUpFilesIndex = i;
				Bitmap bitmap;
				String filePath = mFilePaths.get(i);

				if ("0".equals(String.valueOf(width)) && "0".equals(String.valueOf(height))) {
					if (size == 0) {
						bitmap = BaseUtil.getSmallBitmap(filePath);
					} else {
						bitmap = BaseUtil.getSmallBitmap(filePath, size);
					}
				} else {
					if (size == 0) {
						bitmap = BaseUtil.getSmallBitmap(filePath, width, height);
					} else {
						bitmap = BaseUtil.getSmallBitmap(filePath, width, height, size);
					}
				}

				int start = filePath.lastIndexOf("/");
				int end = filePath.length();
				String fileName;
				if (start != -1 && end != -1) {
					try {
						fileName = filePath.substring(start + 1, end);
					} catch (Exception e) {
						Log.e("UPFILE", TAG + "rename upload file name error:" + e.getMessage());
						Log.e("SYS", TAG + "rename upload file name error:" + e.getMessage());
						fileName = filePath.substring(start + 1, filePath.length() - 1);
					}
				} else {
					fileName = null;
				}
				if (TextUtils.isEmpty(fileName)) {
					continue;
				}

				Map map = new HashMap();
				map.put(filePath.substring(start + 1, end),
						new Pair<>(FileHelper
								.getMimeType(filePath), compressImage(bitmap)));
				fileBytesMap.add(map);
			}
			return "Success";
		}

		@Override
		protected void onPostExecute(String result) {
			// showToast(result);
			if (result.equals("Success")) {
				// 启动上传文件的线程
				UploadFileThread thread = new UploadFileThread(key, fileBytesMap);
				thread.start();
			}
		}

	}

	private byte[] compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

//	private byte[] compressImage2(Bitmap bitmap) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int options = 80;//个人喜欢从80开始,
//		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//		while (baos.toByteArray().length / 1024 > 100) {
//			baos.reset();
//			options -= 10;
//			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//		}
//		try {
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(baos.toByteArray());
//			fos.flush();
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return baos.toByteArray();
//	}

	public void setOnProgressListener(OnUpProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}

	public interface OnUpProgressListener {

		void onAddPart(CustomMultipartEntity multipartContent ,int currentUpFilesIndex);

		void onProgress(int progress);

		void onFinished(String response);

		void onFailure(String response);

	}

}
