/**
 * @file AccountSetActivity.java
 * @description 账号设置Activity类
 * @author Guan
 * @date 2015-6-9 下午3:12:45 
 * @version 1.0
 */
package cn.heart.main.controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.main.activity.PassSetActivity;
import cn.heart.utils.ActivityManager;
import cn.heart.view.SwitchView;
import cn.heart.view.SwitchView.OnCheckedChangeListener;

import com.example.wechatsample.R;

/**
 * @description 账号设置Activity类
 * @author Guan
 * @date 2015-6-9 下午3:12:45
 * @version 1.0
 */
public class AccountSetActivity extends Activity {

	@InjectView(R.id.tv_user)
	TextView mUser;
	@InjectView(R.id.btn_out_login)
	Button mOutLogin;
	@InjectView(R.id.rlyt_modify_pass)
	RelativeLayout mModifyPass;

	private String mUserString;
	private ActionBar mActionBar;
	private SharedPreferences mPreferences;
	private LinearLayout mSwitchR, mSwitchA;
	private SwitchView mSwitchViewR, mSwitchViewA;
	private static SharedPreferences.Editor sEditor;
	private static String sRString = "ON", sAString = "ON";
	public static Context sContext;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_account_set);
		ButterKnife.inject(this);
		// ActionBar
		Action();
		// 初始化
		Init();
		// 功能化
		Initivite();
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
	private void Init() {
		mSwitchR = (LinearLayout) findViewById(R.id.llyt_switch_rem);
		mSwitchA = (LinearLayout) findViewById(R.id.llyt_switch_auto);
	}

	/**
	 * @description 功能化
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	private void Initivite() {
		ActivityManager.getInstance().addActivity(this);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		sEditor = mPreferences.edit();
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
			mPreferences.getString("NICK", "");
			sRString = mPreferences.getString("REMEMBER_PASSWORD", "ON");
			sAString = mPreferences.getString("AUTOMATIC_LOGIN", "ON");
		}
		mUser.setText(mUserString);
		sContext = AccountSetActivity.this;
		mSwitchViewR = new SwitchView(this, SwitchStateRemember(), "whether");
		mSwitchViewA = new SwitchView(this, SwitchStateAutomatic(), "whether");
		mSwitchViewA
				.setOnCheckedChangeListener(new OnSwitchautomaticCheckedChangeListener());
		mSwitchViewR
				.setOnCheckedChangeListener(new OnSwitchRememberCheckedChangeListener());
		mSwitchR.addView(mSwitchViewR);
		mSwitchA.addView(mSwitchViewA);
	}

	/**
	 * @description 记住密码
	 * @author Guan
	 * @date 2015-6-9 下午3:14:38
	 * @version 1.0
	 */
	private final class OnSwitchRememberCheckedChangeListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(boolean isChecked) {
			sRString = getState(isChecked);
		}
	}

	/**
	 * @description 自动登录
	 * @author Guan
	 * @date 2015-6-9 下午3:14:52
	 * @version 1.0
	 */
	private final class OnSwitchautomaticCheckedChangeListener implements
			OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(boolean isChecked) {
			sAString = getState(isChecked);
		}
	}

	/**
	 * @description 修改密码
	 * @author Guan
	 * @date 2015-6-9 下午3:15:03
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_modify_pass)
	void ModifyPasswordClickListener(View v) {
		startActivity(new Intent(AccountSetActivity.this, PassSetActivity.class));
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 退出登录
	 * @author Guan
	 * @date 2015-6-9 下午3:15:20
	 * @version 1.0
	 */
	@OnClick(R.id.btn_out_login)
	void OutLoginClickListener(View v) {
		startActivity(new Intent(AccountSetActivity.this, LoginActivity.class));
		ActivityManager.getInstance().exit();
	}

	/**
	 * @description SwitchStateRemember
	 * @return
	 */
	private boolean SwitchStateRemember() {
		if (sRString.equals("OFF")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @description SwitchStateAutomatic
	 * @return
	 */
	private boolean SwitchStateAutomatic() {
		if (sAString.equals("OFF")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @description 设置滑动控件的状态
	 * @param state
	 * @return String
	 */
	private String getState(boolean state) {
		if (state) {
			return "ON";
		}
		return "OFF";
	}

	/**
	 * @description 保存是否记住密码、自动登录
	 */
	public void editor() {
		sEditor.putString("REMEMBER_PASSWORD", sRString);
		sEditor.putString("AUTOMATIC_LOGIN", sAString);
		if (sRString.equals("OFF")) {
			sEditor.putString("PASSWORD", "");
		}
		sEditor.commit();
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		editor();
		startActivity(new Intent(AccountSetActivity.this,
				ManageSetActivity.class));
		AccountSetActivity.this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
		return false;
	}

	/**
	 * @description home图标监听
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			editor();
			startActivity(new Intent(AccountSetActivity.this,
					ManageSetActivity.class));
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}
}
