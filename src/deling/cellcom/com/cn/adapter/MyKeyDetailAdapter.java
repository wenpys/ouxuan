package deling.cellcom.com.cn.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.me.MyKeyDetialActivity;
import deling.cellcom.com.cn.bean.Keyinfo;

public class MyKeyDetailAdapter extends BaseAdapter {
	private Context context;
	private List<Keyinfo> infos=new ArrayList<Keyinfo>();
	
	
	public MyKeyDetailAdapter(Context context) {
		super();
		this.context = context;
	}
	public void addAllData(List<Keyinfo> infos){
		this.infos=infos;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return infos.size();
	}
	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if(view==null||view.getTag(R.drawable.ic_launcher+position)==null){
			view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mykeydetail_adapter, null);
			ViewHolder viewHolder=new ViewHolder();
			viewHolder.ivOpenDoor=(ImageView) view.findViewById(R.id.iv_opendoor);
			viewHolder.tvTime=(TextView) view.findViewById(R.id.tv_time);
			viewHolder.tvHavePwd=(TextView) view.findViewById(R.id.tv_havepwd);
			viewHolder.tvNoPwd=(TextView) view.findViewById(R.id.tv_nopwd);
			view.setTag(R.drawable.ic_launcher+position, viewHolder);
		}
		final ViewHolder holder=(ViewHolder) view.getTag(R.drawable.ic_launcher+position);
		holder.ivOpenDoor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				((MyKeyDetialActivity)context).openDoorByKey(infos.get(position));
				startAnim(holder.ivOpenDoor);
			}
		});
		Keyinfo info=infos.get(position);
		setData(holder,info);
		return view;
	}
	private void setData(ViewHolder holder, Keyinfo info) {
		holder.tvTime.setText("使用期限："+info.getValuetime());
		holder.tvHavePwd.setText("门禁名称："+info.getKeyname());
	}
	private class ViewHolder{
		ImageView ivOpenDoor;
		TextView tvTime;
		TextView tvHavePwd;
		TextView tvNoPwd;
	}
	public void startAnim(View view){
		AnimationSet set=new AnimationSet(true);
		ScaleAnimation animation=new ScaleAnimation(1, 0.5f, 1, 0.5f,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		animation.setDuration(100);
		set.addAnimation(animation);
		view.startAnimation(set);
	}
}
