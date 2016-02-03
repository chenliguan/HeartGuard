/**
 * @file ECGSurfaceView.java
 * @description 心电图画图类
 * @author Guan
 * @date 2015-6-10 下午3:31:19 
 * @version 1.0
 */
package cn.heart.view;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import cn.heart.bean.ResultData;
import cn.heart.main.controller.MeasureActivity;
import cn.heart.service.algorithm.FindP;
import cn.heart.service.algorithm.FindR;
import cn.heart.service.algorithm.FindTQS;

/**
 * @description 心电图画图类
 * @author Guan
 * @date 2015-6-10 下午3:31:19
 * @version 1.0
 */
public class ECGSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	// 控制对象
	private SurfaceHolder holder = null;
	private Vector<Float> xs = new Vector<Float>();
	private Vector<Float> ys = new Vector<Float>();
	int refreshCount = 0;
	private ResultData resultData;
	private boolean isDestroyed;
	private int POINTNUM;
	@SuppressLint("SdCardPath")
	private static String fileLocation = "/sdcard/myHead/outData.txt"; // 手机内存路径

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * 保存滤波前R波峰值的表
	 */
	private List<SparseArray<Float>> prelist;
	/**
	 * 保存R波峰值的表
	 */
	private List<SparseArray<Float>> list;

	/**
	 * 每次分析心电数据的单元，目前以5秒为一个单位
	 */
	private static final int ALLPOINT = 1620;

	/**
	 * 刷新心率的频率，目前以4秒为一个单位,为了画图跟心率的检测能同步做的延时
	 */
	private static final int DELEYSHOWRATE = 3500;

	/**
	 * 控制画图的数率，具体怎么算出来的不知道，反正能1分钟的数据能在1分钟左右跑玩
	 */
	private static final int RATE = 100;
	/**
	 * 画笔对象
	 */
	private Paint linePaint = new Paint();
	/**
	 * 心电数据数组
	 */
	private ArrayList<Float> dataY = new ArrayList<Float>();
	/**
	 * 心电数据集合
	 */
	private ArrayList<Float> dataListY = new ArrayList<Float>();
	/**
	 * 滤波后并处理完倒置R波后心电数据集合
	 */
	private ArrayList<Float> newDataListY = new ArrayList<Float>();
	/**
	 * 滤波后心电数据集合
	 */
	private ArrayList<Float> DataListY;
	/**
	 * 标示是否是第一次画图
	 */
	private boolean isFirstDraw = true;
	/**
	 * 在画布上正在显示的数据集合
	 */
	private ArrayList<Float> showedList = new ArrayList<Float>();
	/**
	 * 已经显示的数据波形 最多能够显示的单位格数量(横坐标 15px 为一个单位格)
	 */
	private int maxNum = 0;

	/**
	 * 显示的波形每个单元格开始的位置(X轴坐标)
	 */
	private int showedBeginX = 0;
	/**
	 * 显示的波形每个单元格结束的位置(X轴坐标)
	 */
	private int showedEndX = 0;
	/**
	 * 显示的波形每个单元格开始的时候波形长度的值(Y轴坐标)
	 */
	private float showedBeginY = 0;
	/**
	 * 显示的波形每个单元格结束的时候波形长度的值(Y轴坐标)
	 */
	private float showedEndY = 0;
	public static int lineNum;

	private static final int dp = 72;
	private MyLoop myLoop;

	public ECGSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		printError("ECGSurfaceView-----");
		holder = getHolder();
		holder.addCallback(this);
		myLoop = new MyLoop(); // 画图线程
	}

	/**
	 * 控制是否刷新屏幕
	 */
	private boolean isrun = true;

	public boolean isrun() {
		return this.isrun;
	}

	public void setrun(boolean run) {
		this.isrun = run;
	}

	public ArrayList<Float> getDataY() {
		return DataListY;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Thread a = new Thread(myLoop);
		a.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		printError("surfaceChanged!!-----");
	}

	/**
	 * 以下在本类中调用
	 */

	/**
	 * MyLoop类
	 * 
	 * @author Aries
	 * 
	 * @create 2014-10-15 下午5:14:42
	 */
	class MyLoop implements Runnable {
		Canvas mCanvas = null;
		int i = 0;

		public void run() {
			while (isrun) {
				try {
					mCanvas = holder.lockCanvas();
					mCanvas.drawColor(Color.parseColor("#ffffff"));
					doDraw(mCanvas);
					Thread.sleep(200);

				} catch (Exception e) {
					System.out.println("MyLoop Thread exception!!!---------");
					break;
				} finally {
					if (mCanvas != null) {
						// System.out.println("MyLoop Thread finally unlockCanvasAndPost()!!!!");
						holder.unlockCanvasAndPost(mCanvas);
						i++;
					}
				}
			}
		}
	}

	/**
	 * 画图的方法 在MyLoop中调用
	 * 
	 * @param canvas
	 */
	@SuppressLint("WrongCall")
	private Canvas doDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = canvas.getWidth();
		int height = canvas.getHeight();
		// Log.d("123", "长"+width+"\n高"+height);
		// 判断是否是第一次在屏幕上画波形
		if (isFirstDraw) {
			maxNum = (width - (dp / 72)) / (dp / 72);
			// Log.d("123", "maxNum:" + maxNum);
		}
		int lStartX = 0;
		int lStartY = 0;
		// 画背景格子，画横线
		for (int i = 0; i < height / 15; i++) {
			linePaint.setStrokeWidth(1); // 画笔对象,linePaint = new Paint();
			linePaint.setColor(Color.GRAY);
			canvas.drawLine(0, lStartY, width, lStartY, linePaint);
			lStartY += 60;
		}

		// 画背景格子，画竖线
		for (int i = 0; i < width / 15; i++) {
			linePaint.setStrokeWidth(1);
			linePaint.setColor(Color.GRAY);
			canvas.drawLine(lStartX, 0, lStartX, height, linePaint);
			lStartX += 60;
		}

		for (int j = 0; j < RATE; j++) {
			// 判断心电数据集合中是否有数据
			if (dataListY.size() == 1 || dataListY.size() == 0) {
				dataListY.add((float) (height / 2));
			}

			// 如果心电数据集合中有数据
			if (dataListY.size() != 0 && dataListY != null) {
				// 如果能完全显示已经显示过的波形数据就就直接添加进显示过波形数据的集合中否则去掉第一个再添加进去
				if (showedList.size() < maxNum) {
					showedList.add(dataListY.get(0));
					dataListY.remove(0);
				} else {
					showedList.remove(0);
					showedList.add(dataListY.get(0));
					dataListY.remove(0);
					// 此时已经没有了心电数据
					if (dataListY.size() == 0) {
						// 则将心电数据设置为没有的状态值(显示为一条直线)
						dataListY.add((float) (height / 2));
					}
				}
			}
		}
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.parseColor("#6495ed"));
		// 设置画笔的线条粗细
		linePaint.setStrokeWidth(4f);

		if (showedList.size() != 0 && showedList != null) {
			showedBeginX = width - (dp / 72 * showedList.size() + dp / 72); // dp=72,显示的波形每个单元格开始的位置(X轴坐标)
			showedEndX = showedBeginX + (dp / 72); // 显示的波形每个单元格结束的位置(X轴坐标)
			showedBeginY = (float) (height / 2); // 显示的波形每个单元格开始的时候波形长度的值(Y轴坐标)
			for (int i = 0; i < showedList.size(); i++) {
				showedEndY = showedList.get(i);
				canvas.drawLine(showedBeginX, showedBeginY, showedEndX,
						showedEndY, linePaint);
				showedBeginX = showedEndX;
				showedEndX = showedBeginX + (dp / 72);
				showedBeginY = showedEndY;
				if (showedList.size() != 1 && showedList.size() != (i + 1)) {
					try {
						showedEndY = showedList.get(i + 1);
					} catch (Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}

		isFirstDraw = false;
		return canvas;
	}

	/**
	 * 以下在其他类,即：EcgtestActivity类中调用
	 */

	/**
	 * 开始从文件导入数据
	 */
	public void startLoadData() {
		new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				if (!isDestroyed) {
					InputStream inputStream = null;
					try {
						inputStream = MeasureActivity.sInputStream;
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(inputStream));
						lineNum = 0;
						// reader.readLine();
						// 保存滤波前R波峰值的表
						prelist = new ArrayList<SparseArray<Float>>();

						// FileReader fr = new FileReader(fileLocation);
						// @SuppressWarnings("resource")
						// BufferedReader br = new BufferedReader(fr);
						// while (br.readLine() != null) {
						// String s = br.readLine();
						// synchronized (dataY) {
						// // 保存滤波前R波峰值的表
						// dataY.add(Float.parseFloat(s));
						// }
						// // 整除,完整读入
						// if (dataY.size() % ALLPOINT == 0) {
						// // 对数据进行处理,让波形显示在屏幕中间,并不改变形状
						// StartDraw(dataY.size() - ALLPOINT, dataY.size());
						// new Thread(new Runnable() {
						// @Override
						// public void run() {
						// if (!isDestroyed) {
						// int rate;
						// FindR fqrs = new FindR(dataY
						// .subList(dataY.size()
						// - ALLPOINT,
						// dataY.size()),
						// dataY.size() - ALLPOINT);
						// // 获取到R波下标，并添加到“保存滤波前R波峰值的表”
						// prelist.addAll(fqrs
						// .StartToSearchQRS());
						// /**
						// * 创建消息 与MeasureActivity通信
						// */
						// Message m = new Message();
						// // 指定用户自定义的消息代码
						// m.what = MeasureActivity.Measure_LoadData;
						// // 心率=60*360/R值平均下标差
						// rate = fqrs.getHeartRate();
						// m.arg1 = rate;
						// // m.arg2 = lineNum - 2;
						// // 向MeasureActivity的handler发送消息
						// MeasureActivity.sHandler
						// .sendMessageDelayed(m,
						// DELEYSHOWRATE);
						// }
						// }
						// }).start();
						// Thread.sleep(DELEYSHOWRATE); // 为了画图跟心率的检测能同步做的延时
						// }
						// }
						// br.close();

						for (String s = reader.readLine(); s != null; s = reader
								.readLine()) {
							if (lineNum >= 3) {
								String a = s.substring(17, 18);
								s = s.substring(16, 23);
								if (a.equals("-"))
									resultData.getLogData_ifPositive().add(
											a + lineNum);
								synchronized (dataY) {
									dataY.add(Float.parseFloat(s)); // 保存滤波前R波峰值的表
								}
								if (dataY.size() % ALLPOINT == 0) { // 整除,完整读入
									// 对数据进行处理,让波形显示在屏幕中间,并不改变形状
									StartDraw(dataY.size() - ALLPOINT,
											dataY.size());
									new Thread(new Runnable() {
										@Override
										public void run() {
											if (!isDestroyed) {
												int rate;
												FindR fqrs = new FindR(dataY
														.subList(dataY.size()
																- ALLPOINT,
																dataY.size()),
														dataY.size() - ALLPOINT);
												prelist.addAll(fqrs
														.StartToSearchQRS()); // 获取到R波下标，并添加到“保存滤波前R波峰值的表”
												/**
												 * 创建消息 与MeasureActivity通信
												 */
												Message m = new Message();
												m.what = MeasureActivity.Measure_LoadData; // 指定用户自定义的消息代码
												// 心率=60*360/R值平均下标差
												rate = fqrs.getHeartRate();
												m.arg1 = rate;
												// m.arg2 = lineNum - 2;
												// 向MeasureActivity的handler发送消息
												MeasureActivity.sHandler
														.sendMessageDelayed(m,
																DELEYSHOWRATE);
											}
										}
									}).start();
									Thread.sleep(DELEYSHOWRATE); // 为了画图跟心率的检测能同步做的延时
								}
							}
							// }
							lineNum++;
						}
						// 这是漏读入数据的部分，因为dataY.size()%ALLPOINT 取余数，导致少读了一段数据
						// 对固定数据 含有的R波的检测率有一定的提高
						// 对读入的数据的R波的检测率还是一样的
						if (dataY.size() % ALLPOINT != 0) { // 不整除,漏读，并对漏读部分处理
							StartDraw(dataY.size() - (dataY.size() % ALLPOINT),
									dataY.size());

							new Thread(new Runnable() {
								@Override
								public void run() {
									if (!isDestroyed) {
										int rate;
										FindR fqrs = new FindR(
												dataY.subList(
														dataY.size()
																- (dataY.size() % ALLPOINT),
														dataY.size()),
												dataY.size()
														- (dataY.size() % ALLPOINT));
										prelist.addAll(fqrs.StartToSearchQRS());
										Message m = new Message();
										m.what = MeasureActivity.Measure_LoadData;
										// 心率=60*360/R值平均下标差
										rate = fqrs.getHeartRate();
										// Log.e("123", j + "心率是===" + rate);
										m.arg1 = rate;
										MeasureActivity.sHandler
												.sendMessageDelayed(m,
														DELEYSHOWRATE);
									}
								}
							}).start();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("错就是错，是我咎由自取" + e);
					}
				}
			}
		}).start();
	}

	/**
	 * 更新数据
	 */
	public void refreshData() {
		/**
		 * 滤波算法实现
		 */
		synchronized (dataY) { // 线程锁：一次只能有一个线程进入该方法
			for (int i = 45; i < dataY.size() - 45; i++) {
				List<Float> sortList = new ArrayList<Float>(dataY.subList(
						i - 45, i + 45));
				// subList返回一个以i-45为起始索引（包含），以i+45为终止索引（不包含）的子列表（List）。
				Collections.sort(sortList); // 排序
				float mid = sortList.get(sortList.size() / 2);
				newDataListY.add((dataY.get(i) - mid)); // 滤波后并处理完倒置R波后心电数据集合
			}
			DataListY = new ArrayList<Float>(newDataListY);
		}
		list = new ArrayList<SparseArray<Float>>();

		for (int i = ALLPOINT; i < newDataListY.size()
				&& refreshCount * ALLPOINT + ALLPOINT < newDataListY.size(); i = i
				+ ALLPOINT) {

			new Thread(new Runnable() {
				@Override
				public void run() {
					// if(!isDestroyed){
					POINTNUM = 540;
					List<Float> sublist = new ArrayList<Float>(
							newDataListY.subList(refreshCount * ALLPOINT,
									refreshCount * ALLPOINT + ALLPOINT));
					for (int m = POINTNUM; m < ALLPOINT + 1; m = m + POINTNUM) {
						absDataIfNeed(m - POINTNUM, POINTNUM, sublist,
								refreshCount * ALLPOINT);
					}
					FindR fqrs = new FindR(newDataListY.subList(refreshCount
							* ALLPOINT, refreshCount * ALLPOINT + ALLPOINT),
							refreshCount * ALLPOINT);
					list.addAll(fqrs.StartToSearchQRS());
					resultData.getLogData_dataGate().add(
							fqrs.getLogData_dateGate());
					resultData.getLogData_slopeGate().add(
							fqrs.getLogData_slopeGate());
					resultData.getAvgList().addAll(fqrs.getAvgList());
					resultData.getMin_avg().addAll(fqrs.getMin_avg());
					resultData.getMax_avg().addAll(fqrs.getMax_avg());
					resultData.getRpoint().addAll(fqrs.getAllRPoint());
					resultData.getVariance().add(fqrs.getVariance());
				}
			}).start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refreshCount++;
			if (newDataListY.size() % ALLPOINT == 0
					&& i + ALLPOINT >= newDataListY.size()) {
				MeasureActivity.sHandler
						.sendEmptyMessage(MeasureActivity.Measure_R);
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (newDataListY.size() % ALLPOINT != 0) { // 不整除,漏读，并对漏读部分处理

			new Thread(new Runnable() {
				@Override
				public void run() {
					// if(!isDestroyed){
					List<Float> sublist = new ArrayList<Float>(newDataListY
							.subList(newDataListY.size()
									- (newDataListY.size() % ALLPOINT),
									newDataListY.size()));
					POINTNUM = 540;
					for (int m = POINTNUM; m - POINTNUM < sublist.size(); m = m
							+ POINTNUM) {
						absDataIfNeed(m - POINTNUM, POINTNUM, sublist,
								newDataListY.size()
										- (newDataListY.size() % ALLPOINT));
					}
					if (sublist.size() % POINTNUM != 0) {
						absDataIfNeed(sublist.size()
								- (sublist.size() % POINTNUM), POINTNUM,
								sublist,
								newDataListY.size()
										- (newDataListY.size() % ALLPOINT));
					}
					// Log.e("进来", newDataListY.size()+"第1步");
					FindR fqrs = new FindR(newDataListY.subList(
							newDataListY.size()
									- (newDataListY.size() % ALLPOINT),
							newDataListY.size()), newDataListY.size()
							- (newDataListY.size() % ALLPOINT));
					// Log.e("进来", newDataListY.size()+"第2步");
					list.addAll(fqrs.StartToSearchQRS());
					resultData.getLogData_dataGate().add(
							fqrs.getLogData_dateGate());
					resultData.getLogData_slopeGate().add(
							fqrs.getLogData_slopeGate());
					resultData.getAvgList().addAll(fqrs.getAvgList());
					resultData.getMin_avg().addAll(fqrs.getMin_avg());
					resultData.getMax_avg().addAll(fqrs.getMax_avg());

					resultData.getRpoint().addAll(fqrs.getAllRPoint());
					resultData.getVariance().add(fqrs.getVariance());
					// 更新数据后,向EcgtestActivity的handler发送消息,进行下一项：病症诊断
					MeasureActivity.sHandler
							.sendEmptyMessage(MeasureActivity.Measure_R);
				}
			}).start();
		}

	}

	/**
	 * 病症的诊断 在检测结束后，计算出诊断所需要的各种参数，如：心率，P波宽度及高度、PR间期，RR间期，QRS时限等
	 */
	public void StartCheck() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// if(!isDestroyed){
				try {

					/**
					 * 分析R波
					 */
					resultData.setR(list.size());
					// normalrr为所有rr间期的平均值（一次心搏的采样点数）
					float normalrr = 0;
					if (list.size() > 1) {
						// list保存R波峰值的表
						// keyAt()查看第几个位置的键,即是：时间。值,即是：波值（纵坐标值）
						normalrr = (list.get(list.size() - 1).keyAt(0) - list
								.get(0).keyAt(0)) / (list.size() - 1);
					}
					/**
					 * normalrr保存多少个数据 数据数*单个数据占用毫秒数 每秒360个数据
					 * normalrr*(1000/360)
					 */
					resultData.setRRAverage(normalrr / 360 * 1000); // 存放RR间期的平均值
					resultData
							.setAverageHeart_rate((int) (60 * 360 / normalrr)); // RR平均心率
					for (int i = 0; i < list.size() - 2; i++) {
						// 整段数据的RR平均心率
						resultData.getAllAverageHeart_rate()
								.add((int) (60 * 360 / (list.get(i + 1)
										.keyAt(0) - list.get(i).keyAt(0))));
					}
					resultData.setRRAverage(normalrr / 360 * 1000);
					resultData
							.setAverageHeart_rate((int) (60 * 360 / normalrr));

					/**
					 * 分析TQS波
					 */
					for (int k = 0; k < list.size(); k++) {
						FindTQS findt;
						if (k + 2 < list.size()) {
							// 整段数据的RR间期的差值（心室率）
							resultData.getRRInternal().add(
									(list.get(k + 2).keyAt(0) - list.get(k + 1)
											.keyAt(0))
											- (list.get(k + 1).keyAt(0) - list
													.get(k).keyAt(0)));
						}
						if (k + 1 == list.size() && k > 0)
							findt = new FindTQS(normalrr,
									(list.get(k).keyAt(0) - list.get(k - 1)
											.keyAt(0)), newDataListY);
						else
							findt = new FindTQS(normalrr, (list.get(k + 1)
									.keyAt(0) - list.get(k).keyAt(0)),
									newDataListY);
						findt.startToSearchT(list.get(k).keyAt(0));
						findt.findQSpoint();
						resultData.getTempTstart().add(findt.getTempStart());
						resultData.getTempTend().add(findt.getTempEnd());
						resultData.getTstart().add(findt.gettFinalStart());
						resultData.getTend().add(findt.gettFinalEnd());
						resultData.getTIsUp().add(findt.isUp());
						resultData.getQRSAverage().add(findt.getQRSAverage());
						resultData.getQpoint().add(findt.getQpoint());
						resultData.getQstart().add(findt.getQStart());
						resultData.getSpoint().add(findt.getSpoint());
						resultData.getTpoint().add(findt.gettPoint());
					}

					ArrayList<Float> QRSAvg_Type2 = new ArrayList<Float>();
					ArrayList<Float> NewQRSAverage = null;
					ArrayList<Integer> NewR = null;
					// 目的在于计算出每个方差对应的QRS的平均值，以及R波下标
					// 每次分析心电数据的单元，目前以4.5秒为一个单位, ALLPOINT = 4.5*360 = 1620
					for (int p = ALLPOINT; p < dataY.size() + ALLPOINT; p = p
							+ ALLPOINT) {
						// System.out.println("sdfsdfsdf"+p);
						float sum = 0;
						int index = 0; // QRS间期的个数
						boolean isfirst = true;
						int Rnum = 0;
						float[] Rindex = new float[2];
						NewQRSAverage = new ArrayList<Float>();
						NewR = new ArrayList<Integer>();
						for (int k = 0; k < list.size(); k++) {
							if (list.get(k).keyAt(0) < p
									&& list.get(k).keyAt(0) >= p - ALLPOINT) {
								try {
									if (isfirst) {
										// 记录第一个R下标，用于记录4.5秒的心率
										Rindex[0] = list.get(k).keyAt(0);
										// Log.d("123", "第一个R下标Rindex[0]===" +
										// Rindex[0]);
										isfirst = false;
									}
									if (list.size() > k + 1
											&& list.get(k + 1).keyAt(0) > p) {
										// 记录最后一个R下标，用于记录4.5秒的心率
										Rindex[1] = list.get(k).keyAt(0);
										// Log.d("123", "最后一个R下标Rindex[1]===" +
										// Rindex[1]);
									}
									Rnum++;
									sum += resultData.getQRSAverage().get(k); // 存放QRS间期的平均值
									NewQRSAverage.add(resultData
											.getQRSAverage().get(k));
									NewR.add(resultData.getRpoint().get(k));
									// System.out.println("现在是第"+k+"次:"+resultData.getQRSAverage().get(k)+"____"+sum);
									index++;
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								if (index == 0)
									index++;
							}
						}

						if (Rindex[1] != 0) {
							if (Rnum == 0) {
								resultData.getItemAverageHeart_rate().add(0); // 每4.5秒数据的RR平均心率
							} else if (Rnum == 1) {
								// Log.e("123",
								// "\n(int) (60*360/(Rindex[1]-Rindex[0]))===000000000===="
								// + (int) (60*360/(Rindex[1]-Rindex[0])));
								resultData
										.getItemAverageHeart_rate()
										.add((int) (60 * 360 / (Rindex[1] - Rindex[0])));
							} else {
								// Log.e("123",
								// "\n(int) (60*360/((Rindex[1]-Rindex[0])/(Rnum-1)))===000000000===="
								// + (int)
								// (60*360/((Rindex[1]-Rindex[0])/(Rnum-1))));
								resultData
										.getItemAverageHeart_rate()
										.add((int) (60 * 360 / ((Rindex[1] - Rindex[0]) / (Rnum - 1))));
							}
						}
						resultData.getNewQRSAverage().add(NewQRSAverage);
						resultData.getNewRpointList().add(NewR);
						sum = sum / index;
						QRSAvg_Type2.add(sum);
					}
					for (int z = 0; z < resultData.getNewQRSAverage().size(); z++) {
						// Log.e("123", "\nNewQRSAverage===000000000====" +
						// resultData.getNewQRSAverage().get(z).toString());
					}
					resultData.getQRSAverage().clear();
					resultData.setQRSAverage(QRSAvg_Type2);

					/**
					 * 分析P波
					 */
					for (int k = 0; k < list.size(); k++) {
						// Log.e("123", "realrr是===" +
						// (list.get(k+1).keyAt(0)-list.get(k).keyAt(0)));
						// float
						// realRR=1000*(list.get(k+1).keyAt(0)-list.get(k).keyAt(0))/360;
						FindP findP;
						if (k + 1 == list.size() && k > 0)
							findP = new FindP(normalrr,
									(list.get(k).keyAt(0) - list.get(k - 1)
											.keyAt(0)), newDataListY);
						else
							findP = new FindP(normalrr, (list.get(k + 1).keyAt(
									0) - list.get(k).keyAt(0)), newDataListY);
						findP.startToSearchT(list.get(k).keyAt(0));
						resultData.getTempPstart().add(findP.getTempStart());
						resultData.getTempPend().add(findP.getTempEnd());
						resultData.getPstart().add(findP.getpFinalStart());
						resultData.getPend().add(findP.getpFinalEnd());
						resultData.getPpoint().add(findP.getpPoint());
						// resultData.getPIsUp().add(findP.isUp());
						int doublep = findP.getDoublePpoint();
						// if(doublep!=0)
						resultData.getDoubleP().add(doublep); // 双峰P波，或者说是P波增宽的下标值
						float sum = 0;
						// getTend(),即tEnd,T波终点；getPstart(),即pStart,p波起点
						for (int i = resultData.getTend().get(
								resultData.getTend().size() - 1); i < resultData
								.getPstart().get(
										resultData.getPstart().size() - 1); i++) {
							sum += newDataListY.get(i);
						}
						int size = resultData.getPstart().get(
								resultData.getPstart().size() - 1)
								- resultData.getTend().get(
										resultData.getTend().size() - 1);
						resultData.getBaseline().add(sum / size); // TP段，基线值
					}
					// 诊断完毕后,向EcgtestActivity的handler发送消息,进行下一项
					MeasureActivity.sHandler
							.sendEmptyMessage(MeasureActivity.Measure_T);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("错就是错，是我咎由自取" + e);
				}
			}
			// }
		}).start();
	}

	/**
	 * 让波形显示在屏幕中间,并不改变形状 每当加载ALLPOINT个数据，强制对dataListY添加数据，以完成画图，而不再Activity进行控制
	 * 。。
	 * 
	 * 在startLoadData()中被调用,导入数据时就对数据进行处理
	 */
	private void StartDraw(final int start, final int end) {
		// if(this.isrun)
		// 将心电数据数组转换成心电数据集合
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!isDestroyed) {
					// Log.e("111", "一共多少个数据啊：" + dataY.size());
					for (float i = start; i < end; i++) {
						dataListY.add(250 + dataY.get((int) i) * -80);
					}
				}
			}
		}).start();
	}

	/**
	 * 对倒置R波取绝对值
	 * 
	 * 在refreshData()中调用
	 * 
	 * @param startPosition
	 * @param POINTNUM
	 * @param mydataList
	 * @param listIndex
	 */
	public void absDataIfNeed(int startPosition, int POINTNUM,
			List<Float> mydataList, int listIndex) {
		float avg = 0;
		float max = mydataList.get(startPosition), min = mydataList
				.get(startPosition);
		for (int i = startPosition; (i < startPosition + POINTNUM && i < mydataList
				.size()); i++) {
			avg += mydataList.get(i);
			if (max < mydataList.get(i)) { // 求两秒内最大值和最小值及其下标
				max = mydataList.get(i);
			}
			if (min > mydataList.get(i)) {
				min = mydataList.get(i);
			}
		}
		avg = avg / POINTNUM; // 求2秒内平均值
		// max_avg.add(max-avg); //保存信息打印
		// min_avg.add(min-avg);
		// avgList.add(avg);
		if (Math.abs(max - avg) < Math.abs(min - avg)) {
			// R波倒置
			for (int i = startPosition; (i < startPosition + POINTNUM && i < mydataList
					.size()); i++) { // 绝对值化
				newDataListY.set(i + listIndex, -mydataList.get(i));
			}
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		setrun(false);
		printError("surfaceDestroyed!!-----");
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xs.add(event.getX());
			ys.add(event.getY());
		}
		return true;
	}

	public void printError(String msg) {
		Log.e("123", msg);
	}

	public ResultData getResultData() {
		return resultData;
	}

	public void setResultData(ResultData resultData) {
		this.resultData = resultData;
	}

}
