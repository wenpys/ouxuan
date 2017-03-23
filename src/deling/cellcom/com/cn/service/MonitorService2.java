package deling.cellcom.com.cn.service;

import java.util.List;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.StrongService;
import deling.cellcom.com.cn.activity.MyApplication;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MonitorService2 extends Service {

	// 用于判断进程是否运行
	private String Process_Name = "cellcom.com.cn.deling:monitor1";
	private String Service_Name = "deling.cellcom.com.cn.service.MonitorService";


	/**
	 *启动MonitorService1 
	 */
	private StrongService startS1 = new StrongService.Stub() {
		@Override
		public void stopService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), MonitorService.class);
			getBaseContext().stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
			Intent i = new Intent(getBaseContext(), MonitorService.class);
			getBaseContext().startService(i);
		}
	};

	//系统优化内存
	@Override
	public void onTrimMemory(int level){
		Log.i("MonitorService2", "MonitorService2 onTrimMemory..."+level);
		
		keepService2();//保持MonitorService1一直运行
		
	}

	@Override
	public void onCreate() {
		Log.i("MonitorService2", "MonitorService2 onCreate...");
		MyApplication.getInstances().setKeepService(this);
		keepService2();
	}
	/**
	 * 判断MonitorService1是否还在运行，如果不是则启动MonitorService1
	 */
	private  void keepService2(){
		boolean isRun = isServiceRunning(MonitorService2.this, Service_Name);
		if (isRun == false) {
			try {
				Log.i("MonitorService2", "重新启动 MonitorService");
				startS1.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else{
			try {
				startS1.stopService();
				startS1.startService();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Notification notification = new Notification(R.drawable.ic_launcher,  
//	   			 getString(R.string.app_name), System.currentTimeMillis()); 
//	   			  
//			PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(this, MyApplication.class), 0);  
//			notification.setLatestEventInfo(this, "uploadservice", "服务运行中....",  
//			pendingintent);  
//			startForeground(0x111, new Notification());
//			stopSelf();
		startService(new Intent(this, InnerService.class));
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) startS1;
	}

	private boolean isProessRunning(Context context, String proessName) {  
		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
			if (info.processName.equals(proessName)) {
				isRunning = true;
			}
		}

		return isRunning;
	}

	public boolean isServiceRunning(Context context, String serviceName) {  
	    boolean isWork = false;  
	    ActivityManager myAM = (ActivityManager) context  
	            .getSystemService(Context.ACTIVITY_SERVICE);  
	    List<RunningServiceInfo> myList = myAM.getRunningServices(40);  
	    if (myList.size() <= 0) {  
	        return false;  
	    }  
	    for (int i = 0; i < myList.size(); i++) {
//			Log.e("serviceName","na:"+myList.get(i).service.getClassName());
	        String mName = myList.get(i).service.getClassName().toString();  
	        if (mName.equals(serviceName)) {  
	        	Log.e("MonitorService1","runservice="+serviceName);
		    	isWork = true;  
	            break;
	        }  
	    }  
	    return isWork;  
	}
	
	private void startApp(){
		Log.e("MonitorService","startApp");
//		Intent intent = new Intent(); 
//	 	PackageManager packageManager = this.getPackageManager(); 
//	 	intent = packageManager.getLaunchIntentForPackage("deling.cellcom.com.cn"); 
//	 	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
//	 	this.startActivity(intent);
		ComponentName componetName = new ComponentName(  
              //这个是另外一个应用程序的包名   
             "cellcom.com.cn.deling",  
             //这个参数是要启动的Activity   
             "deling.cellcom.com.cn.activity.main.MainActivity");   
		Intent intent= new Intent();
		Bundle bundle = new Bundle();  
		bundle.putString("arge1", "这是跳转过来的！来自apk1");  
		intent.putExtras(bundle);  
		intent.setComponent(componetName);  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
		startActivity(intent);

	}
   
   Handler handler = new Handler() {
       public void handleMessage(Message msg) {
           switch (msg.what) {
			case 1:
	            System.out.println("running");
				
				break;
			case 2:
				System.out.println("norun");
			default:
				break;
			} 
       };
   };
   
   class ThreadShow implements Runnable {

       @Override
       public void run() {
           // TODO Auto-generated method stub
           while (true) {
               try {
                   Thread.sleep(1000);
                   Message msg = new Message();
                   msg.what = 1;
                   handler.sendMessage(msg);
                   System.out.println("send...");
               } catch (Exception e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
                   System.out.println("thread error...");
               }
           }
       }
   }
}
