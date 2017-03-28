package deling.cellcom.com.cn.activity.me;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.deling.R.string;

import cn.jpush.a.a.a;

import com.squareup.picasso.Picasso;
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
import deling.cellcom.com.cn.widget.popupwindow.AlertDialogPopupWindow;

/**
 * 个人
 * 
 * @author wma
 * 
 */
public class PersonFm extends FragmentBase implements OnClickListener {
	private FinalBitmap finalBitmap;
	private LinearLayout llAvatar, llNick, llSex, llUpdatePwd, llLogout;
	private TextView tvNick, tvSex, tvPhone, tvShopname, tvShopaddr, tvTitle;
	public final static int CHANGEHEADER_REQUEST_CODE = 1 * 111;
	public final static int UPDATENICK_REQUEST_CODE = 101;
	public final static int UPDATESEX_REQUEST_CODE = 102;

	public CircleImageView cImg;// 头像
	private Activity activity;
	private String path;// 头像地址
	private AlertDialog myDialog = null;
	
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
		View view = inflater.inflate(R.layout.fragment_personal, container, false);
		initView(view);
		initListener();
		initData();
		return view;
	}

	private void initListener() {
		cImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ActionSheet.showSheet(activity, (OnActionSheetSelected)activity, (OnCancelListener)activity, "1");
			}
		});

	}

	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.title);
		tvNick = (TextView) view.findViewById(R.id.nick);
		tvSex = (TextView) view.findViewById(R.id.sex);
		tvPhone = (TextView) view.findViewById(R.id.phone);
		tvShopname = (TextView) view.findViewById(R.id.shopname);
		tvShopaddr = (TextView) view.findViewById(R.id.shopaddr);
		
		// 昵称
		llNick = (LinearLayout) view.findViewById(R.id.ll_nick);
		// 性别
		llSex = (LinearLayout) view.findViewById(R.id.ll_sex);
		// 修改密码
		llUpdatePwd = (LinearLayout) view.findViewById(R.id.ll_updatepwd);
		// 退出登录
		llLogout = (LinearLayout) view.findViewById(R.id.personal_logout);

		
		llNick.setOnClickListener(this);
		llSex.setOnClickListener(this);
		llUpdatePwd.setOnClickListener(this);
		llLogout.setOnClickListener(this);
		cImg = (CircleImageView) view.findViewById(R.id.avatar);

		finalBitmap = FinalBitmap.create(activity);
	}

	private void initData() {
		tvTitle.setText(getResources().getString(string.main_person));
		String avatar = MyApplication.getInstances().getAvatar();
		//改为默认头像
//		if(avatar != null && !avatar.equals(""))
//			Picasso.with(activity).load(MyApplication.getInstances().getAvatar())
//				.placeholder(R.drawable.logo).into(cImg);
//		else
			Picasso.with(activity).load(R.drawable.touxiang2).into(cImg);
		
//		String phone = PreferencesUtils.getString(activity, "phone", "");
//		if(phone.length() == 11)
//			phone = phone.substring(0, 3) + "****" +  phone.substring(7, 11);
//		tvPhone.setText(phone);
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
		case R.id.ll_nick: //修改昵称
		{
			Intent intent = new Intent(activity, UpdateActivity.class);
			intent.putExtra("title", "修改昵称");
			intent.putExtra("type", 1);
			intent.putExtra("info", tvNick.getText().toString());
			startActivityForResult(intent, UPDATENICK_REQUEST_CODE);
//			final EditText editText = new EditText(activity);
//			myDialog = new AlertDialog.Builder(activity).create();
//			myDialog.show();
//			myDialog.getWindow().setContentView(R.layout.app_alertdialog_edit);
//			TextView tvTitle =  (TextView) myDialog.getWindow().findViewById(R.id.tv_title);
//			TextView tvContent =  (TextView) myDialog.getWindow().findViewById(R.id.tv_content);
//			Button btSetting =  (Button) myDialog.getWindow().findViewById(R.id.btn_ok);
//			Button btCancel =  (Button) myDialog.getWindow().findViewById(R.id.btn_cancel);
//			tvTitle.setText("修改昵称");
//			tvContent.setText(tvNick.getText());
//			btSetting.setText("保存");
//			btCancel.setText("取消");
//			btSetting.setOnClickListener(new View.OnClickListener() {  
//                 @Override  
//                 public void onClick(View v) {  
//                	 
//                 }  
//            });   
//			btCancel.setOnClickListener(new View.OnClickListener() {  
//                 @Override  
//                 public void onClick(View v) {  
//                     myDialog.dismiss();  
//                 }  
//            });
			
			
//			myDialog = new AlertDialog.Builder(activity).setTitle("修改昵称").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
//				.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						String nickString = editText.getText().toString();
//						if (TextUtils.isEmpty(nickString)) {
//							ToastUtils.show(activity, "亲,昵称不能为空哦~");
//							return;
//						}
////						updateRemoteNick(nickString);
//						myDialog.dismiss();
//					}
//				}).setNegativeButton("取消", null).show();
		}
			break;
		case R.id.ll_sex:  //修改性别
		{
			Intent intent = new Intent(activity, UpdateActivity.class);
			intent.putExtra("title", "修改性别");
			intent.putExtra("type", 2);
			intent.putExtra("info", tvSex.getText().toString());
			startActivityForResult(intent, UPDATESEX_REQUEST_CODE);
//			final EditText editText = new EditText(activity);
//			myDialog = new AlertDialog.Builder(activity).setTitle("修改性别").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
//					.setPositiveButton("保存", new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							String nickString = editText.getText().toString();
//							if (TextUtils.isEmpty(nickString)) {
//								ToastUtils.show(activity, "亲,性别不能为空哦~");
//								return;
//							}
////							updateRemoteNick(nickString);
//							myDialog.dismiss();
//						}
//					}).setNegativeButton("取消", null).show();
		}
			break;
		case R.id.ll_updatepwd:  //修改密码
			
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 11111){
			if(requestCode == UPDATENICK_REQUEST_CODE)
				tvNick.setText(data.getStringExtra("info"));
			else if(requestCode == UPDATESEX_REQUEST_CODE)
				tvSex.setText(data.getStringExtra("info"));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}