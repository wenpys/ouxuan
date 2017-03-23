package deling.cellcom.com.cn.activity.me;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.adapter.MyKeyAdapter;
import deling.cellcom.com.cn.bean.Keyinfo;
import deling.cellcom.com.cn.bean.KeyinfoComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

public class MyKeyTwoActivity extends FragmentActivityBase implements OnHeaderRefreshListener, OnFooterLoadListener {
    private static final int KEY_OPEN = 3;
    private static final int KEY_REFRESH = 4;
	private Header header;
	private ListView listview;
	private MyKeyAdapter adapter;
	private List<Keyinfo> keyInfos=new ArrayList<Keyinfo>();
	private Handler handler;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment  mDialogFragment = null;
	private List<Map<String, String>> departList = new ArrayList<Map<String, String>>();
	private List<List<Keyinfo>> departMList = new ArrayList<List<Keyinfo>>();
	private TextView tvAskKey;
	private LinearLayout llProm;
	private LinearLayout llHoriz;
	private TextView tvContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_key_two);
		//记录打开过的activity
		MyApplication.getInstances().getActivities().add(MyKeyTwoActivity.this);
		initView();
		initListener();
		initData();
	}
	public void openDoorByKey(int id){
		 Message msg = new Message();  
         msg.what = KEY_OPEN;
         msg.arg1 = id;
         handler.sendMessage(msg);
	}
	
	private void initView() {
		header=(Header) findViewById(R.id.header);
		listview=(ListView) findViewById(R.id.listview);
		llProm=(LinearLayout) findViewById(R.id.ll_prom);
		tvAskKey=(TextView) findViewById(R.id.tv_askkey);
		llHoriz=(LinearLayout) findViewById(R.id.ll_horiz);
		tvContent=(TextView) findViewById(R.id.tv_content);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.mPullRefreshView);
		
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
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(MyKeyTwoActivity.this, MyKeyDetialActivity.class);
				intent.putExtra("areaname", departList.get(position).get("areaname"));
				intent.putExtra("keys", (Serializable)departMList.get(position));
				startActivity(intent);
			}
		});
		tvAskKey.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OpenActivity(AskKeyActivity.class);
			}
		});
	}
	
	private void initData() {
		handler = MyApplication.getInstances().getHandler();
		header.setTitle(getResources().getString(R.string.title_activity_my_key_two));
		header.setLeftImageVewRes(R.drawable.main_nav_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyKeyTwoActivity.this.finish();
			}
		});
		adapter=new MyKeyAdapter(MyKeyTwoActivity.this, departList);
		listview.setAdapter(adapter);
		getNetData();
	}
	
	public void getNetData(){
		CellComAjaxParams params=new CellComAjaxParams();
		HttpHelper.getInstances(MyKeyTwoActivity.this).send(FlowConsts.KEYINFO, 
				params, 
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
						ShowMsg(strMsg);
					}
					@Override
					public void onSuccess(CellComAjaxResult t) {
						DismissProgressDialog();
						KeyinfoComm comm=null;
						try {
							comm=t.read(KeyinfoComm.class, CellComAjaxResult.ParseType.GSON);
							String st = comm.getReturncode();
							if(!comm.getReturncode().equals(FlowConsts.STATUE_1)){
								if (FlowConsts.STATUE_2.equals(st)) {
									// token失效
									ContextUtil.exitLogin(
											MyKeyTwoActivity.this, header);
									return;
								}else if("3".equals(st)||"4".equals(st)){
									llProm.setVisibility(View.VISIBLE);
									listview.setVisibility(View.GONE);
									llHoriz.setVisibility(View.GONE);
									tvContent.setText(comm.getReturnmessage());
									return;
								}
								
							}
						} catch (Exception e) {
							ShowMsg("获取钥匙信息失败");
							return;
						}
						if(comm.getBody() != null)
							keyInfos.addAll(comm.getBody());
						
						KeyGroup(keyInfos);
						adapter.notifyDataSetChanged();
						
						if(keyInfos.size()<=0){
							llProm.setVisibility(View.VISIBLE);
							listview.setVisibility(View.GONE);
							llHoriz.setVisibility(View.VISIBLE);
							tvContent.setText("审批通过后，摇一摇就可以开门咯!");
//							return;
						}else{
							llProm.setVisibility(View.GONE);
							listview.setVisibility(View.VISIBLE);
						}
						MyApplication.getInstances().setKeylist(keyInfos);
						MyApplication.getInstances().setKeyNum(keyInfos.size());
						Message msg = new Message();  
				        msg.what = KEY_REFRESH;
				        handler.sendMessage(msg);
					}
				});
	}
	
	private void KeyGroup(List<Keyinfo> keylist){
		departList.clear();
		departMList.clear();
		Map<String, List<Keyinfo>> map = new HashMap<String, List<Keyinfo>>();
		for(int i=0;i<keylist.size();i++){
			String departid = keylist.get(i).getDepartid();
			String areaname = keylist.get(i).getAreaname();
			if(map.get(departid) == null){
				List<Keyinfo> tkey = new ArrayList<Keyinfo>();
				map.put(departid, tkey);
				Map<String, String> dmap = new HashMap<String, String>();
				dmap.put("departid", departid);
				dmap.put("areaname", keylist.get(i).getAreaname());
				departList.add(dmap);
				List<Keyinfo> dmkey = new ArrayList<Keyinfo>();
				departMList.add(dmkey);
			}
			map.get(departid).add(keylist.get(i));
			for(int j=0;j<departList.size();j++){
				if(departList.get(j).get("areaname").equals(areaname)){
					departMList.get(j).add(keylist.get(i));					
				}
			}
		}
//		Log.e("keymap","keymap="+map);
	}
	
	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		mAbPullToRefreshView.onFooterLoadFinish();
		
	}
	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
			CellComAjaxParams params=new CellComAjaxParams();
			HttpHelper.getInstances(MyKeyTwoActivity.this).send(FlowConsts.KEYINFO, 
					params, 
					CellComAjaxHttp.HttpWayMode.POST,
					new NetCallBack<CellComAjaxResult>() {
						@Override
						public void onStart() {
							super.onStart();
						}
						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							if(mAbPullToRefreshView != null)
								mAbPullToRefreshView.onHeaderRefreshFinish();
						}
						@Override
						public void onSuccess(CellComAjaxResult t) {
							if(mAbPullToRefreshView != null)
								mAbPullToRefreshView.onHeaderRefreshFinish();
							KeyinfoComm comm=null;
							try {
								comm=t.read(KeyinfoComm.class, CellComAjaxResult.ParseType.GSON);
								String st = comm.getReturncode();
								if(!comm.getReturncode().equals(FlowConsts.STATUE_1)){
									if (FlowConsts.STATUE_2.equals(st)) {
										// token失效
										ContextUtil.exitLogin(
												MyKeyTwoActivity.this, header);
										return;
									}else if("3".equals(st)||"4".equals(st)){
										llProm.setVisibility(View.VISIBLE);
										listview.setVisibility(View.GONE);
										llHoriz.setVisibility(View.GONE);
										tvContent.setText(comm.getReturnmessage());
										return;
									}
									
								}
							} catch (Exception e) {
								ShowMsg("获取钥匙信息失败");
								return;
							}
							keyInfos=comm.getBody();
							KeyGroup(keyInfos);
							adapter.notifyDataSetChanged();
							
							if(keyInfos.size()<=0){
								llProm.setVisibility(View.VISIBLE);
								listview.setVisibility(View.GONE);
								llHoriz.setVisibility(View.VISIBLE);
								tvContent.setText("审批通过后，摇一摇就可以开门咯!");
								return;
							}else{
								llProm.setVisibility(View.GONE);
								listview.setVisibility(View.VISIBLE);
							}
							MyApplication.getInstances().setKeylist(keyInfos);
							MyApplication.getInstances().setKeyNum(keyInfos.size());
							Message msg = new Message();  
					        msg.what = KEY_REFRESH;
					        handler.sendMessage(msg);
						}
					});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//移除list最后一个元素
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
}
