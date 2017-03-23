package deling.cellcom.com.cn.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cellcom.com.cn.util.LogMgr;
import cellcom.com.cn.util.MD5;
import cn.jpush.a.a.ac;
import cn.jpush.android.api.JPushInterface;
import deling.cellcom.com.cn.activity.main.MainActivity;
import deling.cellcom.com.cn.activity.welcome.LoginActivity;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.widget.popupwindow.ActivityExitPopupWindow;
import deling.cellcom.com.cn.widget.popupwindow.ActivityExitPopupWindow.OnClickCallBack;

public class ContextUtil {
	public static final int EXIT_LOGIN = 0x0001;
	public static final String EXIT_LOGINFLAG = "EXITLOGIN";

	public static final int EXIT_APPLICATION = 0x0002;
	public static final String EXIT_APPLICATIONFLAG = "EXITAPPLICATION";

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String getSex(String key) {
		if (FlowConsts.FEMALE.equals(key)) {
			return "女";
		} else if (FlowConsts.MALE.equals(key)) {
			return "男";
		} else if ("-1".equals(key)) {
			return "未知";
		}
		return "";
	}

	/**
	 * 拍照（从视屏流中截取)
	 * **/
	public static int getPhotoFormImg(Context ctx, Bitmap bitmap,
			int screenWidth, int screenHeight, View cv, String name, int type) {
		int result;
		if (bitmap == null) {
			return -1;
		}

		String savePath = initSDCardDir(ctx, type);
		String fileurl = savePath + name;

		try {
			createFile(fileurl, true);
			File file = new File(fileurl);
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			result = -1;
			e.printStackTrace();
		}
		result = 1;
		return result;
	}

	/**
	 * 创建文件或覆盖文件 【不管文件目录是否存在】
	 * 
	 * @param filePath
	 *            文件路径
	 * @param bool
	 *            是否覆盖 true 覆盖 false 不覆盖
	 * @return File 文件对象
	 */
	public static File createFile(String filePath, boolean bool) {
		try {
			File file = new File(filePath);

			if (!file.exists()) {// 不存在
				File parent = file.getAbsoluteFile().getParentFile();// 获得父级目录
				if (parent.exists() || parent.mkdirs())// 目录不存在则创建目录
					if (file.createNewFile())// 创建文件
						return file;

				return null;
			} else if (bool) {// 存在 是否覆盖
				file.delete();
				file.createNewFile();
			}

			return file;
		} catch (IOException e) {
			return null;
		}
	}

	public static boolean isExist(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * dip转换成px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// sdcard是否可读写
	public static boolean IsCanUseSdCard() {
		try {
			return android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取程序外部的缓存目录
	 * 
	 * @param context
	 * @return
	 */
	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	// 初始化SD卡文件目录
	public static String initSDCardDir(Context ctx, int type) {
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(ctx)
				.getPath() : ctx.getCacheDir().getPath();
		File PHOTO_DIR = new File(cachePath + File.separator + "DCIM");
		PHOTO_DIR.mkdirs();
		String savePath = PHOTO_DIR.getAbsolutePath();
		// 创建二级目录
		if (type == 1) {
			savePath += "/video/";
		} else if (type == 2) {
			savePath += "/videoPhoto/";
		} else if (type == 3) {
			savePath += "/monitorPhoto/";
		} else if (type == 4) {
			savePath += "/alarmPhoto/";
		} else {
			savePath += "/photo/";
		}
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		return savePath;
	}

	public static String[] getAppVersionName(Context context) {
		String[] codename = null;
		String versionCode = ""; //
		String versionName = ""; //
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionCode = pi.versionCode + "";
			versionName = pi.versionName;

			codename = new String[] { versionName, versionCode };
			// codename=new String[]{versionCode,versionName};
		} catch (Exception e) {
			codename = new String[] { "0", "" };
			LogMgr.showLog("getAppVersionName->" + e.toString());
		}
		return codename;
	}

	public static int getWidth(Context cx) {
		int width = 0;
		DisplayMetrics dm = null;
		dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		width = dm.widthPixels;
		return width;
	}

	public static int getHeith(Context cx) {
		int height = 0;
		DisplayMetrics dm = null;
		dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		height = dm.heightPixels;
		return height;
	}

	public static String getImsiImei(Context act, String type) {
		String IMSI = "";
		String IMEI = "";
		// TelephonyManager tm =
		// (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		TelephonyManager tm = (TelephonyManager) act
				.getSystemService(Context.TELEPHONY_SERVICE);
		IMSI = tm.getSubscriberId() == null ? "" : tm.getSubscriberId(); // IMSI
		IMEI = tm.getDeviceId(); // IMEI
		boolean right = "imsi".equalsIgnoreCase(type);
		if (right) {
			return IMSI;
		} else if ("imei".equalsIgnoreCase(type)) {
			return IMEI;
		} else {
			return IMSI;
		}
	}

	public static long getDiffTime(String time1, String time2) {
		long minute = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(time1);

			Date d2 = df.parse(time2);

			long diff = d1.getTime() - d2.getTime();

			minute = diff / (1000 * 60);
		} catch (Exception e) {
		}
		return minute;
	}

	public static String getDealTime(Long timeSnap) {
		if (timeSnap != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String d = simpleDateFormat.format(timeSnap);
			try {
				Date date = simpleDateFormat.parse(d);
				Calendar dealcal = Calendar.getInstance();
				dealcal.setTime(date);
				Calendar syscal = Calendar.getInstance();
				int year = syscal.get(Calendar.YEAR);
				int month = syscal.get(Calendar.MONTH);
				int day = syscal.get(Calendar.DAY_OF_MONTH);
				if (dealcal.get(Calendar.YEAR) == year) {
					if (dealcal.get(Calendar.MONTH) == month) {
						if (dealcal.get(Calendar.DAY_OF_MONTH) == day) {
							return (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
									.get(Calendar.HOUR_OF_DAY) : "0"
									+ dealcal.get(Calendar.HOUR_OF_DAY))
									+ ":"
									+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
											.get(Calendar.MINUTE)
											: ("0" + dealcal
													.get(Calendar.MINUTE)));
						} else {
							return (dealcal.get(Calendar.MONTH) + 1)
									+ "-"
									+ dealcal.get(Calendar.DAY_OF_MONTH)
									+ " "
									+ (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
											.get(Calendar.HOUR_OF_DAY) : "0"
											+ dealcal.get(Calendar.HOUR_OF_DAY))
									+ ":"
									+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
											.get(Calendar.MINUTE)
											: ("0" + dealcal
													.get(Calendar.MINUTE)));
						}
					} else {
						return (dealcal.get(Calendar.MONTH) + 1)
								+ "-"
								+ dealcal.get(Calendar.DAY_OF_MONTH)
								+ " "
								+ (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
										.get(Calendar.HOUR_OF_DAY) : "0"
										+ dealcal.get(Calendar.HOUR_OF_DAY))
								+ ":"
								+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
										.get(Calendar.MINUTE) : ("0" + dealcal
										.get(Calendar.MINUTE)));
					}
				} else {
					return dealcal.get(Calendar.YEAR)
							+ "-"
							+ (dealcal.get(Calendar.MONTH) + 1)
							+ "-"
							+ dealcal.get(Calendar.DAY_OF_MONTH)
							+ " "
							+ (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
									.get(Calendar.HOUR_OF_DAY) : "0"
									+ dealcal.get(Calendar.HOUR_OF_DAY))
							+ ":"
							+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
									.get(Calendar.MINUTE) : ("0" + dealcal
									.get(Calendar.MINUTE)));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return timeSnap + "";
	}

	public static String getSubStringPrefix(String text, String mark) {
		if (!TextUtils.isEmpty(text)) {
			if (text.contains(mark)) {
				String str = text.substring(0, text.lastIndexOf(mark));
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				try {
					Date date = simpleDateFormat.parse(str);
					Calendar dealcal = Calendar.getInstance();
					dealcal.setTime(date);
					Calendar syscal = Calendar.getInstance();
					int year = syscal.get(Calendar.YEAR);
					@SuppressWarnings("unused")
					int month = syscal.get(Calendar.MONTH);
					int day = syscal.get(Calendar.DAY_OF_MONTH);
					if (dealcal.get(Calendar.YEAR) == year) {
						if (dealcal.get(Calendar.DAY_OF_MONTH) == day) {
							return (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
									.get(Calendar.HOUR_OF_DAY) : "0"
									+ dealcal.get(Calendar.HOUR_OF_DAY))
									+ ":"
									+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
											.get(Calendar.MINUTE)
											: ("0" + dealcal
													.get(Calendar.MINUTE)));
						} else {
							return (dealcal.get(Calendar.MONTH) + 1)
									+ "-"
									+ dealcal.get(Calendar.DAY_OF_MONTH)
									+ " "
									+ (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
											.get(Calendar.HOUR_OF_DAY) : "0"
											+ dealcal.get(Calendar.HOUR_OF_DAY))
									+ ":"
									+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
											.get(Calendar.MINUTE)
											: ("0" + dealcal
													.get(Calendar.MINUTE)));
						}
					} else {
						return dealcal.get(Calendar.YEAR)
								+ "-"
								+ (dealcal.get(Calendar.MONTH) + 1)
								+ "-"
								+ dealcal.get(Calendar.DAY_OF_MONTH)
								+ " "
								+ (dealcal.get(Calendar.HOUR_OF_DAY) > 9 ? dealcal
										.get(Calendar.HOUR_OF_DAY) : "0"
										+ dealcal.get(Calendar.HOUR_OF_DAY))
								+ ":"
								+ (dealcal.get(Calendar.MINUTE) > 9 ? dealcal
										.get(Calendar.MINUTE) : ("0" + dealcal
										.get(Calendar.MINUTE)));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return text;
	}

	public static String getSubStringByTag(String text, String mark) {
		if (!TextUtils.isEmpty(text)) {
			if (text.contains(mark)) {
				return text.substring(0, text.lastIndexOf(mark));
			}
		}
		return text;
	}

	public static String getWeekOfDate() {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String getWeekTranDate(String data) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = dateFormat.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (date != null) {
			cal.setTime(date);
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
			if (w < 0)
				w = 0;
			return weekDays[w];
		} else {
			return weekDays[0];
		}
	}

	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeMD5(String str) {
		if (str == null)
			return str;
		return MD5.compile(str);
		// return encrypt(str.getBytes()).toString();
	}

	/**
	 * 判断白天和黑夜
	 * 
	 * @return
	 */
	public static boolean isNight() {
		Date date = new Date();
		@SuppressWarnings("deprecation")
		long hours = date.getHours();
		if (hours < 19 && hours > 6) {
			return false;
		}
		return true;
	}

	// 退出程序(退出前确认)
	public static void exitLogin(final Activity activity, View view) {

		if (activity == null || view == null) {
			return;
		}
		Log.e("exitLogin","activity:"+activity+"---------view:"+view);
		JPushInterface.stopPush(activity);

		// exitAll(activity);

		ActivityExitPopupWindow p = ActivityExitPopupWindow
				.createPopupWindow(activity);

		p.setOnClickCallBack(new OnClickCallBack() {

			@Override
			public void setOnClick(String type) {
				if (FlowConsts.ZERO.equals(type)) {// 退出

					cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
							new String[][] { { "userid", "" } });
					PreferencesUtils.putString(activity, "is_first_main", "N");
					Intent mIntent = new Intent();
					mIntent.setClass(activity, MainActivity.class);
					// 这里设置flag还是比较 重要的
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// 发出退出程序指示
					mIntent.putExtra("flag", EXIT_APPLICATION);
					mIntent.putExtra("from", "exit");
					mIntent.putExtra("exitall", EXIT_APPLICATIONFLAG);
					activity.startActivity(mIntent);
					activity.finish();
				} else if (FlowConsts.ONE.equals(type)) {// 重新登录

					cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
							new String[][] { { "userid", "" } });
					PreferencesUtils.putString(activity, "is_first_main", "N");
					Intent mIntent = new Intent();
					mIntent.setClass(activity, LoginActivity.class);
					// 这里设置flag还是比较 重要的
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// 发出退出程序指示
					mIntent.putExtra("flag", EXIT_APPLICATION);
					mIntent.putExtra("from", "exit");		
					mIntent.putExtra("exitall", EXIT_LOGINFLAG);
					activity.startActivity(mIntent);
					activity.finish();
				} else {
					exitAll(activity);
				}

			}
		});
		Log.e("MYTAG","p="+p.isShow());
		if(p.isShow())
			p.dimissDialog();
		p.showAtLocation(view, Gravity.FILL, 0, 0,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

	}

	// 退出程序(退出前确认)
	public static void exitLogin(final Activity activity) {
		JPushInterface.stopPush(activity);

		cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
				new String[][] { { "userid", "" } });
		Intent mIntent = new Intent();
		mIntent.setClass(activity, LoginActivity.class);
		// 这里设置flag还是比较 重要的
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 发出退出程序指示
		mIntent.putExtra("flag", EXIT_APPLICATION);
		mIntent.putExtra("from", "exit");
		mIntent.putExtra("exitall", EXIT_LOGINFLAG);
		activity.startActivity(mIntent);

	}

	// 退出程序(退出前确认)
	public static void exitAll(Activity activity) {
		Intent mIntent = new Intent();
		mIntent.setClass(activity, MainActivity.class);
		// 这里设置flag还是比较 重要的
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// 发出退出程序指示
		mIntent.putExtra("flag", EXIT_APPLICATION);
		mIntent.putExtra("from", "exit");
		mIntent.putExtra("exitall", EXIT_APPLICATIONFLAG);
		activity.startActivity(mIntent);

	}

	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = "";
		time = simpleDateFormat.format(new Date());
		return time;
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateAfter(int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		Date date = now.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String time = "";
		time = simpleDateFormat.format(date);
		return time;
	}

	public static String getCurrentDaySysTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}