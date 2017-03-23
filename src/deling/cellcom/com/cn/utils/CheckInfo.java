package deling.cellcom.com.cn.utils;

import java.io.File;

import net.tsz.afinal.http.HttpHandler;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import deling.cellcom.com.cn.activity.main.LaunchActivity;
import deling.cellcom.com.cn.bean.SysComm;
import deling.cellcom.com.cn.service.DBManager;
import deling.cellcom.com.cn.service.UpBaseDataService;
import deling.cellcom.com.cn.widget.UpdateApkSheet;


/**
 * 检查信息
 * 
 * @author Administrator
 * 
 */
public class CheckInfo {

	private static UpdateApkSheet updateApkSheet;

	@SuppressWarnings("static-access")
	public static UpdateApkSheet checkInfo(final SysComm sysComm,
			final Activity activity, final HttpHandler<File> handler,
			final long begintime) {
		String downloadurl = sysComm.getBody().getDownurl();
		final String version = sysComm.getBody().getVersion();
		String minversion = sysComm.getBody().getMinversion();
		Double oldversion = Double.parseDouble(ContextUtil
				.getAppVersionName(activity)[0]);
		final String introduce = sysComm.getBody().getIntroduce();
		final String startgg = sysComm.getBody().getStartgg();
		final String indexgg = sysComm.getBody().getIndexgg();
                           
		cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
				new String[][] { { "token", sysComm.getBody().getToken() } });
		cellcom.com.cn.util.SharepreferenceUtil.saveData(activity,
				new String[][] { { "authstringkey",
						sysComm.getBody().getAuthstringkey() } });
		cellcom.com.cn.util.SharepreferenceUtil
				.saveData(activity, new String[][] { { "deskey",
						sysComm.getBody().getJsonkey() } });
		PreferencesUtils.putString(activity, "downloadurl", downloadurl);
		 PreferencesUtils.putString(activity, "startgg", startgg);
		PreferencesUtils.putString(activity, "indexgg", indexgg);

		// 分享内容保存
		PreferencesUtils.putString(activity, "shareurl", sysComm.getBody()
				.getShareurl());

		PreferencesUtils.putString(activity, "sharecontent", sysComm.getBody()
				.getSharecontent());
		startUpdateService(sysComm, activity);
		//改为自动更新
		/*if (Double.parseDouble(minversion) > oldversion) {
			updateApkSheet = new UpdateApkSheet(activity);
			updateApkSheet.setOnActionSheetSelected(new OnActionSheetSelected() {

						@Override
						public void onClick(int whichButton) {
							if (whichButton == 1) {
								// 立即更新
								activity.downLoadApk(version, updateApkSheet);
							} else {
								// 暂未更新
								if (handler != null) {
									handler.stop();
								}
								updateApkSheet.dismiss();
								activity.finish();
							}
						}
					});
			updateApkSheet.showSheet(activity);
			updateApkSheet.setData(version, introduce);
			updateApkSheet.setljgxtvTxt(activity.getResources().getString(R.string.ljgx));
			updateApkSheet.setzwgxtvTxt(activity.getResources().getString(R.string.qxgx));
			updateApkSheet.setzxbbmstvVISIBLEORGONE(View.VISIBLE);
			updateApkSheet.setProgressVISIBLEORGONE(View.GONE);
		} else if (Double.parseDouble(version) > oldversion) {
			updateApkSheet = new UpdateApkSheet(activity);
			updateApkSheet.setOnActionSheetSelected(new OnActionSheetSelected() {

						@Override
						public void onClick(int whichButton) {
							if (whichButton == 1) {
								// 立即更新
								activity.downLoadApk(version, updateApkSheet);
							} else {
								// 暂未更新
								loadDataBase(activity);
								startUpdateService(sysComm, activity);
								updateApkSheet.dismiss();
								Intent intent = new Intent(activity,LoginActivity.class);
								activity.startActivity(intent);
								activity.finish();
							}
						}
					});
			updateApkSheet.showSheet(activity);
			updateApkSheet.setData(version, introduce);
			updateApkSheet.setzwgxtvTxt(activity.getResources().getString(R.string.zbgx));
			
			updateApkSheet.setzxbbmstvVISIBLEORGONE(View.VISIBLE);
			updateApkSheet.setProgressVISIBLEORGONE(View.GONE);
		} else {
			loadDataBase(activity);
			startUpdateService(sysComm, activity);
//			long endtime = System.currentTimeMillis();// 时间戳
//			if (endtime - begintime < 4000) {
//				try {
//					Thread.sleep(4000 - (endtime - begintime));
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			String isFirstLogin = PreferencesUtils.getString(activity,
					"is_first_login");
			if (TextUtils.isEmpty(isFirstLogin)) {// 首次使用
				PreferencesUtils.putString(activity, "is_first_login", "Y");
				//默认选择记住帐号，密码
				PreferencesUtils.putBoolean(activity, "isrememb", true);
				Intent intent = new Intent(activity, GuideActivity.class);
				activity.startActivityForResult(intent, FlowConsts.ADDPAIBAN);
				activity.finish();
			} else {
				Intent loginintent = new Intent(activity,LoginActivity.class);
				activity.startActivity(loginintent);
				activity.finish();
			}
		}*/
		return updateApkSheet;

	}

	private static void startUpdateService(SysComm sysComm,
			Activity activity) {
		String oldversiontime_s;
		String oldversiontime_c;
		String oldversiontime_a;
		oldversiontime_s = PreferencesUtils.getString(activity,
				"oldversiontime_s");
		oldversiontime_c = PreferencesUtils.getString(activity,
				"oldversiontime_c");
		oldversiontime_a = PreferencesUtils.getString(activity,
				"oldversiontime_a");
		// 启动一个Service,对省、市、区数据进行同步
		Intent intent = new Intent(activity, UpBaseDataService.class);
		intent.putExtra("oldversiontime_s", oldversiontime_s);
		intent.putExtra("oldversiontime_c", oldversiontime_c);
		intent.putExtra("oldversiontime_a", oldversiontime_a);

		intent.putExtra("newversiontime_s", sysComm.getBody()
				.getProvincedatetime());
		intent.putExtra("newversiontime_c", sysComm.getBody()
				.getCitydatetime());
		intent.putExtra("newversiontime_a", sysComm.getBody()
				.getRegiondatetime());
		activity.startService(intent);
	}

	private static void loadDataBase(LaunchActivity activity) {
		// 加载数据库
		String isImport = PreferencesUtils.getString(activity, "is_import");
		if (TextUtils.isEmpty(isImport) || "N".equalsIgnoreCase(isImport)) {
			DBManager dbHelper = new DBManager();
			dbHelper.importData(activity);
			PreferencesUtils.putString(activity, "oldversiontime_s",
					"2015-11-27 16:38:18");//初始化数据库版本时间--省
			PreferencesUtils.putString(activity, "oldversiontime_c",
					"2015-11-27 16:38:19");//初始化数据库版本时间--市
			PreferencesUtils.putString(activity, "oldversiontime_a",
					"2015-11-27 16:38:19");//初始化数据库版本时间--区
		}
		PreferencesUtils.putString(activity, "is_import", "Y");
	}

}
