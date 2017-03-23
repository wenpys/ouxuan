package deling.cellcom.com.cn.widget.zhy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cellcom.com.cn.deling.R;

public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public ArrayList<String> mSelectedImage = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	private Activity context;
	private int maxvalue;

	public MyAdapter(Activity context, List<String> mDatas, int itemLayoutId,
			String dirPath,int maxvalue)
	{
		super(context, mDatas, itemLayoutId);
		this.mDirPath = dirPath;
		this.context=context;
		this.maxvalue=maxvalue;
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{
		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.picselect_pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.drawable.picselect_picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		
		mImageView.setColorFilter(null);
		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v)
			{
			    if(mSelectedImage.contains(mDirPath + "/" + item)){
			    	mSelectedImage.remove(mDirPath + "/" + item);
		            mSelect.setImageResource(R.drawable.picselect_picture_unselected);
		            mImageView.setColorFilter(Color.parseColor("#00000000"));
			    }else{
			    	if(mSelectedImage.size()>maxvalue-1){
			    		Toast.makeText(context, "所选图片不能超过"+maxvalue+"张哦", Toast.LENGTH_SHORT).show();
			    		return;
			    	}
					  mSelectedImage.add(mDirPath + "/" + item);
		              mSelect.setImageResource(R.drawable.picselect_pictures_selected);
		              mImageView.setColorFilter(Color.parseColor("#77000000"));
			    }
//             ( (PicSelectActivity)mContext).onItemClick();
			}
		});
		
		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item))
		{
			mSelect.setImageResource(R.drawable.picselect_pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}

	}
}
