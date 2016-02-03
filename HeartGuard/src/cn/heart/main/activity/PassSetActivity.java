/**
 * @file PassSetActivity.java
 * @description 密码设置类
 * @author Guan
 * @date 2015-6-9 下午2:55:06 
 * @version 1.0
 */
package cn.heart.main.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.app.App;
import cn.heart.config.Common;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.Check;

import com.example.wechatsample.R;

/**
 * @description 密码设置Activity类
 * @author Guan
 * @date 2015-6-9 下午2:55:06
 * @version 1.0
 */
public class PassSetActivity extends Activity {

	@InjectView(R.id.et_old_pass)
	EditText mOldPass;
	@InjectView(R.id.et_new_pass)
	EditText mNewPass;
	@InjectView(R.id.et_ok_new_pass)
	EditText mOkNewPass;

	private ActionBar mActionBar;
	private SharedPreferences mPreferences;
	private String mUserString, mOldPassString, mNewPassString,
			mOkNewPassString;
	private static ProgressDialog sProgressDialog;
	public static Context sContext;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_pass_set);
		ButterKnife.inject(this);
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
		mPreferences.edit();
		sContext = PassSetActivity.this;
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
		}
	}

	/**
	 * @description 下拉菜单 onCreateOptionsMenu()方法中去加载main.xml文件(non-Javadoc)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.onedetail_tab, menu);
		MenuItem passwordsetitem = menu.findItem(R.id.check).setChecked(true);
		passwordsetitem
				.setOnMenuItemClickListener(new OnMenuItemClickListener());
		return true;
	}

	/**
	 * @description 菜单项触发监听
	 */
	public class OnMenuItemClickListener implements
			android.view.MenuItem.OnMenuItemClickListener {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			mOldPassString = mOldPass.getText().toString();
			mNewPassString = mNewPass.getText().toString();
			mOkNewPassString = mOkNewPass.getText().toString();
			String checkresult = Check.PasswordSetCheck(mOldPassString,
					mNewPassString, mOkNewPassString);
			if (checkresult.equals("")) {
				PassSetThreadDialog(); // 修改密码滚动对话框并发送线程
			} else {
				Common.Toast(getApplicationContext(), checkresult);
			}
			return false;
		}
	}

	/**
	 * @description 修改密码消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x111) {
				sProgressDialog.dismiss(); // 关闭ProgressDialog
				((Activity) sContext).finish();
				Common.Toast(sContext, "修改密码成功");
			} else {
				Common.Toast(sContext, "修改密码失败");
			}
		}
	};

	/**
	 * @description 修改密码滚动对话框并发送线程
	 */
	public void PassSetThreadDialog() {
		
		// 显示ProgressDialog
		sProgressDialog = ProgressDialog.show(PassSetActivity.this,
				"正在修改.....", "请等待.....", true, false);
		
		// 新建线程
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					VolleyUserhttp.VolleyModifyPass(sContext, mUserString,
							mOldPassString, mNewPassString, mOkNewPassString);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		PassSetActivity.this.finish();
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
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("ModifyPass");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("ModifyPass");
	}
}
