package deling.cellcom.com.cn.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jpush.a.a.ac;

import android.app.Activity;
import android.graphics.Color;
import android.media.Rating;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.bean.AreaNotice;
import deling.cellcom.com.cn.bean.SvRecord;
import deling.cellcom.com.cn.db.BaseDataManager;
public class FragCenterRecAdapter extends BaseAdapter {
	private Activity activity;
	List<SvRecord> records = new ArrayList<SvRecord>();
	public FragCenterRecAdapter(Activity activity, List<SvRecord> records){
		this.activity = activity;
		this.records = records;
	}
	
	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public SvRecord getItem(int position) {
		return records.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(view==null||view.getTag(R.drawable.ic_launcher+position)==null){
			view=LayoutInflater.from(activity).inflate(R.layout.frag_center_rec_item, null);
			ViewHolder holder=new ViewHolder();
			findView(view, holder);
			view.setTag(R.drawable.ic_launcher+position, holder);
		}
		viewHolder=(ViewHolder) view.getTag(R.drawable.ic_launcher+position);
		SvRecord record=records.get(position);
		bindView(viewHolder,record,position);
		return view;
	}
	private void findView(View view, ViewHolder holder) {
		holder.tvName=(TextView) view.findViewById(R.id.tv_name);
		holder.tvDate=(TextView) view.findViewById(R.id.tv_date);
		holder.rtIntent=(RatingBar) view.findViewById(R.id.rt_intention);
	}
	private void bindView(final ViewHolder holder,final SvRecord record,final int position) {
		 if(record!=null){
			Date date = record.getDate();
			DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
			holder.tvDate.setText(df3.format(date));
			
			holder.tvName.setText(record.getName());
			holder.rtIntent.setRating(record.getRating());
			 
			
		 }
	}
	private class ViewHolder{
		TextView tvName;
		TextView tvDate;
		RatingBar rtIntent;
	}
}
