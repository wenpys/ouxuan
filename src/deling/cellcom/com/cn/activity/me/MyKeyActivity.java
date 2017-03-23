package deling.cellcom.com.cn.activity.me;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.YzmComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;

/**
 * 我的钥匙
 * 
 * @author xpw
 * 
 */
public class MyKeyActivity extends FragmentActivityBase {
	private LinearLayout llDisableText;
	private LinearLayout llNormalText;
	private ImageView ivKeyState;
	private Button btOper;
	private YzmComm loginComm;
	private Header header;
	private boolean mKeyState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mykey);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void InitData() {
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								header.getWindowToken(), 0);
						MyKeyActivity.this.finish();
					}
				});
		header.setTitle("我的钥匙", null);

		mKeyState = MyApplication.getInstances().isKeyState();
		setKeyState(mKeyState);
	}
	
	private void setKeyState(boolean state){
		if(state){
			ivKeyState.setImageResource(R.drawable.myke_st2);
			llDisableText.setVisibility(View.GONE);
			llNormalText.setVisibility(View.VISIBLE);
			btOper.setText("我要放弃钥匙");
		}else{
			ivKeyState.setImageResource(R.drawable.myke_st);
			llDisableText.setVisibility(View.VISIBLE);
			llNormalText.setVisibility(View.GONE);
			btOper.setText("我要使用钥匙");
		}
	}

	private void InitView() {
		llDisableText = (LinearLayout) findViewById(R.id.disable_text);
		llNormalText = (LinearLayout) findViewById(R.id.normal_text);
		btOper = (Button) findViewById(R.id.operbtn);
		ivKeyState = (ImageView) findViewById(R.id.myke_st);
		header = (Header) findViewById(R.id.header);
	}

	private void InitListeners() {
		btOper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submit(mKeyState ? "0" : "1");
			}
		});
	}	

	private void submit(final String state) {
//		mKeyState = state.equals("0") ? false : true;
//		MyApplication.getInstances().setKeyState(mKeyState);
//		setKeyState(mKeyState);
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("settype", state);
		HttpHelper.getInstances(MyKeyActivity.this).send(FlowConsts.KEYSET,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
				ShowProgressDialog(R.string.app_loading);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				DismissProgressDialog();
				ShowMsg(strMsg);
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				DismissProgressDialog();
				try{
					Comm bean = arg0.read(Comm.class,
						CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						mKeyState = state.equals("0")?false:true;
						MyApplication.getInstances().setKeyState(mKeyState);
						setKeyState(mKeyState);
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									MyKeyActivity.this, header);
							return;
						}else
							ShowMsg(msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg("数据解析失败！");
				}
			}
		});

	}

}
