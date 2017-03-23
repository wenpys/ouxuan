package deling.cellcom.com.cn.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.umeng.socialize.utils.Log;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentBase;
import deling.cellcom.com.cn.adapter.FragNoticeAdapter;
import deling.cellcom.com.cn.bean.AreaNotice;
import deling.cellcom.com.cn.bean.Comm;
import deling.cellcom.com.cn.bean.Notice;
import deling.cellcom.com.cn.bean.NoticeComm;
import deling.cellcom.com.cn.bean.NoticeLogComm;
import deling.cellcom.com.cn.db.BaseDataManager;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.ToastUtils;

public class NoticeFragment extends FragmentBase implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	private Activity activity;
	private ListView listView;
	private FragNoticeAdapter adapter;
	private RelativeLayout rlDefault;
	private List<AreaNotice> notices;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment mDialogFragment = null;
	private String noticeLogIds ="-1";
	private String phone;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_notice, container, false);
		initView(view);
		initData();
		initListener();
		return view;
	}

	private void initView(View view) {
		listView = (ListView) view.findViewById(R.id.listview);
		rlDefault = (RelativeLayout) view.findViewById(R.id.rl_default);
		adapter = new FragNoticeAdapter(activity);
		listView.setAdapter(adapter);
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) view
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

	private void initListener() {
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// }
		// });
	}

	public void setNoticeNum(int count) {
		((MainActivity) activity).setNoticeMsg(count);
	}

	private void initData() {
		// for(int i=0;i<10;i++){
		// AreaNotice areaNotice = new AreaNotice((i+1)+"", "金碧世纪花园",
		// "得令APP新版上线", "得令开门每天给你惊喜，" +
		// "本周推出美食商家优惠券赠送，开门记得用得令哦", "147027664"+i+"174", false);
		// BaseDataManager.getInstance(activity).saveAreaNotice(areaNotice);
		// }
		notices = BaseDataManager.getInstance(activity).getAllAreaNotice();
		if(notices==null){
			BaseDataManager.getInstance(activity).deleteAllNotice();
			notices = new ArrayList<AreaNotice>();
		}
		adapter.addAllData(notices);
		adapter.notifyDataSetChanged();
		if (adapter.getCount() <= 0) {
			rlDefault.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			rlDefault.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}

		setNoticeNum(BaseDataManager.getInstance(activity).getAreaNoticeNum());
		String maxid = BaseDataManager.getInstance(activity).getMaxAreaNoticeId();
		getData(maxid);
	}

	// 获取小区通知
	public void getData(String maxid) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("noticeid", maxid);
		HttpHelper.getInstances(activity).send(FlowConsts.GETNOTICE,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						if (mAbPullToRefreshView != null)
							mAbPullToRefreshView.onHeaderRefreshFinish();
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						try {
							if (mAbPullToRefreshView != null)
								mAbPullToRefreshView.onHeaderRefreshFinish();
							NoticeComm bean = arg0.read(NoticeComm.class,
									CellComAjaxResult.ParseType.GSON);
							String st = bean.getReturncode();
							String msg = bean.getReturnmessage();
							if (FlowConsts.STATUE_1.equals(st)) {

								List<Notice> notice = bean.getBody();
								for (int i = 0; i < notice.size(); i++) {
									Notice not=notice.get(i);
									int userid=MyApplication.getInstances().getUserid();
									AreaNotice areaNotice = new AreaNotice(
											notice.get(i).getId(), notice
													.get(i).getAreaname(),
											notice.get(i).getTitle(), notice
													.get(i).getContent(),
											notice.get(i).getTime(), false,userid+"");
									BaseDataManager.getInstance(activity)
											.saveAreaNotice(areaNotice);
								}
								if (notice.size() > 0) {
									rlDefault.setVisibility(View.GONE);
									listView.setVisibility(View.VISIBLE);
									notices.clear();
									List<AreaNotice> tmp = new ArrayList<AreaNotice>();
									tmp = BaseDataManager.getInstance(activity)
											.getAllAreaNotice();
									notices.addAll(tmp);
									adapter.notifyDataSetChanged();
								}
								setNoticeNum(BaseDataManager.getInstance(activity).getAreaNoticeNum());
								getNoticeLog();
							} else {
								if (FlowConsts.STATUE_2.equals(st)) {
									// token失效
									ContextUtil.exitLogin(activity, rlDefault);
									return;
								} else
									ShowMsg(msg);
							}
						} catch (Exception e) {
							e.printStackTrace();
							ShowMsg(getResources().getString(
									R.string.dataparsefail));
						}
					}
				});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		mAbPullToRefreshView.onFooterLoadFinish();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		String maxid = BaseDataManager.getInstance(activity)
				.getMaxAreaNoticeId();
		getData(maxid);
	}

	// 获取点击过的小区的id
	public void getNoticeLog() {

		CellComAjaxParams params = new CellComAjaxParams();
		HttpHelper.getInstances(activity).send(FlowConsts.GETNOTICELOG, params,
				CellComAjaxHttp.HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						Log.e("MYTAG", strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						NoticeLogComm comm = null;
						try {
							comm = t.read(NoticeLogComm.class,
									CellComAjaxResult.ParseType.GSON);
							if (comm != null) {
								String status = comm.getReturncode();
								String msg = comm.getReturnmessage();
								if (!status.equals(FlowConsts.STATUE_1)) {
									Log.e("MYTAG", msg);
								}
								noticeLogIds= comm.getBody().getNoticeids();
//								adapter.refreshAdapter(noticeLogIds);
//								int len=notices.size();
//								if(len!=0){
//									for (int i = 0; i < len; i++) {
//										AreaNotice notice=notices.get(i);
//										if(!noticeLogIds.contains(notice.getId()+"")){
//											((MainActivity)activity).sum++;
//										}
//									}
//								}
//								String maxid = BaseDataManager.getInstance(activity)
//										.getMaxAreaNoticeId();
//								getData(maxid);
								BaseDataManager.getInstance(activity).changeNoticeState(noticeLogIds);
								notices = BaseDataManager.getInstance(activity).getAllAreaNotice();
								adapter.addAllData(notices);
								adapter.notifyDataSetChanged();
								setNoticeNum(BaseDataManager.getInstance(activity).getAreaNoticeNum());								
							}
						} catch (Exception e) {
							Log.e("MYTAG", "解析读过的小区信息id异常");
							return;
						}
					}
				});
	}

	// 更新点击过的消息
	public void updateNoticeIds(String ids) {
		CellComAjaxParams params = new CellComAjaxParams();
		params.put("noticeids", ids);
		HttpHelper.getInstances(activity).send(FlowConsts.UPDATENOTICELOG,
				params, CellComAjaxHttp.HttpWayMode.POST,
				new NetCallBack<CellComAjaxResult>() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						Log.e("MYTAG", strMsg);
					}

					@Override
					public void onSuccess(CellComAjaxResult t) {
						Comm comm=null;
						try {
							comm=t.read(Comm.class, CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ToastUtils.show(activity, "更新失败，请重新尝试");
							return;
						}
						if(!comm.getReturncode().equals(FlowConsts.STATUE_1)){
							Log.e("MYTAG", comm.getReturnmessage());
						}
					}
				});
	}
}
