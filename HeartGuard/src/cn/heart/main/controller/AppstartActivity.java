/**
 * @file AppstartActivity.java
 * @description APP启动Activity类
 * @author Guan
 * @date 2015-6-9 下午3:27:29 
 * @version 1.0
 */
package cn.heart.main.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.app.App;
import cn.heart.app.MyApplication;
import cn.heart.config.Common;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.ActivityManager;

import com.example.wechatsample.R;

/**
 * @description APP启动Activity类
 * @author Guan
 * @date 2015-6-9 下午3:27:29
 * @version 1.0
 */
@SuppressLint("WorldReadableFiles")
public class AppstartActivity extends Activity {

	@InjectView(R.id.iv_welcome)
	ImageView mWelcomeImg;
	
	private SharedPreferences mPreferences;
	private static boolean sLoginIs = false;
	private static String sUserString, sPassString, sAutomaticString;
	public static Context sContext;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_start);
		ButterKnife.inject(this);
		MyApplication.getInstance().addActivity(this);
		// 动画设置
		Animation();
		// 初始化与功能化
		Initivite();
	}

	/**
	 * @description 动画设置
	 */
	private void Animation() {
		AlphaAnimation animation = new AlphaAnimation(0.05f, 1.0f);
		// 延续时间
		animation.setDuration(3000);
		mWelcomeImg.startAnimation(animation);
		animation.setAnimationListener(new AnimationImpl());
	}

	/**
	 * @description 初始化并设置监听
	 */
	@SuppressWarnings("deprecation")
	private void Initivite() {
		
		ActivityManager.getInstance().addActivity(this);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		mPreferences.edit();
		sContext = AppstartActivity.this;
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserString = mPreferences.getString("USER_NAME", "");
			sPassString = mPreferences.getString("PASSWORD", "");
			sAutomaticString = mPreferences.getString("AUTOMATIC_LOGIN", "");
		}
		if (sAutomaticString.equals("ON")) {
			// 获取服务器登录请求
			VolleyUserhttp.VolleyLogin(sContext, "AppstartActivity", null,
					sUserString, sPassString);
		}
	}

	/**
	 * @description 进入动画
	 * @author Guan
	 * @date 2015-6-9 下午3:29:08
	 * @version 1.0
	 */
	private class AnimationImpl implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {
			mWelcomeImg.setBackgroundResource(R.drawable.bg_welcome);
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			skip();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	private void skip() {
		if (sLoginIs == true) {
			startActivity(new Intent(AppstartActivity.this, MainActivity.class));
		} else {
			startActivity(new Intent(AppstartActivity.this, LoginActivity.class));
		}
		finish();
	}

	/**
	 * @description 登录消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				Common.Toast(sContext, msg.obj.toString());
			}
			if ((msg.obj.toString()).equals("登录成功！")) {
				// 登录标记
				sLoginIs = true;
			} else {
				Common.Toast(sContext, msg.obj.toString());
			}
		}
	};

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("Login");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("Login");
	}
}
