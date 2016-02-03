/**
 * @file StreamTool.java
 * @description 数据流工具类
 * @author Guan
 * @date 2015-6-10 下午3:24:57 
 * @version 1.0
 */
package cn.heart.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @description 数据流工具类
 * @author Guan
 * @date 2015-6-10 下午3:24:57
 * @version 1.0
 */
public class StreamTool {

	/**
	 * @description 读文件流
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] read(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}
