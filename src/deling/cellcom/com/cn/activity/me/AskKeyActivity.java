package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.umeng.analytics.MobclickAgent;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.AreaBy;
import deling.cellcom.com.cn.bean.AreaByComm;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.Door;
import deling.cellcom.com.cn.bean.DoorComm;
import deling.cellcom.com.cn.bean.Gate;
import deling.cellcom.com.cn.bean.GateComm;
import deling.cellcom.com.cn.bean.SubNoticeComm;
import deling.cellcom.com.cn.bean.Village;
import deling.cellcom.com.cn.bean.VillageComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;
import deling.cellcom.com.cn.widget.timepickerview.OptionsPickerView;
import deling.cellcom.com.cn.widget.timepickerview.OptionsPickerView.OnOptionsSelectListener;
import deling.cellcom.com.cn.widget.timepickerview.TimePickerView;

/**
 * 申请密钥界面
 * */
public class AskKeyActivity extends FragmentActivityBase {
	private Header header;
	private EditText etName;
	private TextView tvPhone;
	private TextView tvStatus;
	private TextView tvCity;
	private TextView etAreas;
	private TextView etDoor;
	private TextView etRoom;
	private LinearLayout llCity;
	private TextView tvSubmit;
	private LinearLayout llStatus;
	private LinearLayout llArea;
	private LinearLayout llDoor;
	private LinearLayout llRoom;
	// 地图相关
	private LocationClient locationClient;
	private MyMapListener listener;
	// 时间选择器
	TimePickerView pvTime;
	private AnimationDrawable animationDrawable;
	private OptionsPickerView optionsPickerView;
	private OptionsPickerView gateView;
	private ArrayList<String> options;
	private ArrayList<String> gateOptions;
	private OptionsPickerView doorView;
	private ArrayList<String> names;
	private OptionsPickerView areaView;
	private ArrayList<String> areas;
	private int index;
	private List<Door> doors;
	private String selGateId;
	private String selDoorId = "错误";
	private String selAreaId;

	private Village village;
	private List<Gate> gates;
	private List<AreaBy> areaBeans;
	
	private CheckBox checkBox;
	private TextView tvAgree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_key);
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
		initListener();
	}
	
	private void initView() {
		header = (Header) findViewById(R.id.header);
		etName = (EditText) findViewById(R.id.et_name);
		tvPhone = (TextView) findViewById(R.id.et_phone);
		tvStatus = (TextView) findViewById(R.id.tv_status);
		tvCity = (TextView) findViewById(R.id.et_city);
		etAreas = (TextView) findViewById(R.id.et_areas);
		etDoor = (TextView) findViewById(R.id.et_door);
		etRoom = (TextView) findViewById(R.id.et_room);
		llCity = (LinearLayout) findViewById(R.id.ll_city);
		tvSubmit = (TextView) findViewById(R.id.tv_submit);
		llStatus = (LinearLayout) findViewById(R.id.ll_status);
		llArea = (LinearLayout) findViewById(R.id.ll_area);
		llDoor = (LinearLayout) findViewById(R.id.ll_door);
		llRoom = (LinearLayout) findViewById(R.id.ll_room);
		checkBox=(CheckBox) findViewById(R.id.agree);
		tvAgree=(TextView) findViewById(R.id.user_key_agreement);
	}

	private void initData() {
		initMap();
		header.setTitle(getResources().getString(R.string.activity_askkey));
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		// header.setRightImageViewRes(R.drawable.icon_activity_askkey_scan, new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent=new Intent(AskKeyActivity.this,CaptureActivity.class);
		// startActivity(intent);
		// finish();
		// }
		// });
		header.setRightTextViewRes(R.string.sqjl, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OpenActivity(ApplyKeyLogActivity.class);
			}
		});
		tvPhone.setText(MyApplication.getInstances().getPhone());
		// 时间选择器
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);

		optionsPickerView = new OptionsPickerView<String>(AskKeyActivity.this);
		options = new ArrayList<String>();
		options.add("户主");
		options.add("家属");
		options.add("租客");
		optionsPickerView.setPicker(options);
		optionsPickerView.setCyclic(false);
		optionsPickerView.setCancelable(true);

		gateView = new OptionsPickerView<String>(AskKeyActivity.this);
		doorView = new OptionsPickerView<String>(AskKeyActivity.this);
		areaView = new OptionsPickerView<String>(AskKeyActivity.this);
	}

	private void initMap() {
		locationClient = new LocationClient(AskKeyActivity.this);
		listener = new MyMapListener();
		locationClient.registerLocationListener(listener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		// 1分钟定位一次
		int span = 1000 * 60 * 60 * 24;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		locationClient.setLocOption(option);
		locationClient.start();
	}

	private void initListener() {
		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(tvCity.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，城市信息不能为空哦!!!");
					return;
				}
				if (TextUtils.isEmpty(etAreas.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，小区信息不能为空哦!!!");
					return;
				}
				if (TextUtils.isEmpty(etDoor.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，门栋信息不能为空哦!!!");
					return;
				}
				if (TextUtils.isEmpty(etName.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，姓名不能为空哦!!!");
					return;
				}
				if(isPassForName(etName.getText().toString())){
					ToastUtils.show(AskKeyActivity.this, "亲，姓名不能包含非法字符哦!!!");
					return;
				}
				if (!RegExpValidator.IsHandset(tvPhone.getText().toString())) {
					ShowMsg(getResources().getString(R.string.sjhgscw));
					return;
				}
				if (TextUtils.isEmpty(tvPhone.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，手机号码不能为空哦!!!");
					return;
				}
				if (TextUtils.isEmpty(tvStatus.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，身份不能为空哦!!!");
					return;
				}
				if (TextUtils.isEmpty(etRoom.getText().toString())) {
					ToastUtils.show(AskKeyActivity.this, "亲，房号不能为空哦!!!");
					return;
				}
				pushDataToService();
			}
		});
		// 城市
		llCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
				pvTime.show();
			}
		});
		// 时间选择后回调
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Intent intent) {
				// TODO
				if (intent != null) {
					String content = intent.getStringExtra("content");
					String areaId = intent.getStringExtra("areaid");
					tvCity.setText(content);
					etAreas.setText("");
					etDoor.setText("");
					etRoom.setText("");
					getAreas(areaId);
				}
			}
		});
		// 身份
		llStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionsPickerView.setSelectOptions(0);
				optionsPickerView.show();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
			}
		});
		optionsPickerView
				.setOnoptionsSelectListener(new OnOptionsSelectListener() {

					@Override
					public void onOptionsSelect(int options1, int option2,
							int options3) {
						tvStatus.setText(options.get(options1));
						index = options1 + 1;
					}
				});
		// 小区
		gateView.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				selGateId = gates.get(options1).getId();
				etDoor.setText(gateOptions.get(options1));
				etRoom.setText("");
				getDoorid();
			}
		});
		doorView.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				selDoorId = doors.get(options1).getId();
				etRoom.setText(names.get(options1));
			}
		});
		// 门栋
		llDoor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gateOptions != null && gateOptions.size() > 0) {
					gateView.show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
				}
			}
		});
		// 房间
		llRoom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (names != null && names.size() > 0) {
					doorView.show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
				}
			}
		});
		areaView.setOnoptionsSelectListener(new OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int option2, int options3) {
				village.setAreaid(areaBeans.get(options1).getAreaid());
				etAreas.setText(areas.get(options1));
				etDoor.setText("");
				etRoom.setText("");
				getGateid();
			}
		});
		// 区域
		llArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (areas != null && areas.size() > 0) {
					areaView.show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
				}
			}
		});
		tvAgree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AskKeyActivity.this, AgreementActivity.class);
				intent.putExtra("title", "用户手机钥匙使用协议");
				startActivity(intent);
			}
		});
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				tvSubmit.setEnabled(isChecked);
			}
		});
	}

	/**
	 * 提交钥匙申请
	 */
	protected void pushDataToService() {
		CellComAjaxParams params = new CellComAjaxParams();
		if (selDoorId.equals("错误")) {
			ToastUtils.show(AskKeyActivity.this, "请选择房号");
			return;
		}
//		if(!RegExpValidator.IsChinese(etName.getText().toString())){
//			ToastUtils.show(AskKeyActivity.this, "请输入中文姓名");
//			return;
//		}
		params.put("doorid", selDoorId);
		params.put("type", index + "");
		params.put("name", etName.getText().toString());
		HttpHelper.getInstances(AskKeyActivity.this).send(FlowConsts.APPLYKEY,
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
						ToastUtils.show(AskKeyActivity.this, strMsg);
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
											AskKeyActivity.this, header);
									return;
								}
								ShowMsg(msg);
							}
						} catch (Exception e) {
							ToastUtils.show(AskKeyActivity.this, "提交失败，服务器数据异常");
							e.printStackTrace();
						}
					}
				});
	}

	public class MyMapListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			getNoticeData(lat, lon);
		}
	}

	public void getNoticeData(double lat, double lon) {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("lat", lat + "");
		params.put("lon", lon + "");
		HttpHelper.getInstances(AskKeyActivity.this).send(
				FlowConsts.GETDEPARTMENT, params,
				CellComAjaxHttp.HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						super.onStart();
						ShowProgressDialog(R.string.app_loading);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						village = new Village();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						VillageComm comm = null;
						try {
							comm = t.read(VillageComm.class,
									CellComAjaxResult.ParseType.GSON);
							String st = comm.getReturncode();
							if (!comm.getReturncode().equals(FlowConsts.STATUE_1)) {
								if (FlowConsts.STATUE_2.equals(st)) {
									// token失效
									ContextUtil.exitLogin(
											AskKeyActivity.this, header);
									return;
								}
								ToastUtils.show(AskKeyActivity.this,
										"搜索不到相关的小区信息!!!");
								if (village == null) {
									village = new Village();
								}
								return;
							}
							village = comm.getBody();
							updateView();
							getGateid();
							getAreas(village.getRegionid());
						} catch (Exception e) {
							ToastUtils.show(AskKeyActivity.this, "数据异常!!!");
							if (village == null) {
								village = new Village();
							}
							return;
						}
					}
				});
	}

	// 获取小区
	protected void getAreas(String regionid) {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("regionid", regionid);
		HttpHelper.getInstances(AskKeyActivity.this).send(FlowConsts.GETAREA,
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
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						AreaByComm comm = null;
						try {
							comm = t.read(AreaByComm.class,
									CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ToastUtils.show(AskKeyActivity.this, "获取小区数据异常!!!");
							return;
						}
						if (!comm.getReturncode().equals(FlowConsts.STATUE_1)) {
							ToastUtils.show(AskKeyActivity.this,
									"获取小区信息数据异常!!!");
							return;
						}
						areaBeans = comm.getBody();
						areas = new ArrayList<String>();
						for (AreaBy area : areaBeans) {
							areas.add(area.getName());
						}
						areaView.setPicker(areas);
						areaView.setCyclic(false);
						areaView.setCancelable(true);
					}
				});

	}

	protected void updateView() {
		tvCity.setText(village.getProvincename() + "-" + village.getCityname()
				+ "-" + village.getRegionname());
		etAreas.setText(village.getAreaname());
	}

	// 获取门的id
	public void getDoorid() {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("id", selGateId);
		HttpHelper.getInstances(AskKeyActivity.this).send(FlowConsts.GETDOOR,
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
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						DoorComm comm = null;
						try {
							comm = t.read(DoorComm.class,
									CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ToastUtils.show(AskKeyActivity.this, "获取门数据异常!!!");
							return;
						}
						if (!comm.getReturncode().equals(FlowConsts.STATUE_1)) {
							ToastUtils.show(AskKeyActivity.this, "获取信息数据异常!!!");
							return;
						}
						doors = comm.getBody();
						names = new ArrayList<String>();
						for (Door door : doors) {
							names.add(door.getName());
						}
						doorView.setPicker(names);
						doorView.setCyclic(false);
						doorView.setCancelable(true);
					}
				});
	}

	// 获取门栋id
	public void getGateid() {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("id", village.getAreaid());
		HttpHelper.getInstances(AskKeyActivity.this).send(FlowConsts.GETGATE,
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
						DismissProgressDialog();
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						GateComm comm = null;
						try {
							comm = t.read(GateComm.class,
									CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ToastUtils.show(AskKeyActivity.this, "获取门栋数据异常");
							return;
						}
						if (!comm.getReturncode().equals(FlowConsts.STATUE_1)) {
							ToastUtils.show(AskKeyActivity.this, "数据异常");
							return;
						}
						gates = comm.getBody();
						gateOptions = new ArrayList<String>();
						for (Gate g : gates) {
							gateOptions.add(g.getName());
						}
						gateView.setPicker(gateOptions);
						gateView.setCyclic(false);
						gateView.setCancelable(true);
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        MobclickAgent.onPageStart("AskKeyScreen");
        MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onEvent(AskKeyActivity.this, "applykey");
        MobclickAgent.onPageEnd("AskKeyScreen");
        MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationClient != null && listener != null) {
			locationClient.unRegisterLocationListener(listener);
		}
		if (locationClient.isStarted()) {
			locationClient.stop();
		}
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode , Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 判断名字是否有火星文
	 * */
	public boolean isPassForName(String name){
		  String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		  Pattern p = Pattern.compile(regEx);
		  Matcher m = p.matcher(name);
		  return m.find();
	}
}
