/**
 * @file OneDetailActivity.java
 * @description 个人信息Activity类
 * @author Guan
 * @date 2015-6-9 下午4:30:58 
 * @version 1.0
 */
package cn.heart.main.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.app.App;
import cn.heart.config.Common;
import cn.heart.config.Constant;
import cn.heart.main.activity.FamilyListActivity;
import cn.heart.net.ImageDownLoader;
import cn.heart.net.ImageUploadHttp;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.SpellHelper;
import cn.heart.view.CircleImageView;
import cn.heart.view.MyPopupWindow;
import cn.heart.view.SwitchView;
import cn.heart.view.SwitchView.OnCheckedChangeListener;

import com.example.wechatsample.R;

/**
 * @description 个人信息Activity类
 * @author Guan
 * @date 2015-6-9 下午4:30:58
 * @version 1.0
 */
public class UserDetailActivity extends Activity {

	@InjectView(R.id.civ_head_aud)
	CircleImageView mCircleHead;
	@InjectView(R.id.tv_user_aud)
	TextView mUser;
	@InjectView(R.id.tv_nick_aud)
	TextView mNick;
	@InjectView(R.id.tv_birth_aud)
	TextView mBirth;
	@InjectView(R.id.tv_high_aud)
	TextView mHigh;
	@InjectView(R.id.tv_weight_aud)
	TextView mWeight;
	@InjectView(R.id.btn_delete)
	Button mDelete;

	@InjectView(R.id.rlyt_head_aud)
	RelativeLayout mHeadRelative;
	@InjectView(R.id.rlyt_nick_aud)
	RelativeLayout mNickRelative;
	@InjectView(R.id.rlyt_gender_aud)
	RelativeLayout mGenderRelative;
	@InjectView(R.id.rlyt_birth_aud)
	RelativeLayout mBirthRelative;
	@InjectView(R.id.rlyt_high_aud)
	RelativeLayout mHighRelative;
	@InjectView(R.id.rlyt_weight_aud)
	RelativeLayout mWeightRelative;
	@InjectView(R.id.llyt_gender_aud)
	LinearLayout mLinearSView;

	private Bitmap mBitmap;;
	private int mPosition;
	private String mImageUrl;
	private View mFamilyview;
	private SwitchView mSwitchView;
	private ActionBar mActionBar;
	private SharedPreferences mPreferences;
	private PopupWindow mFamilywindow = null;
	private RelativeLayout mUploadPhoto, mUploadAlbum;
	private List<HashMap<String, Object>> mData;
	private static ProgressDialog sProgressDialog;
	private static String sSwitchActivity, sUserString, sNickString,
			sGenderString, sBirthString, sHighString, sWeightString, sNicked,
			sFamilyGroup;
	public UserDetailActivity activity;
	public static Context sContext;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_detail);

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
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings({ "deprecation", "unchecked" })
	private void Init() {

		Intent intent = this.getIntent();
		// 选择是哪一个activity功能
		sSwitchActivity = intent.getExtras().getString("activity");
		if (sSwitchActivity.equals("UserListActivity")) {
			mData = new ArrayList<HashMap<String, Object>>();
			mData = (List<HashMap<String, Object>>) intent
					.getSerializableExtra("object");
			mPosition = intent.getExtras().getInt("position");
		}
		mPreferences = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		mPreferences.edit();
		sContext = UserDetailActivity.this;
		activity = UserDetailActivity.this;

		// 初始化数据
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserString = mPreferences.getString("USER_NAME", "");
			switch (sSwitchActivity) {
			case "UserListActivity":
				sNickString = (String) mData.get(mPosition).get("nick");
				sGenderString = (String) mData.get(mPosition).get("gender");
				sBirthString = (String) mData.get(mPosition).get("birthday");
				sHighString = (String) mData.get(mPosition).get("high");
				sWeightString = (String) mData.get(mPosition).get("weight");
				sFamilyGroup = (String) mData.get(mPosition).get("familygroup");
				break;
			case "AddFamilyActivity":
				sNickString = "亲友";
				sGenderString = "男";
				sBirthString = "1970年1月1日";
				sHighString = "170cm";
				sWeightString = "60kg";
				sFamilyGroup = "否";
				break;
			}
		}
	}

	/**
	 * @description 功能化
	 */
	private void Initivite() {

		// 将未修改的nick传给nicked
		sNicked = sNickString;
		mUser.setText(sUserString);
		mNick.setText(sNickString);
		mBirth.setText(sBirthString);
		mHigh.setText(sHighString);
		mWeight.setText(sWeightString);
		mSwitchView = new SwitchView(this, SwitchState(), "gender");
		mSwitchView
				.setOnCheckedChangeListener(new OnSwitchCheckedChangeListener());
		mLinearSView.addView(mSwitchView);

		switch (sSwitchActivity) {
		case "UserListActivity":
			if (sFamilyGroup.equals("是")) {
				mDelete.setVisibility(View.GONE);
			}
			if (((String) mData.get(mPosition).get("nick")).equals(mPreferences
					.getString("NICK", ""))) {
				// 当前用户
				mDelete.setVisibility(View.GONE);
			}
			// 从缓存获取图片
			ImageDownLoader mImageDownLoader = new ImageDownLoader(sContext);
			mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
					+ sUserString + SpellHelper.getEname(sNickString) + ".jpg";
			// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
			Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl
					.replaceAll("[^\\w]", ""));
			if (bitmap != null)
				mCircleHead.setImageBitmap(bitmap);
			break;
		case "AddFamilyActivity":
			mDelete.setVisibility(View.GONE);
			Resources res = sContext.getResources();
			Bitmap bitmaps = BitmapFactory.decodeResource(res,
					R.drawable.ic_head);
			if (bitmaps != null)
				mCircleHead.setImageBitmap(bitmaps);
			break;
		}
	}

	/**
	 * @description 更换头像
	 * @author Guan
	 * @date 2015-6-9 下午5:02:39
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_head_aud)
	void Relative_headClickListener(View v) {
		ModifyFamilyPhotoPopup(v);
	}

	/**
	 * @description 更换昵称
	 * @author Guan
	 * @date 2015-6-9 下午6:16:11
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_nick_aud)
	void Relative_nickClickListener(View v) {
		MyPopupWindow
				.ModifyNickPopup(v, sNickString, mNick, sContext, activity);
	}

	/**
	 * @description 更换性别
	 * @author Guan
	 * @date 2015-6-9 下午6:16:37
	 * @version 1.0
	 */
	public class OnSwitchCheckedChangeListener implements
			OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(boolean isChecked) {
			sGenderString = getState(isChecked);
		}
	}

	/**
	 * @description 更换生日
	 * @author Guan
	 * @date 2015-6-9 下午6:16:48
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_birth_aud)
	void Relative_birthdayClickListener(View v) {

		DatePickerDialog datePicker = new DatePickerDialog(
				UserDetailActivity.this, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						sBirthString = year + "年" + (monthOfYear + 1) + "月"
								+ dayOfMonth + "日";
						mBirth.setText(sBirthString);
						Common.Toast(UserDetailActivity.this, sBirthString);
					}
				}, 1970, 1, 1);

		datePicker.show();
	}

	/**
	 * @description 更换身高
	 * @author Guan
	 * @date 2015-6-9 下午6:17:01
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_high_aud)
	void Relative_highClickListener(View v) {
		MyPopupWindow
				.ModifyHighPopup(v, sHighString, mHigh, sContext, activity);
	}

	/**
	 * @description 更换体重
	 * @author Guan
	 * @date 2015-6-9 下午6:17:12
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_weight_aud)
	void Relative_weightClickListener(View v) {
		MyPopupWindow.ModifyWeightPopup(v, sWeightString, mWeight, sContext,
				activity);
	}

	/**
	 * @description 删除此家人
	 * @author Guan
	 * @date 2015-6-9 下午6:17:27
	 * @version 1.0
	 */
	@OnClick(R.id.btn_delete)
	void DeleteFamilyClickListener(View v) {

		// 显示ProgressDialog
		sProgressDialog = ProgressDialog.show(UserDetailActivity.this,
				"正在删除.....", "请等待.....", true, false);
		// 新建线程
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					// 删除家人服务器请求
					VolleyFamilyhttp.VolleyDeleteFamily(sContext,
							sProgressDialog, sUserString, sNickString);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * @description 实现更换头像
	 * @param parent
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void ModifyFamilyPhotoPopup(View parent) {
		if (mFamilywindow == null) {
			mFamilyview = LayoutInflater.from(this).inflate(
					R.layout.ppw_modify_image, null);
			mFamilyview.setFocusable(true); // 这个很重要
			mFamilyview.setFocusableInTouchMode(true);
			mUploadPhoto = (RelativeLayout) mFamilyview
					.findViewById(R.id.rlyt_upload_photo);
			mUploadAlbum = (RelativeLayout) mFamilyview
					.findViewById(R.id.rlyt_upload_album);
			mFamilywindow = new PopupWindow(mFamilyview, 630, 240);
		}
		mFamilywindow.setAnimationStyle(R.style.popwin_anim_style);
		mFamilywindow.setFocusable(true);
		mFamilywindow.setBackgroundDrawable(new BitmapDrawable());
		backgroundAlpha(0.5f);
		// 添加pop窗口关闭事件
		mFamilywindow.setOnDismissListener(new popupDismissListener());
		// 重写onKeyListener
		mFamilyview.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					mFamilywindow.dismiss();
					backgroundAlpha(1f);
					return true;
				}
				return false;
			}
		});
		mFamilywindow.update();
		mFamilywindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		mUploadPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), sUserString
								+ SpellHelper.getEname(sNickString) + ".jpg")));
				// 采用ForResult打开
				startActivityForResult(intent2, 2);
				mFamilywindow.dismiss();
				backgroundAlpha(1f);
			}
		});
		mUploadAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 1);
				mFamilywindow.dismiss();
				backgroundAlpha(1f);
			}
		});
	}

	/**
	 * @description 调用系统功能
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				// 裁剪图片
				cropPhoto(data.getData());
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/" + sUserString + SpellHelper.getEname(sNickString)
						+ ".jpg");
				// 裁剪图片
				cropPhoto(Uri.fromFile(temp));
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				mBitmap = extras.getParcelable("data");
				if (mBitmap != null) {
					// 保存头像并上传头像到服务器
					ImageUploadHttp.PreserImage(mBitmap, sUserString
							+ SpellHelper.getEname(sNickString) + ".jpg");
					// 存入缓存中
//					ImageDownLoader.addBitmapToMemory(mImageUrl, mBitmap);
					mCircleHead.setImageBitmap(mBitmap);
				}
			} else {
				Toast.makeText(sContext, data + ":为空", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	/**
	 * @description 下拉菜单 onCreateOptionsMenu()方法中去加载main.xml文件(non-Javadoc)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.onedetail_tab, menu);
		MenuItem familyitem = menu.findItem(R.id.check).setChecked(true);
		familyitem.setOnMenuItemClickListener(new OnMenuItemClickListener());
		return true;
	}

	/**
	 * @description 菜单项触发监听
	 * @author Guan
	 * @date 2015-6-9 下午6:18:46
	 * @version 1.0
	 */
	public class OnMenuItemClickListener implements
			android.view.MenuItem.OnMenuItemClickListener {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (sSwitchActivity) {
			// case "CurrentUserActivity":
			case "UserListActivity":
				// 修改家人信息
				ModifyFamily();
				break;
			case "AddFamilyActivity":
				// 添加家人
				AddFamily();
				break;
			}
			return false;
		}
	}

	/**
	 * @description 添加家人消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 关闭ProgressDialog
			sProgressDialog.dismiss();
			if (msg.what == 0x111) {
				Common.Toast(sContext, "添加家人成功");
			} else {
				Common.Toast(sContext, "添加家人失败");
			}
			((Activity) sContext).finish();
			sContext.startActivity(new Intent(sContext, ManageSetActivity.class));
		}
	};

	/**
	 * @description 更改家人消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler modifyHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 关闭ProgressDialog
			sProgressDialog.dismiss();
			if (msg.what == 0x134) {
				Common.Toast(sContext, "更改信息成功");
			} else {
				Common.Toast(sContext, "更改信息失败");
			}
			((Activity) sContext).finish();
			sContext.startActivity(new Intent(sContext, FamilyListActivity.class));
		}
	};

	/**
	 * @description 删除家人消息处理
	 */
	@SuppressLint("HandlerLeak")
	public static Handler deleteHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 关闭ProgressDialog
			sProgressDialog.dismiss();
			if (msg.what == 0x101) {
				((Activity) sContext).finish();
				FamilyListActivity.sActivity.finish();
				sContext.startActivity(new Intent(sContext,
						FamilyListActivity.class));
				Common.Toast(sContext, "删除家人成功");
			} else {
				Common.Toast(sContext, "删除家人失败");
			}
		}
	};

	/**
	 * @description 裁剪图片
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * @description 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		// 0.0-1.0
		lp.alpha = bgAlpha;
		this.getWindow().setAttributes(lp);
	}

	/**
	 * @description 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * @author Guan
	 * @date 2015-6-9 下午6:20:39
	 * @version 1.0
	 */
	public class popupDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	/**
	 * @description 设置滑动控件的状态
	 * @param state
	 * @return
	 */
	private String getState(boolean state) {
		if (state) {
			return "男";
		}
		return "女";
	}

	/**
	 * @description SwitchView
	 * @return boolean
	 */
	private boolean SwitchState() {
		switch (sSwitchActivity) {
		case "UserListActivity":
			if (sGenderString.equals("女")) {
				return false;
			} else {
				return true;
			}
		case "AddFamilyActivity":
			return true;
		}
		return false;
	}

	/**
	 * @description 修改家人信息线程
	 */
	public void ModifyFamily() {
		sNickString = mNick.getText().toString();
		sHighString = mHigh.getText().toString();
		sWeightString = mWeight.getText().toString();
		// 显示ProgressDialog
		sProgressDialog = ProgressDialog.show(UserDetailActivity.this,
				"正在修改.....", "请等待.....", true, false);

		// 新建线程
		new Thread() {
			@Override
			public void run() {
				try {
					// 睡眠2秒
					Thread.sleep(2000);
					if (sFamilyGroup.equals("是")) {
						VolleyUserhttp.VolleyModifyUser(sContext, sUserString,
								sNickString, sGenderString, sBirthString,
								sHighString, sWeightString);
						// 更改家人信息服务器请求
						VolleyFamilyhttp.VolleyModifyFamily(sContext,
								sProgressDialog, sUserString, sNickString,
								sGenderString, sBirthString, sHighString,
								sWeightString, sNicked);
					} else {
						// 更改家人信息服务器请求
						VolleyFamilyhttp.VolleyModifyFamily(sContext,
								sProgressDialog, sUserString, sNickString,
								sGenderString, sBirthString, sHighString,
								sWeightString, sNicked);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * @description 添加家人
	 */
	public void AddFamily() {

		// 显示ProgressDialog
		sProgressDialog = ProgressDialog.show(UserDetailActivity.this,
				"正在添加.....", "请等待.....", true, false);
		// 新建线程
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					// 获取服务器添加家人请求
					VolleyFamilyhttp.VolleyAddFamily(sContext,
							"OneDetailActivity", sUserString, mNick.getText()
									.toString(), sGenderString, sBirthString,
							mHigh.getText().toString(), mWeight.getText()
									.toString(), "否");
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
		Intent intent = new Intent();
		switch (sSwitchActivity) {
		case "UserListActivity":
//			intent.setClass(UserDetailActivity.this, FamilyListActivity.class);
			this.finish();
			break;
		case "AddFamilyActivity":
			intent.setClass(UserDetailActivity.this,
					ManageSetActivity.class);
			StartActivity(intent);
			break;
		}
		return false;
	}
	
	/**
	 * @description home图标返回监听
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent();
			switch (sSwitchActivity) {
			case "UserListActivity":
				intent.setClass(UserDetailActivity.this, FamilyListActivity.class);
				this.finish();
				break;
			case "AddFamilyActivity":
				intent.setClass(UserDetailActivity.this,
						ManageSetActivity.class);
				StartActivity(intent);
				break;
			}
			break;
		}
		return false;
	}

	
	/**
	 * @description Activity跳转
	 */
	private void StartActivity(Intent intent) {
		startActivity(intent);
		this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
	}
	
	/**
	 * @description 消除volley请求队列
	 */
	public void CancelAll() {
		App.getHttpQueue().cancelAll("DeleteFamily");
		App.getHttpQueue().cancelAll("ModifyUser");
		App.getHttpQueue().cancelAll("ModifyFamily");
	}
	
	@Override
	protected void onDestroy() {
		CancelAll();
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		CancelAll();
		super.onStop();
	}
}
