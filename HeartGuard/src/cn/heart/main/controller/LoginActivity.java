/**
 * @file LoginActivity.java
 * @description 登录Activity类
 * @author Guan
 * @date 2015-6-9 下午3:56:35 
 * @version 1.0
 */
package cn.heart.main.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.app.App;
import cn.heart.config.Common;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.ActivityManager;
import cn.heart.utils.Check;
import cn.heart.view.MyPopupWindow;

import com.example.wechatsample.R;

/**
 * @description 登录Activity类
 * @author Guan
 * @date 2015-6-9 下午3:56:35
 * @version 1.0
 */
public class LoginActivity extends Activity {

	@InjectView(R.id.tv_forget)
	TextView mForget;
	@InjectView(R.id.tv_register)
	TextView mRegister;
	@InjectView(R.id.et_user_al)
	EditText mUserName;
	@InjectView(R.id.et_pass_al)
	EditText mPassWord;
	@InjectView(R.id.btn_login)
	Button mLogin;
	@InjectView(R.id.iv_hide)
	ImageView mHide;
	@InjectView(R.id.iv_show)
	ImageView mShow;
	
	private SharedPreferences mPreferences;
	private SharedPreferences.Editor mEditor;
	private static Intent sIntent;
	private static ProgressDialog sProgressDialog;
	private static String sUserString, sPassString, sRemString = "ON";
	public static Context sContext;
	public static Activity sActivity;

	/**
	 * @description onCreate
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_login);
		ButterKnife.inject(this);
		// 初始化并设置监听
		Init();
		// 记住密码,读取数据
		Reader();
	}

	/**
	 * @description 初始化
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public void Init() {
		// 给多个编辑框添加监听
		mUserName.addTextChangedListener(new TextChange());
		mPassWord.addTextChangedListener(new TextChange());
		// 给注册文本设置监听器
		ActivityManager.getInstance().addActivity(this);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		mEditor = mPreferences.edit();
		sContext = LoginActivity.this;
		sActivity = LoginActivity.this;
	}

	/**
	 * @description 读出账号密码
	 */
	public void Reader() {
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserString = mPreferences.getString("USER_NAME", "");
			sPassString = mPreferences.getString("PASSWORD", "");
			sRemString = mPreferences.getString("REMEMBER_PASSWORD", "ON");
			mUserName.setText(sUserString);
			mPassWord.setText(sPassString);
			mUserName.setSelection(sUserString.length());
			mPassWord.setSelection(mPassWord.length());
		} 
		else {
		}
	}

	/**
	 * @description 登录按钮监听实现
	 * @author Guan
	 * @date 2015-6-9 下午3:58:46
	 * @version 1.0
	 */
	@OnClick(R.id.btn_login)
	void LoginClickListener() {
		
		sUserString = mUserName.getText().toString();
		sPassString = mPassWord.getText().toString();
		String checkresult = Check.LoginCheck(sUserString, sPassString);
		if (checkresult.equals("")) {
			mEditor.putString("USER_NAME", sUserString);
			if (sRemString.equals("ON")) {
				mEditor.putString("PASSWORD", sPassString);
			}
			mEditor.commit();

			sProgressDialog = ProgressDialog.show(LoginActivity.this,
					"正在登录.....", "请等待.....", true, false);
			// 新建线程
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						VolleyUserhttp.VolleyLogin(sContext, "LoginActivity",
								sProgressDialog, sUserString, sPassString); // 获取服务器登录请求
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		} else {
			Common.Toast(getApplicationContext(), checkresult);
		}
	}

	/**
	 * @description 登录消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			sProgressDialog.dismiss(); // 关闭ProgressDialog

			if (msg.what == 0x123 & (msg.obj.toString()).equals("登录成功！")) {
				Intent intent = new Intent();
				intent.setClass(sContext, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 注意本行的FLAG设置:清除该进程空间的所有Activity
				sContext.startActivity(intent);
				Common.Toast(sContext, msg.obj.toString());
			} 
			else {
				Common.Toast(sContext, msg.obj.toString());
			}
		}
	};

	/**
	 * @description 获取密码消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler passhandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x125) {
				Common.Toast(sContext, msg.obj.toString());
			}
		}
	};

	/**
	 * @description 创建注册监听
	 * @author Guan
	 * @date 2015-6-9 下午3:59:41
	 * @version 1.0
	 */
	@OnClick(R.id.tv_register)
	void registerClickListener() {
		sIntent = new Intent(LoginActivity.this, RegisterActivity.class);
		startActivity(sIntent);
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 创建忘记密码监听
	 * @author Guan
	 * @date 2015-6-9 下午3:59:51
	 * @version 1.0
	 */
	@OnClick(R.id.tv_forget)
	void forgetClickListener(View v) {
		// popupwindow忘记密码框
		MyPopupWindow.FindUserNamePopup(v, sContext, sActivity);
	}

	/**
	 * @description EditText监听器
	 * @author Guan
	 * @date 2015-6-9 下午4:00:29
	 * @version 1.0
	 */
	private class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign2 = mUserName.getText().length() > 0;
			boolean Sign3 = mPassWord.getText().length() > 0;
			if (Sign2 & Sign3) {
				mLogin.setEnabled(true);
			} else {
				mLogin.setEnabled(false);
			}
		}
	}

	/**
	 * @description 隐藏密码监听
	 * @author Guan
	 * @date 2015-6-9 下午4:00:41
	 * @version 1.0
	 */
	@OnClick(R.id.iv_hide)
	void hideClickListener() {
		mHide.setVisibility(View.GONE);
		mShow.setVisibility(View.VISIBLE);
		mPassWord.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		// 切换后将EditText光标置于末尾
		CharSequence charSequence = mPassWord.getText();
		if (charSequence instanceof Spannable) {
			Spannable spanText = (Spannable) charSequence;
			Selection.setSelection(spanText, charSequence.length());
		}
	}

	/**
	 * @description 显示密码监听
	 * @author Guan
	 * @date 2015-6-9 下午4:00:54
	 * @version 1.0
	 */
	@OnClick(R.id.iv_show)
	void showClickListener() {
		mShow.setVisibility(View.GONE);
		mHide.setVisibility(View.VISIBLE);
		mPassWord.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		// 切换后将EditText光标置于末尾
		CharSequence charSequence = mPassWord.getText();
		if (charSequence instanceof Spannable) {
			Spannable spanText = (Spannable) charSequence;
			Selection.setSelection(spanText, charSequence.length());
		}
	}

	/**
	 * @description设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	/**
	 * @description 退出监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.finish();
		return false;
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("Password");
		App.getHttpQueue().cancelAll("Login");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("Password");
		App.getHttpQueue().cancelAll("Login");
	}
}
