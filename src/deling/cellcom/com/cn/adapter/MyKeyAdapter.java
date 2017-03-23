package deling.cellcom.com.cn.adapter;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

public class MyKeyAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, String>> infos;
	
	
	public MyKeyAdapter(Context context, List<Map<String, String>> infos) {
		super();
		this.context = context;
		this.infos = infos;
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
		ViewHolder viewHolder = null;
		if(view == null){
			view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mykey_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.ivPic=(ImageView) view.findViewById(R.id.iv_areapic);
			viewHolder.tvName=(TextView) view.findViewById(R.id.tv_areaname);
			view.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tvName.setText(infos.get(position).get("areaname"));
		return view;
	}
	
	private class ViewHolder{
		ImageView ivPic;
		TextView tvName;
	}
}
