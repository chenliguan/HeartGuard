/**
 * @file User.java
 * @description 用户信息类
 * @author Guan
 * @date 2015-6-8 下午9:58:18 
 * @version 1.0
 */
package cn.heart.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * @description 用户信息类
 * @author Guan
 * @date 2015-6-8 下午9:58:18
 * @version 1.0
 */
@SuppressWarnings("serial")
public class User implements Serializable {
	private String mUsername, mNick, mPassword, mEmail, mBirthday, mCode,
			mGender, mHigh, mWeight;

	public User() {
		super();
	}

	public User(String username, String nick, String password, String email,
			String birthday, String code, String high, String weight) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mPassword = password;
		this.mEmail = email;
		this.mBirthday = birthday;
		this.mCode = code;
		this.mHigh = high;
		this.mWeight = weight;
	}

	public User(String username, String nick, String password, String email,
			String code) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mPassword = password;
		this.mEmail = email;
		this.mCode = code;
	}

	public User(String username, String nick, String code, String gender,
			String birthday, String high, String weight) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mCode = code;
		this.mGender = gender;
		this.mBirthday = birthday;
		this.mHigh = high;
		this.mWeight = weight;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		this.mUsername = username;
	}

	public String getNick() {
		return mNick;
	}

	public void setNick(String nick) {
		this.mNick = nick;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}

	public String getCode() {
		return mCode;
	}

	public void setCode(String code) {
		this.mCode = code;
	}

	public String getBirthday() {
		return mBirthday;
	}

	public void setBirthday(String birthday) {
		this.mBirthday = birthday;
	}

	public String getGender() {
		return mGender;
	}

	public void setGender(String gender) {
		this.mGender = gender;
	}

	public String getHigh() {
		return mHigh;
	}

	public void setHigh(String high) {
		this.mHigh = high;
	}

	public String getWeight() {
		return mWeight;
	}

	public void setWeight(String weight) {
		this.mWeight = weight;
	}

	/**
	 * @description 解析JSON数据
	 * @param string
	 * @return
	 */
	public static List<User> ParseJSON(String string) {
		List<User> users = new ArrayList<User>();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				User user = new User(jsonObject.getString("username"),
						jsonObject.getString("nick"),
						jsonObject.getString("code"),
						jsonObject.getString("gender"),
						jsonObject.getString("birthday"),
						jsonObject.getString("high"),
						jsonObject.getString("weight"));
				Log.d("tag", "UserParseJSON :" + jsonObject.getString("gender"));
				users.add(user);
			}
			return users;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
}
