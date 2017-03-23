package deling.cellcom.com.cn.activity.welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import deling.cellcom.com.cn.activity.me.AgreementActivity;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.bean.LoginComm;
import deling.cellcom.com.cn.bean.YzmComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.SmsUtil;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;

/**
 * 注册
 * 
 * @author wma
 * 
 */
public class RegisterActivity extends FragmentActivityBase {
	private EditText countet;// 账号
	private EditText verificationCode;// 验证码
	private EditText etPwd;//密码
	private EditText etCmPwd;//确认密码
	private Button btRegister;// 注册
	private Button cerificationCodeBtn;// 获取验证码
	private Header header;
	private MyCount myCount;
	private CheckBox cbAgree;
	private LoginComm loginComm;
	private boolean isRememb;
	private TextView tvAgreement;
	
	private BroadcastReceiver counterActionReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_welcom_reister);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myCount != null) {
			myCount.cancel();// 方法结束
		}
		this.unregisterReceiver(counterActionReceiver);
		EventBus.getDefault().unregister(this);
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
						RegisterActivity.this.finish();
					}
				});
		header.setTitle(getResources().getString(R.string.welcome_zcxyh), null);
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

						if (msgTxt != null
								&& msgTxt.indexOf(SmsUtil.SMS_CONTENT) != -1) {
							int index = SmsUtil.SMS_CONTENT.length();
							String yzm = msgTxt.substring(index, index
									+ SmsUtil.SMS_YZMCD);
							verificationCode.setText(yzm);
						}
						return;
					}
					return;
				}
			}
		};
		IntentFilter counterActionFilter = new IntentFilter(
				SmsUtil.SMS_RECEIVED);
		registerReceiver(counterActionReceiver, counterActionFilter);
		myCount = new MyCount(60000, 1000);
		EventBus.getDefault().register(this);
	}

	private void InitView() {
		countet = (EditText) findViewById(R.id.countet);
		verificationCode = (EditText) findViewById(R.id.yzmet);
		etPwd = (EditText) findViewById(R.id.mmet);
		etCmPwd = (EditText) findViewById(R.id.qrmmet);
		btRegister = (Button) findViewById(R.id.registerbtn);
		cerificationCodeBtn = (Button) findViewById(R.id.yzmbtn);
		header = (Header) findViewById(R.id.header);
		cbAgree = (CheckBox) findViewById(R.id.agree);
		tvAgreement = (TextView) findViewById(R.id.agreement);
	}

	private void InitListeners() {
		cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				btRegister.setEnabled(arg1);
			}
		});
		tvAgreement.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegisterActivity.this, AgreementActivity.class);
				intent.putExtra("title", "用户使用协议");
				startActivity(intent);
			}
		});
		btRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String countettxt = countet.getText().toString();
				String pwdtxt = etPwd.getText().toString();
				String cmpwdtxt = etCmPwd.getText().toString();
				String yzmettxt = verificationCode.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg(getResources().getString(R.string.qsrzh));
					countet.setFocusable(true);
					return;
				}
				if (!RegExpValidator.IsHandset(countettxt)) {
					ShowMsg(getResources().getString(R.string.sjhgscw));
					return;
				}
				if (TextUtils.isEmpty(yzmettxt)) {
					ShowMsg(getResources().getString(R.string.qsryzm));
					verificationCode.setFocusable(true);
					return;
				}
				if (TextUtils.isEmpty(pwdtxt)) {
					ShowMsg(getResources().getString(R.string.qsrmm));
					etPwd.setFocusable(true);
					return;
				}
				if (!RegExpValidator.IsPasswLength(pwdtxt)) {
					ShowMsg(getResources().getString(R.string.mmcdcw));
					return;
				}
				if (!RegExpValidator.IsPassword(pwdtxt)) {
					ShowMsg(getResources().getString(R.string.mmgscw));
					return;
				}
				if (TextUtils.isEmpty(cmpwdtxt)) {
					ShowMsg(getResources().getString(R.string.qsrqrmm));
					etCmPwd.setFocusable(true);
					return;
				}
				if (!cmpwdtxt.equals(pwdtxt)) {
					ShowMsg(getResources().getString(R.string.cqmmsrbyz));
					return;
				}
				register(countettxt, pwdtxt, yzmettxt);
			}
		});
		// yzm
		cerificationCodeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String countettxt = countet.getText().toString();
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

	// 注册
	private void register(final String account, final String password, final String verifysms) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("password", password);
		cellComAjaxParams.put("verifysms", verifysms);
		HttpHelper.getInstances(RegisterActivity.this).send(FlowConsts.DL_REGISTER,
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
						Comm comm=null;
						try {
							comm= arg0.read(Comm.class,
									CellComAjaxResult.ParseType.GSON); 
							String state = comm.getReturncode();
							String msg = comm.getReturnmessage();
							if (!FlowConsts.STATUE_1.equals(state)) {
								ShowMsg(msg);
								return;
							}
							PreferencesUtils.putBoolean(RegisterActivity.this, "isrememb", false);
							PreferencesUtils.putString(RegisterActivity.this, "phone", "");
							PreferencesUtils.putString(RegisterActivity.this, "phone2", "");
	//						OpenActivity(MainActivity.class);
							//登录
							MobclickAgent.onProfileSignIn(account);
							login(account,password);
						} catch (Exception e) {
							DismissProgressDialog();
							ShowMsg("注册返回结果异常!!!");
						}
					}
				});

	}

	// yzm
	private void ykysSendverifysms(final String account) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("method", "1");
		HttpHelper.getInstances(RegisterActivity.this).send(
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
						YzmComm loginComm = arg0.read(YzmComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = loginComm.getReturncode();
						String msg = loginComm.getReturnmessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						cerificationCodeBtn.setBackgroundResource(R.drawable.app_btnyzmselector);
						cerificationCodeBtn.setEnabled(false);
						myCount.start();
						ShowMsg(getResources().getString(R.string.yzmhqcg));
					}
				});
	}

	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			cerificationCodeBtn.setEnabled(true);
			cerificationCodeBtn.setText(RegisterActivity.this.getResources().getString(
					R.string.welcome_hyyzm));
			cerificationCodeBtn.setBackgroundResource(R.drawable.app_btnyzmselector);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int second = (int) (millisUntilFinished / 1000);
			/*
			 * if(second<=9){ yzmbtn.setText("0" + second+"秒"); }else
			 */if (second <= 30) {
				cerificationCodeBtn.setBackgroundResource(R.drawable.app_btnyzmclick);
				cerificationCodeBtn.setText(getResources().getString(R.string.wsd));
				cerificationCodeBtn.setEnabled(true);
			} else {
				cerificationCodeBtn.setText(second + getResources().getString(R.string.m));
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	// 在ui线程执行
	public void onUserEvent(FirstEvent event) {
		RegisterActivity.this.finish();
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
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("password", password);
		HttpHelper.getInstances(RegisterActivity.this).send(FlowConsts.LOGIN,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				DismissProgressDialog();
				OpenActivity(LoginActivity.class);
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				DismissProgressDialog();
				try {
					loginComm = arg0.read(LoginComm.class,
							CellComAjaxResult.ParseType.GSON);
				} catch (Exception e) {
					ToastUtils.show(RegisterActivity.this, "登录失败，请稍后再尝试!!!");
					return;
				}
				try{
					loginComm = arg0.read(LoginComm.class,
						CellComAjaxResult.ParseType.GSON);
					String state = loginComm.getReturncode();
					String msg = loginComm.getReturnmessage();
					if (!FlowConsts.STATUE_1.equals(state)) {
						ShowMsg(msg);
						return;
					}
					MobclickAgent.onProfileSignIn(phone);
					cellcom.com.cn.util.SharepreferenceUtil.saveData(
							RegisterActivity.this, new String[][] { {
									"userid",
									loginComm.getBody().getUserid()+""} });

					cellcom.com.cn.util.SharepreferenceUtil.saveData(
							RegisterActivity.this, new String[][] { {
									"token",
									loginComm.getBody().getToken()} });

					MyApplication.getInstances().setUserid(loginComm.getBody().getUserid());
					MyApplication.getInstances().setAvatar(loginComm.getBody().getImgurl());
					MyApplication.getInstances().setPhone(phone);
					MyApplication.getInstances().setUserType(loginComm.getBody().getUsertype());
					MyApplication.getInstances().setKeyNum(loginComm.getBody().getKeynum());
					MyApplication.getInstances().setKeyState(loginComm.getBody().getKeystate()==1?true:false);

					OpenActivity(MainActivity.class);
					RegisterActivity.this.finish();
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg(getResources().getString(R.string.dataparsefail));
				}
			}
		});
	}
}
