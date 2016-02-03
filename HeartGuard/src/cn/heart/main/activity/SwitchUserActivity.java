/**
 * @file SwitchUserActivity.java
 * @description 切换用户Activity类
 * @author Guan
 * @date 2015-6-9 下午2:43:49 
 * @version 1.0
 */
package cn.heart.main.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.heart.app.App;
import cn.heart.config.Common;
import cn.heart.main.controller.ManageSetActivity;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.service.adapter.SwitchUsertAdapter;

import com.example.wechatsample.R;

/**
 * @description 切换用户Activity类
 * @author Guan
 * @date 2015-6-9 下午2:43:49
 * @version 1.0
 */
public class SwitchUserActivity extends Activity {

	private ActionBar mActionBar;
	private static ListView sListFamily;
	private static int mCurrentPosition;
	private static String sUserString, sNickString;
	private static SharedPreferences.Editor sEditor;
	private static SharedPreferences mPreferences;
	private static List<HashMap<String, Object>> sObject;
	public static Context sContext;
	public static SwitchUserActivity sActivity;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_family_switch);
		// ActionBar
		Action();
		// 初始化
		Init();
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
	 * @description 初始化
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private void Init() {

		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		sEditor = mPreferences.edit();
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserString = mPreferences.getString("USER_NAME", "");
			sNickString = mPreferences.getString("NICK", "");
		}
		sObject = new ArrayList<HashMap<String, Object>>();
		// 获取服务器家人信息
		VolleyFamilyhttp.VolleyAllfamily(sContext, "SwitchUserActivity",
				sUserString);
		sActivity = SwitchUserActivity.this;
		sContext = SwitchUserActivity.this;
		sListFamily = (ListView) findViewById(R.id.lv_family_switch);
	}

	/**
	 * @description handler
	 */
	public static Handler switchHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {

			sObject = (List<HashMap<String, Object>>) msg.obj;
			final SwitchUsertAdapter adapter = new SwitchUsertAdapter(
					sActivity, sObject, mPreferences, sUserString);
			// 循环获取当前项
			for (int location = 0; location < sObject.size(); location++) {
				if (sNickString.equals(sObject.get(location).get("nick"))) {
					adapter.setInt(location);
				}
			}
			sListFamily.setAdapter(adapter);

			// 触发监听
			sListFamily.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					mCurrentPosition = position;
					adapter.setTag(mCurrentPosition);
				}
			});

		}
	};

	/**
	 * @description 下拉菜单 onCreateOptionsMenu()方法中去加载main.xml文件(non-Javadoc)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.onedetail_tab, menu);
		MenuItem familyitem = menu.findItem(R.id.check).setChecked(true);
		familyitem.setOnMenuItemClickListener(new OnMenuItemClickListener());
		return true;
	}

	/**
	 * @description 菜单项触发监听
	 * @author Guan
	 * @date 2015-6-9 下午6:18:46
	 * @version 1.0
	 */
	public class OnMenuItemClickListener implements
			android.view.MenuItem.OnMenuItemClickListener {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			/** 保存本地更新后信息 */
			sEditor.putString("NICK", (String) sObject.get(mCurrentPosition)
					.get("nick"));
			sEditor.putString("GENDER", (String) sObject.get(mCurrentPosition)
					.get("gender"));
			sEditor.putString("BIRTHDAY", (String) sObject
					.get(mCurrentPosition).get("birthday"));
			sEditor.putString("HIGH", (String) sObject.get(mCurrentPosition)
					.get("high"));
			sEditor.putString("WEIGHT", (String) sObject.get(mCurrentPosition)
					.get("weight"));
			sEditor.putString("FAMILYGROUP",
					(String) sObject.get(mCurrentPosition).get("familygroup"));
			sEditor.commit();

			StartActivity();
			Common.Toast(sContext, "切换用户成功");
			return false;
		}
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		StartActivity();
		return false;
	}

	/**
	 * @description home图标监听
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			StartActivity();
			break;
		}
		return false;
	}

	/**
	 * @description Activity跳转
	 */
	private void StartActivity() {
		startActivity(new Intent(SwitchUserActivity.this,
				ManageSetActivity.class));
		this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("familylist");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("familylist");
	}
}
