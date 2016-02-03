/**
 * @file HttpService.java
 * @description HttpService
 * @author Guan
 * @date 2015-6-9 下午10:36:25 
 * @version 1.0
 */
package cn.heart.net;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * @description HttpService
 * @author Guan
 * @date 2015-6-9 下午10:36:25
 * @version 1.0
 */
public class HttpClientService {

	/**
	 * @description 通过HttpClient发送Post请求
	 * @param httppath
	 *            请求路径
	 * @param params
	 *            请求参数
	 * @param encoding
	 *            编码
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean HttpClientPOSTRequestBoolean(String httppath,
			Map<String, String> params, String encoding) throws Exception {
		HttpResponse response = GetResponse(httppath, params, encoding);
		// 如果服务器成功的返回响应
		if (response.getStatusLine().getStatusCode() == 200) {
			return true;
		}
		return false;
	}

	/**
	 * @description 通过HttpClient发送Post请求
	 * @param httppath
	 * @param params
	 * @param encoding
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream HttpClientPOSTRequest(String httppath,
			Map<String, String> params, String encoding) throws Exception {
		HttpResponse response = GetResponse(httppath, params, encoding);
		// 如果服务器成功的返回响应
		if (response.getStatusLine().getStatusCode() == 200) {
			// 得到客户段响应的实体内容
			HttpEntity reponseEntity = response.getEntity();
			// 得到输入流
			InputStream inStream = reponseEntity.getContent();
			return inStream;
		}
		return null;
	}

	/**
	 * @description 获取 response
	 * @param httppath
	 * @param params
	 * @param encoding
	 * @return HttpResponse
	 * @throws Exception
	 */
	public static HttpResponse GetResponse(String httppath,
			Map<String, String> params, String encoding) throws Exception {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				// pairs封装请求参数
				pairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
		}
		// 创建一个httpPost对象
		HttpPost post = new HttpPost(httppath);
		// UrlEncodedFormEntity这个类是用来把输入数据编码成合适的内容
		// eg：param1=value1&param2=value2
		UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(pairs,
				encoding);
		// 设置请求参数
		post.setEntity(urlEntity);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// 发送post请求
		HttpResponse response = httpClient.execute(post);
		return response;
	}
}
