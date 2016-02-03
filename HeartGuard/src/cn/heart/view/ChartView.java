/**
 * @file ChartView.java
 * @description 最近十天折线图类
 * @author Guan
 * @date 2015-6-10 下午3:26:40 
 * @version 1.0
 */
package cn.heart.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @description 最近十天折线图类
 * @author Guan
 * @date 2015-6-10 下午3:26:40
 * @version 1.0
 */
public class ChartView extends View {

	/**
	 * 原点的X坐标
	 */
	public int XPoint;
	/**
	 * 原点的Y坐标
	 */
	public int YPoint;
	/**
	 * X的刻度长度
	 */
	public int XScale;
	/**
	 * Y的刻度长度
	 */
	public int YScale;
	/**
	 * X轴的长度
	 */
	public int XLength;
	/**
	 * Y轴的长度
	 */
	public int YLength;
	/**
	 * X的刻度
	 */
	public String[] XLabel;
	/**
	 * Y的刻度
	 */
	public String[] YLabel;
	/**
	 * 数据
	 */
	public String[] Data;
	/**
	 * 显示的标题
	 */
	public String Title;
	/**
	 * 画图区域的宽
	 */
	public int width;
	/**
	 * 画图区域的高
	 */
	public int height;

	public ChartView(Context context) {
		super(context);
	}

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData,
			String strTitle) {
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	/**
	 * @description onDraw方法
	 */
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		// 重写onDraw方法
		super.onDraw(canvas);
		// canvas.drawColor(Color.WHITE);//设置背景颜色
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		// 去锯齿
		paint.setAntiAlias(true);
		// 颜色
		paint.setColor(Color.parseColor("#6495ed"));
		paint.setStrokeWidth(1.5f);
		// 设置轴文字大小
		paint.setTextSize(16);

		Paint paints = new Paint();
		paints.setStyle(Paint.Style.STROKE);
		// 去锯齿
		paints.setAntiAlias(true);
		// 颜色
		paints.setColor(Color.parseColor("#6495ed"));
		paints.setStrokeWidth(1.5f);
		// 设置轴文字大小
		paints.setTextSize(16);

		/** 设置起点、终点等的值 注意：更换版本后应该是以整个屏幕为高度（屏幕左上角为坐标原点） */
		width = canvas.getWidth();
		height = canvas.getHeight();
		XPoint = 40;
		YPoint = height - 925;
		XScale = (width - 50) / 10;
		YScale = (height - 925) / 5;
		YLength = height - 945;
		XLength = width - 95;

		/** 设置Y轴 */
		// 轴线
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);
		for (int i = 0; i * YScale < YLength; i++) {
			// 刻度
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint);
			try {
				// 文字
				canvas.drawText(YLabel[i], XPoint - 32,
						YPoint - i * YScale + 5, paint);
			} catch (Exception e) {
			}
		}
		// 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint);
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);

		/** 设置X轴 */
		// 轴线
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
		for (int i = 0; i * XScale < XLength; i++) {
			// 刻度
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
					YPoint - 5, paint);
			try {
				// 文字
				canvas.drawText(XLabel[i], XPoint + i * XScale - 5,
						YPoint + 20, paint);
				// 数据值，保证有效数据
				if (i > 0 && YCoord(Data[i - 1]) != -999
						&& YCoord(Data[i]) != -999)
					canvas.drawLine(XPoint + (i - 1) * XScale,
							YCoord(Data[i - 1]), XPoint + i * XScale,
							YCoord(Data[i]), paint);
				// 画圆点
				canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 3,
						paints);
				// 画圆点对应的数值
				canvas.drawText(Data[i], XPoint + i * XScale - 10,
						YCoord(Data[i]) - 15, paints);
			} catch (Exception e) {
			}
		}
		// 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint);
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);

		// paint.setTextSize(30);
		// canvas.drawText(Title, 250, 50, paint);
	}

	/**
	 * @description 计算绘制时的Y坐标，无数据时返回-999@description
	 * @param y0
	 * @return
	 */
	private int YCoord(String y0) {
		int y;
		try {
			y = Integer.parseInt(y0);
		} catch (Exception e) {
			// 出错则返回-999
			return -999;
		}
		try {
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e) {
		}
		return y;
	}
}