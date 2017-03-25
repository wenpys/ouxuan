package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

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
 * 个人
 * 
 * @author wma
 * 
 */
public class CenterFragment extends FragmentBase implements
OnHeaderRefreshListener, OnFooterLoadListener {
	private FinalBitmap finalBitmap;
	private RelativeLayout mLyinfo;
	public ImageView cImg;// 头像
	private TextView tvName;
	private TextView tvStatus;
	private TextView tvSvcount;
	private TextView tvNorecord;
	private ListView listView;
	private LinearLayout llNotice;
	private TextView tvNotice;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbLoadDialogFragment mDialogFragment = null;
	public final static int CHANGEHEADER_REQUEST_CODE = 1 * 111;

	private Activity activity;
	private String userid;
	private String path;// 头像地址
	private List<SvRecord> records = new ArrayList<SvRecord>();
	private FragCenterRecAdapter adapter;
	
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
		View view = inflater.inflate(R.layout.fragment_center, container, false);
		initView(view);
		initListener();
		userid = cellcom.com.cn.util.SharepreferenceUtil
				.getDate(activity, "userid");
		initData();
		return view;
	}

	private void initListener() {
		cImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
//		tvNotice.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(activity, CallActivity.class));
//			}
//		});
	}

	private void initView(View view) {
		tvName = (TextView) view.findViewById(R.id.name);
		tvStatus = (TextView) view.findViewById(R.id.status);
		tvSvcount = (TextView) view.findViewById(R.id.svcount);
		tvNorecord = (TextView) view.findViewById(R.id.norecd);
		mLyinfo = (RelativeLayout) view.findViewById(R.id.personal_info);
		
		cImg = (ImageView) view.findViewById(R.id.main_found_lv_item_img);
		listView = (ListView) view.findViewById(R.id.listview);
		llNotice = (LinearLayout) view.findViewById(R.id.ll_notice);
		tvNotice = (TextView) view.findViewById(R.id.notice);
		
		llNotice.setVisibility(View.GONE);
		
		adapter = new FragCenterRecAdapter(activity, records);
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
		
		finalBitmap = FinalBitmap.create(activity);
	}

	private void initData() {
		String avatar = MyApplication.getInstances().getAvatar();
		//改为默认头像
//		if(avatar != null && !avatar.equals(""))
//			Picasso.with(activity).load(MyApplication.getInstances().getAvatar())
//				.placeholder(R.drawable.logo).into(cImg);
//		else
			Picasso.with(activity).load(R.drawable.logo).into(cImg);
		
		Random random = new Random();
		for(int i=0;i<3;i++){
			SvRecord record = new SvRecord();
			record.setName("张三"+i);
			record.setDate(new Date());
			record.setRating(random.nextInt(5));
			records.add(record);
		}
		tvNorecord.setVisibility(View.GONE);
		tvSvcount.setText(records.size()+"");
		adapter.notifyDataSetChanged();
			
			
		String phone = PreferencesUtils.getString(activity, "phone", "");
		if(phone.length() == 11)
			phone = phone.substring(0, 3) + "****" +  phone.substring(7, 11);
//		tvName.setText(phone);
		
		mLyinfo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (ContextUtil.getWidth(activity)/2.16)));
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