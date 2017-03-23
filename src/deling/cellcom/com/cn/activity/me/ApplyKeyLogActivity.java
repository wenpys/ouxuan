package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import deling.cellcom.com.cn.adapter.ApplyKeyLogListAdapter;
import deling.cellcom.com.cn.bean.ApplyLog;
import deling.cellcom.com.cn.bean.ApplyLogComm;
import deling.cellcom.com.cn.bean.FirstEvent;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;

/**
 * 申请记录
 * 
 * @author xpw
 * 
 */
public class ApplyKeyLogActivity extends FragmentActivityBase implements OnHeaderRefreshListener, OnFooterLoadListener {
	private ListView lvAutho;
	private Header header;
	private ApplyKeyLogListAdapter myListViewAdapter = null;
	private List<ApplyLog> list = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment  mDialogFragment = null;
	private final int requestCode=1212;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_log);
		MyApplication.getInstances().getActivities().add(this);
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
		lvAutho.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ApplyLog applyLog=list.get(position);
				Intent intent=new Intent(ApplyKeyLogActivity.this,KeyLogDetailsPassActivity.class);
				intent.putExtra("id", applyLog.getId());
				intent.putExtra("type", applyLog.getState());
				startActivityForResult(intent, requestCode);
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
					ApplyKeyLogActivity.this.finish();
				}
			});
		header.setTitle("申请记录", null);

		
		list = new ArrayList<ApplyLog>();
//		Map map = new HashMap<String,Object>();
//		map.put("logtime", "2016-06-24 15:12:09");
//		map.put("door", "乐意居花苑  B栋  803房");
//		map.put("state", "1");
//		list.add(map);
//
//		map = new HashMap<String,Object>();
//		map.put("logtime", "2016-06-25 12:22:09");
//		map.put("door", "乐意居花苑  B栋  803房");
//		map.put("state", "0");
//		list.add(map);
//
//		map = new HashMap<String,Object>();
//		map.put("logtime", "2016-07-11 05:32:29");
//		map.put("door", "乐意居花苑  B栋  803房");
//		map.put("state", "2");
//		list.add(map);
		
		myListViewAdapter = new ApplyKeyLogListAdapter(this, list);
		lvAutho.setAdapter(myListViewAdapter);
		
		getData();
	}

	// 获取申请记录
	public void getData() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(ApplyKeyLogActivity.this).send(FlowConsts.CHECKAPPLYKEY,
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
					ApplyLogComm bean = arg0.read(ApplyLogComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						List<ApplyLog> applylog = bean.getBody();
						list.addAll(applylog);
						myListViewAdapter.notifyDataSetChanged();
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									ApplyKeyLogActivity.this, header);
							return;
						}
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
		ApplyKeyLogActivity.this.finish();
	}
	
	public void refreshTask() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(ApplyKeyLogActivity.this).send(FlowConsts.CHECKAPPLYKEY,
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
				mAbPullToRefreshView.onHeaderRefreshFinish();
				try{
					ApplyLogComm bean = arg0.read(ApplyLogComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									ApplyKeyLogActivity.this, header);
						}
						list.clear();
						List<ApplyLog> applylog = bean.getBody();
						list.addAll(applylog);
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
	

	public void loadMoreTask() {
		AbTask mAbTask = new AbTask();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
//				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
//				if (newList != null && newList.size() > 0) {
//					list.addAll(newList);
//					myListViewAdapter.notifyDataSetChanged();
//					newList.clear();
//				}
				mAbPullToRefreshView.onFooterLoadFinish();

			}

			@Override
			public List<?> getList() {
				List<ApplyLog> newList = null;
				try {
					newList = new ArrayList<ApplyLog>();
//					Map<String, Object> map = null;
//
//					for (int i = 0; i < 5; i++) {
//						map = new HashMap<String, Object>();
//						map.put("logtime", "2016-06-24 15:12:09");
//						map.put("door", "乐意居花苑  B栋  803房");
//						map.put("state", i/2+"");
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
	protected void onActivityResult(int requestcode, int resultCode, Intent data) {
		if(requestcode == 100 && resultCode == RESULT_OK){
			mAbPullToRefreshView.headerRefreshing();
			refreshTask();
		}else if(requestcode==requestCode&&resultCode==RESULT_OK){
			if(data!=null&&data.getStringExtra("data")!=null){
				if(data.getStringExtra("data").equals("0")){
					finish();
				}
			}
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
