package com.android.main.lib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.main.lib.base.BaseApplication;
import com.android.main.lib.variable.Variable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 此类为基类，没有的方法需自行添加，用时直接继承即可
 * @author Administrator
 *
 */
public class BaseUtil {

	/**
	 * 日志打印
	 * 
	 * @param tag
	 * @param emsg
	 */
	private static final String NULL_EMSG = "";

	public static void e(String tag, String emsg) {
		if (Variable.IS_DEBUG)
			Log.e(tag, emsg == null ? NULL_EMSG : emsg);
	}

	public static void e(String emsg) {
		if (Variable.IS_DEBUG)
			Log.e(Variable.DEFAULT_LOG_TAG, emsg == null ? NULL_EMSG : emsg);
	}

	public static void i(String tag, String emsg) {
		if (Variable.IS_DEBUG)
			Log.i(tag, emsg == null ? NULL_EMSG : emsg);
	}

	public static void i(String emsg) {
		if (Variable.IS_DEBUG)
			Log.i(Variable.DEFAULT_LOG_TAG, emsg == null ? NULL_EMSG : emsg);
	}

	public static void w(String tag, String emsg) {
		if (Variable.IS_DEBUG)
			Log.w(tag, emsg == null ? NULL_EMSG : emsg);
	}

	public static void w(String emsg) {
		if (Variable.IS_DEBUG)
			Log.w(Variable.DEFAULT_LOG_TAG, emsg == null ? NULL_EMSG : emsg);
	}

	public static void d(String tag, String emsg) {
		if (Variable.IS_DEBUG)
			Log.d(tag, emsg == null ? NULL_EMSG : emsg);
	}

	public static void d(String emsg) {
		if (Variable.IS_DEBUG)
			Log.d(Variable.DEFAULT_LOG_TAG, emsg == null ? NULL_EMSG : emsg);
	}

	public static void v(String tag, String emsg) {
		if (Variable.IS_DEBUG)
			Log.v(tag, emsg == null ? NULL_EMSG : emsg);
	}

	public static void v(String emsg) {
		if (Variable.IS_DEBUG)
			Log.v(Variable.DEFAULT_LOG_TAG, emsg == null ? NULL_EMSG : emsg);
	}
	
	/**
	 * 关闭流
	 * 
	 * @param stream
	 */
	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
				stream = null;
			} catch (IOException e) {
				e(e.getMessage());
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						stream = null;
						e(e.getMessage());
					}
					stream = null;
				}
			}
		}
	}
	
	/**
	 * 判断有无内存卡
	 */
	public static boolean hasStorage() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	/**
	 * md5密钥
	 */
	public static String md5(String md5) {
		if (TextUtils.isEmpty(md5)) {
			return null;
		}
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(md5.getBytes("UTF-8"));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 判断网络是否连接
	 * 
	 */
	public static boolean isConnected() {
		Context context = BaseApplication.getInstance();// 这个context是不可能为空的，除非application停止了
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		return networkinfo != null && networkinfo.isAvailable();
	}

	public static String enCodeUtf8(String data) throws UnsupportedEncodingException {
		return URLEncoder.encode(data, "UTF-8");
	}
	
	/**
	 * 取得版本名
	 * @param context
	 * @return
	 */
    public static String getVersionName(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}
    
    /**
     * 取得版本号
     */
    public static int getVersionCode(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return manager.versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}
    
    /**
     * 获得设备相关信息
     * @param c
     * @param lat
     * @param log
     * @return
     */
    public static String getDeviceInfo(Context c,String lat,String log){		
		Map<String, String> map = new HashMap<>();
		try {
			map.put("system", BaseUtil.enCodeUtf8(getSystem()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("program_name", BaseUtil.enCodeUtf8(getProgramName(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("types", BaseUtil.enCodeUtf8(getTypes()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("debug", BaseUtil.enCodeUtf8(getDebug()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("program_version", BaseUtil.enCodeUtf8(getVersionName(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("device_token", BaseUtil.enCodeUtf8(getDeviceToken(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("long", BaseUtil.enCodeUtf8(log));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			map.put("lat", BaseUtil.enCodeUtf8(lat));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("imei", BaseUtil.enCodeUtf8(getIMEI(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("iccid", BaseUtil.enCodeUtf8(getICCID(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			map.put("phone_num", BaseUtil.enCodeUtf8(getPhoneNum(c)));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return BaseJsonUtil.map2json(map);
    }

    public static String getDeviceInfo(Context c){
    	return getDeviceInfo(c,"","");
    }
    
    /**
     * 获取设备SIM卡编码，一般由20位数字组成
     * @param c
     * @return
     */
    public static String getICCID(Context c){
    	TelephonyManager telephonyManager = (TelephonyManager)c
    			.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimSerialNumber();
	}
    
    /**
     * 获取设备IMEI号
     * @param c
     * @return
     */
    public static String getIMEI(Context c){
    	TelephonyManager telephonyManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
    	return telephonyManager.getDeviceId();
    }

    /**
     * 获取设备IMSI号
     */
    public static String getIMSI(Context c){
    	TelephonyManager telephonyManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
    	return telephonyManager.getSubscriberId();
    }

    /**
     * 获取手机号码
     * @param c
     * @return
     */
	public static String getPhoneNum(Context c) {
		TelephonyManager telephonyManager = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getLine1Number();
	}

	/**
     * 获取设备操作系统版本
     * @param 
     * @return 系统版本
     */
    public static String getSystem() {
		String release = Build.VERSION.RELEASE;
		if (!release.toLowerCase().contains("android")) {
			release = "Android " + release;
		}
		return release;
	}
    
    public static String getTypes(){
    	return Build.MODEL;
    }
    
    public static String getDebug(){
    	return Variable.IS_DEBUG ? "1" : "0";
    }
    
    /**
     * 获取项目名
     * @param c
     * @return
     */
	public static String getProgramName(Context c) {
		String applicationName = null;
		try {
			PackageManager packageManager;
			ApplicationInfo applicationInfo;
			packageManager = c.getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(c.getPackageName(), 0);
			applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return applicationName;
	}
    
	/**
	 * 获取设备号
	 * @param c
	 * @return
	 */
    public static String getDeviceToken(Context c) {
    	String deviceToken;
			String androidId = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
			if (!"9774d56d682e549c".equals(androidId)) {
				deviceToken = md5(androidId + c.getPackageName());
			} else {
				String deviceId = ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				deviceToken = md5(deviceId + c.getPackageName());
			}
		return deviceToken;
	}   
    //End 获得设备相关信息
    
    /**
     * 根据屏幕密度获取一个合适的像素值
     * @param c
     * @param dipValue
     * @return
     */
    public static int dip2px(Context c,float dipValue) {
		return (int) (dipValue * Variable.DENSITY + 0.5f);
	}
    
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param context
	 * @param pxValue px值
	 * @return 转化后的dp值
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
    
	/**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
	
	/**
	 * 获取签名  
	 * @param baseString
	 * @param key
	 * @return
	 */
    public static String getSignature(String baseString,String key){  
        String returnStr;
        Mac mac;
        byte[] byteHMAC;
        try {  
            mac = Mac.getInstance("HmacSHA1");  
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");  
            mac.init(spec);  
            byteHMAC = mac.doFinal(baseString.getBytes());  
            returnStr = Base64Encoder.encode(byteHMAC);  
        } catch (Exception e1) {  
            returnStr="error";  
            e1.printStackTrace();  
        }  
        return returnStr;  
    }  
    
    /**
     * Base64位编码
     * @author Administrator
     *
     */
    private static class Base64Encoder {  
        private static final char last2byte = (char) Integer.parseInt("00000011", 2);  
        private static final char last4byte = (char) Integer.parseInt("00001111", 2);  
        private static final char last6byte = (char) Integer.parseInt("00111111", 2);  
        private static final char lead6byte = (char) Integer.parseInt("11111100", 2);  
        private static final char lead4byte = (char) Integer.parseInt("11110000", 2);  
        private static final char lead2byte = (char) Integer.parseInt("11000000", 2);  
        private static final char[] encodeTable = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};  
      
        public static String encode(byte[] from) {  
            StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);  
            int num = 0;  
            char currentByte = 0;  
            for (int i = 0; i < from.length; i++) {  
                num = num % 8;  
                while (num < 8) {  
                    switch (num) {  
                        case 0:  
                            currentByte = (char) (from[i] & lead6byte);  
                            currentByte = (char) (currentByte >>> 2);  
                            break;  
                        case 2:  
                            currentByte = (char) (from[i] & last6byte);  
                            break;  
                        case 4:  
                            currentByte = (char) (from[i] & last4byte);  
                            currentByte = (char) (currentByte << 2);  
                            if ((i + 1) < from.length) {  
                                currentByte |= (from[i + 1] & lead2byte) >>> 6;  
                            }  
                            break;  
                        case 6:  
                            currentByte = (char) (from[i] & last2byte);  
                            currentByte = (char) (currentByte << 4);  
                            if ((i + 1) < from.length) {  
                                currentByte |= (from[i + 1] & lead4byte) >>> 4;  
                            }  
                            break;  
                    }  
                    to.append(encodeTable[currentByte]);  
                    num += 6;  
                }  
            }  
            if (to.length() % 4 != 0) {  
                for (int i = 4 - to.length() % 4; i > 0; i--) {  
                    to.append("=");  
                }  
            }  
            return to.toString();  
        }  
    }  
    
    /**
     * 获取一个统一的路径名，以便统一清除，但项目一定要继承BaseApplication，否则将导致此方法无法正常运行
     * @return 路径名
     */
    public static String getPath(){
    	String path;
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			path=Environment.getExternalStorageDirectory()+"/Android/data/"+ BaseApplication.getInstance().getPackageName()+"/cache/";
		}else{
			path= BaseApplication.getInstance().getCacheDir()+"";
		}
    	return path;
    }
    
    /**
     * 隐藏软键盘
     * @param v
     */
    public static void hideSoftInput(View v){
    	InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
    }
    
    public static void showSoftInput(View v){
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			v.requestFocus();
			imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
    }
    
    /**
     * 数据库升级，往表中添加某字段
     * @param db 可操作的数据库对象
     * @param tableName 要操作的表明（比如：‘news’）
     * @param addedColumnName 要添加的某字段（比如：‘index_pic’）
     * @param type 要添加的某字段的类型（比如：‘varchar(20)’）
     */
    public static void addTableColumn(SQLiteDatabase db,String tableName,String addedColumnName,String type){
    	try {
			db.execSQL("alter table " + tableName + " add " + addedColumnName + " "+ type);
		} catch (SQLException e) {
			Log.e("Log", "DB update:" + tableName + "-" + addedColumnName + " already updated.");
		}
    }

    /**
     * 把指定的文字保存到指定的filename
     * @param msg 
     * @param fileName
     */
    public static void saveToFile(String msg,String fileName){
    	try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = formatter.format(new Date()); 
			msg = time + ":\n" + msg + "\n\n\n";
			 if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { 
			     
				if (!FileHelper.fileExist(Variable.LOG_PATH, fileName)) {
					FileHelper.createFile(Variable.LOG_PATH, fileName);
				}
			     FileOutputStream fos = new FileOutputStream(Variable.LOG_PATH + fileName);
			     fos.write(msg.getBytes()); 
			     fos.close(); 
			 }
		} catch (Exception e) {
		} 
    	
    	
    }
    
	//以下是处理图片的各种工具方法
    /**
     * 按照默认宽高压缩
     * @param filePath
     * @return
     */
		public static Bitmap getSmallBitmap(String filePath) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(filePath, options); 

        //默认按照480*800
        options.inSampleSize = calculateInSampleSize(options, 480, 800); 

        options.inJustDecodeBounds = false; 

        Bitmap bm = BitmapFactory.decodeFile(filePath, options); 
        if(bm == null){ 
            return  null; 
        } 
        int degree = readPictureDegree(filePath); 
        bm = rotateBitmap(bm,degree) ; 
        ByteArrayOutputStream baos = null ; 
        try{ 
            baos = new ByteArrayOutputStream(); 
            bm.compress(CompressFormat.JPEG, 100, baos);
        }finally{ 
            try { 
                if(baos != null) 
                    baos.close() ; 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return bm ; 

    }
	
	 /**
     * 按照默认宽高压缩
     * @param filePath
     * @param size 指定范围大小的图片
     * @return
     */
	public static Bitmap getSmallBitmap(String filePath, int size) { 
        final BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(filePath, options); 

        //默认按照480*800
        options.inSampleSize = calculateInSampleSize(options, 480, 800); 

        options.inJustDecodeBounds = false; 

        Bitmap bm = BitmapFactory.decodeFile(filePath, options); 
        if(bm == null){ 
            return  null; 
        } 
        int degree = readPictureDegree(filePath); 
        bm = rotateBitmap(bm,degree) ; 
        ByteArrayOutputStream baos = null ; 
        try{ 
            baos = new ByteArrayOutputStream(); 
            int quality = 100;
            bm.compress(CompressFormat.JPEG, quality, baos);
            while(baos.toByteArray().length/1024 > size){
            	quality -= 10;
            	baos.reset();
            	bm.compress(CompressFormat.JPEG, quality, baos);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            bm = BitmapFactory.decodeStream(bais, null, null);
        }finally{ 
            try { 
                if(baos != null) 
                    baos.close() ; 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return bm ; 

    }
	
	/**
	 * 按照指定宽高压缩
	 * @param filePath
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) { 
        final BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(filePath, options); 

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); 

        options.inJustDecodeBounds = false; 

        Bitmap bm = BitmapFactory.decodeFile(filePath, options); 
        if(bm == null){ 
            return  null; 
        } 
        int degree = readPictureDegree(filePath); 
        bm = rotateBitmap(bm,degree) ; 
        ByteArrayOutputStream baos = null ; 
        try{ 
            baos = new ByteArrayOutputStream(); 
            bm.compress(CompressFormat.JPEG, 100, baos);
        }finally{ 
            try { 
                if(baos != null) 
                    baos.close() ; 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return bm ; 

    }
	
	/**
	 * 按照指定宽高压缩
	 * @param filePath
	 * @param reqWidth
	 * @param reqHeight
	 * @param size 指定范围的图片
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight, int size) { 
        final BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inJustDecodeBounds = true; 
        BitmapFactory.decodeFile(filePath, options); 

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); 

        options.inJustDecodeBounds = false; 

        Bitmap bm = BitmapFactory.decodeFile(filePath, options); 
        if(bm == null){ 
            return  null; 
        } 
        int degree = readPictureDegree(filePath); 
        bm = rotateBitmap(bm,degree) ; 
        ByteArrayOutputStream baos = null ; 
        try{ 
            baos = new ByteArrayOutputStream();
            int quality = 100;
            bm.compress(CompressFormat.JPEG, quality, baos);
            while(baos.toByteArray().length/1024 > size){
            	quality -= 10;
            	baos.reset();
            	bm.compress(CompressFormat.JPEG, quality, baos);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            bm = BitmapFactory.decodeStream(bais, null, null);
        }finally{ 
            try { 
                if(baos != null) 
                    baos.close() ; 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return bm ; 

    }
	
	/**
	 * 根据指定的宽高计算一个指定的inSampleSize值，用于压缩图片
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) { 
        final int height = options.outHeight; 
        final int width = options.outWidth; 
        int inSampleSize = 1; 

        if (height > reqHeight || width > reqWidth) { 
            final int heightRatio = Math.round((float) height/ (float) reqHeight); 
            final int widthRatio = Math.round((float) width / (float) reqWidth); 

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio; 
        } 

        return inSampleSize; 
    }
		
	/**
	 * 旋转图片，将倒立或侧立的图片处理为正立
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate){ 
        if(bitmap == null) 
            return null ; 

        int w = bitmap.getWidth(); 
        int h = bitmap.getHeight(); 

        Matrix mtx = new Matrix(); 
        mtx.postRotate(rotate); 
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true); 
    }

	/**
	 * 读取图片的拍摄角度，以便旋转
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) { 
        int degree  = 0; 
        try { 
        	ExifInterface exifInterface = new ExifInterface(path); 
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL); 
            switch (orientation) { 
            case ExifInterface.ORIENTATION_ROTATE_90: 
                 degree = 90; 
                 break; 
            case ExifInterface.ORIENTATION_ROTATE_180: 
                 degree = 180; 
                 break; 
            case ExifInterface.ORIENTATION_ROTATE_270: 
                 degree = 270; 
                 break; 
            }    
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return degree; 
    }
	//End 处理图片结束
    
	/**
	 * 创建一个倒影图片
	 * @param originalImage  原始图片
	 * @param rateHeight 原图的高度比，也就是占原图高度的多少比
	 * @param reflectionGap  原图和倒影之间的间隔距离，此距离在此处是透明的
	 * @param shaderStartColor ex:0×70ffffff
	 * @param shaderEndColor ex:0×00ffffff
	 * @return 倒影图片
	 */
	public static Bitmap createReflectedBitmap(Bitmap originalImage, float rateHeight, int reflectionGap, int shaderStartColor, int shaderEndColor) {
	    int width = originalImage.getWidth(); 
	    int height = originalImage.getHeight();
	    //首先我们需要创建一个倒影倒影图
	    Matrix matrix = new Matrix(); 
	    matrix.preScale(1, -1);
	    Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 
	            (int)(height*rateHeight), width, (int)(height*rateHeight), matrix, false);
	    //然后再创建一个画布，用于将三个部分绘入画布：1，原图  2，间隙  3，倒影图
	    Bitmap bitmapWithReflection = Bitmap.createBitmap(width, 
	            (height + (int)(height*rateHeight)), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmapWithReflection);
	    canvas.drawBitmap(originalImage, 0, 0, null);
	    Paint defaultPaint = new Paint(); 
	    canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
	    canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
	    //最后我们需要画渐变色，这样感觉就像倒影了
	    Paint paint = new Paint(); 
	    LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight()  + reflectionGap, 
	    										   shaderStartColor, shaderEndColor, TileMode.MIRROR);
	    paint.setShader(shader);
	    paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	    canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
	    return bitmapWithReflection; 
	}
	
	/**
	 * 微信分享相关
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
    /**
     * 对图片本身进行操作。尽量不要使用setImageBitmap、setImageResource、BitmapFactory.decodeResource
     * 来设置一张大图，因为这些方法在完成decode后，最终都是通过java层的createBitmap来完成的，需要消耗更多内存。
     * 因此，改用先通过BitmapFactory.decodeStream方法，创建出一个bitmap，再将其设为ImageView的source，decodeStream
     * 最大的秘密在于其直接调用JNI>>nativeDecodeAsset()来完成decode，无需再使用java层的createBitmap，从而节省了java层的空间。
     * @param context
     * @param resId
     * @return
     * 
     */
    @SuppressWarnings("deprecation")
	public static Bitmap getBackgroundResouceBitMap(Context context, int resId){  
	    BitmapFactory.Options opt = new BitmapFactory.Options();  
	    opt.inPreferredConfig = Config.RGB_565;
	    opt.inPurgeable = true;  
	    opt.inInputShareable = true;  
	    InputStream is = context.getResources().openRawResource(resId);  
	    return BitmapFactory.decodeStream(is,null,opt);  
	}
    
    @SuppressWarnings("deprecation")
	public static Bitmap getBackgroundResouceBitMap(InputStream is){  
	    BitmapFactory.Options opt = new BitmapFactory.Options();  
	    opt.inPreferredConfig = Config.RGB_565;
	    opt.inPurgeable = true;  
	    opt.inInputShareable = true;  
	    return BitmapFactory.decodeStream(is,null,opt);  
	}
	
    /**
     * 启动与此Context相关的App
     * @param c
     */
	public static void StartApp(Context c){
		Intent intent=c.getPackageManager().getLaunchIntentForPackage(c.getPackageName());
		c.startActivity(intent);
	}
	
	/**
     * 启动指定的App
     * @param c
     */
	public static void StartApp(Context c, String packageName){
		Intent intent=c.getPackageManager().getLaunchIntentForPackage(packageName);
		c.startActivity(intent);
	}

	/**
	 * Unchecked Cast 解决方法
	 * @param obj check对象
	 * @return (T) obj
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object obj) {
		return (T) obj;
	}


}
