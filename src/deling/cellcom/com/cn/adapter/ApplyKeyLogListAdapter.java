package deling.cellcom.com.cn.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.bean.ApplyLog;

public class ApplyKeyLogListAdapter extends BaseAdapter {
	private Context context;
	private List<ApplyLog> list = null;
	
	public ApplyKeyLogListAdapter(Context context, List<ApplyLog> list){
		this.context = context;
		this.list = list;
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
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder=null;
		if(view==null){
			view=LayoutInflater.from(context).inflate(R.layout.apply_log_item, null);
			holder=new ViewHolder();
			holder.tvDate=(TextView) view.findViewById(R.id.date);
			holder.tvPlace=(TextView) view.findViewById(R.id.place);
			holder.tvState=(TextView) view.findViewById(R.id.state);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}
		String data = list.get(position).getLogtime();
//		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if(data != null && !data.equals(""))
//			data = df3.format(Long.valueOf(data));
		holder.tvDate.setText(data);
		holder.tvPlace.setText(list.get(position).getArea());
		final int state = Integer.valueOf(list.get(position).getState());
		switch (state) {
		case 0:
			holder.tvState.setText("审核中");
			holder.tvState.setTextColor(context.getResources().getColor(R.color.yellow_dark));
			break;
		case 1:
			holder.tvState.setText("已通过");
			holder.tvState.setTextColor(context.getResources().getColor(R.color.green));
			break;
		case 3:
			holder.tvState.setText("未通过");
			holder.tvState.setTextColor(context.getResources().getColor(R.color.red));
			break;
		case 4:
			holder.tvState.setText("临时授权");
			holder.tvState.setTextColor(context.getResources().getColor(R.color.red));
			break;
		default:
			break;
		}
		
		return view;
	}
	private class ViewHolder{
		TextView tvDate;
		TextView tvPlace;
		TextView tvState;
	}
}
