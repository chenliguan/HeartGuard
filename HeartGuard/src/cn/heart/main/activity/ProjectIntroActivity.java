/**
 * @file ProjectIntroActivity.java
 * @description 项目介绍Activity类
 * @author Guan
 * @date 2015-6-9 下午3:09:09 
 * @version 1.0
 */
package cn.heart.main.activity;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wechatsample.R;

/**
 * @description 项目介绍Activity类
 * @author Guan
 * @date 2015-6-9 下午3:09:09
 * @version 1.0
 */
public class ProjectIntroActivity extends Activity {

	private ActionBar mActionBar;

	/**
	 * @description onCreate
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_project_intro);
		// ActionBar
		Action();
		// 读取文件
		Reader();
	}

	/**
	 * @description 读取文件
	 */
	private void Reader() {
		try {
			InputStream is = getAssets().open("projectintro.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer, "GB2312");
			TextView tv = (TextView) findViewById(R.id.text);
			tv.setText(text);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
	 * @description 返回监听
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ProjectIntroActivity.this.finish();
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
