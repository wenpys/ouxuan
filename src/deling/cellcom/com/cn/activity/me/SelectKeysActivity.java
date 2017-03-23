package deling.cellcom.com.cn.activity.me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.stream.Position;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.bean.RoomKey;
import deling.cellcom.com.cn.bean.ValueRoomKey;
import deling.cellcom.com.cn.bean.ValueRoomKeyComm;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.ContextUtil;
import deling.cellcom.com.cn.widget.Header;

/**
 * 
 * @author 选择钥匙
 * 
 */
public class SelectKeysActivity extends FragmentActivityBase {

	private Header header;
	private ExpandableListView expandableListView;
	private String[] roomids;
	private List<String> groupArray = new  ArrayList<String>();  
	private List<List<String>> childArray = new  ArrayList<List<String>>();  
	private List<String> curSelArray = new  ArrayList<String>(); 
	private Map<String,String> curRoomids = new HashMap<String, String>();
	private Map<String,String> curGatename = new HashMap<String, String>();
	private MyExpandableListViewAdapter adapter;
//	private String[] selRooms;
	private String[] selRoomsPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_roomkeys);
		initView();
		initLinster();
		initData();
	}
	
	private void initLinster() {
		
	}

	private void initView() {
		header = (Header) findViewById(R.id.header);
		expandableListView = (ExpandableListView)findViewById(R.id.explist);  
	}

	private void initData() {
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setTitle("选择钥匙", null);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						StringBuffer ids = new StringBuffer();
						for (String key : curRoomids.keySet()) {  
							ids.append(curRoomids.get(key)).append(",");
						}
						StringBuffer gns = new StringBuffer();
						for (String key : curGatename.keySet()) {  
							gns.append(curGatename.get(key)).append(",");
						}
						StringBuffer pos = new StringBuffer();
						for(int i=0;i<curSelArray.size();i++){
							if(curSelArray.get(i).equals("1"))
								pos.append(i+"").append(",");
						}
						Intent intent=new Intent();
				        intent.putExtra("roomids", ids.toString());
				        intent.putExtra("roomidpos", pos.toString());
				        intent.putExtra("gatenames", gns.toString());
				        setResult(302, intent);
						SelectKeysActivity.this.finish();
					}
				});
		header.setRightTextViewRes(R.string.selectall, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView tx = (TextView)v;
				if(tx.getText().toString().equals("全选")){
					curRoomids.clear();
					curGatename.clear();
					curSelArray.clear();
					for(int i=0;i<groupArray.size();i++){
						curRoomids.put(i+"", roomids[i]);
						curGatename.put(i+"", groupArray.get(i));						
						curSelArray.add("1");
					}
					tx.setText("全不选");
				}else{
					curRoomids.clear();
					curGatename.clear();
					curSelArray.clear();
					for(int i=0;i<groupArray.size();i++){
						curSelArray.add("0");
					}
					tx.setText("全选");			
				}
				adapter.notifyDataSetChanged();
			}
		});
//		selRooms = getIntent().getStringExtra("ids").split(",");
		selRoomsPos = getIntent().getStringExtra("ids").split(",");
		adapter = new MyExpandableListViewAdapter(SelectKeysActivity.this, groupArray, childArray,curSelArray);
		expandableListView.setAdapter(adapter);
		getValueKeyInfo();
	}

	// 获取可授权钥匙信息
	public void getValueKeyInfo() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		HttpHelper.getInstances(SelectKeysActivity.this).send(FlowConsts.CHECKUSERROOM,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

			@Override
			public void onStart() {
				super.onStart();
				ShowProgressDialog(R.string.app_loading);
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				DismissProgressDialog();
			}

			@Override
			public void onSuccess(CellComAjaxResult arg0) {
				DismissProgressDialog();
				try{
					ValueRoomKeyComm bean = arg0.read(ValueRoomKeyComm.class,
							CellComAjaxResult.ParseType.GSON);
					String st = bean.getReturncode();
					String msg = bean.getReturnmessage();
					List<ValueRoomKey> valueRoomKeys = bean.getBody();
	
					if (FlowConsts.STATUE_1.equals(st)) {
						roomids = new String[valueRoomKeys.size()];
						for(int i=0;i<valueRoomKeys.size();i++){
							roomids[i] = valueRoomKeys.get(i).getRoomid();
							groupArray.add(valueRoomKeys.get(i).getGatename()); 
							List<RoomKey> keys =  valueRoomKeys.get(i).getKeys();
							List<String> tempArray = new  ArrayList<String>(); 
							for(int j=0;j<keys.size();j++){ 
								tempArray.add(keys.get(j).getKeyname());  
							}
//							boolean ishave = false;
//							for(int j=0;i<selRooms.length;j++){
//								if(valueRoomKeys.get(i).getRoomid().equals(selRooms)){
//									ishave =true;
//									curSelArray.add("1");
//									break;
//								}
//							}
//							if(!ishave)
//								curSelArray.add("0");

							curSelArray.add("0");
							childArray.add(tempArray);
						}
						for(int i=0;i<selRoomsPos.length;i++){
							if(selRoomsPos[i].equals(""))
								continue;
							curSelArray.set(Integer.valueOf(selRoomsPos[i]), "1");
						}
						adapter.notifyDataSetChanged();
					}else{
						if (FlowConsts.STATUE_2.equals(st)) {
							// token失效
							ContextUtil.exitLogin(
									SelectKeysActivity.this, header);
							return;
						}
						Log.e("getkeyinfo","error="+msg);
					}
				}catch(Exception e){
					e.printStackTrace();
					ShowMsg(getResources().getString(R.string.dataparsefail));
				}
			}
		});
	}

	class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
		private List<String> group_list;
		private List<List<String>> item_list;
		private List<String> cur_select;
		private Context context;

		public MyExpandableListViewAdapter(Context context, List<String> group_list, List<List<String>> item_list,List<String> cur_select) {
			this.context = context;
			this.group_list = group_list;
			this.item_list = item_list;
			this.cur_select = cur_select;
		}

		  @Override
		  public int getGroupCount() {
		   return group_list.size();
		  }

		  @Override
		  public int getChildrenCount(int groupPosition) {
		   return item_list.get(groupPosition).size();
		  }

		  @Override
		  public Object getGroup(int groupPosition) {
		   return group_list.get(groupPosition);
		  }

		  @Override
		  public Object getChild(int groupPosition, int childPosition) {
		   return item_list.get(groupPosition).get(childPosition);
		  }

		  @Override
		  public long getGroupId(int groupPosition) {
		   return groupPosition;
		  }

		  @Override
		  public long getChildId(int groupPosition, int childPosition) {
		   return childPosition;
		  }

		  @Override
		  public boolean hasStableIds() {
		   return true;
		  }

		  @Override
		  public View getGroupView(final int groupPosition, boolean isExpanded,
		    View convertView, ViewGroup parent) {
			  GroupHolder groupHolder = null;
			  if (convertView == null) {
				  convertView = (View) getLayoutInflater().from(context).inflate(
						  R.layout.expendlist_group, null);
				  groupHolder = new GroupHolder();
				  groupHolder.txt = (TextView) convertView.findViewById(R.id.txt);
				  groupHolder.cbSel = (CheckBox) convertView.findViewById(R.id.checkbox);
		    
				  convertView.setTag(groupHolder);
			  } else {
				  groupHolder = (GroupHolder) convertView.getTag();
			  }
			  groupHolder.txt.setText(group_list.get(groupPosition));
			  groupHolder.cbSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean checked) {
						curRoomids.remove(groupPosition+"");
						curGatename.remove(groupPosition+"");
						if(checked){
							curRoomids.put(groupPosition+"", roomids[groupPosition]);
							curGatename.put(groupPosition+"", groupArray.get(groupPosition));
							curSelArray.set(groupPosition, "1");
						}else{
							curSelArray.set(groupPosition, "0");
						}
					}
				});
			  	if(cur_select.get(groupPosition).equals("1"))
			  		groupHolder.cbSel.setChecked(true);
			  	else
			  		groupHolder.cbSel.setChecked(false);
//			  	for(int i=0;i<selRooms.length;i++){
//			  		if(roomids[groupPosition].equals(selRooms[i]))
//			  			groupHolder.cbSel.setChecked(true);
//			  	}
			  return convertView;
		  }

		  @Override
		  public View getChildView(int groupPosition, int childPosition,
		    boolean isLastChild, View convertView, ViewGroup parent) {
				ItemHolder itemHolder = null;
				if (convertView == null) {
					convertView = (View) getLayoutInflater().from(context).inflate(
							R.layout.expendlist_item, null);
					itemHolder = new ItemHolder();
					itemHolder.txt = (TextView) convertView.findViewById(R.id.txt);
					convertView.setTag(itemHolder);
				} else {
					itemHolder = (ItemHolder) convertView.getTag();
				}
				itemHolder.txt.setText(item_list.get(groupPosition).get(childPosition));
				return convertView;
		  }

		  @Override
		  public boolean isChildSelectable(int groupPosition, int childPosition) {
			  return true;
		  }


		 class GroupHolder {
			 public TextView txt;
			 public CheckBox cbSel;
		 }

		 class ItemHolder {
			 public TextView txt;
		 }
	}
	
}
