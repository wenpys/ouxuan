package deling.cellcom.com.cn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.bean.Keyinfo;
public class HorizAdapter extends BaseAdapter {
	private List<Keyinfo> keys=new ArrayList<Keyinfo>();
	@Override
	public int getCount() {
		return keys.size();
	}
	public void setData(List<Keyinfo> keys){
		this.keys=keys;
		notifyDataSetChanged();
	}
	public void clearData(){
		keys.clear();
		notifyDataSetChanged();
	}
	@Override
	public Keyinfo getItem(int position) {
		return keys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(view==null||view.getTag(R.drawable.ic_launcher+position)==null){
			view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_horiz, null);
			ViewHolder holder=new ViewHolder();
			holder.tvAddr=(TextView) view.findViewById(R.id.tv_addr);
			view.setTag(R.drawable.ic_launcher+position, holder);
		}
		viewHolder=(ViewHolder) view.getTag(R.drawable.ic_launcher+position);
		Keyinfo keyinfo=keys.get(position);
		viewHolder.tvAddr.setText(keyinfo.getKeyname());
		return view;
	}
	private class ViewHolder{
		TextView tvAddr;
	}
}
