package cn.heart.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Result {

	private String mUsername, mNick, mTime, mRate_grade, mSymptoms_rhythm,
			mRate_average, mRate_min, mRate_max, mRhythm_heart, mSinus_arrest,
			mCardia_heart, mHeart_beat_number, mPsvc_number, mPvc_number, mQRS,
			mRR, mQT, mPR, mQTC, mSymptoms_heart, mSymptoms_heart_left,
			mSymptoms_heart_right, mSymptoms_heart_two;

	public Result(String username, String nick, String time, String rate_grade,
			String symptoms_rhythm, String rate_average, String rate_min,
			String rate_max, String rhythm_heart, String sinus_arrest,
			String cardia_heart, String heart_beat_number, String psvc_number,
			String pvc_number, String qRS, String rR, String qT, String pR,
			String qTC, String symptoms_heart, String symptoms_heart_left,
			String symptoms_heart_right, String symptoms_heart_two) {
		super();
		this.mUsername = username;
		this.mNick = nick;
		this.mTime = time;
		this.mRate_grade = rate_grade;
		this.mSymptoms_rhythm = symptoms_rhythm;
		this.mRate_average = rate_average;
		this.mRate_min = rate_min;
		this.mRate_max = rate_max;
		this.mRhythm_heart = rhythm_heart;
		this.mSinus_arrest = sinus_arrest;
		this.mCardia_heart = cardia_heart;
		this.mHeart_beat_number = heart_beat_number;
		this.mPsvc_number = psvc_number;
		this.mPvc_number = pvc_number;
		this.mQRS = qRS;
		this.mRR = rR;
		this.mQT = qT;
		this.mPR = pR;
		this.mQTC = qTC;
		this.mSymptoms_heart = symptoms_heart;
		this.mSymptoms_heart_left = symptoms_heart_left;
		this.mSymptoms_heart_right = symptoms_heart_right;
		this.mSymptoms_heart_two = symptoms_heart_two;
	}

	public Result(String rate_grade) {
		super();
		this.mRate_grade = rate_grade;
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

	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}

	public String getRate_grade() {
		return mRate_grade;
	}

	public void setRate_grade(String rate_grade) {
		this.mRate_grade = rate_grade;
	}

	public String getSymptoms_rhythm() {
		return mSymptoms_rhythm;
	}

	public void setSymptoms_rhythm(String symptoms_rhythm) {
		this.mSymptoms_rhythm = symptoms_rhythm;
	}

	public String getRate_average() {
		return mRate_average;
	}

	public void setRate_average(String rate_average) {
		this.mRate_average = rate_average;
	}

	public String getRate_min() {
		return mRate_min;
	}

	public void setRate_min(String rate_min) {
		this.mRate_min = rate_min;
	}

	public String getRate_max() {
		return mRate_max;
	}

	public void setRate_max(String rate_max) {
		this.mRate_max = rate_max;
	}

	public String getRhythm_heart() {
		return mRhythm_heart;
	}

	public void setRhythm_heart(String rhythm_heart) {
		this.mRhythm_heart = rhythm_heart;
	}

	public String getSinus_arrest() {
		return mSinus_arrest;
	}

	public void setSinus_arrest(String sinus_arrest) {
		this.mSinus_arrest = sinus_arrest;
	}

	public String getCardia_heart() {
		return mCardia_heart;
	}

	public void setCardia_heart(String cardia_heart) {
		this.mCardia_heart = cardia_heart;
	}

	public String getHeart_beat_number() {
		return mHeart_beat_number;
	}

	public void setHeart_beat_number(String heart_beat_number) {
		this.mHeart_beat_number = heart_beat_number;
	}

	public String getPsvc_number() {
		return mPsvc_number;
	}

	public void setPsvc_number(String psvc_number) {
		this.mPsvc_number = psvc_number;
	}

	public String getPvc_number() {
		return mPvc_number;
	}

	public void setPvc_number(String pvc_number) {
		this.mPvc_number = pvc_number;
	}

	public String getQRS() {
		return mQRS;
	}

	public void setQRS(String qRS) {
		mQRS = qRS;
	}

	public String getRR() {
		return mRR;
	}

	public void setRR(String rR) {
		mRR = rR;
	}

	public String getQT() {
		return mQT;
	}

	public void setQT(String qT) {
		mQT = qT;
	}

	public String getPR() {
		return mPR;
	}

	public void setPR(String pR) {
		mPR = pR;
	}

	public String getQTC() {
		return mQTC;
	}

	public void setQTC(String qTC) {
		mQTC = qTC;
	}

	public String getSymptoms_heart() {
		return mSymptoms_heart;
	}

	public void setSymptoms_heart(String symptoms_heart) {
		this.mSymptoms_heart = symptoms_heart;
	}

	public String getSymptoms_heart_left() {
		return mSymptoms_heart_left;
	}

	public void setSymptoms_heart_left(String symptoms_heart_left) {
		this.mSymptoms_heart_left = symptoms_heart_left;
	}

	public String getSymptoms_heart_right() {
		return mSymptoms_heart_right;
	}

	public void setSymptoms_heart_right(String symptoms_heart_right) {
		this.mSymptoms_heart_right = symptoms_heart_right;
	}

	public String getSymptoms_heart_two() {
		return mSymptoms_heart_two;
	}

	public void setSymptoms_heart_two(String symptoms_heart_two) {
		this.mSymptoms_heart_two = symptoms_heart_two;
	}

	/**
	 * 解析JSON数据
	 * 
	 * @param inStream
	 * @return
	 */
	public static List<Result> ParseJSON(String string) {
		List<Result> results = new ArrayList<Result>();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Result result = new Result(jsonObject.getString("username"),
						jsonObject.getString("nick"),
						jsonObject.getString("time"),
						jsonObject.getString("rate_grade"),
						jsonObject.getString("symptoms_rhythm"),
						jsonObject.getString("rate_average"),
						jsonObject.getString("rate_min"),
						jsonObject.getString("rate_max"),
						jsonObject.getString("rhythm_heart"),
						jsonObject.getString("sinus_arrest"),
						jsonObject.getString("cardia_heart"),
						jsonObject.getString("heart_beat_number"),
						jsonObject.getString("psvc_number"),
						jsonObject.getString("pvc_number"),
						jsonObject.getString("QRS"),
						jsonObject.getString("RR"), jsonObject.getString("QT"),
						jsonObject.getString("PR"),
						jsonObject.getString("QTC"),
						jsonObject.getString("symptoms_heart"),
						jsonObject.getString("symptoms_heart_left"),
						jsonObject.getString("symptoms_heart_right"),
						jsonObject.getString("symptoms_heart_two"));
				results.add(result);
			}
			return results;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * 解析JSON_Rate_grade数据
	 * 
	 * @param inStream
	 * @return
	 */
	public static List<Result> ParseJSON_Rate_grade(String string) {
		List<Result> results = new ArrayList<Result>();
		try {
			JSONArray array = new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				Result result = new Result(jsonObject.getString("rate_grade"));
				results.add(result);
			}
			return results;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}
}
