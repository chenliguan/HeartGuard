/**
 * @file RegisterActivity.java
 * @description 注册Activity类
 * @author Guan
 * @date 2015-6-9 下午6:23:51 
 * @version 1.0
 */
package cn.heart.main.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.app.App;
import cn.heart.config.Common;
import cn.heart.net.ImageUploadHttp;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.Check;

import com.example.wechatsample.R;

/**
 * @description 注册Activity类
 * @author Guan
 * @date 2015-6-9 下午6:23:51
 * @version 1.0
 */
public class RegisterActivity extends Activity {

	@InjectView(R.id.tv_code)
	TextView mCodetext;
	@InjectView(R.id.et_nick_ar)
	EditText mNick;
	@InjectView(R.id.et_user_ar)
	EditText mUser;
	@InjectView(R.id.et_pass_ar)
	EditText mPass;
	@InjectView(R.id.et_code)
	EditText mCode;
	@InjectView(R.id.btn_register)
	Button mRegister;
	@InjectView(R.id.iv_hide_ar)
	ImageView mHide;
	@InjectView(R.id.iv_show_ar)
	ImageView mShow;

	private TimeCount mTimeCount;
	private SharedPreferences mPreferences;
	/**
	 * 定义Intent对象,用来连接两个Activity
	 */
	private static Intent sIntent;
	private static SharedPreferences.Editor sEditor;
	private static String sUserString, sPassString, sNickString, sCodeString;
	public static Context sContext;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		ButterKnife.inject(this);
		// 功能化
		Initivite();
	}

	/**
	 * @description 初始化并设置监听
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	public void Initivite() {
		mNick.addTextChangedListener(new TextChange());
		mCode.addTextChangedListener(new TextChange());
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		sEditor = mPreferences.edit();
		// 构造CountDownTimer对象
		mTimeCount = new TimeCount(60000, 1000);
		sContext = RegisterActivity.this;
	}

	/**
	 * @description 获取验证码监听实现
	 * @author Guan
	 * @date 2015-6-9 下午6:27:33
	 * @version 1.0
	 */
	@OnClick(R.id.tv_code)
	void getcodeClickListener(View arg0) {
		sUserString = mUser.getText().toString();
		String checkresult = Check.CodeCheck(sUserString);
		if (checkresult.equals("")) {
			// 开始计时
			mTimeCount.start();
			// 获取验证码请求
			VolleyUserhttp.VolleyCode(sContext, sUserString);
		} else {
			Common.Toast(getApplicationContext(), checkresult);
		}
	}

	/**
	 * @description 创建注册监听实现
	 * @author Guan
	 * @date 2015-6-9 下午6:27:46
	 * @version 1.0
	 */
	@OnClick(R.id.btn_register)
	void registerClickListener(View arg0) {
		sUserString = mUser.getText().toString();
		sPassString = mPass.getText().toString();
		sNickString = mNick.getText().toString();
		sCodeString = mCode.getText().toString();
		String checkresult = Check.RegisterCheck(sUserString, sPassString,
				sNickString, sCodeString);
		if (checkresult.equals("")) {
			// 获取服务器注册请求
			VolleyUserhttp.VolleyRegister(sContext, sUserString, sPassString,
					sNickString, sCodeString);
		} else {
			Common.Toast(getApplicationContext(), checkresult);
		}
	}

	/**
	 * @description 隐藏密码监听
	 * @author Guan
	 * @date 2015-6-9 下午6:28:00
	 * @version 1.0
	 */
	@OnClick(R.id.iv_hide_ar)
	void hideClickListener(View arg0) {

		mHide.setVisibility(View.GONE);
		mShow.setVisibility(View.VISIBLE);
		mPass.setTransformationMethod(HideReturnsTransformationMethod
				.getInstance());
		// 切换后将EditText光标置于末尾
		CharSequence charSequence = mPass.getText();
		if (charSequence instanceof Spannable) {
			Spannable spanText = (Spannable) charSequence;
			Selection.setSelection(spanText, charSequence.length());
		}
	}

	/**
	 * @description 显示密码监听
	 * @author Guan
	 * @date 2015-6-9 下午6:28:12
	 * @version 1.0
	 */
	@OnClick(R.id.iv_show_ar)
	void showClickListener(View arg0) {

		mShow.setVisibility(View.GONE);
		mHide.setVisibility(View.VISIBLE);
		mPass.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		// 切换后将EditText光标置于末尾
		CharSequence charSequence = mPass.getText();
		if (charSequence instanceof Spannable) {
			Spannable spanText = (Spannable) charSequence;
			Selection.setSelection(spanText, charSequence.length());
		}
	}

	/**
	 * @description EditText监听器
	 * @author Guan
	 * @date 2015-6-9 下午6:28:32
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
			boolean Sign2 = mUser.getText().length() > 0;
			boolean Sign3 = mPass.getText().length() > 0;
			boolean Sign4 = mNick.getText().length() > 0;
			boolean Sign5 = mCode.getText().length() > 0;
			if (Sign2 & Sign3 & Sign4 & Sign5) {
				mRegister.setEnabled(true);
			} else {
				mRegister.setEnabled(false);
			}
		}
	}

	/**
	 * @description 消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if (msg.what == 0x124) {
				Common.Toast(sContext, msg.obj.toString());
			}
			if ((msg.obj).equals("注册成功")) {
				// 记住账号密码
				sEditor.putString("USER_NAME", sUserString);
				sEditor.putString("PASSWORD", sPassString);
				sEditor.commit();
				Resources res = sContext.getResources();
				Bitmap bitmap = BitmapFactory.decodeResource(res,
						R.drawable.ic_head);
				// 保存头像在SD卡并上传头像到服务器
				ImageUploadHttp.PreserImage(bitmap, sUserString + sNickString);
				// 获取服务器添加家人请求
				VolleyFamilyhttp.VolleyAddFamily(sContext, "RegisterActivity",
						sUserString, sNickString, "", "", "", "", "是");
				sIntent = new Intent(sContext, MainActivity.class);
				sContext.startActivity(sIntent);
			} else {
				Common.Toast(sContext, msg.obj.toString() + "并登陆成功");
			}
		}
	};

	/**
	 * @description 重写倒计时类
	 * @author Guan
	 * @date 2015-6-9 下午6:29:18
	 * @version 1.0
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			// 参数依次为总时长,和计时的时间间隔
			super(millisInFuture, countDownInterval);
		}

		/**
		 * @description 计时完毕时触发
		 */
		@Override
		public void onFinish() {
			mCodetext.setText("获取验证码");
			mCodetext.setTextColor(Color.parseColor("#5785d5"));
			mCodetext.setClickable(true);
		}

		/**
		 * @description 计时过程显示
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			mCodetext.setClickable(false);
			mCodetext.setTextColor(Color.parseColor("#d3d3d3"));
			mCodetext.setText(millisUntilFinished / 1000 + "秒" + "后可以重新获取验证码");
		}
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

	/**
	 * @description 退出监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
		return false;
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("Register");
		App.getHttpQueue().cancelAll("AddFamily");
		App.getHttpQueue().cancelAll("Code");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("Register");
		App.getHttpQueue().cancelAll("AddFamily");
		App.getHttpQueue().cancelAll("Code");
	}
}