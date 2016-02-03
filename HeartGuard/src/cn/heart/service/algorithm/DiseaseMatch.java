/**
 * @file DiseaseMatch.java
 * @description 病症诊断类
 * @author Aries
 * @date 2015-6-19 下午9:46:44 
 * @version 1.0
 */
package cn.heart.service.algorithm;

import java.util.ArrayList;

import cn.heart.bean.ResultData;

/**
 * @description 病症诊断类
 * @author Aries
 * @date 2015-6-19 下午9:46:44
 * @version 1.0
 */
public class DiseaseMatch {
	ResultData resultData;

	public DiseaseMatch(ResultData resultdata) {
		resultData = resultdata;
	}

	/**
	 * 根据方差判断是否存在早搏，并且将早搏个数加1
	 * 
	 * getVariance()获取RR间期的方差值
	 * 
	 * @return
	 */
	public boolean ifPrematureBeat() {
		boolean isPrematureBeat = false;
		for (int i = 0; i < resultData.getVariance().size(); i++) {
			if (resultData.getVariance().get(i) > 1018) {
				resultData.setPrematureBeat(resultData.getPrematureBeat() + 1);
			}
		}
		if (resultData.getPrematureBeat() > 0) {
			isPrematureBeat = true;
		}
		return isPrematureBeat;
	}

	/**
	 * QRS大于0.12m 为室性，即42个数据点，取39偏差3个数据
	 */
	private final int FLAG = 40;

	/**
	 * 当RR间期大于120时认为出现早搏
	 */
	private final int Internal = 100;

	/**
	 * 根据方差判断是否存在早搏，并且在此基础上根据每个R波的QRS时限鉴别室上性，室性期前收缩
	 * 
	 * @return
	 */
	public boolean ifPremature() {

		boolean isPrematureBeat = false;
		resultData.getAtrialPrematureBeatIndex().clear(); // 存放心房期前收缩的对应R波下标
		resultData.getPrematureVentricualrContractionIndex().clear(); // 存放心室期前收缩的R波下标

		for (int i = 0; i < resultData.getVariance().size(); i++) { // 遍历所有的方差

			if (resultData.getVariance().get(i) > 1018) { // 当大于1018时可认为在次4.5s内出现了心律不齐的现象

				for (int j = 0; j < resultData.getNewQRSAverage().get(i).size(); j++) {

					// 一个方差对应一个NewRpointList，NewRpointList代表的是4.5s数据内的R波下标值
					if (j + 2 < resultData.getNewRpointList().get(i).size()) {

						// 逻辑判断，若RR(i)-RR(i+1)的绝对值大于100满足早搏条件，这认为这3个R波内最少出现一个早搏
						if (Math.abs((resultData.getNewRpointList().get(i)
								.get(j + 2) - resultData.getNewRpointList()
								.get(i).get(j + 1))
								- (resultData.getNewRpointList().get(i)
										.get(j + 1) - resultData
										.getNewRpointList().get(i).get(j))) > Internal) {

							// 为了区分是房性、室性做的一个判断
							/**
							 * 室性：对应的3个QRS时限中任意一个大于40个数据点,作为室性
							 */
							if (resultData.getNewQRSAverage().get(i).get(j) > FLAG
									|| resultData.getNewQRSAverage().get(i)
											.get(j + 1) > FLAG
									|| resultData.getNewQRSAverage().get(i)
											.get(j + 2) > FLAG) {

								// 早搏个数加1
								resultData.setPrematureBeat(resultData
										.getPrematureBeat() + 1);

								// 为了区分是哪个R波是早搏只能逐个判断其QRS实现

								/**
								 * 是：第一个R波的QRS时限大于40且RR间期小于-80
								 */
								if (resultData.getNewQRSAverage().get(i).get(j) > FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) < -80) {
									// PrematureVentricualrContractionIndex:存放心室期前收缩的R波下标
									if (resultData
											.getPrematureVentricualrContractionIndex()
											.size() == 0
											|| resultData
													.getPrematureVentricualrContractionIndex()
													.get(resultData
															.getPrematureVentricualrContractionIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j)) {
										resultData
												.getPrematureVentricualrContractionIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j)); // get(i).get(j)
									}

								}
								/**
								 * 是：第二个R波的QRS时限大于40且RR间期大于100(Internal)
								 */
								if (resultData.getNewQRSAverage().get(i)
										.get(j + 1) > FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) > Internal) {
									if (resultData
											.getPrematureVentricualrContractionIndex()
											.size() == 0
											|| resultData
													.getPrematureVentricualrContractionIndex()
													.get(resultData
															.getPrematureVentricualrContractionIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j + 1)) {
										resultData
												.getPrematureVentricualrContractionIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j + 1)); // get(i).get(j+1)
									}
								}
								/**
								 * 是：第三个R波的QRS时限大于40且RR间期为-80
								 */
								if (resultData.getNewQRSAverage().get(i)
										.get(j + 2) > FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) < -80) {
									if (resultData
											.getPrematureVentricualrContractionIndex()
											.size() == 0
											|| resultData
													.getPrematureVentricualrContractionIndex()
													.get(resultData
															.getPrematureVentricualrContractionIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j + 2)) {
										resultData
												.getPrematureVentricualrContractionIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j + 2)); // get(i).get(j+2)
									}
								}
							}
							/**
							 * 房性：对应的3个QRS时限中任何一个都小于40个数据点,作为房性
							 * 即：QRS时限小于40,是必备条件
							 */
							else {
								/**
								 * 是：第一个R波的QRS时限小于40且RR间期小于-80
								 */
								if (resultData.getNewQRSAverage().get(i).get(j) < FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) < -80) {
									// AtrialPrematureBeatIndex:存放心房期前收缩的对应R波下标
									if (resultData
											.getAtrialPrematureBeatIndex()
											.size() == 0
											|| resultData
													.getAtrialPrematureBeatIndex()
													.get(resultData
															.getAtrialPrematureBeatIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j)) {
										resultData
												.getAtrialPrematureBeatIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j));
									}

								}
								/**
								 * 是：第二个R波的QRS时限小于40且RR间期大于100(Internal)
								 */
								if (resultData.getNewQRSAverage().get(i)
										.get(j + 1) < FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) > Internal) {
									if (resultData
											.getAtrialPrematureBeatIndex()
											.size() == 0
											|| resultData
													.getAtrialPrematureBeatIndex()
													.get(resultData
															.getAtrialPrematureBeatIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j + 1)) {
										resultData
												.getAtrialPrematureBeatIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j + 1));
									}
								}
								/**
								 * 是：第三个R波的QRS时限小于40且RR间期为-80
								 */
								if (resultData.getNewQRSAverage().get(i)
										.get(j + 2) < FLAG
										&& ((resultData.getNewRpointList()
												.get(i).get(j + 2) - resultData
												.getNewRpointList().get(i)
												.get(j + 1)) - (resultData
												.getNewRpointList().get(i)
												.get(j + 1) - resultData
												.getNewRpointList().get(i)
												.get(j))) < -80) {
									if (resultData
											.getAtrialPrematureBeatIndex()
											.size() == 0
											|| resultData
													.getAtrialPrematureBeatIndex()
													.get(resultData
															.getAtrialPrematureBeatIndex()
															.size() - 1) != resultData
													.getNewRpointList().get(i)
													.get(j + 2)) {
										resultData
												.getAtrialPrematureBeatIndex()
												.add(resultData
														.getNewRpointList()
														.get(i).get(j + 2));
									}

								}
							}
						}

					}

				}
			}
		}

		resultData.setAtrialPrematureBeat(resultData
				.getAtrialPrematureBeatIndex().size());
		resultData.setPrematureVentricualrContraction(resultData
				.getPrematureVentricualrContractionIndex().size());

		if (resultData.getPrematureBeat() > 0) {
			isPrematureBeat = true;
		}
		return isPrematureBeat;
	}

	/**
	 * 判断一共有多少个两房负荷 判断P波是否存在异常（增宽，双峰，增高） 应该建立在这样的前提下：
	 * 
	 * 增宽：原本P波大于0.11s 增高：原本P波波峰高于0线值，P波峰高于0线2.5mm，即0.25mv
	 * 
	 * @return
	 */
	public int isBaoLoad(ArrayList<Float> dataY) {
		int bao = 0;
		for (int i = 0; i < resultData.getPpoint().size(); i++) {
			if (resultData.getDoubleP().get(i) > 0) {
				if (Math.abs(resultData.getBaseline().get(i)
						- dataY.get(resultData.getPpoint().get(i))) > 0.25) {
					bao++;
				}
			}
		}
		return bao;

	}

	/**
	 * 心率小于20
	 */
	private int SinusArrest = 20;

	/**
	 * 计算最大心率，最小心率 ，同时找停搏个数
	 * 
	 * @return minmax
	 */
	public int[] findMinMax() {
		int min = 220;
		int max = 0;
		int[] minmax = new int[2];
		for (int i = 0; i < resultData.getAllAverageHeart_rate().size(); i++) {
			// Log.d("123", "getAllAverageHeart_rate.get(i)是"
			// + resultData.getAllAverageHeart_rate().get(i));
			if (max < resultData.getAllAverageHeart_rate().get(i)) { // 求最大值和最小值
				max = resultData.getAllAverageHeart_rate().get(i);
			}
			if (min > resultData.getAllAverageHeart_rate().get(i)) {
				min = resultData.getAllAverageHeart_rate().get(i);
			}
			if (SinusArrest > resultData.getAllAverageHeart_rate().get(i)) {
				resultData.setSinusArrest(resultData.getSinusArrest() + 1);
			}
		}
		minmax[0] = min;
		minmax[1] = max;
		return minmax;

	}

	/**
	 * 判断是否RonT 在心室复极化期间，出现室性早搏，而且检测到R波落在T波上
	 * 
	 * @return
	 */
	public boolean ifRonT() {
		return false;
	}

	/**
	 * 判断是否心动过速
	 * 
	 * @param RR_duration
	 *            单位毫秒
	 * @return
	 */
	public boolean ifTachyrhythm(float RR_duration) {
		if (RR_duration < 500)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否心动过缓
	 * 
	 * @return
	 */
	public boolean ifBradycardia(float RR_duration) {
		if (RR_duration > 1500)
			return true;
		else
			return false;
	}

	/**
	 * 以下方法:在整个程序中没有被调用 同时,也不理解
	 */

	/**
	 * 判断是否房性期前收缩(房性早搏 ) 单位为毫秒，面积单位随意
	 * 
	 * @param HRV1
	 *            心室率之差1（心室率即RR间期）
	 * @param HRV2
	 *            心室率之差2（心室率即RR间期）
	 * @param HRV3
	 *            心室率之差3（心室率即RR间期）
	 * @param RR_duration
	 *            RR间期
	 * @param avg_RR_duration
	 *            平均RR间期
	 * @param s
	 *            QRS波曲线面积
	 * @param avg_s
	 *            平均QRS波曲线面积
	 * @return
	 */
	public boolean ifAtrialPrematureBeat(float HRV1, float HRV2, float HRV3,
			float RR_duration, float avg_RR_duration, float s, float avg_s) {
		if (HRV1 < 120 && HRV2 < 120 && HRV3 < 120
				&& RR_duration < 0.88 * avg_RR_duration && s < 1.5 * avg_s)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否停搏 已完成
	 * 
	 * @param RR_duration
	 *            RR间期，时间为毫秒
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean ifStopBeat(float RR_duration) {
		if (RR_duration > 3000)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否漏搏 已完成
	 * 
	 * @param RR_duration
	 *            RR间期，时间为毫秒 avg_RR_duration 平均RR间期，时间单位为毫秒
	 * @return
	 */
	public boolean ifMissBeat(float RR_duration, float avg_RR_duration) {
		if (RR_duration > 2.3 * avg_RR_duration
				&& RR_duration < 2.6 * RR_duration)
			return true;
		else
			return false;
	}

	/**
	 * 判断是否室性早搏
	 * 
	 * @param HRV1
	 *            心室率之差1（心室率即RR间期）
	 * @param HRV2
	 *            心室率之差2（心室率即RR间期）
	 * @param HRV3
	 *            心室率之差3（心室率即RR间期）
	 * @param QRS_duration
	 *            QRS时长 单位毫秒
	 * @return
	 */
	public boolean ifPrematureVentricualrContraction(float HRV1, float HRV2,
			float HRV3, float QRS_duration) {
		if (HRV1 > 120 && HRV2 > 120 && HRV3 > 120 && QRS_duration >= 120)
			return true;
		else
			return false;
	}

}
