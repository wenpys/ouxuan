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

public class InnerService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		MyApplication.getInstances().setForeground(MyApplication.getInstances().getKeepService(), this);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
