package deling.cellcom.com.cn.activity.me;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import deling.cellcom.com.cn.activity.zxing.activity.CaptureActivity;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.Door;
import deling.cellcom.com.cn.bean.DoorComm;
import deling.cellcom.com.cn.bean.Village;
import deling.cellcom.com.cn.bean.VillageComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;
import deling.cellcom.com.cn.widget.timepickerview.OptionsPickerView;
import deling.cellcom.com.cn.widget.timepickerview.OptionsPickerView.OnOptionsSelectListener;
/**
 * 通过扫描二维码申请钥匙
 *
 */
public class AskKeyByScanActivity extends Activity {
	private Header header;
	private TextView tvSubmit;
	private TextView tvCity;
	private TextView tvAreas;
	private TextView tvDoor;
	private EditText etName;
	private EditText etPhone;
	private TextView tvStatus;
	private TextView etRoom;
	//二维码扫描的结果
	private String resultData;
	private Village village;
	
	private OptionsPickerView pickerView;
	private ArrayList<String> mIdentityList;
	private ArrayList<String> doorOptions;
	private OptionsPickerView doorView;
	private int index;
	private List<Door> doors;
	private String doorId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_askkey_scan);
		resultData=getIntent().getStringExtra("data");
		initView();
		initData();
		initListener();
	}

	private void initView() {
		header=(Header) findViewById(R.id.header);
		tvSubmit=(TextView) findViewById(R.id.tv_submit);
		tvCity=(TextView) findViewById(R.id.tv_city);
		tvAreas=(TextView) findViewById(R.id.tv_areas);
		tvDoor=(TextView) findViewById(R.id.tv_door);
		etName=(EditText) findViewById(R.id.et_name);
		etPhone=(EditText) findViewById(R.id.et_phone);
		tvStatus=(TextView) findViewById(R.id.et_status);
		etRoom=(TextView) findViewById(R.id.et_room);
	}

	private void initData() {
		header.setTitle(getResources().getString(R.string.activity_askkey));
		header.setLeftImageVewRes(R.drawable.main_nav_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AskKeyByScanActivity.this.finish();
			}
		});
		header.setRightImageViewRes(R.drawable.icon_activity_askkey_scan, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(AskKeyByScanActivity.this,CaptureActivity.class);
				startActivity(intent);
				finish();
			}
		});
		pickerView=new OptionsPickerView<String>(AskKeyByScanActivity.this);
        //控制时间范围
        mIdentityList = new ArrayList<String>();
        mIdentityList.add("业主");
        mIdentityList.add("家属");
        mIdentityList.add("租户");
        
        pickerView.setPicker(mIdentityList);
        pickerView.setCyclic(false);
        pickerView.setCancelable(true);
		getDoorData();
		doorView=new OptionsPickerView<String>(AskKeyByScanActivity.this);
	}

	private void initListener() {
		tvStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickerView.setSelectOptions(0);
				pickerView.show();
			}
		});
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(tvCity.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，城市信息不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(tvAreas.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，小区信息不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(tvDoor.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，门栋信息不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(etName.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，姓名不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(etPhone.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，手机号码不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(tvStatus.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，身份不能为空哦!!!");
					return;
				}
				if(TextUtils.isEmpty(etRoom.getText().toString())){
					ToastUtils.show(AskKeyByScanActivity.this, "亲，房号不能为空哦!!!");
					return;
				}
				pushDataToService();
			}
		});
		pickerView.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				tvStatus.setText(mIdentityList.get(options1));
				index=options1+1;
			}
		});
		etRoom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doorView.show();
			}
		});
		doorView.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				doorId=doors.get(options1).getId();
				etRoom.setText(doorOptions.get(options1));
			}
		});
	}
	/**
	 * 提交钥匙申请
	 */
	protected void pushDataToService() {
		CellComAjaxParams params=new CellComAjaxParams();
		if(TextUtils.isEmpty(doorId)){
			ToastUtils.show(AskKeyByScanActivity.this, "房号不能为空");
			return;
		}
		params.put("doorid", doorId);
		params.put("type", index+"");
		params.put("name", etName.getText().toString());
		HttpHelper.getInstances(AskKeyByScanActivity.this).send(FlowConsts.APPLYKEY, 
				params, 
				CellComAjaxHttp.HttpWayMode.POST, 
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						ToastUtils.show(AskKeyByScanActivity.this, strMsg);
					}
					@Override
					public void onSuccess(CellComAjaxResult t) {
						Comm comm=t.read(Comm.class, CellComAjaxResult.ParseType.GSON);
						String st = comm.getReturncode();
						ToastUtils.show(AskKeyByScanActivity.this, comm.getReturnmessage());
						if(comm.getReturncode().equals(FlowConsts.STATUE_1)){
							finish();
						}else{
							if (FlowConsts.STATUE_2.equals(st)) {
								// token失效
								ContextUtil.exitLogin(
										AskKeyByScanActivity.this, header);
								return;
							}							
						}
					}
			});
	}
	
	/**
	 * 根据扫描的二维码的信息获取门栋信息
	 */
	private void getDoorData() {
		CellComAjaxParams params=new CellComAjaxParams();
		params.put("key", resultData);
		HttpHelper.getInstances(AskKeyByScanActivity.this).send(FlowConsts.GETDEPARTMENTFROMKEY, 
				params, 
				CellComAjaxHttp.HttpWayMode.POST, 
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						ToastUtils.show(AskKeyByScanActivity.this, strMsg);
					}
					@Override
					public void onSuccess(CellComAjaxResult t) {
						VillageComm comm=null;
						try {
							comm=t.read(VillageComm.class, CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ToastUtils.show(AskKeyByScanActivity.this, e.toString());
						}
						if(!FlowConsts.STATUE_1.equals(comm.getReturncode())){
							ToastUtils.show(AskKeyByScanActivity.this, comm.getReturnmessage());
						}
						updateView(comm.getBody());
					}
			});
	}

	protected void updateView(Village village) {
		this.village=village;
		tvCity.setText(village.getProvincename()+village.getCityname()+village.getRegionname());
		tvAreas.setText(village.getAreaname());
		tvDoor.setText(village.getGatename());
		getDoorid();
	}
	//获取门的id
	public void getDoorid(){
		CellComAjaxParams params=new CellComAjaxParams();
		params.put("id", village.getGateid());
		HttpHelper.getInstances(AskKeyByScanActivity.this).send(FlowConsts.GETDOOR, 
				params,
				CellComAjaxHttp.HttpWayMode.POST, 
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
					}
					@Override
					public void onSuccess(CellComAjaxResult t) {
						DoorComm comm=t.read(DoorComm.class, CellComAjaxResult.ParseType.GSON);
						if(!comm.getReturncode().equals(FlowConsts.STATUE_1)){
							ToastUtils.show(AskKeyByScanActivity.this, "数据异常");
							return;
						}
						doors=comm.getBody();
						doorOptions=new ArrayList<String>();
						for (Door door : doors) {
							doorOptions.add(door.getName());
						}
						doorView.setPicker(doorOptions);
						doorView.setCyclic(false);
						doorView.setCancelable(true);
					}
				});
	}
}
