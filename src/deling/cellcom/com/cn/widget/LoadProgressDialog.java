package deling.cellcom.com.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
/**
 * 
 * @author zsw	����ʱ������	LoadProgressDialog
 *
 */
public class LoadProgressDialog extends Dialog {

	private static LoadProgressDialog customProgressDialog = null;

	public LoadProgressDialog(Context context) {
		super(context);
	}

	public LoadProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LoadProgressDialog createDialog(Context context) {
		customProgressDialog = new LoadProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.common_loadprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
//		ImageView imageView = (ImageView) customProgressDialog
//				.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
//				.getBackground();
//		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile ����
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LoadProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage ��ʾ����
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LoadProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}
}
