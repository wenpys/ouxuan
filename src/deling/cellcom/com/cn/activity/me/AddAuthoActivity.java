package deling.cellcom.com.cn.activity.me;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.ActionSheet;
import deling.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import deling.cellcom.com.cn.widget.Header;
import deling.cellcom.com.cn.widget.timepickerview.OptionsPickerView;
import deling.cellcom.com.cn.widget.timepickerview.TimePickerView2;
import deling.cellcom.com.cn.widget.timepickerview.TimePickerView2.OnTimeSelectListener;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

/**
 * 增加家属
 * 
 * @author xpw
 * 
 */
public class AddAuthoActivity extends FragmentActivityBase implements OnActionSheetSelected, OnCancelListener {
	private EditText etName;
	private EditText etPhone;
	private TextView tvIdentity;
	private TextView tvExpiry;
	private CheckBox cbAgree;
	private TextView tvUserKyeAgreement;
	private TextView tvAuthoKey;
	private LinearLayout llAgreeMent;
	private Button btSumbit;
	private Header header;
	private List<String> mCouponSCList;
	private List<String> mCouponGQList;
//	private OptionsPickerView expiryOptions;
	private OptionsPickerView identityOptions;
	private TimePickerView2 expiryTime;
	private ArrayList<String> mExpiryList;
	private ArrayList<String> mIdentityList;
	private int currExpiry = 0;
	private int currIdentity = 0;
	private String Roomids = "";
	private String roomidPosition = "";
//	private Map<String,String> curKey = new HashMap<String, String>();
//	private Map<String,String> curRoomids = new HashMap<String, String>();
//	private List<String> groupArray = new  ArrayList<String>();  
//	private List<List<String>> childArray = new  ArrayList<List<String>>();  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addautho);
		MyApplication.getInstances().getActivities().add(this);
//		keylist = new String[2];
//		keysel = new boolean[2];
//		for(int i=0;i<2;i++){
//			keylist[i] = "富力庭C栋21"+i;
//			keysel[i] = false;
//		}
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
		etName = (EditText) findViewById(R.id.name);
		etPhone = (EditText) findViewById(R.id.phone);
		tvIdentity = (TextView) findViewById(R.id.identity);
		tvExpiry = (TextView) findViewById(R.id.expiry);
		cbAgree = (CheckBox) findViewById(R.id.agree);
		tvUserKyeAgreement = (TextView) findViewById(R.id.user_key_agreement);
		btSumbit = (Button) findViewById(R.id.submitbtn);
		tvAuthoKey = (TextView) findViewById(R.id.authokey);
		tvAuthoKey.setMovementMethod(ScrollingMovementMethod.getInstance());
		llAgreeMent = (LinearLayout) findViewById(R.id.llagreement);
	}

	private void InitListeners() {
		tvIdentity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
		        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
				identityOptions.setSelectOptions(currIdentity);
				identityOptions.show();
			}
		});
		tvUserKyeAgreement.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddAuthoActivity.this, AgreementActivity.class);
				intent.putExtra("title", "用户手机钥匙使用协议");
				startActivity(intent);
			}
		});
		tvExpiry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
		        imm.hideSoftInputFromWindow(etName.getWindowToken(),0);
		        //默认选中当前时间
		        String strDate = tvExpiry.getText().toString();
		        if(!strDate.equals("")){
					try {		        	
			        	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			        	Date date;
						date = sdf.parse(strDate);
				        expiryTime.setTime(date);
					} catch (ParseException e) {						
						e.printStackTrace();
					}
		        }
				expiryTime.show();
			}
		});
		expiryTime.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(String str) {
				tvExpiry.setText(str);
			}
		});
		identityOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

			@Override
			public void onOptionsSelect(int options1, int options2, int options3) {

				tvIdentity.setText(mIdentityList.get(options1));
				currIdentity = options1;
			}
		});
		cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				btSumbit.setEnabled(arg1);
			}
		});
		tvAuthoKey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddAuthoActivity.this, SelectKeysActivity.class);
				intent.putExtra("ids", roomidPosition);
				startActivityForResult(intent, 301);
			}
		});
		btSumbit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String xm = etName.getText().toString();
				String sj = etPhone.getText().toString();
				String sf = tvIdentity.getText().toString();
				String yxq = tvExpiry.getText().toString();
				if (TextUtils.isEmpty(xm)) {
					ShowMsg(getResources().getString(R.string.qsrjsxm));
					etName.setFocusable(true);
					etName.requestFocus();
					return;
				}
				if(!RegExpValidator.IsChinese(xm)){
					ShowMsg("请输入中文姓名");
					etName.setFocusable(true);
					etName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(sj)) {
					ShowMsg(getResources().getString(R.string.qsrjssjh));
					etName.setFocusable(true);
					etName.requestFocus();
					return;
				}
				if (!RegExpValidator.IsHandset(sj)) {
					ShowMsg(getResources().getString(R.string.sjhgscw));
					return;
				}
				if (TextUtils.isEmpty(sf)) {
					ShowMsg(getResources().getString(R.string.qxzjssf));
					return;
				}
				if (TextUtils.isEmpty(yxq)) {
					ShowMsg("请选择门禁有效时间");
					return;
				}
				try {
					SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
				    Date date = simpleDateFormat.parse(yxq);
				    long timeStemp = date.getTime();
				    long curtime = System.currentTimeMillis();
				    if(curtime > timeStemp){
				    	ShowMsg("钥匙期限必须大于当前日期");
						return;
				    }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (TextUtils.isEmpty(yxq)) {
					ShowMsg("请选择门禁有效时间");
					return;
				}
				if (TextUtils.isEmpty(Roomids)) {
					ShowMsg("请选择授权钥匙");
					return;
				}
				int reale=currIdentity==15?-5:currIdentity;
				submit(sj, xm, (reale+8)+"", yxq, Roomids);
			}
		});
	}	
	
	//弹窗选择钥匙
/*	public void SelectRoomKey(){
		AlertDialog.Builder builder = new Builder(AddAuthoActivity.this);
		builder.setTitle("选择授权手机钥匙")
			.setMultiChoiceItems(keylist, keysel,new DialogInterface.OnMultiChoiceClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int position, boolean checked) {
					curKey.remove(position+"");
					curRoomids.remove(position+"");
					if(checked){
						curKey.put(position+"", keylist[position]);
						curRoomids.put(position+"", roomids[position]);
						keysel[position] = true;
					}else
						keysel[position] = false;
				}
			})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					StringBuffer str = new StringBuffer();
					for (String key : curKey.keySet()) {  
						str.append(curKey.get(key)).append(",");
					}
					tvAuthoKey.setText(str.toString());
				}
			})
			.setNegativeButton("取消", null)
			.create()
			.show();
		  
		groupArray.add("C2-c栋" );  
		  
		List<String> tempArray = new  ArrayList<String>();  
		tempArray.add("C2-c栋" );  
		tempArray.add("南大门" );  
		tempArray.add("C2-c栋车库" );  
		  
		for (int  index = 0 ; index <groupArray.size(); ++index)  
		{  
		    childArray.add(tempArray);  
		}

		AlertDialog.Builder builder = new Builder(AddAuthoActivity.this);
		builder.setTitle("选择钥匙");
		View view = LayoutInflater.from(AddAuthoActivity.this).inflate(R.layout.select_roomkeys, null);
		AlertDialog alertDialog = new AlertDialog.Builder(AddAuthoActivity.this).setTitle("选择钥匙").create();  
		alertDialog.show();  
		Window window = alertDialog.getWindow();  
		window.setContentView(R.layout.select_roomkeys);
		
		builder.setView(view);
		builder.setPositiveButton("确定", null);
		builder.setNegativeButton("取消", null);
		builder.create().show();

	}*/
	
	private void InitData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			etName.setText(bundle.getString("name"));
			etPhone.setText(bundle.getString("phone"));
			tvExpiry.setText(bundle.getString("expiry"));
			etName.setEnabled(false);
			etPhone.setEnabled(false);
			tvAuthoKey.setEnabled(false);
			tvExpiry.setEnabled(false);
			tvIdentity.setEnabled(false);
			llAgreeMent.setVisibility(View.GONE);
			btSumbit.setVisibility(View.GONE);
			header.setRightTextViewRes(R.string.edit, new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					TextView v = ((TextView)arg0);
					if(v.getText().equals("编辑")){
						v.setText("完成");
						etName.setEnabled(true);
						etPhone.setEnabled(true);
						tvAuthoKey.setEnabled(true);
						tvExpiry.setEnabled(true);
						tvIdentity.setEnabled(true);
					}else{
						v.setText("编辑");
						etName.setEnabled(false);
						etPhone.setEnabled(false);
						tvAuthoKey.setEnabled(false);
						tvExpiry.setEnabled(false);
						tvIdentity.setEnabled(false);					
					}
				}
			});
		}
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
			new OnClickListener() {

				@Override
				public void onClick(View v) {
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(
							header.getWindowToken(), 0);
					AddAuthoActivity.this.finish();
				}
			});
		header.setTitle("家属授权", null);

		expiryTime = new TimePickerView2(this, TimePickerView2.Type.YEAR_MONTH_DAY);
		// 控制时间范围
		expiryTime.setTime(new Date());
		expiryTime.setCyclic(false);
		expiryTime.setCancelable(true);
		        
        identityOptions = new OptionsPickerView<String>(AddAuthoActivity.this);
        //控制时间范围
        mIdentityList = new ArrayList<String>();
//        mIdentityList.add("父母");
//        mIdentityList.add("夫妻");
//        mIdentityList.add("儿女");
//        mIdentityList.add("朋友");
        mIdentityList.add("父亲");
        mIdentityList.add("母亲");
        mIdentityList.add("丈夫");
        mIdentityList.add("妻子");
        mIdentityList.add("儿子");
        mIdentityList.add("女儿");
        mIdentityList.add("其他亲戚");
        mIdentityList.add("租客");
        
        identityOptions.setPicker(mIdentityList);
        identityOptions.setCyclic(false);
        identityOptions.setCancelable(true);
	}
	

	// 获取可授权钥匙信息
	/*public void getValueKeyInfo() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(AddAuthoActivity.this).send(FlowConsts.CHECKUSERROOM,
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
					ValueRoomKeyComm bean = arg0.read(ValueRoomKeyComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
					List<ValueRoomKey> valueRoomKeys = bean.getBody();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						keylist = new String[valueRoomKeys.size()];
						keysel = new boolean[valueRoomKeys.size()];
						roomids = new String[valueRoomKeys.size()];
						for(int i=0;i<valueRoomKeys.size();i++){
//							keylist[i] = valueRoomKeys.get(i).getGatename();
							roomids[i] = valueRoomKeys.get(i).getRoomid();
//							keysel[i] = false;
							groupArray.add(valueRoomKeys.get(i).getGatename()); 
							List<RoomKey> keys =  valueRoomKeys.get(i).getKeys();
							List<String> tempArray = new  ArrayList<String>(); 
							Log.e("keys","key="+keys);
							for(int j=0;j<keys.size();j++){ 
								tempArray.add(keys.get(j).getKeyname());  
							}
							childArray.add(tempArray);
						}					
						SelectRoomKey();
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									AddAuthoActivity.this, header);
							return;
						}
						Log.e("getkeyinfo","error="+msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg("服务器数据异常！");
				}
			}
		});
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 302){
			Roomids = data.getExtras().getString("roomids");
			roomidPosition = data.getExtras().getString("roomidpos");
			String gatenames = data.getExtras().getString("gatenames");
			Log.e("curRoomid","id="+Roomids);
			Log.e("roomidpos","pos="+roomidPosition);
			Log.e("gatenames","gatenames="+gatenames);
			tvAuthoKey.setText(gatenames);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//提交
	public void submit(final String phone, final String name, final String identity, String expiry, String roomids) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("name", name);
		cellComAjaxParams.put("identity", identity);
		cellComAjaxParams.put("valuetime", expiry);
		cellComAjaxParams.put("roomids", roomids);
		HttpHelper.getInstances(AddAuthoActivity.this).send(FlowConsts.ADDGRANT,
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
											AddAuthoActivity.this, header);
								}
//								ShowMsg(getResources().getString(R.string.sqsqtjcg));
//								setResult(RESULT_OK);
//								finish();
								ActionSheet.showSheet(AddAuthoActivity.this, 
										(OnActionSheetSelected)AddAuthoActivity.this, 
										(OnCancelListener)AddAuthoActivity.this, "4");
							}else{
								if (FlowConsts.STATUE_2.equals(state)) {
									// token失效
									ContextUtil.exitLogin(
											AddAuthoActivity.this, header);
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

	@Override
	public void onClick(int whichButton) {
		String grant_content = PreferencesUtils.getString(AddAuthoActivity.this, "grant_content", "");
		String grant_url = PreferencesUtils.getString(AddAuthoActivity.this, "grant_url", "");
		switch (whichButton) {
		case 6:
			setResult(RESULT_OK);
			finish();
		case 7:
			new ShareAction(this)
			.setPlatform(SHARE_MEDIA.WEIXIN)
			.withText(grant_content)
			.withTargetUrl(grant_url)
			.withMedia(new UMImage(AddAuthoActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
			.share();
			break;
		default:
			break;
		}
	}

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		
	}
}
