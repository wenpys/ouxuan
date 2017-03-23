package deling.cellcom.com.cn.activity.base;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.CustomProgressDialog;

public class FragmentBase extends Fragment {

	protected static final int SHOW_TIME = 1;
	private Activity activity;
	private CustomProgressDialog m_ProgressDialog;

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	public void ShowMsg(String pMsg) {
		ToastUtils.show(activity, pMsg);
	}

	public void ShowMsg(int p_ResID) {
		ToastUtils.show(activity, p_ResID);
	}

	protected void OpenActivity(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivity(_Intent);
	}

	protected void OpenActivityForResult(Class<?> pClass, int requestCode) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivityForResult(_Intent, requestCode);
	}

	protected void OpenActivityFinsh(Class<?> pClass) {
		Intent _Intent = new Intent();
		_Intent.setClass(activity, pClass);
		startActivity(_Intent);
		activity.finish();
	}

	protected LayoutInflater GetLayouInflater() {
		LayoutInflater _LayoutInflater = LayoutInflater.from(activity);
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
		return new AlertDialog.Builder(activity).setTitle(p_Title)
				.setMessage(p_Message)
				.setPositiveButton(R.string.app_btnyes, p_YesClickListener)
				.setNegativeButton(R.string.app_btnno, p_NoClickListener)
				.show();
	}

	protected AlertDialog ShowViewAlertDialog(String p_Title, View view,
			DialogInterface.OnClickListener p_YesClickListener,
			DialogInterface.OnClickListener p_NoClickListener) {
		return new AlertDialog.Builder(activity).setTitle(p_Title)
				.setView(view)
				.setPositiveButton(R.string.app_btnyes, p_YesClickListener)
				.setNegativeButton(R.string.app_btnno, p_NoClickListener)
				.show();
	}

	public CustomProgressDialog ShowProgressDialog(int p_MessageResID) {
		try {
			if (m_ProgressDialog == null) {
				m_ProgressDialog = CustomProgressDialog.createDialog(activity);
				m_ProgressDialog.setMessage(getString(p_MessageResID));
				m_ProgressDialog.setCanceledOnTouchOutside(false);
				m_ProgressDialog.show();
			}
			if (m_ProgressDialog != null && !m_ProgressDialog.isShowing()) {
				m_ProgressDialog.setMessage(getString(p_MessageResID));
				m_ProgressDialog.setCanceledOnTouchOutside(false);
				m_ProgressDialog.show();
				if (m_ProgressDialog.animationDrawable != null) {
					m_ProgressDialog.animationDrawable.start();
				}
			}
			return m_ProgressDialog;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m_ProgressDialog;
	}

	public void DismissProgressDialog() {
		if (m_ProgressDialog != null) {
			if (m_ProgressDialog.animationDrawable != null) {
				m_ProgressDialog.animationDrawable.stop();
			}
			m_ProgressDialog.dismiss();
		}
	}
}