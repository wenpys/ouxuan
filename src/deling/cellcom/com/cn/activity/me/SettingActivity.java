package deling.cellcom.com.cn.activity.me;

import java.io.File;

import net.tsz.afinal.http.HttpHandler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.LogMgr;

import com.umeng.analytics.MobclickAgent;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.activity.welcome.ForgetActivity;
import deling.cellcom.com.cn.bean.SysComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.service.DownLoadManager;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;

/**
 * 
 * @author 设置
 * 
 */
public class SettingActivity extends FragmentActivityBase implements
		OnClickListener, OnCheckedChangeListener {
	private static final int SHARK_OPEN = 1;
	private static final int AUTO_OPEN = 2;

	private Header header;
	private RelativeLayout mRLChangePwd;
	private RelativeLayout mRLabout;
	private RelativeLayout mRLCheck;
	private CheckBox ckAuto, ckShark;
	private Button registerbtn;// 退出登录
	private TextView tvSensitivity;
	private SeekBar sbSensitivity;
	private Handler handler;
	private String target;
	private HttpHandler<File> insHandler=null;
	private int progress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private ProgressBar mProgress;
    private final String mPageName = "SettingScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_setting);
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
	@Override
	protected void onResume() {
		super.onResume();
		ckAuto.setOnCheckedChangeListener(null);
		ckShark.setOnCheckedChangeListener(null);

		boolean yykm = PreferencesUtils.getBoolean(SettingActivity.this,
				"shark_open", true);

		setCkboxState(ckAuto, !yykm);
		setCkboxState(ckShark, yykm);

		initListener();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(this);
	}

	private void initListener() {
		ckAuto.setOnCheckedChangeListener(this);
		ckShark.setOnCheckedChangeListener(this);
		sbSensitivity
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar arg0) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						tvSensitivity.setText(progress + "");
						PreferencesUtils.putInt(SettingActivity.this,
								"Sensitivity", progress);

					}
				});
	}

	private void initView() {
		header = (Header) findViewById(R.id.header);
		mRLabout = (RelativeLayout) findViewById(R.id.personal_setting_about);
		mRLCheck = (RelativeLayout) findViewById(R.id.personal_check_update);
		mRLCheck.setOnClickListener(this);
		mRLabout.setOnClickListener(this);
		mRLChangePwd = (RelativeLayout) findViewById(R.id.personal_setting_changepwd);
		mRLChangePwd.setOnClickListener(this);
		sbSensitivity = (SeekBar) findViewById(R.id.sensitivity);
		tvSensitivity = (TextView) findViewById(R.id.tv_sensitivity);
		ckAuto = (CheckBox) findViewById(R.id.ck_auto_open);
		ckShark = (CheckBox) findViewById(R.id.ck_shark_open);
		registerbtn = (Button) findViewById(R.id.registerbtn);
		registerbtn.setOnClickListener(this);

	}

	private void initData() {
		handler = MyApplication.getInstances().getHandler();
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setTitle("设置", null);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								header.getWindowToken(), 0);
						SettingActivity.this.finish();
					}
				});
		int sen = PreferencesUtils.getInt(SettingActivity.this, "Sensitivity",
				0);
		sbSensitivity.setProgress(sen);
		tvSensitivity.setText(sen + "");
	}

	private void setCkboxState(CheckBox ck, boolean state) {
		ck.setChecked(state);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_setting_about:
			OpenActivity(AboutActivity.class);
			break;
		case R.id.personal_setting_changepwd:
			Intent intent = new Intent(SettingActivity.this,
					ForgetActivity.class);
			intent.putExtra("title", "重置密码");
			startActivityForResult(intent, 302);
			break;
		case R.id.registerbtn:// 退出登录
			setResult(RESULT_OK);
			ContextUtil.exitLogin(SettingActivity.this);
			MobclickAgent.onProfileSignOff();
			//退出登录
			MyApplication.getInstances().getActivities().clear();
			finish();
			break;
		case R.id.personal_check_update:// 检查更新
			getNetData();
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Message msg = new Message();
		switch (buttonView.getId()) {
		case R.id.ck_auto_open:
			PreferencesUtils.putBoolean(SettingActivity.this, "shark_open",
					!isChecked);
			MyApplication.getInstances().setSharkOpen(!isChecked);
			ckShark.setChecked(!isChecked);
			if (isChecked)
				msg.what = AUTO_OPEN;
			break;
		case R.id.ck_shark_open:
			PreferencesUtils.putBoolean(SettingActivity.this, "shark_open",
					isChecked);
			MyApplication.getInstances().setSharkOpen(isChecked);
			ckAuto.setChecked(!isChecked);
			if (isChecked)
				msg.what = SHARK_OPEN;
			break;
		default:
			break;
		}
		if (msg.what > 0)
			handler.sendMessage(msg);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == ContextUtil.EXIT_APPLICATION){
			setResult(ContextUtil.EXIT_APPLICATION);
			finish();
		}
	}

	/**
	 * 获取系统参数
	 */
	private void getNetData() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(SettingActivity.this).send(
				FlowConsts.GETSYSTEMPARAM, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						ShowMsg(strMsg);
						// ToastUtils.show(LaunchActivity.this, strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						SysComm sysComm = null;
						try {
							sysComm = cellComAjaxResult.read(SysComm.class,
									CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ShowMsg("系统参数错误");
							return;
						}
						if (!FlowConsts.STATUE_1.equals(sysComm.getReturncode())) {
							ToastUtils.show(SettingActivity.this,
									sysComm.getReturnmessage());
							return;
						}
						Double oldversion = Double.parseDouble(ContextUtil
								.getAppVersionName(SettingActivity.this)[0]);
						String newStringVersion=sysComm.getBody().getVersion();
						final Double newVersion=Double.valueOf(newStringVersion);
						if(newVersion>oldversion){
							ShowAlertDialog("更新提示", "当前版本为："+oldversion+",最新的版本为："+newVersion+"，点击确定进行更新",new DialogInterface.OnClickListener(){
								//点击确定按钮
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									showCustomMessageOK(newVersion+"");
								}},new DialogInterface.OnClickListener(){
									//点击取消按钮
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										setResult(ContextUtil.EXIT_APPLICATION);
										finish();
									}});
						}else{
							ToastUtils.show(SettingActivity.this, "当前已为最新版本");
						}
					}

		});
	}
	/**
	 * 下载apk
	 * 
	 * @param dialog_message
	 */
	public void downLoadApk(final AlertDialog dialog,String newVersion) {
		final DownLoadManager updateManager = new DownLoadManager();
		String url = PreferencesUtils.getString(SettingActivity.this,
				"downloadurl");
		target = updateManager.createApkTarget(SettingActivity.this);

		if (TextUtils.isEmpty(target)) {
			Uri myBlogUri = null;
			if (url == null || "".equalsIgnoreCase(url)) {
				ShowMsg(getResources().getString(R.string.downloaderror));
			}
			LogMgr.showLog("down from utl=" + url);
			myBlogUri = Uri.parse(url);
			Intent returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri);
			SettingActivity.this.startActivity(returnIt);
			SettingActivity.this.finish();
		} else {
			LogMgr.showLog("target:" + target);
			String apkName = url.substring(url.lastIndexOf("/")+1);// 接口名称
			target = target + apkName;
			File apkfile = new File(target);
			if (apkfile.exists()) {
				apkfile.delete();
			}

			String u = target;
			insHandler = updateManager.downLoadApk(SettingActivity.this, url,
					target, true, new NetCallBack<File>() {
						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							dialog.dismiss();
							if (strMsg.contains(FlowConsts.LODER)) {
								installApk(target);
								SettingActivity.this.finish();
							}
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							progress = (int) (((float) current / count) * 100);
							// 更新进度
							mHandler.sendEmptyMessage(DOWN_UPDATE);
						}

						@Override
						public void onSuccess(File arg0) {
							dialog.dismiss();
							installApk(target);
							SettingActivity.this.finish();
						}
					});
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:

				break;
			default:
				break;
			}
		};
	};
	/**
	 * 安装指定地址(filePath)的apk
	 */
	private void installApk(String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		startActivity(i);
	}
	/**
	 * 弹出更新提示框
	 */
	private void showCustomMessageOK(String version) {
		AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
		builder.setTitle("更新");
		final LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
		View view = inflater.inflate(R.layout.app_welcome_download, null);
		TextView dialog_message = (TextView) view
				.findViewById(R.id.dialog_message);
		dialog_message.setText("正在更新!!!");
		mProgress = (ProgressBar) view.findViewById(R.id.progress);
		builder.setView(view);

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				insHandler.stop();
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.show();
		downLoadApk(dialog,version);
	}
}
