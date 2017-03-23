package deling.cellcom.com.cn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

import com.dh.bluelock.object.LEDevice;

public class DeviceAdapter extends BaseAdapter {

	private List<LEDevice> list;
	private Context mContext;
	
	public DeviceAdapter(Context context){
		mContext = context;
		this.list = new ArrayList<LEDevice>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if( null == list){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(null ==view){
			view = LayoutInflater.from(mContext).inflate(
					R.layout.device_item, null);
		}
		LEDevice device = list.get(position);
		TextView deviceNameTxtView = (TextView) view.findViewById(R.id.deviceNameTxtView);
		TextView deviceMacTxtView = (TextView) view.findViewById(R.id.deviceMacTxtView);
		deviceNameTxtView.setText(device.getDeviceName());
		deviceMacTxtView.setText(device.getDeviceAddr());
		
		return view;
	}

	public List<LEDevice> getList() {
		return list;
	}

	public void setList(List<LEDevice> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	public void addDevice(LEDevice device){
		for( int i=0;i<list.size();i++){
			if( device.getDeviceAddr().equals(list.get(i).getDeviceAddr())){
				return;
			}
		}
		this.list.add(device);
		this.notifyDataSetChanged();
	}

}
