/**
 * @file Speech.java
 * @description 语音发音类
 * @author Guan
 * @date 2015-6-10 下午3:18:02 
 * @version 1.0
 */
package cn.heart.utils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * @description 语音发音类
 * @author Guan
 * @date 2015-6-10 下午3:18:02
 * @version 1.0
 */
public class Speech {

	public static void set_mTts(SpeechSynthesizer mTts) {
		/**
		 * 设置发音人
		 */
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		/**
		 * 设置语速
		 */
		mTts.setParameter(SpeechConstant.SPEED, "40");
		/**
		 * 设置音调
		 */
		mTts.setParameter(SpeechConstant.PITCH, "50");
		/**
		 * 设置音量0-100
		 */
		mTts.setParameter(SpeechConstant.VOLUME, "100");
		/**
		 * 设置播放器音频流类型
		 */
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		// 如果不需要保存保存合成音频，请注释下行代码
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
		// "./sdcard/iflytek.pcm");

	}

	/**
	 * @description SynthesizerListener监听
	 */
	public static SynthesizerListener mTtsListener = new SynthesizerListener() {
		/**
		 * @description 
		 *              缓冲进度回调，arg0为缓冲进度，arg1为缓冲音频在文本中开始的位置，arg2为缓冲音频在文本中结束的位置，arg3为附加信息
		 */
		@Override
		public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

		}

		/**
		 * @description 会话结束回调接口，没有错误时error为空
		 */
		@Override
		public void onCompleted(SpeechError error) {

		}

		/**
		 * @description 开始播放
		 */
		@Override
		public void onSpeakBegin() {

		}

		/**
		 * @description 停止播放
		 */
		@Override
		public void onSpeakPaused() {

		}

		/**
		 * @description 
		 *              播放进度回调,arg0为播放进度0-100；arg1为播放音频在文本中开始的位置，arg2为播放音频在文本中结束的位置
		 */
		@Override
		public void onSpeakProgress(int arg0, int arg1, int arg2) {

		}

		/**
		 * @description 恢复播放回调接口
		 */
		@Override
		public void onSpeakResumed() {

		}

	};
}
