package deling.cellcom.com.cn.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.a.a.ac;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.bean.AreaNotice;
import deling.cellcom.com.cn.db.BaseDataManager;
public class FragNoticeAdapter extends BaseAdapter {
	private Activity activity;
	private List<AreaNotice> notices=new ArrayList<AreaNotice>();
	private boolean flag=true;
	private String noticeIds="-1";
	public FragNoticeAdapter(Activity activity){
		this.activity=activity;
	}
	public void updateData(int position) {
		if(!noticeIds.contains(notices.get(position).getId()+"")){
			noticeIds=noticeIds+","+notices.get(position).getId();
			notifyDataSetChanged();
			//更新服务器上的读过的消息ids
		}
		notices.get(position).setRead(true);
		BaseDataManager.getInstance(activity).updateAreaNotice(notices.get(position));
	}
	public void addAllData(List<AreaNotice> notices){
		this.notices=notices;
		notifyDataSetChanged();
	}
	public void addItem(AreaNotice notice){
		this.notices.add(0,notice);
		notifyDataSetChanged();
	}
	public void refreshAdapter(String noticeIds){
		if(!TextUtils.isEmpty(noticeIds)){
			this.noticeIds=noticeIds;
		}
		notifyDataSetChanged();
	}
	public String getNoticeIds(){
		return noticeIds;
	}
	@Override
	public int getCount() {
		return notices.size();
	}

	@Override
	public AreaNotice getItem(int position) {
		return notices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(view==null||view.getTag(R.drawable.ic_launcher+position)==null){
			view=LayoutInflater.from(activity).inflate(R.layout.frag_notice_item, null);
			ViewHolder holder=new ViewHolder();
			findView(view, holder);
			view.setTag(R.drawable.ic_launcher+position, holder);
		}
		viewHolder=(ViewHolder) view.getTag(R.drawable.ic_launcher+position);
		AreaNotice notice=notices.get(position);
		bindView(viewHolder,notice,position);
		if(position==0){
			viewHolder.tvTitleLine.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.tvTitleLine.setVisibility(View.VISIBLE);
		}
		return view;
	}
	private void findView(View view, ViewHolder holder) {
		holder.ivTitle=(ImageView) view.findViewById(R.id.iv_title);
		holder.tvTitle=(TextView) view.findViewById(R.id.tv_title);
		holder.tvTime=(TextView) view.findViewById(R.id.tv_time);
		holder.tvContent=(TextView) view.findViewById(R.id.tv_content);
		holder.llMain=(LinearLayout) view.findViewById(R.id.rl_main);
		holder.tvTitleLine=(TextView) view.findViewById(R.id.tv_title_line);
		holder.llContent=(LinearLayout) view.findViewById(R.id.ll_content);
	}
	private void bindView(final ViewHolder holder,final AreaNotice notice,final int position) {
		 if(notice!=null){
			String data = notice.getTime();
//			DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat df3 = new SimpleDateFormat("MM-dd HH:mm");
			if(data != null && !data.equals(""))
				data = df3.format(Long.valueOf(data));
			holder.tvTime.setText(data);
			
			 holder.tvTitle.setText(notice.getTitle());
			 holder.tvContent.setText(notice.getContent());
			 
			 if(notice.isRead()){
				 holder.ivTitle.setBackgroundResource(R.drawable.icon_notice_item_title_old); 
				 holder.llMain.setBackgroundColor(0xf4f4f4);
			 }else{
				 holder.ivTitle.setBackgroundResource(R.drawable.icon_notice_item_title_new);
				 holder.llMain.setBackgroundColor(new Color().WHITE); 
			 }
			holder.llMain.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					updateData(position);
					if(flag){
						flag=false;
						holder.tvContent.setEllipsize(null);
						holder.tvContent.setSingleLine(flag);
					}else{
						flag=true;
						holder.tvContent.setEllipsize(TextUtils.TruncateAt.END);
						holder.tvContent.setLines(2);
					}
				}
			});
		 }
	}
	private class ViewHolder{
		ImageView ivTitle;
		TextView tvTitle;
		TextView tvTime;
		TextView tvContent;
		LinearLayout llMain;
		TextView tvTitleLine;
		LinearLayout llContent;
	}
}
