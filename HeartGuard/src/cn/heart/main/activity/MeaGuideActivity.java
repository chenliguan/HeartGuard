/**
 * @file MeasureGuideActivity.java
 * @description 测量指导类
 * @author Guan
 * @date 2015-6-9 下午2:52:25 
 * @version 1.0
 */
package cn.heart.main.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.wechatsample.R;

/**
 * @description 测量指导Activity类
 * @author Guan
 * @date 2015-6-9 下午2:52:25
 * @version 1.0
 */
public class MeaGuideActivity extends Activity {

	private ActionBar mActionBar;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_measure_guide);
		// ActionBar
		Action();
		// 初始化
		Init();
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
	private void Init() {

	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		MeaGuideActivity.this.finish();
		overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
		return false;
	}

	/**
	 * @description home图标监听
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}
}
