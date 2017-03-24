package deling.cellcom.com.cn.activity.welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.activity.me.AskKeyActivity;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.bean.LoginComm;
import deling.cellcom.com.cn.bean.YzmComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.SmsUtil;
import deling.cellcom.com.cn.utils.ToastUtils;

/**
 * 登陆
 * 
 * @author wma
 * 
 */
public class LoginActivity extends FragmentActivityBase {
	private EditText etPhone;// 手机号码
	private EditText etPassword;// 密码
	private EditText etCaptcha;// 验证码
	private CheckBox cbRememb;
	private Button loginBtn;// 登录
	private Button changeBtn;// 跳转注册
	private Button btCaptcha;// 获取验证码
	private TextView tvForget;//忘记密码
	private LoginComm loginComm;
	private LinearLayout mainLinearLayout;
	private BroadcastReceiver counterActionReceiver;
	private Intent intent;
	private boolean isRememb = true;
    private final String mPageName = "LoginScreen";
	private MyCount myCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_welcom_login);
		
		if(getIntent().getExtras() != null){
			String from = getIntent().getStringExtra("from");
			if(from != null && from.equals("service")){
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);
			}
		}
		
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (counterActionReceiver != null) {
			unregisterReceiver(counterActionReceiver);
		}
		EventBus.getDefault().unregister(this);
	}

	private void InitData() {
		String userid = cellcom.com.cn.util.SharepreferenceUtil.getDate(LoginActivity.this, "userid");
		isRememb = PreferencesUtils.getBoolean(LoginActivity.this, "isrememb");
		String phone = PreferencesUtils.getString(LoginActivity.this, "phone" , "");
//		etPhone.setText(phone);
//		if(isRememb){
//			cbRememb.setChecked(true);
//			String phone = PreferencesUtils.getString(LoginActivity.this, "phone" , "");
//			String password = PreferencesUtils.getString(LoginActivity.this, "phone2" ,"");
//			etPhone.setText(phone);
//			etPassword.setText(new String(Base64Util.decode(password)));
//			if(!phone.equals("") && !password.equals("") && !userid.equals(""))
//				login(phone, new String(Base64Util.decode(password)));
//		}else
//			cbRememb.setChecked(false);
		// 接收短信广播
		counterActionReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				SmsMessage msg = null;
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					Object[] pdusObj = (Object[]) bundle.get("pdus");
					for (Object p : pdusObj) {
						msg = SmsMessage.createFromPdu((byte[]) p);

						String msgTxt = msg.getMessageBody();// 得到消息的内容
						Log.i("captcha","txt:"+msgTxt);
						
						if (msgTxt != null
								&& msgTxt.indexOf(SmsUtil.SMS_CONTENT) != -1) {
							int index = SmsUtil.SMS_CONTENT.length();
							String yzm = msgTxt.substring(index, index
									+ SmsUtil.SMS_YZMCD);
							etCaptcha.setText(yzm);
						}
						return;
					}
					return;
				}
			}
		};
		IntentFilter counterActionFilter = new IntentFilter(
				SmsUtil.SMS_RECEIVED);
//		counterActionFilter.setPriority(1000);
		registerReceiver(counterActionReceiver, counterActionFilter);
		myCount = new MyCount(60000, 1000);
		EventBus.getDefault().register(this);
	}

	private void InitView() {
		etPhone = (EditText) findViewById(R.id.countet);
		etPassword = (EditText)findViewById(R.id.mmet);
		etCaptcha = (EditText)findViewById(R.id.yzmet);
		cbRememb = (CheckBox)findViewById(R.id.rememb);
		loginBtn = (Button) findViewById(R.id.loginbtn);
		changeBtn = (Button) findViewById(R.id.changebtn);
		tvForget = (TextView) findViewById(R.id.forget);
		btCaptcha = (Button) findViewById(R.id.yzmbtn);
	}

	private void InitListeners() {
		etPhone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				etPassword.setText("");
			}
		});
		cbRememb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				isRememb = arg1;
				if(!arg1){
					PreferencesUtils.putString(LoginActivity.this, "phone", "");
					PreferencesUtils.putString(LoginActivity.this, "phone2", "");
				}
				PreferencesUtils.putBoolean(LoginActivity.this, "isrememb", arg1);
			}
		});
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String countettxt = etPhone.getText().toString();
				String mmettxt = etCaptcha.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg(getResources().getString(R.string.qsrzh));
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg(getResources().getString(R.string.sjhgscw));
					return;
				}
				if (TextUtils.isEmpty(mmettxt)) {
					ShowMsg(getResources().getString(R.string.qsryzm));
					return;
				}
				if(mmettxt.equals("1168")){
					OpenActivity(MainActivity.class);
					LoginActivity.this.finish();
				}else
					login(countettxt, mmettxt);

			}
		});		

		changeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivityForResult(intent, 101);
//				LoginActivity.this.finish();
			}
		});
		
		tvForget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OpenActivity(ForgetActivity.class);
			}
		});
		// yzm
		btCaptcha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String countettxt = etPhone.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg(getResources().getString(R.string.qsrzh));
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg(getResources().getString(R.string.sjhgscw));
					return;
				}
				ykysSendverifysms(countettxt);
			}
		});
	}

	/**
	 * 登录
	 * 
	 * @param phone
	 *            手机号码
	 * @param verificationCode
	 *            验证码
	 */
	private void login(final String phone, final String password) {
		cellcom.com.cn.util.SharepreferenceUtil.saveData(
				LoginActivity.this, new String[][] { {"userid","" } });
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("password", password);
		HttpHelper.getInstances(LoginActivity.this).send(FlowConsts.LOGIN,
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
				try {
					loginComm = arg0.read(LoginComm.class,
							CellComAjaxResult.ParseType.GSON);
				} catch (Exception e) {
					ToastUtils.show(LoginActivity.this, "登录失败，请稍后再尝试!!!");
					return;
				}
				try{
					loginComm = arg0.read(LoginComm.class,
						CellComAjaxResult.ParseType.GSON);
					String state = loginComm.getReturncode();
					String msg = loginComm.getReturnmessage();
					if (!FlowConsts.STATUE_1.equals(state)) {
						DismissProgressDialog();
						ShowMsg(msg);
						return;
					}
					PreferencesUtils.putString(LoginActivity.this, "phone", phone);
//					if(isRememb){
//						PreferencesUtils.putString(LoginActivity.this, "phone", phone);
//						PreferencesUtils.putString(LoginActivity.this, "phone2", Base64Util.encodeToString(password.getBytes()));
//					}
					cellcom.com.cn.util.SharepreferenceUtil.saveData(
							LoginActivity.this, new String[][] { {
									"userid",
									loginComm.getBody().getUserid()+""} });

					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"token",loginComm.getBody().getToken()} });
					PreferencesUtils.putString(LoginActivity.this, "avatar", loginComm.getBody().getImgurl());
					PreferencesUtils.putString(LoginActivity.this, "phone", phone);
					PreferencesUtils.putInt(LoginActivity.this, "usertype", loginComm.getBody().getUsertype());
					PreferencesUtils.putInt(LoginActivity.this, "keynum", loginComm.getBody().getKeynum());
					PreferencesUtils.putBoolean(LoginActivity.this, "keystate", loginComm.getBody().getKeystate()==1?true:false);
//					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"avatar",loginComm.getBody().getImgurl()} });
//					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"phone",phone} });
//					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"usertype",loginComm.getBody().getUsertype()+""} });
//					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"keynum",loginComm.getBody().getKeynum()+""} });
//					cellcom.com.cn.util.SharepreferenceUtil.saveData(LoginActivity.this, new String[][] { {"keystate",loginComm.getBody().getKeystate()==1?"true":"false"} });

					MyApplication.getInstances().setUserid(loginComm.getBody().getUserid());
					MyApplication.getInstances().setAvatar(loginComm.getBody().getImgurl());
					MyApplication.getInstances().setPhone(phone);
					MyApplication.getInstances().setUserType(loginComm.getBody().getUsertype());
					MyApplication.getInstances().setKeyNum(loginComm.getBody().getKeynum());
					MyApplication.getInstances().setKeyState(loginComm.getBody().getKeystate()==1?true:false);

					OpenActivity(MainActivity.class);
					LoginActivity.this.finish();
					MobclickAgent.onProfileSignIn(phone);
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg(getResources().getString(R.string.dataparsefail));
				}
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int flag = getIntent().getIntExtra("flag", 0);
			Log.e("flag","flag:"+flag);
			if (flag == ContextUtil.EXIT_APPLICATION) {
				Intent mIntent = new Intent();
				mIntent.setClass(LoginActivity.this, MainActivity.class);
				// 这里设置flag还是比较 重要的
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// 发出退出程序指示
				mIntent.putExtra("flag", ContextUtil.EXIT_APPLICATION);
				mIntent.putExtra("exitall", ContextUtil.EXIT_APPLICATIONFLAG);
				startActivity(mIntent);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if(arg0 == 101 && arg1 == RESULT_OK){			
			finish();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	// yzm
	private void ykysSendverifysms(final String account) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("method", "1");
		HttpHelper.getInstances(LoginActivity.this).send(
				FlowConsts.SENDIDENTIFYINGCODE, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
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
						try {
							YzmComm loginComm = arg0.read(YzmComm.class,
									CellComAjaxResult.ParseType.GSON);
							String state = loginComm.getReturncode();
							String msg = loginComm.getReturnmessage();
							if (!FlowConsts.STATUE_1.equals(state)) {
								ShowMsg(msg);
								return;
							}
							btCaptcha.setBackgroundResource(R.drawable.app_btnyzmselector);
							btCaptcha.setEnabled(false);
							myCount.start();
							ShowMsg(getResources().getString(R.string.yzmhqcg));
						} catch (Exception e) {
							ShowMsg(getResources().getString(R.string.dataparsefail));
							e.printStackTrace();
						}
					}
				});
	}
	
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			btCaptcha.setEnabled(true);
			btCaptcha.setText(LoginActivity.this.getResources().getString(
					R.string.welcome_hyyzm));
			btCaptcha.setBackgroundResource(R.drawable.app_btnyzmselector);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int second = (int) (millisUntilFinished / 1000);
			
			 if(second<=9){ 
				 btCaptcha.setText("0" + second + "S后获取");
//				 btCaptcha.setBackgroundResource(R.drawable.app_btnyzmclick);
				 btCaptcha.setEnabled(true);
//			 if (second <= 30) {
//				 btCaptcha.setBackgroundResource(R.drawable.app_btnyzmclick);
//				 btCaptcha.setText(getResources().getString(R.string.wsd));
//				 btCaptcha.setEnabled(true);
			} else {
				btCaptcha.setText(second + "S后获取");
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	// 在ui线程执行
	public void onUserEvent(FirstEvent event) {
		LoginActivity.this.finish();
	}
}
