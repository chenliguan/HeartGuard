/**
 * @file ResultListAdapter.java
 * @description  结果Adapter
 * @author Guan
 * @date 2015-6-9 下午10:59:03 
 * @version 1.0
 */
package cn.heart.service.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.heart.config.Constant;
import cn.heart.fragment.ResultFragment.Callbacks;
import cn.heart.net.ImageDownLoader;
import cn.heart.utils.SpellHelper;

import com.example.wechatsample.R;

/**
 * @description 结果Adapter
 * @author Guan
 * @date 2015-6-9 下午10:59:03
 * @version 1.0
 */
@SuppressLint("SdCardPath")
public class ResultListAdapter extends BaseAdapter {

	private Context mContext;
	private String mUsersString;
	private Callbacks mCallbacks;
	// Image 下载器
	private ImageDownLoader mImageDownLoader;
	private List<HashMap<String, Object>> mData = new ArrayList<HashMap<String, Object>>();

	public ResultListAdapter(Context context,
			List<HashMap<String, Object>> data, String userstring,
			Callbacks mCallbacks) {
		this.mContext = context;
		this.mData = data;
		this.mUsersString = userstring;
		this.mCallbacks = mCallbacks;
		mImageDownLoader = new ImageDownLoader(context);
	}

	// 元素的个数(以此判别数量)
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
		// 优化ListView
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_result, null);
			viewCache = new ItemViewCache(convertView);
			convertView.setTag(viewCache);
		}else {
			viewCache = (ItemViewCache) convertView.getTag();
		}
	
		final String nickstring = (String) mData.get(position).get("nick");
		final String mImageUrl = Constant.httpUrl + "DownloadFileServlet?file="
				+ mUsersString + SpellHelper.getEname(nickstring) + ".jpg";

		// 获取Bitmap, 内存中没有就去手机或者sd卡中获取
		Bitmap bitmap = mImageDownLoader.showCacheBitmap(mImageUrl.replaceAll(
				"[^\\w]", ""));
		if (bitmap != null)
			viewCache.mImageHead.setImageBitmap(bitmap);
		viewCache.mNick.setText(nickstring);
		viewCache.mWholeTime.setText((String) mData.get(position).get("time"));
		viewCache.mRateGrade
				.setText((String) mData.get(position).get("rate_grade"));
		viewCache.mRateAverage.setText((String) mData.get(position).get(
				"rate_average"));
		viewCache.mSypsRhythm.setText((String) mData.get(position).get(
				"symptoms_rhythm"));
		viewCache.mSypsHeart.setText((String) mData.get(position).get(
				"symptoms_heart"));
		viewCache.mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 调用接口方法
				mCallbacks.intent_R_S(mData, position);
			}
		});
		return convertView;
	}

	/**
	 * @description 元素的缓冲类,用于优化ListView
	 * @author Guan
	 * @date 2015-6-9 下午11:01:51
	 * @version 1.0
	 */
    class ItemViewCache {
    	
    	@InjectView(R.id.civ_head_ir)
		public ImageView mImageHead;
    	@InjectView(R.id.tv_nick_ir)
		public TextView mNick;
    	@InjectView(R.id.tv_grade_ir)
		public TextView mRateGrade;
    	@InjectView(R.id.tv_time_ir)
		public TextView mWholeTime;
    	@InjectView(R.id.tv_average_ir)
		public TextView mRateAverage;
    	@InjectView(R.id.tv_syps_rhythm_ir)
		public TextView mSypsRhythm;
    	@InjectView(R.id.tv_syps_heart_ir)
		public TextView mSypsHeart;
    	@InjectView(R.id.iv_details_ir)
		public ImageView mImageView;
		
		public ItemViewCache(View view) {
			ButterKnife.inject(this, view);
		}
	}
}