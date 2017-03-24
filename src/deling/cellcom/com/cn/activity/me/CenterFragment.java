package deling.cellcom.com.cn.activity.me;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.WebViewActivity;
import deling.cellcom.com.cn.activity.base.FragmentBase;
import deling.cellcom.com.cn.activity.main.LaunchActivity;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
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
public class CenterFragment extends FragmentBase implements OnClickListener {
	private FinalBitmap finalBitmap;
	private RelativeLayout mLyinfo;
	private LinearLayout mShare;// 分享
	private LinearLayout mSetting, llVisitor, llCoupon, llFeedback, llQuestion, llAbout, llLogout;
	private ImageView ivMyKey, ivApplyKey, ivOpenlog, ivAccredit;
	private TextView tvPhone;
	public final static int CHANGEHEADER_REQUEST_CODE = 1 * 111;

	public CircleImageView cImg;// 头像
	private Activity activity;
	private String userid;
	private String path;// 头像地址
	
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
//				ActionSheet.showSheet(activity, (OnActionSheetSelected)activity, (OnCancelListener)activity, "1");
			}
		});

	}

	private void initView(View view) {
		tvPhone = (TextView) view.findViewById(R.id.phone);
		ivMyKey = (ImageView) view.findViewById(R.id.mykey);
		ivOpenlog = (ImageView) view.findViewById(R.id.openlog);
		ivAccredit = (ImageView) view.findViewById(R.id.accredit);
		// 申请钥匙
		ivApplyKey = (ImageView) view.findViewById(R.id.applykey);
		
		mLyinfo = (RelativeLayout) view.findViewById(R.id.personal_info);
		// 分享
		mShare = (LinearLayout) view.findViewById(R.id.personal_share);
		// 设置
		mSetting = (LinearLayout) view.findViewById(R.id.personal_setting);
		// 咨询服务
		llCoupon = (LinearLayout) view.findViewById(R.id.coupon);
		// 问题反馈
		llFeedback = (LinearLayout) view.findViewById(R.id.feedback);
		// 常见问题
		llQuestion = (LinearLayout) view.findViewById(R.id.question);
		// 关于我们
		llAbout = (LinearLayout) view.findViewById(R.id.personal_about);
		// 退出登录
		llLogout = (LinearLayout) view.findViewById(R.id.personal_logout);
		llVisitor = (LinearLayout) view.findViewById(R.id.visitor);

		
		// personal_wdqb.setVisibility(view.GONE);
		// personal_zlfw.setVisibility(View.GONE);
		// mLyinfo.setOnClickListener(this);
		ivMyKey.setOnClickListener(this);
		ivOpenlog.setOnClickListener(this);
		ivAccredit.setOnClickListener(this);
		llVisitor.setOnClickListener(this);
		mSetting.setOnClickListener(this);
		mShare.setOnClickListener(this);
		mLyinfo.setOnClickListener(this);
		ivApplyKey.setOnClickListener(this);
		llCoupon.setOnClickListener(this);
		llFeedback.setOnClickListener(this);
		llQuestion.setOnClickListener(this);
		llAbout.setOnClickListener(this);
		llLogout.setOnClickListener(this);
		cImg = (CircleImageView) view.findViewById(R.id.main_found_lv_item_img);

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
		
		String phone = PreferencesUtils.getString(activity, "phone", "");
		if(phone.length() == 11)
			phone = phone.substring(0, 3) + "****" +  phone.substring(7, 11);
		tvPhone.setText(phone);
		
		mLyinfo.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				(int) (ContextUtil.getWidth(activity)/2.16)));
	}
	
	private void openShare(){
		ActionSheet.showSheet(activity, (OnActionSheetSelected)activity, (OnCancelListener)activity, "3");
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mykey:
//			OpenActivity(MyKeyActivity.class);
			OpenActivity(MyKeyTwoActivity.class);
			break;
		case R.id.openlog:  //开门记录
		{
//			Intent intent = new Intent(activity,WebViewActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("title", "开门记录");
//			bundle.putString("url", "http://falago.cn/door/static/dlkm/open_record3.html");
//			intent.putExtras(bundle);
//			startActivity(intent);
			ToastUtils.show(activity, "该版本暂时无法使用此功能");
		}
			break;
		case R.id.accredit://家属授权
			int keynum = MyApplication.getInstances().getKeyNum();
			if(MyApplication.getInstances().getUserType() == 1 && keynum > 0){
				OpenActivity(AuthoActivity.class);
			}else if(keynum < 1){
				ShowMsg("抱歉，您暂无可授权钥匙");
			}else
				ShowMsg("您需要拥有业主身份，才能进行授权操作");
			
			break;
		case R.id.visitor:
			ToastUtils.show(activity, "该版本暂时无法使用此功能");
			break;
		case R.id.personal_share:
			openShare();
			break;
		case R.id.personal_setting:
		{
			Intent intent = new Intent(activity, SettingActivity.class);
			activity.startActivityForResult(intent, 201);
		}
			break;
		case R.id.applykey:	// 申请钥匙
			OpenActivity(AskKeyActivity.class);
			break;
		case R.id.coupon:	// 优惠券 
			OpenActivity(CouponActivity.class);
			break;
		case R.id.feedback:	// 问题反馈
			OpenActivity(FaultActivity.class);
			break;
		case R.id.question:	// 常见问题
		{
			Intent intent = new Intent(activity,WebViewActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("title", "常见问题");
			bundle.putString("url", "http://www.ideling.com/door/static/dlkm/com_question2.html");
			intent.putExtras(bundle);
			startActivity(intent);
		}
			break;
		case R.id.personal_about:	// 关于我们
			OpenActivity(AboutActivity.class);
			break;
		case R.id.personal_logout:	// 退出登录
			AlertDialog.Builder builder = new Builder(activity);
			builder.setTitle("提示")
				.setMessage("是否确定退出登录?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
								new String[][] { { "userid", "" } });
						Intent mIntent = new Intent(activity, LoginActivity.class);			
						startActivity(mIntent);
						MyApplication.getInstances().getActivities().clear();
						MobclickAgent.onProfileSignOff();
						activity.finish();
					}
				})
				.setNegativeButton("取消", null)
				.create().show();
			break;
		default:
			break;
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}