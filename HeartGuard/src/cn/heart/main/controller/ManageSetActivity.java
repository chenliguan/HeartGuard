/**
 * @file FamilyDetailActivity.java
 * @description 家人信息管理Activity类
 * @author Guan
 * @date 2015-6-9 下午3:33:06 
 * @version 1.0
 */
package cn.heart.main.controller;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.config.Constant;
import cn.heart.main.activity.AboutAppActivity;
import cn.heart.main.activity.BoothSetActivity;
import cn.heart.main.activity.SwitchUserActivity;
import cn.heart.main.activity.FamilyListActivity;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.ActivityManager;
import cn.heart.utils.SpellHelper;
import cn.heart.view.CircleImageView;

import com.example.wechatsample.R;

/**
 * @description 家人信息管理Activity类
 * @author Guan
 * @date 2015-6-9 下午3:33:06
 * @version 1.0
 */
public class ManageSetActivity extends Activity {

	@InjectView(R.id.civ_head_afd)
	CircleImageView mCircleHead;
	@InjectView(R.id.tv_nick_afd)
	TextView mNick;
	@InjectView(R.id.rlyt_user_afd)
	RelativeLayout mUserDetail;
	@InjectView(R.id.rlyt_group)
	RelativeLayout mFamilyGroup;
	@InjectView(R.id.rlyt_add_family)
	RelativeLayout mAddFamily;
	@InjectView(R.id.rlyt_account_set)
	RelativeLayout mAccountSetting;
	@InjectView(R.id.rlyt_my_ecgsensor)
	RelativeLayout mMyEcgSensor;
	@InjectView(R.id.rlyt_about_app)
	RelativeLayout mAboutApp;

	private ActionBar mActionBar;
	private String mUserString, mNickString;
	private SharedPreferences mPreferences;
	// Image 下载器
	private ImageDownLoader mMImageDownLoader;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_family_manage);
		ButterKnife.inject(this);
		// ActionBar
		Action();
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
	 * @description 功能化
	 * @return void
	 */
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private void Initivite() {

		ActivityManager.getInstance().addActivity(this);
		mMImageDownLoader = new ImageDownLoader(this);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
			mNickString = mPreferences.getString("NICK", "");
		}
		mNick.setText(mNickString);
		final String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUserString + SpellHelper.getEname(mNickString) + ".jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mMImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			mCircleHead.setImageBitmap(bitmap);
	}

	/**
	 * @description 当前使用用户详情
	 * @author Guan
	 * @date 2015-6-9 下午3:35:44
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_user_afd)
	void UserDetailClickListener(View v) {
		startActivity(new Intent(ManageSetActivity.this, SwitchUserActivity.class));
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 家人成员信息
	 * @author Guan
	 * @date 2015-6-9 下午3:35:57
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_group)
	void FamilyGroupClickListener(View v) {
		startActivity(new Intent(ManageSetActivity.this, FamilyListActivity.class));
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 添加家人
	 * @author Guan
	 * @date 2015-6-9 下午3:36:12
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_add_family)
	void AddFamilyClickListener(View v) {
		Intent intent = new Intent();
		intent.setClass(ManageSetActivity.this, UserDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("activity", "AddFamilyActivity");
		intent.putExtras(bundle);
		startActivity(intent);
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 账户设置
	 * @author Guan
	 * @date 2015-6-9 下午3:36:24
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_account_set)
	void AccountSettingClickListener(View v) {
		startActivity(new Intent(ManageSetActivity.this,
				AccountSetActivity.class));
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 我的智能云心电仪
	 * @author Guan
	 * @date 2015-6-9 下午3:36:36
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_my_ecgsensor)
	void MyEcgsensorClickListener(View v) {
		startActivity(new Intent(ManageSetActivity.this, BoothSetActivity.class));
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 关于app
	 * @author Guan
	 * @date 2015-6-9 下午3:36:48
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_about_app)
	void AboutAppClickListener(View v) {
		startActivity(new Intent(ManageSetActivity.this, AboutAppActivity.class));
		ManageSetActivity.this.finish();
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.finish();
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
}
