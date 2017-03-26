package deling.cellcom.com.cn.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.Select;

import com.testin.agent.TestinAgent;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cellcom.com.cn.util.LogMgr;
import cn.jpush.android.api.JPushInterface;

import com.testin.agent.TestinAgent;

import deling.cellcom.com.cn.activity.main.KeepLiveActivity;
import deling.cellcom.com.cn.bean.Keyinfo;

public class MyApplication extends Application {
	private int userid = -1;
	private String avatar;
	private String phone;
	private int keyNum = 0;
	private boolean isSharkOpen = true;
	private boolean keyState = true;
	private int userType = 1;
	private List<Keyinfo> keylist = new ArrayList<Keyinfo>();
	private static KeepLiveActivity keepLiveActivity;
	private List<Activity> activities=new ArrayList<Activity>();
	
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public List<Keyinfo> getKeylist() {
		return keylist;
	}

	public void setKeylist(List<Keyinfo> keylist) {
		this.keylist.clear();
		this.keylist.addAll(keylist);
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(int keyNum) {
		this.keyNum = keyNum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public boolean isKeyState() {
		return keyState;
	}

	public void setKeyState(boolean keyState) {
		this.keyState = keyState;
	}

	private Handler handler;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	private static MyApplication mApplication;// 全局变量

	public static MyApplication getInstances() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		mApplication = this;
		
//		TestinAgent.init(this, "57c978675dd7eb1be3eeaabd5d56a0ff", "hgzy");
    	JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
	}

	public boolean isSharkOpen() {
		return isSharkOpen;
	}

	public void setSharkOpen(boolean isSharkOpen) {
		this.isSharkOpen = isSharkOpen;
	}

	public KeepLiveActivity getKeepLiveActivity() {
		return keepLiveActivity;
	}

	public void setKeepLiveActivity(KeepLiveActivity keepLiveActivity) {
		this.keepLiveActivity = keepLiveActivity;
	}	
	
	private Service mkeepService;
	public void setKeepService(Service keepService) {
		this.mkeepService = keepService;
	}
	public Service getKeepService() {
		return mkeepService;
	}
	
	public void setForeground(final Service keepLiveService, final Service innerService){
		final int foregroundPushId = 1;
		Log.e("myapp","keep:"+keepLiveService+"--inner:"+innerService);
		if (keepLiveService != null) {
			if (Build.VERSION.SDK_INT  < Build.VERSION_CODES.JELLY_BEAN_MR2) {
				keepLiveService.startForeground(foregroundPushId, new Notification());				
			}else {
				keepLiveService.startForeground(foregroundPushId, new Notification());
				if(innerService != null){
					innerService.startForeground(foregroundPushId, new Notification());
					innerService.stopSelf();
				}
			}
		}
	}
}