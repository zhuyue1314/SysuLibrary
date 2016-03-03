package com.project.sysumobilelibrary.global;

import com.project.sysumobilelibrary.entity.User;
import com.project.sysumobilelibrary.utils.MyDBHelper;
import com.project.sysumobilelibrary.utils.MyNetworkUtil;
import com.project.sysumobilelibrary.utils.MyVolley;

import android.app.Application;

public class MyApplication extends Application {
	private static final String TAG = "MyApplication";

	private static MyNetworkUtil myNetworkUtil;
	private static MyVolley myVolley;
	private static User user;
//	private static MyDBHelper myDBHelper;
	
	@Override
	public void onCreate() {
		super.onCreate();
		myNetworkUtil = new MyNetworkUtil();
		myNetworkUtil.initNetworkEnvironment(getApplicationContext());
		myVolley = new MyVolley(this);
		user = new User();
//		myDBHelper = new MyDBHelper(getApplicationContext());
	}

	public static MyNetworkUtil getMyNetworkUtil() {
		return myNetworkUtil;
	}
	public static MyVolley getMyVolley(){
		return myVolley;
	}
	public static User getUser(){
		return user;
	}
//	public static MyDBHelper getMyDBHelper(){
//		return myDBHelper;
//	}
}
