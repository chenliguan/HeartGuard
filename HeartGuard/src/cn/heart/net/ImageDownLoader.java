package cn.heart.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import cn.heart.utils.FileUtils;

@SuppressLint("HandlerLeak")
public class ImageDownLoader {
	/**
	 * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
	 */
	private static LruCache<String, Bitmap> mMemoryCache;
	/**
	 * 操作文件相关类对象的引用
	 */
	private static FileUtils fileUtils;
	/**
	 * 下载Image的线程池
	 */
	private ExecutorService mImageThreadPool = null;

	/**
	 * @description 构造函数
	 * @param context
	 */
	public ImageDownLoader(Context context) {
		// 获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		// 给LruCache分配1/8 4M
		mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

			// 必须重写此方法，来测量Bitmap的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		fileUtils = new FileUtils(context);
	}

	/**
	 * @description 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
	 * @return
	 */
	public ExecutorService getThreadPool() {
		if (mImageThreadPool == null) {
			synchronized (ExecutorService.class) {
				if (mImageThreadPool == null) {
					// 为了下载图片更加的流畅，我们用了2个线程来下载图片
					mImageThreadPool = Executors.newFixedThreadPool(2);
				}
			}
		}
		return mImageThreadPool;
	}

	/**
	 * @description 保存在SD卡或者手机目录、添加Bitmap到内存缓存
	 * @param url
	 * @param bitmap
	 */
	public static void addBitmapToMemory(final String url, Bitmap bitmap) {
		final String subUrl = url.replaceAll("[^\\w]", "");
		try {
			// 保存在SD卡或者手机目录
			fileUtils.savaBitmap(subUrl, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 将Bitmap 加入内存缓存
		addBitmapToMemoryCache(subUrl, bitmap);
	}

	/**
	 * @description 添加Bitmap到内存缓存
	 * @param key
	 * @param bitmap
	 */
	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * @description 从内存缓存中获取一个Bitmap
	 * @param key
	 * @return
	 */
	public static Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	/**
	 * @description 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取，SD卡或者手机缓存 没有就去下载
	 * @param url
	 * @param listener
	 * @return
	 */
	public Bitmap downloadImage(final String url,
			final onImageLoaderListener listener) {
		// 替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url
		// 是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，
		// 我们没有创建此目录保存文件就会保存
		final String subUrl = url.replaceAll("[^\\w]", "");
		// 1.从内存中、手机或者sd卡中获取Bitmap
		Bitmap bitmap = showCacheBitmap(subUrl);
		if (bitmap != null) {
			return bitmap;
		} else {
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// 调用异步下载图片的回调接口
					listener.onImageLoader((Bitmap) msg.obj, url);
				}
			};

			getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					// 2.异步下载图片的回调接口
					Bitmap bitmap = getBitmapFormUrl(url);
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					try {
						// 保存在SD卡或者手机目录
						fileUtils.savaBitmap(subUrl, bitmap);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// 将Bitmap 加入内存缓存
					addBitmapToMemoryCache(subUrl, bitmap);
				}
			});
		}
		return null;
	}

	/**
	 * @description 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
	 * @param url
	 * @return
	 */
	public Bitmap showCacheBitmap(String url) {
		if (getBitmapFromMemCache(url) != null) {
			return getBitmapFromMemCache(url);
		} else if (fileUtils.isFileExists(url)
				&& fileUtils.getFileSize(url) != 0) {
			// 从SD卡获取手机里面获取Bitmap
			Bitmap bitmap = fileUtils.getBitmap(url);
			// 将Bitmap 加入内存缓存
			addBitmapToMemoryCache(url, bitmap);
			return bitmap;
		}
		return null;
	}

	/**
	 * @description 从网络中Url中下载获取Bitmap
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFormUrl(String url) {
		Bitmap bitmap = null;
		HttpURLConnection con = null;
		try {
			// URL mImageUrl = new URL(url);
			// con = (HttpURLConnection) mImageUrl.openConnection();
			// con.setConnectTimeout(10 * 1000);
			// con.setReadTimeout(10 * 1000);
			// con.setDoInput(true);
			// con.setDoOutput(true);
			Map<String, String> params = new HashMap<String, String>();
			InputStream inputStream = HttpClientService.HttpClientPOSTRequest(url,
					params, "UTF-8");
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;
	}

	/**
	 * @description 取消正在下载的任务
	 */
	public synchronized void cancelTask() {
		if (mImageThreadPool != null) {
			mImageThreadPool.shutdownNow();
			mImageThreadPool = null;
		}
	}

	/**
	 * @description 异步下载图片的回调接口
	 * @author Guan
	 * @date 2015-6-8 下午8:45:47
	 * @version 1.0
	 */
	public interface onImageLoaderListener {
		void onImageLoader(Bitmap bitmap, String url);
	}
}
