package deling.cellcom.com.cn.adapter;

import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

public class ExpandableAdapter extends BaseExpandableListAdapter {
	private List<String> groupArray;
	private List<List<String>> childArray;
	private Activity activity;

	public ExpandableAdapter(Activity activity, List<String> group_list, List<List<String>> item_list) {
		this.activity = activity;
		this.groupArray = group_list;
		this.childArray = item_list;
	}
	public  Object getChild(int  groupPosition, int  childPosition)  
    {  
        return  childArray.get(groupPosition).get(childPosition);  
    }  
    public  long  getChildId(int  groupPosition, int  childPosition)  
    {  
        return  childPosition;  
    }  
    public  int  getChildrenCount(int  groupPosition)  
    {  
        return  childArray.get(groupPosition).size();  
    }  
    public  View getChildView(int  groupPosition, int  childPosition,  
            boolean  isLastChild, View convertView, ViewGroup parent)  
    {  
        String string = childArray.get(groupPosition).get(childPosition);  
        return  getGenericView(string);  
    }  
    // group method stub   
    public  Object getGroup(int  groupPosition)  
    {  
        return  groupArray.get(groupPosition);  
    }  
    public  int  getGroupCount()  
    {  
        return  groupArray.size();  
    }  
    public  long  getGroupId(int  groupPosition)  
    {  
        return  groupPosition;  
    }  
    public  View getGroupView(int  groupPosition, boolean  isExpanded,  
            View convertView, ViewGroup parent)  
    {  
    	GroupHolder groupHolder = null;
    	if (convertView == null) {
			convertView = (View) activity.getLayoutInflater().from(activity).inflate(
					R.layout.expendlist_group, null);
		    groupHolder = new GroupHolder();
		    groupHolder.txt = (TextView) convertView.findViewById(R.id.txt);
		    
		    convertView.setTag(groupHolder);
    	} else {
		    groupHolder = (GroupHolder) convertView.getTag();
    	}
	   groupHolder.txt.setText(groupArray.get(groupPosition));
	   return convertView;
//        String string = groupArray.get(groupPosition);  
//        
//        return  getGenericView(string);  
    }  
    // View stub to create Group/Children 's View   
    public  TextView getGenericView(String string)  
    {  
        // Layout parameters for the ExpandableListView   
        AbsListView.LayoutParams layoutParams = new  AbsListView.LayoutParams(  
                ViewGroup.LayoutParams.FILL_PARENT, 64 );  
        TextView text = new  TextView(activity);  
        text.setLayoutParams(layoutParams);  
        // Center the text vertically   
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);  
        // Set the text starting position   
        text.setPadding(36 , 0 , 0 , 0 );  
        text.setText(string);  
        return  text;  
    }  
    public  boolean  hasStableIds()  
    {  
        return  false ;  
    }  
    public  boolean  isChildSelectable(int  groupPosition, int  childPosition)  
    {  
        return  true ;  
    }

	class GroupHolder {
		public TextView txt;
		public CheckBox cbSel;
	}

	class ItemHolder {
		public TextView txt;
	}
}