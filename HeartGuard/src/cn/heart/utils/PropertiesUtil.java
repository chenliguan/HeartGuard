/**
 * @file PropertiesUtil.java
 * @description PropertiesUtil类
 * @author Guan
 * @date 2015-6-10 下午3:15:00 
 * @version 1.0
 */
package cn.heart.utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

/**
 * @description PropertiesUtil类
 * @author Guan
 * @date 2015-6-10 下午3:15:00
 * @version 1.0
 */
public class PropertiesUtil {
	private static String urlProps;

	public static String getProperties(Context context) {

		Properties props = new Properties();
		try {
			// 通过activity中的context攻取setting.properties的FileInputStream
			InputStream in = context.getAssets().open("http.properties");
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}

		urlProps = props.getProperty("URL");

		// 测试是否能获得setting.properties中url的值
		System.out.println(urlProps);
		Log.d("tag", "urlProps:" + urlProps);

		return urlProps;
	}
}