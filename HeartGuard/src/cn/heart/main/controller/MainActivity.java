/**
 * @file MainActivity.java
 * @description 主Activity 
 * @author Guan
 * @date 2015-6-8 下午7:53:33 
 * @version 1.0
 */
package cn.heart.main.controller;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.app.App;
import cn.heart.config.Constant;
import cn.heart.fragment.GroupFragment;
import cn.heart.fragment.MeasureFragment;
import cn.heart.fragment.ResultFragment;
import cn.heart.net.ImageDownLoader;
import cn.heart.net.ImageDownLoader.onImageLoaderListener;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.service.adapter.MainPagerAdapter;
import cn.heart.service.adapter.UserListAdapter;
import cn.heart.utils.ActivityManager;
import cn.heart.utils.Speech;
import cn.heart.utils.SpellHelper;
import cn.heart.view.PagerSlidingTabStrip;

import com.example.wechatsample.R;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * @description 主Activity
 * @author Guan
 * @date 2015-6-8 下午8:18:23
 * @version 1.0
 */
@SuppressLint({ "SdCardPath", "WorldReadableFiles", "InflateParams" })
public class MainActivity extends FragmentActivity implements
		MeasureFragment.Callbacks, ResultFragment.Callbacks,
		GroupFragment.Callbacks {

	/**
	 * 聊天界面的Fragment
	 */
	private MeasureFragment mMeasureFragment;

	/**
	 * 发现界面的Fragment
	 */
	private ResultFragment mResultFragment;

	/**
	 * 通讯录界面的Fragment
	 */
	private GroupFragment mInfoFragment;

	/**
	 * PagerSlidingTabStrip的实例
	 */
	public static PagerSlidingTabStrip sTabs;

	@InjectView(R.id.rlyt_main)
	RelativeLayout sMainRelative;

	private View mView;
	private DisplayMetrics mDm;
	private SpeechSynthesizer mMTts;
	private SharedPreferences mPreferences;
	private boolean isExit = false; // 判断退出
	private static String sUserString;
	private static ViewPager sPager;
	private static ListView sListView = null;
	private static PopupWindow sWindow = null;
	private static SharedPreferences.Editor sEditor;
	private static ArrayList<String> sImageThumbUrls;
	/**
	 * Image 下载器
	 */
	private static ImageDownLoader sImageDownLoader;
	private static List<HashMap<String, Object>> sObjects;
	public static MainActivity sActivity;
	public static Context sContext;

	// 从MeasureFrament经由MainActivity向InfoFragment传递消息，且此Fragment必须实现的接口
	public interface sendMessage {
		public void send(String string);
	}

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		setOverflowShowingAlways();
		// 初始化
		Init();
		// 功能化
		Initivite();
	}

	/**
	 * @description 初始化并从本地和网络获取数据
	 * @return void
	 */
	public void Init() {

		mDm = getResources().getDisplayMetrics();
		sImageThumbUrls = new ArrayList<String>();
		sObjects = new ArrayList<HashMap<String, Object>>();
		sPager = (ViewPager) findViewById(R.id.vp_pager);
		sTabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);
		mMeasureFragment = new MeasureFragment();
		mResultFragment = new ResultFragment();
		mInfoFragment = new GroupFragment();
		// 调用适配器获取当前的页面
		sPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
				mMeasureFragment, mResultFragment, mInfoFragment));
		sTabs.setViewPager(sPager);
		setTabsValue();
		// 选择从第几个Item开始
		// pager.setCurrentItem(0);
	}

	/**
	 * @description 功能化
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	private void Initivite() {

		sActivity = MainActivity.this;
		sContext = MainActivity.this;
		ActivityManager.getInstance().addActivity(this);
		sImageDownLoader = new ImageDownLoader(sContext);
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		sEditor = mPreferences.edit();
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserString = mPreferences.getString("USER_NAME", "");
		}

		SpeechUtility.createUtility(this, SpeechConstant.APPID + "53c4c169");
		mMTts = SpeechSynthesizer.createSynthesizer(this, null);
		Speech.set_mTts(mMTts);
		// 获取服务器家人信息
		VolleyFamilyhttp.VolleyAllfamily(sContext, "MainActivity", sUserString);
		Message popupmsg = new Message();
		popupHandler.sendMessageDelayed(popupmsg, 50);
	}

	/**
	 * @description 对PagerSlidingTabStrip的各项属性进行赋值
	 */
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		sTabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		sTabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		sTabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, mDm));
		// 设置Tab Indicator的高度
		sTabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 3, mDm));
		// 设置Tab标题文字的大小
		sTabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 16, mDm));
		// 设置点击Tab的颜色
		sTabs.setIndicatorColor(Color.parseColor("#6495ed"));
		// 取消点击Tab时的背景色
		sTabs.setTabBackground(0);
	}

	/**
	 * @description 弹出popupWindow监听
	 * @author Guan
	 * @date 2015-6-8 下午8:21:03
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_main)
	void popupWindowClickListener(View v) {
		showPopupwindow(v);
	}

	/**
	 * @description popupwindow实现
	 * @param parent
	 */
	@SuppressWarnings("deprecation")
	private void showPopupwindow(View parent) {

		if (sWindow == null) {
			mView = LayoutInflater.from(this).inflate(R.layout.ppw_listview,
					null);
			// 这个很重要
			mView.setFocusable(false);
			mView.setFocusableInTouchMode(true);
			sListView = (ListView) mView.findViewById(R.id.popupListView);
			sWindow = new PopupWindow(mView, LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
		}
		sWindow.setFocusable(false);
		sWindow.setBackgroundDrawable(new BitmapDrawable());
		sWindow.update();
		sWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
	}

	/**
	 * @description 下拉菜单 onCreateOptionsMenu()方法中去加载main.xml文件
	 * @param menu
	 * @return boolean
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_tab, menu);
		MenuItem familyitem = menu.findItem(R.id.family).setChecked(true);
		familyitem.setOnMenuItemClickListener(new OnMenuItemClickListener());
		return true;
	}

	/**
	 * @description onMenuOpened()方法用于让隐藏在overflow当中的Action按钮的图标显示出来
	 * @param featureId
	 * @param menu
	 * @return boolean
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	/**
	 * @description setOverflowShowingAlways()方法则是屏蔽掉物理Menu键
	 *              不然在有物理Menu键的手机上，overflow按钮会显示不出来
	 */
	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退出的handler
	 */
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * 弹出popupwindow的handler
	 */
	@SuppressLint("HandlerLeak")
	Handler popupHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			sMainRelative.performClick();
		}
	};

	/**
	 * 获取用户信息的handler
	 */
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	/**
	 * 获取用户名handler
	 */
	public static Handler popuplisthandler = new Handler() {

		@SuppressWarnings({ "unchecked" })
		public void handleMessage(Message msg) {
			sObjects = (List<HashMap<String, Object>>) msg.obj;
			for (HashMap<String, Object> data : sObjects) {
				sImageThumbUrls.add(Constant.httpUrl
						+ "DownloadFileServlet?file=" + sUserString
						+ SpellHelper.getEname((String) data.get("nick"))
						+ ".jpg");
			}
			sImageThumbUrls.add(Constant.httpUrl + "DownloadFileServlet?file="
					+ sUserString + "home.jpg");
			// 缓存所有头像
			DownAllImage(sImageThumbUrls);
			sListView.setAdapter(new UserListAdapter(sActivity, sObjects,
					sUserString, sEditor, sWindow, sListView));
		}
	};

	/**
	 * @description 缓存所有图片，先会去查找LruCache， LruCache没有就去sd卡或者手机目录查找，没有就开启线程去下载
	 * @param imageThumbUrls
	 */
	private static void DownAllImage(ArrayList<String> imageThumbUrls) {

		for (int i = 0; i < imageThumbUrls.size(); i++) {
			String mImageUrl = imageThumbUrls.get(i);
			// 异步加载图片
			sImageDownLoader.downloadImage(mImageUrl,
					new onImageLoaderListener() {
						@Override
						public void onImageLoader(Bitmap bitmap, String url) {
						}
					});
		}
	}

	/**
	 * @description 菜单项触发监听
	 * @author Guan
	 * @date 2015-6-8 下午8:41:14
	 * @version 1.0
	 */
	public class OnMenuItemClickListener implements
			android.view.MenuItem.OnMenuItemClickListener {

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			startActivity(new Intent(MainActivity.this, ManageSetActivity.class));
			overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
			return false;
		}
	}

	/**
	 * @description 实现MessageFragment接口回调消息方法intent_M_E
	 */
	@Override
	public void intent_M_E() {
		startActivity(new Intent(MainActivity.this, MeasureActivity.class));
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
		mMTts.startSpeaking("开始测量，请保持平和的心态", Speech.mTtsListener);
	}

	/**
	 * @description 实现ResultFragment接口回调消息方法intent_R_S
	 */
	@Override
	public void intent_R_S(List<HashMap<String, Object>> object, int position) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ShowActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("object", (Serializable) object);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		MainActivity.this.startActivity(intent);
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 实现GroupFragment接口回调消息方法intent_M_O
	 */
	@Override
	public void intent_M_O(List<HashMap<String, Object>> data, int position) {
		Intent intent = new Intent();
		intent.setClass(sContext, UserDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("activity", "UserListActivity");
		bundle.putSerializable("object", (Serializable) data);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		sContext.startActivity(intent);
//		((Activity) sContext).finish();
		((Activity) sContext).overridePendingTransition(R.anim.out_from_right,
				R.anim.out_to_left);

	}

	/**
	 * @description 退出监听
	 * @return boolean
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			exit();
		}
		return isExit;
	}

	/**
	 * @description 退出的实现
	 */
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	@Override
	protected void onDestroy() {
		App.getHttpQueue().cancelAll("familylist");
		App.getHttpQueue().cancelAll("UserDetail");
		mMTts.stopSpeaking();
		mMTts.destroy();// 退出时释放连接
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("familylist");
		App.getHttpQueue().cancelAll("UserDetail");
	}

}
