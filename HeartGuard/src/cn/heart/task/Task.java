/**
 * @file Task.java
 * @description 后台任务框架任务Task封装类
 * @author Michael
 * @date 2015-6-19 下午3:38:32 
 * @version 1.0
 */
package cn.heart.task;

import android.app.Activity;

/**
 * @description 后台任务框架任务Task封装类
 * @author Michael
 * @date 2015-6-19 下午3:38:32
 * @version 1.0
 */
public class Task {
	/** 通过长条码来查找患者信息 */
	public static final int DB_SEARCH_PATIENT_BY_BAR_CODE = 0;
	/** 通过患者姓名来查找患者信息 */
	public static final int DB_SEARCH_PATIENT_BY_NAME = 1;
	/** 通过任务ID来查找患者信息 */
	public static final int DB_SEARCH_PATIENT_BY_TASK_ID = 2;
	/** 将患者信息保存到Application中 */
	public static final int APP_SAVE = 3;
	/** 将患者插入数据库 */
	public static final int DB_INSERT_PATIENT = 4;
	/** 访问WebService，参数1：WebService名，参数2：WebService参数，参数3：超时时间 */
	public static final int NET_CALL_WEB_SERVICE = 5;
	/** 访问WebService */
	public static final int TASK_TIME_OUT = 6;
	/** 删除所有数据 */
	public static final int DB_DELETE_ALL = 7;
	/** 通过蓝牙读取心电数据 */
	public static final int BT_READ_ECG = 8;
	/** 通过蓝牙读取心电数据 */
	public static final int BT_START_ECG = 9;
	/** 停止读取心电数据 */
	public static final int BT_STOP_ECG = 10;
	/** 通过蓝牙读取电池数据 */
	public static final int BT_READ_BAT = 11;
	/** 通过蓝牙读取体温数据 */
	public static final int BT_READ_TEMP = 12;
	/** 停止体温 */
	public static final int BT_STOP_TEMP = 17;
	/** 开启血压 */
	public static final int BT_START_BP = 13;
	/** 停止血压 */
	public static final int BT_STOP_BP = 14;
	/** 开始读取血压数据 */
	public static final int BT_READ_BP = 15;
	/** 通过蓝牙读取长条码数据 */
	public static final int BT_READ_BAR_CODE = 16;
	/** 执行SQL语句，返回Cursor对象 */
	public static final int DB_EXEC_SQL = 18;

	/**
	 * @description 每个后台任务执行完后，通过该接口通知Activity任务已经完成
	 */
	public interface Callback {
		/**
		 * @description 当任务完成时被Activity回调，因此Activity需要执行后台任务时，都需要实现该接口
		 * @param taskID
		 *            任务ID
		 * @param mResult
		 *            任务执行结果
		 */
		public void onTaskFinished(Task task);
	}

	private int mId;
	public Object[] mParams;
	public Object mResult;
	public boolean isTimeOut = false;
	private Activity mActivity;

	/**
	 * @description 任务构造器
	 * @param taskId
	 *            任务ID
	 * @param params
	 *            任务执行用到的参数
	 */
	public Task(Activity context, int taskId, Object[] params) {
		System.out.println("开始任务===========");
		this.mId = taskId;
		this.mParams = params;
		mActivity = context;
	}

	/**
	 * @description 返回执行当前任务的Activity
	 * @return
	 */
	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * @description 返回任务ID
	 * @return 任务ID
	 */
	public int getTaskId() {
		return this.mId;
	}
}
