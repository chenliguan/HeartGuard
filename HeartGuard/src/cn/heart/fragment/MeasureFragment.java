/**
 * @file MeasureFragment.java
 * @description 测心电碎片
 * @author Guan
 * @date 2015-6-8 下午10:11:13 
 * @version 1.0
 */
package cn.heart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.wechatsample.R;

/**
 * @description 测心电碎片
 * @author Guan
 * @date 2015-6-8 下午10:11:13
 * @version 1.0
 */
public class MeasureFragment extends Fragment {

	private ImageButton mButtonMeasure;
	private Callbacks mCallbacks;

	/**
	 * @description 用来存放fragment的Activtiy必须实现的接口
	 * @author Guan
	 * @date 2015-6-8 下午10:12:26
	 * @version 1.0
	 */
	public interface Callbacks {
		// public void onMessage(String string);
		public void intent_M_E();
	}

	/**
	 * @description onCreate
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager();
	}

	/**
	 * @description 获取布局文件ID
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_measure, container,
				false);
		return view;
	}

	/**
	 * @description 实例化资源
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mButtonMeasure = (ImageButton) getActivity().findViewById(R.id.Measure);
		mButtonMeasure.setOnClickListener(new intent_M_EOnClickListener());
	}

	/**
	 * @description 当该Fragment被添加到Activity或者显示时被回调
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常
		try {
			mCallbacks = (Callbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	/**
	 * @description 触发监听
	 * @author Guan
	 * @date 2015-6-8 下午10:13:35
	 * @version 1.0
	 */
	public class intent_M_EOnClickListener implements OnClickListener {
		public void onClick(View v) {
			mCallbacks.intent_M_E(); // 调用接口方法
		}
	}
}
