package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.adapter.MyKeyDetailAdapter;
import deling.cellcom.com.cn.bean.Keyinfo;
import deling.cellcom.com.cn.bean.KeyinfoComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;
//import com.hzblzx.miaodou.sdk.MiaodouKeyAgent;
//import com.hzblzx.miaodou.sdk.core.model.MDVirtualKey;

public class MyKeyDetialActivity extends FragmentActivityBase{
    private static final int KEY_OPEN = 3;
    private static final int KEY_REFRESH = 4;
	private Header header;
	private ListView listview;
	private MyKeyDetailAdapter adapter;
	private List<Keyinfo> keyInfos=new ArrayList<Keyinfo>();
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_key_detail);
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
	}
	private void initData() {
		if(getIntent().getSerializableExtra("keys") != null)
			keyInfos.addAll((List<Keyinfo>) getIntent().getSerializableExtra("keys"));
		Log.e("keyinfo","info="+keyInfos);
		handler = MyApplication.getInstances().getHandler();
		header.setTitle(getIntent().getStringExtra("areaname"));
		header.setLeftImageVewRes(R.drawable.main_nav_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyKeyDetialActivity.this.finish();
			}
		});
		adapter=new MyKeyDetailAdapter(MyKeyDetialActivity.this);
		adapter.addAllData(keyInfos);
		listview.setAdapter(adapter);
//		getNetData();
	}
	
	public void openDoorByKey(Keyinfo info){
		 Message msg = new Message();  
         msg.what = KEY_OPEN;
         msg.obj=info;
         handler.sendMessage(msg);
	}
	
	private void initView() {
		header=(Header) findViewById(R.id.header);
		listview=(ListView) findViewById(R.id.listview);
	}
	
	public void getNetData(){
		CellComAjaxParams params=new CellComAjaxParams();
		HttpHelper.getInstances(MyKeyDetialActivity.this).send(FlowConsts.KEYINFO, 
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
											MyKeyDetialActivity.this, header);
									return;
								}else
									ShowMsg("获取钥匙信息失败");
							}
						} catch (Exception e) {
							ShowMsg("获取钥匙信息失败");
							return;
						}
						keyInfos=comm.getBody();
						adapter.addAllData(keyInfos);
						KeyGroup(keyInfos);
						if(keyInfos.size()<=0){
							ShowMsg("暂无钥匙信息哦");
							return;
						}
//						ArrayList<MDVirtualKey> keylist = new ArrayList<MDVirtualKey>(); 
//						for(int i=0;i<comm.getBody().size();i++){
//							String userId = MyApplication.getInstances().getPhone();
//							String keyName = comm.getBody().get(i).getPid();
//							String community = comm.getBody().get(i).getDepartid();
//							String keyId = comm.getBody().get(i).getLock_id();
//							MDVirtualKey m1 = MiaodouKeyAgent.makeVirtualKey(MyKeyTwoActivity.this, 
//									userId, 
//									keyName, 
//									community, 
//									keyId);	
//							keylist.add(m1);
//						}
						MyApplication.getInstances().setKeylist(keyInfos);
						Message msg = new Message();  
				        msg.what = KEY_REFRESH;
				        handler.sendMessage(msg);
					}
				});
	}
	
	private void KeyGroup(List<Keyinfo> keylist){
		Map<String, List<Keyinfo>> map = new HashMap<String, List<Keyinfo>>();
		for(int i=0;i<keylist.size();i++){
			String departid = keylist.get(i).getDepartid();
			if(map.get(departid) == null){
				List<Keyinfo> tkey = new ArrayList<Keyinfo>();
				map.put(departid, tkey);
			}
			map.get(departid).add(keylist.get(i));			
		}
		Log.e("keymap","size="+map);
	}	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//移除list最后一个元素
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
}
