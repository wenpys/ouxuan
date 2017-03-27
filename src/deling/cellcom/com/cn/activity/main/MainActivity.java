package deling.cellcom.com.cn.activity.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.HttpHandler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.Des3;
import cellcom.com.cn.util.LogMgr;
import cellcom.com.cn.util.MD5;
import cellcom.com.cn.util.SharepreferenceUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ab.view.sliding.AbSlidingPlayView;
import com.dh.bluelock.imp.BlueLockPubCallBackBase;
import com.dh.bluelock.imp.BlueLockPubImp;
import com.dh.bluelock.object.LEDevice;
import com.dh.bluelock.pub.BlueLockPub;
import com.dh.bluelock.util.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import de.greenrobot.event.EventBus;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.WebViewActivity;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.activity.base.FragmentBase;
import deling.cellcom.com.cn.activity.me.AskKeyActivity;
import deling.cellcom.com.cn.activity.me.CenterFragment;
import deling.cellcom.com.cn.activity.me.PersonFm;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
import deling.cellcom.com.cn.activity.zxing.activity.CaptureActivity;
import deling.cellcom.com.cn.bean.Adver;
import deling.cellcom.com.cn.bean.AdverComm;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.bean.Keyinfo;
import deling.cellcom.com.cn.bean.KeyinfoComm;
import deling.cellcom.com.cn.bean.OpenLog;
import deling.cellcom.com.cn.bean.SysComm;
import deling.cellcom.com.cn.bean.UploadPicInfoBean;
import deling.cellcom.com.cn.db.BaseDataManager;
import deling.cellcom.com.cn.jpush.MyReceiver;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.service.DownLoadManager;
import deling.cellcom.com.cn.service.MonitorService;
import deling.cellcom.com.cn.utils.CheckInfo;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import deling.cellcom.com.cn.widget.CustomProgressDialog;
import deling.cellcom.com.cn.widget.CustomViewPager;
import deling.cellcom.com.cn.widget.Header;
import deling.cellcom.com.cn.widget.UpdateApkSheet;
import deling.cellcom.com.cn.widget.zhy.PicSelectActivity;

/**
 * 主界面
 * 
 * @author xpw
 * 
 */
public class MainActivity extends FragmentActivityBase implements  OnActionSheetSelected, OnCancelListener{
	/**
	 * Called when the activity is first created.
	 */
	private final static int CAMERA_REQUEST_CODE = 1;
	private final static int GALLERY_REQUEST_CODE = 2;
	private final static int DEVICE_LIST_REQUEST_CODE = 3;
	private String userid;
	public LayoutInflater mInflater;

	private RadioGroup rgs;
	private RadioButton tabRbCenter;// 接待中心
	private RadioButton tabRbShow;//  内容展示
	private RadioButton tabRbSale;//  互动营销
	private RadioButton tabRbPerson;// 我的信息

	private Drawable centerOn;// 接待中心选中
	private Drawable centerOff;// 接待中心未选中
	private Drawable showOn;// 内容展示选中
	private Drawable showOff;//内容展示未选中
	private Drawable saleOn;//互动营销选中
	private Drawable saleOff;// 互动营销未选中
	private Drawable personOn;// 个人信息选中
	private Drawable personOff;// 个人信息未选中
	private RelativeLayout rlMain;
	public List<FragmentBase> fragments = new ArrayList<FragmentBase>();

	private long mExitTime = 0;// 退出时间
	private long mClickTime;//点击开门时间

	private TextView tvMsg;// 消息文本
	private boolean isSharkOpen;

	private List<String> mIndexADImg = new ArrayList<String>();
	private List<String> mIndexADLink = new ArrayList<String>();
	private String[] cacheIndexADImg;
	private String[] cacheIndexADLink;

	private CenterFragment centerFm;	//接待中心
	private ShowFragment showFm;	//车型展示
	private SaleFragment saleFm;	//互动营销
	private NoticeFragment noticeFragment;// 通知
	private MainFragment mainFragment;// 主页
	private PersonFm personFm;	//我的

	private CustomViewPager vpContent;
	private AbSlidingPlayView mSlidingPlayView = null;
	private AlertDialog myDialog = null;
	
	private final static String FLAG = "flag";
	private final static String TAG = "MainActivity";

	private static final int SENSOR_SHAKE = 10;
    private static final int SHARK_OPEN = 1;
    private static final int AUTO_OPEN = 2;
    private static final int KEY_OPEN = 3;
    private static final int KEY_REFRESH = 4;
    private static final int APP_UPDATE = 5;
    private static final int KEY_CONNECT=5;
    private static final int KEY_OPEN_KEY=6;
    private static final int KEY_OPEN_AUTO=7;
	public List<Keyinfo> keylist = new ArrayList<Keyinfo>();;
//	private autoThread AutoOpenThread;
	private CustomProgressDialog m_ProgressDialog;
	private String path;
	private FinalBitmap finalBitmap;
	private int curFrm = 0;
//	private boolean isOpening = false;
    private PowerManager pm;  
    private PowerManager.WakeLock wakeLock;
    private boolean islockScreen = false;
    private boolean isLock=false;
    private UpdateApkSheet updateApkSheet;
    //钥匙map  主键为钥匙id
    Map<Keyinfo,LEDevice> mapKeyDevices=new HashMap<Keyinfo,LEDevice>();
//    private List<LEDevice> devices=new ArrayList<LEDevice>();
//    private BlueLockPubImp blueLockPub;
    //开门的类
    private BlueLockPub blueLockPub;
    //扫描的类
    private BlueLockPubImp blueLockPubScan;
	private final int MST_WHAT_START_SCAN_DEVICE = 0x01;
	private boolean isClear=true;
	private boolean isOpened=false;
	private LEDevice device;
	private Keyinfo keyinfo;
	private boolean isOnline = true;
	private boolean isMain=false;
	private boolean isShow=false;
	private Handler LeHandler;
	private String openPid;
	private int openFlag;
	public static boolean isForeground = false;
	private boolean isRunscan = false;	
	private boolean isOpenComplete = true; //开门流程是否完成;
	private String target;
	private int progress;
	
	private String matchTime;
	private String openTime;
	
	private long checkMatchTime;
	private long checkOpenTime;
	
	
	public static void start(Activity activity, String flag) {
		Intent intent = new Intent(activity, MainActivity.class);
		intent.putExtra(FLAG, flag);
		activity.startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.i("JPush","id="+JPushInterface.getRegistrationID(MainActivity.this));
		
		mInflater = LayoutInflater.from(this);
		
		int flag = getIntent().getIntExtra(FLAG, 0);
		Log.e("flag","f:"+flag+"="+getIntent().getExtras());
		if (flag == ContextUtil.EXIT_LOGIN || flag == ContextUtil.EXIT_APPLICATION) {
			finish();
		}
		if(getIntent().getExtras() != null){
			String from = getIntent().getStringExtra("from");
			if(MyApplication.getInstances().getUserid() == -1){
				Intent intent = new Intent(this,LoginActivity.class);
				intent.putExtra("from", from);
				startActivity(intent);
				myReceiver = null;
				finish();
				return;
			}else{
				if(from.equals("notice")){
					curFrm = 0;
					String maxid = BaseDataManager.getInstance(this).getMaxAreaNoticeId();
					noticeFragment.getData(maxid);
				}
				else if(from.equals("service")){
					Intent home = new Intent(Intent.ACTION_MAIN);
					home.addCategory(Intent.CATEGORY_HOME);
					startActivity(home);
				}
			}
		}
		String userid = cellcom.com.cn.util.SharepreferenceUtil.getDate(MainActivity.this,"userid");
		initView();
		initListener();
		initData();
//		addLBT();
		tabRbCenter.setChecked(true);
	}
	
	private void alertAD(){
		String isFirstLogin = PreferencesUtils.getString(MainActivity.this,"is_first_login");
		String vendor = android.os.Build.MANUFACTURER.toLowerCase();

		//广告弹窗
		if (TextUtils.isEmpty(isFirstLogin)) {
			PreferencesUtils.putString(MainActivity.this, "is_first_login", "N");
			Intent intent = new Intent(this, AdDialogActivity.class);
			intent.putExtra("vendor", vendor);
			startActivity(intent);
		}
	}
	
	private void initView() {
		rlMain = (RelativeLayout) findViewById(R.id.rl_main);
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);
		tabRbCenter = (RadioButton) findViewById(R.id.tab_rb_a);
		tabRbShow = (RadioButton) findViewById(R.id.tab_rb_b);
		tabRbSale = (RadioButton) findViewById(R.id.tab_rb_c);
		tabRbPerson = (RadioButton) findViewById(R.id.tab_rb_d);

		tvMsg = (TextView) findViewById(R.id.tv_msg);

		vpContent = (CustomViewPager) findViewById(R.id.vp_content);
	}
	@SuppressLint("NewApi")
	private void initData() {
		Log.e(TAG,"isSharkOpen="+isSharkOpen);
		
		MyApplication.getInstances().setHandler(handler);
//		AutoOpenThread = new autoThread(handler);
//		AutoOpenThread.start();			
		
		EventBus.getDefault().post(new FirstEvent("FirstEvent btn clicked"));
		// chat login
		userid = cellcom.com.cn.util.SharepreferenceUtil.getDate(
				MainActivity.this, "userid");
		
		getDrawable();

		centerFm = new CenterFragment();
		showFm = new ShowFragment();
		saleFm = new SaleFragment();
		personFm = new PersonFm();
		noticeFragment = new NoticeFragment();
		mainFragment = new MainFragment();
		fragments.add(centerFm);
		fragments.add(showFm);
		fragments.add(saleFm);
		fragments.add(personFm);
		
		vpContent.setAdapter(new deling.cellcom.com.cn.adapter.MainAdapter(getSupportFragmentManager(),
				fragments));
		vpContent.setOffscreenPageLimit(3);

		vpContent.setCurrentItem(curFrm, true);
	}
	
	private void initListener() {
		rgs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_rb_a:// 接待中心
					isMain=false;
					vpContent.setCurrentItem(0, false);
					
					setTabUI(0);					
					break;
				case R.id.tab_rb_b:// 内容展示
					isMain=true;
					vpContent.setCurrentItem(1, false);
					
					setTabUI(1);
					break;
				case R.id.tab_rb_c:// 互动营销
					isMain=false;
//					mSlidingPlayView.setVisibility(View.GONE);
					vpContent.setCurrentItem(2, false);
					setTabUI(2);
					break;
				case R.id.tab_rb_d:// 我的信息
					isMain=false;
//					mSlidingPlayView.setVisibility(View.GONE);
					vpContent.setCurrentItem(3, false);
					setTabUI(3);
					break;

				default:
					break;
				}

			}

		});
	}
	/**
	 * 初始化图片资源
	 */
	private void getDrawable() {
		centerOn = getResources().getDrawable(R.drawable.tab_ic_jiedai_pre);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		centerOn.setBounds(0, 0, centerOn.getMinimumWidth(), centerOn.getMinimumHeight());

		centerOff = getResources().getDrawable(R.drawable.tab_ic_jiedai_def);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		centerOff.setBounds(0, 0, centerOff.getMinimumWidth(),
				centerOff.getMinimumHeight());

		showOn = getResources().getDrawable(R.drawable.tab_ic_hudong_pre);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		showOn.setBounds(0, 0, 		showOn.getMinimumWidth(),
				showOn.getMinimumHeight());

		showOff = getResources().getDrawable(R.drawable.tab_ic_hudong_def);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		showOff.setBounds(0, 0, showOff.getMinimumWidth(),
				showOff.getMinimumHeight());

		saleOn = getResources().getDrawable(R.drawable.tab_ic_car_pre);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		saleOn.setBounds(0, 0, saleOn.getMinimumWidth(),
				saleOn.getMinimumHeight());

		saleOff = getResources().getDrawable(R.drawable.tab_ic_car_def);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		saleOff.setBounds(0, 0, saleOff.getMinimumWidth(),
				saleOff.getMinimumHeight());

		personOn = getResources().getDrawable(R.drawable.tab_ic_wo_pre);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		personOn.setBounds(0, 0, personOn.getMinimumWidth(),
				personOn.getMinimumHeight());

		personOff = getResources().getDrawable(R.drawable.tab_ic_wo_def);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		personOff.setBounds(0, 0, personOff.getMinimumWidth(),
				personOff.getMinimumHeight());
	}
	
	/**
	 * 设置选中状态
	 * 
	 * @param index
	 *            下标
	 */
	private void setTabUI(int index) {
		tabRbCenter.setTextColor(getResources().getColor(R.color.darkgrey));
		tabRbShow.setTextColor(getResources().getColor(R.color.darkgrey));
		tabRbSale.setTextColor(getResources().getColor(R.color.darkgrey));
		tabRbPerson.setTextColor(getResources().getColor(R.color.darkgrey));

		tabRbCenter.setCompoundDrawables(null, centerOff, null, null); // 设置左图标
		tabRbShow.setCompoundDrawables(null, showOff, null, null); // 设置左图标
		tabRbSale.setCompoundDrawables(null, saleOff, null, null); // 设置左图标
		tabRbPerson.setCompoundDrawables(null, personOff, null, null); // 设置左图标

		switch (index) {
		case 0:
			tabRbCenter.setCompoundDrawables(null, centerOn, null, null);
			tabRbCenter.setTextColor(getResources().getColor(R.color.darkblue));
			break;
		case 1:
			tabRbShow.setCompoundDrawables(null, showOn, null, null);
			tabRbShow.setTextColor(getResources().getColor(R.color.darkblue));
			break;
		case 2:
			tabRbSale.setCompoundDrawables(null, saleOn, null, null);
			tabRbSale.setTextColor(getResources().getColor(R.color.darkblue));
			break;
		case 3:
			tabRbPerson.setCompoundDrawables(null, personOn, null, null);
			tabRbPerson.setTextColor(getResources().getColor(R.color.darkblue));
			break;

		default:
			break;
		}
	}
	/**
	 * 获取系统参数
	 */
	private void getNetData() {
		Log.e("MYTAG", "getNetData");
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(MainActivity.this).send(
				FlowConsts.GETSYSTEMPARAM, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						SysComm sysComm=null;
						try {
							sysComm= cellComAjaxResult.read(SysComm.class,
											CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						if (!FlowConsts.STATUE_1.equals(sysComm.getReturncode())) {
							ToastUtils.show(MainActivity.this, sysComm.getReturnmessage());
							return;
						}
						updateApkSheet = CheckInfo.checkInfo(sysComm,
								MainActivity.this, null, System.currentTimeMillis());
						
						final String downloadurl = sysComm.getBody().getDownurl();
						final String version = sysComm.getBody().getVersion();
						String minversion = sysComm.getBody().getMinversion();
						final String introduce = sysComm.getBody().getIntroduce();
						Double oldversion = Double.parseDouble(ContextUtil
								.getAppVersionName(MainActivity.this)[0]);
						String sharecontent = sysComm.getBody().getSharecontent();
						String shareurl = sysComm.getBody().getShareurl();
						String grant_content = sysComm.getBody().getGrant_content();
						String grant_url = sysComm.getBody().getGrant_url();
						String timer = sysComm.getBody().getTimer();
						
						PreferencesUtils.putString(MainActivity.this, "downloadurl", downloadurl);
						PreferencesUtils.putString(MainActivity.this, "minversion", minversion);
						PreferencesUtils.putString(MainActivity.this, "introduce", introduce);
						PreferencesUtils.putString(MainActivity.this, "sharecontent", sharecontent);
						PreferencesUtils.putString(MainActivity.this, "shareurl", shareurl);
						PreferencesUtils.putString(MainActivity.this, "grant_content", grant_content);
						PreferencesUtils.putString(MainActivity.this, "grant_url", grant_url);
						PreferencesUtils.putString(MainActivity.this, "timer", timer);
						
						if (Double.parseDouble(version) > oldversion) {
							new Thread(){
	
								@Override
								public void run() {
								    String filename;
									try {
										runOnUiThread(new Runnable() {
	
					                        public void run() {
					                        	
					                        }
					                    });
										updateApkSheet = new UpdateApkSheet(MainActivity.this);
										downLoadApk(downloadurl);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									super.run();
								}
							}.start();
						}
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
		target = updateManager.createApkTarget(MainActivity.this);
		final String minversion = PreferencesUtils.getString(MainActivity.this, "minversion");
		final String introduce = PreferencesUtils.getString(MainActivity.this, "introduce");
		final Double oldversion = Double.parseDouble(ContextUtil.getAppVersionName(MainActivity.this)[0]);
				
		if (!TextUtils.isEmpty(target)) {			
			LogMgr.showLog("target:" + target);
			String apkName = url.substring(url.lastIndexOf("/")+1);// 接口名称
			target = target + apkName;
			PreferencesUtils.putString(MainActivity.this, "target", target);
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

			updateManager.downLoadApk(MainActivity.this, url,
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
	 * 安装指定地址(filePath)的apk
	 */
	private void installApk(String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		startActivity(i);
	}
	
	
	private void appUpdate(){
		final String target = PreferencesUtils.getString(MainActivity.this, "target", "");
		final String minversion = PreferencesUtils.getString(MainActivity.this, "minversion");
		final String introduce = PreferencesUtils.getString(MainActivity.this, "introduce", "");
		final Double oldversion = Double.parseDouble(ContextUtil.getAppVersionName(MainActivity.this)[0]);
		Log.e("target","target:"+target);
		new AlertDialog.Builder(this).setTitle("更新提示")
			.setMessage(introduce)
			.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					installApk(target);
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if (Double.parseDouble(minversion) > oldversion) {
						setResult(ContextUtil.EXIT_APPLICATION);
						finish();
					}
				}
			})
			.show();
	}

	//添加头部轮播图
	private void addLBT(){

		if(mSlidingPlayView != null){
			rlMain.removeView(mSlidingPlayView);
			mSlidingPlayView.removeAllViews();
			mSlidingPlayView.stopPlay();
		}else
	        mSlidingPlayView = new AbSlidingPlayView(this);
		
        
        if(mIndexADImg != null && mIndexADImg.size() > 0){
        	for(int i=0;i<mIndexADImg.size();i++){
			    final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
				ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
				TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
				mPlayText.setText("");
				Picasso.with(MainActivity.this).load(mIndexADImg.get(i)).placeholder(R.drawable.advert_02).into(mPlayImage);
				mSlidingPlayView.addView(mPlayView);
        	}
        }else{
        	if(cacheIndexADImg !=null && cacheIndexADImg.length>0 && !cacheIndexADImg[0].equals("")){
        		for(int i=0;i<cacheIndexADImg.length;i++){
    			    final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
    				ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
    				TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
    				mPlayText.setText("");
    				Picasso.with(MainActivity.this).load(cacheIndexADImg[i]).placeholder(R.drawable.advert_02).into(mPlayImage);
    				mSlidingPlayView.addView(mPlayView);
            	}
        	}else{
				final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
				ImageView mPlayImage1 = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
				TextView mPlayText1 = (TextView) mPlayView.findViewById(R.id.mPlayText);
				mPlayText1.setText("");
				mPlayImage1.setBackgroundResource(R.drawable.advert_02);
				mSlidingPlayView.addView(mPlayView);
        	}
        }

		mSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
		mSlidingPlayView.startPlay();
		//设置高度
		mSlidingPlayView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (ContextUtil.getWidth(MainActivity.this)/2.16)));
		rlMain.addView(mSlidingPlayView);
		mSlidingPlayView.setVisibility(View.GONE);
		//解决冲突问题
//		mSlidingPlayView.setParentListView(mListView);
		mSlidingPlayView.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {
			
			@Override
			public void onClick(int position) {
				if(mIndexADLink.size() > 0){
					Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "");
					bundle.putString("url", mIndexADLink.get(position));
					intent.putExtras(bundle);
					if(!TextUtils.isEmpty(mIndexADLink.get(position))){
						startActivity(intent);
					}
				}else if(cacheIndexADLink.length>0 && !cacheIndexADLink[0].equals("")){
					Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", "");
					bundle.putString("url", cacheIndexADLink[position]);
					intent.putExtras(bundle);
					if(!TextUtils.isEmpty(cacheIndexADLink[position])){
						startActivity(intent);
					}
					startActivity(intent);
				}
			}
		});
	    
        mSlidingPlayView.setOnPageChangeListener(new AbSlidingPlayView.AbOnChangeListener() {
			
			@Override
			public void onChange(int position) {
//				AbToastUtil.showToast(MainActivity.this,"改变"+position);
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				ToastUtils.show(MainActivity.this, getResources().getString(R.string.esc));
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		super.onStart();
        Log.e("onStart","from="+getIntent().getExtras());

		String isFirstLogin = PreferencesUtils.getString(MainActivity.this,
				"is_first_main");
		if (TextUtils.isEmpty(isFirstLogin)
				|| !FlowConsts.STRING_Y.equals(isFirstLogin)) {// 首次使用

			PreferencesUtils.putString(MainActivity.this, "is_first_main",
					FlowConsts.STRING_Y);
			
		}

		int flag = getIntent().getIntExtra(FLAG, 0);
		Log.e("onStart","flag:"+flag);
		if (flag == ContextUtil.EXIT_LOGIN) {
			finish();
		} else if (flag == ContextUtil.EXIT_APPLICATION) {
			finish();
		}

	}
	//TODO
    Handler handler = new Handler() {  
  
        @Override  
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            Log.i("handler","msg="+msg.what);
            switch (msg.what) {  
            case SENSOR_SHAKE: 
                Log.i("shark", "检测到摇晃，执行操作！"+isLock);
                break;
            case SHARK_OPEN:
            	break;
            case AUTO_OPEN:
            	break;
            case KEY_OPEN:
            	break;
            case KEY_REFRESH:
            	break;
            case APP_UPDATE:
            	appUpdate();
            	break;
            case KEY_OPEN_KEY://点击钥匙开门
            	break;
            case KEY_OPEN_AUTO://摇一摇开门
            	break;
            }
        }  
    };  
    
	private void saveBitmap(Bitmap bm) {
		File tmpDir = new File(Environment.getExternalStorageDirectory()
				+ FlowConsts.IMG_PATH);
		if (!tmpDir.exists()) {
			tmpDir.mkdir();
		}
		File img = new File(tmpDir.getAbsolutePath() + FlowConsts.IMG_FILE);
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			uploadpic(img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void uploadpic(final File img) {
		Log.e("img","img="+img.getAbsolutePath());
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		try {
			cellComAjaxParams.put("file", img);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HttpHelper.getInstances(this).send(FlowConsts.UPLOADIMG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						super.onStart();						
						ShowProgressDialog(R.string.app_picloading);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						personFm.cImg.setImageBitmap(BitmapFactory.decodeFile(img
								.getPath()));
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						DismissProgressDialog();
						UploadPicInfoBean pafkComm = arg0.read(
								UploadPicInfoBean.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equalsIgnoreCase(pafkComm
								.getReturncode())) {
							return;
						}
						Log.e("img","img="+personFm.cImg);

						Picasso.with(MainActivity.this).load("file://"+img.getAbsolutePath()).placeholder(R.drawable.avatar_default)
							.into(personFm.cImg);

						commit(pafkComm.getBody().getImageid()+"");
					}
				});
	}

	//获取首页轮播广告
	private void getIndexAD() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("adpos", "1");

		HttpHelper.getInstances(MainActivity.this).send(
				FlowConsts.CHECKAD, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						Log.e("getIndexAD","fail = "+strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						try{
							AdverComm bean = arg0.read(AdverComm.class,
									CellComAjaxResult.ParseType.GSON);
							String state = bean.getReturncode();
							String msg = bean.getReturnmessage();
	
							if (!FlowConsts.STATUE_1.equals(state)) {
								if (FlowConsts.STATUE_2.equals(state)) {
									// token失效
									ContextUtil.exitLogin(
											MainActivity.this, rlMain);
								}
								ShowMsg(msg);
								return;
							}
							List<Adver> advers = bean.getBody();
							
							
							StringBuffer indexadurl = new StringBuffer();
							StringBuffer indexadlink = new StringBuffer();
							if(advers.size()>0){
								mIndexADImg.clear();
								mIndexADLink.clear();
							}
							for(int i=0;i<advers.size();i++){
								mIndexADImg.add(advers.get(i).getGgimgeurl());
								mIndexADLink.add(advers.get(i).getLinkurl());
								indexadurl.append(advers.get(i).getGgimgeurl()).append(",");
								indexadlink.append(advers.get(i).getLinkurl()).append(",");
							}
							if(indexadurl.length()>0)
								indexadurl.deleteCharAt(indexadurl.length()-1);
							if(indexadlink.length()>0)
								indexadlink.deleteCharAt(indexadlink.length()-1);
							
							PreferencesUtils.putString(MainActivity.this,
									"indexadurl", indexadurl.toString());
							PreferencesUtils.putString(MainActivity.this,
									"indexadlink",  indexadlink.toString());
							if(mIndexADImg.size()>0)
								addLBT();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
	}

	private BroadcastReceiver myReceiver = new BroadcastReceiver() {  
        
        @Override  
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub  
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) ) {//当按下电源键，屏幕亮起的时候  
            	isLock = false;
            }  
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) ) {//当按下电源键，屏幕变黑的时候  
                islockScreen = true;
                isLock = true;
                OpenActivity(KeepLiveActivity.class);
            }  
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) ) {//当解除锁屏的时候  
                islockScreen = false;
    			KeepLiveActivity activity = MyApplication.getInstances().getKeepLiveActivity();
    			if(activity!=null)
    				activity.finish();
            }
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){//网络变化
            	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                	isOnline = false;
                }else {
                	if(!isOnline){
                		Log.e("myReceiver","have net work");
                		//离线连接网络，上传开门日志
                	}
                	isOnline = true;
                }
            }
        }  
    };
	// 获取钥匙信息
	public void getKeyInfo() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(MainActivity.this).send(FlowConsts.KEYINFO,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				try{
					mainFragment.refresh();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	//修改用户头像
	private void commit(String imageid) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("imageid", imageid);

		HttpHelper.getInstances(MainActivity.this).send(
				FlowConsts.HEADIMG, cellComAjaxParams,
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
						Comm bean = arg0.read(Comm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = bean.getReturncode();

						if (!FlowConsts.STATUE_1.equals(state)) {
							if (FlowConsts.STATUE_2.equals(state)) {
								// token失效
								ContextUtil.exitLogin(
										MainActivity.this, rlMain);
							}
						}
					}
				});
	}

	private Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩   
    	    Log.i("", "压缩中文件大小:" + baos.toByteArray().length / 1024);      
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	
	private Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 1440f;//这里设置高度为800f  
        float ww = 1080f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
//	    Log.i("", "按比例缩小后宽度--" + width);
//	    Log.i("", "按比例缩小后高度--" + height);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }
	
	//将压缩的bitmap保存到sdcard卡临时文件夹img_interim，用于上传
	public File saveMyBitmap(String filename, Bitmap bit) {  
	    File dir = new File("/sdcard/cache/");
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }
	    File f = new File("/sdcard/cache/" + filename);
	    if(!f.exists()){
		    try {
		        f.createNewFile();
		        FileOutputStream fOut = null;
		        fOut = new FileOutputStream(f);  
		        bit.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		        fOut.flush();  
		        fOut.close();  
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        f = null;
		        e1.printStackTrace();
		    }  
	    }
	    
	    return f;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Ignore failed requests
		Log.i("activityres","rq:"+requestCode+";rs:"+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
		if(resultCode == ContextUtil.EXIT_APPLICATION){
			finish();
		}
		if(requestCode == 201 && resultCode == RESULT_OK){			
			finish();
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case DEVICE_LIST_REQUEST_CODE:
				device = data.getExtras().getParcelable(
						Constants.EXTRA_LEDEVICE_OBJ);
				//TODO
				break;
			case GALLERY_REQUEST_CODE:
				if (data.getSerializableExtra("value") != null) {
					ArrayList<String> mSelectedImage = (ArrayList<String>) data
							.getSerializableExtra("value");
					for (int i = 0; i < mSelectedImage.size(); i++) {
						uploadpic(new File(mSelectedImage.get(i)));
					}
				}
				break;
			case CAMERA_REQUEST_CODE:
				new Thread(){

					@Override
					public void run() {
					    String filename;
						try {
							runOnUiThread(new Runnable() {

		                        public void run() {

									Toast.makeText(MainActivity.this,"图片正在处理，请稍后...",1).show();
		                        }
		                    });
						    Bitmap bit = getimage(Cpic);
							filename = MD5.MD5Encode(Cpic+System.currentTimeMillis())+".jpg";
						    File file = saveMyBitmap(filename, bit);
						    Log.i("","file:"+file);
							uploadpic(file);
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						super.run();
					}
				}.start();
				break;
			}
		}
	}
	
	@Override  
    protected void onResume() {  
        super.onResume();
        isLock=false;
		isForeground = true;
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isForeground = false;
	}

	@Override
	public void finish() {
		if(mSlidingPlayView != null)
			mSlidingPlayView.stopPlay();
		super.finish();
	}

	@Override
	protected void onDestroy() {
//		if(myReceiver != null)
//			unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	private File img;
	private String Cpic;
	
	@Override
	public void onClick(int whichButton) {
		String sharecontent = PreferencesUtils.getString(MainActivity.this, "sharecontent", "");
		String shareurl = PreferencesUtils.getString(MainActivity.this, "shareurl", "");
		switch (whichButton) {
		case 1:
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				String path = Environment.getExternalStorageDirectory()
						.toString() + FlowConsts.IMG_PATH;
				File path1 = new File(path);
				if (!path1.exists()) {
					path1.mkdirs();
				}
				Cpic = path+FlowConsts.IMG_FILE;
				
				img = new File(Cpic);
				Uri mOutPutFileUri = Uri.fromFile(img);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);

				startActivityForResult(intent, CAMERA_REQUEST_CODE);
			} else {
				ToastUtils.show(MainActivity.this, getResources().getString(R.string.nocard));
			}
			break;
		case 2:
			Intent intent = new Intent(getApplicationContext(),
					PicSelectActivity.class);
			intent.putExtra("maxvalue", 1);
			startActivityForResult(intent, GALLERY_REQUEST_CODE);
			break;
		case 7:
			break;
		case 8:
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
