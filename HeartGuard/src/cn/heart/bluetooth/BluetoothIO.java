/**
 * @file BluetoothIO.java
 * @description BluetoothIO类
 * @author Michael
 * @date 2015-6-19 下午3:44:34 
 * @version 1.0
 */
package cn.heart.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

/**
 * @description BluetoothIO类
 * @author Michael
 * @date 2015-6-19 下午3:44:34
 * @version 1.0
 */
public class BluetoothIO {
	private final static String TAG = "BluetoothIO";
	public static final int ERROR = -1;
	public static final int OK = 0;

	/**
	 * @description 读取ECG数据
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static int readECGData(InputStream in) throws IOException {
		if (in == null) {
			return ERROR;
		}

		byte[] buffer = new byte[EcgProtocol.ECG_DATA_LEN];
		int size = 0;
		while (size < EcgProtocol.ECG_DATA_LEN) {
			size += in.read(buffer, size, EcgProtocol.ECG_DATA_LEN - size);
		}

		// 协议判断
		return EcgProtocol.processEcgData(buffer);
	}

	public static boolean isConnected(Socket socket) {
		if (socket == null)
			return false;
		return socket.isConnected();
	}

	public static boolean startECG(OutputStream out) {
		LOGD("startECG");
		byte[] buf = new byte[] { EcgProtocol.DATA_HEAD,
				EcgProtocol.DATA_CMD_START_ECG, 0 };
		buf[2] = EcgProtocol.checkSum(buf, 2);
		return sendCmd(buf, out);
	}

	public static boolean stopECG(OutputStream out) {
		LOGD("stopECG");
		byte[] buf = new byte[] { EcgProtocol.DATA_HEAD,
				EcgProtocol.DATA_CMD_STOP_ECG, 0 };
		buf[2] = EcgProtocol.checkSum(buf, 2);
		return sendCmd(buf, out);
	}

	public static String readBarCode(InputStream in) throws IOException {
		if (in == null) {
			return null;
		}
		int MAX_LEN = 32;
		byte[] buffer = new byte[MAX_LEN];
		in.read(buffer);
		LOGE("Bar code:" + new String(buffer));
		return new String(buffer);
	}

	/**
	 * @description 发�?蓝牙控制命令
	 */
	public static boolean sendCmd(byte[] cmd, OutputStream out) {
		// TODO Auto-generated method stub
		Log.d("IBlueToothServiceStub", "sendECGCmd");
		if (out == null)
			return false;
		if (cmd == null || cmd.length <= 0)
			return false;
		try {
			out.write(cmd);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static void LOGD(String msg) {
		Log.d(TAG, msg);
	}

	private static void LOGE(String msg) {
		Log.e(TAG, msg);
	}

}
