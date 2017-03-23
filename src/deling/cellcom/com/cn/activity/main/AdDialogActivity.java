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

public class AdDialogActivity extends FragmentActivityBase{
	private RelativeLayout layout;
	private WebView webView;
	private ImageView ivImage;
	private ImageView iv_drawable;
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.ad_dialog); 
		layout=(RelativeLayout)findViewById(R.id.ad_layout); 
		layout.setOnClickListener(new OnClickListener() { 
			@Override 
			public void onClick(View v) { 
				// TODO Auto-generated method stub 
				Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！", 
				Toast.LENGTH_SHORT).show(); 
			} 
		});
		ivImage = (ImageView) findViewById(R.id.ad_image);
		iv_drawable = (ImageView) findViewById(R.id.iv_drawable);
		webView=(WebView)findViewById(R.id.ad_webview); 
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				AdDialogActivity.this.getWindow().setFeatureInt(
						Window.FEATURE_PROGRESS, newProgress * 100);
				super.onProgressChanged(view, newProgress);
				if (newProgress >= 99) {
					webView.setVisibility(View.VISIBLE);
					iv_drawable.setVisibility(View.GONE);
				} else {
					webView.setVisibility(View.GONE);
					iv_drawable.setVisibility(View.VISIBLE);
				}
			}
		});
		
		Bundle bundle = getIntent().getExtras();
		String vendor = "whitelist_default";
		if(bundle != null){
			vendor = bundle.getString("vendor");
		}
		Log.i("vendor","vendor="+vendor);
//		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.whitelist_default);
//		int height = bitmap.getHeight();
//		int width= bitmap.getWidth();
//		
//		ivImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
//				200));
		
//		if(vendorid!=0){
//			Picasso.with(AdDialogActivity.this).load(vendorid).into(ivImage);
//		}else
//			Picasso.with(AdDialogActivity.this).load(R.drawable.whitelist_default).into(ivImage);
		String url = "file:///android_asset/";
		if(vendor.equals("meizu"))
			url += "meizu.htm";
		else if(vendor.equals("huawei"))
			url += "huawei.htm";
		else if(vendor.equals("xiaomi"))
			url += "xiaomi.htm";
		else
			url += "whitelist_default.htm";
		webView.loadUrl(url);
	} 
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){ 
		finish(); 
		return true; 
	} 	
}
