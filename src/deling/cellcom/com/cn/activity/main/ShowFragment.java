package deling.cellcom.com.cn.activity.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import u.aly.cu;

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
import android.widget.GridView;
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
import deling.cellcom.com.cn.adapter.FragShowGridAdapter;
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
public class ShowFragment extends FragmentBase {
	private TextView tvTitle;
	private ImageView imQrcode;
	private ImageView imStateLight;
	private GridView gvContent;

	private Activity activity;
	private FragShowGridAdapter adapter;
	private List<Map<String, String>> datas = new ArrayList<Map<String,String>>();

	private long mLastTime = 0;
	private int curSelect = -1;
	
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
		View view = inflater.inflate(R.layout.fragment_show, container, false);
		initView(view);
		initListener();
		initData();
		return view;
	}

	private void initListener() {
		imQrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		gvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(curSelect != -1 && curSelect != position){
					ToastUtils.show(activity, "请先双击释放模块");
					return;
				}
                //发送鼠标左键功能
                long mCurTime = System.currentTimeMillis();
                
                //判断是否发生了双击
                if(mLastTime != 0 && mCurTime - mLastTime < 500) {
                    System.out.println("************双击************onclickTimes************"+"第"+position+"条");
                    mLastTime = mCurTime - 500;
    				adapter.setSeclection(-1);
    				adapter.notifyDataSetChanged();
    				curSelect = -1;
                }else {
                    mLastTime = mCurTime;
    				adapter.setSeclection(position);
    				adapter.notifyDataSetChanged();
    				curSelect = position;
                }
			}
		});
	}
    
	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.title);
		imQrcode = (ImageView) view.findViewById(R.id.leftimg);
		imStateLight = (ImageView) view.findViewById(R.id.statelight);
		gvContent = (GridView) view.findViewById(R.id.gridView);
		
		adapter = new FragShowGridAdapter(activity, datas);
		gvContent.setAdapter(adapter);
		
	}

	private void initData() {
		tvTitle.setText(getResources().getString(string.main_show));
		imQrcode.setVisibility(View.GONE);
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("title","外观");
		data.put("icon",R.drawable.waiguan+"");
		data.put("url","");
		datas.add(data);

		data = new HashMap<String, String>();
		data.put("title","内饰");
		data.put("icon",R.drawable.neishi+"");
		data.put("url","");
		datas.add(data);

		data = new HashMap<String, String>();
		data.put("title","动力");
		data.put("icon",R.drawable.dongli+"");
		data.put("url","");
		datas.add(data);

		data = new HashMap<String, String>();
		data.put("title","操控");
		data.put("icon",R.drawable.caokong+"");
		data.put("url","");
		datas.add(data);

		data = new HashMap<String, String>();
		data.put("title","安全");
		data.put("icon",R.drawable.anquan+"");
		data.put("url","");
		datas.add(data);
		
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
}