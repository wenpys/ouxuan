package deling.cellcom.com.cn.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;

public class CallActivity extends FragmentActivityBase{
	private RelativeLayout layout;
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_call); 
		
	} 	
}
