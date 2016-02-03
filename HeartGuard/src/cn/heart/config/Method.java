/**
 * @file Method.java
 * @description Method方法类
 * @author Guan
 * @date 2015-6-10 下午3:13:25 
 * @version 1.0
 */
package cn.heart.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.WindowManager;

/**
 * @description Method方法类
 * @author Guan
 * @date 2015-6-10 下午3:13:25
 * @version 1.0
 */
@SuppressLint("SdCardPath")
public class Method {

	/**
	 * @description 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public static void backgroundAlpha(float bgAlpha, Activity activity) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * @description 随机数方法
	 * @param max
	 * @return int
	 */
	public static int RandomTest(int max) {
		int radom = (int) (Math.random() * max);
		System.out.println(radom);
		return radom;
	}
}
