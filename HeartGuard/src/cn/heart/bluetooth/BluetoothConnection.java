/**
 * @file BluetoothConnection.java
 * @description 蓝牙连接类
 * @author Michael
 * @date 2015-6-19 下午3:42:30 
 * @version 1.0
 */
package cn.heart.bluetooth;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import cn.heart.settings.SysSettings;

import com.example.wechatsample.R;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @description 蓝牙连接类
 * @author Michael
 * @date 2015-6-19 下午3:42:30
 * @version 1.0
 */
public class BluetoothConnection {
	private static final String TAG = "BluetoothConnection";

	/** 心电 */
	public static final int TYPE_ECG = 0;
	/** 血压 */
	public static final int TYPE_BLOOD_PRESSURE = 1;
	/** 血糖 */
	public static final int TYPE_GRUSS = 2;
	/** 体温 */
	public static final int TYPE_BODY_TEMP = 3;
	public static final int TYPE_CODE_SCANEER = 4;
	public static final int TYPE_NFC = 5;

	public static final int TYPE_LAST = TYPE_NFC;

	public static final int ERROR = -1;
	// =========================================================
	private static BluetoothAdapter btAdapter;
	private int mType = -1;

	private ConnThread mConnThread;
	private static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	public static final String KEY_DEVICE = "com.farsight.bluetooth.BlueToothDevice";

	public static final String ACTION_BLUETOOTH_CONNECTED = "com.farsight.bluetooth.BlueToothDevice.action.CONNECTED";

	private BluetoothSocket mSocket = null;

	private InputStream mIn;
	private OutputStream mOut;
	private boolean isReceiverRegisted = false;;
	private Context mContext;
	private BluetoothReceiver btReceiver;
	// =========================================================
	private static BluetoothConnection mInstance;

	public static BluetoothConnection getConnection(Context context) {
		if (mInstance == null) {
			mInstance = new BluetoothConnection(context);
		}
		return mInstance;
	}

	private BluetoothConnection(Context context) {
		mContext = context;
	}

	public InputStream getInputStream() {
		return mIn;
	}

	public OutputStream getOutputStream() {
		return mOut;
	}

	public BluetoothSocket getSocket() {
		return mSocket;
	}

	/**
	 * @description 初始化蓝牙服务，根据不同的type来连接不同的蓝牙设备
	 * @param type
	 * @return
	 */
	public boolean initBluetoothService(Context ctx, int type) {
		LOGD("initBlueToothService: type = " + type);

		String addr = SysSettings.getBondDeviceAddr(mContext, type);
		if ((addr == null) || !BluetoothAdapter.checkBluetoothAddress(addr)) {
			if (type == BluetoothConnection.TYPE_CODE_SCANEER) {
				Toast tst = Toast.makeText(mContext,
						R.string.bt_device_not_set_error_msg2,
						Toast.LENGTH_LONG);
				tst.setGravity(Gravity.CENTER, 0, -60);
				tst.show();
			} else {
				// CustomUI.createAlertDialog(ctx,
				// R.string.bt_device_not_set_error_title,
				// R.string.bt_device_not_set_error_msg2);
			}
			return false;
		}

		if (!isReceiverRegisted) {
			btReceiver = new BluetoothReceiver();
			// 注册Receiver来获取蓝牙设备相关的结果
			IntentFilter intent = new IntentFilter();
			intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			intent.addAction(ACTION_PAIRING_REQUEST);
			intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			mContext.registerReceiver(btReceiver, intent);
			LOGD("Bluetooth registerReceiver");
			isReceiverRegisted = true;
		}

		// 初始化本机蓝牙功能
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null) {
			if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) { // 开蓝牙
				LOGD("Bluetooth enable");
				btAdapter.enable();
				return true;
			}

			this.mType = type;

			BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(addr);
			if (checkBondedState(remoteDevice)) {
				// 开始连接蓝牙服务
				LOGD("ReadyConnectThread start");
				new ReadyConnectThread(remoteDevice, mType).start();
			}
		} else {
			return false;
		}

		return true;
	}

	// 连接Socket前先通过该线程判断是否已经存在蓝牙连接
	private class ReadyConnectThread extends Thread {
		private BluetoothDevice mRemoteDev;
		private int mType;

		public ReadyConnectThread(BluetoothDevice remoteDeivce, int type) {
			mRemoteDev = remoteDeivce;
			mType = type;
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			if (mRemoteDev == null) {
				LOGE("mRemoteDev == null");
				return;
			}
			LOGD("start connect to remote device..." + mRemoteDev.getName());

			if (mSocket != null && mSocket.isConnected()) {
				LOGD("socket != null");
				try {
					mSocket.close();
					mSocket = null;
				} catch (Exception e) {
				}
			}

			if (btAdapter.isDiscovering())
				btAdapter.cancelDiscovery();

			// 如果当前正在连接蓝牙设备
			if (mConnThread != null) {
				synchronized (mConnThread) {
					if (mConnThread.isAlive()) {
						// 取消连接线程
						mConnThread.cancel();
						// 等待连接线程结束
						try {
							System.out.println("ready to wait...");
							mConnThread.wait();
							System.out.println("awake from wait...");
						} catch (InterruptedException e) {
						}
					}
				}
			}
			// 创建连接设备线程
			mConnThread = new ConnThread(mRemoteDev, mType);
			mConnThread.start();
			Log.d(TAG, "ConnThread.start()");
		}

	}

	/**
	 * 连接线程
	 */
	private class ConnThread extends Thread {
		private int type;
		private BluetoothDevice mRemoteDev;
		private boolean isStart = true;

		public void cancel() {
			isStart = false;
			try {
				if (mSocket != null)
					mSocket.close();
			} catch (Exception e) {
			}
		}

		public ConnThread(BluetoothDevice dev, int type) {
			mRemoteDev = dev;
			this.type = type;
		}

		public void run() {
			// 尝试连接12次
			int i;
			for (i = 1; i <= 12 && isStart; i++) {
				switch (type) {
				case TYPE_ECG:
					Log.d(TAG, "connect to ECG , try: " + i);
					if (connect(mRemoteDev)) {
						// 连接成功，结束连接线程
						synchronized (this) {
							isStart = false;
						}
					}
					break;
				case TYPE_BODY_TEMP:
					Log.d(TAG, "connect to Temprature , try: " + i);
					if (spp_connect(mRemoteDev)) {
						// 连接成功，结束连接线程
						synchronized (this) {
							isStart = false;
						}
					}
					break;
				case TYPE_BLOOD_PRESSURE:
					Log.d(TAG, "connect to BLOOD PRESSURE , try: " + i);
					if (spp_connect(mRemoteDev)) {
						// 连接成功，结束连接线程
						synchronized (this) {
							isStart = false;
						}
					}
					break;
				case TYPE_CODE_SCANEER:
					Log.d(TAG, "connect to Bar code device , try: " + i);
					if (spp_connect(mRemoteDev)) {
						// 连接成功，结束连接线程
						synchronized (this) {
							isStart = false;
						}
					}
					break;
				}
				synchronized (this) {
					this.notifyAll();
				}
				delay();
			}
			if (i >= 13 && isStart) {
				LOGE("Timeout to connect remote device " + mRemoteDev.getName());
			}
			unRegisterReceiver();
		}

		private void delay() {
			if (!isStart)
				return;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	@SuppressLint("NewApi")
	public void closeConnection() {
		// 正在连接时，结束连接并关闭Socket
		if (mConnThread != null && mConnThread.isAlive()) {
			mConnThread.cancel();
		} else if (mSocket != null && mSocket.isConnected()) {
			// 已经连接成功，只关闭socket
			try {
				mSocket.close();
			} catch (Exception e) {
			}
		}
		// 注销广播
		unRegisterReceiver();
	}

	public void unRegisterReceiver() {
		if (btReceiver != null && isReceiverRegisted) {
			LOGD("Bluetooth unregisterReceiver");
			try {
				mContext.unregisterReceiver(btReceiver);
			} catch (Exception e) {
			}
			btReceiver = null;
			isReceiverRegisted = false;
		}
	}

	/**
	 * @description 连接蓝牙设备，得到输入输出流
	 * @param remoteDev
	 * @return 正常连接返回true, 错误返回false
	 */
	private synchronized boolean connect(BluetoothDevice remoteDev) {
		LOGD("connecting..." + remoteDev.getName());
		if (btAdapter == null || remoteDev == null)
			return false;
		// 取消被发现
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		Method m;
		try {
			m = remoteDev.getClass().getMethod("createRfcommSocket",
					new Class[] { int.class });

			mSocket = (BluetoothSocket) m.invoke(remoteDev, Integer.valueOf(1));

			// 连接服务端
			mSocket.connect();
			mIn = mSocket.getInputStream();
			mOut = mSocket.getOutputStream();
			Intent i = new Intent(ACTION_BLUETOOTH_CONNECTED);
			i.putExtra("TYPE", mType);
			mContext.sendBroadcast(i);
			LOGD("Send connected Broadcast:" + remoteDev.getName());

		} catch (Exception e) {
			LOGE("socket.connect()" + remoteDev.getName());
			return false;
		}
		return true;
	}

	// UUID可以看做一个端口号
	private synchronized boolean spp_connect(BluetoothDevice remoteDev) {
		LOGD("spp connecting...");
		if (btAdapter == null || remoteDev == null)
			return false;

		// 取消被发现
		if (btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();

		final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		UUID uuid = UUID.fromString(SPP_UUID);

		try {
			mSocket = remoteDev.createRfcommSocketToServiceRecord(uuid);
			mSocket.connect();
		} catch (Exception e1) {
			LOGE("failed to socket.connect() " + remoteDev.getName());
			return false;
		}
		try {
			mIn = mSocket.getInputStream();
			mOut = mSocket.getOutputStream();
			Intent i = new Intent(ACTION_BLUETOOTH_CONNECTED);
			i.putExtra("TYPE", mType);
			mContext.sendBroadcast(i);
		} catch (Exception e1) {
			try {
				mSocket.close();
			} catch (Exception e2) {
				LOGE("unable to close() socket during connection failure");
			}
			LOGE("socket.getStream()");
			return false;
		}

		return true;
	}

	private boolean checkBondedState(BluetoothDevice remoteDevice) {
		if (remoteDevice != null
				&& remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
			ClsUtils.createBond(remoteDevice.getClass(), remoteDevice);
			return false;
		}
		return true;
	}

	/**
	 * 蓝牙广播接收对象
	 */
	private class BluetoothReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {

			LOGD("btReceiver::onReceive");
			String action = intent.getAction();

			LOGD("Action = " + action);

			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			if (action.equals(ACTION_PAIRING_REQUEST)) {
				LOGD("PAIRING_REQUEST: " + device.getAddress());

				if (btAdapter.isDiscovering())
					btAdapter.cancelDiscovery();

				// 手机和蓝牙采集器配对
				ClsUtils.setPin(device.getClass(), device,
						SysSettings.getPINCode(context, mType));
			} else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
				LOGD("ACTION_BOND_STATE_CHANGED: " + device.getAddress());
				if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
					String addr = SysSettings.getBondDeviceAddr(context, mType);
					// 连接已经建立
					if (mSocket != null
							&& mSocket.getRemoteDevice().getAddress()
									.equals(addr)) {
						return;
					} else {
						// 开始连接蓝牙服务
						LOGD("device BOND_BONDED then startConnect()");
						new ReadyConnectThread(device, mType).start();
					}
				}
			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				LOGD("ACTION_STATE_CHANGED:" + btAdapter.getState());
				if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
					String addr = SysSettings.getBondDeviceAddr(context, mType);
					if ((addr == null)
							|| !BluetoothAdapter.checkBluetoothAddress(addr)) {
						Toast.makeText(context, "没有设置蓝牙设备", Toast.LENGTH_LONG)
								.show();
						return;
					}

					BluetoothDevice remoteDevice = btAdapter
							.getRemoteDevice(addr);
					if (checkBondedState(remoteDevice)) {
						// 开始连接蓝牙服务
						LOGD("STATE_ON then startConnect()");
						new ReadyConnectThread(remoteDevice, mType).start();
					}
				}
			}
		}
	};

	private void LOGD(String msg) {
		Log.d(TAG, msg);
	}

	private void LOGE(String msg) {
		Log.e(TAG, msg);
	}

	public static String getBondStateString(BluetoothDevice device) {
		switch (device.getBondState()) {
		case BluetoothDevice.BOND_BONDED:
			return "已配对";
		case BluetoothDevice.BOND_BONDING:
			return "正在配对...";
		case BluetoothDevice.BOND_NONE:
			return "未配对";
		}
		return null;
	}
}
