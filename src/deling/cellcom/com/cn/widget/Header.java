package deling.cellcom.com.cn.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.utils.ContextUtil;

/**
 * 页面标题栏公用控件
 */
public class Header extends RelativeLayout {
	private TextView tv;

//	private ImageView iv = new ImageView(getContext());

	public Header(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Header(Context context) {
		super(context);
		init();
	}

	private void init() {
		// int p = (int) (getResources().getDisplayMetrics().density * 10);
		// setPadding(p, 0, p, 0);
	}

	public void setTitle(String text) {
		if (tv == null) {
			tv = new TextView(getContext());
			tv.setSingleLine();
			tv.setTextSize(22);
			tv.setTextColor(Color.WHITE);
			tv.getPaint().setFakeBoldText(false);
			setTitleView(tv);
		}
		tv.setText(text);
	}

	public void setTitle(String text, OnClickListener listener) {
		if (tv == null) {
			tv = new TextView(getContext());
			tv.setSingleLine();
			tv.setMaxWidth(ContextUtil.dip2px(getContext(), 200));
			tv.setTextSize(18);
			tv.setTextColor(Color.WHITE);
			tv.getPaint().setFakeBoldText(false);
			tv.setOnClickListener(listener);
			setTitleView(tv);
		}
		tv.setText(text);
	}

	public void hiddleTitle() {
		if (tv != null) {
			tv.setVisibility(View.GONE);
		}
	}

	public void showTitle() {
		if (tv != null) {
			tv.setVisibility(View.VISIBLE);
		}
	}

	// --------------------2015-9-10 hjy ------------------------------
	public void setTitleImageViewRes(View view) {

		int p = (int) (getResources().getDisplayMetrics().density * 10);
		// ImageView iv = new ImageView(getContext());
		/*
		 * iv.setImageResource(res);
		 * iv.setBackgroundResource((R.drawable.common_head_itemselector));
		 */
		view.setPadding(p, p, p, p);
		setTitleView(view);
	}

	public void setTitleView(View view) {
		RelativeLayout.LayoutParams viewLp = new RelativeLayout.LayoutParams(
				-2, -2);
		viewLp.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(view, viewLp);
	}

	public void setRightView(View view, OnClickListener listener) {
		int p = (int) (getResources().getDisplayMetrics().density * 20);
		int pt = (int) (getResources().getDisplayMetrics().density * 8);
		RelativeLayout.LayoutParams ivLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		ivLp.addRule(RelativeLayout.CENTER_VERTICAL);
		ivLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ivLp.setMargins(p, pt, p, pt);
		addView(view, ivLp);
		view.setOnClickListener(listener);
	}

	public ImageView setRightImageViewRes(int res, OnClickListener listener) {
		int p = (int) (getResources().getDisplayMetrics().density * 10);
		int pt = (int) (getResources().getDisplayMetrics().density * 20);
		ImageView iv = new ImageView(getContext());
		iv.setImageResource(res);
		iv.setBackgroundResource((R.drawable.common_head_itemselector));
		iv.setPadding(p, p, pt, p);
		// setRightView(iv, listener);
		RelativeLayout.LayoutParams ivLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		ivLp.addRule(RelativeLayout.CENTER_VERTICAL);
		ivLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(iv, ivLp);
		iv.setOnClickListener(listener);
		return iv;
	}

	public void setRightTextViewRes(int res, OnClickListener listener) {
		int p = (int) (getResources().getDisplayMetrics().density * 10);
		TextView iv = new TextView(getContext());
		iv.setTextColor(Color.parseColor("#FFFFFF"));
		iv.setTextSize(14);
		iv.setText(res);
		iv.setBackgroundResource((R.drawable.common_head_itemselector));
		iv.setPadding(p, p, p, p);
		// setRightView(iv, listener);
		RelativeLayout.LayoutParams ivLp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		ivLp.addRule(RelativeLayout.CENTER_VERTICAL);
		ivLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		addView(iv, ivLp);
		iv.setOnClickListener(listener);
	}

	public void setLeftView(View view, OnClickListener listener) {
		RelativeLayout.LayoutParams ivLp = new RelativeLayout.LayoutParams(-2,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		ivLp.addRule(RelativeLayout.CENTER_VERTICAL);
		ivLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		addView(view, ivLp);
		view.setOnClickListener(listener);
	}

	public void setLeftImageVewRes(int res, OnClickListener listener) {
		int p = (int) (getResources().getDisplayMetrics().density * 10);
		ImageView iv = new ImageView(getContext());
		iv.setImageResource(res);
		iv.setBackgroundResource((R.drawable.common_head_itemselector));
		iv.setPadding(p, p, p, p);
		setLeftView(iv, listener);
	}

}
