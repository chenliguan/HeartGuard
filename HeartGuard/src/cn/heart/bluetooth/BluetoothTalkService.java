/**
 * @file BluetoothTalkService.java
 * @description BluetoothTalkService类
 * @author Michael
 * @date 2015-6-19 下午3:47:03 
 * @version 1.0
 */
package cn.heart.bluetooth;

import java.util.Vector;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import cn.heart.task.Task;

/**
 * @description BluetoothTalkService类
 * @author Michael
 * @date 2015-6-19 下午3:47:03
 * @version 1.0
 */
public class BluetoothTalkService extends Service implements Runnable {
	private static final String TAG = "BluetoothTalkService";

	private static Vector<Task> mTaskList = new Vector<Task>();

	private Thread mThread;
	private boolean isRun = true;

	@Override
	public void onCreate() {
		super.onCreate();
		mThread = new Thread(this);
		mThread.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOGD("onStartCommand");
		System.out.println("onStartCommand");
		if (mThread == null && !isRun) {
			isRun = true;
			mThread = new Thread(this);
			mThread.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public static void newTask(Task task) {
		mTaskList.add(task);
		System.out.println("添加蓝牙事务=============");
	}

	@Override
	public void run() {
		System.out.println("run===============");
		while (isRun) {
			Task lastTask = null;
			System.out.println("mTaskList:" + mTaskList.size());
			if (mTaskList.size() > 0) {
				lastTask = mTaskList.get(0);
				System.out.println("开始执行任务=============");
				doTask(lastTask);
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		mThread = null;
	}

	private void doTask(Task task) {
		BluetoothConnection conn = BluetoothConnection.getConnection(task
				.getActivity());

		LOGD("==== BluetoothTalkService doTask ====");
		int delay = 200;
		switch (task.getTaskId()) {
		case Task.BT_READ_ECG:
			LOGD("读取心电数据");
			System.out.println("读取心电数据");
			if (task.mParams != null && (Integer) task.mParams[0] > 0)
				delay = (Integer) task.mParams[0];
			if (mEcgDataReadLooper != null && mEcgDataReadLooper.isAlive()) {
				LOGD("EcgDataReadLooper is alive.");
				break;
			}
			mEcgDataReadLooper = new DataReadLooper(task, delay);
			mEcgDataReadLooper.setName("EcgDataReadLooper");
			mEcgDataReadLooper.start();
			break;
		case Task.BT_START_ECG:
			LOGD("开始心电采集");
			System.out.println("开始心电采集");
			task.mResult = BluetoothIO.startECG(conn.getOutputStream());
			System.out.println("doTask的taskmResult:" + task.mResult);
			break;
		case Task.BT_STOP_ECG:
			LOGD("停止心电采集");
			System.out.println("停止心电采集");
			if (mEcgDataReadLooper != null && mEcgDataReadLooper.isAlive()) {
				mEcgDataReadLooper.cancel();
				task.mResult = BluetoothIO.stopECG(conn.getOutputStream());
			} else {
				LOGD("Ecg read looper  already stop.");
			}
			try {
				conn.closeConnection();
			} catch (Exception e) {
			}

			break;
		}

		// 执行任务结束，移出任务
		mTaskList.remove(task);

		// 得到执行目标任务的Activity
		Activity act = task.getActivity();
		if (act == null) {
			return;
		}
		Task.Callback cb = (Task.Callback) act;
		// 调用目标任务完成后需要Activity中UI线程需要做的工作
		cb.onTaskFinished(task);

	}

	private DataReadLooper mEcgDataReadLooper;

	/**
	 * @description 设备数据读取线程
	 * @author Michael
	 */
	private class DataReadLooper extends Thread {
		private boolean isRun = true;
		private Task mTask;
		private BluetoothConnection mConn;
		private int mDelay;

		public void cancel() {
			LOGD("DataReadLooper cancel()");
			isRun = false;
		}

		public DataReadLooper(Task task, int delay) {
			LOGD("DataReadLooper()...");
			this.mTask = task;
			mDelay = delay;
			mConn = BluetoothConnection.getConnection(task.getActivity());
		}

		@Override
		public void run() {
			while (isRun) {
				try {
					Thread.sleep(mDelay);
				} catch (InterruptedException e) {
				}
				LOGD(this.getName() + " > DataReadLooper..");
				switch (mTask.getTaskId()) {
				case Task.BT_READ_ECG:
					try {
						mTask.mResult = BluetoothIO.readECGData(mConn
								.getInputStream());
					} catch (Exception e) {
						cancel();
						mEcgDataReadLooper = null;
						mTask.mResult = null;
					}
					break;
				}

				// 得到执行目标任务的Activity
				Activity act = mTask.getActivity();
				if (act == null)
					return;
				Task.Callback cb = (Task.Callback) act;
				// 调用目标任务完成后需要Activity中UI线程需要做的工作
				cb.onTaskFinished(mTask);
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void LOGD(String msg) {
		Log.d(TAG, msg);
	}
}
