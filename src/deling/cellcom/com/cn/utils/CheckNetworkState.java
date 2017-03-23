package deling.cellcom.com.cn.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import cellcom.com.cn.deling.R;

public class CheckNetworkState {

	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	public static void checkNetworkState(Activity activity) {
		boolean flag = false;
		// 得到网络连接信息
		@SuppressWarnings("static-access")
		// The static field Context.CONNECTIVITY_SERVICE should be accessed in a
		// static way
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(activity.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {

			ToastUtils.show(activity,
					activity.getResources().getString(R.string.lostnet));
		} else {
		}

	}

	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	public static boolean checkNetwork(Activity activity) {
		boolean flag = false;
		// 得到网络连接信息
		@SuppressWarnings("static-access")
		// The static field Context.CONNECTIVITY_SERVICE should be accessed in a
		// static way
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(activity.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;

	}
}
