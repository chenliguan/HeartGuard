/**
 * @file AboutAppActivity.java
 * @description 关于APP的Activity类
 * @author Guan
 * @date 2015-6-8 下午10:23:43 
 * @version 1.0
 */
package cn.heart.main.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.heart.main.controller.ManageSetActivity;

import com.example.wechatsample.R;

/**
 * @description 关于APP的Activity类
 * @author Guan
 * @date 2015-6-8 下午10:23:43
 * @version 1.0
 */
public class AboutAppActivity extends Activity {

	@InjectView(R.id.rlyt_guide)
	RelativeLayout mGuideRelative;
	@InjectView(R.id.rlyt_contact)
	RelativeLayout mContactRelative;
	@InjectView(R.id.rlyt_introduce)
	RelativeLayout mIntroRelative;

	private ActionBar mActionBar;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_about_app);
		ButterKnife.inject(this);
		// ActionBar
		Action();
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
	 * @description 测量指导监听
	 * @author Guan
	 * @date 2015-6-8 下午10:25:39
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_guide)
	void MeasureGuideClickListener() {
		startActivity(new Intent(AboutAppActivity.this,
				MeaGuideActivity.class));
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 联系我们监听类
	 * @author Guan
	 * @date 2015-6-8 下午10:26:03
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_contact)
	void ContactUsClickListener() {
		startActivity(new Intent(AboutAppActivity.this, ContactUsActivity.class));
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 项目介绍监听类
	 * @author Guan
	 * @date 2015-6-8 下午10:26:46
	 * @version 1.0
	 */
	@OnClick(R.id.rlyt_introduce)
	void ProjectIntroClickListener() {
		startActivity(new Intent(AboutAppActivity.this,
				ProjectIntroActivity.class));
		overridePendingTransition(R.anim.out_from_right, R.anim.out_to_left);
	}

	/**
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startActivity(new Intent(AboutAppActivity.this,
				ManageSetActivity.class));
		AboutAppActivity.this.finish();
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
			startActivity(new Intent(AboutAppActivity.this,
					ManageSetActivity.class));
			this.finish();
			overridePendingTransition(R.anim.in_from_right, R.anim.in_to_left);
			break;
		}
		return false;
	}
}
