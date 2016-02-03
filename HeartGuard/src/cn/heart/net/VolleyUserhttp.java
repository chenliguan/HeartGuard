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
import cn.heart.bean.Notice;
import cn.heart.bean.User;
import cn.heart.config.Constant;
import cn.heart.main.activity.PassSetActivity;
import cn.heart.main.controller.AppstartActivity;
import cn.heart.main.controller.LoginActivity;
import cn.heart.main.controller.MainActivity;
import cn.heart.main.controller.UserDetailActivity;
import cn.heart.main.controller.RegisterActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class VolleyUserhttp {

	/**
	 * @description 获取用户请求
	 * @param context
	 * @param userstring
	 */
	public static void VolleyUserDetail(final Context context,
			final String userstring) {

		String url = Constant.httpUrl + "UserServlet";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						// 解析JSON数据
						List<User> list = User.ParseJSON(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (User user : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("username", user.getUsername());
							item.put("nick", user.getNick());
							item.put("code", user.getCode());
							item.put("birthday", user.getBirthday());
							item.put("gender", user.getGender());
							item.put("high", user.getHigh());
							item.put("weight", user.getWeight());
							Log.d("tag", "user.getGender():" + user.getGender());
							data.add(item);
						}
						// handler异步消息处理
						Message msg = new Message();
						msg.what = 0x129;
						msg.obj = data;
						MainActivity.handler.sendMessage(msg);
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
				params.put("operation", "userinfo");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("UserDetail");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 获取验证码请求
	 * @param context
	 * @param userstring
	 */
	public static void VolleyCode(final Context context, final String userstring) {

		String url = Constant.httpUrl + "SmsServlet";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {

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
				params.put("operation", "sendcode");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("Code");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 获取密码请求
	 * @param context
	 * @param userstring
	 */
	public static void VolleyPassword(final Context context,
			final String userstring) {

		String url = Constant.httpUrl + "SmsServlet";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						List<Notice> list = Notice.ParseJSON(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (Notice notice : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("title", notice.getTitle());
							data.add(item);
						}
						String result = (String) data.get(0).get("title");
						Message msg = new Message();
						msg.what = 0x125;
						msg.obj = result;
						LoginActivity.passhandler.sendMessage(msg);
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
				params.put("operation", "sendpassword");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("Password");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 登录请求
	 * @param context
	 * @param activity
	 * @param progressDialog
	 * @param userstring
	 * @param passstring
	 */
	public static void VolleyLogin(final Context context,
			final String activity, final ProgressDialog progressDialog,
			final String userstring, final String passstring) {

		String url = Constant.httpUrl + "CenterController";
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
						// handler异步消息处理
						Message msg = new Message();
						msg.what = 0x123;
						msg.obj = result;
						switch (activity) {
						case "AppstartActivity":
							AppstartActivity.handler.sendMessage(msg);
							break;
						case "LoginActivity":
							LoginActivity.handler.sendMessage(msg);
							break;
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
						Toast.makeText(context, "网络请求失败", Toast.LENGTH_LONG)
								.show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("operation", "login");
				params.put("format", "json");
				params.put("username", userstring);
				params.put("password", passstring);
				return params;
			}
		};
		request.setTag("Login");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 注册请求
	 * @param context
	 * @param userstring
	 * @param passstring
	 * @param nickstring
	 * @param codestring
	 */
	public static void VolleyRegister(final Context context,
			final String userstring, final String passstring,
			final String nickstring, final String codestring) {

		String url = Constant.httpUrl + "CenterController";
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
						msg.what = 0x124;
						msg.obj = result;
						RegisterActivity.handler.sendMessage(msg);
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
				params.put("operation", "regist");
				params.put("format", "json");
				params.put("nick", nickstring);
				params.put("username", userstring);
				params.put("password", passstring);
				params.put("code", codestring);
				return params;
			}
		};
		request.setTag("Register");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 修改密码请求
	 * @param context
	 * @param userstring
	 * @param old_password
	 * @param new_password
	 * @param ok_new_password
	 */
	public static void VolleyModifyPass(final Context context,
			final String userstring, final String old_password,
			final String new_password, final String ok_new_password) {

		String url = Constant.httpUrl + "UserServlet";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						Message msg = new Message();
						msg.what = 0x111;
						PassSetActivity.handler.sendMessage(msg);
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
				params.put("operation", "modifypassword");
				params.put("username", userstring);
				params.put("old_password", old_password);
				params.put("new_password", new_password);
				params.put("ok_new_password", ok_new_password);
				return params;
			}
		};
		request.setTag("ModifyPass");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 修改用户信息
	 * @param context
	 * @param userstring
	 * @param nickstring
	 * @param genderstring
	 * @param birthdaystring
	 * @param highstring
	 * @param weightstring
	 */
	public static void VolleyModifyUser(final Context context,
			final String userstring, final String nickstring,
			final String genderstring, final String birthdaystring,
			final String highstring, final String weightstring) {

		String url = Constant.httpUrl + "UserServlet";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						Message msg = new Message();
						msg.what = 0x111;
						UserDetailActivity.handler.sendMessage(msg);
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
				params.put("operation", "modifyuser");
				params.put("username", userstring);
				params.put("nick", nickstring);
				params.put("gender", genderstring);
				params.put("birthday", birthdaystring);
				params.put("high", highstring);
				params.put("weight", weightstring);
				return params;
			}
		};
		request.setTag("ModifyUser");
		App.getHttpQueue().add(request);
	}

}
