/**
 * @file FamilyFragment.java
 * @description 家人群碎片
 * @author Guan
 * @date 2015-6-8 下午10:00:18 
 * @version 1.0
 */
package cn.heart.fragment;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import cn.heart.app.App;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.net.ImageUploadHttp;
import cn.heart.net.VolleyFamilyhttp;
import cn.heart.service.adapter.GridAdapter;

import com.example.wechatsample.R;

/**
 * @description 家人群碎片
 * @author Guan
 * @date 2015-6-8 下午10:00:18
 * @version 1.0
 */
@SuppressLint({ "SdCardPath", "WorldReadableFiles", "InflateParams" })
public class GroupFragment extends Fragment {

	private View mView;
	private Bitmap mBitmap;
	private ImageView mHomeImage;
	private PopupWindow mWindow = null;
	private SharedPreferences mPreferences;
	private static GridView mGrid;
	private static String mUserString;
	private static Callbacks sCallbacks;
	private static List<HashMap<String, Object>> objects;
	public RelativeLayout relativeLayout, uploadPhoto, uploadAlbum;
	public static Context sContext;
	public static final int sRESULT_OK = -1;
	public static final int sRESULT_FIRST_USER = 1;

	/**
	 * @description 用来存放fragment的Activtiy必须实现的接口
	 * @author Guan
	 * @date 2015-6-8 下午10:05:28
	 * @version 1.0
	 */
	public interface Callbacks {
		public void intent_M_O(List<HashMap<String, Object>> data, int position);
	}

	/**
	 * @description onCreate
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * @description 获取布局文件ID
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getActivity().getLayoutInflater();
		View view = inflater
				.inflate(R.layout.fragment_family, container, false);
		return view;
	}

	/**
	 * @description 实例化资源
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		sContext = getActivity();
		mGrid = (GridView) getActivity().findViewById(R.id.gv_user);
		mHomeImage = (ImageView) getActivity().findViewById(R.id.iv_home);
		mPreferences = getActivity().getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			mUserString = mPreferences.getString("USER_NAME", "");
		}
		mHomeImage.setOnLongClickListener(new OnLongListener());

		ImageDownLoader mImageDownLoader = new ImageDownLoader(sContext);
		final String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUserString + "home.jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			mHomeImage.setImageBitmap(bitmap);
		// 获取服务器家人信息
		VolleyFamilyhttp.VolleyAllfamily(sContext, "GroupFragment",
				mUserString);
	}

	/**
	 * @description handler
	 */
	public static Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			objects = (List<HashMap<String, Object>>) msg.obj;
			mGrid.setAdapter(new GridAdapter(sContext, objects, mUserString,sCallbacks));
		}
	};
	
	/**
	 * @description 当该Fragment被添加到Activity或者显示时被回调
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常
		try {
			sCallbacks = (Callbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	/**
	 * @description 长按更换照片触发
	 * @author Guan
	 * @date 2015-6-8 下午10:07:35
	 * @version 1.0
	 */
	private class OnLongListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			showPopupwindow(v);
			return false;
		}
	}

	/**
	 * @description popupwindow实现
	 * @param parent
	 */
	@SuppressWarnings("deprecation")
	private void showPopupwindow(View parent) {
		
		if (mWindow == null) {
			mView = LayoutInflater.from(getActivity()).inflate(
					R.layout.ppw_modify_image, null);
			mView.setFocusable(true); // 这个很重要
			mView.setFocusableInTouchMode(true);
			uploadPhoto = (RelativeLayout) mView
					.findViewById(R.id.rlyt_upload_photo);
			uploadAlbum = (RelativeLayout) mView
					.findViewById(R.id.rlyt_upload_album);
			mWindow = new PopupWindow(mView, 630, 240);
		}
		mWindow.setAnimationStyle(R.style.popwin_anim_style);
		mWindow.setFocusable(true);
		mWindow.setBackgroundDrawable(new BitmapDrawable());
		backgroundAlpha(0.5f);
		// 添加pop窗口关闭事件
		mWindow.setOnDismissListener(new popupDismissListener());
		// 重写onKeyListener
		mView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					mWindow.dismiss();
					backgroundAlpha(1f);
					return true;
				}
				return false;
			}
		});
		mWindow.update();
		mWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		uploadPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(), mUserString
								+ "home" + ".jpg")));
				// 采用ForResult打开
				startActivityForResult(intent2, 2);
				mWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
		uploadAlbum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 1);
				mWindow.dismiss();
				backgroundAlpha(1f);
			}
		});
	}

	/**
	 * @description 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getActivity().getWindow().setAttributes(lp);
	}

	/**
	 * @description 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * @author Guan
	 * @date 2015-6-8 下午10:08:16
	 * @version 1.0
	 */
	public class popupDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	/**
	 * @description 调用系统功能
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == sRESULT_OK) {
				// 裁剪图片
				cropPhoto(data.getData());
			}
			break;
		case 2:
			if (resultCode == sRESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/" + mUserString + "home.jpg");
				// 裁剪图片
				cropPhoto(Uri.fromFile(temp));
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				mBitmap = extras.getParcelable("data");
				if (mBitmap != null) {
					// 保存家庭照在SD卡并上传头像到服务器
					ImageUploadHttp.PreserImage(mBitmap, mUserString
							+ "home.jpg");
					// 用ImageView显示出来
					mHomeImage.setImageBitmap(mBitmap);
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	/**
	 * @description 调用系统的裁剪
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 5);
		intent.putExtra("aspectY", 3);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 560);
		intent.putExtra("outputY", 327);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	@Override
	public void onDestroy() {
		App.getHttpQueue().cancelAll("familylist");
		super.onDestroy();
	}

	@Override
	public void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("familylist");
	}
}
