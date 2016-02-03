/**
 * @file UserListAdapter.java
 * @description 选择使用者Adapter
 * @author Guan
 * @date 2015-6-9 下午11:02:07 
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.config.Constant;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.SpellHelper;

import com.example.wechatsample.R;

/**
 * @description 选择使用者Adapter
 * @author Guan
 * @date 2015-6-9 下午11:02:07
 * @version 1.0
 */
@SuppressLint("SdCardPath")
public class UserListAdapter extends BaseAdapter {

	private Context mContext;
	public ListView mListView;
	public String mUserString;
	private PopupWindow mWindow;
	private SharedPreferences.Editor mEditor;
	// Image 下载器
	private ImageDownLoader mImageDownLoader;
	List<HashMap<String, Object>> mData = new ArrayList<HashMap<String, Object>>();

	public UserListAdapter(Context context, List<HashMap<String, Object>> data,
			String userstring, SharedPreferences.Editor editor,
			PopupWindow window, ListView listView) {
		this.mContext = context;
		this.mData = data;
		this.mUserString = userstring;
		this.mEditor = editor;
		this.mWindow = window;
		this.mListView = listView;
		mImageDownLoader = new ImageDownLoader(context);
	}

	// 元素的个数(以此判别数量
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
	 * @description 用以生成在ListView中展示的一个个元素View
	 */
	@SuppressLint({ "InflateParams", "ResourceAsColor" })
	public View getView(final int position, View convertView, ViewGroup parent) {

		ItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_user, null);
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
		if (bitmap != null)
			viewCache.mImageHead.setImageBitmap(bitmap);
		viewCache.mTextNick.setText(nickstring);

		viewCache.mRelativeFamily.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/** 保存本地更新后信息 */
				mEditor.putString("NICK", nickstring);
				mEditor.putString("GENDER",
						(String) mData.get(position).get("gender"));
				mEditor.putString("BIRTHDAY",
						(String) mData.get(position).get("birthday"));
				mEditor.putString("HIGH",
						(String) mData.get(position).get("high"));
				mEditor.putString("WEIGHT",
						(String) mData.get(position).get("weight"));
				mEditor.putString("FAMILYGROUP", (String) mData.get(position)
						.get("familygroup"));
				mEditor.commit();
				mWindow.dismiss();
			}
		});
		return convertView;
	}

	/**
	 * @description 元素的缓冲类,用于优化ListView
	 * @author Guan
	 * @date 2015-6-9 下午11:03:46
	 * @version 1.0
	 */
	static class ItemViewCache {

		@InjectView(R.id.rlyt_item_iu)
		RelativeLayout mRelativeFamily;
		@InjectView(R.id.civ_head_iu)
		ImageView mImageHead;
		@InjectView(R.id.tv_nick_iu)
		TextView mTextNick;

		public ItemViewCache(View view) {
			ButterKnife.inject(this, view);
		}
	}

	/**
	 * @description 取消下载任务
	 */
	public void cancelTask() {
		mImageDownLoader.cancelTask();
	}
}
