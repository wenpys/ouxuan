package deling.cellcom.com.cn.utils;

import android.app.Activity;
import android.net.ConnectivityManager;
import cellcom.com.cn.deling.R;

public class CheckNetworkState {

	/**
	 * ��������Ƿ�����
	 * 
	 * @return
	 */
	public static void checkNetworkState(Activity activity) {
		boolean flag = false;
		// �õ�����������Ϣ
		@SuppressWarnings("static-access")
		// The static field Context.CONNECTIVITY_SERVICE should be accessed in a
		// static way
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(activity.CONNECTIVITY_SERVICE);
		// ȥ�����ж������Ƿ�����
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
	 * ��������Ƿ�����
	 * 
	 * @return
	 */
	public static boolean checkNetwork(Activity activity) {
		boolean flag = false;
		// �õ�����������Ϣ
		@SuppressWarnings("static-access")
		// The static field Context.CONNECTIVITY_SERVICE should be accessed in a
		// static way
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(activity.CONNECTIVITY_SERVICE);
		// ȥ�����ж������Ƿ�����
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;

	}
}
