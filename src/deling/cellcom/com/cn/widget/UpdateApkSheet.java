package deling.cellcom.com.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

/**
 * 
 * @author zsw	升级下载弹出
 *
 */
public class UpdateApkSheet {
	private Dialog dlg;
	private TextView ljgxtv;
	private TextView zwgxtv;
	private TextView zxbbtv;
	private TextView zxbbsjtv;
	private TextView zxbbmstv;
	private ProgressBar progress;
	private Context context;
	private OnActionSheetSelected actionSheetSelected;
	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	public UpdateApkSheet(Context context) {
		this.context=context;
	}
	
	public void setOnActionSheetSelected(OnActionSheetSelected actionSheetSelected){
		this.actionSheetSelected=actionSheetSelected;
	}
	public void setProgressVISIBLEORGONE(int VISIBLEORGONE){
		progress.setVisibility(VISIBLEORGONE);
	}
	
	public void setzxbbmstvVISIBLEORGONE(int VISIBLEORGONE){
		zxbbmstv.setVisibility(VISIBLEORGONE);
	}
	public void setData(String newVersion,String versioncontent){
		zxbbtv.setText("最新版本 "+newVersion);
		zxbbsjtv.setText("");
		zxbbmstv.setText(versioncontent.replace("|","\n"));
	}

	public void setProgress(int pro) {
		if(progress!=null){
			this.progress.setProgress(pro);
		}
	}
	
	public void setzwgxtvTxt(String zwgxtxt) {
		zwgxtv.setText(zwgxtxt);
	}
	
	public void setljgxtvTxt(String ljgxtvtxt) {
		ljgxtv.setText(ljgxtvtxt);
	}
	/**
	 * 
	 * @param context
	 * @param actionSheetSelected
	 * @param cancelListener
	 * @param type
	 *            1.上传照片 2.预览下载
	 * @return
	 */
	public Dialog showSheet(
			OnCancelListener cancelListener) {
		dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.updateapksheet, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		ljgxtv = (TextView) layout
				.findViewById(R.id.ljgxtv);
		zwgxtv = (TextView) layout
				.findViewById(R.id.zwgxtv);
		zxbbtv = (TextView) layout
				.findViewById(R.id.zxbbtv);
		zxbbsjtv = (TextView) layout
				.findViewById(R.id.zxbbsjtv);
		zxbbmstv = (TextView) layout
				.findViewById(R.id.zxbbmstv);
		progress=(ProgressBar)layout.findViewById(R.id.progress);
		ljgxtv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 立即更新
				actionSheetSelected.onClick(1);
			}
		});

		zwgxtv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂未更新
				actionSheetSelected.onClick(2);
			}
		});


		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}
	
	public void dismiss(){
		if(dlg!=null){			
			dlg.dismiss();
		}
	}

}
