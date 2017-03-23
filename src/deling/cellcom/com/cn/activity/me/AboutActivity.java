package deling.cellcom.com.cn.activity.me;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.widget.Header;

/**
 * 
 * @author 关于
 * 
 */
public class AboutActivity extends FragmentActivityBase {

	private Header header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		MyApplication.getInstances().getActivities().add(this);
		initData();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
	private void initData() {
		header = (Header) findViewById(R.id.header);
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setTitle("关于", null);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								header.getWindowToken(), 0);
						AboutActivity.this.finish();
					}
				});
	}
}
