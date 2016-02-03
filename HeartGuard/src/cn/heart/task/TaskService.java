/**
 * @file TaskService.java
 * @description TaskService类
 * @author Michael
 * @date 2015-6-19 下午3:40:58 
 * @version 1.0
 */
package cn.heart.task;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * @description TaskService类
 * @author Michael
 * @date 2015-6-19 下午3:40:58
 * @version 1.0
 */
public class TaskService extends Service implements Runnable {
	private final String TAG = "TaskService";

	private static ArrayList<Task> mTaskList = new ArrayList<Task>();
	private static ArrayList<Activity> mActivityList = new ArrayList<Activity>();

	private Thread mThread;
	private boolean isRun = true;

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("启动后台服务=============");
		Log.d(TAG, "onCreate");

		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOGD("onStartCommand");
		if (mThread == null && !isRun) {
			mThread = new Thread(this);
			mThread.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public static void newTask(Task task) {
		mTaskList.add(task);
	}

	/**
	 * @description 根据Activity的名字，返回其对应的Activity对象
	 * @param name
	 *            Activity的名字
	 * @return 对应的Activity对象
	 */
	public static Activity getActivityByName(String name) {
		for (Activity ac : mActivityList) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}

		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		mThread = null;
		isRun = false;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void run() {
		while (isRun) {
			Task lastTask = null;
			if (mTaskList.size() > 0) {
				lastTask = mTaskList.get(0);
				doTask(lastTask);
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
	}

	private void doTask(Task task) {
		Message msg = mHandler.obtainMessage();
		msg.what = task.getTaskId();
		Log.d(TAG, "==== doTask ====");
		msg.obj = task;
		mHandler.sendMessage(msg); // 发送消息
	}

	private static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 得到已完成的目标任务
			Task task = (Task) (msg.obj);
			if (task == null)
				return;
			// 得到执行目标任务的Activity
			Activity act = task.getActivity();
			if (act == null)
				return;
			Task.Callback cb = (Task.Callback) act;
			// 调用目标任务完成后需要Activity中UI线程需要做的工作
			cb.onTaskFinished(task);
			// 执行任务结束，移出任务
			mTaskList.remove(task);
		}
	};

	private void LOGD(String msg) {
		Log.d(TAG, msg);
	}
}
