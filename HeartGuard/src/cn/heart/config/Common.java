/**
 * @file Common.java
 * @description 公共类
 * @author Guan
 * @date 2015-6-8 下午9:40:56 
 * @version 1.0
 */
package cn.heart.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * @description 公共类
 * @author Guan
 * @date 2015-6-8 下午9:40:56
 * @version 1.0
 */
public class Common {

	@SuppressLint("SdCardPath")
	private static String fileLocation = "/sdcard/myHead/outData.txt"; // 手机内存路径

	public static void Toast(Context context, String string) {
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @description 写数据到问文件中
	 * @param word
	 */
	public static void writeWordToFile(String word) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					fileLocation), true));
			bw.write(word);
			bw.newLine();
			bw.close();
		} catch (IOException e) {

		}
	}
}
