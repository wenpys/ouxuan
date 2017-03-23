package deling.cellcom.com.cn.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.KeyDetailsInfo;
import deling.cellcom.com.cn.bean.KeyDetailsInfoComm;
import deling.cellcom.com.cn.bean.Keyinfo;
import deling.cellcom.com.cn.bean.SubNoticeComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;

public class KeyLogDetailsPassActivity extends FragmentActivityBase {
	private final int requestcode=1212;
	private String id;
	private String type;
	private Header header;
	//待审核，审核通过界面
	private TextView tvArea;
	private TextView tvDoor;
	private TextView tvRoom;
	private TextView tvStatus;
	private TextView tvState;
	//审核未通过界面
	private TextView tvName;
	private TextView tvPhone;
	private TextView tvReason;
	private TextView tvSubmit;
//	private TextView tvAreaNo;
//	private TextView tvDoorNo;
//	private TextView tvRoomNo;
//	private TextView tvStatusNo;
//	private TextView tvStateNo;
	private KeyDetailsInfo keyinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id=getIntent().getStringExtra("id");
		type=getIntent().getStringExtra("type");
		if(isOwner()){
			setContentView(R.layout.activity_key_log_details_nopass);
		}else{
			setContentView(R.layout.activity_key_log_details_pass);
		}
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
		initListener();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
	private void initListener() {
		if(isOwner()){
			tvSubmit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//再次提交
					pushDataToService();
				}
			});
		}
	}
	private void initData() {
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setTitle(getResources().getString(R.string.title_activity_key_log_details_pass));
		header.setLeftImageVewRes(R.drawable.main_nav_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data=new Intent();
				data.putExtra("data", "1");
				setResult(requestcode, data);
				finish();
				KeyLogDetailsPassActivity.this.finish();
			}
		});
		getNetData();
	}
	private void initView() {
		header=(Header) findViewById(R.id.header);
		tvArea=(TextView) findViewById(R.id.tv_area);
		tvDoor=(TextView) findViewById(R.id.tv_door);
		tvRoom=(TextView) findViewById(R.id.tv_room);
		tvStatus=(TextView) findViewById(R.id.tv_status);
		tvState=(TextView) findViewById(R.id.tv_state);
		tvName=(TextView) findViewById(R.id.tv_name);
		tvPhone=(TextView) findViewById(R.id.tv_phone);
		tvReason=(TextView) findViewById(R.id.tv_reason);
		tvSubmit=(TextView) findViewById(R.id.tv_submit);
	}
	//获取某个id的数据
	public void getNetData(){
		CellComAjaxParams params=new CellComAjaxParams();
		params.put("id", id);
		HttpHelper.getInstances(this).send(FlowConsts.CHECKAPPLYINFO, 
				params, 
				CellComAjaxHttp.HttpWayMode.POST, 
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						super.onStart();
					}
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
					}
					@Override
					public void onSuccess(CellComAjaxResult t) {
						KeyDetailsInfoComm comm=null;
						try {
							comm=t.read(KeyDetailsInfoComm.class, CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							Log.e("MYTAG", "数据异常");
							return;
						}
						String status=comm.getReturncode();
						String msg=comm.getReturnmessage();
						if(!status.equals(FlowConsts.STATUE_1)){
							ToastUtils.show(KeyLogDetailsPassActivity.this, msg);
							return;
						}
						//把数据显示出来
						keyinfo=comm.getBody();
						dealData(keyinfo);
					}
				});
	}
	public void dealData(KeyDetailsInfo info){
		tvArea.setText(info.getArea());
		tvDoor.setText(info.getGate());
		tvRoom.setText(info.getRoom());
		if(info.getType().equals("3")){
			tvStatus.setText(getResources().getString(R.string.activity_askkey_status_tenant));
		}else if(info.getType().equals("2")){
			tvStatus.setText(getResources().getString(R.string.activity_askkey_status_family));
		}else{
			tvStatus.setText(getResources().getString(R.string.activity_askkey_status_owner));
		}
		if(info.getState().equals("0")){
			tvState.setText(getResources().getString(R.string.activity_askkey_load));
			tvState.setTextColor(getResources().getColor(R.color.yellow_dark));
		}else if(info.getState().equals("1")){
			tvState.setText(getResources().getString(R.string.activity_askkey_pass));
			tvState.setTextColor(getResources().getColor(R.color.green));
		}else{
			tvState.setText(getResources().getString(R.string.activity_askkey_nopass));
		}
		if(type.equals("3")){
			tvName.setText(info.getName());
			tvPhone.setText(info.getPhone());
			tvReason.setText(info.getContent());
		}
	}
	private boolean isOwner(){
		if(type.equals("3")){
			return true;
		}
		return false;
	}
	/**
	 * 提交钥匙申请
	 */
	protected void pushDataToService() {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("doorid", keyinfo.getRoomid());
		params.put("type", keyinfo.getType());
		params.put("name", keyinfo.getName());
		HttpHelper.getInstances(KeyLogDetailsPassActivity.this).send(FlowConsts.APPLYKEY,
				params, CellComAjaxHttp.HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						ToastUtils.show(KeyLogDetailsPassActivity.this, strMsg);
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						try {
							SubNoticeComm comm = t.read(SubNoticeComm.class,
									CellComAjaxResult.ParseType.GSON);
							String st = comm.getReturncode();
							String msg = comm.getReturnmessage();
							if (comm.getReturncode().equals(FlowConsts.STATUE_1)) {
								ShowMsg(comm.getBody().getContent());
								finish();
							}else{
								if (FlowConsts.STATUE_2.equals(st)) {
									// token失效
									ContextUtil.exitLogin(
											KeyLogDetailsPassActivity.this, header);
									return;
								}
								ShowMsg(msg);
							}
						} catch (Exception e) {
							ToastUtils.show(KeyLogDetailsPassActivity.this, "提交失败，服务器数据异常");
							e.printStackTrace();
						}
					}
				});
	}

}
