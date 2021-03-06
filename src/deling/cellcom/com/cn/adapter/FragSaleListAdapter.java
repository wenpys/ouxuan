package deling.cellcom.com.cn.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.squareup.picasso.Picasso;

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

public class FragSaleListAdapter extends BaseAdapter {
	private Activity activity;
	List<Map<String, String>> records = new ArrayList<Map<String, String>>();
	public FragSaleListAdapter(Activity activity, List<Map<String, String>> records){
		this.activity = activity;
		this.records = records;
	}
	
	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public Map<String, String> getItem(int position) {
		return records.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(view==null){
			view=LayoutInflater.from(activity).inflate(R.layout.frag_sale_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.imIcon=(ImageView) view.findViewById(R.id.icon);
			viewHolder.tvTitle=(TextView) view.findViewById(R.id.title);
			viewHolder.imContent=(ImageView) view.findViewById(R.id.image);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) view.getTag();
		}
		Map<String, String> record=records.get(position);
		bindView(viewHolder,record,position);
		return view;
	}

	private void bindView(final ViewHolder holder,final Map<String, String> record,final int position) {
		 if(record!=null){
			String title = record.get("title");
			String icon = record.get("icon");
			String image = record.get("image");

			if(icon != null && !icon.equals("")){
				Picasso.with(activity).load(icon).into(holder.imIcon);
			}
			if(title != null && !title.equals("")){
				holder.tvTitle.setText(title);
			}
			if(image != null && !image.equals("")){
				Picasso.with(activity).load(image).into(holder.imContent);
			}			
		 }
	}
	private class ViewHolder{
		ImageView imIcon;
		TextView tvTitle;
		ImageView imContent;
	}
}
