package deling.cellcom.com.cn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.main.GuideActivity;

public class AdImageAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private int[] resId = {R.drawable.icon_bg_guide1,R.drawable.icon_bg_guide2,R.drawable.icon_bg_guide3,R.drawable.icon_bg_guide4};
	public AdImageAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public AdImageAdapter(Context context, int[] resId) {
		mContext = context;
		this.resId = resId;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return resId.length;
	}

	@Override
	public Object getItem(int position) {
		return resId[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_guide_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.ivBg = (ImageView) convertView.findViewById(R.id.iv_bg);
//			viewHolder.tvSkip=(TextView) convertView.findViewById(R.id.tv_skip);
			viewHolder.ivStart=(ImageView) convertView.findViewById(R.id.iv_start);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.ivBg.setBackgroundResource(resId[position]);
		if(position==getCount()-1){
			viewHolder.ivStart.setVisibility(View.VISIBLE);
//			viewHolder.tvSkip.setVisibility(View.GONE);
		}else{
			viewHolder.ivStart.setVisibility(View.GONE);
//			viewHolder.tvSkip.setVisibility(View.VISIBLE);
		}
		viewHolder.ivStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(position==resId.length-1){
					((GuideActivity)mContext).backToMainDesk();
				}
			}
		});
//		viewHolder.tvSkip.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				((GuideActivity)mContext).backToMainDesk();
//			}
//		});
		return convertView;
	}

	static class ViewHolder{
		private ImageView ivBg;
		private TextView tvSkip;
		private ImageView ivStart;
	}
}
