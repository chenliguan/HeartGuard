/**
 * @file ActivityManager.java
 * @description 用于处理退出程序时可以退出所有的activity，而编写的通用类
 * @author Guan
 * @date 2015-6-9 下午11:14:47 
 * @version 1.0
 */
package cn.heart.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;

/**
 * @description 用于处理退出程序时可以退出所有的activity，而编写的通用类
 * @author Guan
 * @date 2015-6-9 下午11:14:47
 * @version 1.0
 */
public class ActivityManager {

	private List<Activity> mActivityList = new LinkedList<Activity>();
	private static ActivityManager mInstance;

	private ActivityManager() {
	}

	/**
	 * @description 单例模式中获取唯一的MyApplication实例@description
	 * @return
	 */
	public static ActivityManager getInstance() {
		if (null == mInstance) {
			mInstance = new ActivityManager();
		}
		return mInstance;
	}

	/**
	 * @description 添加Activity到容器中
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivityList.add(activity);
	}

	/**
	 * @description 遍历所有Activity并finish
	 */
	public void exit() {
		for (Activity activity : mActivityList) {
			activity.finish();
		}
		System.exit(0);
	}
}