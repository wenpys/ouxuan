package deling.cellcom.com.cn.activity.base;

import java.lang.reflect.Field;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import cellcom.com.cn.deling.R;
import cn.jpush.android.api.JPushInterface;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.CustomProgressDialog;
import deling.cellcom.com.cn.widget.LoadProgressDialog;

public abstract class FragmentActivityBase extends FragmentActivity {
	private boolean isRun;
	private boolean isBackground = false;
	protected static final int SHOW_TIME = 1;
	private CustomProgressDialog m_ProgressDialog;
	private LoadProgressDialog loadProgressDialog;

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	protected void OpenActivity(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
	}

	protected void OpenActivityForResult(Class<?> pClass, int requestCode) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivityForResult(_Intent, requestCode);
	}

	protected void OpenActivityFinsh(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(this, pClass);
		startActivity(_Intent);
		this.finish();
	}

	protected LayoutInflater GetLayouInflater() {
		LayoutInflater _LayoutInflater = LayoutInflater.from(this);
		return _LayoutInflater;
	}

	public void SetAlertDialogIsClose(DialogInterface pDialog, Boolean pIsClose) {
		try {
			Field _Field = pDialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			_Field.setAccessible(true);
			_Field.set(pDialog, pIsClose);
		} catch (Exception e) {
		}
	}

	public interface MyDialogInterface {
		public void onClick(DialogInterface dialog);
	}

	public CustomProgressDialog ShowProgressDialog(int p_MessageResID) {
		try {
			if (m_ProgressDialog == null) {
				m_ProgressDialog = CustomProgressDialog.createDialog(this);
				m_ProgressDialog.setMessage(getString(p_MessageResID));
				m_ProgressDialog.setCanceledOnTouchOutside(false);
				m_ProgressDialog.show();
			}
			if (m_ProgressDialog != null && !m_ProgressDialog.isShowing()) {
				m_ProgressDialog.setMessage(getString(p_MessageResID));
				m_ProgressDialog.setCanceledOnTouchOutside(false);
				m_ProgressDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m_ProgressDialog;
	}

	public LoadProgressDialog ShowLoadingProgressDialog(int p_MessageResID) {
		loadProgressDialog = LoadProgressDialog.createDialog(this);
		loadProgressDialog.setMessage(getString(p_MessageResID));
		loadProgressDialog.setCanceledOnTouchOutside(false);
		loadProgressDialog.show();
		return loadProgressDialog;
	}

	public void DismissLoadingProgressDialog() {
		if (loadProgressDialog != null) {
			loadProgressDialog.dismiss();
		}
	}

	public void DismissProgressDialog() {
		if (m_ProgressDialog != null) {
			m_ProgressDialog.dismiss();
		}
	}

	protected AlertDialog ShowAlertDialog(int p_TitelResID, String p_Message,
			DialogInterface.OnClickListener p_YesClickListener,
			DialogInterface.OnClickListener p_NoClickListener) {
		String _Title = getResources().getString(p_TitelResID);
		return ShowAlertDialog(_Title, p_Message, p_YesClickListener,
				p_NoClickListener);
	}

	protected AlertDialog ShowAlertDialog(String p_Title, String p_Message,
			DialogInterface.OnClickListener p_YesClickListener,
			DialogInterface.OnClickListener p_NoClickListener) {
		return new AlertDialog.Builder(this).setTitle(p_Title)
				.setMessage(p_Message)
				.setPositiveButton(R.string.app_btnyes, p_YesClickListener)
				.setNegativeButton(R.string.app_btnno, p_NoClickListener)
				.show();
	}

	protected AlertDialog ShowViewAlertDialog(String p_Title, View view,
			DialogInterface.OnClickListener p_YesClickListener,
			DialogInterface.OnClickListener p_NoClickListener) {
		return new AlertDialog.Builder(this).setTitle(p_Title).setView(view)
				.setPositiveButton(R.string.app_btnyes, p_YesClickListener)
				.setNegativeButton(R.string.app_btnno, p_NoClickListener)
				.show();
	}

	@Override
	protected void onDestroy() {
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
		super.onDestroy();
		onExit();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
		super.onCreate(arg0);
		onRun();
	}

	protected void onExit() {
		isRun = false;
	}

	protected void onRun() {
		isRun = true;
	}

	public void ShowMsg(String pMsg) {

		ToastUtils.show(this, pMsg);
	}

	public void ShowMsg(int p_ResID) {
		ToastUtils.show(this, p_ResID);
	}

	@Override
	protected void onStop() {
		super.onStop();
		isBackground = isApplicationBroughtToBackground(FragmentActivityBase.this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	/**
	 * �жϵ�ǰӦ�ó�����ǰ̨���Ǻ�̨
	 */
	public static boolean isApplicationBroughtToBackground(final Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		// The method getRunningTasks(int) from the type ActivityManager is
		// deprecated
		List<RunningTaskInfo> tasks = am.getRunningTasks(SHOW_TIME);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

}