/**
 * @file UserListActivity.java
 * @description 家人成员类
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
import android.view.MenuItem;
import android.widget.ListView;
import cn.heart.app.App;
import cn.heart.main.controller.ManageSetActivity;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.service.adapter.FamilyListAdapter;

import com.example.wechatsample.R;

/**
 * @description 家人成员Activity类
 * @author Guan
 * @date 2015-6-9 下午2:43:49
 * @version 1.0
 */
public class FamilyListActivity extends Activity {

	
	private ActionBar mActionBar;
	private SharedPreferences mPreferences;
	private static ListView sListFamily;
	private static String sUserstring;
	private static List<HashMap<String, Object>> sObject;
	public static Context sContext;
	public static FamilyListActivity sActivity;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_family_list);
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
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserstring = mPreferences.getString("USER_NAME", "");
			mPreferences.getString("NICK", "");
		}
		sObject = new ArrayList<HashMap<String, Object>>();
		// 获取服务器家人信息
		VolleyFamilyhttp.VolleyAllfamily(sContext, "UserListActivity",
				sUserstring);
		sActivity = FamilyListActivity.this;
		sContext = FamilyListActivity.this;
		sListFamily = (ListView) findViewById(R.id.lv_family);
	}

	/**
	 * @description handler
	 */
	public static Handler listHandler = new Handler() {
		
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			sObject = (List<HashMap<String, Object>>) msg.obj;
			sListFamily.setAdapter(new FamilyListAdapter(sActivity, sObject,
					sUserstring));	
		}
	};

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
		startActivity(new Intent(FamilyListActivity.this,
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
