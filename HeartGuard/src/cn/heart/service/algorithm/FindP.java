/**
 * @file FindP.java
 * @description 寻找R波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44 
 * @version 1.0
 */
package cn.heart.service.algorithm;

import java.util.List;

/**
 * @description 寻找P波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44
 * @version 1.0
 */
public class FindP {
	/**
	 * 待定t波起点
	 */
	private float pStart;
	/**
	 * 待定t波终点
	 */
	private float pEnd;
	/**
	 * R波点
	 */
	private int rPoint;
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
	private float pMax, pMin;
	/**
	 * tStart和tEnd之间的最大最小值对应的下标
	 */
	private int pMaxPoint, pMinPoint;
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
	private int pPoint;
	/**
	 * 待定找出来的t波起点和终点
	 */
	private int tempStart, tempEnd;
	/**
	 * 最后找出来的t波起点和终点
	 */
	private int pFinalStart, pFinalEnd;

	/**
	 * @param normalRR
	 *            心搏平均值，用来对每次的心搏做评估
	 * @param realRR
	 * @param data
	 */
	public FindP(float normalRR, float realRR, List<Float> data) {
		this.data = data;
		this.realRR = realRR;
		this.normalRR = normalRR;
		// 以下四个参数待定
		// c1,c2对应的状态：小于normalRR
		c1 = 0.62f;
		c2 = 0.9f;
		// c3,c4对应的状态：约等于normalRR
		c3 = 0.6f;
		c4 = 0.9f;
		// c5,c6对应的状态：大于normalRR
		c5 = 0.58f;
		c6 = 0.9f;
		// 以上四个参数要通过经验慢慢调节
		// sfrequency=360;
	}

	/**
	 * 分析P波相关参数总入口 执行该方法后通过gettPoint获取t波波峰所在点,通过gettFinalStart
	 * 和gettFinalEnd分别获取t波起点和终点 通过isUp()获取波形,true代表正向波，false代表负向波
	 */
	public void startToSearchT(int rPoint) {

		// Log.e("123", "rPoint========="+rPoint);
		this.rPoint = rPoint;
		analyseTempStartAndEnd(); // 分析出待定T波区间内的起始和终点值（tStart和tEnd的值）
		if (pStart >= data.size()) {
			setpPoint(0);
			return;
		}
		if (pEnd >= data.size()) {
			pEnd = data.size() - 1;
		}
		// 分析出待定T波区间内的最大最小值
		analyseMaxAndMin();
		// 基线值
		baseLine = (data.get((int) pStart) + data.get((int) pEnd)) / 2;
		// Math.abs()返回
		if (Math.abs(pMax - baseLine) > Math.abs(pMin - baseLine)) {
			// 参数值的绝对值
			// 设置倒置
			setUp(true);
			// 设置最大值
			setpPoint(pMaxPoint);
		} else {
			setUp(false);
			setpPoint(pMinPoint);
		}
		// 设置(T波的起点)
		setpFinalStart(findFinalStartTPoint((int) pStart, pPoint));
		// 设置(P波的起点)
		setpFinalEnd(findFinalEndTPoint(pPoint, (int) pEnd));
	}

	/**
	 * 分析出待定T波区间内的起始和终点值（tStart和tEnd的值）
	 */
	public void analyseTempStartAndEnd() {
		// 首先这里realRR 代表的是此次心搏所包含的采样个数，而不是心率（时间上）
		if (realRR < normalRR - OFFSET) {
			pStart = rPoint + c1 * realRR;
			pEnd = rPoint + c2 * realRR;
			setTempStart((int) pStart);
			setTempEnd((int) pEnd);
			// Log.d("123", "realRR~~<<<<<<~~normalRR=========");
		} else if (realRR > normalRR - OFFSET && realRR < normalRR + OFFSET) {
			pStart = rPoint + c3 * realRR;
			pEnd = rPoint + c4 * realRR;
			setTempStart((int) pStart);
			setTempEnd((int) pEnd);
			// Log.d("123", "realRR~========~normalRR*********");
		} else {
			pStart = rPoint + c5 * realRR;
			pEnd = rPoint + c6 * realRR;
			setTempStart((int) pStart);
			setTempEnd((int) pEnd);
			// Log.d("123", "realRR~>>>>>>>>>~~normalRR=========");
		}
		// Log.e("123", "pStart========"+pStart);
		// Log.e("123", "pEnd========="+pEnd);

		// if(realRR>normalRR){
		// pStart=rPoint+c3*realRR;
		// pEnd=rPoint+c4*realRR;
		// settFinalStart((int) pStart);
		// settFinalEnd((int) pEnd);
		// Log.e("123", "realRR>normalRR*********");
		// Log.e("123", "pStart*********"+pStart);
		// Log.e("123", "pEnd*********"+pEnd);
		// }
		// else{
		// pStart=rPoint+c5*realRR;
		// pEnd=rPoint+c6*realRR;
		// settFinalStart((int) pStart);
		// settFinalEnd((int) pEnd);
		// Log.e("123", "realRR<normalRR=========");
		// Log.e("123", "pStart========"+pStart);
		// Log.e("123", "pEnd========="+pEnd);
		// }
	}

	/**
	 * 分析出待定T波区间内的最大最小值，即T波波峰点
	 */
	public void analyseMaxAndMin() {
		float tempMax, tempMin;
		tempMax = data.get((int) pStart);
		tempMin = data.get((int) pStart);
		for (int i = (int) pStart; i < pEnd; i++) {
			if (tempMax < data.get(i)) {
				tempMax = data.get(i);
				pMaxPoint = i;
			}
			if (tempMin > data.get(i)) {
				tempMin = data.get(i);
				pMinPoint = i;
			}
		}
		// pMax=tempMax;
		// pMin=tempMin;
		// 这个地方就是最大的错误
		pMax = data.get(pMaxPoint);
		pMin = data.get(pMinPoint);
		// Log.e("123", "tMaxPoint最大值下标========"+pMaxPoint);
		// Log.e("123", "tMinPoint最小值下标========"+pMinPoint);
	}

	/**
	 * Z(n)=f(n0)+(n-n0)(f(ne)-f(n0))/(ne-n0) n0<=n<=ne
	 * 
	 * @param n0
	 *            ,ne为常量,n自变量
	 * @return D(n),D(n)=|f(n)-z(n)|
	 */
	public float changeEquationStart(int n0, int ne, int n) {
		float z, d;
		z = data.get(n0) + (n - n0) * (data.get(ne) - data.get(n0)) / (ne - n0);
		d = Math.abs(data.get(n) - z);
		// Log.d("123", "data.get(n)========"+data.get(n));
		// Log.d("123", "z========"+z);
		// Log.d("123", "Math.abs(data.get(n)-z)========"+d);

		return d;

	}

	/**
	 * Z(n)=f(n0)+(n-n0)(f(ne)-f(n0))/(ne-n0) n0<=n<=ne
	 * 
	 * @param n0
	 *            ,ne为常量,n自变量
	 * @return D(n),D(n)=|f(n)-z(n)|
	 */
	public float changeEquationEnd(int n0, int ne, int n) {
		float z, d;
		z = data.get(n0) + (n - ne) * (data.get(ne) - data.get(n0)) / (ne - n0);
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
	private int findFinalStartTPoint(int n0, int ne) {
		float result = changeEquationStart(n0, ne, n0);
		// Log.e("123", "最大值是多少啊========"+result);
		int m = n0;
		for (int i = n0 + 1; i <= ne; i++) {
			float d = changeEquationStart(n0, ne, i);
			if (result < d) {
				result = d;
				m = i;
				// Log.e("123", "最大值是多少啊========"+result);
			}
			// Log.d("123", "findFinalStartTPoint起始值是多少啊========"+m);
		}

		return m;

	}

	/**
	 * n0为tPoint,ne为tEnd时，返回终点，n0为tStart,ne为tPoint时，返回起点 ne>n0
	 * 
	 * @param n0
	 * @param ne
	 * @return
	 */
	private int findFinalEndTPoint(int n0, int ne) {
		float result = changeEquationEnd(n0, ne, n0);
		int m = n0;
		for (int i = n0; i <= ne; i++) {
			if (result < changeEquationEnd(n0, ne, i)) {
				result = changeEquationEnd(n0, ne, i);
				m = i;
			}
			// Log.d("123", "findFinalStartTPoint终点值是多少啊========"+m);
		}
		return m;
	}

	// 大于波峰范围的值
	private final int FLAG = 6;

	/**
	 * 判断P波是否存在异常（增宽，双峰，增高） 应该建立在这样的前提下：
	 * 
	 * 增宽：原本P波大于0.11s
	 * 
	 * 增高：原本P波波峰高于0线值，P波峰高于0线2.5mm，即0.25mv
	 * 
	 * @return 返回双峰p波的下标值
	 */
	// 在其他函数中调用
	public int getDoublePpoint() {
		// 存放双峰p波的下标值
		int maxpoint1 = this.pPoint - FLAG;
		int maxpoint2 = this.pPoint + FLAG;

		// 假定最大值
		float max1 = data.get(this.pFinalStart);
		float max2 = data.get(this.pFinalEnd);
		if (this.pFinalEnd - this.pFinalStart > 39) { // ???????
			// 从pFinalStart向p点偏移FLAG点找最大值，若不是pPoint-FLAG说明可能存在双峰
			for (int i = this.pFinalStart; i < this.pPoint - FLAG + 1; i++) {
				if (max1 < data.get(i)) {
					max1 = data.get(i);
					maxpoint1 = i;
					// Log.e("123444444",
					// "max1最大值是什么啊"+max1+"最大值下标呢是什么啊"+maxpoint1);
				}
			}
			// 从p点偏移FLAG点向pFinalEnd找最大值，若不是pPoint+FLAG说明可能存在双峰
			for (int i = this.pPoint + FLAG; i < this.pFinalEnd; i++) {
				if (max2 < data.get(i)) {
					max2 = data.get(i);
					maxpoint2 = i;
					// Log.e("123444444",
					// "max2最大值是什么啊"+max2+"最大值下标呢是什么啊"+maxpoint2);
				}
			}
			if ((maxpoint1 != (this.pPoint - FLAG))
					&& (data.get(maxpoint1) > data.get(this.pPoint - FLAG))
					&& Math.abs(max1 - data.get(this.pPoint)) < 0.05) {
				return maxpoint1;
			}
			if ((maxpoint2 != (this.pPoint + FLAG))
					&& (data.get(maxpoint2) > data.get(this.pPoint + FLAG))
					&& Math.abs(max2 - data.get(this.pPoint)) < 0.05) {
				return maxpoint2;
			}
		}

		return 0;
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

	public float getpStart() {
		return pStart;
	}

	public void setpStart(float pStart) {
		this.pStart = pStart;
	}

	public float getpEnd() {
		return pEnd;
	}

	public void setpEnd(float pEnd) {
		this.pEnd = pEnd;
	}

	public int getpPoint() {
		return pPoint;
	}

	public void setpPoint(int pPoint) {
		this.pPoint = pPoint;
	}

	public int getpFinalStart() {
		return pFinalStart;
	}

	public void setpFinalStart(int pFinalStart) {
		this.pFinalStart = pFinalStart;
	}

	public int getpFinalEnd() {
		return pFinalEnd;
	}

	public void setpFinalEnd(int pFinalEnd) {
		this.pFinalEnd = pFinalEnd;
	}

}
