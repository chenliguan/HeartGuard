/**
 * @file EcgProtocol.java
 * @description Ecg协议
 * @author Michael
 * @date 2015-6-19 下午3:52:11 
 * @version 1.0
 */
package cn.heart.bluetooth;

/**
 * @description Ecg协议
 * @author Michael
 * @date 2015-6-19 下午3:52:11
 * @version 1.0
 */
public class EcgProtocol {
	// ===================命令字定义========================
	// 命令头
	public static final byte DATA_HEAD = (byte) 0xF0;
	// 开始心电数据采集
	public static final byte DATA_CMD_START_ECG = (byte) 0xC0;
	// 停止心电数据采集
	public static final byte DATA_CMD_STOP_ECG = (byte) 0xC1;
	// 读取设备号
	public static final byte DATA_CMD_SN = (byte) 0xC2;
	// 读取生产日期
	public static final byte DATA_CMD_DATE = (byte) 0xC3;
	// 查询锂电池电量
	public static final byte DATA_CMD_BATT = (byte) 0xCF;
	// 心电数据帧长度
	public static final int ECG_DATA_LEN = 5;
	// 心电计电量数据长度
	public static final int BAT_DATA_LEN = 4;

	/**
	 * @description 校验和方法
	 * @param data
	 *            校验数据
	 * @param len
	 *            校验数据个数
	 * @return 返回校验字（低字节）
	 */
	public static byte checkSum(byte[] data, int len) {
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += data[i];
		}
		return (byte) (sum & 0xFF);
	}

	// 协议判断
	public static int processEcgData(byte[] buffer) {
		if (buffer[0] == DATA_HEAD && buffer[1] == DATA_CMD_START_ECG) {
			return (buffer[2] & 0xFF) << 8 | buffer[3] & 0xFF;
		}
		return -1;
	}

	// 协议判断
	public static int processBatData(byte[] buffer) {
		if (buffer[0] == EcgProtocol.DATA_HEAD
				&& buffer[1] == EcgProtocol.DATA_CMD_BATT) {
			return buffer[2] & 0xFF;
		}
		return -1;
	}
}
