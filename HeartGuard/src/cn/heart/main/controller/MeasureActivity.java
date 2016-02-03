/**
 * @file MeasureActivity.java
 * @description 测量Activity类
 * @author Guan
 * @date 2015-6-9 下午4:08:55 
 * @version 1.0
 */
package cn.heart.main.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.app.App;
import cn.heart.bean.ResultData;
import cn.heart.bluetooth.BluetoothConnection;
import cn.heart.bluetooth.BluetoothTalkService;
import cn.heart.config.Common;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.task.Task;
import cn.heart.task.Task.Callback;
import cn.heart.utils.SpellHelper;
import cn.heart.view.CircleImageView;
import cn.heart.view.ECGSurfaceView;

import com.example.wechatsample.R;

/**
 * @description 测量Activity类
 * @author Guan
 * @date 2015-6-9 下午4:08:55
 * @version 1.0
 */
public class MeasureActivity extends Activity implements OnClickListener,
		Callback {

	@InjectView(R.id.civ_head_am)
	CircleImageView mCircleHead;
	@InjectView(R.id.tv_nick)
	TextView mNick;
	@InjectView(R.id.tv_rate)
	TextView mRate;
	@InjectView(R.id.sfv_ecg)
	ECGSurfaceView mSurfaceView; 
	
	private App mApp;
//	private String[] mMItems;
	private BTReceiver receiver;
	private ActionBar mActionBar;
	private ResultData mResultData;
	private String mUserstring, mNickstring;
	private SharedPreferences mPreferences;
//	private ECGSurfaceView mSurfaceView;
	private final Handler mTimeHandler = new Handler();
	/**
	 * 全局变量，用于判断是否是第一次执行
	 */
	private int mRunCount = 0;
	private int delay = 5;
	public static final int Measure_LoadData = 0;
	public static final int Measure_R = 1;
	public static final int Measure_T = 2;
	public static CheckHandler sHandler;
	public static MeasureActivity sActivity;
	/**
	 * 心电数据输入流
	 */
	public static InputStream sInputStream = null;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_measure);
		ButterKnife.inject(this);
		// ActionBar
		Action();
		// 初始化
		Init();
		// 蓝牙模块连接
		// Bluetooth();
		// 开始获取数据和导入数据
		Start();
	}

	/**
	 * @description ActionBar
	 */
	@SuppressLint("NewApi")
	private void Action() {
		mActionBar = getActionBar();
		mActionBar.setLogo(R.drawable.ic_back);
		mActionBar.setHomeButtonEnabled(true);
	}

	/**
	 * @description 初始化函数
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public void Init() {

		sHandler = new CheckHandler();
		mResultData = new ResultData();
		mApp = (App) getApplication();
		mApp.setResultData(mResultData);
		mSurfaceView.setResultData(mResultData);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserstring = mPreferences.getString("USER_NAME", "");
			mNickstring = mPreferences.getString("NICK", "");
		}
		mNick.setText(mNickstring);
		sActivity = MeasureActivity.this;
		ImageDownLoader mImageDownLoader = new ImageDownLoader(this);
		final String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUserstring + SpellHelper.getEname(mNickstring) + ".jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			mCircleHead.setImageBitmap(bitmap);
	}

	/**
	 * @description 蓝牙模块连接
	 */
	@SuppressLint("NewApi")
	private void Bluetooth() {
		// 获得指定的连接
		BluetoothConnection.getConnection(this).initBluetoothService(this,
				BluetoothConnection.TYPE_CODE_SCANEER);
		// 绑定并启动蓝牙连接服务
		startService(new Intent(this, BluetoothTalkService.class));
		if (BluetoothConnection.getConnection(this).initBluetoothService(this,
				BluetoothConnection.TYPE_ECG)) {
			// 注册接收器，接收蓝牙建立连接广播
			receiver = new BTReceiver();
			registerReceiver(receiver, new IntentFilter(
					BluetoothConnection.ACTION_BLUETOOTH_CONNECTED));
			// 启动蓝牙服务
			System.out.println("开始启动心电蓝牙服务====================");
			Intent intent = new Intent(MeasureActivity.this,
					BluetoothTalkService.class);
			System.out.println("intent:" + intent);
			startService(intent);
		}
	}

	/**
	 * @description 开始获取数据和导入数据
	 */
	private void Start() {
		// 1.心电数据输入流从文件获取数据
		try {
//			sInputStream = getResources().getAssets().open(
//					mMItems[Method.RandomTest(20)] + ".xml");
			 sInputStream = getResources().getAssets().open("data.xml");
//			Log.d("tag", "xml：" + mMItems[Method.RandomTest(20)] + ".xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		TimerTask task = new TimerTask() {
			public void run() {
				mSurfaceView.destroyDrawingCache();
				// 2.开始从文件导入数据
				mSurfaceView.startLoadData();
			}
		};
		Timer timer = new Timer();
		// 延时2秒
		timer.schedule(task, 2000);
		// 打开定时器，执行测量计时操作
		mTimeHandler.postDelayed(runnable, 20000);
	}

	/**
	 * @description 测量计时线程
	 */
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (mRunCount == 1) {
				mTimeHandler.removeCallbacks(this);
				mSurfaceView.setDestroyed(true);
				// 3.更新数据方法
				mSurfaceView.refreshData();
			}
			mTimeHandler.postDelayed(this, 50);
			mRunCount++;
		}
	};

	// /**
	// * @description 监听触发实现
	// * @author Guan
	// * @date 2015-6-9 下午4:15:07
	// * @version 1.0
	// */
	// public class ResultButtonOnClickListener implements OnClickListener {
	// @Override
	// public void onClick(View arg0) {
	// mSurfaceView.setDestroyed(true);
	// // 3.更新数据方法
	// mSurfaceView.refreshData();
	// }
	// }

	/**
	 * @description handler机制 在android中提供了一种异步回调机制Handler,使用它
	 *              我们可以在完成一个很长时间的任务后做出相应的通知 接受消息
	 * @author Guan
	 * @date 2015-6-9 下午4:15:28
	 * @version 1.0
	 */
	@SuppressLint("HandlerLeak")
	public class CheckHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Measure_LoadData:
				mRate.setText(msg.arg1 + "");
				break;
			case Measure_R:
				// 4.病症的诊断
				mSurfaceView.StartCheck();
				break;
			case Measure_T:
				Toast.makeText(MeasureActivity.this, "检测完毕", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(MeasureActivity.this,
						ShowResultActivity.class);
				intent.putExtra("dataY", mSurfaceView.getDataY());
				startActivity(intent);
				overridePendingTransition(R.anim.out_from_right,
						R.anim.out_to_left);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * @description 蓝牙广播接收
	 * @author Guan
	 * @date 2015-6-11 下午11:16:04
	 * @version 1.0
	 */
	private class BTReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 开始心电数据采集
			System.out.println("开始心电数据采集=============");
			BluetoothTalkService.newTask(new Task(MeasureActivity.this,
					Task.BT_START_ECG, null));
		}
	}

	/**
	 * @description 接口监听实现
	 */
	@Override
	public void onClick(View arg0) {
		BluetoothTalkService.newTask(new Task(MeasureActivity.this,
				Task.BT_STOP_ECG, null));
		this.finish();
	}

	/**
	 * @description 系统自调用
	 * @param task
	 */
	public void onTaskFinished(Task task) {

		switch (task.getTaskId()) {
		case Task.BT_START_ECG:
			if (task.mResult == null)
				return;
			if ((Boolean) task.mResult) {
				// 读取心电数据
				BluetoothTalkService.newTask(new Task(MeasureActivity.this,
						Task.BT_READ_ECG, new Object[] { delay }));
			}
			break;
		case Task.BT_READ_ECG:
			if (task.mResult == null) {
				return;
			}
			int ecgData = (Integer) task.mResult;
			double ecgDouble = ecgData;
			double ecg = (ecgDouble - 250) / (-80);
			Common.writeWordToFile(ecg + "");

			Log.d("tag", ecg + "-");
			Log.d("tag", (ecgDouble - 250) / (-80) + "-");

			if (ecgData != -1) {
				// TimerTask tasks = new TimerTask() {
				// public void run() {
				// // 如果数据采集完，则发送停止采集数据命令
				// BluetoothTalkService.newTask(new Task(
				// MeasureActivity.this, Task.BT_STOP_ECG, null));
				// }
				// };
				// Timer timers = new Timer();
				// // 延时20秒
				// timers.schedule(tasks, 20000);
			}
			break;
		}
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			this.finish();
		}
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
		return false;
	}

	/**
	 * @description 销毁surfaceview
	 */
	@Override
	protected void onDestroy() {
		sInputStream = null;
		if (mSurfaceView != null) {
			mSurfaceView.setDestroyed(true);
			mSurfaceView.setrun(false);
			mSurfaceView.destroyDrawingCache();
			mSurfaceView = null;
		}
		if (receiver != null)
			unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * @description home图标监听
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}
}
