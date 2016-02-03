/**
 * @file FindR.java
 * @description 寻找R波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44 
 * @version 1.0
 */
package cn.heart.service.algorithm;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.util.SparseArray;

/**
 * @description 寻找R波类
 * @author Aries
 * @date 2015-6-19 下午9:46:44
 * @version 1.0
 */
public class FindR {
	/**
	 * 2s采集点的个数（2*360）
	 */
	private int POINTNUM;
	/**
	 * 完成分析
	 */
	private static final int END = -1;
	/**
	 * 读入点的总数
	 */
	private int ALLPOINT;
	/**
	 * R-R间期门限最大值
	 */
	private float MaxRRInternal; // 主要的值
	/**
	 * R-R间期门限最小值
	 */
	private float MinRRInternal;
	/**
	 * 小于R-R间期门限最小值
	 */
	private static final int LOWERMINRR = 1; // lowerminrr
	/**
	 * 大于R-R间期门限最大值
	 */
	private static final int OVERMAXRR = 2; // overmaxrr
	/**
	 * 阀值递增值ChangePer = 0.05f;
	 */
	private float ChangePer; // 主要的值
	/**
	 * 斜率阀值
	 */
	private float SlopePer; // 主要的值
	/**
	 * 斜率阀值缓存
	 */
	private float tempSlopePer;
	/**
	 * 数值阀值
	 */
	private float DataPer; // 主要的值
	/**
	 * 数值阀值缓存
	 */
	private float tempDataPer;
	float Data[], MaxSlope, MaxData, MaxSlopeGate, MaxDataGate; // 主要的值
	/**
	 * 测试用打印信息：门限值
	 */
	private float logData_dateGate, logData_slopeGate;
	/**
	 * 测试用打印信息：平均值及偏移值,
	 */
	private List<Float> avgList, max_avg, min_avg;
	int LastRPoint, LastOperate;
	/**
	 * R波下标
	 */
	List<Integer> RPoint;
	private boolean finishAnalyse = false;
	private int startIndex = 0;

	public void setALLPOINT(int point_num) {
		ALLPOINT = point_num;
	}

	/**
	 * 构造函数，初始化数据
	 * 
	 * @param data
	 */
	public FindR(List<Float> data, int startIndex) {
		if (data.size() < 540) {
			POINTNUM = data.size(); // pointnum
		} else {
			POINTNUM = 540;
		}
		ALLPOINT = data.size();
		print("A=" + ALLPOINT);
		MaxRRInternal = 1.6f * 360;
		MinRRInternal = 0.3f * 360;
		ChangePer = 0.05f;
		SlopePer = 0.55f;
		tempSlopePer = SlopePer;
		DataPer = 0.7f;
		// SlopePer = 0.6f;
		// tempSlopePer=SlopePer;
		// DataPer = 0.58f;
		tempDataPer = DataPer;
		LastRPoint = 0;
		LastOperate = 0;
		avgList = new ArrayList<Float>();
		max_avg = new ArrayList<Float>();
		min_avg = new ArrayList<Float>();
		this.startIndex = startIndex;
		Data = new float[data.size()];
		for (int i = 0; i < data.size(); i++) {
			Data[i] = data.get(i);
		}
		RPoint = new ArrayList<Integer>();
	}

	public List<Integer> getRPoint() {
		return RPoint;
	}

	public void setRPoint(List<Integer> rPoint) {
		RPoint = rPoint;
	}

	public List<Float> getAvgList() {
		return avgList;
	}

	public List<Float> getMax_avg() {
		return max_avg;
	}

	public List<Float> getMin_avg() {
		return min_avg;
	}

	/**
	 * 获取R点的SparseArray
	 * 
	 * @return
	 */
	public List<SparseArray<Float>> StartToSearchQRS() {
		int NewR;
		// 第0步：根据R波是否倒置情况对数据进行调整
		// for(int m=POINTNUM;m<ALLPOINT+1;m=m+POINTNUM){
		// absDataIfNeed(m-POINTNUM);
		// }
		// if(ALLPOINT%POINTNUM!=0){
		// absDataIfNeed(ALLPOINT-(ALLPOINT%POINTNUM));
		// }
		// 第一步：找到斜率最大的
		// print("step0"+Data.length/POINTNUM);
		MaxSlopeGate = GetMaxSlopeGate(); // 获取最大斜率门限值

		// 第二步：找到最大的数，取阀值
		// print("step1_MaxSlopeGate__" + MaxSlopeGate);
		MaxDataGate = GetMaxDataGate(); // 获取最大值门限值

		// 第三步：找第一个R波
		// print("step2_MaxDataGate__" + MaxDataGate);
		NewR = FindRPoint(0);
		// print("step3");

		RPoint.add(LastRPoint, NewR);

		// print("第" + RPoint.get(RPoint.size() - 1) + "个数据点，峰值为"
		// + Data[RPoint.get(RPoint.size() - 1)]);
		//
		// print("step4");

		int isEnd = 0;
		while (RPoint.get(LastRPoint) + 100 < ALLPOINT && isEnd != END) {
			isEnd = FindRestRPoint(RPoint.get(LastRPoint) + 100);
			// Log.e("123", "你是多少啊" + (RPoint.get(LastRPoint) + 100));
		}
		// print("step5");
		List<SparseArray<Float>> list = new ArrayList<SparseArray<Float>>();
		// print("step6");
		for (int i = 0; i < RPoint.size(); i++) {
			print("step7");
			SparseArray<Float> m = new SparseArray<Float>();
			m.put(RPoint.get(i) + startIndex, Data[RPoint.get(i)]);
			list.add(m);
		}
		// print("step8");

		finishAnalyse = true;

		// print("step9");
		// findQSpoint();
		return list;
	}

	/**
	 * 当波形倒置时处理,对R波倒置取绝对值
	 * 
	 * @param startPosition
	 */
	public void absDataIfNeed(int startPosition) {
		float avg = 0;
		float max = Data[startPosition], min = Data[startPosition];
		for (int i = startPosition; (i < startPosition + POINTNUM && i < Data.length); i++) {
			avg += Data[i];
			if (max < Data[i]) { // 求两秒内最大值和最小值及其下标
				max = Data[i];
			}
			if (min > Data[i]) {
				min = Data[i];
			}
		}
		avg = avg / POINTNUM; // 求2秒内平均值
		max_avg.add(max - avg); // 保存信息打印
		min_avg.add(min - avg);
		avgList.add(avg);
		if (Math.abs(max - avg) < Math.abs(min - avg)) {
			// R波倒置
			// Log.e("倒置", startPosition+startIndex+"_________");
			for (int i = startPosition; (i < startPosition + POINTNUM && i < Data.length); i++) { // 绝对值化
				Data[i] = -Data[i];
			}
		}
	}

	/**
	 * 获取最大斜率门限值
	 * 
	 * @return 第一组数的最大斜率*SlopePer
	 */
	public float GetMaxSlopeGate() {
		int i;
		MaxSlope = Data[2] - Data[0]; // 初始化,3个点求一次斜率
		// if (ALLPOINT < POINTNUM) {
		// POINTNUM = ALLPOINT;
		// }
		for (i = 3; i < ALLPOINT; i++) {
			if (MaxSlope < Data[i] - Data[i - 2]) {
				MaxSlope = Data[i] - Data[i - 2];
			}
		}
		// Log.e("MaxSlope", "最大的斜率==（未乘上阀值）========="+MaxSlope);
		setLogData_slopeGate(MaxSlope * SlopePer);
		return MaxSlope * SlopePer;
	}

	/**
	 * 获取最大峰值门限值
	 * 
	 * @return 第一组数的最大值*DataPer
	 */
	public float GetMaxDataGate() {
		int i;
		MaxData = Data[0] * 100 + 500; // 初始化第一个数,*100+500
		for (i = 1; i < ALLPOINT; i++) {
			if (MaxData < Data[i] * 100 + 500) {
				MaxData = Data[i] * 100 + 500;
			}
		}
		setLogData_dateGate(MaxData * DataPer);
		return MaxData * DataPer;
	}

	/**
	 * 获取从s到ALLPOINT个数中斜率大于最大斜率门限的第一个数的下标
	 * 
	 * @param s
	 * @return
	 */
	public int FindOverMaxSlopeGate(int s) {
		if (s != END) {
			do {
				if (s >= ALLPOINT - 2)
					return END;
				s++;
			} while (Data[s + 1] - Data[s - 1] < MaxSlopeGate);
		}

		// print("Over1这是第几个数啊："+s+"数值是"+Data[s]);
		return s;
	}

	/**
	 * 以start为起始下标，获取其后20个数的最大值的下标
	 * 
	 * @param start
	 * @return 其后20个数的最大值的下标
	 */
	public int GetMaxData(int start) {
		int i, M;
		if (start == END) {
			return END;
		}
		// if (start >= ALLPOINT) {
		// start--;
		// }
		M = start;
		float MData = Data[start];
		for (i = 1; i < 20; i++) {
			if (start + i >= ALLPOINT)
				break;
			if (MData < Data[start + i]) {
				MData = Data[start + i];
				M = start + i;
			}
		}

		return M;
	}

	/**
	 * 先找到超出MaxSlope的值的下标，再找出该下标后20个数内的最大值的下标，假如这个值大于最大门限值那么就是R值，否则下标+1
	 * 继续找后20个数的最大值
	 * 
	 * @param start
	 * @return R波下标
	 */
	public int FindRPoint(int start) {
		int n = END; // = -1, 完成分析
		if (start != END) {
			if (start < ALLPOINT) {
				// print("find0");
				n = GetMaxData(FindOverMaxSlopeGate(start));
				if (n == END) {
					return END;
				}
				// print("find1____" + n);
				while (Data[n] * 100 + 500 < MaxDataGate && n + 1 < ALLPOINT) { // MaxDataGate
																				// =
																				// GetMaxDataGate();最大值门限值
					// print("find2______" + n);
					n = GetMaxData(++n); // 从下一个点开始循环查找
				}
				// print("find3这是第" + n + "个数：" + Data[n]);
			} else {
				n = start + 1;
			}
		}
		return n;
	}

	/**
	 * 1、由MakeSureRPoint方法演变而来，将递归调用修改为循环调用，防止深度递归引发的bug
	 * 2、为了防止FindRestRPoint一直执行下去，规定只能执行10次；
	 * 
	 * @param start
	 * @return R值的下标
	 */
	public int FindRestRPoint(int start) {
		int NewR = FindRPoint(start); // R波下标
		int i = 0;
		if (NewR != END) {
			while (i < 5 && ((NewR - RPoint.get(LastRPoint) < MinRRInternal // LastRPoint=0,LastOperate=0
					|| NewR - RPoint.get(LastRPoint) > MaxRRInternal))) {
				if (NewR - RPoint.get(LastRPoint) < MinRRInternal) {
					if (LastOperate == 0 || LastOperate == LOWERMINRR) { // LOWERMINRR=1,OVERMAXRR
																			// =
																			// 2;
						LastOperate = LOWERMINRR;
						SlopePer = SlopePer + ChangePer;
						MaxSlopeGate = MaxSlope * SlopePer;
						print("上次小等，这次小了啦");
						NewR = FindRPoint(start);
						if (NewR == END)
							return NewR;
					} else {
						LastOperate = LOWERMINRR;
						SlopePer = SlopePer + ChangePer / 10;
						MaxSlopeGate = MaxSlope * SlopePer;
						print("上次大，这次小了啦");
						NewR = FindRPoint(start);
						if (NewR == END)
							return NewR;
					}
				} else if (NewR - RPoint.get(LastRPoint) > MaxRRInternal) {
					if (LastOperate == 0 || LastOperate == OVERMAXRR) {
						LastOperate = OVERMAXRR;
						SlopePer = SlopePer - ChangePer;
						MaxSlopeGate = MaxSlope * SlopePer;
						print("上次大等，这次大了啦");
						NewR = FindRPoint(RPoint.get(LastRPoint) + 100);
						if (NewR == END)
							return NewR;
					} else {
						LastOperate = OVERMAXRR;
						SlopePer = SlopePer - ChangePer / 10;
						MaxSlopeGate = MaxSlope * SlopePer;
						print("上次小，这次大了啦");
						NewR = FindRPoint(RPoint.get(LastRPoint) + 100);
						if (NewR == END)
							return NewR;
					}
				}
				i++;
			}
		}

		ChangePer = 0.05f;
		LastOperate = 0;

		if (NewR != END) {
			++LastRPoint;
			RPoint.add(LastRPoint, NewR);
			// print("第" + RPoint.get(RPoint.size() - 1) + "个数据点，峰值为"
			// + Data[RPoint.get(RPoint.size() - 1)]);
		}
		DataPer = tempDataPer; // tempDataPer=DataPer
		SlopePer = tempSlopePer;
		MaxSlopeGate = MaxSlope * SlopePer;
		return NewR;

	}

	/**
	 * 
	 * 以下方法在其他类中调用
	 * 
	 */

	/**
	 * 获取R值下标的平均差
	 * 
	 * @return
	 */
	public float GetRRInternal() {
		if (finishAnalyse) { // finishAnalyse = true;
			int period = 0;
			for (int i = 0; i < RPoint.size() - 1; i++) {
				period = period + RPoint.get(i + 1) - RPoint.get(i);
			}
			period = period / RPoint.size() - 1;
			return period;
		} else
			return 0;
	}

	/**
	 * 得到本次数据的R波下标
	 * 
	 * @return
	 */
	public List<Integer> getAllRPoint() {
		for (int i = 0; i < RPoint.size(); i++) {
			RPoint.set(i, RPoint.get(i) + startIndex);
		}
		return RPoint;
	}

	/**
	 * 计算心率
	 * 
	 * @return
	 */
	public int getHeartRate() {
		int rate = 0;
		if (RPoint.size() > 1) {
			// Log.e("123",
			// "你有被调用吗(RPoint.get(RPoint.size()-1)==="+RPoint.get(RPoint.size()-1));
			// Log.e("123", "你有被调用吗RPoint.get(0)======"+RPoint.get(0));
			// Log.e("123", "你有被调用吗(RPoint.size()-1)==="+(RPoint.size()-1));
			rate = (60 * (RPoint.size() - 1) * 360)
					/ ((RPoint.get(RPoint.size() - 1) - RPoint.get(0)));
			// Log.e("123", "你有被调用吗rate==="+rate);
		}
		return rate;
	}

	/**
	 * @return RR间期的方差值
	 */
	public float getVariance() {
		float variance = 0;
		float average = 0;
		int[] RRPeriod = new int[RPoint.size() - 1];

		for (int i = 0; i < RPoint.size() - 1; i++) {
			RRPeriod[i] = RPoint.get(i + 1) - RPoint.get(i);
			average += RRPeriod[i];
			// Log.e("123", "RRPeriod[i]==="+i+"====="+RRPeriod[i]);
		}

		average = average / (RPoint.size() - 1);
		// Log.d("123", "average==="+average);
		for (int i = 0; i < RPoint.size() - 1; i++) {
			variance += Math.pow(RRPeriod[i] - average, 2);
		}
		variance = variance / (RPoint.size() - 1);
		return variance;
	}

	/**
	 * 从start下标开始先FindRPoint，得到一个R点下标后和前一个R点进行比较，如果超出正常的RR间期，则修正数据重新查找。
	 * LastOperate==0代表上次的判断在正常RR间期内，==1代表小于最小RR间期，==2代表大于最大RR间期
	 * 太深的递归调用会引起崩溃，已弃用
	 * 
	 * @param start
	 */
	public void MakeSureRPoint(int start) {
		int NewR = FindRPoint(start);
		if (NewR - RPoint.get(LastRPoint) < MinRRInternal) {
			if (LastOperate == 0 || LastOperate == LOWERMINRR) {
				LastOperate = LOWERMINRR;
				SlopePer = SlopePer + ChangePer;
				MaxSlopeGate = MaxSlope * SlopePer;
				print("上次大等，这次小了啦");
				MakeSureRPoint(NewR);
			} else {
				LastOperate = LOWERMINRR;
				SlopePer = SlopePer + ChangePer / 10;
				MaxSlopeGate = MaxSlope * SlopePer;
				print("上次小，这次小了啦");
				MakeSureRPoint(NewR);
			}

		} else if (NewR - RPoint.get(LastRPoint) > MaxRRInternal) {
			if (LastOperate == 0 || LastOperate == OVERMAXRR) {
				LastOperate = OVERMAXRR;
				SlopePer = SlopePer - ChangePer;
				MaxSlopeGate = MaxSlope * SlopePer;
				print("上次大等，这次大了啦");
				MakeSureRPoint(RPoint.get(LastRPoint) + 100);
			} else {
				LastOperate = OVERMAXRR;
				SlopePer = SlopePer - ChangePer / 10;
				MaxSlopeGate = MaxSlope * SlopePer;
				print("上次小，这次小了啦");
				MakeSureRPoint(RPoint.get(LastRPoint) + 100);
			}
		} else {
			ChangePer = 0.05f;
			LastOperate = 0;

			++LastRPoint;
			RPoint.add(LastRPoint, NewR);
			print("第" + RPoint.get(RPoint.size() - 1) + "个数据点，峰值为"
					+ Data[RPoint.get(RPoint.size() - 1)]);
		}
	}

	public void print(String text) {
		Log.e("123", text);
	}

	public float getLogData_dateGate() {
		return logData_dateGate;
	}

	public void setLogData_dateGate(float logData_dateGate) {
		this.logData_dateGate = logData_dateGate;
	}

	public float getLogData_slopeGate() {
		return logData_slopeGate;
	}

	public void setLogData_slopeGate(float logData_slopeGate) {
		this.logData_slopeGate = logData_slopeGate;
	}
}
