/**
 * @file App.java
 * @description App
 * @author Guan
 * @date 2015-6-8 下午9:39:41 
 * @version 1.0
 */
package cn.heart.app;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;

import cn.heart.bean.ResultData;

/**
 * @description 存储Volley网络请求的队列
 * @author Guan
 * @date 2015-6-8 下午9:39:41
 * @version 1.0
 */
public class App extends Application {
	private ResultData mResultData;
	public static RequestQueue sQueue;

	/**
	 * @description onCreate
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		sQueue = Volley.newRequestQueue(getApplicationContext());
	}

	public ResultData getResultData() {
		return mResultData;
	}

	public void setResultData(ResultData resultData) {
		this.mResultData = resultData;
	}

	public static RequestQueue getHttpQueue() {
		return sQueue;
	}

}
