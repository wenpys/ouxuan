package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.adapter.AuthoListAdapter;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.bean.Grant;
import deling.cellcom.com.cn.bean.GrantComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.Header;

/**
 * 家属授权
 * 
 * @author xpw
 * 
 */
public class AuthoActivity extends FragmentActivityBase implements OnHeaderRefreshListener, OnFooterLoadListener {
	private ListView lvAutho;
	private Header header;
	private AuthoListAdapter myListViewAdapter = null;
	private List<Grant> list = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment  mDialogFragment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autho);
		MyApplication.getInstances().getActivities().add(AuthoActivity.this);
		InitView();
		InitData();
		InitListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}

	private void InitView() {
		lvAutho = (ListView) findViewById(R.id.mListView);
		header = (Header) findViewById(R.id.header);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

	}

	private void InitListeners() {
		lvAutho.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AuthoActivity.this, AuthoDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("from", "edit");
				bundle.putString("id", list.get(arg2).getId());
				bundle.putString("name", list.get(arg2).getName());
				bundle.putString("phone", list.get(arg2).getPhone());
				bundle.putString("identity", list.get(arg2).getIdentity());
				bundle.putString("expiry", list.get(arg2).getValuetime());
				bundle.putString("authokey", list.get(arg2).getGatename());
				bundle.putString("state", list.get(arg2).getState());
				bundle.putString("type", list.get(arg2).getType());
				intent.putExtras(bundle);
				
				startActivityForResult(intent, 100);
			}
		});
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
					AuthoActivity.this.finish();
				}
			});
		
		header.setRightTextViewRes(R.string.zjjs, new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int keynum = MyApplication.getInstances().getKeyNum();
				if(MyApplication.getInstances().getUserType() == 1 && keynum > 0){
					Intent intent = new Intent(AuthoActivity.this, AddAuthoActivity.class);
					startActivityForResult(intent, 100);
				}else if(keynum < 1){
					ShowMsg("抱歉，您暂无可授权钥匙");
				}else
					ShowMsg("亲，户主才可以授权哦！");
			}
		});
		
		header.setTitle("钥匙授权", null);

		
		list = new ArrayList<Grant>();
//		Map map = new HashMap<String,Object>();
//		map.put("phone", "18923591421");
//		map.put("starttime", "2016-06-24 15:12:09");
//		map.put("state", "1");
//		list.add(map);
//
//		map = new HashMap<String,Object>();
//		map.put("phone", "17623914212");
//		map.put("starttime", "2016-06-25 12:22:09");
//		map.put("state", "0");
//		list.add(map);
//
//		map = new HashMap<String,Object>();
//		map.put("phone", "15730468215");
//		map.put("starttime", "2016-07-11 05:32:29");
//		map.put("state", "2");
//		list.add(map);
		
		myListViewAdapter = new AuthoListAdapter(this, list);
		lvAutho.setAdapter(myListViewAdapter);
		
		getData();
	}
	
	
	private void ResetPwd(){
		finish();
	}

	public void getData(){
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", MyApplication.getInstances().getPhone());
		HttpHelper.getInstances(AuthoActivity.this).send(FlowConsts.GETGRANT,
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
				try{
					GrantComm bean = arg0.read(GrantComm.class,
							CellComAjaxResult.ParseType.GSON);
					String state = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(state)) {
						if (FlowConsts.STATUE_2.equals(state)) {
							// token失效
							ContextUtil.exitLogin(
									AuthoActivity.this, header);
						}
	//					list.get(position).remove("state");
	//					list.get(position).put("state","0");
						List<Grant> grant = bean.getBody();
						list.addAll(grant);
						myListViewAdapter.notifyDataSetChanged();
					}else{
						if (FlowConsts.STATUE_2.equals(state)) {
							// token失效
							ContextUtil.exitLogin(
									AuthoActivity.this, header);
							return;
						}else
							ShowMsg(msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg(getResources().getString(R.string.dataparsefail));
				}
			}
		});
	}
	
	// 授权
	public void autho(final String phone, final int position, final int state) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", phone);
		String flow;
		if(state == 1)
			flow = FlowConsts.DELGRANT;
		else
			flow = FlowConsts.AGREEGRANT;
		HttpHelper.getInstances(AuthoActivity.this).send(flow,
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
				try{
					Comm bean = arg0.read(Comm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									AuthoActivity.this, header);
						}
	
						if(state == 1){
							list.get(position).setState("0");		
						}else{
							list.get(position).setState("1");
						}
							
						myListViewAdapter.notifyDataSetChanged();
					}else{
						ShowMsg(msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg(getResources().getString(R.string.dataparsefail));
				}
			}
		});

	}

	@Subscribe(threadMode = ThreadMode.MainThread)
	// 在ui线程执行
	public void onUserEvent(FirstEvent event) {
		AuthoActivity.this.finish();
	}
	
	public void refreshTask() {
//		AbLogUtil.prepareLog(AuthoActivity.class);
//		AbTask mAbTask = new AbTask();
//		final AbTaskItem item = new AbTaskItem();
//		item.setListener(new AbTaskListListener() {
//			@Override
//			public List<?> getList() {
//				List<Grant> newList = null;
//				try {
//					Thread.sleep(1000);
//					newList = new ArrayList<Grant>();
////					Map<String, Object> map = null;
////
////					for (int i = 0; i < 5; i++) {
////						map = new HashMap<String, Object>();
////						map.put("phone", "1892359142"+i);
////						map.put("starttime", "2016-06-24 15:12:09");
////						map.put("state", "1");
////						newList.add(map);
////					}
//				} catch (Exception e) {
//				}
//				return newList;
//			}
//
//			@Override
//			public void update(List<?> paramList) {
//				
//				Log.e("list","list="+paramList);
//				//通知Dialog
////				mDialogFragment.loadFinish();
//				AbLogUtil.d(AuthoActivity.class, "返回", true);
//				List<Grant> newList = (List<Grant>) paramList;
//				list.clear();
//				if (newList != null && newList.size() > 0) {
//					list.addAll(newList);
//					myListViewAdapter.notifyDataSetChanged();
//					newList.clear();
//				}
//				mAbPullToRefreshView.onHeaderRefreshFinish();
//			}
//
//		});
//
//		mAbTask.execute(item);
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("phone", MyApplication.getInstances().getPhone());
		HttpHelper.getInstances(AuthoActivity.this).send(FlowConsts.GETGRANT,
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
				DismissProgressDialog();
				try{
					GrantComm bean = arg0.read(GrantComm.class,
							CellComAjaxResult.ParseType.GSON);
					String state = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(state)) {
						if (FlowConsts.STATUE_2.equals(state)) {
							ContextUtil.exitLogin(
									AuthoActivity.this, header);
						}
						list.clear();
						List<Grant> grant = bean.getBody();
						list.addAll(grant);
						myListViewAdapter.notifyDataSetChanged();
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}else{
						if (FlowConsts.STATUE_2.equals(state)) {
							// token失效
							ContextUtil.exitLogin(
									AuthoActivity.this, header);
							return;
						}else
							ShowMsg(msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg("数据解析失败！");
				}
			}
		});
	}
	

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				List<Grant> newList = (List<Grant>) paramList;
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullToRefreshView.onFooterLoadFinish();

			}

			@Override
			public List<?> getList() {
				List<Grant> newList = null;
				try {
					newList = new ArrayList<Grant>();
					Map<String, Object> map = null;

//					for (int i = 0; i < 5; i++) {
//						map = new HashMap<String, Object>();
//						map.put("phone", "1892359142"+i);
//						map.put("starttime", "2016-06-24 15:12:09");
//						map.put("state", "1");
//						newList.add(map);
//					}

				} catch (Exception e) {
					
				}
				return newList;
			};
		});

		mAbTask.execute(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 100 && resultCode == RESULT_OK){
			mAbPullToRefreshView.headerRefreshing();
			refreshTask();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		refreshTask();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		Log.e("load","load");
		loadMoreTask();
	}

}
