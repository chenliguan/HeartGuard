/**
 * @file ResultData.java
 * @description 测量结果数据
 * @author Guan
 * @date 2015-6-8 下午9:56:49 
 * @version 1.0
 */
package cn.heart.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 测量结果数据
 * @author Guan
 * @date 2015-6-8 下午9:56:49
 * @version 1.0
 */
public class ResultData {
	private List<Float> logData_dataGate;
	private List<Float> logData_slopeGate;
	private List<String> logData_ifPositive;
	private List<Float> avgList;
	private List<Float> max_avg;
	private List<Float> min_avg;

	private ArrayList<Integer> Tstart;
	private ArrayList<Integer> Tend;
	private ArrayList<Integer> tempTstart;
	private ArrayList<Integer> tempTend;

	private int R_Answer; // R点的导诊答案
	private int T_Answer; // T点的导诊答案
	private int P_Answer; // P点的导诊答案
	private int SEQUENCE_Answer; // 顺序问题的导诊答案

	/**
	 * 双峰P波，或者说是P波增宽的下标值
	 */
	private ArrayList<Integer> DoubleP;
	/**
	 * P波的方向
	 */
	private ArrayList<Boolean> PIsUp;
	private ArrayList<Integer> Pstart;
	private ArrayList<Integer> Pend;
	/**
	 * T波的方向
	 */
	private ArrayList<Boolean> TIsUp;
	private ArrayList<Integer> tempPstart;
	private ArrayList<Integer> tempPend;

	private ArrayList<Integer> Qpoint;
	private ArrayList<Integer> Qstart;
	private ArrayList<Integer> Spoint;
	private ArrayList<Integer> Rpoint;
	private ArrayList<Integer> Tpoint;
	private ArrayList<Integer> Ppoint;

	/**
	 * TP段，基线值
	 */
	private ArrayList<Float> Baseline;

	public ArrayList<Float> getBaseline() {
		return Baseline;
	}

	public void setBaseline(ArrayList<Float> baseline) {
		Baseline = baseline;
	}

	/**
	 * 平均PR间期
	 */
	private float AveragePR;

	/**
	 * 主要病症
	 */
	private String Symptoms;
	/**
	 * 存放方差
	 */
	private ArrayList<Float> Variance = new ArrayList<Float>();
	/**
	 * 存放QRS的平均值，方便与方差配对使用，鉴别室上性，室性
	 */
	private ArrayList<Float> QRSAverage = new ArrayList<Float>();
	/**
	 * 存放QRS的平均值，方便与方差配对使用，鉴别室上性，室性
	 */
	private ArrayList<ArrayList<Float>> NewQRSAverage;

	/**
	 * 存放心室期前收缩的R波下标
	 */
	private ArrayList<Integer> PrematureVentricualrContractionIndex;

	/**
	 * 存放心房期前收缩的对应R波下标
	 */
	private ArrayList<Integer> AtrialPrematureBeatIndex;

	/**
	 * 整段数据的RR间期的差值（心室率）
	 */
	private ArrayList<Integer> RRInternal;
	/**
	 * 整段数据的存在多少个窦性停搏
	 */
	private int SinusArrest;

	/**
	 * 存放每个方差对应的R波
	 */
	private ArrayList<ArrayList<Integer>> NewRpointList;

	/**
	 * 存放RR间期的平均值 normalrr保存多少个数据
	 */
	private float RRAverage;
	/**
	 * 弃用 早搏个数（包括室上性，室性）
	 */
	private int PrematureBeat = 0;
	/**
	 * 室上性个数 小于42
	 */
	private int AtrialPrematureBeat = 0;
	/**
	 * 室性个数 大于42
	 */
	private int PrematureVentricualrContraction = 0;
	/**
	 * RR平均心率
	 */
	private int AverageHeart_rate = 0;
	/**
	 * 整段数据的RR平均心率
	 */
	private ArrayList<Integer> AllAverageHeart_rate;
	/**
	 * 每4.5秒数据的RR平均心率
	 */
	private ArrayList<Integer> ItemAverageHeart_rate;

	/**
	 * R心跳个数
	 */
	private int R = 0;
	/**
	 * QRS平均时间
	 */
	private float AverageQRS = 0;
	/**
	 * QT平均时间
	 */
	private float AverageQT = 0;
	/**
	 * QTcB平均时间
	 */
	private double AverageQTcB = 0;

	public ResultData() {
		logData_dataGate = new ArrayList<Float>();
		logData_slopeGate = new ArrayList<Float>();
		logData_ifPositive = new ArrayList<String>();
		avgList = new ArrayList<Float>();
		max_avg = new ArrayList<Float>();
		min_avg = new ArrayList<Float>();
		Tstart = new ArrayList<Integer>();
		Tend = new ArrayList<Integer>();
		tempTstart = new ArrayList<Integer>();
		tempTend = new ArrayList<Integer>();

		DoubleP = new ArrayList<Integer>();
		Pstart = new ArrayList<Integer>();
		Pend = new ArrayList<Integer>();
		tempPstart = new ArrayList<Integer>();
		tempPend = new ArrayList<Integer>();
		Qpoint = new ArrayList<Integer>();
		Spoint = new ArrayList<Integer>();
		Rpoint = new ArrayList<Integer>();
		Tpoint = new ArrayList<Integer>();
		Ppoint = new ArrayList<Integer>();
		TIsUp = new ArrayList<Boolean>();
		PIsUp = new ArrayList<Boolean>();

		Qstart = new ArrayList<Integer>();
		Baseline = new ArrayList<Float>();
		NewQRSAverage = new ArrayList<ArrayList<Float>>();
		RRInternal = new ArrayList<Integer>();
		NewRpointList = new ArrayList<ArrayList<Integer>>();

		PrematureVentricualrContractionIndex = new ArrayList<Integer>();
		AtrialPrematureBeatIndex = new ArrayList<Integer>();
		AllAverageHeart_rate = new ArrayList<Integer>();
		ItemAverageHeart_rate = new ArrayList<Integer>();
	}

	public List<Float> getLogData_dataGate() {
		return logData_dataGate;
	}

	public void setLogData_dataGate(List<Float> logData_dataGate) {
		this.logData_dataGate = logData_dataGate;
	}

	public List<Float> getLogData_slopeGate() {
		return logData_slopeGate;
	}

	public void setLogData_slopeGate(List<Float> logData_slopeGate) {
		this.logData_slopeGate = logData_slopeGate;
	}

	public List<String> getLogData_ifPositive() {
		return logData_ifPositive;
	}

	public void setLogData_ifPositive(List<String> logData_ifPositive) {
		this.logData_ifPositive = logData_ifPositive;
	}

	public List<Float> getAvgList() {
		return avgList;
	}

	public void setAvgList(List<Float> avgList) {
		this.avgList = avgList;
	}

	public List<Float> getMax_avg() {
		return max_avg;
	}

	public void setMax_avg(List<Float> max_avg) {
		this.max_avg = max_avg;
	}

	public List<Float> getMin_avg() {
		return min_avg;
	}

	public void setMin_avg(List<Float> min_avg) {
		this.min_avg = min_avg;
	}

	public ArrayList<Integer> getTstart() {
		return Tstart;
	}

	public void setTstart(ArrayList<Integer> tstart) {
		Tstart = tstart;
	}

	public ArrayList<Integer> getTend() {
		return Tend;
	}

	public void setTend(ArrayList<Integer> tend) {
		Tend = tend;
	}

	public ArrayList<Integer> getTempTstart() {
		return tempTstart;
	}

	public void setTempTstart(ArrayList<Integer> tempTstart) {
		this.tempTstart = tempTstart;
	}

	public ArrayList<Integer> getTempTend() {
		return tempTend;
	}

	public ArrayList<Integer> getTpoint() {
		return Tpoint;
	}

	public void setTpoint(ArrayList<Integer> tpoint) {
		Tpoint = tpoint;
	}

	public ArrayList<Integer> getPpoint() {
		return Ppoint;
	}

	public void setPpoint(ArrayList<Integer> ppoint) {
		Ppoint = ppoint;
	}

	public void setTempTend(ArrayList<Integer> tempTend) {
		this.tempTend = tempTend;
	}

	public ArrayList<Integer> getQpoint() {
		return Qpoint;
	}

	public void setQpoint(ArrayList<Integer> qpoint) {
		Qpoint = qpoint;
	}

	public ArrayList<Integer> getSpoint() {
		return Spoint;
	}

	public void setSpoint(ArrayList<Integer> spoint) {
		Spoint = spoint;
	}

	public ArrayList<Integer> getRpoint() {
		return Rpoint;
	}

	public void setRpoint(ArrayList<Integer> rpoint) {
		Rpoint = rpoint;
	}

	public ArrayList<Float> getVariance() {
		return Variance;
	}

	public void setVariance(ArrayList<Float> variance) {
		Variance = variance;
	}

	public ArrayList<Float> getQRSAverage() {
		return QRSAverage;
	}

	public void setQRSAverage(ArrayList<Float> qRSAverage) {
		QRSAverage = qRSAverage;
	}

	public int getPrematureBeat() {
		return PrematureBeat;
	}

	public void setPrematureBeat(int prematureBeat) {
		PrematureBeat = prematureBeat;
	}

	public int getAtrialPrematureBeat() {
		return AtrialPrematureBeat;
	}

	public void setAtrialPrematureBeat(int atrialPrematureBeat) {
		AtrialPrematureBeat = atrialPrematureBeat;
	}

	public int getPrematureVentricualrContraction() {
		return PrematureVentricualrContraction;
	}

	public void setPrematureVentricualrContraction(
			int prematureVentricualrContraction) {
		PrematureVentricualrContraction = prematureVentricualrContraction;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public float getAverageQRS() {

		return AverageQRS;
	}

	public void countAverageQRS() {
		float sum = 0;
		for (int i = 0; i < QRSAverage.size(); i++) {
			sum += QRSAverage.get(i);
		}
		AverageQRS = sum / QRSAverage.size() / 360 * 1000;
	}

	public void setAverageQRS(float averageQRS) {
		AverageQRS = averageQRS;
	}

	public String getSymptoms() {
		return Symptoms;
	}

	public void setSymptoms(String symptoms) {
		Symptoms = symptoms;
	}

	public ArrayList<Integer> getDoubleP() {
		return DoubleP;
	}

	public void setDoubleP(ArrayList<Integer> doubleP) {
		DoubleP = doubleP;
	}

	public ArrayList<Integer> getPstart() {
		return Pstart;
	}

	public void setPstart(ArrayList<Integer> pstart) {
		Pstart = pstart;
	}

	public ArrayList<Integer> getPend() {
		return Pend;
	}

	public void setPend(ArrayList<Integer> pend) {
		Pend = pend;
	}

	public ArrayList<Integer> getTempPstart() {
		return tempPstart;
	}

	public void setTempPstart(ArrayList<Integer> tempPstart) {
		this.tempPstart = tempPstart;
	}

	public ArrayList<Integer> getTempPend() {
		return tempPend;
	}

	public void setTempPend(ArrayList<Integer> tempPend) {
		this.tempPend = tempPend;
	}

	public float getAverageQT() {

		return AverageQT;
	}

	public void countAverageQT() {
		float sum = 0;
		int index = 0;
		for (int i = 0; i < Qpoint.size(); i++) {
			try {
				if (Qpoint.get(i) != 0 && Tpoint.get(i) != 0) {
					sum += Tpoint.get(i) - Qpoint.get(i);
					index++;
				}
			} catch (Exception e) {
				// Tpoint.size()<=i
			}
		}
		AverageQT = sum / index / 360 * 1000;
	}

	public void setAverageQT(float averageQT) {

		AverageQT = averageQT;
	}

	public int getAverageHeart_rate() {
		return AverageHeart_rate;
	}

	public void setAverageHeart_rate(int averageHeart_rate) {
		AverageHeart_rate = averageHeart_rate;
	}

	public float getRRAverage() {
		return RRAverage;
	}

	public void setRRAverage(float rRAverage) {
		RRAverage = rRAverage;
	}

	public float getAveragePR() {
		return AveragePR;
	}

	public void countAveragePR() {
		float sum = 0;
		int index = 0;
		for (int i = 0; i < Ppoint.size(); i++) {
			try {
				if (Rpoint.get(i) != 0 && Ppoint.get(i) != 0
						&& i + 1 < Rpoint.size()) {
					sum += Rpoint.get(i + 1) - Ppoint.get(i);
					index++;
				}
			} catch (Exception e) {
				// Tpoint.size()<=i
			}
		}
		AveragePR = sum / index / 360 * 1000;
	}

	public void setAveragePR(float averagePR) {
		AveragePR = averagePR;
	}

	public double getAverageQTcB() {
		return AverageQTcB;
	}

	public void countAverageQTcB() {
		AverageQTcB = AverageQT / Math.sqrt(RRAverage / 1000);
	}

	public void setAverageQTcB(double averageQTcB) {
		AverageQTcB = averageQTcB;
	}

	public ArrayList<Boolean> getPIsUp() {
		return PIsUp;
	}

	public void setPIsUp(ArrayList<Boolean> pIsUp) {
		PIsUp = pIsUp;
	}

	public ArrayList<Boolean> getTIsUp() {
		return TIsUp;
	}

	public void setTIsUp(ArrayList<Boolean> tIsUp) {
		TIsUp = tIsUp;
	}

	public ArrayList<Integer> getQstart() {
		return Qstart;
	}

	public void setQstart(ArrayList<Integer> qstart) {
		Qstart = qstart;
	}

	public int getR_Answer() {
		return R_Answer;
	}

	public void setR_Answer(int r_Answer) {
		R_Answer = r_Answer;
	}

	public int getT_Answer() {
		return T_Answer;
	}

	public void setT_Answer(int t_Answer) {
		T_Answer = t_Answer;
	}

	public int getP_Answer() {
		return P_Answer;
	}

	public void setP_Answer(int p_Answer) {
		P_Answer = p_Answer;
	}

	public int getSEQUENCE_Answer() {
		return SEQUENCE_Answer;
	}

	public void setSEQUENCE_Answer(int sEQUENCE_Answer) {
		SEQUENCE_Answer = sEQUENCE_Answer;
	}

	public ArrayList<ArrayList<Integer>> getNewRpointList() {
		return NewRpointList;
	}

	public void setNewRpointList(ArrayList<ArrayList<Integer>> newRpointList) {
		NewRpointList = newRpointList;
	}

	public ArrayList<Integer> getRRInternal() {
		return RRInternal;
	}

	public void setRRInternal(ArrayList<Integer> rRInternal) {
		RRInternal = rRInternal;
	}

	public ArrayList<Integer> getAtrialPrematureBeatIndex() {
		return AtrialPrematureBeatIndex;
	}

	public void setAtrialPrematureBeatIndex(
			ArrayList<Integer> atrialPrematureBeatIndex) {
		AtrialPrematureBeatIndex = atrialPrematureBeatIndex;
	}

	public ArrayList<Integer> getPrematureVentricualrContractionIndex() {
		return PrematureVentricualrContractionIndex;
	}

	public void setPrematureVentricualrContractionIndex(
			ArrayList<Integer> prematureVentricualrContractionIndex) {
		PrematureVentricualrContractionIndex = prematureVentricualrContractionIndex;
	}

	public ArrayList<ArrayList<Float>> getNewQRSAverage() {
		return NewQRSAverage;
	}

	public void setNewQRSAverage(ArrayList<ArrayList<Float>> newQRSAverage) {
		NewQRSAverage = newQRSAverage;
	}

	public ArrayList<Integer> getAllAverageHeart_rate() {
		return AllAverageHeart_rate;
	}

	public void setAllAverageHeart_rate(ArrayList<Integer> allAverageHeart_rate) {
		AllAverageHeart_rate = allAverageHeart_rate;
	}

	public int getSinusArrest() {
		return SinusArrest;
	}

	public void setSinusArrest(int sinusArrest) {
		SinusArrest = sinusArrest;
	}

	public ArrayList<Integer> getItemAverageHeart_rate() {
		return ItemAverageHeart_rate;
	}

	public void setItemAverageHeart_rate(
			ArrayList<Integer> itemAverageHeart_rate) {
		ItemAverageHeart_rate = itemAverageHeart_rate;
	}

}
