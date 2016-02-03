/**
 * @file Check.java
 * @description 检查类
 * @author Guan
 * @date 2015-6-10 下午2:48:18 
 * @version 1.0
 */
package cn.heart.utils;

/**
 * @description 检查类
 * @author Guan
 * @date 2015-6-10 下午2:48:18
 * @version 1.0
 */
public class Check {

	/**
	 * @description 检验注册权限
	 * @param userstring
	 * @param passstring
	 * @param nickstring
	 * @param codestring
	 * @return String
	 */
	public static String RegisterCheck(String userstring, String passstring,
			String nickstring, String codestring) {
		String checkresult = "";
		if (userstring.length() != 11 || !userstring.startsWith("1")) {
			checkresult = "手机号码格式不对";
			return checkresult;
		} else if (gbk(passstring)) {
			checkresult = "密码只能是数字和字母";
			return checkresult;
		}
		return checkresult;
	}

	/**
	 * @description 检验登陆权限
	 * @param userstring
	 * @param passstring
	 * @return String
	 */
	public static String LoginCheck(String userstring, String passstring) {
		String checkresult = "";
		if (userstring.length() != 11 || !userstring.startsWith("1")) {
			checkresult = "手机号码格式不对";
			return checkresult;
		} else if (gbk(passstring)) {
			checkresult = "密码只能是数字和字母";
			return checkresult;
		}
		return checkresult;
	}

	/**
	 * @description 验证码权限
	 * @param userstring
	 * @return String
	 */
	public static String CodeCheck(String userstring) {
		String checkresult = "";
		if (userstring.equals("")) {
			checkresult = "手机号码不能为空";
			return checkresult;
		} else if (userstring.length() != 11 || !userstring.startsWith("1")) {
			checkresult = "手机号码格式不对";
			return checkresult;
		}
		return checkresult;
	}

	/**
	 * @description 检验修改密码权限
	 * @param old_passwordString
	 * @param new_passwordString
	 * @param ok_new_passwordString
	 * @return String
	 */
	public static String PasswordSetCheck(String old_passwordString,
			String new_passwordString, String ok_new_passwordString) {
		String checkresult = "";
		if (old_passwordString.equals("") & new_passwordString.equals("")
				& ok_new_passwordString.equals("")) {
			checkresult = "密码不能为空";
			return checkresult;
		} else if (old_passwordString.equals("")) {
			checkresult = "原密码不能为空";
			return checkresult;
		} else if (new_passwordString.equals("")) {
			checkresult = "新密码不能为空";
			return checkresult;
		} else if (ok_new_passwordString.equals("")) {
			checkresult = "确认新密码不能为空";
			return checkresult;
		} else if (!new_passwordString.equals(ok_new_passwordString)) {
			checkresult = "新密码与确认新密码不一致";
			return checkresult;
		} else if (old_passwordString.equals(new_passwordString)) {
			checkresult = "原密码与新密码一致";
			return checkresult;
		}
		return checkresult;
	}

	/**
	 * @description 判断密码字符串是否含有中文字
	 * @param string
	 * @return boolean
	 */
	public static boolean gbk(String string) {
		boolean is = false;
		// 返回字符串的字节长度，一个中文两个字节
		int bytesLength = string.getBytes().length;
		// 返回字符串的字符个数，一个中文算一个字符
		int sLength = string.length();
		int hasNum = bytesLength - sLength;
		if (hasNum == 0) {
			is = false;
		} else if (hasNum > 0) {
			// Log.d("tag", "字符串中含有汉字" + hasNum + "个");
			is = true;
		}
		return is;
	}

}
