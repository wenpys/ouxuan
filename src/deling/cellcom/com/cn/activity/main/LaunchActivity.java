package deling.cellcom.com.cn.activity.main;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import org.apache.http.util.LangUtils;
import net.tsz.afinal.http.HttpHandler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.Des3;
import cellcom.com.cn.util.LogMgr;
import cellcom.com.cn.util.SharepreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.umeng.socialize.utils.Log;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
import deling.cellcom.com.cn.bean.KeyinfoComm;
import deling.cellcom.com.cn.bean.SysComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.service.DownLoadManager;
import deling.cellcom.com.cn.service.MonitorService;
import deling.cellcom.com.cn.service.MonitorService2;
import deling.cellcom.com.cn.service.UpBaseDataService;
import deling.cellcom.com.cn.utils.CheckInfo;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.UpdateApkSheet;

public class LaunchActivity extends FragmentActivityBase implements OnCancelListener{
    private static final int APP_UPDATE = 5;
	private String isFirstLogin;
	private UpdateApkSheet updateApkSheet;
	private String target;
	private int progress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private long mExitTime;
	private HttpHandler<File> handler = null;
	private long begintime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
//		startService();
		
		String userid = cellcom.com.cn.util.SharepreferenceUtil.getDate(this, "userid");
		if (!userid.equals("")) {
			//ShowErrDialog(getResources().getString(R.string.lostnet));
			PreferencesUtils.putBoolean(LaunchActivity.this, "autologin", true);
//			readLocalInfo();
			OpenActivity(MainActivity.class);
			finish();
			return;
		}else if(!isConnect()){
			ShowErrDialog(getResources().getString(R.string.lostnet));
			return;
		}
		PreferencesUtils.putBoolean(LaunchActivity.this, "autologin", false);
		initView();
		initData();
	}
	
	private void readLocalInfo(){
		int userid = PreferencesUtils.getInt(this, "userid",-1);
		String avatar = PreferencesUtils.getString(this, "avatar","");
		String phone = PreferencesUtils.getString(this, "phone","");
		int usertype = PreferencesUtils.getInt(this, "usertype",0);
		int keynum = PreferencesUtils.getInt(this, "keynum",0);
		boolean keystate = PreferencesUtils.getBoolean(this, "keystate",false);
		String keylist = PreferencesUtils.getString(this, "keylist","");
		String desRsaKey = SharepreferenceUtil.getDate(LaunchActivity.this, "deskey");
		try {
			keylist = Des3.decode(keylist, desRsaKey);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		KeyinfoComm ki = gson.fromJson(keylist, KeyinfoComm.class);
		
		MyApplication.getInstances().setUserid(userid);
		MyApplication.getInstances().setAvatar(avatar);
		MyApplication.getInstances().setPhone(phone);
		MyApplication.getInstances().setUserType(usertype);
		MyApplication.getInstances().setKeyNum(keynum);
		MyApplication.getInstances().setKeyState(keystate);
		if(!keylist.equals("")&&ki.getBody()!=null)
			MyApplication.getInstances().setKeylist(ki.getBody());
	}
	
	private void initData() {
		begintime = System.currentTimeMillis();// 时间戳
		getNetData();
	}
	private void initView() {
	}
	
	public boolean isBlueSupport(){
		// 使用此检查确定BLE是否支持在设备上，然后你可以有选择性禁用BLE相关的功能
		return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
	}
	
	public boolean isConnect() { 
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理） 
	    try { 
	        ConnectivityManager connectivity = (ConnectivityManager) LaunchActivity.this
	                .getSystemService(CONNECTIVITY_SERVICE); 
	        if (connectivity != null) { 
	            // 获取网络连接管理的对象 
	            NetworkInfo info = connectivity.getActiveNetworkInfo(); 
	            if (info != null&& info.isConnected()) { 
	                // 判断当前网络是否已经连接 
	                if (info.getState() == NetworkInfo.State.CONNECTED) { 
	                    return true; 
	                } 
	            } 
	        } 
	    } catch (Exception e) { 
		// TODO: handle exception 
		    Log.v("error",e.toString()); 
		} 
        return false; 
    } 
		
	private void startService(){ 
//		startService(new Intent(LaunchActivity.this, MonitorService.class));
		startService(new Intent(LaunchActivity.this, MonitorService2.class));
    } 

	private void ShowErrDialog(String msg){
		
		AlertDialog.Builder builder = new Builder(LaunchActivity.this);
		builder.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					LaunchActivity.this.finish();
				}
			}).create().show();
	}
	
	private void ShowFailDialog(String msg){
		
		AlertDialog.Builder builder = new Builder(LaunchActivity.this);
		builder.setTitle("提示")
			.setMessage("连接服务器失败("+msg+")，请稍后重试")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					LaunchActivity.this.finish();
				}
			}).create().show();
	}
	
	/**
	 * 获取系统参数
	 */
	private void getNetData() {
		Log.e("MYTAG", "getNetData");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(LaunchActivity.this).send(
				FlowConsts.GETSYSTEMPARAM, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						ShowFailDialog(strMsg);
//						ToastUtils.show(LaunchActivity.this, strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						SysComm sysComm=null;
						try {
							sysComm= cellComAjaxResult.read(SysComm.class,
											CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ShowFailDialog("系统参数错误");
							return;
						}
						if (!FlowConsts.STATUE_1.equals(sysComm.getReturncode())) {
							ToastUtils.show(LaunchActivity.this, sysComm.getReturnmessage());
							return;
						}
						updateApkSheet = CheckInfo.checkInfo(sysComm,
								LaunchActivity.this, handler, begintime);
						
						final String downloadurl = sysComm.getBody().getDownurl();
						final String version = sysComm.getBody().getVersion();
						String minversion = sysComm.getBody().getMinversion();
						final String introduce = sysComm.getBody().getIntroduce();
						Double oldversion = Double.parseDouble(ContextUtil
								.getAppVersionName(LaunchActivity.this)[0]);
						String sharecontent = sysComm.getBody().getSharecontent();
						String shareurl = sysComm.getBody().getShareurl();
						String grant_content = sysComm.getBody().getGrant_content();
						String grant_url = sysComm.getBody().getGrant_url();
						String timer = sysComm.getBody().getTimer();
						
						PreferencesUtils.putString(LaunchActivity.this, "downloadurl", downloadurl);
						PreferencesUtils.putString(LaunchActivity.this, "minversion", minversion);
						PreferencesUtils.putString(LaunchActivity.this, "introduce", introduce);
						PreferencesUtils.putString(LaunchActivity.this, "sharecontent", sharecontent);
						PreferencesUtils.putString(LaunchActivity.this, "shareurl", shareurl);
						PreferencesUtils.putString(LaunchActivity.this, "grant_content", grant_content);
						PreferencesUtils.putString(LaunchActivity.this, "grant_url", grant_url);
						PreferencesUtils.putString(LaunchActivity.this, "timer", timer);
						
						if (!version.equals(oldversion)) {
							new Thread(){
	
								@Override
								public void run() {
								    String filename;
									try {
										runOnUiThread(new Runnable() {
	
					                        public void run() {
					                        	
					                        }
					                    });
										updateApkSheet = new UpdateApkSheet(LaunchActivity.this);
										downLoadApk(downloadurl);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									super.run();
								}
							}.start();
						}
						Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}					
				});
	}
	
	/**
	 * 检查apk有效性
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static boolean getUninatllApkInfo(Context context, String filePath) {		  
		boolean result = false;
		try {
			PackageManager pm = context.getPackageManager();
			Log.e("archiveFilePath", filePath);
			PackageInfo info = pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
			String packageName = null;
			if (info != null) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
			Log.e("getUninatllApkInfo","*****  解析未安装的 apk 出现异常 *****"+e.getMessage());
		}
		return result;
	}
	/**
	 * 静默下载apk
	 * 
	 * @param dialog_message
	 */
	public void downLoadApk(String url) {
		final DownLoadManager updateManager = new DownLoadManager();
		target = updateManager.createApkTarget(LaunchActivity.this);
		final String minversion = PreferencesUtils.getString(LaunchActivity.this, "minversion");
		final String introduce = PreferencesUtils.getString(LaunchActivity.this, "introduce");
		final Double oldversion = Double.parseDouble(ContextUtil.getAppVersionName(LaunchActivity.this)[0]);
				
		if (!TextUtils.isEmpty(target)) {			
			LogMgr.showLog("target:" + target);
			String apkName = url.substring(url.lastIndexOf("/")+1);// 接口名称
			target = target + apkName;
			PreferencesUtils.putString(LaunchActivity.this, "target", target);
			File apkfile = new File(target);
			if (apkfile.exists()) {
				if(getUninatllApkInfo(this, target)){
					new Thread(){
						@Override
						public void run() {
							Handler handler = MyApplication.getInstances().getHandler();
							while(handler == null){
								try {
									sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								handler = MyApplication.getInstances().getHandler();
							}
							Message xx = new Message();  
							xx.what = APP_UPDATE;
							xx.obj = target;
							handler.sendEmptyMessage(APP_UPDATE);
						}
					}.start();
					return;
				}else
					apkfile.delete();
			}

			updateManager.downLoadApk(LaunchActivity.this, url,
					target, true, new NetCallBack<File>() {
						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							Log.e("downLoadApk","error="+strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							progress = (int) (((float) current / count) * 100);
							// 更新进度
							Log.i("downLoadApk","loading="+progress);
						}

						@Override
						public void onSuccess(File arg0) {	
							new Thread(){
								@Override
								public void run() {
									Handler handler = MyApplication.getInstances().getHandler();
									while(handler == null){
										try {
											sleep(2000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										handler = MyApplication.getInstances().getHandler();
									}
									Message xx = new Message();  
									xx.what = APP_UPDATE;
									xx.obj = target;
									handler.sendEmptyMessage(APP_UPDATE);
								}
							}.start();
						}
					});
		}
	}
	
	/**
	 * 下载apk
	 * 
	 * @param dialog_message
	 */
	public void downLoadApk(String newVersion, UpdateApkSheet updateApkSheet) {
		this.updateApkSheet = updateApkSheet;
		final DownLoadManager updateManager = new DownLoadManager();
		String url = PreferencesUtils.getString(LaunchActivity.this,
				"downloadurl");
		target = updateManager.createApkTarget(LaunchActivity.this);

		if (TextUtils.isEmpty(target)) {
			Uri myBlogUri = null;
			if (url == null || "".equalsIgnoreCase(url)) {
				ShowMsg(getResources().getString(R.string.downloaderror));
			}
			LogMgr.showLog("down from utl=" + url);
			myBlogUri = Uri.parse(url);
			Intent returnIt = new Intent(Intent.ACTION_VIEW, myBlogUri);
			LaunchActivity.this.startActivity(returnIt);
			LaunchActivity.this.finish();
		} else {
			LogMgr.showLog("target:" + target);
			String apkName = url.substring(url.lastIndexOf("/")+1);// 接口名称
			target = target + apkName;
			File apkfile = new File(target);
			if (apkfile.exists()) {
				apkfile.delete();
			}

			String u = target;
			updateApkSheet.setData(newVersion,
					getResources().getString(R.string.downloading) + target
							+ getResources().getString(R.string.thankyou));
			updateApkSheet.setzxbbmstvVISIBLEORGONE(View.VISIBLE);
			updateApkSheet.setProgressVISIBLEORGONE(View.VISIBLE);
			handler = updateManager.downLoadApk(LaunchActivity.this, url,
					target, true, new NetCallBack<File>() {
						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							if (strMsg.contains(FlowConsts.LODER)) {
								installApk(target);
								LaunchActivity.this.updateApkSheet.dismiss();
								LaunchActivity.this.finish();
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
							installApk(target);
							LaunchActivity.this.updateApkSheet.dismiss();
							LaunchActivity.this.finish();
						}
					});
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				updateApkSheet.setProgress(progress);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.show(this, R.string.esc);
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		
	}
}
