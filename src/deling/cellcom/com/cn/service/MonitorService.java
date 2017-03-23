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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.activity.MyApplication;
import android.widget.Toast;

public class MonitorService extends Service {

	// 用于判断进程是否运行
	private String Process_Name = "cellcom.com.cn.deling";


	/**
	 *启动MonitorService2 
	 */
	private StrongService startS2 = new StrongService.Stub() {
		@Override
		public void stopService() throws RemoteException {
//			Intent i = new Intent(getBaseContext(), MonitorService2.class);
//			getBaseContext().stopService(i);
		}

		@Override
		public void startService() throws RemoteException {
//			Intent i = new Intent(getBaseContext(), MonitorService2.class);
//			getBaseContext().startService(i);
			startApp();
		}
	};

	//系统优化内存
	@Override
	public void onTrimMemory(int level){
		Log.i("MonitorService", "MonitorService1 onTrimMemory..."+level);
//		isRuning(MonitorService1.this, Process_Name);
//		isServiceRunning(MonitorService.this, "cellcom.com.cn.deling:monitor2");
		
		keepService2();
	}

	@Override
	public void onCreate() {
		Log.i("MonitorService", "MonitorService onCreate...");
		keepService2();
	}
	/**
	 * 判断MonitorService2是否还在运行，如果不是则启动MonitorService2
	 */
	private  void keepService2(){
		new Thread(){
			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				boolean isRun = isAppRuning(MonitorService.this, Process_Name);
				if (isRun == false) {
					try {
						Log.i("MonitorService1", "重新启动APP");
						startS2.startService();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
		}.start();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return (IBinder) startS2;
	}

	private boolean isProessRunning(Context context, String proessName) {  
		boolean isRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : lists) {
//			Log.e("processName","na:"+info.processName);
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
			Log.e("serviceName","na:"+myList.get(i).service.getClassName());
	        String mName = myList.get(i).service.getClassName().toString();  
	        if (mName.equals(serviceName)) {  
	        	Log.e("MonitorService1","runservice="+serviceName);
		    	isWork = true;  
	            break;
	        }  
	    }  
	    return isWork;  
	}
	
    public boolean isAppRuning(Context context, String proessName){
    	ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
    	List<RunningTaskInfo> list = am.getRunningTasks(100);
    	boolean isAppRunning = false;
    	for (RunningTaskInfo info : list) {
			Log.e("appName","na:"+info.baseActivity.getPackageName());
	    	if (info.topActivity.getPackageName().equals(proessName) || info.baseActivity.getPackageName().equals(proessName)) {
		    	isAppRunning = true;
		    	Log.e("MonitorService1",info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
		    	break;
	    	}
    	}
    	return isAppRunning;
    }
     
    public  String getTopActivityName(Context context){
        String topActivityClassName=null;
         ActivityManager activityManager =
        (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
         if(runningTaskInfos != null){
             ComponentName f=runningTaskInfos.get(0).topActivity;
             topActivityClassName=f.getClassName();
         }
         return topActivityClassName;
    }
	
	private void startApp(){
		Log.e("MonitorService1","startApp");
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
		bundle.putString("from", "service");  
		intent.putExtras(bundle);  
		intent.setComponent(componetName);  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ; 
		getBaseContext().startActivity(intent);
	}
	public static boolean isBackground(Context context) {  
		   ActivityManager activityManager = (ActivityManager) context  
		                .getSystemService(Context.ACTIVITY_SERVICE);  
	       List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();  
		        for (RunningAppProcessInfo appProcess : appProcesses) {  
	           if (appProcess.processName.equals(context.getPackageName())) {   
		               if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
		                   return true;  
		                } else {  
		                   return false;  
		                }  
		            }  
		        }  
		        return false;  
		  }  

}
