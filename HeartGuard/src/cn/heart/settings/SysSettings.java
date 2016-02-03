package cn.heart.settings;

import cn.heart.bluetooth.BluetoothConnection;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 系统设置数据保存接口类 ================
 * 
 * @author tangpan09@gmail.com http://blog.csdn.net/mr_raptor
 * 
 */
public class SysSettings {
	public static final String KEY_WEB_SERVICE_PATH = "WebService_Path_setting";
	public static final String KEY_ECG_DURATION = "key_ECG_setting";
	public static final String KEY_LAST_LOGIN_USER = "key_login_user";

	private static SharedPreferences sp;

	private static SharedPreferences getInstance(Context context) {
		if (sp == null) {
			sp = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return sp;
	}

	/**
	 * 设置服务器地址
	 * 
	 * @param context
	 * @param webURL
	 *            服务器地址
	 * @return 设置是否成功
	 */
	public static boolean setWebServicePath(Context context, String webURL) {
		return getInstance(context).edit()
				.putString(KEY_WEB_SERVICE_PATH, webURL).commit();
	}

	/**
	 * 获得系统设置中设置的ECG心电模块监听时长
	 * 
	 * @param context
	 * @return 返回ECG采集秒数
	 */
	public static int getEcgDuration(Context context) {
		return Integer.valueOf(getInstance(context).getString(KEY_ECG_DURATION,
				"12"));
		// return 20;
	}

	/**
	 * 设置ECG采集时间
	 * 
	 * @param context
	 * @param duration
	 *            采集时间
	 * @return 是否设置成功
	 */
	public static boolean setEcgDuration(Context context, String duration) {
		System.out.println("是否设置成功："
				+ getInstance(context).edit()
						.putString(KEY_ECG_DURATION, duration).commit());
		return getInstance(context).edit()
				.putString(KEY_ECG_DURATION, duration).commit();
	}

	/**
	 * 获得蓝牙密码
	 * 
	 * @param context
	 * @param type
	 *            蓝牙设备类型
	 * @return 返回配对PIN码
	 */
	public static String getPINCode(Context context, int type) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).getString("key_blood_press_pin", null);
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).getString("key_body_temperature_pin",
					null);
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).getString("key_ecg_pin", null);
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).getString("key_code_scanner_pin", null);
		}
		return null;
	}

	/**
	 * 设置指定医疗设备类型的PIN码
	 * 
	 * @param context
	 * @param type
	 *            设备类型
	 * @param pin
	 *            PIN码
	 * @return 是否设置成功
	 */
	public static boolean setPINCode(Context context, int type, String pin) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).edit()
					.putString("key_blood_press_pin", pin).commit();
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).edit()
					.putString("key_body_temperature_pin", pin).commit();
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).edit().putString("key_ecg_pin", pin)
					.commit();
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).edit()
					.putString("key_code_scanner_pin", pin).commit();
		}
		return false;
	}

	/**
	 * 获得指定医疗设备类型绑定的蓝牙设备名
	 * 
	 * @param context
	 * @param type
	 *            设备类型
	 * @return 蓝牙设备名
	 */
	public static String getBondDeviceName(Context context, int type) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).getString(
					"key_blood_press_bond_device_name", null);
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).getString(
					"key_body_temperature_bond_device_name", null);
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).getString("key_ecg_bond_device_name",
					null);
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).getString(
					"key_code_scanner_bond_device_name", null);
		}
		return null;
	}

	/**
	 * 设置指定类型医疗设备绑定的蓝牙设备名
	 * 
	 * @param context
	 * @param type
	 *            疗设备类型医
	 * @param name
	 *            蓝牙设备名
	 * @return 设置是否成功
	 */
	public static boolean setBondDeviceName(Context context, int type,
			String name) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).edit()
					.putString("key_blood_press_bond_device_name", name)
					.commit();
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).edit()
					.putString("key_body_temperature_bond_device_name", name)
					.commit();
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).edit()
					.putString("key_ecg_bond_device_name", name).commit();
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).edit()
					.putString("key_code_scanner_pin", name).commit();
		}
		return false;
	}

	/**
	 * 
	 * 判断是否配置了蓝牙设备
	 * 
	 * @param context
	 *            上下文
	 * @return 蓝牙设备都配置时，返回true, 有一个蓝牙设备未配置，返回false
	 */
	public static boolean checkBluetoothDevice(Context context) {
		for (int i = 0; i < BluetoothConnection.TYPE_LAST; i++) {
			if (getBondDeviceName(context, i) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获得指定医疗设备类型绑定的蓝牙设备地址
	 * 
	 * @param context
	 * @param type
	 *            设备类型
	 * @return 蓝牙设备地址
	 */
	public static String getBondDeviceAddr(Context context, int type) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).getString(
					"key_blood_press_bond_device_addr", null);
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).getString(
					"key_body_temperature_bond_device_addr", null);
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).getString("key_ecg_bond_device_addr",
					null);
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).getString(
					"key_code_scanner_bond_device_addr", null);
		}
		return null;
	}

	/**
	 * 设置指定医疗设备类型的绑定蓝牙设备地址
	 * 
	 * @param context
	 * @param type
	 * @param addr
	 * @return
	 */
	public static boolean setBondDeviceAddr(Context context, int type,
			String addr) {
		switch (type) {
		case BluetoothConnection.TYPE_BLOOD_PRESSURE:
			return getInstance(context).edit()
					.putString("key_blood_press_bond_device_addr", addr)
					.commit();
		case BluetoothConnection.TYPE_BODY_TEMP:
			return getInstance(context).edit()
					.putString("key_body_temperature_bond_device_addr", addr)
					.commit();
		case BluetoothConnection.TYPE_ECG:
			return getInstance(context).edit()
					.putString("key_ecg_bond_device_addr", addr).commit();
		case BluetoothConnection.TYPE_CODE_SCANEER:
			return getInstance(context).edit()
					.putString("key_code_scanner_bond_device_addr", addr)
					.commit();
		}
		return false;
	}

	// /**
	// * 设置登录医师信息
	// * @param context JsonCheckDoctor doctor
	// */
	// public static void setLoginUser(Context context, Doctor doctor){
	// getInstance(context).edit().putString("doctor_id",
	// doctor.doctor_id).commit();
	// getInstance(context).edit().putString("doctor_name",
	// doctor.doctor_name).commit();
	// getInstance(context).edit().putString("doctor_tname",
	// doctor.doctor_tname).commit();
	// getInstance(context).edit().putString("doctor_level",
	// doctor.doctor_level).commit();
	// getInstance(context).edit().putString("doctor_add",
	// doctor.doctor_add).commit();
	// getInstance(context).edit().putString("doctor_dob",
	// doctor.doctor_dob).commit();
	// getInstance(context).edit().putString("doctor_gender",
	// doctor.doctor_gender).commit();
	// getInstance(context).edit().putString("doctor_tel",
	// doctor.doctor_tel).commit();
	// getInstance(context).edit().putString("card_data",
	// doctor.card_data).commit();
	//
	// }
	//
	// /**
	// * 获得最近登录用户名
	// * @param context
	// * @return 用户名封装对象
	// */
	// public static Doctor getLoginUser(Context context){
	// Doctor doctor = new Doctor();
	// doctor.doctor_id = getInstance(context).getString("doctor_id", null);
	// doctor.card_data = getInstance(context).getString("card_data", null);
	// doctor.doctor_add = getInstance(context).getString("doctor_add", null);
	// doctor.doctor_dob = getInstance(context).getString("doctor_dob", null);
	// doctor.doctor_gender = getInstance(context).getString("doctor_gender",
	// null);
	// doctor.doctor_level = getInstance(context).getString("doctor_level",
	// null);
	// doctor.doctor_name = getInstance(context).getString("doctor_name", null);
	// //doctor.doctor_permisson = jsonDoctor.getString("doctor_permisson");
	// doctor.doctor_tel = getInstance(context).getString("doctor_tel", null);
	// return doctor;
	// }
}
