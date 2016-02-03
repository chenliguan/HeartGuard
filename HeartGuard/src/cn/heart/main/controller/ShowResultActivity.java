/**
 * @file ShowActivity.java
 * @description 测量结束显示结果详情Activity类
 * @author Guan
 * @date 2015-6-9 下午9:21:09 
 * @version 1.0
 */
package cn.heart.main.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.app.App;
import cn.heart.bean.ResultData;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.net.VolleyResultHttp;
import cn.heart.service.algorithm.DiseaseMatch;
import cn.heart.utils.Speech;
import cn.heart.utils.SpellHelper;
import cn.heart.view.ChartView;
import cn.heart.view.CircleImageView;

import com.example.wechatsample.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * @description 结果详情Activity类
 * @author Guan
 * @date 2015-6-9 下午9:21:09
 * @version 1.0
 */
public class ShowResultActivity extends Activity {

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

	@InjectView(R.id.rlyt_onclick)
	RelativeLayout mRelativeOnclick;

	private App mApplication;
	private ArrayList<Float> mDataY;
	private ActionBar mActionBar;
	private Handler mTimeHandler;
	private SharedPreferences mPreferences;
	private TimeThread mTimeThread;
	private int[] mMinmax = new int[2];
	private SpeechSynthesizer mMTts;
	private StringBuilder mSymptoms;
	private String mUserString, mNickString, mFamilyGroup, mRhythm = "",
			mCardia = "", mHeartLeft = "", mHeartRight = "", mHeartTwo = "",
			mHeart = "", mTime = "", mSinus = "否";
	private static ChartView mMyView;
	private static ResultData mResultData;
	private static List<HashMap<String, Object>> mData;
	public static Context mContext;

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
		// 启动发送结果线程
		InitSendResult();
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
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void Init() {

		/** 数据获取 */
		mSymptoms = new StringBuilder();
		mData = new ArrayList<HashMap<String, Object>>();
		mDataY = (ArrayList<Float>) getIntent().getSerializableExtra("dataY");
		mApplication = (App) getApplication();
		mResultData = mApplication.getResultData();
		mMyView = (ChartView) findViewById(R.id.cv_view);
		mContext = ShowResultActivity.this;
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
			mNickString = mPreferences.getString("NICK", "");
			mFamilyGroup = mPreferences.getString("FAMILYGROUP", "");
		}

		// 获取服务器分数请求
		VolleyResultHttp.VolleyGetRate(mContext, mUserString);
		SpeechUtility.createUtility(this, SpeechConstant.APPID + "53c4c169");
		mMTts = SpeechSynthesizer.createSynthesizer(this, null);
		Speech.set_mTts(mMTts);
		Message speechmsg = new Message();
		SpeechHandler.sendMessage(speechmsg);
	}

	/**
	 * @description 初始化心律函数
	 */
	@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
	public void InitRhythm() {

		/** 头像显示 */
		ImageDownLoader mImageDownLoader = new ImageDownLoader(this);
		String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUserString + SpellHelper.getEname(mNickString) + ".jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			mCircleHead.setImageBitmap(bitmap);

		/** 调用诊断心率函数 */
		initRhythmSymptoms();

		/** 获取数据值 */
		mNick.setText(mNickString);
		mSymRhythm.setText(mSymptoms.toString());
		mResultData.setSymptoms(mSymptoms.toString());
		// 心跳个数
		mHeartBeatNum.setText(mResultData.getR() + "个");
		// 心率得分
		mRateGrade.setText(mResultData.getAverageHeart_rate() + "");
		// 心率平均值
		mRateAverage.setText(mResultData.getAverageHeart_rate() + "次");
		// 室上性期前收缩个数
		mPsvcNum.setText(mResultData.getAtrialPrematureBeat() + "个");
		// 室性期前收缩个数
		mPvcNum.setText(mResultData.getPrematureVentricualrContraction() + "个");
		if (mResultData.getSinusArrest() > 0) {
			// 窦性停搏
			mSinusArrest.setText(getResources().getString(R.string.yes));
			mSinus = getResources().getString(R.string.yes);
		}
		mRhythmHeart.setText(mRhythm);
		mCardiaHeart.setText(mCardia);
		mTimeHandler = new Handler() {
			public void handleMessage(Message msg) {
				mWholeTime.setText((String) msg.obj);
			}
		};
		mTime = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss")
				.format(new Date());
		mTimeThread = new TimeThread();
		mTimeThread.start();
	}

	/**
	 * @description 初始化心房函数
	 */
	public void InitHeart() {

		/** 调用诊断心房函数 */
		initHeartSymptoms();

		/** 获取数据值 */
		mSymHeartLeft.setText(mHeartLeft);
		mSymHeartRight.setText(mHeartRight);
		mSymHeartTwo.setText(mHeartTwo);
		mSymHeart.setText(mHeart);
	}

	/**
	 * @description 诊断心律，在onCreate()中调用
	 */
	private void initRhythmSymptoms() {

		// 诊断psvc pvc
		DiseaseMatch dm = new DiseaseMatch(mResultData);
		// 初始化诊断单
		mResultData.setPrematureBeat(0);
		mResultData.setAtrialPrematureBeat(0);
		mResultData.setPrematureVentricualrContraction(0);
		// 找最小最大心率的同时找停搏个数
		mMinmax = dm.findMinMax();

		if (dm.ifPremature()) {
			/** 心律病症 */
			if (mResultData.getAtrialPrematureBeat() > 0) {
				mSymptoms.append(getResources().getString(
						R.string.symptoms_rhythm_psvc));
				mImageRhythm.setImageResource(R.drawable.ic_sym_bad);
				mSymRhythm.setTextSize(15);
			}
			if (mResultData.getPrematureVentricualrContraction() > 0) {
				mSymptoms.append(getResources().getString(
						R.string.symptoms_rhythm_pvc));
				mImageRhythm.setImageResource(R.drawable.ic_sym_medium);
				mSymRhythm.setTextSize(16);
			}
			/** 心律节奏 */
			if (mResultData.getPrematureBeat() < 5) {
				mRhythm = getResources().getString(R.string.rhythm_little_bit);
			} else {
				mRhythm = getResources().getString(R.string.rhythm_shif);
			}
		} else {
			// 健康
			mSymptoms.append(getResources().getString(
					R.string.symptoms_rhythm_health));
			mImageRhythm.setImageResource(R.drawable.ic_sym_health);
			mSymRhythm.setTextSize(30);
			mRhythm = getResources().getString(R.string.rhythm_good);
		}

		/**
		 * 心动情况
		 */
		if (dm.ifBradycardia(60 * 1000 / mResultData.getAverageHeart_rate())) {
			// 是否心动过缓
			mCardia = getResources().getString(R.string.bradycardia);
		} else if (dm.ifTachyrhythm(60 * 1000 / mResultData
				.getAverageHeart_rate())) {
			// 是否心动过速
			mCardia = getResources().getString(R.string.tachycardia);
		} else {
			mCardia = getResources().getString(R.string.good);
		}
	}

	/**
	 * @description 启动发送结果线程
	 */
	private void InitSendResult() {
		// 发送异常消息推送给群主
		if (mFamilyGroup.equals("否")
				& (mResultData.getAverageHeart_rate() > 90
						| !mSymptoms.toString().equals("健康") | !mHeart
							.equals("无症状"))) {
			Log.d("tag", "familygroup1:" + mFamilyGroup);
			VolleyResultHttp.VolleyAbnormalNotice(mContext, mUserString,
					mNickString, mTime,
					mResultData.getAverageHeart_rate() + "",
					mSymptoms.toString(), mHeart);
		}

		// 发送测量结果
		VolleyResultHttp.VolleySendResult(mContext, mUserString, mNickString,
				mTime, mResultData.getAverageHeart_rate() + "",
				mSymptoms.toString(), mResultData.getAverageHeart_rate() + "次",
				mMinmax[0] + "", mMinmax[1] + "", mRhythm, mSinus, mCardia,
				mResultData.getR() + "", mResultData.getAtrialPrematureBeat()
						+ "", mResultData.getPrematureVentricualrContraction()
						+ "", mResultData.getAverageQRS() + "ms",
				mResultData.getRRAverage() + "ms", mResultData.getAverageQT()
						+ "ms", mResultData.getAveragePR() + "ms",
				mResultData.getAverageQTcB() + "ms", mHeart, mHeartLeft,
				mHeartRight, mHeartTwo);
	}

	/**
	 * @description 初始化心房病症，在onCreate()中调用
	 */
	private void initHeartSymptoms() {
		DiseaseMatch dm = new DiseaseMatch(mResultData);
		/** 诊断左房 */
		int sum = 0;
		for (int i = 0; i < mResultData.getDoubleP().size(); i++) {
			if (mResultData.getDoubleP().get(i) != 0) {
				sum++;
			}
		}
		if (sum == 0) {
			mHeart = getResources().getString(R.string.loadnice);
			mHeartLeft = getResources().getString(R.string.loadnice);
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			mHeart = getResources().getString(R.string.lao);
			if (sum < 5) {
				mHeartLeft = getResources().getString(R.string.loadbit);
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (sum < 10) {
				mHeartLeft = getResources().getString(R.string.loadbad);
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mHeartLeft = getResources().getString(R.string.loadshit);
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}

		/** 诊断右房 */
		sum = 0;
		for (int i = 0; i < mResultData.getPpoint().size(); i++) {
			if (Math.abs(mResultData.getBaseline().get(i)
					- mDataY.get(mResultData.getPpoint().get(i))) > 0.25) {
				sum++;
			}
		}
		if (sum == 0) {
			mHeart = getResources().getString(R.string.loadnice);
			mHeartRight = getResources().getString(R.string.loadnice);
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			mHeart = getResources().getString(R.string.rao);
			if (sum < 5) {
				mHeartRight = getResources().getString(R.string.loadbit);
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (sum < 10) {
				mHeartRight = getResources().getString(R.string.loadbad);
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}

		/** 诊断出的两房负荷总数 */
		int bao = dm.isBaoLoad(mDataY);
		if (bao == 0) {
			mHeart = getResources().getString(R.string.loadnice);
			mHeartTwo = getResources().getString(R.string.loadnice);
			mImageHeart.setImageResource(R.drawable.ic_sym_health);
		} else {
			mHeart = getResources().getString(R.string.bao);
			if (bao < 5) {
				mHeartTwo = getResources().getString(R.string.loadbit);
				mImageHeart.setImageResource(R.drawable.ic_sym_normal);
			} else if (bao < 10) {
				mHeartTwo = getResources().getString(R.string.loadbad);
				mImageHeart.setImageResource(R.drawable.ic_sym_medium);
			} else {
				mHeartTwo = getResources().getString(R.string.loadshit);
				mImageHeart.setImageResource(R.drawable.ic_sym_bad);
			}
		}
	}

	/**
	 * GetRate_gradeServicece的handler
	 */
	public static Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			mData = (List<HashMap<String, Object>>) msg.obj;
			// 在此画图
			String[] X = new String[] { "1", "2", "3", "4", "5", "6", "7", "8",
					"9", "10" };
			String[] Y = new String[] { "", "25", "50", "75", "100" };
			String[] DATA = new String[] { "", "", "", "", "", "", "", "", "",
					"" };
			for (int i = 0; i < mData.size() - 1; i++) {
				DATA[i] = (String) mData.get(mData.size() - 2 - i).get(
						"rate_grade");
			}
			DATA[DATA.length - 1] = mResultData.getAverageHeart_rate() + "";
			X[DATA.length - 1] = "本次";
			// X轴刻度，Y轴刻度， 数据
			mMyView.SetInfo(X, Y, DATA, "图标的标题");
		}
	};

	/**
	 * @description 时间线程类
	 * @author Guan
	 * @date 2015-6-9 下午10:15:34
	 * @version 1.0
	 */
	public class TimeThread extends Thread {

		public TimeThread() {
			super();
		}

		@SuppressLint("SimpleDateFormat")
		public void run() {
			try {
				while (true) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy年MM月dd日   HH:mm:ss");
					String str = sdf.format(new Date());
					mTimeHandler.sendMessage(mTimeHandler.obtainMessage(100,
							str));
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 播报测量报告handler
	 */
	@SuppressLint("HandlerLeak")
	Handler SpeechHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mRelativeOnclick.performClick();
		}
	};

	/**
	 * @description 播报测量报告
	 * @author Guan
	 * @date 2015-6-9 下午10:15:56
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_onclick)
	void SpeechClickListener(View v) {
		mMTts.startSpeaking(
				"测试完毕，播报测试报告。心率为" + mResultData.getAverageHeart_rate()
						+ "，心律病症为" + mSymptoms.toString() + "，心房病症" + mHeart,
				Speech.mTtsListener);
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MeasureActivity.sActivity.finish();
			this.finish();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			MeasureActivity.sActivity.finish();
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
			MeasureActivity.sActivity.finish();
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("SendResult");
		App.getHttpQueue().cancelAll("GetRate");
		App.getHttpQueue().cancelAll("AbnormalNotice");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("SendResult");
		App.getHttpQueue().cancelAll("GetRate");
		App.getHttpQueue().cancelAll("AbnormalNotice");
	}
}
