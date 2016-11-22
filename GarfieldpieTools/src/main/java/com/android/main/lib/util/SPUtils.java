package com.android.main.lib.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * SharedPreferences封装类
 * 
 * @author 徐宁生
 * @since 2.0
 */
public class SPUtils {

	private static SPUtils SPUtilsManager = null;

	/**
	 * 保存在手机里面的文件名
	 */
	public static String FILE_NAME = "share_data";

	SharedPreferences sp;
	SharedPreferences.Editor editor;

	public SPUtils(Context mContext) {
		sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	/**
	 * 获取单例
     */
	public static SPUtils getInstance(Context mContext){
		if (SPUtilsManager == null) {
			SPUtilsManager = new SPUtils(mContext);
		}
		return SPUtilsManager;
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 */
	public void put(String key, Object object) {
		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 */
	public Object get(String key, Object defaultObject) {

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	public String get(String key, String defaultValue) { return sp.getString(key, defaultValue); }

	public float get(String key, float defaultValue) {
		return sp.getFloat(key, defaultValue);
	}

	public boolean get(String key, boolean defaultValue) { return sp.getBoolean(key, defaultValue); }

	public long get(String key, long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public int get(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 */
	public void remove(String key) {
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 */
	public void clear() {
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 */
	public boolean contains(String key) {
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 */
	public Map<String, ?> getAll() {
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}
