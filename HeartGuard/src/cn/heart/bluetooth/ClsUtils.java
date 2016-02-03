/**
 * @description 蓝牙相关反射工具类
 * @author Michael
 * @Email：tangpan09@gmail.com
 * @date 2015-6-19 下午3:48:41 
 * @version 1.0
 */
package cn.heart.bluetooth;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * @description 蓝牙相关反射工具类
 * @author Michael
 * @date 2015-6-19 下午3:48:41
 * @version 1.0
 */
public class ClsUtils {

	/**
	 * @description与设备配对 参考源码：platform/packages/apps/Settings.git
	 *                   /Settings/src/com
	 *                   /android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean createBond(Class<? extends BluetoothDevice> btClass,
			BluetoothDevice btDevice) {
		try {
			Method createBondMethod = btClass.getMethod("createBond");
			createBondMethod.setAccessible(true);
			Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
			return returnValue.booleanValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Throwable t = e.getCause();
			t.printStackTrace();
			return false;
		}
	}

	/**
	 * @description 与设备解除配对 参考源码：platform/packages/apps/Settings.git
	 *              /Settings/src
	 *              /com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean removeBond(Class<?> btClass, BluetoothDevice btDevice) {
		try {
			Method removeBondMethod = btClass.getMethod("removeBond");
			removeBondMethod.setAccessible(true);
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
			return returnValue.booleanValue();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Throwable t = e.getCause();
			t.printStackTrace();
			return false;
		}
	}

	/**
	 * @description 取消自动配对话框
	 * @param btClass
	 * @param btDevice
	 * @param pin
	 * @return
	 * @throws Exception
	 */
	static public boolean setPin(Class<? extends BluetoothDevice> btClass,
			BluetoothDevice btDevice, String pinCode) {
		if (btDevice == null || pinCode == null)
			return false;
		try {
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
					new Class[] { byte[].class });
			removeBondMethod.setAccessible(true);
			Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
					new Object[] { pinCode.getBytes("UTF-8") });
			Log.e("setPin returnValue", "" + returnValue);
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	// 取消用户输入
	static public boolean cancelPairingUserInput(Class<?> btClass,
			BluetoothDevice device) throws Exception {
		Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
		// cancelBondProcess()

		createBondMethod.setAccessible(true);
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		Log.e("cancelPairingUserInput returnValue", "" + returnValue);
		return returnValue.booleanValue();
	}

	// 取消配对
	static public boolean cancelBondProcess(Class<?> btClass,
			BluetoothDevice device) throws Exception {
		Method createBondMethod = btClass.getMethod("cancelBondProcess");
		createBondMethod.setAccessible(true);
		Boolean returnValue = (Boolean) createBondMethod.invoke(device);
		Log.e("cancelBondProcess returnValue", "" + returnValue);
		return returnValue.booleanValue();
	}

	static public boolean pair(String strAddr, String strPsw) {
		boolean result = false;
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		bluetoothAdapter.cancelDiscovery();

		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
		}

		if (!BluetoothAdapter.checkBluetoothAddress(strAddr)) { // 检查蓝牙地址是否有效

			Log.d("mylog", "devAdd un effient!");
		}

		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

		if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				Log.d("mylog", "NOT BOND_BONDED");
				ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
				ClsUtils.createBond(device.getClass(), device);
				// remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block

				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			} //

		} else {
			Log.d("mylog", "HAS BOND_BONDED");
			try {
				ClsUtils.createBond(device.getClass(), device);
				ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
				ClsUtils.createBond(device.getClass(), device);
				// remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
				result = true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("mylog", "setPiN failed!");
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * @description printAllInform
	 * @param clsShow
	 */
	static public void printAllInform(Class<?> clsShow) {
		try {
			// 取得所有方法
			Method[] hideMethod = clsShow.getMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {
				Log.e("method name", hideMethod[i].getName());
			}
			// 取得所有常量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++) {
				Log.e("Field name", allFields[i].getName());
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
