package com.android.main.lib.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.main.lib.util.FileHelper;
import com.android.main.lib.variable.Variable;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	public Context context;

	public CrashHandler(Context context) {
		this.context = context;
	}

	public void uncaughtException(Thread arg0, Throwable arg1) {
		String versioninfo = getVersionInfo();
		String mobileInfo = getMobileInfo();
		String errorinfo = getErrorInfo(arg1);
		Log.e(TAG, "versioninfo-->" + versioninfo);
		Log.e(TAG, "mobileInfo-->" + mobileInfo);
		Log.e(TAG, "errorinfo-->" + errorinfo);

		if (Variable.CRASH_2_FILE) {// 是发布版，并且还得运行输入错误日志到文件中
			StringBuilder sb = new StringBuilder();
			sb.append("当前应用程序版本：").append(versioninfo).append("\n").append("手机设备信息：\n").append(mobileInfo).append("\n").append("崩溃日志：\n").append(errorinfo);
			saveCrashInfo2File(sb.toString());
		}
	}

	/**
	 * 获取错误的信息
	 * 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

	/**
	 * 获取手机的硬件信息
	 * 
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		try {
			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取手机的版本信息
	 * 
	 * @return
	 */
	private String getVersionInfo() {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param logMsg
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(String logMsg) {
		DateFormat formatter = new SimpleDateFormat("MM-dd-HH-mm-ss");
		try {
			String time = formatter.format(new Date());

			String fileName = "log-" + time + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				if (!FileHelper.fileExist(Variable.LOG_PATH, fileName)) {
					FileHelper.createFile(Variable.LOG_PATH, fileName);
				}

				FileOutputStream fos = new FileOutputStream(Variable.LOG_PATH + fileName);
				fos.write(logMsg.getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
