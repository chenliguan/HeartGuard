/**
 * @file ShowActivity.java
 * @description 测量结束显示结果详情Activity类
 * @author Guan
 * @date 2015-6-9 下午9:21:09 
 * @version 1.0
 */
package cn.heart.main.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.app.App;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.SpellHelper;
import cn.heart.view.ChartView;
import cn.heart.view.CircleImageView;

import com.example.wechatsample.R;

/**
 * @description 测量结束显示结果详情Activity类
 * @author Guan
 * @date 2015-6-9 下午9:21:09
 * @version 1.0
 */
public class ShowActivity extends Activity {
	
	/**
	 * 折线图
	 */
	@InjectView(R.id.cv_view)
	ChartView mMyView;
	/**
	 * 用户昵称
	 */
	@InjectView(R.id.tv_nick)
	TextView mNick;
	/**
	 * 头像
	 */
	@InjectView(R.id.civ_head_asr)
	CircleImageView mCircleHead;
	/**
	 * 时间
	 */
	@InjectView(R.id.tv_whole_time)
	TextView mWholeTime;
	/**
	 * 心律症状图
	 */
	@InjectView(R.id.iv_rhythm)
	ImageView mImageRhythm;
	/**
	 * 心房症状图
	 */
	@InjectView(R.id.iv_heart)
	ImageView mImageHeart;
	/**
	 * 心律主要病症
	 */
	@InjectView(R.id.tv_symptoms_rhythm)
	TextView mSymRhythm;
	/**
	 * 心房主要病症
	 */
	@InjectView(R.id.tv_symptoms_heart)
	TextView mSymHeart;
	/**
	 * 心率得分
	 */
	@InjectView(R.id.tv_rate_grade)
	TextView mRateGrade;
	/**
	 * 心率平均值
	 */
	@InjectView(R.id.tv_rate_average)
	TextView mRateAverage;
	/**
	 * 心律节奏
	 */
	@InjectView(R.id.tv_rhythm_heart)
	TextView mRhythmHeart;
	/**
	 * 心动情况
	 */
	@InjectView(R.id.tv_cardia_heart)
	TextView mCardiaHeart;
	/**
	 * 心跳个数
	 */
	@InjectView(R.id.tv_heart_beat_num)
	TextView mHeartBeatNum;
	/**
	 * 窦性停搏
	 */
	@InjectView(R.id.tv_sinus_arrest)
	TextView mSinusArrest;
	/**
	 * 室上性期前收缩个数
	 */
	@InjectView(R.id.tv_psvc_num)
	TextView mPsvcNum;
	/**
	 * 室性期前收缩个数
	 */
	@InjectView(R.id.tv_pvc_num)
	TextView mPvcNum;
	/**
	 * 左心房负荷增重
	 */
	@InjectView(R.id.tv_heart_left)
	TextView mSymHeartLeft;
	/**
	 * 右心房负荷增重
	 */
	@InjectView(R.id.tv_heart_right)
	TextView mSymHeartRight;
	/**
	 * 两房负荷增重
	 */
	@InjectView(R.id.tv_heart_two)
	TextView mSymHeartTwo;

	private int mPosition;
	private App mApplication;
	private ActionBar mActionBar;
	private String mUserString;
	private SharedPreferences mPreferences;
	private List<HashMap<String, Object>> mData;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_result0);
		ButterKnife.inject(this);
		// ActionBar
		Action();

		// 初始化函数
		Init();
		// 初始化心律
		InitRhythm();
		// 初始化心房
		InitHeart();
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
	 * @description 初始化函数
	 */
	@SuppressWarnings("unchecked")
	public void Init() {
		Intent intent = this.getIntent();
		mData = new ArrayList<HashMap<String, Object>>();
		mData = (List<HashMap<String, Object>>) intent
				.getSerializableExtra("object");
		mPosition = intent.getExtras().getInt("position");

		mApplication = (App) getApplication();
		mApplication.getResultData();
		String[] X = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "10" };
		String[] Y = new String[] { "", "25", "50", "75", "100" };
		String[] DATA = new String[] { "", "", "", "", "", "", "", "", "", "" };
		for (int i = 0; i < mData.size(); i++) {
			DATA[i] = (String) mData.get(mData.size() - 1 - i)
					.get("rate_grade");
		}
		X[mPosition] = "本次";
		// X轴刻度，Y轴刻度，数据
		mMyView.SetInfo(X, Y, DATA, "图标的标题");
	}

	/**
	 * @description 初始化心律函数
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint({ "HandlerLeak", "SimpleDateFormat", "WorldReadableFiles" })
	public void InitRhythm() {

		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
		}

		// 调用诊断心率函数
		initRhythmSymptoms();

		/** 获取数据值 */
		mNick.setText((String) mData.get(mPosition).get("nick"));
		mWholeTime.setText((String) mData.get(mPosition).get("time"));
		mSymRhythm
				.setText((String) mData.get(mPosition).get("symptoms_rhythm"));
		// 心跳个数
		mHeartBeatNum.setText((String) mData.get(mPosition).get(
				"heart_beat_number")
				+ "个");
		// 心率得分
		mRateGrade.setText((String) mData.get(mPosition).get("rate_grade"));
		// 心率平均值
		mRateAverage.setText((String) mData.get(mPosition).get("rate_average"));
		// 室上性期前收缩个数
		mPsvcNum.setText((String) mData.get(mPosition).get("psvc_number") + "个");
		// 室性期前收缩个数
		mPvcNum.setText((String) mData.get(mPosition).get("pvc_number") + "个");
		// 窦性停搏
		mSinusArrest.setText((String) mData.get(mPosition).get("sinus_arrest"));
		mRhythmHeart.setText((String) mData.get(mPosition).get("rhythm_heart"));
		mCardiaHeart.setText((String) mData.get(mPosition).get("cardia_heart"));

		// 头像显示
		ImageDownLoader mImageDownLoader = new ImageDownLoader(this);
		String mImageUrl = Constant.httpUrl
				+ "DownloadFileServlet?file="
				+ mUserString
				+ SpellHelper.getEname((String) mData.get(mPosition)
						.get("nick")) + ".jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			mCircleHead.setImageBitmap(bitmap);
	}

	/**
	 * @description初始化心房函数
	 */
	public void InitHeart() {
	
		// 调用诊断心房函数
		initHeartSymptoms();
		/** 获取数据值 */
		mSymHeartLeft.setText((String) mData.get(mPosition).get(
				"symptoms_heart_left"));
		mSymHeartRight.setText((String) mData.get(mPosition).get(
				"symptoms_heart_right"));
		mSymHeartTwo.setText((String) mData.get(mPosition).get(
				"symptoms_heart_two"));
		mSymHeart.setText((String) mData.get(mPosition).get("symptoms_heart"));
	}

	/**
	 * @description 诊断心律，在onCreate()中调用
	 */
	private void initRhythmSymptoms() {
		/** 心律病症 */
		if (Integer.parseInt((String) mData.get(mPosition).get("psvc_number")) > 0) {
			mImageRhythm.setImageResource(R.drawable.ic_sym_bad);
			mSymRhythm.setTextSize(15);
		} else if (Integer.parseInt((String) mData.get(mPosition).get(
				"pvc_number")) > 0) {
			mImageRhythm.setImageResource(R.drawable.ic_sym_medium);
			mSymRhythm.setTextSize(16);
		} else {
			// 健康
			mImageRhythm.setImageResource(R.drawable.ic_sym_health);
			mSymRhythm.setTextSize(30);
		}
	}

	/**
	 * @description 初始化心房病症，在onCreate()中调用
	 */
	private void initHeartSymptoms() {
		/** 诊断左房 */
		if (((String) mData.get(mPosition).get("symptoms_heart_left"))
				.equals("无症状")) {
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			if (((String) mData.get(mPosition).get("symptoms_heart_left"))
					.equals("较轻")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (((String) mData.get(mPosition)
					.get("symptoms_heart_left")).equals("严重")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}

		/** 诊断右房 */
		if (((String) mData.get(mPosition).get("symptoms_heart_right"))
				.equals("无症状")) {
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			if (((String) mData.get(mPosition).get("symptoms_heart_right"))
					.equals("较轻")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (((String) mData.get(mPosition).get(
					"symptoms_heart_right")).equals("严重")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}

		/** 诊断出的两房负荷总数 */
		if (((String) mData.get(mPosition).get("symptoms_heart_two"))
				.equals("无症状")) {
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			if (((String) mData.get(mPosition).get("symptoms_heart_two"))
					.equals("较轻")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (((String) mData.get(mPosition).get("symptoms_heart_two"))
					.equals("严重")) {
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			this.finish();
		}
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