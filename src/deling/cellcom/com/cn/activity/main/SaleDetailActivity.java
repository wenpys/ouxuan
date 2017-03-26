package deling.cellcom.com.cn.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.deling.R.drawable;
import cellcom.com.cn.deling.R.string;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.activity.zxing.activity.CaptureActivity;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;

/**
 * 互动营销内容
 * 
 * @author xpw
 * 
 */
public class SaleDetailActivity extends FragmentActivityBase {
	private TextView tvTile;
	private ImageView imBack;
	private ImageView imSetting;
	private ImageView imStateLight;
	private Button btShow;
	private Button btStart;
	private boolean isRuning = false;
	private boolean isDisplay = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saledetail);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void InitView() {
		tvTile = (TextView) findViewById(R.id.title);
		imBack = (ImageView) findViewById(R.id.leftimg);
		imSetting = (ImageView) findViewById(R.id.setting);
		imStateLight = (ImageView) findViewById(R.id.statelight);
		btShow = (Button) findViewById(R.id.show);
		btStart = (Button) findViewById(R.id.start);
	}

	private void InitListeners() {
		imBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		imSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		btShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(btShow.getText().equals("显示")){
					isDisplay = true;
					btShow.setText("隐藏");
				}else{
					btShow.setText("显示");
					isDisplay = false;
				}
			}
		});
		btStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(btStart.getText().equals("开始")){
					isRuning = true;
					btStart.setText("结束");
				}else{
					btStart.setText("开始");
					isRuning = false;
				}
			}
		});
	}

	private void InitData() {
		tvTile.setText(getResources().getString(string.main_center));
//		String title = getIntent().getStringExtra("title");
//		tvTile.setText(title);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(isRuning){
				ToastUtils.show(SaleDetailActivity.this, getResources().getString(R.string.activiey_runing));
				return true;
			}
			if(isDisplay){
				ToastUtils.show(SaleDetailActivity.this, getResources().getString(R.string.activiey_opening));
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
