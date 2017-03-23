package deling.cellcom.com.cn.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cellcom.com.cn.deling.R;

import com.ab.activity.AbActivity;

import deling.cellcom.com.cn.activity.welcome.LoginActivity;
import deling.cellcom.com.cn.adapter.AdImageAdapter;
import deling.cellcom.com.cn.widget.viewflow.CircleFlowIndicator;
import deling.cellcom.com.cn.widget.viewflow.ViewFlow;

public class GuideActivity extends AbActivity {
	private ViewFlow viewFlow;
	public TextView tvSkip;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		viewFlow = (ViewFlow)findViewById(R.id.guide);
		tvSkip = (TextView)findViewById(R.id.tv_skip);
		viewFlow.setmSideBuffer(4); // 实际图片张数， 我的ImageAdapter实际图片张数为3
		viewFlow.setAdapter(new AdImageAdapter(this));
		tvSkip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				backToMainDesk();
			}
		});
		
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.guide_dot);
		viewFlow.setFlowIndicator(indic);
//		viewFlow.setTimeSpan(4500);
//		viewFlow.setSelection(2000);	//设置初始位置
		viewFlow.startAutoFlowTimer();  //启动自动播放
		viewFlow.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {
			
			@Override
			public void onSwitched(View view, int position) {
				// TODO Auto-generated method stub
				if(position == 3)
					tvSkip.setVisibility(View.GONE);
				else
					tvSkip.setVisibility(View.VISIBLE);
				
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(viewFlow != null){
			viewFlow.destroyDrawingCache();
		}
	}

	/**
	 * 初始化监听
	 */
	private void initListener(){
		
	}
	
	public void backToMainDesk() {
		Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
