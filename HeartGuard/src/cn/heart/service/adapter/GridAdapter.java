/**
 * @file GridAdapter.java
 * @description 家人图显示列表Adapter
 * @author Guan
 * @date 2015-6-28 上午10:57:11 
 * @version 1.0
 */
package cn.heart.service.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.config.Constant;
import cn.heart.fragment.GroupFragment.Callbacks;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.SpellHelper;

import com.example.wechatsample.R;

/**
 * @description 家人图显示列表Adapter
 * @author Guan
 * @date 2015-6-28 上午10:57:11
 * @version 1.0
 */
@SuppressLint("InflateParams")
public class GridAdapter extends BaseAdapter {

	public String mUserString;
	private Context mContext;
	private Callbacks mCallbacks;
	LinearLayout.LayoutParams params;
	private ImageDownLoader mImageDownLoader;
	List<HashMap<String, Object>> mData = new ArrayList<HashMap<String, Object>>();

	public GridAdapter(Context context, List<HashMap<String, Object>> data,
			String userstring, Callbacks mCallbacks) {
		this.mContext = context;
		this.mData = data;
		this.mUserString = userstring;
		this.mCallbacks = mCallbacks;
		LayoutInflater.from(context);
		mImageDownLoader = new ImageDownLoader(context);

		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * @description 用以生成在GridView中展示的一个个元素View
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {

		ItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view, null);
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

		viewCache.mNick.setText(nickstring);
		viewCache.mImageHead.setLayoutParams(params);
		viewCache.mImageHead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用接口方法
				mCallbacks.intent_M_O(mData, position);
			}
		});
		
		return convertView;
	}

	/**
	 * @description 元素的缓冲类,用于优化GirdView
	 * @author Guan
	 * @date 2015-6-28 上午11:02:12
	 * @version 1.0
	 */
	class ItemViewCache {

		@InjectView(R.id.civ_head_ig)
		ImageView mImageHead;
		@InjectView(R.id.tv_user_ig)
		TextView mNick;

		public ItemViewCache(View view) {
			ButterKnife.inject(this, view);
		}
	}
}
