package deling.cellcom.com.cn.activity.welcome;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.bean.YzmComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.RegExpValidator;
import deling.cellcom.com.cn.utils.SmsUtil;
import deling.cellcom.com.cn.widget.Header;

/**
 * 忘记密码
 * 
 * @author xpw
 * 
 */
public class ForgetActivity extends FragmentActivityBase {
	private ImageView ivBack;
	private EditText countet;// 账号
	private EditText verificationCode;// 验证码
	private EditText etNewPwd;// 新密码
	private Button btOK;// 下一步
	private Button cerificationCodeBtn;// 获取验证码
	private YzmComm loginComm;
	private MyCount myCount;
	
	private BroadcastReceiver counterActionReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_welcom_forget);
		MyApplication.getInstances().getActivities().add(this);
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
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}

	private void InitData() {
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
		etNewPwd = (EditText) findViewById(R.id.mmet);
		btOK = (Button) findViewById(R.id.ok);
		cerificationCodeBtn = (Button) findViewById(R.id.yzmbtn);
		ivBack = (ImageView) findViewById(R.id.leftimg);
	}

	private void InitListeners() {
		ivBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputmanger.hideSoftInputFromWindow(
						countet.getWindowToken(), 0);
				ForgetActivity.this.finish();
			}
		});
		btOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String countettxt = countet.getText().toString();
				String pwdtxt = etNewPwd.getText().toString();
				String yzmettxt = verificationCode.getText().toString();
				if (TextUtils.isEmpty(countettxt)) {
					ShowMsg(getResources().getString(R.string.qsrzh));
					countet.setFocusable(true);
					countet.requestFocus();
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
					etNewPwd.setFocusable(true);
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
				
//				updatePwd(countettxt, pwdtxt, yzmettxt);
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
//				ykysSendverifysms(countettxt);
			}
		});
	}

	// 校对验证码
	private void CheckCaptcha(final String phone, final String captcha) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", phone);
		cellComAjaxParams.put("yzm", captcha);
		HttpHelper.getInstances(ForgetActivity.this).send(FlowConsts.DL_REGISTER,
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
						loginComm = arg0.read(YzmComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = loginComm.getReturncode();
						String msg = loginComm.getReturnmessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
					}
				});

	}

	// 重置密码
	private void updatePwd(final String account, final String password, final String yzm) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("password", password);
		cellComAjaxParams.put("verifysms", yzm);
		HttpHelper.getInstances(ForgetActivity.this).send(FlowConsts.UPDATEPWD,
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
						Comm bean = arg0.read(Comm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = bean.getReturncode();
						String msg = bean.getReturnmessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							return;
						}
						PreferencesUtils.putString(ForgetActivity.this, "phone", "");
						PreferencesUtils.putString(ForgetActivity.this, "phone2", "");
						PreferencesUtils.putBoolean(ForgetActivity.this, "isrememb", false);
						
						ShowMsg("修改密码成功");
						finish();
					}
				});

	}

	private void ShowErrDialog(String msg){
		
		AlertDialog.Builder builder = new Builder(ForgetActivity.this);
		builder.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					OpenActivity(LoginActivity.class);
					setResult(ContextUtil.EXIT_APPLICATION);
					ForgetActivity.this.finish();
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					setResult(ContextUtil.EXIT_APPLICATION);
					ForgetActivity.this.finish();
				}
			}).create().show();
	}
	
	// yzm
	private void ykysSendverifysms(final String account) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", account);
		cellComAjaxParams.put("method", "1");
		HttpHelper.getInstances(ForgetActivity.this).send(
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
			cerificationCodeBtn.setText(ForgetActivity.this.getResources().getString(
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
		ForgetActivity.this.finish();
	}

}
