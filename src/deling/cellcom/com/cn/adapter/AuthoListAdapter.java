package deling.cellcom.com.cn.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.me.AuthoActivity;
import deling.cellcom.com.cn.bean.Grant;

public class AuthoListAdapter extends BaseAdapter {
	private AuthoActivity mActivity;
	private List<Grant> list = null;
	
	public AuthoListAdapter(AuthoActivity mActivity, List<Grant> list){
		this.mActivity=mActivity;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Grant getItem(int position) {
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
			view=LayoutInflater.from(mActivity).inflate(R.layout.autho_item, null);
			holder=new ViewHolder();
			holder.tvPhone=(TextView) view.findViewById(R.id.phone);
			holder.tvKey=(TextView) view.findViewById(R.id.key);
			holder.tvExpiry=(TextView) view.findViewById(R.id.expiry);
			holder.tvDate=(TextView) view.findViewById(R.id.date);
			holder.tvState=(TextView) view.findViewById(R.id.state);
			view.setTag(holder);
		}else{
			holder=(ViewHolder) view.getTag();
		}

		holder.tvPhone.setText(list.get(position).getName()+" "+list.get(position).getPhone());
		holder.tvKey.setText(list.get(position).getGatename());
		String expiry = list.get(position).getValuetime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(expiry != null && !expiry.equals(""))
			expiry = df.format(Long.valueOf(expiry));
		holder.tvExpiry.setText(expiry);
		String data = list.get(position).getStarttime();
		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(data != null && !data.equals(""))
			data = df3.format(Long.valueOf(data));
		holder.tvDate.setText(data);
		final int state = Integer.valueOf(list.get(position).getState());
		switch (state) {
		case 0:
			holder.tvState.setText("已取消授权");
			holder.tvState.setTextColor(mActivity.getResources().getColor(R.color.red));
			break;
		case 1:
			holder.tvState.setText("授权成功");
			holder.tvState.setTextColor(mActivity.getResources().getColor(R.color.green_light));
			break;
		case 2:
			holder.tvState.setText("申请授权");
			holder.tvState.setTextColor(mActivity.getResources().getColor(R.color.orange));
			break;
		default:
			break;
		}
		
		return view;
	}
	private class ViewHolder{
		TextView tvPhone;
		TextView tvKey;
		TextView tvExpiry;
		TextView tvDate;
		TextView tvState;
	}
}
