package deling.cellcom.com.cn.activity.me;

import org.simpleframework.xml.Text;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * 修改信息
 * 
 * @author xpw
 * 
 */
public class UpdateActivity extends FragmentActivityBase {
	private LinearLayout llBack;
	private TextView tvTitle;
	private TextView tvSave;
	private EditText etInfo;
	private LinearLayout llSex;
	private TextView tvMale;
	private TextView tvFemale;
	private YzmComm loginComm;
	private String title = "";
	private	int type = 1;
	private String info = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_info);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void InitData() {
		if(getIntent().getExtras() != null){
			title = getIntent().getStringExtra("title");
			type =  getIntent().getIntExtra("type", 1);
			info = getIntent().getStringExtra("info");
		}
		tvTitle.setText(title);
		etInfo.setText(info);
		if(type == 1){
			llSex.setVisibility(View.GONE);
		}else{
			if(info.equals("女")){
				tvMale.setText("男");
				tvFemale.setText("女 √");
			}
			etInfo.setVisibility(View.GONE);
		}
	}

	private void InitView() {
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		tvTitle = (TextView) findViewById(R.id.title);
		tvSave = (TextView) findViewById(R.id.save);
		etInfo = (EditText) findViewById(R.id.info);
		llSex = (LinearLayout) findViewById(R.id.ll_sex);
		tvFemale = (TextView) findViewById(R.id.female);
		tvMale = (TextView) findViewById(R.id.male);
	}

	private void InitListeners() {
		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(
						tvTitle.getWindowToken(), 0);
				UpdateActivity.this.finish();
			}
		});
		tvSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if(type == 1)
					intent.putExtra("info", etInfo.getText().toString());
				else
					intent.putExtra("info", info);
				setResult(11111, intent);
				UpdateActivity.this.finish();
			}
		});
		tvFemale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvMale.setText("男");
				tvFemale.setText("女 √");
				info = "女";
			}
		});
		tvMale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tvMale.setText("男 √");
				tvFemale.setText("女");
				info = "男";
			}
		});
	}	

	private void submit(final String state) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("settype", state);
		HttpHelper.getInstances(UpdateActivity.this).send(FlowConsts.KEYSET,
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
						
					}else{
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
