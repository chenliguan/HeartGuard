/**
 * @file MyPopupWindow.java
 * @description PopupWindow方法类
 * @author Guan
 * @date 2015-6-10 下午3:32:21 
 * @version 1.0
 */
package cn.heart.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.heart.config.Common;
import cn.heart.config.Method;
import cn.heart.net.VolleyUserhttp;
import cn.heart.utils.Check;

import com.example.wechatsample.R;

/**
 * @description PopupWindow方法类
 * @author Guan
 * @date 2015-6-10 下午3:32:21
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class MyPopupWindow {

	private static Button mOk, mUndo;
	private static EditText mFindUser;
	private static View mView, sNickView, sWeightView, sHighView;
	private static EditText sPopupNick, sPopupHigh, sPopupWeight;
	private static PopupWindow mWindow = null, sNickWindow = null,
			sHighWindow = null, sWeightWindow = null;
	public static RelativeLayout sUploadPhoto, sUploadAlbum, sUndo, sOk;

	/**
	 * @description FindUserNamePopup实现
	 * @param parent
	 */
	@SuppressLint("InflateParams")
	public static void FindUserNamePopup(View parent, final Context context,
			final Activity activity) {
		if (mWindow == null) {
			mView = LayoutInflater.from(context).inflate(R.layout.ppw_getpass,
					null);
			mView.setFocusable(true);
			mView.setFocusableInTouchMode(true);
			mOk = (Button) mView.findViewById(R.id.btn_ok);
			mUndo = (Button) mView.findViewById(R.id.btn_undo);
			mFindUser = (EditText) mView.findViewById(R.id.et_find_user);
			mWindow = new PopupWindow(mView, 630, 300);
		}
		mWindow.setAnimationStyle(R.style.popwin_anim_style);
		mWindow.setFocusable(true);
		Method.backgroundAlpha(0.5f, activity);
		// 添加pop窗口关闭事件
		mWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Method.backgroundAlpha(1f, activity);
			}
		});
		// 重写onKeyListener
		mView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					mWindow.dismiss();
					Method.backgroundAlpha(1f, activity);
					return true;
				}
				return false;
			}
		});
		mWindow.update();
		mWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);

		mOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sUserString = mFindUser.getText().toString();
				String checkresult = Check.CodeCheck(sUserString);
				if (checkresult.equals("")) {
					// 获取服务器密码请求
					VolleyUserhttp.VolleyPassword(context, sUserString);
				} else {
					Common.Toast(context, checkresult);
				}
				mWindow.dismiss();
				Method.backgroundAlpha(1f, activity);
			}
		});
		mUndo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWindow.dismiss();
				Method.backgroundAlpha(1f, activity);
			}
		});
	}

	/**
	 * @description 更改昵称
	 * @param parent
	 * @param nickstring
	 * @param nick
	 * @param context
	 * @param activity
	 */
	@SuppressWarnings({ "deprecation" })
	public static void ModifyNickPopup(View parent, final String nickstring,
			final TextView nick, Context context, final Activity activity) {
		if (sNickWindow == null) {
			sNickView = LayoutInflater.from(context).inflate(
					R.layout.ppw_modify_nick, null);
			// 这个很重要
			sNickView.setFocusable(true);
			sNickView.setFocusableInTouchMode(true);
			sOk = (RelativeLayout) sNickView.findViewById(R.id.ok);
			sPopupNick = (EditText) sNickView.findViewById(R.id.modifynick);
			sNickWindow = new PopupWindow(sNickView, 630, 285);
		}
		sPopupNick.setText(nickstring);
		sPopupNick.setSelection(nickstring.length());
		sNickWindow.setAnimationStyle(R.style.popwin_anim_style);
		sNickWindow.setFocusable(true);
		sNickWindow.setBackgroundDrawable(new BitmapDrawable());
		Method.backgroundAlpha(0.5f, activity);
		// 添加pop窗口关闭事件
		sNickWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Method.backgroundAlpha(1f, activity);
			}
		});
		// 重写onKeyListener
		sNickView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					sNickWindow.dismiss();
					Method.backgroundAlpha(1f, activity);
					return true;
				}
				return false;
			}
		});
		sNickWindow.update();
		sNickWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		sOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// nickstring = popupnick.getText().toString();
				nick.setText(sPopupNick.getText().toString());
				sNickWindow.dismiss();
				Method.backgroundAlpha(1f, activity);
			}
		});
	}

	/**
	 * @description 更改身高
	 * @param parent
	 * @param highstring
	 * @param high
	 * @param context
	 * @param activity
	 */
	@SuppressWarnings("deprecation")
	public static void ModifyHighPopup(View parent, final String highstring,
			final TextView high, Context context, final Activity activity) {
		if (sHighWindow == null) {
			sHighView = LayoutInflater.from(context).inflate(
					R.layout.ppw_modify_high, null);
			// 这个很重要
			sHighView.setFocusable(true);
			sHighView.setFocusableInTouchMode(true);
			sOk = (RelativeLayout) sHighView.findViewById(R.id.ok);
			sPopupHigh = (EditText) sHighView.findViewById(R.id.modifyhigh);
			sHighWindow = new PopupWindow(sHighView, 630, 285);
		}
		sPopupHigh.setText(highstring);
		sPopupHigh.setSelection(highstring.length() - 2);
		sHighWindow.setAnimationStyle(R.style.popwin_anim_style);
		sHighWindow.setFocusable(true);
		sHighWindow.setBackgroundDrawable(new BitmapDrawable());
		Method.backgroundAlpha(0.5f, activity);
		// 添加pop窗口关闭事件
		sHighWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Method.backgroundAlpha(1f, activity);
			}
		});
		// 重写onKeyListener
		sHighView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					sHighWindow.dismiss();
					Method.backgroundAlpha(1f, activity);
					return true;
				}
				return false;
			}
		});
		sHighWindow.update();
		sHighWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		sOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				high.setText(sPopupHigh.getText().toString());
				sHighWindow.dismiss();
				Method.backgroundAlpha(1f, activity);
			}
		});
	}

	/**
	 * @description 更改体重
	 * @param parent
	 * @param weightstring
	 * @param weight
	 * @param context
	 * @param activity
	 */
	@SuppressWarnings("deprecation")
	public static void ModifyWeightPopup(View parent,
			final String weightstring, final TextView weight, Context context,
			final Activity activity) {
		if (sWeightWindow == null) {
			sWeightView = LayoutInflater.from(context).inflate(
					R.layout.ppw_modify_weight, null);
			sWeightView.setFocusable(true); // 这个很重要
			sWeightView.setFocusableInTouchMode(true);
			sOk = (RelativeLayout) sWeightView.findViewById(R.id.ok);
			sPopupWeight = (EditText) sWeightView
					.findViewById(R.id.modifyweight);
			sWeightWindow = new PopupWindow(sWeightView, 630, 285);
		}
		sPopupWeight.setText(weightstring);
		sPopupWeight.setSelection(weightstring.length() - 2);
		sWeightWindow.setAnimationStyle(R.style.popwin_anim_style);
		sWeightWindow.setFocusable(true);
		sWeightWindow.setBackgroundDrawable(new BitmapDrawable());
		Method.backgroundAlpha(0.5f, activity);
		// 添加pop窗口关闭事件
		sWeightWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				Method.backgroundAlpha(1f, activity);
			}
		});
		// 重写onKeyListener
		sWeightView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					sWeightWindow.dismiss();
					Method.backgroundAlpha(1f, activity);
					return true;
				}
				return false;
			}
		});
		sWeightWindow.update();
		sWeightWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
		sOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				weight.setText(sPopupWeight.getText().toString());
				sWeightWindow.dismiss();
				Method.backgroundAlpha(1f, activity);
			}
		});
	}
}
