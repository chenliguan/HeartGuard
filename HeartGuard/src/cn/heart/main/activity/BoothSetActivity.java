/**
 * @file SettingActivity.java
 * @description 蓝牙设置Activity类
 * @author Guan
 * @date 2015-6-19 下午3:35:40 
 * @version 1.0
 */
package cn.heart.main.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import cn.heart.bluetooth.BluetoothConnection;
import cn.heart.bluetooth.ClsUtils;
import cn.heart.main.controller.ManageSetActivity;
import cn.heart.settings.SysSettings;

import com.example.wechatsample.R;

/**
 * @description 蓝牙设置Activity类
 * @author Guan
 * @date 2015-6-19 下午3:35:40
 * @version 1.0
 */
public class BoothSetActivity extends Activity implements OnItemClickListener {

	private static final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

	// 扫描到的蓝牙设备信息列表
	private ArrayList<HashMap<String, String>> mBtDeviceList = new ArrayList<HashMap<String, String>>();

	// 绑定的蓝牙设备列表
	private ArrayList<String> mBondDeviceList;

	private BluetoothDevice mRemoteDevice;

	private String targetPinCode;

	private boolean isPaired = false;

	// 开始搜索按钮
	private Switch mSwitchBtn;

	// 蓝牙设备列表ListView
	private ListView btDeviceListView;

	// 蓝牙设备列表
	private SimpleAdapter mDeviceListAdapter;

	// 绑定蓝牙设备列表
	private ArrayAdapter<String> mBondDeviceListAdapter;

	private Context context = BoothSetActivity.this;

	// 当前系统支持的医疗设备类型
	private String[] mDeviceName = { "心电", "血压", "血糖", "体温", "扫描枪" };
	private BluetoothAdapter btAdapter;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting);

		// 注册Receiver来获取蓝牙设备相关的结果
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(ACTION_PAIRING_REQUEST);
		filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(btReceiver, filter);

		// 设置开始扫描按钮的事件
		mSwitchBtn = (Switch) findViewById(R.id.switch1);
		mSwitchBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					startScan();
					findViewById(R.id.btDeviceScanLayout).setVisibility(
							View.VISIBLE);
				} else {
					stopScan();
				}
			}
		});
		updateListView(context);
		initBondDeviceListView(context);

	}

	/**
	 * 开始扫描蓝牙设置 注册广播接收器并使能蓝牙设备
	 */
	public void startScan() {
		// 初始化本机蓝牙功能
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null) {
			if (btAdapter.getState() == BluetoothAdapter.STATE_OFF) // 开蓝牙
				btAdapter.enable();
			else
				btAdapter.startDiscovery(); // 直接开始扫描
		}
	}

	/**
	 * 停止扫描蓝牙设备，同时注销广播接收器
	 */
	public void stopScan() {
		// 初始化本机蓝牙功能
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter != null) {
			if (btAdapter.getState() == BluetoothAdapter.STATE_ON
					&& btAdapter.isDiscovering()) // 开蓝牙
				btAdapter.cancelDiscovery();
		}
	}

	/**
	 * 蓝牙广播接收对象
	 */
	private BroadcastReceiver btReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(">>>>>>", "Action = " + action);

			BluetoothDevice device = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			// 搜索设备时，取得设备的MAC地址
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				if (device == null)
					return;

				// 判断是否有重复扫描到的设备
				for (HashMap<String, String> map : mBtDeviceList) {
					if (device.getName() == null
							|| device.getName().length() <= 0) {
						if (map.get("name").equals(device.getAddress())) {
							return;
						}
					} else {
						if (map.get("name").equals(device.getName())) {
							return;
						}
					}
				}

				// 保存扫描到的蓝牙设备信息到mBtDeviceList中
				HashMap<String, String> map = new HashMap<String, String>();
				if (device.getName() == null || device.getName().length() <= 0)
					map.put("name", device.getAddress());
				else
					map.put("name", device.getName());

				map.put("addr", device.getAddress());
				map.put("state", BluetoothConnection.getBondStateString(device));

				mBtDeviceList.add(map);
				mDeviceListAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(btDeviceListView);
			} else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
				// 更新蓝牙设备状态信息
				for (HashMap<String, String> map : mBtDeviceList) {
					if (device.getName() == null
							|| device.getName().length() <= 0) {
						if (map.get("name").equals(device.getAddress())) {
							map.put("state", BluetoothConnection
									.getBondStateString(device));
						}
					} else {
						if (map.get("name").equals(device.getName())) {
							map.put("state", BluetoothConnection
									.getBondStateString(device));
						}
					}
					// 通知ListView数据发生了变化
					mDeviceListAdapter.notifyDataSetChanged();
					setListViewHeightBasedOnChildren(btDeviceListView);

					// 如果蓝牙设备状态从已配对变成未配对，则还要更新设备绑定列表信息
					if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
						isPaired = false;
						Toast.makeText(BoothSetActivity.this, "配对成功",
								Toast.LENGTH_LONG).show();
					}
				}

			} else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				// 如果蓝牙设备打开，则开始扫描
				if (btAdapter.getState() == BluetoothAdapter.STATE_ON) {
					if (!btAdapter.isDiscovering())
						btAdapter.startDiscovery();
				}
			} else if (action
					.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				// 如果扫描超时结束，则让扫描按钮关闭
				if (mSwitchBtn != null)
					mSwitchBtn.setChecked(false);
			} else if (action.equals(ACTION_PAIRING_REQUEST)) {
				Log.d(">>>>>>", "PAIRING_REQUEST: " + device.getAddress());
				if (mRemoteDevice.getAddress().equals(device.getAddress())) {
					if (isPaired)
						return;
					// 手机和蓝牙采集器配对
					isPaired = ClsUtils.setPin(device.getClass(), device,
							targetPinCode);
					// ClsUtils.createBond(device.getClass(), device);
				}
			}
		}
	};

	/**
	 * 从XML中读取得到保存的用户上次选择的设置及配对信息
	 * 
	 * @param view
	 *            ListView所在父控件对象
	 */
	private void initBondDeviceListView(Context context) {
		ListView bondDeviceListView = (ListView) this
				.findViewById(R.id.bondDeviceList);
		mBondDeviceList = new ArrayList<String>();
		// 添加ECG的连接设备名和地址及PIN码
		String name = SysSettings.getBondDeviceName(context,
				BluetoothConnection.TYPE_ECG);
		String addr = SysSettings.getBondDeviceAddr(context,
				BluetoothConnection.TYPE_ECG);
		String pin = SysSettings.getPINCode(context,
				BluetoothConnection.TYPE_ECG);
		String result = (name == null ? addr : name);
		mBondDeviceList.add(mDeviceName[BluetoothConnection.TYPE_ECG] + ": "
				+ (result == null ? "无" : "[" + result + "]")
				+ (result == null ? "" : " [" + pin + "]"));

		// 添加血压的连接设备名和地址及PIN码
		name = SysSettings.getBondDeviceName(context,
				BluetoothConnection.TYPE_BLOOD_PRESSURE);
		addr = SysSettings.getBondDeviceAddr(context,
				BluetoothConnection.TYPE_BLOOD_PRESSURE);
		pin = SysSettings.getPINCode(context,
				BluetoothConnection.TYPE_BLOOD_PRESSURE);
		result = (name == null ? addr : name);
		mBondDeviceList
				.add(mDeviceName[BluetoothConnection.TYPE_BLOOD_PRESSURE]
						+ ": " + (result == null ? "无" : "[" + result + "]")
						+ (result == null ? "" : " [" + pin + "]"));

		// 添加血糖的连接设备名和地址及PIN码
		mBondDeviceList
				.add(mDeviceName[BluetoothConnection.TYPE_GRUSS] + ": 无");

		// 添加体温的连接设备名和地址及PIN码
		name = SysSettings.getBondDeviceName(context,
				BluetoothConnection.TYPE_BODY_TEMP);
		addr = SysSettings.getBondDeviceAddr(context,
				BluetoothConnection.TYPE_BODY_TEMP);
		pin = SysSettings.getPINCode(context,
				BluetoothConnection.TYPE_BODY_TEMP);
		result = name == null ? addr : name;
		mBondDeviceList.add(mDeviceName[BluetoothConnection.TYPE_BODY_TEMP]
				+ ": " + (result == null ? "无" : "[" + result + "]")
				+ (result == null ? "" : " [" + pin + "]"));

		// 添加扫描枪的连接设备名和地址及PIN码
		name = SysSettings.getBondDeviceName(context,
				BluetoothConnection.TYPE_CODE_SCANEER);
		addr = SysSettings.getBondDeviceAddr(context,
				BluetoothConnection.TYPE_CODE_SCANEER);
		pin = SysSettings.getPINCode(context,
				BluetoothConnection.TYPE_CODE_SCANEER);
		result = name == null ? addr : name;
		mBondDeviceList.add(mDeviceName[BluetoothConnection.TYPE_CODE_SCANEER]
				+ ": " + (result == null ? "无" : "[" + result + "]")
				+ (result == null ? "" : " [" + pin + "]"));

		mBondDeviceListAdapter = new ArrayAdapter<String>(context,
				R.layout.bt_bonded_device_list_item, mBondDeviceList);
		bondDeviceListView.setAdapter(mBondDeviceListAdapter);

		// 为ListView添加 点击事件
		bondDeviceListView.setOnItemClickListener(this);

		// 设置ListVIew的高度
		setListViewHeightBasedOnChildren(bondDeviceListView);
	}

	/**
	 * 将扫描到的蓝牙设备信息显示到ListView上
	 * 
	 * @param view
	 *            蓝牙设置Frament对象
	 */
	private void updateListView(Context context) {
		btDeviceListView = (ListView) this.findViewById(R.id.btDeviceList);
		mDeviceListAdapter = new SimpleAdapter(context, mBtDeviceList,
				R.layout.bt_device_list_item_layout, new String[] { "name",
						"state" }, new int[] { R.id.bt_name, R.id.bt_state });

		/*
		 * // 设置取消配对点击事件 btDeviceListView.setOnItemClickListener(new
		 * OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { // TODO Auto-generated method stub ;
		 * BluetoothDevice remoteDevice =
		 * btAdapter.getRemoteDevice(mBtDeviceList.get(arg2).get("addr"));
		 * if(remoteDevice != null){
		 * ClsUtils.removeBond(remoteDevice.getClass(), remoteDevice); } } });
		 */
		btDeviceListView.setAdapter(mDeviceListAdapter);
	}

	/**
	 * 根据ListView设置其高度，用于和ScrollView联合使用时，ListView中项目不能正常显示的情况
	 * 
	 * @param listView
	 */
	private void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		listView.setLayoutParams(params);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,
			final int typeIndex, long arg3) {
		// TODO Auto-generated method stub
		if (mBtDeviceList.size() <= 0) {
			Toast.makeText(context, "请扫描周围蓝牙设备并确认蓝牙设备已经打开", Toast.LENGTH_LONG)
					.show();
			return;
		}
		// 当用户点击选择蓝牙设备项时，弹出可用蓝牙设备对话框
		final Dialog selectDeviceDialog = new Dialog(context,
				R.style.dialogStyle);
		selectDeviceDialog.setContentView(R.layout.bt_device_dialog_layout);
		// 设置对话框的标题部分
		((TextView) selectDeviceDialog.findViewById(R.id.dialogTextView))
				.setText("选择" + mDeviceName[typeIndex] + "设备");

		ListView dialogListView = (ListView) selectDeviceDialog
				.findViewById(R.id.dialogListView);
		// 设置对话框中的蓝牙设备列表
		dialogListView.setAdapter(new SimpleAdapter(context, mBtDeviceList,
				R.layout.bt_device_list_item_layout, new String[] { "name",
						"state" }, new int[] { R.id.bt_name, R.id.bt_state }));
		// 蓝牙设备列表点击事件
		dialogListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int btDeviceIndex, long arg3) {
				// 将蓝牙设备列表对话框关闭
				selectDeviceDialog.dismiss();
				// 弹出设置蓝牙设备PIN码的对话框
				final Dialog setPinDialog = new Dialog(context,
						R.style.dialogStyle);
				setPinDialog
						.setContentView(R.layout.bt_device_dialog_setpin_layout);
				((TextView) setPinDialog.findViewById(R.id.pinCodeTextView))
						.setText("设置" + mDeviceName[typeIndex] + "设备的配对码");
				Button enterBtn = (Button) setPinDialog
						.findViewById(R.id.pinCodeEnterBtn);
				Button cancelBtn = (Button) setPinDialog
						.findViewById(R.id.pinCodeCancelBtn);
				final EditText pinEditText = (EditText) setPinDialog
						.findViewById(R.id.pinCodeEditText);
				// 用户点击取消按钮
				cancelBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						setPinDialog.dismiss();
					}
				});
				// 用户点击确定按钮
				enterBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String name = mBtDeviceList.get(btDeviceIndex).get(
								"name");
						String addr = mBtDeviceList.get(btDeviceIndex).get(
								"addr");
						// 先将用户设置的蓝牙设备PIN码保存到XML文件中
						SysSettings.setBondDeviceName(context, typeIndex, name);
						SysSettings.setBondDeviceAddr(context, typeIndex, addr);
						SysSettings.setPINCode(context, typeIndex, pinEditText
								.getText().toString());
						targetPinCode = pinEditText.getText().toString();
						mBondDeviceList.set(typeIndex, mDeviceName[typeIndex]
								+ ": [" + (name == null ? addr : name) + "]"
								+ " [" + targetPinCode + "]");
						// 将新选择的蓝牙设备及PIN码更新到选择设备列表
						mBondDeviceListAdapter.notifyDataSetChanged();
						mRemoteDevice = btAdapter.getRemoteDevice(addr);
						if (mRemoteDevice != null) {
							ClsUtils.createBond(mRemoteDevice.getClass(),
									mRemoteDevice);
						}
						setPinDialog.dismiss();
					}
				});
				setPinDialog.show();
			}
		});
		selectDeviceDialog.show();
	}
	
	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startActivity(new Intent(BoothSetActivity.this,
				ManageSetActivity.class));
		BoothSetActivity.this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
		return false;
	}

}
