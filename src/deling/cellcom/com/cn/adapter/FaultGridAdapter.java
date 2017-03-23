package deling.cellcom.com.cn.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cellcom.com.cn.deling.R;

public class FaultGridAdapter extends BaseAdapter {
	private Context context;
	private List<String> info;
	private LayoutInflater inflater;
	private FinalBitmap finalBitmap;
	public FaultGridAdapter(Context mContext,List<String> list){
		this.context=mContext;
		this.info = list;
		this.inflater = LayoutInflater.from(context);
		finalBitmap = FinalBitmap.create(mContext);
	}
	@Override
	public int getCount() {
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		return info.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setList(List<String> list){
	    this.info=list;
	  }
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder=null;
		if(view==null||view.getTag(R.drawable.ic_launcher+position)==null){
			view=LayoutInflater.from(context).inflate(R.layout.item_fault_grid, null);
			holder=new ViewHolder();
			holder.imageView=(ImageView) view.findViewById(R.id.iv_image);
			view.setTag(R.drawable.ic_launcher+position, holder);
		}else{
			holder=(ViewHolder) view.getTag(R.drawable.ic_launcher+position);
		}
		if(TextUtils.isEmpty(info.get(position))){
			holder.imageView.setImageResource(R.drawable.img_add);        
	    }else if("loading".equals(info.get(position))){
	    	holder.imageView.setImageResource(R.drawable.app_addin_pic);   
	    }else{      
	    	finalBitmap.display(holder.imageView, info.get(position));		   
	    }
		return view;
	}
	private class ViewHolder{
		ImageView imageView;
	}
}
