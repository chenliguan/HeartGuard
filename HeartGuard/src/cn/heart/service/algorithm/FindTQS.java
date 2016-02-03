/**
 * @file FindTQS.java
 * @description 寻找TQS波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44 
 * @version 1.0
 */
package cn.heart.service.algorithm;

import java.util.List;

import android.util.Log;

/**
 * @description 寻找TQS波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44
 * @version 1.0
 */
public class FindTQS {
	/**
	 * 待定t波起点
	 */
	private float tStart;
	/**
	 * 待定t波终点
	 */
	private float tEnd;
	/**
	 * R波点
	 */
	private int rPoint;
	private int Qpoint, Spoint;
	private int QStart, SEnd;
	/**
	 * 确定T波的常量，根据RR间期来适配c1-c2,c3-c4,c5-c6
	 */
	private float c1, c2, c3, c4, c5, c6;
	/**
	 * 本次检测的平均RR间期值
	 */
	private float normalRR;
	/**
	 * 在平均心率normalRR左右的偏移量对应c3-c4
	 */
	private static final int OFFSET = 30;
	/**
	 * 实际RR间期
	 */
	private float realRR;
	/**
	 * 基线
	 */
	private float baseLine;
	/**
	 * tStart和tEnd之间的最大最小值
	 */
	private float tMax, tMin;
	/**
	 * tStart和tEnd之间的最大最小值对应的下标
	 */
	private int tMaxPoint, tMinPoint;
	/**
	 * 数据
	 */
	private List<Float> data;
	// private float sfrequency; //采样频率
	/**
	 * 波形方向
	 */
	private boolean isUp;
	/**
	 * t波波峰所在点
	 */
	private int tPoint;
	/**
	 * 待定找出来的t波起点和终点
	 */
	private int tempStart, tempEnd;
	/**
	 * 最后找出来的t波起点和终点
	 */
	private int tFinalStart, tFinalEnd;

	/**
	 * @param normalRR
	 *            心搏平均值，用来对每次的心搏做评估
	 * @param realRR
	 * @param data
	 */
	public FindTQS(float normalRR, float realRR, List<Float> data) {
		this.data = data;
		this.realRR = realRR;
		this.normalRR = normalRR;
		// 以下四个参数待定
		// c1,c2对应的状态：小于normalRR
		c1 = 0.18f;
		c2 = 0.5f;
		// c3,c4对应的状态：约等于normalRR
		c3 = 0.12f;
		c4 = 0.6f;
		// c5,c6对应的状态：大于normalRR
		c5 = 0.1f;
		c6 = 0.6f;
		// 以上四个参数要通过经验慢慢调节
		// sfrequency=360;
	}

	/**
	 * 分析T波相关参数总入口 执行该方法后通过gettPoint获取t波波峰所在点,通过gettFinalStart
	 * 和gettFinalEnd分别获取t波起点和终点 通过isUp()获取波形,true代表正向波，false代表负向波
	 */
	public void startToSearchT(int rPoint) {
		this.rPoint = rPoint;
		analyseTempStartAndEnd();
		if (tStart >= data.size()) {
			settPoint(0);
			return;
		}
		if (tEnd >= data.size()) {
			tEnd = data.size() - 1;
		}
		analyseMaxAndMin();
		baseLine = (data.get((int) tStart) + data.get((int) tEnd)) / 2;
		if (Math.abs(tMax - baseLine) > Math.abs(tMin - baseLine)) {
			setUp(true);
			settPoint(tMaxPoint);
			Log.e("123", "我是向上的=========TTTTTTTTT");
		} else {
			setUp(false);
			settPoint(tMinPoint);
			Log.d("123", "我是向下的=========TTTTTTTTT");
		}
		// findFinalStartAndEndTPoint((int) tStart, tPoint);
		// findFinalStartAndEndTPoint(tPoint, (int) tEnd);
		settFinalStart(findFinalStartAndEndTPoint((int) tStart, tPoint));
		settFinalEnd(findFinalStartAndEndTPoint(tPoint, (int) tEnd));
		// tPoint就是最终找到的t波峰所在点,tFinalStart和tFinalEnd
		// 就是最终找到的t波起点和终点
		// isUp代表t波的波形，true代表正向波，false代表负向波
	}

	/**
	 * Q波检测
	 */
	public void findQSpoint() {
		float QslopeGate = 0;
		if (rPoint - 3 > 0)
			// 经验阀值*斜率=斜率门限值
			QslopeGate = 0.3f * (data.get(rPoint) - data.get(rPoint - 3)) / 3;
		for (int i = rPoint; i >= 0; i--) {
			if (i - 3 >= 0 && (data.get(i) - data.get(i - 3)) / 3 < 0) {
				// 确认Q波。i是从R波下标往回确认
				if (makeSureQpoint(i, QslopeGate)) {
					setQpoint(i);
					// 寻找Q波起点
					setQStart(findQStart(i));
					break;
				}
			}
		}
		if (rPoint + 5 < data.size()) {
		}
		for (int i = rPoint + 2; i < data.size(); i++) {
			if (i + 5 < data.size() && (data.get(i + 5) - data.get(i)) > 0) {
				setSpoint(i + 3);
				break;
			}
		}
	}

	/**
	 * 确认Q波下标
	 * 
	 * @param Qpoint
	 * @param slopeGate
	 * @return
	 */
	public boolean makeSureQpoint(int Qpoint, float slopeGate) {
		int start = Qpoint;
		int maxPoint = Qpoint;
		if (start - 10 >= 0) {
			for (int i = start; i > start - 10; i--) {
				if (data.get(maxPoint) < data.get(i)) {
					// 设置点前10个数中最大值
					maxPoint = i;
				}
			}
		}
		int prePoint = maxPoint;
		if (maxPoint - 5 >= 0) {
			prePoint = maxPoint - 5;
		} else {
			return true;
		}
		// 主要判断标准
		if ((data.get(maxPoint) - data.get(prePoint)) / 5 > slopeGate) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 确认S波下标
	 * 
	 * @param Spoint
	 * @param slopeGate
	 * @return
	 */
	public boolean makeSureSpoint(int Spoint, float slopeGate) {
		int start = Spoint;
		int maxSoint = Spoint;
		if (start + 3 < data.size()) {
			for (int i = start; i < start + 3; i++) {
				if (data.get(maxSoint) < data.get(i)) {
					maxSoint = i;
				}
			}
		}
		int endSoint = maxSoint;
		if (maxSoint + 1 < data.size()) {
			endSoint = maxSoint + 1;
		} else
			return true;
		if ((data.get(endSoint) - data.get(maxSoint)) < 0) {

			return false;
		} else {
			return true;
		}
	}

	/**
	 * Q波起止点检测，斜率最大的点视为Q波起点
	 * 
	 * @param minQ
	 *            Q波的谷值及下标
	 * @return
	 */
	public int findQStart(int minQ) {
		int start = minQ;
		float slope;
		if (minQ - 2 >= 0) {
			// 后一点的斜率
			slope = data.get(minQ - 2) - data.get(minQ - 1);
			for (int i = minQ - 2; i > minQ - 10; i--) {
				if (i - 1 < 0 || i + 1 > data.size()) {
					break;
				}
				// 前一点的斜率
				if (slope < data.get(i - 1) - data.get(i + 1)) {
					start = i;
				}
			}
		}
		return start;
	}

	/**
	 * 返回这段数据的QRS波群的下标差
	 * 
	 * 改 利用Spoint-QStart
	 * 
	 * @return
	 */
	public float getQRSAverage() {
		float QRSAverage = 0;
		QRSAverage = Spoint - QStart;
		return QRSAverage;
	}

	public int getQpoint() {
		return Qpoint;
	}

	public void setQpoint(int qpoint) {
		Qpoint = qpoint;
	}

	public int getSpoint() {
		return Spoint;
	}

	public void setSpoint(int spoint) {
		Spoint = spoint;
	}

	/**
	 * 分析出待定T波区间内的起始和终点值（tStart和tEnd的值）
	 */
	public void analyseTempStartAndEnd() {
		// 首先这里realRR 代表的是此次心搏所包含的采样个数，而不是心率（时间上）
		if (realRR < normalRR - OFFSET) {
			tStart = rPoint + c1 * realRR;
			tEnd = rPoint + c2 * realRR;
			setTempStart((int) tStart);
			setTempEnd((int) tEnd);
			// Log.e("123", "realRR<<<<<<<<~~normalRR=====TTTTTTTTTT");
		} else if (realRR > normalRR - OFFSET && realRR < normalRR + OFFSET) {
			tStart = rPoint + c3 * realRR;
			tEnd = rPoint + c4 * realRR;
			setTempStart((int) tStart);
			setTempEnd((int) tEnd);
			// Log.e("123", "realRR===========~~normalRR======TTTTTTTTTT");
		} else {
			tStart = rPoint + c5 * realRR;
			tEnd = rPoint + c6 * realRR;
			setTempStart((int) tStart);
			setTempEnd((int) tEnd);
			// Log.e("123", "realRR>>>>>>>>>>~~normalRR======TTTTTTTTTT");
		}
	}

	/**
	 * 分析出待定T波区间内的最大最小值
	 */
	public void analyseMaxAndMin() {
		float tempMax, tempMin;
		tempMax = data.get((int) tStart);
		tempMin = data.get((int) tStart);
		for (int i = (int) tStart; i < tEnd; i++) {
			if (tempMax < data.get(i)) {
				tempMax = data.get(i);
				tMaxPoint = i;
			}
			if (tempMin > data.get(i)) {
				tempMin = data.get(i);
				tMinPoint = i;
			}
		}
		// tMax=tempMax;
		// tMin=tempMin;
		// 这个地方就是最大的错误
		tMax = data.get(tMaxPoint);
		tMin = data.get(tMinPoint);
		// Log.e("123", "tMaxPoint最大值下标========"+tMaxPoint);
		// Log.e("123", "tMinPoint最小值下标========"+tMinPoint);
	}

	/**
	 * Z(n)=f(n0)+(n-n0)(f(ne)-f(n0))/(ne-n0) n0<=n<=ne
	 * 
	 * @param n0
	 *            ,ne为常量,n自变量
	 * @return D(n),D(n)=|f(n)-z(n)|
	 */
	public float changeEquation(int n0, int ne, int n) {
		float z, d;
		z = data.get(n0) + (n - n0) * (data.get(ne) - data.get(n0)) / (ne - n0);
		d = Math.abs(data.get(n) - z);
		return d;

	}

	/**
	 * n0为tPoint,ne为tEnd时，返回终点，n0为tStart,ne为tPoint时，返回起点 ne>n0
	 * 
	 * @param n0
	 * @param ne
	 * @return
	 */
	private int findFinalStartAndEndTPoint(int n0, int ne) {
		float result = changeEquation(n0, ne, n0);
		int m = n0;
		for (int i = n0; i <= ne; i++) {
			if (result < changeEquation(n0, ne, i)) {
				result = changeEquation(n0, ne, i);
				m = i;
			}
		}
		return m;

	}

	public boolean isUp() {
		return isUp;
	}

	public void setUp(boolean isUp) {
		this.isUp = isUp;
	}

	public int getTempStart() {
		return tempStart;
	}

	public void setTempStart(int tempStart) {
		this.tempStart = tempStart;
	}

	public int getTempEnd() {
		return tempEnd;
	}

	public void setTempEnd(int tempEnd) {
		this.tempEnd = tempEnd;
	}

	public int gettFinalStart() {
		return tFinalStart;
	}

	public void settFinalStart(int tFinalStart) {
		this.tFinalStart = tFinalStart;
	}

	public int gettFinalEnd() {
		return tFinalEnd;
	}

	public void settFinalEnd(int tFinalEnd) {
		this.tFinalEnd = tFinalEnd;
	}

	public int gettPoint() {
		return tPoint;
	}

	public void settPoint(int tPoint) {
		this.tPoint = tPoint;
	}

	public int getQStart() {
		return QStart;
	}

	public void setQStart(int qStart) {
		QStart = qStart;
	}

	public int getSEnd() {
		return SEnd;
	}

	public void setSEnd(int sEnd) {
		SEnd = sEnd;
	}
}
