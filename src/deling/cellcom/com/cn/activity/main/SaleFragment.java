package deling.cellcom.com.cn.activity.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.deling.R.drawable;
import cellcom.com.cn.deling.R.string;

import com.ab.fragment.AbLoadDialogFragment;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.WebViewActivity;
import deling.cellcom.com.cn.activity.base.FragmentBase;
import deling.cellcom.com.cn.activity.main.CallActivity;
import deling.cellcom.com.cn.activity.main.LaunchActivity;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
import deling.cellcom.com.cn.adapter.FragCenterRecAdapter;
import deling.cellcom.com.cn.adapter.FragNoticeAdapter;
import deling.cellcom.com.cn.adapter.FragSaleListAdapter;
import deling.cellcom.com.cn.bean.AreaNotice;
import deling.cellcom.com.cn.bean.SvRecord;
import deling.cellcom.com.cn.utils.CheckNetworkState;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.utils.PreferencesUtils;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.ActionSheet;
import deling.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import deling.cellcom.com.cn.widget.CircleImageView;

/**
 * 互动营销
 * 
 * @author xpw
 * 
 */
public class SaleFragment extends FragmentBase implements
OnHeaderRefreshListener, OnFooterLoadListener {
	private TextView tvTile;
	private ImageView imQrcode;
	private ImageView imStateLight;
	private ListView listView;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment mDialogFragment = null;

	private Activity activity;
	private String userid;
	private List<Map<String, String>> records = new ArrayList<Map<String, String>>();
	private FragSaleListAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		CheckNetworkState.checkNetworkState(activity);
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sale, container, false);
		initView(view);
		initListener();
		userid = cellcom.com.cn.util.SharepreferenceUtil
				.getDate(activity, "userid");
		initData();
		return view;
	}

	private void initListener() {
		imQrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//打开活动内容
				OpenActivity(SaleDetailActivity.class);
			}
		});
	}

	private void initView(View view) {
		tvTile = (TextView) view.findViewById(R.id.title);
		imQrcode = (ImageView) view.findViewById(R.id.leftimg);
		imStateLight = (ImageView) view.findViewById(R.id.statelight);
		listView = (ListView) view.findViewById(R.id.listview);
		
		adapter = new FragSaleListAdapter(activity, records);
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

	private void initData() {
		tvTile.setText(getResources().getString(string.main_sale));
		imQrcode.setImageResource(drawable.qrcode);
		
		Map<String, String> record = new HashMap<String, String>();
		record.put("title","预定送礼");
		record.put("image","https://gw.alicdn.com/tps/TB1YPFVLpXXXXcJXVXXXXXXXXXX-750-263.jpg_760x760Q75.jpg");
		records.add(record);

		record = new HashMap<String, String>();
		record.put("title","摇红包");
		record.put("image","http://p3.so.qhmsg.com/bdr/_240_/t01311577c64cea4fd8.jpg");
		records.add(record);

		record = new HashMap<String, String>();
		record.put("title","现场抽奖");
		record.put("image","http://p0.so.qhimgs1.com/bdr/_240_/t01cfe29dbfbbdd4f1c.jpg");
		records.add(record);
		adapter.notifyDataSetChanged();
	}
	
	private void getPersonInfo() {
		/*CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(getActivity()).send(
				FlowConsts.QUERYDOCTORDETAILS, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				DismissProgressDialog();
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				DismissProgressDialog();
				PersonData pinfo = arg0.read(PersonData.class,
						CellComAjaxResult.ParseType.GSON);
				PersonInfo info = pinfo.getBody();
				if (FlowConsts.STATUE_1.equals(pinfo.getReturncode())
						&& info != null) {
					PreferencesUtils.putString(getActivity(), userid
							+ "personinfo", arg0.getResult());			
					cImg.setBorderWidth(2);

					if (TextUtils.isEmpty(info.getYstx())) {
						cImg.setImageResource(R.drawable.avatar_default);
					} else {
						finalBitmap.display(cImg, info.getYstx());
						finalBitmap
								.configLoadfailImage(R.drawable.avatar_default);
					}

					path = info.getYstx();
				} else if (!FlowConsts.STATUE_1.equals(pinfo
						.getReturncode())) {
					if (FlowConsts.STATUE_2.equals(pinfo
							.getReturncode())) {
						// token失效
						ContextUtil.exitLogin(activity, mLyinfo);
						return;
					}
				}
			}
		});*/
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		mAbPullToRefreshView.onFooterLoadFinish();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		mAbPullToRefreshView.onHeaderRefreshFinish();
	}
}