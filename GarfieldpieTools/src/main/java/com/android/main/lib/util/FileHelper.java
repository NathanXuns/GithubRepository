package com.android.main.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.android.main.lib.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;


public class FileHelper {

	public static final double DOUBLE = 20.;
	public static final double DOUBLE1 = 9.;

	/**
	 * 由指定的路径和文件名创建文件
	 * 
	 * @param path
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String path, String name) throws IOException {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(path + "/" + name);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 生成文件，如果父文件夹不存在，则先生成父文件夹
	 * 
	 * @param fileName
	 *            :要生成的文件全路径
	 * @return File对象，如果有文件名不存在则返回null
	 */
	public static File createFile(String fileName) {

		if (fileName == null || fileName.length() <= 0) {
			return null;
		}
		File file = new File(fileName);
		// 获取父文件夹
		File folderFile = file.getParentFile();
		if (!folderFile.exists()) {
			folderFile.mkdirs();
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 * @param name
	 * @return
	 */
	public static boolean fileExist(String path, String name) {
		File file = new File(path + name);
		if (file.exists() && !file.isDirectory()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean fileExist(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		
		try {
			File f = new File(filePath);
			if (!f.exists()) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title: copyDrawableFiles
	 * @Description: 拷贝drawable下的图片到指定位置
	 * @param name
	 * @param path
	 * @return boolean
	 */
	public static boolean copyDrawableFiles(Context c,String name, String path) {
		try {
			int id = R.drawable.class.getField(name).getInt(R.drawable.class);
			Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), id);
			savBitmap(bitmap, name + ".png", path);
		} catch (IllegalArgumentException e) {
			BaseUtil.e(e.getMessage());
			return false;
		} catch (IllegalAccessException e) {
			BaseUtil.e(e.getMessage());
			return false;
		} catch (NoSuchFieldException e) {
			BaseUtil.e(e.getMessage());
			return false;
		} catch (IOException e) {
			BaseUtil.e(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title: savBitmap
	 * @Description: 保存bitmap成图片
	 * @param bitmap
	 * @param name
	 * @param path
	 * @throws IOException
	 */
	public static void savBitmap(Bitmap bitmap, String name, String path) throws IOException {
		if (bitmap == null) {
			return;
		}
		File file = createFile(path, name);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (FileNotFoundException e) {
			BaseUtil.e(e.getMessage());
		} finally {
			BaseUtil.closeStream(fos);
		}
	}

	/**
	 * 
	 * @Title: savBitmap
	 * @Description: 保存bitmap成图片
	 * @param bitmap
	 * @param name
	 *            完整的文件名（带有路径）
	 * @param quality
	 * @throws IOException
	 */
	public static void savBitmap(Bitmap bitmap, int quality,String name) throws IOException {
		if (bitmap == null) {
			return;
		}
		File f = new File(name);
		if (!f.exists()) {
			f.createNewFile();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(name);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			BaseUtil.e(e.getMessage());
		} finally {
			BaseUtil.closeStream(fos);
		}
	}

	/**
	 * 
	 * @Title: readInitFile
	 * @Description:读取初始化文件（内置数据）
	 * @return void
	 */
	public static JSONObject readInitFile(Context c) {
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		StringBuilder sb = new StringBuilder();
		JSONObject jsonObj = null;
		try {
			inputReader = new InputStreamReader(c.getResources().getAssets().open("module_init.json"));
			bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				if (line.startsWith("//")) {
					continue;
				}
				sb.append(line);
			}
			jsonObj = new JSONObject(sb.toString());
		} catch (IOException e) {
			BaseUtil.e(e.getMessage());
		} catch (JSONException e) {
			BaseUtil.e(e.getMessage());
		} finally {
			BaseUtil.closeStream(bufReader);
			BaseUtil.closeStream(inputReader);
		}
		return jsonObj;
	}

	/**
	 * @Title: getTestData
	 * @Description: 该方法用于读取测试数据，例如一串sql,json,xml等
	 * @param @param rawid raw目录下对应的文件名称
	 * @return String
	 */
	public static String getTestData(Context c,int rawid) {
		InputStream is = null;
		InputStreamReader reader = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			// is =
			// Variable.appllicationContext.getResources().getAssets().open("dbchange.txt");
			is = c.getResources().openRawResource(rawid);
			reader = new InputStreamReader(is);
			br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			BaseUtil.closeStream(br);
			BaseUtil.closeStream(reader);
			BaseUtil.closeStream(is);
		}
		return sb.toString();
	}

	/**
	 * @Title: deleteFile
	 * @Description: 删除文件
	 * @return boolean
	 */
	public static boolean deleteFile(String path, String name) {
		if (!fileExist(path, name)) {
			return false;
		}
		File file = new File(path, name);
		file.delete();
		return true;
	}
	
	/**
	 * @Title: deleteFile
	 * @Description: 删除文件
	 * @param file
	 * @return boolean
	 */
	public static boolean deleteFile(File file) {
		return file.delete();
	}
	
	/**
	 * @Title: deleteFile
	 * @Description: 删除文件
	 * @param path
	 * @return boolean
	 */
	public static boolean deleteFile(String path) {
		File file = new File(path);
		return file.delete();
	}

	/**
	 * 删除文件夹
	 * @param file
     */
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	/**
	* @Title: copyFile
	* @Description: 拷贝文件
	* @param @param srcPath
	* @param @param srcName
	* @param @param desPath
	* @param @param desName
	* @return boolean
	 */
	public static boolean copyFile(String srcPath, String srcName, String desPath, String desName) {
		if (!fileExist(srcPath, srcName)) {
			return false;
		}
		
		InputStream is = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		BufferedOutputStream bos = null;
		try {
			File inFile = new File(srcPath, srcName);
			File outFile = new File(desPath, desName);
			
			if(!fileExist(desPath, desName)){
				createFile(desPath, desName);
			}
			
			is = new FileInputStream(inFile);
			bis = new BufferedInputStream(is);

			os = new FileOutputStream(outFile);
			bos = new BufferedOutputStream(os);

			byte[] buffer = new byte[1024 * 8];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseUtil.closeStream(bis);
			BaseUtil.closeStream(is);
			BaseUtil.closeStream(bos);
			BaseUtil.closeStream(os);
		}
		return false;
	}
	
	/**
	* @Title: copyFile
	* @Description: 拷贝文件
	* @param @param srcPath
	* @param @param desPath
	* @param @param desName
	* @return boolean
	 */
	public static boolean copyFile(String srcPath, String desPath, String desName) {
		if (!new File(srcPath).exists()) {
			return false;
		}
		
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			File inFile = new File(srcPath);
			File outFile = new File(desPath, desName);
			
			if(!fileExist(desPath, desName)){
				createFile(desPath, desName);
			}
			
			fis = new FileInputStream(inFile);
			bis = new BufferedInputStream(fis);

			fos = new FileOutputStream(outFile);
			bos = new BufferedOutputStream(fos);

			byte[] buffer = new byte[1024 * 8];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BaseUtil.closeStream(bis);
			BaseUtil.closeStream(fis);
			BaseUtil.closeStream(bos);
			BaseUtil.closeStream(fos);
		}
		return false;
	}
	
	public static String getMimeType(String fileUrl) {
		try {
			FileNameMap fileNameMap = URLConnection.getFileNameMap();
			String type = fileNameMap.getContentTypeFor(fileUrl);
			return type;
		} catch (Exception e) {
			return "text/plain";
		}
	}
}
