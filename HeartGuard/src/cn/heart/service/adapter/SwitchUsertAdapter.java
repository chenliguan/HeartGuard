/**
 * @file FamilyListViewAdapter.java
 * @description 家人信息列表Adapter
 * @author Guan
 * @date 2015-6-9 下午10:54:07 
 * @version 1.0
 */
package cn.heart.service.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.SpellHelper;

import com.example.wechatsample.R;

/**
 * @description 家人信息列表Adapter
 * @author Guan
 * @date 2015-6-9 下午10:54:07
 * @version 1.0
 */
@SuppressLint("SdCardPath")
public class SwitchUsertAdapter extends BaseAdapter {

	private Context mContext;
	private String mUserString, mNickString;
	private int mCurrentPosition;
	private SharedPreferences mPreferences;
	private boolean mTag = false, mInt = false;
	// Image 下载器
	private ImageDownLoader mImageDownLoader;
	private List<HashMap<String, Object>> mData = new ArrayList<HashMap<String, Object>>();

	public SwitchUsertAdapter(Context context,
			List<HashMap<String, Object>> data, SharedPreferences preferences,
			String userstring) {
		this.mContext = context;
		this.mData = data;
		this.mPreferences = preferences;
		this.mUserString = userstring;
		mImageDownLoader = new ImageDownLoader(context);
	}

	/**
	 * @description 元素的个数(以此判别数量)
	 */
	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	/**
	 * @description 设置mTag boolean值
	 */
	public void setTag(int mCurrentPosition) {
		mTag = true;
		this.mCurrentPosition = mCurrentPosition;
		this.notifyDataSetChanged();
	}

	/**
	 * @description 设置mTag boolean值
	 */
	public void setInt(int mCurrentPosition) {
		mInt = true;
		this.mCurrentPosition = mCurrentPosition;
		this.notifyDataSetChanged();
	}

	/**
	 * @description 用以生成在ListView中展示的一个个元素View
	 */
	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	public View getView(final int position, View convertView, ViewGroup parent) {

		ItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_switch_user, null);
			viewCache = new ItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ItemViewCache) convertView.getTag();
		}

		final String nickstring = (String) mData.get(position).get("nick");
		final String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUserString + SpellHelper.getEname(nickstring) + ".jpg";
		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (mPreferences.getBoolean("AUTO_ISCHECK", true))
			mNickString = mPreferences.getString("NICK", "");

		if (bitmap != null)
			viewCache.mImageHead.setImageBitmap(bitmap);
		viewCache.mNick.setText(nickstring);

		Log.d("tag", "mNickString:" + mNickString + nickstring);

		// 切换选项
		if (mTag == true & mCurrentPosition == position) {
			viewCache.mImagview.setVisibility(View.VISIBLE);
		} else if (mInt == true & mCurrentPosition == position) {
			viewCache.mImagview.setVisibility(View.VISIBLE);
		} else {
			viewCache.mImagview.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	/**
	 * @description 元素的缓冲类,用于优化ListView
	 * @author Guan
	 * @date 2015-6-9 下午10:56:12
	 * @version 1.0
	 */
	static class ItemViewCache {

		@InjectView(R.id.rlyt_item_isu)
		RelativeLayout mRelativeFamily;
		@InjectView(R.id.civ_head_isu)
		ImageView mImageHead;
		@InjectView(R.id.tv_nick_isu)
		TextView mNick;
		@InjectView(R.id.iv_next_isu)
		ImageView mImagview;

		public ItemViewCache(View view) {
			ButterKnife.inject(this, view);
		}
	}
}