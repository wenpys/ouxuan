package deling.cellcom.com.cn.activity.main;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import cn.jpush.a.a.ac;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.google.zxing.oned.rss.FinderPattern;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentBase;
import deling.cellcom.com.cn.activity.me.AskKeyActivity;
import deling.cellcom.com.cn.activity.me.MyKeyActivity;
import deling.cellcom.com.cn.activity.me.MyKeyTwoActivity;
import deling.cellcom.com.cn.adapter.HorizAdapter;
import deling.cellcom.com.cn.bean.Keyinfo;
import deling.cellcom.com.cn.bean.KeyinfoComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.HorizontalListView;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

public class MainFragment extends FragmentBase implements OnHeaderRefreshListener, OnFooterLoadListener {
    private static final int KEY_REFRESH = 4;
    private HorizontalListView lvHoriz;
    private HorizAdapter horizAdapter;
	private Activity activity;
	private ImageView ivShark;
	private TextView tvOpenBlue;
	private TextView tvApKey;
	private ImageView ivRvKey;
	private LinearLayout llHasKey;
	private LinearLayout llNoKey;
	private LinearLayout llFbKey;
	private LinearLayout llNoblue;
	private LinearLayout llNotFound;
	private boolean isHave = false;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment  mDialogFragment = null;
	private AnimationDrawable animationDrawable;
	private boolean isRuning = false;
	private Handler handler;
	private int duration = 0;
	public boolean isBlueOn = true;
	public boolean isFound = false;  //有钥匙但未扫描到设备
	private AlertDialog myDialog = null;
	private TextView tvShow;
	private TextView tvNoKeyEnd;
	private String content="未知";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initListener();
		initData();
		refresh();
	}
	
	public void refresh(){
		if(MyApplication.getInstances().getKeyNum() == 0){
			llHasKey.setVisibility(View.GONE);
			llFbKey.setVisibility(View.GONE);
			llNoKey.setVisibility(View.VISIBLE);
			if(!content.equals("未知")){
				tvApKey.setText(content);
				tvNoKeyEnd.setVisibility(View.GONE);
			}else{
				tvApKey.setText("马上申请钥匙，");
				tvNoKeyEnd.setVisibility(View.VISIBLE);
			}
			llNoblue.setVisibility(View.GONE);
			llNotFound.setVisibility(View.GONE);
			ivShark.setImageResource(R.drawable.shark1);
			isHave = false;
		}else{
			if(MyApplication.getInstances().isKeyState()){
				if(isBlueOn){//isBlueOn){
					if(!isFound){
						llHasKey.setVisibility(View.GONE);
						llFbKey.setVisibility(View.GONE);
						llNoKey.setVisibility(View.GONE);
						llNoblue.setVisibility(View.GONE);
						llNotFound.setVisibility(View.VISIBLE);
						ivShark.setImageResource(R.drawable.shark1);
						isHave = false;						
					}else{
						llHasKey.setVisibility(View.VISIBLE);
						llFbKey.setVisibility(View.GONE);
						llNoKey.setVisibility(View.GONE);
						llNoblue.setVisibility(View.GONE);
						llNotFound.setVisibility(View.GONE);
						ivShark.setImageResource(R.drawable.shark2);
						isHave = true;
					}
				}else{
					llHasKey.setVisibility(View.GONE);
					llFbKey.setVisibility(View.GONE);
					llNoKey.setVisibility(View.GONE);
					llNoblue.setVisibility(View.VISIBLE);
					llNotFound.setVisibility(View.GONE);
					ivShark.setImageResource(R.drawable.shark1);
					isHave = false;					
				}
			}else{
				llHasKey.setVisibility(View.GONE);
				llFbKey.setVisibility(View.VISIBLE);
				llNoKey.setVisibility(View.GONE);
				llNoblue.setVisibility(View.GONE);
				llNotFound.setVisibility(View.GONE);
				ivShark.setImageResource(R.drawable.shark1);
				isHave = false;
			}
		}
	}

	private void initView() {
		lvHoriz=(HorizontalListView) activity.findViewById(R.id.lv_horiz);
		ivShark = (ImageView) activity.findViewById(R.id.shark);
		tvApKey= (TextView) activity.findViewById(R.id.apkey);
		tvOpenBlue = (TextView) activity.findViewById(R.id.openblue);
		ivRvKey = (ImageView) activity.findViewById(R.id.rvkey);
		llHasKey = (LinearLayout) activity.findViewById(R.id.ll_haskey);
		llFbKey = (LinearLayout) activity.findViewById(R.id.ll_fbkey);
		llNoKey = (LinearLayout) activity.findViewById(R.id.ll_nokey);
		tvNoKeyEnd=(TextView) activity.findViewById(R.id.tv_key_end);
		llNoblue = (LinearLayout) activity.findViewById(R.id.ll_noblue);
		llNotFound = (LinearLayout) activity.findViewById(R.id.ll_notfound);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) activity.findViewById(R.id.mPullRefreshView);
		
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
	}

	private void initListener() {
		tvApKey.setOnClickListener(new View.OnClickListener() {//申请
			
			@Override
			public void onClick(View arg0) {
				OpenActivity(AskKeyActivity.class);
			}
		});
		ivRvKey.setOnClickListener(new View.OnClickListener() {//恢复
			
			@Override
			public void onClick(View arg0) {
				OpenActivity(MyKeyTwoActivity.class);
			}
		}); 
		ivShark.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.e("isBlueOn","isBlueOn="+isBlueOn);
				if(isBlueOn){//isBlueOn){
					showAnimation();
					if(MyApplication.getInstances().getKeyNum() == 0){
						ShowMsg("亲，您还没申请钥匙，快快申请吧！");
					}else{
						showAnimation();
						((MainActivity)activity).ManOpendoor(2);
					}
				}else{
					if(((MainActivity)activity).turnOnBluetooth()){
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								new Handler().postDelayed(new Runnable(){    
								    public void run() {  
										showAnimation();
										((MainActivity)activity).ManOpendoor(2); 
								    }    
								 }, 2000); 
							}
						});
					}else{
						myDialog = new AlertDialog.Builder(activity).create();
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
			                 }  
			            });   
						btCancel.setOnClickListener(new View.OnClickListener() {  
			                 @Override  
			                 public void onClick(View v) {  
			                     myDialog.dismiss();  
			                 }  
			            });  
					}
				}
			}
		});
		tvOpenBlue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
			}
		});
		lvHoriz.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((MainActivity)activity).openDoorByKeyinfo(horizAdapter.getItem(position),2);
				startAnim(view);
			}
		});
	}

	private void initData() {
		ivShark.setImageResource(R.anim.shark);
		animationDrawable  = (AnimationDrawable) ivShark.getDrawable();
		for(int i=0;i<animationDrawable.getNumberOfFrames();i++){  
			duration += animationDrawable.getDuration(i);  
	    }
		handler = new Handler();
		horizAdapter=new HorizAdapter();
		lvHoriz.setAdapter(horizAdapter);
		lvHoriz.setVisibility(View.GONE);
	}
	
	public void showAnimation() {
		Log.e("animation","have:"+isHave+"---run:"+isRuning);
		if(isHave && !isRuning){
			isRuning = true;
			ivShark.setImageResource(R.anim.shark);
			animationDrawable  = (AnimationDrawable) ivShark.getDrawable();
			animationDrawable.stop();
			animationDrawable.start();
	        handler.postDelayed(new Runnable() {  
	            public void run() {
	               isRuning = false;
	            }
	        }, duration);
		}
    }
	
	public void refreshTask() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(activity).send(FlowConsts.KEYINFO,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);

				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				try{
					mAbPullToRefreshView.onHeaderRefreshFinish();
					KeyinfoComm bean = arg0.read(KeyinfoComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						if(bean.getBody().size() > 0){
							MyApplication.getInstances().setKeyNum(1);
							MyApplication.getInstances().setKeyState(true);
							refresh();
//							ArrayList<MDVirtualKey> keylist = new ArrayList<MDVirtualKey>(); 
//							for(int i=0;i<bean.getBody().size();i++){
//								String userId = MyApplication.getInstances().getPhone();
//								String keyName = bean.getBody().get(i).getPid();
//								String community = bean.getBody().get(i).getDepartid();
//								String keyId = bean.getBody().get(i).getLock_id();
//								MDVirtualKey m1 = MiaodouKeyAgent.makeVirtualKey(activity, 
//										userId, 
//										keyName, 
//										community, 
//										keyId);
//								
//								keylist.add(m1);
//							}
							MyApplication.getInstances().setKeylist(bean.getBody());
							Message xx = new Message();  
							xx.what = KEY_REFRESH;
					        MyApplication.getInstances().getHandler().sendMessage(xx);
						}
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									activity, llHasKey);
							return;
						}else if("3".equals(st)||"4".equals(st)){
							setViewShow(msg);
						}
						Log.e("getkeyinfo","error="+msg);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});		
	}
	
	@Override
	public void onResume() {
		refresh();
		super.onResume();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		mAbPullToRefreshView.onFooterLoadFinish();
		
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		refreshTask();
		
	}
	public void clearAdapterData(){
		if(horizAdapter.getCount()>0){
			horizAdapter.clearData();
			lvHoriz.setVisibility(View.GONE);
		}
	}
	public void setAdapterData(List<Keyinfo> keyinfos){
		horizAdapter.setData(keyinfos);
		setListViewHeightBasedOnChildren(lvHoriz);
		lvHoriz.setVisibility(View.VISIBLE);
	}
	public static void setListViewHeightBasedOnChildren(HorizontalListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalWidth=0;
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalWidth += listItem.getMeasuredWidth(); // 统计所有子项的总高度
			totalHeight=listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.width = totalWidth/*+ (listView.getMeasuredWidth() * (listAdapter.getCount() - 1))*/;
		params.height=totalHeight;
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
	public void startAnim(View view){
		AnimationSet set=new AnimationSet(true);
		ScaleAnimation animation=new ScaleAnimation(1, 0.5f, 1, 0.5f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		animation.setDuration(100);
		set.addAnimation(animation);
		view.startAnimation(set);
	}

	public void setViewShow(String content){
		this.content=content;
	}
}
