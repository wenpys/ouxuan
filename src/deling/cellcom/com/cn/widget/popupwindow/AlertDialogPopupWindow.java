package deling.cellcom.com.cn.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.utils.ContextUtil;

/**
 * 
 * @author zsw 弹出窗口
 * 
 */
public class AlertDialogPopupWindow {
	private Context context;
	private PopupWindow popupWindow = null;
	private String content;
	private Button buttonOk, buttonCancel;
	private TextView textViewContent;
	private LinearLayout llDialogParent, llContent;
	OnClickCallBack callBack;

	public AlertDialogPopupWindow(Context context, String contentstr) {
		this.context = context;
		this.content = contentstr;
		initView();
	}

	public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
		this.callBack = onClickCallBack;
	}

	/**
	 * 初始化控件
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.app_alertdialog_popup, null);
		llDialogParent = (LinearLayout) view
				.findViewById(R.id.ll_dialog_parent);
		llDialogParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dimissDialog();
			}
		});
		llContent = (LinearLayout) view.findViewById(R.id.ll_dialog);

		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				ContextUtil.getWidth(context) * 6 / 8,
				ContextUtil.getHeith(context) * 7 / 20);
		llContent.setLayoutParams(ll);

		llContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		// 内容
		textViewContent = (TextView) view.findViewById(R.id.tv_content);
		textViewContent.setText(content);

		// 确认
		buttonOk = (Button) view.findViewById(R.id.btn_ok);
		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.setOnClick();
				}
			}
		});

		// 取消
		buttonCancel = (Button) view.findViewById(R.id.btn_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dimissDialog();
			}
		});

		if (popupWindow == null) {
			popupWindow = new PopupWindow(context);
		}
		popupWindow.setAnimationStyle(R.style.PopupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setContentView(view);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
	}

	/**
	 * 显示popupwindow
	 * 
	 * @param parent
	 * @param x
	 * @param y
	 */
	public void showAsDropDown(View parent, int x, int y) {
		popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
		popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
		popupWindow.showAsDropDown(parent, x, y);
	}

	/**
	 * 显示popupwindow
	 * 
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public void showAtLocation(View parent, int gravity, int x, int y,
			int width, int height) {
		popupWindow.setHeight(height);
		popupWindow.setWidth(width);
		popupWindow.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 设置提示内容
	 */
	public void setText(String text) {
		textViewContent.setText(text);
	}

	/**
	 * 隐藏确定按钮
	 */
	public void hideOkBtn() {
		buttonOk.setVisibility(View.GONE);
	}

	/**
	 * 显示确定按钮
	 */
	public void showOkBtn() {
		buttonOk.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏取消按钮
	 */
	public void hideCancelBtn() {
		buttonCancel.setVisibility(View.GONE);
	}

	/**
	 * 显示取消按钮
	 */
	public void showCancelBtn() {
		buttonCancel.setVisibility(View.VISIBLE);
	}

	public void dimissDialog() {
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public interface OnClickCallBack {
		void setOnClick();
	}
}
