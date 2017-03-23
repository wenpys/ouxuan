package deling.cellcom.com.cn.widget.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.net.FlowConsts;

/**
 * 
 * @author zsw 账号被其他手机登录后弹出提示用户退出或重新登录
 * 
 */
public class ActivityExitPopupWindow {
	private Context context;
	private PopupWindow popupWindow = null;
	// private Button tv_ok, tv_cancel;
//	private LinearLayout llDialogParent;
	private Button exitbtn;
	private Button reloginbtn;
	private boolean flag = false;
	private static ActivityExitPopupWindow window;
	OnClickCallBack callBack;

	public static ActivityExitPopupWindow createPopupWindow(Context context) {

		if (window == null) {
			window = new ActivityExitPopupWindow(context);
		}

		return window;

	}

	private ActivityExitPopupWindow(Context context) {
		this.context = context;
		initView();
	}

	public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
		this.callBack = onClickCallBack;
	}

	/**
	 * 初始化控件
	 * 
	 * @param exitbtn
	 */
	@SuppressWarnings("deprecation")
	private void initView() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.app_activity_exit_popup, null);

//		llDialogParent = (LinearLayout) view
//				.findViewById(R.id.ll_dialog_parent);

		exitbtn = (Button) view.findViewById(R.id.exitbtn);
		reloginbtn = (Button) view.findViewById(R.id.reloginbtn);

		exitbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 退出

				flag = true;
				dimissDialog();
				callBack.setOnClick("0");
			}
		});
		reloginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 重新登录
				flag = true;
				dimissDialog();

				callBack.setOnClick("1");

			}
		});

		if (popupWindow == null) {
			popupWindow = new PopupWindow(context);
		}

		// popupWindow.setAnimationStyle(R.style.PopupwindowAnimation);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setContentView(view);
		popupWindow.setFocusable(false);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// Auto-generated method stub
				if (!flag) {
					callBack.setOnClick(FlowConsts.SELECT);
				}

			}
		});
	}

	/**
	 * 显示popupwindow
	 * 
	 * @param parent
	 * @param x
	 * @param y
	 */
	public void showAsDropDown(View parent, int x, int y) {
		popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
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
		if (popupWindow.isShowing()) {
			return;
		}
		popupWindow.setHeight(height);
		popupWindow.setWidth(width);
		popupWindow.showAtLocation(parent, gravity, x, y);
	}

	public void dimissDialog() {
		if (null != popupWindow && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public interface OnClickCallBack {
		public void setOnClick(String type);

	}

	public boolean isShow() {
		if (popupWindow != null) {
			if (popupWindow.isShowing()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		

	}
}
