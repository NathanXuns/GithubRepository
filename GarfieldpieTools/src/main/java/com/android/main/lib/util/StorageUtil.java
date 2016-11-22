package com.android.main.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 本地存储方法，包括Preferences和序列化到本地
 *
 */
public class StorageUtil{
	
	/**
	 * 以SharedPreferences的方式存int数据
	 * @param context
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void saveIntDataBySharedPreferences(Context context,String fileName,String key,int value){
		if(context == null ||fileName == null ||key == null )
			return;
		if(fileName.equals("")||key.equals(""))
			return;
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * 以SharedPreferences的方式存string数据
	 * @param context
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public static void saveStringDataBySharedPreferences(Context context,String fileName,String key,String value){
		if(context == null ||fileName == null ||key == null || value ==null)
			return;
		if(fileName.equals("")||key.equals("")|| value.equals(""))
			return;
		
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 从SharedPreferences中取响应的数据数据
	 * @param context
	 * @param fileName
	 * @param key
	 * @return 获取出来的值
	 */
	public static int getIntDataFromSharedPreferences(Context context,String fileName,String key){
		if(context == null ||fileName == null ||key == null )
			return -1;
		if(fileName.equals("")||key.equals(""))
			return -1;
		
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		int value = sp.getInt(key, 0);
		return value;
	}
	
	/**
	 * 从SharedPreferences中取响应的数据数据
	 * @param context
	 * @param fileName
	 * @param key
	 * @return 获取出来的值
	 */
	public static String getStringDataFromSharedPreferences(Context context,String fileName,String key){
		if(context == null ||fileName == null ||key == null )
			return null;
		if(fileName.equals("")||key.equals(""))
			return null;
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		String value = sp.getString(key,null);
		return value;
	}
	
	/**
	 * 将对像序列化到本地文件
	 * @param obj 序列化的对象
	 * @param fileName 本地路径
	 * @return true：成功，false：失败
	 */
	public static boolean SerializeToFile(Object obj, String fileName) {
		boolean flag = false;
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		if (obj == null || TextUtils.isEmpty(fileName)) {
			return false;
		}

		try {
			File f = new File(fileName);
			if (f.exists()) {
				f.delete();
			} else {
				f = FileHelper.createFile(fileName);
			}
			fos = new FileOutputStream(fileName);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.flush();
			flag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return flag;
	}

	/**
	 * 读一个文件反序列化为一个对像
	 * @param fileName 本地路径
	 * @return object
	 */
	public static Object FileToObject(String fileName) {
		if (fileName == null || fileName.equals("")) {
			return null;
		}
		File file = new File(fileName);
		if(!file.exists()){
			file= FileHelper.createFile(fileName);
		}
		return FileToObject(file);
	}
	
	/**
	 * 读一个文件反序列化为一个对像
	 * @param file File 对象
	 * @return object
	 */
	public static Object FileToObject(File file){
		Object o = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		if(file!=null && file.exists()){
			try {
				fis = new FileInputStream(file) ;
				ois = new ObjectInputStream(fis);
				o = ois.readObject();
		
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if (fis != null)
						fis.close();
					if (ois != null)
						ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return o;
	}

}