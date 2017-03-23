package deling.cellcom.com.cn.activity.me;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

/**
 * 授权详情
 * 
 * @author xpw
 * 
 */
public class AuthoDetailActivity extends FragmentActivityBase{
	private TextView tvKeyid;
	private TextView tvName;
	private TextView tvPhone;
	private TextView tvIdentity;
	private TextView tvExpiry;
	private TextView tvAuthoKey;
	private TextView tvState;
	private Button btSumbit;
	private Button btReject;
	private Header header;
	private String mState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autho_detial);
		MyApplication.getInstances().getActivities().add(this);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}

	private void InitView() {
		header = (Header) findViewById(R.id.header);
		tvKeyid = (TextView) findViewById(R.id.keyid);
		tvName = (TextView) findViewById(R.id.name);
		tvPhone = (TextView) findViewById(R.id.phone);
		tvIdentity = (TextView) findViewById(R.id.identity);
		tvExpiry = (TextView) findViewById(R.id.expiry);
		tvState = (TextView) findViewById(R.id.state);
		tvAuthoKey = (TextView) findViewById(R.id.authokey);
		tvAuthoKey.setMovementMethod(ScrollingMovementMethod.getInstance());
		btSumbit = (Button) findViewById(R.id.submitbtn);
		btReject = (Button) findViewById(R.id.rejectbtn);
	}

	private void InitListeners() {
		btSumbit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String keyid = tvKeyid.getText().toString();
				String phone = tvPhone.getText().toString();
				submit(keyid, getIntent().getExtras().getString("type"), phone, mState.equals("2")?"0":mState);
			}
		});
		btReject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String keyid = tvKeyid.getText().toString();
				String phone = tvPhone.getText().toString();
				submit(keyid, getIntent().getExtras().getString("type"), phone, "1");
			}
		});
	}
	
	private void InitData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			tvKeyid.setText(bundle.getString("id"));
			tvName.setText(bundle.getString("name"));
			tvPhone.setText(bundle.getString("phone"));
			String strDate = bundle.getString("expiry");
			String identity = "";
			switch (Integer.valueOf(bundle.getString("identity"))) {
			case 1:
				identity = "业主";
				break;
			case 2:
				identity = "家属";
				break;
			case 3:
				identity = "租户";
				break;
			case 4:
				identity = "父母";
				break;
			case 5:
				identity = "夫妻";
				break;
			case 6:
				identity = "儿女";
				break;
			case 7:
				identity = "朋友";
				break;
			case 8:
				identity = "父亲";
				break;
			case 9:
				identity = "母亲";
				break;
			case 10:
				identity = "丈夫";
				break;
			case 11:
				identity = "妻子";
				break;
			case 12:
				identity = "儿子";
				break;
			case 13:
				identity = "女儿";
				break;

			default:
				identity = "其他";
				break;
			}
			tvIdentity.setText(identity);
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			String expiry = sdf.format(new Date(System.currentTimeMillis())); 
			tvExpiry.setText(expiry);
			tvAuthoKey.setText(bundle.getString("authokey"));
			mState = bundle.getString("state");
			switch(Integer.valueOf(mState)){
				case 0:
					tvState.setText("已取消授权");
					tvState.setTextColor(getResources().getColor(R.color.red));
					btSumbit.setText("再次授权");
					break;
				case 1:
					tvState.setText("授权成功");
					tvState.setTextColor(getResources().getColor(R.color.green_light));
					btSumbit.setText("取消授权");
					break;
				case 2:
					tvState.setText("申请授权");
					tvState.setTextColor(getResources().getColor(R.color.red));
					btReject.setVisibility(View.VISIBLE);
					btSumbit.setText("同意授权");
					break;
				case 4:
					tvState.setText("临时授权");
					tvState.setTextColor(getResources().getColor(R.color.green_light));
					btSumbit.setText("取消授权");
					break;
			}
		}
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(
							header.getWindowToken(), 0);
					AuthoDetailActivity.this.finish();
				}
			});
		header.setTitle("家属授权详情", null);
	}

	
	//授权操作
	public void submit(final String id, final String type, final String phone, final String state) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("id", id);
		cellComAjaxParams.put("type", type);
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("state", state.equals("1")?"0":"1");
			
		HttpHelper.getInstances(AuthoDetailActivity.this).send(FlowConsts.AGREEGRANT,
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
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						DismissProgressDialog();
						try{
							Comm bean = arg0.read(Comm.class,
									CellComAjaxResult.ParseType.GSON);
							String state = bean.getReturncode();
							String msg = bean.getReturnmessage();
			
							if (FlowConsts.STATUE_1.equals(state)) {
								if (FlowConsts.STATUE_2.equals(state)) {
									ContextUtil.exitLogin(
											AuthoDetailActivity.this, header);
								}
								setResult(RESULT_OK);
								finish();
							}else{
								if (FlowConsts.STATUE_2.equals(state)) {
									// token失效
									ContextUtil.exitLogin(
											AuthoDetailActivity.this, header);
									return;
								}else
									ShowMsg(msg);
							}
						}catch(Exception e){
							e.printStackTrace();
							ShowMsg(getResources().getString(R.string.dataparsefail));
						}
					}
				});

	}
	
}
