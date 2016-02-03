/**
 * @file SpellHelper.java
 * @description 中文转换为英文类
 * @author Guan
 * @date 2015-6-10 下午3:21:36 
 * @version 1.0
 */
package cn.heart.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @description 中文转换为英文类
 * @author Guan
 * @date 2015-6-10 下午3:21:36
 * @version 1.0
 */
public class SpellHelper {
	/**
	 * @description 将中文转换为英文@description
	 * @param name
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getEname(String name) {
		HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
		// 设置样式
		pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		try {
			return PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * @description 姓名转换
	 * @param name
	 * @return
	 */
	public static String getUpEname(String name) {
		char[] strs = name.toCharArray();
		String newname = null;

		if (strs.length == 2) {
			// 如果姓名只有两个字
			newname = getEname("" + strs[0]) + getEname("" + strs[1]);

		} else if (strs.length == 3) {
			// 如果姓名有三个字
			newname = getEname("" + strs[0]) + getEname("" + strs[1] + strs[2]);

		} else if (strs.length == 4) {
			// 如果姓名有四个字
			newname = getEname("" + strs[0] + strs[1])
					+ getEname("" + strs[2] + strs[3]);

		} else {
			newname = getEname(name);
		}

		return newname;
	}
}