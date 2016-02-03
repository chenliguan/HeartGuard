/**
 * @file MainPagerAdapter.java
 * @description Fragment适配器
 * @author Guan
 * @date 2015-6-9 下午10:56:46 
 * @version 1.0
 */
package cn.heart.service.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cn.heart.fragment.GroupFragment;
import cn.heart.fragment.MeasureFragment;
import cn.heart.fragment.ResultFragment;

/**
 * @description Fragment适配器
 * @author Guan
 * @date 2015-6-9 下午10:56:46
 * @version 1.0
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

	private final String[] mTitles = { "测心电", "看结果", "家人圈" };
	private MeasureFragment mMeasureFragment;
	private ResultFragment mResultFragment;
	/**
	 * 通讯录界面的Fragment
	 */
	private GroupFragment mInfoFragment;

	public MainPagerAdapter(FragmentManager fm,
			MeasureFragment measureFragment, ResultFragment resultFragment,
			GroupFragment infoFragment) {
		super(fm);
		this.mMeasureFragment = measureFragment;
		this.mResultFragment = resultFragment;
		this.mInfoFragment = infoFragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitles[position];
	}

	@Override
	public int getCount() {
		return mTitles.length;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			if (mMeasureFragment == null) {
				mMeasureFragment = new MeasureFragment();
			}
			return mMeasureFragment;
		case 1:
			if (mResultFragment == null) {
				mResultFragment = new ResultFragment();
			}
			return mResultFragment;
		case 2:
			if (mInfoFragment == null) {
				mInfoFragment = new GroupFragment();
			}
			return mInfoFragment;
		default:
			return null;
		}
	}
}