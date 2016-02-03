/**
 * @file ResultFragment.java
 * @description 看结果碎片
 * @author Guan
 * @date 2015-6-8 下午10:15:53 
 * @version 1.0
 */
package cn.heart.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import cn.heart.app.App;
import cn.heart.net.VolleyResultHttp;
import cn.heart.service.adapter.ResultListAdapter;

import com.example.wechatsample.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @description 看结果碎片
 * @author Guan
 * @date 2015-6-8 下午10:15:53
 * @version 1.0
 */
@SuppressLint({ "SdCardPath", "WorldReadableFiles" })
public class ResultFragment extends Fragment {

	private SharedPreferences mPreferences;
	private PullToRefreshListView mPullRefreshListView;
	private static ListView sListView;
	private static String sUserstring;
	private static Callbacks sCallbacks;
	private static List<HashMap<String, Object>> objects;
	public static Context sContext;

	/**
	 * @description 用来存放fragment的Activity必须实现的接口
	 * @author Guan
	 * @date 2015-6-8 下午10:21:41
	 * @version 1.0
	 */
	public interface Callbacks {
		public void intent_R_S(List<HashMap<String, Object>> data, int position);
	}

	/**
	 * @description onCreate()
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * @description onCreateView获取视图
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getActivity().getLayoutInflater();
		View view = inflater
				.inflate(R.layout.fragment_result, container, false);
		return view;
	}

	/**
	 * @description onActivityChreated获取组件的id
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		objects = new ArrayList<HashMap<String, Object>>();
		mPreferences = getActivity().getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		if (mPreferences.getBoolean("AUTO_ISCHECK", true)) {
			sUserstring = mPreferences.getString("USER_NAME", "");
		}
		sContext = getActivity();
		// 获取结果请求
		VolleyResultHttp.VolleyGetResult(sContext, sUserstring);

		mPullRefreshListView = (PullToRefreshListView) getActivity()
				.findViewById(R.id.pull_refresh_list);
		// 设置时，清单刷新调用的监听器
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(sContext,
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);

						// 更新LastUpdatedLabel
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);
						// 刷新列表
						new GetDataTask().execute();
					}
				});

		// 添加结束的列表中的监听器
		mPullRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						Toast.makeText(sContext, "最后一列啦", Toast.LENGTH_SHORT)
								.show();
					}
				});
		sListView = mPullRefreshListView.getRefreshableView();
	}

	/**
	 * @description handler
	 */
	public static Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			objects = (List<HashMap<String, Object>>) msg.obj;
			sListView.setAdapter(new ResultListAdapter(sContext, objects,
					sUserstring, sCallbacks));
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
	 * @description 开启后台任务执行处理
	 */
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// mAdapter.notifyDataSetChanged();
			// 获取结果请求
			VolleyResultHttp.VolleyGetResult(sContext, sUserstring);
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroy() {
		App.getHttpQueue().cancelAll("GetResult");
		super.onDestroy();
	}

	@Override
	public void onStop() {
		super.onStop();
		App.getHttpQueue().cancelAll("GetResult");
	}
}
