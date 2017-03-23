package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import com.squareup.picasso.Picasso;

import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.Coupon;
import deling.cellcom.com.cn.bean.CouponComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;

/**
 * 优惠券
 * 
 * @author xpw
 * 
 */
public class CouponActivity extends FragmentActivityBase{
	private RadioGroup rgTab;
	private RadioButton tvYSC;
	private RadioButton tvYGQ;
	private ListView lvCollect;
	private ListView lvOverdue;
	private Header header;
	private List<String> mCouponSCList;
	private List<String> mCouponGQList;
	private ImageAdapter scAdapter;
	private ImageAdapter gqAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
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
		header = (Header) findViewById(R.id.header);
		rgTab = (RadioGroup) findViewById(R.id.tabs_rg);
		tvYSC = (RadioButton) findViewById(R.id.ysc);
		tvYGQ = (RadioButton) findViewById(R.id.ygq);
		lvCollect = (ListView) findViewById(R.id.lv_collect);
		lvOverdue = (ListView) findViewById(R.id.lv_overdue);
	}

	private void InitListeners() {
		rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.ysc:
					lvCollect.setVisibility(View.VISIBLE);
					lvOverdue.setVisibility(View.GONE);
					break;
				case R.id.ygq:
					lvCollect.setVisibility(View.GONE);
					lvOverdue.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
					
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
					CouponActivity.this.finish();
				}
			});
		header.setTitle("优惠券", null);
		
		mCouponGQList = new ArrayList<String>();
		mCouponSCList = new ArrayList<String>();
		
//		mCouponSCList.add("http://p0.so.qhimg.com/bdr/_240_/t017c09bf12c1814bd0.jpg");
//		mCouponSCList.add("http://p3.so.qhimg.com/bdr/_240_/t013e198e3d8cd8b848.jpg");
//		mCouponSCList.add("http://p1.so.qhimg.com/bdr/_240_/t013adef590504f7791.jpg");
//
//		mCouponGQList.add("http://p0.so.qhimg.com/bdr/_240_/t01caa4447495fe0905.jpg");
//		mCouponGQList.add("http://p3.so.qhimg.com/bdr/_240_/t01c7a66becd635c6d3.jpg");
		
		scAdapter = new ImageAdapter(CouponActivity.this, mCouponSCList);
		lvCollect.setAdapter(scAdapter);
		gqAdapter = new ImageAdapter(CouponActivity.this, mCouponGQList);
		lvOverdue.setAdapter(gqAdapter);
		
		getData();
	}

	// 获取申请记录
	public void getData() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(CouponActivity.this).send(FlowConsts.CHECKDISCOUNT,
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
					CouponComm bean = arg0.read(CouponComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									CouponActivity.this, header);
						}
						List<Coupon> coupon = bean.getBody();
						for(int i=0;i<coupon.size();i++){
							String zt = coupon.get(i).getState();
							String url = coupon.get(i).getGgimgeurl();
							if(zt.equals("1")){
								mCouponSCList.add(url);
							}else{
								mCouponGQList.add(url);								
							}
						}
						tvYSC.setText("已收藏("+mCouponSCList.size()+")");
						tvYGQ.setText("已过期("+mCouponSCList.size()+")");
						scAdapter.notifyDataSetChanged();
						gqAdapter.notifyDataSetChanged();
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									CouponActivity.this, header);
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

	public class ImageAdapter extends BaseAdapter {  
	    
	    protected static final int SUCCESS_GET_IMAGE = 0;  
	    private Context context;  
	    private List<String> list;  
	    private LayoutInflater mInflater;  
	  
	    
	    public ImageAdapter(Context context, List<String> list) {  
	        this.context = context;  
	        this.list = list;  
	  
	        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        return list.size();  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	        return list.get(position);  
	    }  
	  
	    @Override  
	    public long getItemId(int position) {  
	        return position;  
	    }  
	  
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
	        ViewHolder viewHolder = null; 
	        if (convertView == null) {
				viewHolder = new ViewHolder();
	        	convertView = mInflater.inflate(R.layout.listview_img_item, null);  
	        	viewHolder.ivCoupon = (ImageView) convertView.findViewById(R.id.item_image);
				convertView.setTag(viewHolder);
	        } else {  
	        	viewHolder = (ViewHolder) convertView.getTag();  
	        }
	        Picasso.with(context).load(list.get(position)).placeholder(R.drawable.coupon_load)
	        	.error(R.drawable.coupon_load).into(viewHolder.ivCoupon);
	  
	        return convertView;  
	    }

		class ViewHolder{
			private ImageView ivCoupon;
		}
	} 
}
