/**
 * @file VolleyResultHttp.java
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

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import cn.heart.app.App;
import cn.heart.bean.Result;
import cn.heart.config.Constant;
import cn.heart.fragment.ResultFragment;
import cn.heart.main.controller.ShowResultActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/**
 * @description Volley框架结果服务器请求
 * @author Guan
 * @date 2015-6-9 下午10:43:02
 * @version 1.0
 */
public class VolleyResultHttp {

	/**
	 * @description 获取用户请求
	 * @param context
	 * @param userstring
	 */
	public static void VolleyGetRate(final Context context,
			final String userstring) {

		String url = Constant.httpUrl + "ResultsController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						// 解析JSON数据
						List<Result> list = Result.ParseJSON_Rate_grade(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (Result result : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("rate_grade", result.getRate_grade());
							data.add(item);
						}
						// handler异步消息处理
						Message msg = new Message();
						msg.what = 0x124;
						msg.obj = data;
						ShowResultActivity.handler.sendMessage(msg);
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
				params.put("operation", "queryrategrade");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("GetRate");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 获取结果请求
	 * @param context
	 * @param userstring
	 */
	public static void VolleyGetResult(final Context context,
			final String userstring) {

		String url = Constant.httpUrl + "ResultsController";
		StringRequest request = new StringRequest(Method.POST, url,
				new Listener<String>() {

					@Override
					public void onResponse(String string) {
						List<Result> list = Result.ParseJSON(string);
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						for (Result result : list) {
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("time", result.getTime());
							item.put("nick", result.getNick());
							item.put("rate_grade", result.getRate_grade());
							item.put("symptoms_rhythm",
									result.getSymptoms_rhythm());
							item.put("rate_average", result.getRate_average());
							item.put("rate_min", result.getRate_min());
							item.put("rate_max", result.getRate_max());
							item.put("rhythm_heart", result.getRhythm_heart());
							item.put("sinus_arrest", result.getSinus_arrest());
							item.put("cardia_heart", result.getCardia_heart());
							item.put("heart_beat_number",
									result.getHeart_beat_number());
							item.put("psvc_number", result.getPsvc_number());
							item.put("pvc_number", result.getPvc_number());
							item.put("QRS", result.getQRS());
							item.put("RR", result.getRR());
							item.put("QT", result.getQT());
							item.put("PR", result.getPR());
							item.put("QTC", result.getQTC());
							item.put("symptoms_heart",
									result.getSymptoms_heart());
							item.put("symptoms_heart_left",
									result.getSymptoms_heart_left());
							item.put("symptoms_heart_right",
									result.getSymptoms_heart_right());
							item.put("symptoms_heart_two",
									result.getSymptoms_heart_two());
							data.add(item);
						}
						// handler异步消息处理
						Message msg = new Message();
						msg.what = 0x124;
						msg.obj = data;
						ResultFragment.handler.sendMessage(msg);
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
				params.put("operation", "queryresult");
				params.put("username", userstring);
				return params;
			}
		};
		request.setTag("GetResult");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 发送结果请求
	 * @param context
	 * @param userstring
	 * @param nickstring
	 * @param time
	 * @param rate_grade
	 * @param symptoms_rhythm
	 * @param rate_average
	 * @param rate_min
	 * @param rate_max
	 * @param rhythm_heart
	 * @param sinus_arrest
	 * @param cardia_heart
	 * @param heart_beat_number
	 * @param psvc_number
	 * @param pvc_number
	 * @param qRS
	 * @param rR
	 * @param qT
	 * @param pR
	 * @param qTC
	 * @param symptoms_heart
	 * @param symptoms_heart_left
	 * @param symptoms_heart_right
	 * @param symptoms_heart_two
	 */
	public static void VolleySendResult(final Context context,
			final String userstring, final String nickstring,
			final String time, final String rate_grade,
			final String symptoms_rhythm, final String rate_average,
			final String rate_min, final String rate_max,
			final String rhythm_heart, final String sinus_arrest,
			final String cardia_heart, final String heart_beat_number,
			final String psvc_number, final String pvc_number,
			final String qRS, final String rR, final String qT,
			final String pR, final String qTC, final String symptoms_heart,
			final String symptoms_heart_left,
			final String symptoms_heart_right, final String symptoms_heart_two) {

		String url = Constant.httpUrl + "ResultsController";
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
				params.put("operation", "addresult");
				params.put("username", userstring);
				params.put("nick", nickstring);
				params.put("time", time);
				params.put("rate_grade", rate_grade);
				params.put("symptoms_rhythm", symptoms_rhythm);
				params.put("rate_average", rate_average);
				params.put("rate_min", rate_min);
				params.put("rate_max", rate_max);
				params.put("rhythm_heart", rhythm_heart);
				params.put("sinus_arrest", sinus_arrest);
				params.put("cardia_heart", cardia_heart);
				params.put("heart_beat_number", heart_beat_number);
				params.put("psvc_number", psvc_number);
				params.put("pvc_number", pvc_number);
				params.put("QRS", qRS);
				params.put("RR", rR);
				params.put("QT", qT);
				params.put("PR", pR);
				params.put("QTC", qTC);
				params.put("symptoms_heart", symptoms_heart);
				params.put("symptoms_heart_left", symptoms_heart_left);
				params.put("symptoms_heart_right", symptoms_heart_right);
				params.put("symptoms_heart_two", symptoms_heart_two);
				
				Log.v("TAG","userstring:" + userstring);
				Log.v("TAG","nickstring:" +nickstring);
				
				return params;
			}
		};
		request.setTag("SendResult");
		App.getHttpQueue().add(request);
	}

	/**
	 * @description 发送异常通知请求
	 * @param context
	 * @param userstring
	 * @param nickstring
	 * @param time
	 * @param rate_grade
	 * @param symptoms_rhythm
	 * @param symptoms_heart
	 */
	public static void VolleyAbnormalNotice(final Context context,
			final String userstring, final String nickstring,
			final String time, final String rate_grade,
			final String symptoms_rhythm, final String symptoms_heart) {

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
				params.put("operation", "sendnotice");
				params.put("username", userstring);
				params.put("nick", nickstring);
				params.put("time", time);
				params.put("rate_grade", rate_grade);
				params.put("symptoms_rhythm", symptoms_rhythm);
				params.put("symptoms_heart", symptoms_heart);
				return params;
			}
		};
		request.setTag("AbnormalNotice");
		App.getHttpQueue().add(request);
	}
}
