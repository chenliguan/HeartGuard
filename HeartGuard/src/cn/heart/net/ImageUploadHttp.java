/**
 * @file ImageHttpService.java
 * @description 图片上传类
 * @author Guan
 * @date 2015-6-9 下午10:40:00 
 * @version 1.0
 */
package cn.heart.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;
import cn.heart.bean.FormFile;
import cn.heart.config.Constant;
import cn.heart.utils.FileHttp;

/**
 * @description 图片上传类
 * @author Guan
 * @date 2015-6-9 下午10:40:00
 * @version 1.0
 */
@SuppressLint("SdCardPath")
public class ImageUploadHttp {

	/**
	 * 手机内存路径
	 */
	private static String mPath = "/sdcard/myHead/";

	/**
	 * @description 保存并上传
	 * @param bitmap
	 * @param imagename
	 */
	public static void PreserImage(Bitmap bitmap, final String imagename) {
		ImageUploadHttp.setPicToView(bitmap, mPath, imagename);
		TimerTask task = new TimerTask() {
			public void run() {
				ImageUploadHttp.SendImageService(mPath, imagename);
			}
		};
		Timer timer = new Timer();
		// 延时1秒
		timer.schedule(task, 1000);
	}

	/**
	 * @description 发送图片到服务器
	 * @param path
	 * @param imagename
	 * @return boolean
	 */
	public static boolean SendImageService(String path, String imagename) {
		String httppath = Constant.httpUrl + "UploadServlet";
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", "haha");
		params.put("timelength", "22");
		File file = new File(path + imagename);
		FormFile formFile = new FormFile(file, "uploadfile", "image/pjeg");
		try {
			// 自封装httpclient
			return FileHttp.post(httppath, params, formFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @description 保存在SD卡
	 * @param mBitmap
	 * @param path
	 * @param imagename
	 */
	public static void setPicToView(Bitmap mBitmap, String path,
			String imagename) {
		String sdStatus = Environment.getExternalStorageState();
		// 检测sd是否可用
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		// 创建文件夹
		file.mkdirs();
		// 图片名字
		String fileName = path + imagename;
		try {
			b = new FileOutputStream(fileName);
			// 把数据写入文件
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
