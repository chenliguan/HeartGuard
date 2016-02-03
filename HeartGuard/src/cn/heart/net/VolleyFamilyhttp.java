/**
 * @file VolleyFamilyhttp.java
 * @description Volley框架家人服务器请求
 * @author Guan
 * @date 2015-6-9 下午10:43:02 
 * @version 1.0
 */
package cn.heart.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.heart.app.App;
import cn.heart.bean.Family;
import cn.heart.bean.Notice;
import cn.heart.config.Constant;
import cn.heart.fragment.GroupFragment;
import cn.heart.main.activity.FamilyListActivity;
import cn.heart.main.activity.SwitchUserActivity;
import cn.heart.main.controller.MainActivity;
import cn.heart.main.controller.RegisterActivity;
import cn.heart.main.controller.UserDetailActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * @description Volley框架家人服务器请求
 * @author Guan
 * @date 2015-6-9 下午10:43:02
 * @version 1.0
 */
public class VolleyFamilyhttp {

	/**
	 * @description 添加家人请求
	 * @param context
	 * @param activity
	 * @param userstring
	 * @param nickstring
	 * @param genderstring
	 * @param birthdaystring
	 * @param highstring
	 * @param weightstring
	 * @param familygroup
	 */
	public static void VolleyAddFamily(final Context context,
			final String activity, final String userstring,
			final String nickstring, final String genderstring,
			final String birthdaystring, final String highstring,
			final String weightstring, final String familygroup) {

		String url = Constant.httpUrl + "FamilyController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						// 解析JSON数据
						List<Notice> list = Notice.ParseJSON(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (Notice notice : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("title", notice.getTitle());
							data.add(item);
						}
						String result = (String) data.get(0).get("title");
						Message msg = new Message();
						msg.what = 0x111;
						msg.obj = result;
						switch (activity) {
						case "RegisterActivity":
							RegisterActivity.handler.sendMessage(msg);
							break;
						case "OneDetailActivity":
							UserDetailActivity.handler.sendMessage(msg);
							break;
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("operation", "addfamily");
				params.put("username", userstring);
				params.put("nick", nickstring);
				params.put("gender", genderstring);
				params.put("birthday", birthdaystring);
				params.put("high", highstring);
				params.put("weight", weightstring);
				params.put("familygroup", familygroup);
				return params;
			}
		};
		request.setTag("AddFamily");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 所有家人信息请求
	 * @param context
	 * @param activity
	 * @param userstring
	 */
	public static void VolleyAllfamily(final Context context,
			final String activity, final String userstring) {

		String url = Constant.httpUrl + "FamilyController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						List<Family> list = Family.ParseJSON(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (Family family : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("nick", family.getNick());
							item.put("gender", family.getGender());
							item.put("birthday", family.getBirthday());
							item.put("high", family.getHigh());
							item.put("weight", family.getWeight());
							item.put("familygroup", family.getFamilygroup());
							
							Log.d("tag", family.getNick());
							
							data.add(item);
						}
						// handler异步消息处理
						Message msg = new Message();
						msg.what = 0x124;
						msg.obj = data;
						switch (activity) {
						case "UserListActivity":
							FamilyListActivity.listHandler.sendMessage(msg);
							break;
						case "SwitchUserActivity":
							SwitchUserActivity.switchHandler.sendMessage(msg);
							break;
						case "MainActivity":
							MainActivity.popuplisthandler.sendMessage(msg);
							break;
						case "GroupFragment":
							GroupFragment.handler.sendMessage(msg);
							break;												
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("operation", "queryfamily");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("familylist");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 删除家人请求
	 * @param context
	 * @param progressDialog
	 * @param userstring
	 * @param nickstring
	 */
	public static void VolleyDeleteFamily(final Context context,
			final ProgressDialog progressDialog, final String userstring,
			final String nickstring) {

		String url = Constant.httpUrl + "FamilyController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						Message msg = new Message();
						msg.what = 0x101;
						UserDetailActivity.deleteHandler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						progressDialog.dismiss();
						Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("operation", "deletefamily");
				params.put("username", userstring);
				params.put("nick", nickstring);
				return params;
			}
		};
		request.setTag("DeleteFamily");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 修改家人信息请求
	 * @param context
	 * @param userstring
	 * @param nickstring
	 * @param genderstring
	 * @param birthdaystring
	 * @param highstring
	 * @param weightstring
	 * @param nicked
	 */
	public static void VolleyModifyFamily(final Context context,
			final ProgressDialog progressDialog, final String userstring,
			final String nickstring, final String genderstring,
			final String birthdaystring, final String highstring,
			final String weightstring, final String nicked) {

		String url = Constant.httpUrl + "FamilyController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						Message msg = new Message();
						msg.what = 0x134;
						UserDetailActivity.modifyHandler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						progressDialog.dismiss();
						Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("operation", "modifyfamily");
				params.put("username", userstring);
				params.put("nick", nickstring);
				params.put("gender", genderstring);
				params.put("birthday", birthdaystring);
				params.put("high", highstring);
				params.put("weight", weightstring);
				params.put("nicked", nicked);
				return params;
			}
		};
		request.setTag("ModifyFamily");
		App.getHttpQueue().add(request);
	}
}
