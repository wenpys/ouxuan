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
import deling.cellcom.com.cn.activity.me.PersonFm;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
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
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.bluetooth.MDAction;
//import com.hzblzx.miaodou.sdk.core.bluetooth.MDActionListener;
//import com.hzblzx.miaodou.sdk.core.model.BigSurprise;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.bluetooth.MDAction;
//import com.hzblzx.miaodou.sdk.core.bluetooth.MDActionListener;
//import com.hzblzx.miaodou.sdk.core.model.BigSurprise;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

/**
 * 主界面
 * 
 * @author xpw
 * 
 */
public class MainActivity extends FragmentActivityBase implements  OnActionSheetSelected, OnCancelListener, SensorEventListener{
	/**
	 * Called when the activity is first created.
	 */
	private final static int CAMERA_REQUEST_CODE = 1;
	private final static int GALLERY_REQUEST_CODE = 2;
	private final int DEVICE_LIST_REQUEST_CODE = 12345;
	private final int STOP_SCANERDEVICE=12323;
	private final int INITBLUE=12232;
	private final int SCANDEVICE=0x2312;
	private String userid;
	public LayoutInflater mInflater;

	private RadioGroup rgs;
	private RadioButton tabRbMsg;// 小区通知
	private RadioButton tabRbKey;// 钥匙
	private RadioButton tabRbPerson;// 我的
	private RadioButton tabRbCenter;// 排班
	private Drawable msgOn;// 消息选中
	private Drawable msgOff;// 消息未选中
	private Drawable openDoorOn;//开门选中
	private Drawable openDoorOff;// 开门未选中
	private Drawable personOn;// 个人选中
	private Drawable personOff;// 个人未选中
	private Drawable keyOn;// 开门选中
	private Drawable keyOff;// 开门未选中
	private RelativeLayout rlMain;
	public List<FragmentBase> fragments = new ArrayList<FragmentBase>();

	private Header header;// 标题
	private long mExitTime;// 退出时间
	private long mClickTime;//点击开门时间

	private TextView tvMsg;// 消息文本
	private boolean isSharkOpen;

	private List<String> mIndexADImg = new ArrayList<String>();
	private List<String> mIndexADLink = new ArrayList<String>();
	private String[] cacheIndexADImg;
	private String[] cacheIndexADLink;
	 /**
     * 检测的时间间隔
     */
    static final int UPDATE_INTERVAL = 100;
    /**
     * 上一次检测的时间
     */
    long mLastUpdateTime;
    /**
     * 上一次检测时，加速度在x、y、z方向上的分量，用于和当前加速度比较求差。
     */
    float mLastX, mLastY, mLastZ;

    /**
     * 摇晃检测阈值，决定了对摇晃的敏感程度，越小越敏感。
     */
    public int shakeThreshold = 3100;
    
	@SuppressWarnings("unused")
	// The value of the field MainActivity.chatService is not used

	public int getAddPaiBan() {
		return FlowConsts.ADDPAIBAN;
	}
	
	private NoticeFragment noticeFragment;// 通知
	private MainFragment mainFragment;// 主页
	private PersonFm personFm;	//我的

	private CustomViewPager vpContent;
	private AbSlidingPlayView mSlidingPlayView = null;
	private AlertDialog myDialog = null;
	
	private final static String FLAG = "flag";
	private final static String TAG = "MainActivity";

    private SensorManager sensorManager;  
    private Vibrator vibrator;   
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
	private int curFrm = 1;
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
	private LockCallBack lockCallback;
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
	
	//通知消息
	private NotificationManager manager;
	
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
		//分享微信KEY
		PlatformConfig.setWeixin("wx78b612b54ee7de4e", "d61129b3fb369973ebf68a6ba7234b58");
		String userid = cellcom.com.cn.util.SharepreferenceUtil.getDate(MainActivity.this,"userid");
		JPushInterface.setAlias(MainActivity.this, userid, tagCallback);
		initView();
		initListener();
		initData();
//		addLBT();
		tabRbPerson.setChecked(true);
		
		registerMessageReceiver();
		
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}
	
	private TagAliasCallback tagCallback = new TagAliasCallback() {
		
		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			System.out.println("aligscode:"+arg0);
		}
	};
	
	
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
		blueLockPub = BlueLockPub.bleLockInit(this);
		blueLockPubScan=BlueLockPub.bleLockInit(this.getApplicationContext());
		int result = blueLockPub.bleInit(this);
		lockCallback = new LockCallBack();
        blueLockPub.addResultCallBack(lockCallback);
        blueLockPubScan.addResultCallBack(lockCallback);
        
		rlMain = (RelativeLayout) findViewById(R.id.rl_main);
		header = (Header) findViewById(R.id.header);
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);
		tabRbMsg = (RadioButton) findViewById(R.id.tab_rb_a);
		tabRbPerson = (RadioButton) findViewById(R.id.tab_rb_e);
		tabRbKey = (RadioButton) findViewById(R.id.tab_rb_center);
		tabRbCenter = (RadioButton) findViewById(R.id.tab_rb_c);

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
		
		header.setBackgroundResource(R.drawable.main_bg_top);
		header.setTitle(getResources().getString(R.string.app_name));
		
		getDrawable();
		manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		personFm = new PersonFm();
		noticeFragment = new NoticeFragment();
		mainFragment = new MainFragment();
		fragments.add(noticeFragment);
		fragments.add(mainFragment);
		fragments.add(personFm);
		
		vpContent.setAdapter(new deling.cellcom.com.cn.adapter.MainAdapter(getSupportFragmentManager(),
				fragments));
		vpContent.setOffscreenPageLimit(3);

		vpContent.setCurrentItem(curFrm, true);
	}
	
	private void initListener() {
		tabRbCenter
				.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							header.setTitle(
									getResources().getString(R.string.app_name),
									null);
							setTabUI(1);
							tabRbKey.setChecked(true);
						}
					}
				});
		rgs.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_rb_a:// 通知
					isMain=false;
//					mSlidingPlayView.setVisibility(View.GONE);
//					mSlidingPlayView.setVisibility(View.GONE);
					header.setVisibility(View.VISIBLE);
					vpContent.setCurrentItem(0, false);
					header.setTitle(
							getResources().getString(R.string.main_msg), null);
					
					setTabUI(0);
					tabRbCenter.setChecked(false);
					
					break;
				case R.id.tab_rb_c:// 钥匙
					isMain=true;
//					mSlidingPlayView.setVisibility(View.GONE);
					header.setVisibility(View.VISIBLE);
					vpContent.setCurrentItem(1, false);
					setTabUI(1);
					break;
				case R.id.tab_rb_e:// 个人
					isMain=false;
//					mSlidingPlayView.setVisibility(View.GONE);
					header.setVisibility(View.GONE);
					vpContent.setCurrentItem(2, false);
					header.setTitle(getResources().getString(R.string.main_gr),
							null);
					setTabUI(2);
					tabRbCenter.setChecked(false);
					break;

				default:
					break;
				}

			}

		});
		registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));  
        registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));  
        registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT)); 
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); 
	}
	//获取消息frag的对象
	public NoticeFragment getNoticeFrag(){
		return noticeFragment;
	}
	public static boolean isBluetoothEnabled(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null){
        	if(!bluetoothAdapter.isEnabled())
        		return turnOnBluetooth();
        	else
        		return true;
        }
		return turnOnBluetooth();
	}
	
	public static boolean turnOnBluetooth(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.enable();
		}
		return false;
	}

	public static boolean turnOffBluetooth(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter != null)
		{
			return bluetoothAdapter.disable();
		}
		return false;
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
	
	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "cellcom.com.cn.deling.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);

		IntentFilter stateChangeFilter = new IntentFilter(
	            BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(myReceiver, stateChangeFilter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
              String messge = intent.getStringExtra(KEY_MESSAGE);
              String extras = intent.getStringExtra(KEY_EXTRAS);
              String from  = intent.getStringExtra("from");
              StringBuilder showMsg = new StringBuilder();
              showMsg.append(KEY_MESSAGE + " : " + messge + "\n" + "from :"+from);
              Log.e("rec",showMsg.toString());
              if(from != null && from.equals("notice")){
				curFrm = 0;
				String maxid = BaseDataManager.getInstance(MainActivity.this).getMaxAreaNoticeId();
				noticeFragment.getData(maxid);
				tabRbMsg.setChecked(true);
				vpContent.setCurrentItem(curFrm);
              }
			}
		}
	}
	
	public void ManOpendoor(final int type){
        if(isOpenComplete)
        	openFlag = type;
        isOpenComplete = false;
		checkMatchTime=System.currentTimeMillis();
		if(MonitorService.isBackground(MainActivity.this) && !isRunscan){
			Log.e("ManOpendoor", "isRunscan="+isRunscan);
			blueLockPubScan.scanDevice(3000);
			Message message = new Message();
			message.what= STOP_SCANERDEVICE;
			handler.sendMessageDelayed(message,3000);			
			
			handler.postDelayed(new Runnable() {  
	            public void run() {
	    			mateDevice(openFlag);
	            }
	        }, 3000);
		}else{
			mateDevice(openFlag);
		}
	}

	public void openDoorByKey(Keyinfo info){
		if ((System.currentTimeMillis() - mClickTime) <2000) {
//			ToastUtils.show(MainActivity.this, "亲，正在为您开门呢，请不要操作太频繁哦~~");
			return;
		}
		mClickTime = System.currentTimeMillis();
		isConnectForBlue();
		if(info != null && !islockScreen){
			//TODO
//			MobclickAgent.onEvent(MainActivity.this, "opendoor");
			openFlag = 2;
			keyinfo=info;
			if(mapKeyDevices.get(info)==null){
				ToastUtils.show(MainActivity.this, "亲，您离我太远了，请不要离我超过3米！");
				isClear=true;
				blueLockPubScan.scanDevice(3000);
				Message message=new Message();
				message.what=STOP_SCANERDEVICE;
				handler.sendMessageDelayed(message, 3000);
			}else{
				isOpened=true;
				checkOpenTime=System.currentTimeMillis();
				device=mapKeyDevices.get(info);
				blueLockPub.oneKeyOpenDevice(device, info.getPid(), info.getLock_id());
			}
		}
	}
	/**
	 * 自动开门线程
	 * @author Administrator
	 *
	 */
//	private class autoThread extends Thread{
//	         
//        private Handler handler;
//        private boolean isRun = true;
//        
//        public autoThread(Handler handler) { 
//            this.handler = handler;
//        } 
//        
//        @Override 
//        public void run() {
//    		if(MyApplication.getInstances().getKeyNum() == 0)
//    			return;
//        	while(!isSharkOpen && isRun){
//        		MiaodouKeyAgent.scanDevices();
//        		Log.i("autoThread","scan");
//        		try {
//					sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        	}
//        }
//        
//        public void isStop(boolean state){
//        	this.isRun = !state;
//        }
//	}
	/**
	 * 设置选中状态
	 * 
	 * @param index
	 *            下标
	 */
	private void setTabUI(int index) {
		tabRbMsg.setTextColor(getResources().getColor(R.color.white));
		tabRbPerson.setTextColor(getResources().getColor(R.color.white));
		tabRbCenter.setTextColor(getResources().getColor(R.color.white));
		
		tabRbMsg.setCompoundDrawables(null, msgOff, null, null); // 设置左图标
		tabRbPerson.setCompoundDrawables(null, personOff, null, null); // 设置左图标
		tabRbCenter.setCompoundDrawables(null, keyOff, null, null); // 设置左图标

		switch (index) {
		case 0:
			tabRbMsg.setCompoundDrawables(null, msgOn, null, null);
			tabRbMsg.setTextColor(getResources().getColor(R.color.orange_dark));
			break;
		case 1:
			tabRbCenter.setCompoundDrawables(null, keyOn, null, null);
			tabRbCenter.setTextColor(getResources().getColor(R.color.orange_dark));
			break;
		case 2:
			tabRbPerson.setCompoundDrawables(null, personOn, null, null);
			tabRbPerson.setTextColor(getResources().getColor(R.color.orange_dark));
			break;

		default:
			break;
		}
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
	/**
	 * 初始化图片资源
	 */
	private void getDrawable() {
		msgOn = getResources().getDrawable(R.drawable.bugle2);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		msgOn.setBounds(0, 0, msgOn.getMinimumWidth(), msgOn.getMinimumHeight());

		msgOff = getResources().getDrawable(R.drawable.bugle);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		msgOff.setBounds(0, 0, msgOff.getMinimumWidth(),
				msgOff.getMinimumHeight());

		openDoorOn = getResources().getDrawable(R.drawable.key2);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		openDoorOn.setBounds(0, 0, openDoorOn.getMinimumWidth(),
				openDoorOn.getMinimumHeight());

		openDoorOff = getResources().getDrawable(R.drawable.key);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		openDoorOff.setBounds(0, 0, openDoorOff.getMinimumWidth(),
				openDoorOff.getMinimumHeight());

		keyOn = getResources().getDrawable(R.drawable.opendoor2);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		keyOn.setBounds(0, 0, keyOn.getMinimumWidth(),
				keyOn.getMinimumHeight());

		keyOff = getResources().getDrawable(R.drawable.opendoor);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		keyOff.setBounds(0, 0, keyOff.getMinimumWidth(),
				keyOff.getMinimumHeight());

		personOn = getResources().getDrawable(R.drawable.ren2);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		personOn.setBounds(0, 0, personOn.getMinimumWidth(),
				personOn.getMinimumHeight());

		personOff = getResources().getDrawable(R.drawable.ren);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		personOff.setBounds(0, 0, personOff.getMinimumWidth(),
				personOff.getMinimumHeight());
	}
	
	public void setNoticeMsg(int count){
		if(count > 0){
			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setText(count+"");
		}
		else
			tvMsg.setVisibility(View.GONE);			
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/**
			 * 二期修改
			 */
			
//			if ((System.currentTimeMillis() - mExitTime) > 2000) {
//				ToastUtils.show(MainActivity.this, getResources().getString(R.string.esc));
//				mExitTime = System.currentTimeMillis();
//			} else {
//				finish();
//			}
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
//		pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
//        //保持cpu一直运行，不管屏幕是否黑屏  
//        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");  
//        wakeLock.acquire();  

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

    /** 
     * 重力感应监听 
     */  
    private SensorEventListener sensorEventListener = new SensorEventListener() {  
  
        @Override  
        public void onSensorChanged(SensorEvent event) {  
           /* // 传感器信息改变时执行该方法  
            float[] values = event.values;  
            float x = values[0]; // x轴方向的重力加速度，向右为正  
            float y = values[1]; // y轴方向的重力加速度，向前为正  
            float z = values[2]; // z轴方向的重力加速度，向上为正   
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。  
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了  
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {  
//                vibrator.vibrate(200);  //震动
                Message msg = new Message();  
                msg.what = SENSOR_SHAKE;  
                handler.sendMessage(msg);
            }  */
        	 long currentTime = System.currentTimeMillis();
             long diffTime = currentTime - mLastUpdateTime;
             if (diffTime < UPDATE_INTERVAL) {
                 return;
             }
             mLastUpdateTime = currentTime;
             float x = event.values[0];
             float y = event.values[1];
             float z = event.values[2];
             float deltaX = x - mLastX;
             float deltaY = y - mLastY;
             float deltaZ = z - mLastZ;
             mLastX = x;
             mLastY = y;
             mLastZ = z;
             float delta = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / diffTime * 10000);
             // 当加速度的差值大于指定的阈值，认为这是一个摇晃
             if(delta>2000)
            	 Log.i("shark","value = "+delta);
             if (delta > shakeThreshold) {
            	 Message msg = new Message();  
                 msg.what = SENSOR_SHAKE;  
                 handler.sendMessageDelayed(msg, 100);
             }
        }  
  
        @Override  
        public void onAccuracyChanged(Sensor sensor, int accuracy) {  
  
        }  
    };  

//	public void openDoor(MDVirtualKey key){
//		MiaodouKeyAgent.openDoor(key);
//	}


	private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(MainActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
	//TODO
    Handler handler = new Handler() {  
  
        @Override  
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            Log.i("handler","msg="+msg.what);
            switch (msg.what) {  
            case SENSOR_SHAKE: 
                Log.i("shark", "检测到摇晃，执行操作！"+isLock);
                if(isOpened){
                	return;
                }
                if(isLock)
                	return;
                mainFragment.showAnimation();
//                vibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
                //检查蓝牙的连接状态
                if(!isConnectForBlue()){
                	if(turnOnBluetooth()){
                		new Handler().postDelayed(new Runnable(){    
        				    public void run() { 
        		                keyinfo=null;
        		                ManOpendoor(1);	
        				    }    
        				 }, 2000); 
                	}
                }else{
	                keyinfo=null;
	                ManOpendoor(1);
                }
                break;
            case SHARK_OPEN:
            	isSharkOpen = true;
//            	stopAutoOpen();
            	break;
            case AUTO_OPEN:
            	isSharkOpen = false;
//            	startAutoOpen();
            	break;
            case KEY_OPEN:
            	checkMatchTime=System.currentTimeMillis();
            	isConnectForBlue();
                if(isOpened){
                	return;
                }
            	Keyinfo sendInfo=(Keyinfo) msg.obj;
            	openDoorByKey(sendInfo);
            	break;
            case KEY_REFRESH:
            	keylist.clear();
            	keylist.addAll(MyApplication.getInstances().getKeylist());
            	if(handler.hasMessages(SCANDEVICE)){
            		handler.removeMessages(SCANDEVICE);
            		msg=new Message();
            		msg.what=SCANDEVICE;
            		handler.sendMessageDelayed(msg, 0);
            	}
            	break;
            case APP_UPDATE:
            	appUpdate();
            	break;
            case KEY_OPEN_KEY://点击钥匙开门
            	if(checkKeyinfo(keyinfo)){
            		openPid=device.getDeviceId();
            		openTime=getNowTime();
            		MobclickAgent.onEvent(MainActivity.this, "opendoor");
            		runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Log.e("MYTAG", "摇一摇或者点击图标匹配的时间=="+(System.currentTimeMillis()-checkMatchTime)+"毫秒");
							checkOpenTime=System.currentTimeMillis();
							isOpened=true;
							blueLockPub.oneKeyOpenDevice(device, keyinfo.getPid(),keyinfo.getLock_id());
						}
					});
            	}
            	break;
            case KEY_OPEN_AUTO://摇一摇开门
            	for (int i = 0; i < keylist.size(); i++) {
            		final Keyinfo info=keylist.get(i);
                	if(device.getDeviceId().equals(info.getPid())){
                		if(checkKeyinfo(info)){
                			openPid=device.getDeviceId();
                			openTime=getNowTime();
                			MobclickAgent.onEvent(MainActivity.this, "opendoor");
                    		runOnUiThread(new Runnable() {
        						
        						@Override
        						public void run() {
        							blueLockPub.oneKeyOpenDevice(device, device.getDeviceId(), info.getLock_id());
        						}
        					});
                		}
                		break;
                	}
				}
            	break;
            case STOP_SCANERDEVICE://停止扫描
            	blueLockPubScan.stopScanDevice();
            	break;
            case INITBLUE:
           		runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						blueLockPub.oneKeyDisconnectDevice(device);
					}
				});
            	break;
            case SCANDEVICE://扫描
        		if(MyApplication.getInstances().getKeyNum() == 0)
        			return;
    			Message message=new Message();
        		if(!islockScreen||!isLock){
        			isClear=true;
//        			Log.e("MYTAG","scanDevice");
        			Log.e("MYTAG","scan="+blueLockPubScan);

        			if(!MonitorService.isBackground(MainActivity.this)){//是否在前台运行
	        			blueLockPubScan.scanDevice(3000);
//        				ShowMsg("SCANDEVICE");
	        			//停止扫描
	        			message.what=STOP_SCANERDEVICE;
	        			handler.sendMessageDelayed(message,3000);
        			}else{
    					Log.e("handler","isRunScan="+isRunScan());
        				if(isRunScan()){//是否在自动扫描时间段内
        					isRunscan = true;
    	        			blueLockPubScan.scanDevice(3000);
//            				ShowMsg("SCANDEVICE");
    	        			message.what=STOP_SCANERDEVICE;
    	        			handler.sendMessageDelayed(message,3000);
        				}
        			}
        		}
    			//15秒执行一次	        			
    			message=new Message();
    			message.what=SCANDEVICE;
    			handler.sendMessageDelayed(message,15000);
            	break;
            }
        }  
    };  

    private boolean isRunScan(){
    	String timer = PreferencesUtils.getString(MainActivity.this, "timer","");
    	String[] ps = timer.split(",");
    	SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
    	String date = sDateFormat.format(new java.util.Date());
    	boolean run = false;
    	try {
			for(int i=0;i<ps.length;i++){
				String[] arr = ps[i].split("-");
				String stdate = arr[0];
				String eddate = arr[1];
				if(compare_date(date, stdate) >= 0 && compare_date(date, eddate) < 0){
					run = true;
				}else
					run = false;
				if(run)
					return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return run;
    }
    
    private int compare_date(String DATE1, String DATE2) {
    	long longstr1 = Long.valueOf(DATE1.replaceAll("[-\\s:]",""));
    	long longstr2 = Long.valueOf(DATE2.replaceAll("[-\\s:]",""));
    	if(longstr1 > longstr2)
    		return 1;
    	else if(longstr1 == longstr2)
    		return 0;
    	else
    		return -1;
    }
    
//	private void startAutoOpen(){
//		Log.e(TAG,"startAutoOpen"); 
//		 if(!isSharkOpen){
//			 AutoOpenThread = new autoThread(handler);
//			 AutoOpenThread.start();			
//		 }
//	}
//
//	private void stopAutoOpen(){
//		Log.e(TAG,"stopAutoOpen"); 
//		if(AutoOpenThread != null){
//			AutoOpenThread.isStop(true);
//		}
//	}		

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
											MainActivity.this, header);
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
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction()) ) {//当蓝牙变化的时候  
            	 int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                 switch(blueState){
 	                case BluetoothAdapter.STATE_TURNING_ON:
 	                    break;
 	                case BluetoothAdapter.STATE_ON:
	 	           		mainFragment.isBlueOn = true;
	 	           		mainFragment.refresh();
				        Message message=new Message();
				        message.what=SCANDEVICE;
				        handler.sendMessageDelayed(message, 1000);
 	                    break;
 	                case BluetoothAdapter.STATE_TURNING_OFF:
 	                    break;
 	                case BluetoothAdapter.STATE_OFF:
	 	           		mainFragment.isBlueOn = false;
	 	           		mainFragment.refresh();
 	                    break;
                 }     
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
                		List<OpenLog> openLogs = BaseDataManager.getInstance(MainActivity.this).getAllOpenLog();
                		if(openLogs.size()>0){
	                		Gson gson = new Gson();
	                		String openlist = gson.toJson(openLogs);
	//                		Log.e("openlogs","log="+gson.toJson(openLogs));
//	                		OpenNotifyBatch(URLEncoder.encode(openlist));
	                		OpenNotifyBatch(openlist);
                		}
            			long ctime = PreferencesUtils.getLong(MainActivity.this, "getkeytime", 0);
//            			Log.e("getkeytime","time="+ctime+"--"+System.currentTimeMillis());
            			Log.e("MYTAG", "getKeyInfo()");
            			if(System.currentTimeMillis() - ctime > 3600000)
            				getKeyInfo();
                	}
                	isOnline = true;
                }
            }
        }  
    };
    
    /**
     * 离线保存开门日志
     * @param pid
     * @param state
     * @param msg
     * @param mode
     */
    public void saveOpenLog(String pid, int state, String msg, int mode){
    	Log.i("saveOpenLog","log");
    	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String opentime = sdf.format(new Date(System.currentTimeMillis())); 
    	OpenLog openLog = new OpenLog(pid,state,msg,opentime,mode);
    	BaseDataManager.getInstance(MainActivity.this).saveOfflineLog(openLog);
    }
    
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
					PreferencesUtils.putLong(MainActivity.this, "getkeytime", System.currentTimeMillis());
					KeyinfoComm bean = arg0.read(KeyinfoComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
					String desRsaKey = SharepreferenceUtil.getDate(MainActivity.this, "deskey");
					String kjson = Des3.encode(arg0.getResult(), desRsaKey);
					PreferencesUtils.putString(MainActivity.this, "keylist", kjson);
					
					if (FlowConsts.STATUE_1.equals(st)) {
						keylist.clear();
						keylist.addAll(bean.getBody());
						MyApplication.getInstances().setKeylist(keylist);
						MyApplication.getInstances().setKeyNum(keylist.size());
						PreferencesUtils.putInt(MainActivity.this, "keynum", keylist.size());
				        Message message=new Message();
				        message.what=SCANDEVICE;
				        handler.sendMessageDelayed(message, 500);
				        mainFragment.setViewShow("未知");
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									MainActivity.this, header);
							keylist.clear();
							MyApplication.getInstances().setKeylist(keylist);
							MyApplication.getInstances().setKeyNum(keylist.size());
							PreferencesUtils.putInt(MainActivity.this, "keynum", keylist.size());
							return;
						}else if("3".equals(st)){
							mainFragment.setViewShow(msg);
						}else if("4".equals(st)){
							mainFragment.setViewShow(msg);
						}
					}
					mainFragment.refresh();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	// 开门通知(批量上传)
	public void OpenNotifyBatch(String openlist) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("openlist", openlist);
		HttpHelper.getInstances(MainActivity.this).send(FlowConsts.DOOROPENNOTIFYBATCH,
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
					Comm bean = arg0.read(Comm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						BaseDataManager.getInstance(MainActivity.this).clearOpenLog();
						Log.i("dooropennotify","success");
						
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									MainActivity.this, header);
							keylist.clear();
							MyApplication.getInstances().setKeylist(keylist);
							MyApplication.getInstances().setKeyNum(keylist.size());
							PreferencesUtils.putInt(MainActivity.this, "keynum", keylist.size());
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	// 开门通知
	public void OpenNotify(String pid, int state, String msg, int mode) {
		isOpenComplete = true;
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("pid", pid);
		cellComAjaxParams.put("state", state+"");
		cellComAjaxParams.put("msg", msg);
		cellComAjaxParams.put("opentime", getNowTime());
		cellComAjaxParams.put("mode", mode+"");
		HttpHelper.getInstances(MainActivity.this).send(FlowConsts.DOOROPENNOTIFY,
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
					Comm bean = arg0.read(Comm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
					if (FlowConsts.STATUE_1.equals(st)) {
						Log.i("dooropennotify","success");
					}else{
						Log.e("dooropennotify","error="+msg);
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									MainActivity.this, header);
							keylist.clear();
							MyApplication.getInstances().setKeylist(keylist);
							MyApplication.getInstances().setKeyNum(keylist.size());
							PreferencesUtils.putInt(MainActivity.this, "keynum", keylist.size());
						}
					}
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
										MainActivity.this, header);
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
	
	private void initDestopText(View childView){
		 
        //直接通过Activity获取的 WindowManager，在act退出时，桌面组件也将退出。
//      WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE); 
        //一定要通过getApplicationContext()获取WindowManager,这种情况下，当Application终止后，悬浮控件才会被退出
        WindowManager wm = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE); 
         
         
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();  
//      params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        params.type = WindowManager.LayoutParams.TYPE_PHONE; // 根据电话状态调整透明窗位置
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;  
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;  
        wm.addView(childView, params);  
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
//				if (data == null) {
//					if (img != null) {
//
//						uploadpic(img);
//
//					}
//					return;
//				} else {
//					Bundle extras = data.getExtras();
//					if (extras != null) {
//						Bitmap bm = extras.getParcelable("data");
//						saveBitmap(bm);
//					}
//				}
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
        MobclickAgent.onPageStart("MainScreen");
        MobclickAgent.onResume(this);
//		int sen = PreferencesUtils.getInt(MainActivity.this, "Sensitivity", 0);
//		shakeThreshold = 4000 - sen * 30;
        if (sensorManager != null) {// 注册监听器  
            sensorManager.registerListener(sensorEventListener, 
            		sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
            		sensorManager.SENSOR_DELAY_GAME);  
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率  
        }  
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		isLock=false;
		isForeground = false;
        MobclickAgent.onPageEnd("MainScreen");
        MobclickAgent.onPause(this);
//        blueLockPub.removeResultCallBack(lockCallback);
	}

	@Override
	public void finish() {
		if(mSlidingPlayView != null)
			mSlidingPlayView.stopPlay();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		if (sensorManager != null) {// 取消监听器  
            sensorManager.unregisterListener(sensorEventListener);  
        } 
//		stopAutoOpen();
//		MiaodouKeyAgent.unregisterMiaodouAgent();
		if(mMessageReceiver != null)
			unregisterReceiver(mMessageReceiver);
		if(myReceiver != null)
			unregisterReceiver(myReceiver);
		if(blueLockPub != null){
			BlueLockPub.bleLockUnInit();
//			blueLockPubScan.stopScanDevice();			
		}
		if(handler.hasMessages(SCANDEVICE)){
			handler.removeMessages(SCANDEVICE);
		}
		device = null;
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
	}


//	@Override
//	public void findAvaliableKey(MDVirtualKey arg0) {
//		Log.e("MiaoDou","正在开门");
//		MobclickAgent.onEvent(MainActivity.this, "opendoor");
//		if(islockScreen)
//			MobclickAgent.onEvent(MainActivity.this, "opendoor_off");
//		else
//			MobclickAgent.onEvent(MainActivity.this, "opendoor_on");
//		MiaodouKeyAgent.openDoor(arg0,true);
//	}

//	@Override
//	public void onComplete(int action, MDVirtualKey key) {
//		Log.i("MiaoDou", "onComplete");
//		mClickTime = System.currentTimeMillis();
//		isOpening = false;
//		switch (action) {
//		case MDAction.ACTION_OPEN_DOOR:
//			
//			Log.e("MiaoDou","开门成功");
//			vibrator.vibrate(500);
//			OpenNotify(key.name, 1, "开门成功");
//			MobclickAgent.onEvent(MainActivity.this, "opendoor_success");
//			handler.sendEmptyMessageDelayed(0, 1000);
//			break;
//		}
//	}

//	@Override
//	public void onDisconnect() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onError(int arg0, int arg1) {
//		// TODO Auto-generated method stub
//		
//	}

//	private void showOpenMsg(int errcode){
//		String msg = "";
//		switch (errcode) {
//		case -1000:
//			msg = "APP_ID 缺失";
//			break;
//		case -2001:
//			msg = "设备 mac 地址为空";
//			break;
//		case -2002:
//			msg = "蓝牙未开启";
//			break;
//		case -2003:
//			msg = "钥匙失效";
//			break;
//		case -2004:
//			msg = "与设备建立连接失败";
//			break;
//		case -2005:
//			msg = "开门失败";
//			break;
//		case -2006:
//			msg = "与设备断开连接";
//			break;
//		case -2007:
//			msg = "解析数据失败";
//			break;
//		case -2008:
//			msg = "APP_KEY 与应用不匹配";
//			break;
//		case -2009:
//			msg = "附近没有可用设备";
//			break;
//		case -2010:
//			msg = "设备扫描超时";
//			break;
//		case -2011:
//			msg = "设备连接超时";
//			break;
//		case -2012:
//			msg = "钥匙信息解析失败";
//			break;
//		case -2013:
//			msg = "摇一摇钥匙参数信息有误";
//			break;
//		case -2014:
//			msg = "开门参数中存在null或空字符串";
//			break;
//		case -2015:
//			msg = "蓝牙服务发现失败";
//			break;
//		case -2016:
//			msg = "蓝牙特征值发现失败";
//			break;
//		case -2017:
//			msg = "获取蓝牙订阅值错误";
//			break;
//		case -2018:
//			msg = "钥匙有效期失效";
//			break;
//		case -2019:
//			msg = "没有蓝牙设备";
//			break;
//		case -2020:
//			msg = "蓝牙错误";
//			break;
//		case -2021:
//			msg = "手机没有蓝牙模块";
//			break;
//		case -2022:
//			msg = "设备回调超时";
//			break;
//		case -2023:
//			msg = "BLE通道服务未在Mainfest中注册";
//			break;
//		case -2024:
//			msg = "手机不支持BLE";
//			break;
//		case -2027:
//			msg = "亲，您操作太频繁了~~";//写入失败";
//			break;
//
//		default:
//			msg = "未知";
//			break;
//		}
//		ToastUtils.show(MainActivity.this, "开门失败:"+msg, 1);
//	}
	
//	@Override
//	public void onError(int arg0, int errcode, MDVirtualKey key) {
//		isOpening = false;
//		try {
//			String keyname = "";
//			if(key != null)
//				keyname = key.name;
//			Log.i("MiaoDou", "onError:"+keyname+"出错"+arg0+"=>"+errcode);
//			showOpenMsg(errcode);
//			OpenNotify(keyname, 0, errcode+"");
//			MobclickAgent.onEvent(MainActivity.this, "opendoor_fail");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void onOpendoorGetSurpirsed(List<BigSurprise> arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void scaningDevices() {
//		Log.i("MiaoDou", "scaningDevices");
//	}

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		
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
			new ShareAction(this)
			.setPlatform(SHARE_MEDIA.WEIXIN)
			.setCallback(umShareListener)
			.withText(sharecontent)
			.withTargetUrl(shareurl)
			.withMedia(new UMImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
			.share();
			break;
		case 8:
			new ShareAction(this)
			.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
			.setCallback(umShareListener)
			.withTitle("得令开门")
			.withText(sharecontent)
			.withTargetUrl(shareurl)
			.withMedia(new UMImage(MainActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
			.share();
			break;

		default:
			break;
		}
	}
	

	//点击钥匙开门
	public void openDoorByKeyinfo(Keyinfo keyinfo,int type){
//		if((System.currentTimeMillis() - mClickTime) < 500){
//			return;
//		}
//		Log.e("MYTAG", "openDoorByKeyinfo  true");
//		if(device!=null){
//			blueLockPub.oneKeyDisconnectDevice(device);	
//		}
//		mClickTime=System.currentTimeMillis();
//		Log.e("MYTAG", "openDoorByKeyinfo  isOpening=");
//		if(!isOpening){ 
//			isOpening = true;
        	if(isOpened){
        	return;
        	}
            if(isOpenComplete)
            	openFlag = type;
            isOpenComplete = false;
			this.keyinfo=keyinfo;
			device=mapKeyDevices.get(keyinfo);
			if(device==null){//点击了钥匙，如果设备不存在就重新扫描
				Message message=new Message();
				message.what=SENSOR_SHAKE;
				handler.sendMessageDelayed(message, 0);
			}else{//设备存在的话就开门
				openDoor();
			}
		}
//	}
	
	private void openDoor(){
		if(checkKeyinfo(keyinfo)){
    		openPid=device.getDeviceId();
    		openTime=getNowTime();
    		MobclickAgent.onEvent(MainActivity.this, "opendoor");
    		Log.e("MYTAG", "摇一摇或者点击图标匹配的时间=="+(System.currentTimeMillis()-checkMatchTime)+"毫秒");
			checkOpenTime=System.currentTimeMillis();
			isOpened=true;
			blueLockPub.oneKeyOpenDevice(device, keyinfo.getPid(),keyinfo.getLock_id());
//    		runOnUiThread(new Runnable() {
//				
//				@Override
//				public void run() {
//					Log.e("MYTAG", "摇一摇或者点击图标匹配的时间=="+(System.currentTimeMillis()-checkMatchTime)+"毫秒");
//					checkOpenTime=System.currentTimeMillis();
//					isOpened=true;
//					blueLockPub.oneKeyOpenDevice(device, keyinfo.getPid(),keyinfo.getLock_id());
//				}
//			});
    	}
	}
	private class LockCallBack extends BlueLockPubCallBackBase{
		@Override
		public void scanDeviceCallBack(final LEDevice ledevice, int result, int rssi) {
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						if(isClear){
							isClear=false;
//		        			devices.clear();
		        			mapKeyDevices.clear();
						}
						addDeviceToMap(ledevice);
//						devices.add(ledevice);
//						addDevice2List(devices, ledevice);
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}
			});
			super.scanDeviceCallBack(ledevice, result, rssi);
		}

		@Override
		public void scanDeviceEndCallBack(int result) {
			//TODO
			super.scanDeviceEndCallBack(result);
		}

		@Override
		public void connectDeviceCallBack(int result, int status) {
			// TODO Auto-generated method stub
			super.connectDeviceCallBack(result, status);
			Log.e("MYTAG", "connectDeviceCallBack  result="+result+"  status="+status);
			if(result==-6){
				OpenNotify(openPid,0,result+"",openFlag);
				
				Message message=new Message();
				message.what=INITBLUE;
				handler.sendMessageDelayed(message, 1000);
			}
			super.connectDeviceCallBack(result, status);
		}

		@Override
		public void connectingDeviceCallBack(int result) {
			// TODO Auto-generated method stub
			super.connectingDeviceCallBack(result);
		}

		@Override
		public void disconnectDeviceCallBack(int result, int status) {
			Log.e("MYTAG", "disconnectDeviceCallBack  result="+result+"  status="+status);
//			isOpening = false;
			isOpened=false;
			super.disconnectDeviceCallBack(result, status);
		}

		@Override
		public void readVerInfoCallBack(int result, String HWVersion,
				String SWVersion, int configFlage) {
			// TODO Auto-generated method stub
			super.readVerInfoCallBack(result, HWVersion, SWVersion, configFlage);
		}

		@Override
		public void readDeviceInfoCallBack(int result, String deviceId,
				int configStatus) {
			// TODO Auto-generated method stub
			super.readDeviceInfoCallBack(result, deviceId, configStatus);
		}

		@Override
		public void readDeviceConfigCallBack(int result, final int advInt,
				final int connectInt, final int txPower, final int activetime) {
			// TODO Auto-generated method stub
			super.readDeviceConfigCallBack(result, advInt, connectInt, txPower,
					activetime);
			Log.e("MYTAG", "result="+result+" advInt="+advInt+" connectInt="+connectInt+" txPower="+txPower+" activetime="+activetime);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
				}
			});
		}

		@Override
		public void setDeviceConfigCallBack(int result) {
			// TODO Auto-generated method stub
			super.setDeviceConfigCallBack(result);
		}

		@Override
		public void modifyDevicePasswordCallBack(int result) {
			// TODO Auto-generated method stub
			super.modifyDevicePasswordCallBack(result);
		}

		@Override
		public void modifyDeviceNamCallBack(int result) {
			// TODO Auto-generated method stub
			super.modifyDeviceNamCallBack(result);
		}

		@Override
		public void readInputStatusCallBack(int result, int switchStatus) {
			// TODO Auto-generated method stub
			super.readInputStatusCallBack(result, switchStatus);
		}

		@Override
		public void openCloseDeviceCallBack(int result, int battery,
				String... params) {
			Log.e("MYTAG", "开门的时间=="+(System.currentTimeMillis()-checkOpenTime)+"毫秒");
			//上传开门结果     0 表示成功 result为失败结果码
			if(result == 0)
				vibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
			
			if(isOnline){
				if(result==0){
					MobclickAgent.onEvent(MainActivity.this, "opendoor_success");
					OpenNotify(openPid,1,"android开门成功",openFlag);
				}else{
					MobclickAgent.onEvent(MainActivity.this, "opendoor_fail");
					OpenNotify(openPid,0,result+"",openFlag);
				}
			}else{//离线保存开门记录
				OpenLog log=new OpenLog();
				log.setMode(openFlag);
				log.setMsg(result+"");
				log.setOpentime(getNowTime());
				log.setPid(openPid);
				if(result==0){
					log.setState(1);
				}else{
					log.setState(0);
				}
				BaseDataManager.getInstance(mContext).saveOfflineLog(log);
			}
		}

		@Override
		public void readPaswdAndCardKeyCallBack(int result, int totalKey,
				int currentKey, String userId, int keyStatus) {
			// TODO Auto-generated method stub
			super.readPaswdAndCardKeyCallBack(result, totalKey, currentKey,
					userId, keyStatus);
		}

		@Override
		public void addPaswdAndCardKeyCallBack(int result) {
			// TODO Auto-generated method stub
			super.addPaswdAndCardKeyCallBack(result);
		}

		@Override
		public void deletePaswdAndCardKeyCallBack(int result) {
			// TODO Auto-generated method stub
			super.deletePaswdAndCardKeyCallBack(result);
		}

		@Override
		public void readClockCallBack(int result, int year, int month, int day,
				int hour, int minute, int second) {
			// TODO Auto-generated method stub
			super.readClockCallBack(result, year, month, day, hour, minute,
					second);
		}

		@Override
		public void setClockCallBack(int result) {
			// TODO Auto-generated method stub
			super.setClockCallBack(result);
		}

		@Override
		public void configWifiCallBack(int result) {
			// TODO Auto-generated method stub
			super.configWifiCallBack(result);
		}

		@Override
		public void configServerCallBack(int result) {
			// TODO Auto-generated method stub
			super.configServerCallBack(result);
		}

		@Override
		public void configWifiHeartBeatCallBack(int result) {
			// TODO Auto-generated method stub
			super.configWifiHeartBeatCallBack(result);
		}

		@Override
		public void flashAddKeyCallBack(int result) {
			// TODO Auto-generated method stub
			super.flashAddKeyCallBack(result);
		}

		@Override
		public void flashReadKeyWithIndexCallBack(int result) {
			// TODO Auto-generated method stub
			super.flashReadKeyWithIndexCallBack(result);
		}

		@Override
		public void flashDeleteKeyCallBack(int result) {
			// TODO Auto-generated method stub
			super.flashDeleteKeyCallBack(result);
		}
	}
	//把扫描到的设备进行匹配，匹配成功的设备保存到map中
	public void addDeviceToMap(LEDevice device){
//		Log.e("MYTAG", "Allrssi="+device.getRssi());
		if(mapKeyDevices.size()==0){
			for (Keyinfo info : keylist) {
				if(info.getPid().equals(device.getDeviceId())){
					Log.e("MYTAG", "mapKeyDevices put one");
					Log.e("MYTAG", "rssi0="+device.getRssi());
//					ShowMsg(device.getDeviceId()+"="+device.getRssi());
					if(device.getRssi() > -80)
						mapKeyDevices.put(info, device);
					break;
				}
			}
		}else{ //存在相同的数据则不增加
			for (Keyinfo info : keylist) {
				if(info.getPid().equals(device.getDeviceId())){	
//					Log.e("MYTAG", "mapKeyDevices ="+mapKeyDevices.get(info));					
					if(mapKeyDevices.get(info)==null){
						Log.e("MYTAG", "rssi1="+device.getRssi());
						if(device.getRssi() > -80)
							mapKeyDevices.put(info, device);
						Log.e("MYTAG", "mapKeyDevices put more");
						break;
					}
				}
			}
		}
		if(mapKeyDevices.size()>0){
			mainFragment.isFound=true;
			if(mapKeyDevices.size()>1){
				List<Keyinfo> infos=new ArrayList<Keyinfo>(mapKeyDevices.keySet());
				mainFragment.setAdapterData(infos);
			}
		}else{
			mainFragment.isFound=false;
			mainFragment.clearAdapterData();
		}
		mainFragment.refresh();
	}
	//把最新一次扫描到的不同的设备保存到list中
	public void addDevice2List(List<LEDevice> list,LEDevice device){
		if(list.size()==0){
			list.add(device);
		}else{
			for (LEDevice leDevice : list) {
				if(leDevice.getDeviceId().equals(device.getDeviceId())){
					return;
				}
			}
			list.add(device);
		}
	}
	public boolean checkKeyinfo(Keyinfo keyinfo){
		if(keyinfo==null){
			return false;
		}
		String time=keyinfo.getValuetime();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		long serviceTime=0;
		try {
			serviceTime=format.parse(time).getTime();
		} catch (Exception e) {
			Log.e("MYTAG", "时间格式转换错误");
			return true;
		}
		if(System.currentTimeMillis()>serviceTime){
			ToastUtils.show(MainActivity.this, "您申请的手机钥匙已经到期啦，您可以到物业管理处进行办理或重新申请！");
			return false;
		}else{
			return true;
		}
	}
	//弹出提示对话框
	public void showDialog(Activity activity,String title,String content){
		showAlertDialog(activity,title, content, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isShow=false;
			}
		},new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isShow=false;
				//跳到主界面
				int sum=MyApplication.getInstances().getActivities().size();
				if(sum>0){
					for (Activity act : MyApplication.getInstances().getActivities()) {
						act.finish();
					}
				}
				if(!isMain){
					isMain=true;
					tabRbCenter.setChecked(true);
					vpContent.setCurrentItem(1, false);
				}
				ToastUtils.show(MainActivity.this, "亲，你能打开多个门哦，请点击屏幕下方的图标来打开对应的门!");
			}
		} );
	}
	//返回对话框
	private AlertDialog showAlertDialog(Activity activity,String p_Title, String p_Message,
			DialogInterface.OnClickListener p_YesClickListener,
			DialogInterface.OnClickListener p_NoClickListener) {
		return new AlertDialog.Builder(activity).setTitle(p_Title)
				.setMessage(p_Message)
				.setPositiveButton(R.string.btn_cancel, p_YesClickListener)
				.setNegativeButton(R.string.btn_gotochoolse, p_NoClickListener)
				.show();
	}
	//获得指定的格式的时间
	private String getNowTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long strTime=System.currentTimeMillis();
		return format.format(new Date(strTime));
	}
	private void showNotification(){
        Notification myNotify = new Notification();  
        myNotify.icon = R.drawable.logo;
        myNotify.tickerText = "得令开门提醒";  
        myNotify.when = System.currentTimeMillis();  
        myNotify.flags = Notification.FLAG_AUTO_CANCEL;// 不能够自动清除  
        RemoteViews rv = new RemoteViews(getPackageName(),  
                R.layout.my_notification);  
        rv.setTextViewText(R.id.text_content, "哇，你附近有多个蓝牙门禁设备，请进入到应用选择要开的门哦！");  
        myNotify.contentView = rv;  
        Intent intent = new Intent(this,MainActivity.class);  
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT /*| Intent.FLAG_ACTIVITY_SINGLE_TOP*/);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,  
                intent, 1);  
        myNotify.contentIntent = contentIntent;  
        manager.notify(1, myNotify);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	public static boolean isNetworkConnected(Context context) {    
		if (context != null) {       
		       ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);        
		       NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();        
		       if (mNetworkInfo != null) {            
		             return mNetworkInfo.isAvailable()&& mNetworkInfo.isConnectedOrConnecting();    
		                             } 
		                     }    
		       return false;
	}
	public boolean isConnectForBlue(){
        //检查蓝牙的连接状态
        if(!mainFragment.isBlueOn){
        	myDialog = new AlertDialog.Builder(this).create();
			myDialog.show();
			myDialog.getWindow().setContentView(R.layout.app_alertdialog_popup);
			TextView tvContent =  (TextView) myDialog.getWindow().findViewById(R.id.tv_content);
			 Button btSetting =  (Button) myDialog.getWindow().findViewById(R.id.btn_ok);
			 Button btCancel =  (Button) myDialog.getWindow().findViewById(R.id.btn_cancel);
			 tvContent.setText("主人，蓝牙未打开哦，马上打开蓝牙才能摇一摇开门哦！");
			 btSetting.setText("设置");
			 btCancel.setText("好");
			 btSetting.setOnClickListener(new View.OnClickListener() {  
                 @Override  
                 public void onClick(View v) {  
                	 startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                	 myDialog.dismiss(); 
                 }  
             });   
			 btCancel.setOnClickListener(new View.OnClickListener() {  
                 @Override  
                 public void onClick(View v) {  
                     myDialog.dismiss();  
                 }  
             });  
//        	showDialog(MainActivity.this,"蓝牙提醒","主人，蓝牙未打开哦，马上打开蓝牙才能摇一摇开门哦！");
        	mainFragment.clearAdapterData();
        	return false;
        }
        return true;
	}
	public void mateDevice(int type){
		if(!checkKeyInfo()){
			showAlertDialog(MainActivity.this, "温馨提示", "马上申请钥匙，就可以摇一摇开门咯！",
					new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							OpenActivity(AskKeyActivity.class);
						}
					}, 
					new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			return;
		}
		int dLen=mapKeyDevices.size();
		Log.e("MYTAG", "scanDeviceEndCallBack dLen="+dLen);
		if(dLen==1){//只有一个设备，直接开门
			Keyinfo info=mapKeyDevices.keySet().iterator().next();
			openDoorByKeyinfo(info,type);
			mainFragment.clearAdapterData();
		}else if(dLen>1){
			//锁屏状态下
			if(!isLock || MonitorService.isBackground(getApplicationContext())){
				showNotification();
			}else{
				//如果打开了activitry
				if(MyApplication.getInstances().getActivities().size()>0){
					//判断是否已经有dialog显示了
					if(!isShow){
						isShow=true;
						//取出保存的最后一个的activity
						Activity ac=MyApplication.getInstances().getActivities().get(MyApplication.getInstances().getActivities().size()-1);
						showDialog(ac,"蓝牙扫描","亲，您要开哪个门，点我选择吧！");	
					}
				}else{//停留在主界面
					if(!isMain && !isShow){
						isShow=true;
						showDialog(MainActivity.this,"蓝牙扫描","亲，您要开哪个门，点我选择吧！");
					}
				}
			}
		}else{
			ToastUtils.show(MainActivity.this, "亲，再近一点，在3米内才能开门哦！");
			isClear=true;
			blueLockPubScan.scanDevice(3000);
			Message message=new Message();
			message.what=STOP_SCANERDEVICE;
			handler.sendMessageDelayed(message, 3000);
		}
//			if(dLen>0){//多个设备的情况
//			List<Keyinfo> infos=new ArrayList<Keyinfo>();
//			int len=keylist.size();
//			for (int i = 0; i < len; i++) {
//				Keyinfo info=keylist.get(i);
//				for (int j = 0; j < dLen; j++) {
//					LEDevice device=devices.get(j);
//					if(device.getDeviceId().equals(info.getPid())){
//						infos.add(info);
//						continue;
//					}
//				}
//			}
//			//判断匹配到的设备有多少个
//			if(infos.size()==1){
//				Keyinfo keyinfo=infos.get(0);
//				openDoorByKeyinfo(keyinfo,1);
//				mainFragment.clearAdapterData();
//			}else if(infos.size()>1){
//				//锁屏状态下
//				if(!isLock){
//					showNotification();
//				}else{
//					//如果打开了activitry
//					if(MyApplication.getInstances().getActivities().size()>0){
//						//取出保存的最后一个的activity
//						Activity ac=MyApplication.getInstances().getActivities().get(MyApplication.getInstances().getActivities().size()-1);
//						showDialog(ac,"蓝牙扫描","主人，您附近有多个蓝牙开门设备，点下我选择要开的门哦！");
//					}else{//停留在主界面
//						if(!isMain){
//							Log.e("MYTAG", "showDialog");
//							showDialog(MainActivity.this,"蓝牙扫描","主人，您附近有多个蓝牙开门设备，点下我选择要开的门哦！");
//						}
//					}
//				}
//				mainFragment.setAdapterData(infos);	
//			}else{
//				ToastUtils.show(MainActivity.this, "亲，您离我太远了，请不要离我超过3米！");
//			}
//		}else{
//			mainFragment.clearAdapterData();
//			ToastUtils.show(MainActivity.this, "亲，您离我太远了，请不要离我超过3米！");
//		}
	}
	//检查是否有钥匙
	public boolean checkKeyInfo(){
		if(keylist.size()==0){
			return false;
		}else{
			return true;
		}
	}
}
