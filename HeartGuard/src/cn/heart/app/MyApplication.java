/**
 * @file MyApplication.java
 * @description 对象且只创建一个，存储系统的activity信息
 * @author Guan
 * @date 2015-6-8 下午9:43:06 
 * @version 1.0
 */
package cn.heart.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * @description 对象且只创建一个，存储系统的activity信息
 * @author Guan
 * @date 2015-6-8 下午9:43:06
 * @version 1.0
 */
public class MyApplication extends Application {
	private List<Activity> activitys = null;
	private static MyApplication sInstance;

	/**
	 * @description MyApplication
	 */
	private MyApplication() {
		activitys = new LinkedList<Activity>();
	}

	/**
	 * @description 单例模式中获取唯一MyApplication实例
	 * @return
	 */
	public static MyApplication getInstance() {
		if (null == sInstance) {
			sInstance = new MyApplication();
		}
		return sInstance;
	}

	/**
	 * @description 添加Activity到容器中
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}
	}

	/**
	 * @description 遍历Activity并finish
	 */
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		System.exit(0);
	}
}
