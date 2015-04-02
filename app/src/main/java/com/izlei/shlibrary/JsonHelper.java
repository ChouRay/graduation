package com.izlei.shlibrary;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonHelper {
	
	/**
	 * json to Map
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> toMap(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONArray ja = jsonObject.getJSONArray("result");
		
		Map<String, String> result = new HashMap<String, String>();
		Iterator<String> iterator = jsonObject.keys();
		String key = null;
		String value = null;
		
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
			//Log.e("-----", key+"   "+value);
		}
		
		
		//Log.e("--------", ja.getJSONObject(0).getString("picture"));
		return result;
	}
	
	/**
	 * Map to javaBean
	 * @param javaBean
	 * @param data
	 * @return
	 */
	public static Object toJavaBean(Object javaBean	,Map<String,String> data) {
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {

                String field = method.getName();
                field = field.substring(field.indexOf("set") + 3);
                field = field.toLowerCase().charAt(0) + field.substring(1);
                try {
					method.invoke(javaBean, new Object[] {

					data.get(field)

					});
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }
		}
		return javaBean;
	}
	
}
