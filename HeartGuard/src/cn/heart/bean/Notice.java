/**
 * @file Notice.java
 * @description 服务器信息返回通知
 * @author Guan
 * @date 2015-6-8 下午9:42:53 
 * @version 1.0
 */
package cn.heart.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description 服务器信息返回通知
 * @author Guan
 * @date 2015-6-8 下午9:42:53
 * @version 1.0
 */
public class Notice {
	private String mTitle;

	public Notice() {
	}

	public Notice(String title) {
		this.mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	/**
	 * 解析JSON数据
	 * 
	 * @param inStream
	 * @return
	 */
	public static List<Notice> ParseJSON(String string) {
		List<Notice> notices = new ArrayList<Notice>();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Notice notice = new Notice(jsonObject.getString("title"));
				notices.add(notice);
			}
			return notices;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return notices;
	}
}
