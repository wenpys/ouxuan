package deling.cellcom.com.cn.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import deling.cellcom.com.cn.db.BaseDataManager;
import deling.cellcom.com.cn.net.FlowConsts;
import deling.cellcom.com.cn.net.HttpHelper;
import deling.cellcom.com.cn.utils.FileUtils;
import deling.cellcom.com.cn.utils.PreferencesUtils;

/**
 * 更新数据
 * 
 * @author wma
 * 
 */
public class UpBaseDataService extends Service {
	private BaseDataManager baseDataManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		baseDataManager = BaseDataManager.getInstance(UpBaseDataService.this);
		if (intent != null) {
			String oldversiontime_s = intent.getStringExtra("oldversiontime_s");
			String oldversiontime_c = intent.getStringExtra("oldversiontime_c");
			String oldversiontime_a = intent.getStringExtra("oldversiontime_a");

			String newversiontime_s = intent.getStringExtra("newversiontime_s");
			String newversiontime_c = intent.getStringExtra("newversiontime_c");
			String newversiontime_a = intent.getStringExtra("newversiontime_a");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				// 省信息
				Date newversiontime_s_date = null;
				Date oldversiontime_s_date = null;
				if (!TextUtils.isEmpty(newversiontime_s)) {
						newversiontime_s_date = simpleDateFormat
								.parse(newversiontime_s);
				}else{
					newversiontime_s_date=simpleDateFormat
							.parse("2016-08-01 16:05:35");
				}
				if (!TextUtils.isEmpty(oldversiontime_s)) {
					oldversiontime_s_date = simpleDateFormat
							.parse(oldversiontime_s);
				}
				if (TextUtils.isEmpty(oldversiontime_s)
						|| (oldversiontime_s_date != null && oldversiontime_s_date
								.before(newversiontime_s_date))||!FileUtils.checkFile("province")) {
					// 更新省份数据
					yk_updateBaseData(FlowConsts.PROVINCE, oldversiontime_s,
							newversiontime_s);
				}

				// 城市信息
				Date newversiontime_c_date = null;
				Date oldversiontime_c_date = null;
				if (!TextUtils.isEmpty(newversiontime_c)) {
					newversiontime_c_date = simpleDateFormat
							.parse(newversiontime_c);
				}else{
					newversiontime_c_date=simpleDateFormat
							.parse("2016-08-01 16:05:35");
				}
				if (!TextUtils.isEmpty(oldversiontime_c)) {
					oldversiontime_c_date = simpleDateFormat
							.parse(oldversiontime_c);
				}
				if (TextUtils.isEmpty(oldversiontime_c)
						|| (oldversiontime_c_date != null && oldversiontime_c_date
								.before(newversiontime_c_date))||!FileUtils.checkFile("city")) {
					// 更新城市数据
					yk_updateBaseData(FlowConsts.CITY, oldversiontime_c,
							newversiontime_c);
				}

				// 更新区域信息
				Date newversiontime_a_date = null;
				Date oldversiontime_a_date = null;
				if (!TextUtils.isEmpty(newversiontime_a)) {
					newversiontime_a_date = simpleDateFormat
							.parse(newversiontime_a);
				}else{
					newversiontime_a_date=simpleDateFormat
							.parse("2016-08-01 16:05:35");
				}
				if (!TextUtils.isEmpty(oldversiontime_a)) {
					oldversiontime_a_date = simpleDateFormat
							.parse(oldversiontime_a);
				}
				if (TextUtils.isEmpty(oldversiontime_a)
						|| (oldversiontime_a_date != null && oldversiontime_a_date
								.before(newversiontime_a_date))||!FileUtils.checkFile("area")) {
					// 更新区域数据
					yk_updateBaseData(FlowConsts.AREA, oldversiontime_a,
							newversiontime_a);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	// 更新数据
	private void yk_updateBaseData(final String datatype, String oldversiontime,
			final String newversiontime) {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("datatype", datatype);
		cellComAjaxParams.put("oldversiontime", oldversiontime);
		HttpHelper.getInstances(UpBaseDataService.this).send(
				FlowConsts.GETPROCITY, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);

					}

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						if (FlowConsts.PROVINCE.equals(datatype)) {
							final String result=cellComAjaxResult.getResult();
							
//							final ProvinceComm baseDataComm = cellComAjaxResult
//									.read(ProvinceComm.class,
//											CellComAjaxResult.ParseType.GSON);
//							if (!FlowConsts.STATUE_1.equals(baseDataComm
//									.getReturncode())) {
//
//								ToastUtils.show(UpBaseDataService.this,
//										baseDataComm.getReturnmessage());
//								return;
//							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									baseDataManager.updateProvince(result,"province");
									PreferencesUtils.putString(
											UpBaseDataService.this,
											"oldversiontime_s", newversiontime);
								}
							}).start();
						} else if (FlowConsts.CITY.equals(datatype)) {
							final String result=cellComAjaxResult.getResult();
//							final CityComm baseDataComm = cellComAjaxResult
//									.read(CityComm.class,
//											CellComAjaxResult.ParseType.GSON);
//							if (!FlowConsts.STATUE_1.equals(baseDataComm
//									.getReturncode())) {
//								ToastUtils.show(UpBaseDataService.this,
//										baseDataComm.getReturnmessage());
//								return;
//							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									baseDataManager.updateProvince(result,"city");
									PreferencesUtils.putString(
											UpBaseDataService.this,
											"oldversiontime_c", newversiontime);
								}
							}).start();
						} else if (FlowConsts.AREA.equals(datatype)) {
							final String result=cellComAjaxResult.getResult();
//							final AreaComm baseDataComm = cellComAjaxResult
//									.read(AreaComm.class,
//											CellComAjaxResult.ParseType.GSON);
//							if (!FlowConsts.STATUE_1.equals(baseDataComm
//									.getReturncode())) {
//								ToastUtils.show(UpBaseDataService.this,
//										baseDataComm.getReturnmessage());
//								return;
//							}
							new Thread(new Runnable() {

								@Override
								public void run() {
									baseDataManager.updateProvince(result,"area");
									PreferencesUtils.putString(
											UpBaseDataService.this,
											"oldversiontime_a", newversiontime);
								}
							}).start();
						}
					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
