/**
 * @file Family.java
 * @description 家人信息类
 * @author Guan
 * @date 2015-6-8 下午9:42:06 
 * @version 1.0
 */
package cn.heart.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @description 家人信息类
 * @author Guan
 * @date 2015-6-8 下午9:42:06
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Family implements Serializable {
	private String mUsername, mNick, mEmail, mBirthday, mGender, mHigh,
			mWeight, mFamilygroup;

	public Family() {
		super();
	}

	public Family(String username, String nick, String email, String birthday,
			String gender, String high, String weight, String familygroup) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mEmail = email;
		this.mBirthday = birthday;
		this.mGender = gender;
		this.mHigh = high;
		this.mWeight = weight;
		this.mFamilygroup = familygroup;
	}

	public Family(String username, String nick, String birthday, String gender,
			String high, String weight, String familygroup) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mBirthday = birthday;
		this.mGender = gender;
		this.mHigh = high;
		this.mWeight = weight;
		this.mFamilygroup = familygroup;
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

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
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

	public String getFamilygroup() {
		return mFamilygroup;
	}

	public void setFamilygroup(String familygroup) {
		this.mFamilygroup = familygroup;
	}

	/**
	 * 解析JSON数据
	 * 
	 * @param inStream
	 * @return
	 */
	public static List<Family> ParseJSON(String string) {
		List<Family> familys = new ArrayList<Family>();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Family family = new Family(jsonObject.getString("username"),
						jsonObject.getString("nick"),
						jsonObject.getString("gender"),
						jsonObject.getString("birthday"),
						jsonObject.getString("high"),
						jsonObject.getString("weight"),
						jsonObject.getString("familygroup"));
				familys.add(family);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return familys;
	}
}
