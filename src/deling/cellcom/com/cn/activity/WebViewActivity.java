package deling.cellcom.com.cn.activity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.widget.Header;

@SuppressLint({ "JavascriptInterface", "NewApi" })
public class WebViewActivity extends FragmentActivityBase {
	private WebView webView;
	private String nowurl;
	private ImageView iv_drawable;
	private AnimationDrawable animationDrawable;
	private String content;
	private boolean isWeatherNotes = false;
	private long myReference;
	private DownloadManager manager;
	private BroadcastReceiver receiver;
	private Header header;
	private String mTitle = "";
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.app_webview);
		// 记录创建的webview
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		header = (Header) findViewById(R.id.header);
		iv_drawable = (ImageView) findViewById(R.id.iv_drawable);
		webView = (WebView) findViewById(R.id.webview);
		settings = webView.getSettings();
		manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		IntentFilter filter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE)) { // 下载完成
					long downId = intent.getLongExtra(
							DownloadManager.EXTRA_DOWNLOAD_ID, -1);
					if (downId == myReference) {
						openFile();
					}
				}
			}

		};
		registerReceiver(receiver, filter);
	}

	private void initListener() {
		header.setBackgroundResource(R.drawable.main_nav_bg);
		header.setLeftImageVewRes(R.drawable.main_nav_back,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputmanger.hideSoftInputFromWindow(
								header.getWindowToken(), 0);
						if (webView.canGoBack()) {
							webView.goBack();// 返回前一个页面
						} else {
							WebViewActivity.this.finish();
						}
					}
				});
	}

	private void openFile() {
		Uri uri = manager.getUriForDownloadedFile(myReference);
		// Log.e("MYTAG", uri.toString());
		Intent intent2 = new Intent();
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent2.setAction(Intent.ACTION_VIEW);
		// 设置intent的data和Type属性。
		intent2.setDataAndType(uri, "text/html");
		// 跳转
		startActivity(intent2);
	}

	private void initData() {
		String url = getIntent().getExtras().getString("url");
		mTitle = getIntent().getExtras().getString("title");
		Log.e("url", "url=" + url);
		webViewShow(url);
		if (!mTitle.equals(""))
			header.setTitle(mTitle);

		animationDrawable = (AnimationDrawable) iv_drawable.getDrawable();
		iv_drawable.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				animationDrawable.start();
			}
		});
	}

	private void webViewShow(String url) {
		webView.addJavascriptInterface(new getJavaScriptLocalObj(), "ncp");
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebPageClient());
		webView.getSettings().setDefaultTextEncodingName("GBK");
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				WebViewActivity.this.getWindow().setFeatureInt(
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

			@Override
			public void onReceivedTitle(WebView view, String title) {
				if (!mTitle.equals("")) {
					header.setTitle(getIntent().getExtras().getString("title"));
				} else
					header.setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		webView.loadUrl(url);
		nowurl = url;
	}

	private class WebPageClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			// 把当前页面的地址赋值到全局变量，方便监听
			nowurl = url;
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			getHtmlTitle(view);
			super.onPageFinished(view, url);
		}
	}

	private void getHtmlTitle(WebView view) {
		view.loadUrl("javascript:window.html_obj.showSource('<head>'+"
				+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String httptitle = "";
				String cardtitle = "";
				int ElemetLen = 0;
				System.out.println("httptitle--------->" + content);
				if (content != null) {
					if (content.indexOf("<title>") != -1) {
						ElemetLen = ("<title>").length();
						httptitle = content.substring(
								content.indexOf("<title>") + ElemetLen,
								content.indexOf("</title>"));
					} else if (content.contains("card title")) {
						ElemetLen = ("<card title=").length() + 1;
						cardtitle = content.substring(content
								.indexOf("<card title=") + ElemetLen);
						httptitle = cardtitle.substring(0,
								cardtitle.indexOf("\""));
					}
				}
			}
		}, 1000);
	}

	final class getJavaScriptLocalObj {
		@JavascriptInterface
		public void callOnJs(String url, String name) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// 如果当前页面为主页就不允许后退，直接退出
		if (webView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0 && notIndex(nowurl)) {
			webView.goBack();
			nowurl = webView.getOriginalUrl();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	// 判断这个页面是不是主页面
	public boolean notIndex(String url) {
		if (url.endsWith("index.html")) {
			return false;
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (myReference > 0) {
			manager.remove(myReference);
		}
		if (webView != null) {
			webView.setVisibility(View.GONE);
		}
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onResume() {
		settings.setJavaScriptEnabled(true);
		super.onResume();
	}

	@Override
	protected void onStop() {
		settings.setJavaScriptEnabled(false);
		super.onStop();
	}
}