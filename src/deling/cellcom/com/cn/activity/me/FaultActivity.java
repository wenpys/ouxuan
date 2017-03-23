package deling.cellcom.com.cn.activity.me;
/**
 * 问题反馈界面
 * */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.deling.R;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.net.base.CellComHttpInterface.NetCallBack;
import cellcom.com.cn.util.MD5;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.activity.base.FragmentActivityBase;
import deling.cellcom.com.cn.adapter.FaultGridAdapter;
import deling.cellcom.com.cn.bean.UploadPicInfoBean;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.widget.ActionSheet;
import deling.cellcom.com.cn.widget.ActionSheet.OnActionSheetSelected;
import deling.cellcom.com.cn.widget.Header;
import deling.cellcom.com.cn.widget.zhy.PicSelectActivity;

public class FaultActivity extends FragmentActivityBase implements OnActionSheetSelected ,OnCancelListener{
	private Header header;
	private EditText etContent;
	private GridView gridView;
	private TextView tvSubmit;
	private TextView tvNumber;
	private final int WORD_NUMB_MAX=500;
	private boolean isMoreWord = false;
	private int bitNumber=0;
	private FaultGridAdapter adapter;
	private static int CAMERA_REQUEST_CODE = 1;
	private static int GALLERY_REQUEST_CODE = 2;
	private List<String> mSelectedImage;
	private List<String> mImageID;
	private FinalBitmap finalBitmap;
	private File tmpDir = new File(Environment.getExternalStorageDirectory()
			+ "/cache");
	private ArrayList<String> paths = new ArrayList<String>();
    private Thread uploadThread;
	private String Cpic = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faultsuggest);
		MyApplication.getInstances().getActivities().add(this);
		initView();
		initData();
		initListener();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstances().getActivities().remove(MyApplication.getInstances().getActivities().size()-1);
	}
	private void initListener() {
		etContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {

				int len = etContent.getText().toString().length();
				if (len <= WORD_NUMB_MAX) {
					isMoreWord = false;
					if (tvNumber.getCurrentTextColor() == Color.RED) {
						tvNumber.setTextColor(Color.BLACK);
					}
				} else {
					tvNumber.setTextColor(Color.RED);
					isMoreWord = true;
				}
				tvNumber.setText(len + "/" + 500);
			}
		});
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isMoreWord) {
					showToast("描述内容不能超过500个字哦");
					return;
				}
				if (TextUtils.isEmpty(etContent.getText().toString())) {
					showToast("描述内容不能为空哦");
					return;
				}
				if(bitNumber!=0){
					showToast("图片还没上传完哦，请稍等");
					return;
				}
				String content = etContent.getText().toString();
				StringBuffer imageids = new StringBuffer();
				for(int i=0;i<mImageID.size();i++){
					imageids.append(mImageID.get(i)).append(",");
				}
				if(imageids.length()>0)
					imageids.deleteCharAt(imageids.length()-1);
				pullDataToServicer(content, imageids.toString());
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==adapter.getCount()-1){
					if(adapter.getCount()>3)
						ShowMsg("只能上传3张图片哦");
					else
						addBitmap();
				}
			}
		});
	}

	protected void addBitmap() {
		ActionSheet.showSheet(FaultActivity.this,
				FaultActivity.this,
				FaultActivity.this, "1");
	}
	private void pullDataToServicer(String content, String imageids) {
		CellComAjaxParams params=new CellComAjaxParams();
		params.put("content", content);
		params.put("imageids", imageids);
		HttpHelper.getInstances(this).send(FlowConsts.SUGGEST, 
				params, 
				CellComAjaxHttp.HttpWayMode.POST, 
				new NetCallBack<CellComAjaxResult>() {
					
					@Override
					public void onSuccess(CellComAjaxResult t) {
						ShowMsg("提交成功");
						finish();
					}
				});
	}
	private void initData() {
		mImageID = new ArrayList<String>();
		paths.add("");
		adapter=new FaultGridAdapter(this, paths);
		gridView.setAdapter(adapter);
		finalBitmap=FinalBitmap.create(this);
		header.setTitle(getResources().getString(R.string.title_activity_fault));
		header.setLeftImageVewRes(R.drawable.main_nav_back, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FaultActivity.this.finish();
			}
		});  
	    File dir = new File("/sdcard/cache/");
	    if (!dir.exists()) {
	        dir.mkdir();
	    }
	}
	private void initView() {
		etContent=(EditText) findViewById(R.id.et_content);
		gridView=(GridView) findViewById(R.id.gridview);
		tvSubmit=(TextView) findViewById(R.id.tv_submit);
		tvNumber=(TextView) findViewById(R.id.tv_number);
		header=(Header) findViewById(R.id.header);
	}
	private void showToast(String str) {
		Toast.makeText(FaultActivity.this, str, Toast.LENGTH_SHORT).show();
	}
	


	private Bitmap compressImage(Bitmap image) {  
		  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩   
    	    Log.i("", "压缩中文件大小:" + baos.toByteArray().length / 1024);      
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	
	private Bitmap getimage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 1440f;//这里设置高度为800f  
        float ww = 1080f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    Log.i("", "按比例缩小后宽度--" + width);
	    Log.i("", "按比例缩小后高度--" + height);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩  
    }
	
	//将压缩的bitmap保存到sdcard卡临时文件夹img_interim，用于上传
	public File saveMyBitmap(String filename, Bitmap bit) {  
	    File dir = new File("/sdcard/cache/");
	    if (!dir.exists()) {
	        dir.mkdir();
	    }
	    File f = new File("/sdcard/cache/" + filename);
	    if(!f.exists()){
		    try {
		        f.createNewFile();
		        FileOutputStream fOut = null;
		        fOut = new FileOutputStream(f);  
		        bit.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		        fOut.flush();  
		        fOut.close();  
		    } catch (IOException e1) {
		        // TODO Auto-generated catch block
		        f = null;
		        e1.printStackTrace();
		    }  
	    }
	    long fileLength=f.length();
		String size = fileLength+"";
		if(fileLength<1024 && fileLength>0){
			size =String.format("%dB", fileLength);
		}else if(fileLength>=1024 && fileLength<(1024*1024)){
			fileLength=fileLength/1024;
			size=String.format("%dK", fileLength);
		}else if(fileLength>(1024*1024*1024)){ 
			fileLength=(fileLength/1024)/1024;
			size=String.format("%dM", fileLength);
		}
	    Log.i("", "压缩后文件大小:" + size);
	    
	    return f;
	}
	
	@Override
	public void onClick(int whichButton) {

		// TODO Auto-generated method stub
		if (whichButton == 1) {
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
			  Cpic = "/sdcard/cache/"+System.currentTimeMillis()+".jpg";
	  	      File img = new File(Cpic);
	    	  //获取保存之后的图片路径，默认以当前时间为文件命名
	          Uri u = Uri.fromFile(img);
	          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	          intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
	          intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
	          startActivityForResult(intent, CAMERA_REQUEST_CODE);
			} else {
				Toast.makeText(FaultActivity.this, "没有储存卡",
						Toast.LENGTH_LONG).show();
			}
		} else if (whichButton == 2) {
			// 选择图片
			Intent intent = new Intent(FaultActivity.this,
					PicSelectActivity.class);
			intent.putExtra("maxvalue", 3);
			startActivityForResult(intent, GALLERY_REQUEST_CODE);
		}
	
	}

	  //上传图片
  	private void uploadpic(final File img) {
  		// TODO Auto-generated method stub
  		final String name=img.getName();
  		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
  		try {
  			cellComAjaxParams.put("file", img);
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  		}
  		HttpHelper.getInstances(FaultActivity.this).send(
				FlowConsts.UPLOADIMG, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
  				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

  					@Override
  					public void onStart() {
  						super.onStart();
  						if (paths.size() >= 9) {
  							paths.set(paths.size() - 1, "loading");
  						} else {
  							paths.add(paths.size() - 1, "loading");
  						}
  						adapter.setList(paths);
  						adapter.notifyDataSetChanged();
  					}

  					@Override
  					public void onFailure(Throwable t, String strMsg) {
  						// TODO Auto-generated method stub
  						super.onFailure(t, strMsg);
  						bitNumber--;
  						try {
  							if(uploadThread != null)
  								uploadThread.interrupt();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
  						int index=-1;
  						for (int i = 0; i < paths.size(); i++) {
  							if("loading".equals(paths.get(i))){
  								index=i;
  							}
  						}
  						if(index!=-1){
  							paths.remove(index);
  						}
  						if (!TextUtils.isEmpty(paths.get(paths.size() - 1))) {
  							if (paths.size() < 9) {
  								paths.add("");
  							}
  						} 
  						adapter.setList(paths);
  						adapter.notifyDataSetChanged();
  					}

  					@Override
  					public void onSuccess(CellComAjaxResult arg0) {
  						// TODO Auto-generated method stub
  						bitNumber--;
  						try {
  							if(uploadThread != null)
  								uploadThread.interrupt();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
  						UploadPicInfoBean pafkComm=null;
  						try {
  	  						pafkComm= arg0.read(
  	  								UploadPicInfoBean.class,
  	  								CellComAjaxResult.ParseType.GSON);
						} catch (Exception e) {
							ShowMsg("图片上传失败");
							return;
						}
  						ShowMsg(name+":"+pafkComm.getReturnmessage());
  						if (!FlowConsts.STATUE_1.equalsIgnoreCase(pafkComm
								.getReturncode())) {
							return;
						}
  						String path = pafkComm.getBody().getImageid();
  						mImageID.add(path);
  						if (!TextUtils.isEmpty(path)) {
  							for (int i = 0; i < paths.size(); i++) {
  								if("loading".equals(paths.get(i))){
  									paths.set(i, img.getAbsolutePath());
  									break;
  								}
  							}
  						}
  						if (!TextUtils.isEmpty(paths.get(paths.size() - 1))) {
  							if (paths.size() < 9) {
  								paths.add("");
  							}
  						} 
  						for (int i = 0; i < paths.size(); i++) {
  							Log.e("Success", "path==>"+paths.get(i));
  						}
  						Log.e("Success", "==============================>");
  						adapter.setList(paths);
  						adapter.notifyDataSetChanged();
  					}
  				});
	  	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 2:
				if (data.getSerializableExtra("value") != null) {
					uploadThread = new Thread(){

						@Override
						public void run() {
							mSelectedImage = new ArrayList<String>();
							mSelectedImage = (ArrayList<String>) data.getSerializableExtra("value");
							bitNumber = mSelectedImage.size();
							for(int i = 0; i < mSelectedImage.size(); i++){
								String path = mSelectedImage.get(i);
							    Bitmap bit = getimage(path);//压缩bitmap
							    String filename;
								try {
									String n = path.substring(path.lastIndexOf("/") + 1);
									String z = n.substring(n.lastIndexOf(".") + 1);
									filename = MD5.MD5Encode(n.substring(0,n.length()-z.length()-1))+"."+z;
								    File file = saveMyBitmap(filename, bit);
								    Log.i("","file:"+file);
									uploadpic(file);
									sleep(10000);
								} catch (NoSuchAlgorithmException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							super.run();
						}
						
					};
					uploadThread.start();

				}
				break;
			case 1:
				uploadThread = new Thread(){

					@Override
					public void run() {
					    Bitmap bit = getimage(Cpic);
						bitNumber = 1;
					    String filename;
						try {
							runOnUiThread(new Runnable() {

		                        public void run() {

									Toast.makeText(FaultActivity.this,"图片正在处理，请稍后...",0).show();
		                        }
		                    });
							filename = MD5.MD5Encode(Cpic)+".jpg";
						    File file = saveMyBitmap(filename, bit);
						    Log.i("","file:"+file);
							uploadpic(file);
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						super.run();
					}
				};
	      		uploadThread.start();
				break;
			default:
			}
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		
	}
}
