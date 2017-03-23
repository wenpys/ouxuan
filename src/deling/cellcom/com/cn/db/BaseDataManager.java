package deling.cellcom.com.cn.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.db.sqlite.DbModel;
import net.tsz.afinal.db.sqlite.SqlInfo;
import net.tsz.afinal.db.table.TableInfo;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import deling.cellcom.com.cn.activity.MyApplication;
import deling.cellcom.com.cn.bean.Area;
import deling.cellcom.com.cn.bean.AreaNotice;
import deling.cellcom.com.cn.bean.City;
import deling.cellcom.com.cn.bean.Notice;
import deling.cellcom.com.cn.bean.OpenLog;
import deling.cellcom.com.cn.bean.Province;

public class BaseDataManager {

	private static BaseDataManager managerCity;
	private static Context context;

	public static synchronized BaseDataManager getInstance(Context con) {
		if (managerCity == null) {
			managerCity = new BaseDataManager();
		}
		context = con;
		return managerCity;
	}

	// 获取省份信息
	public List<Province> getAllProvince() {
		return DbHelp.getInstance(context).findAll(Province.class, " pid asc");
	}

	// 获取所有城市
	public List<City> getAllCity() {
		List<City> cities = DbHelp.getInstance(context).findAll(City.class);
		return cities;
	}

	// 获取城市
	public List<City> getCityByProvince(String provinceid) {
		List<City> cities = DbHelp.getInstance(context).findAllByWhere(
				City.class, " pid = '" + provinceid + "'");
		return cities;
	}

	// 获取所有区域
	public List<Area> getAllArea() {
		List<Area> areas = DbHelp.getInstance(context).findAll(Area.class);
		return areas;
	}

	// 获取区域
	public List<Area> getAreaByCity(String cityid) {
		List<Area> areas = DbHelp.getInstance(context).findAllByWhere(
				Area.class, " cid = '" + cityid + "'");
		return areas;
	}

	// 获取区域by区域id
	public Area getAreaByid(String areaid) {
		List<Area> areas = DbHelp.getInstance(context).findAllByWhere(
				Area.class, " id = '" + areaid + "'");
		if (areas.size() > 0) {
			return areas.get(0);
		}
		return null;
	}

	// 更新省份
	public void updateProvince(String result,String name) {
		File path=new File("/sdcard/myFolder/");
		if(!path.exists()){
			path.mkdir();
		}
		path=new File("/sdcard/myFolder/deling/");
		if(!path.exists()){
			path.mkdir();
		}
		String newPath="/sdcard/myFolder/deling/"+name+".txt";
		File file=new File(newPath);
		try {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			FileOutputStream stream=new FileOutputStream(file,false);
			try {
				stream.write(result.getBytes());
				stream.close();
				Log.e("MYTAG", "文件写入成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		for (int i = 0; i < provinces.size(); i++) {
//			if ("Y".equalsIgnoreCase(provinces.get(i).getVeriosnstatus())) {
//				if (DbHelp
//						.getInstance(context)
//						.findAllByWhere(Province.class,
//								" pid=" + provinces.get(i).getPid()).size() > 0) {
//					DbHelp.getInstance(context).update(provinces.get(i),
//							" pid=" + provinces.get(i).getPid());
//				} else {
//					DbHelp.getInstance(context).save(provinces.get(i));
//				}
//			} else {
//				DbHelp.getInstance(context).deleteByWhere(Province.class,
//						" pid=" + provinces.get(i).getPid());
//			}
//		}
	}

	// 更新城市
	public void updateCity(List<City> cities) {
		for (int i = 0; i < cities.size(); i++) {
			if ("Y".equalsIgnoreCase(cities.get(i).getVeriosnstatus())) {
				if (DbHelp
						.getInstance(context)
						.findAllByWhere(City.class,
								" cid=" + cities.get(i).getCid()).size() > 0) {
					DbHelp.getInstance(context).update(cities.get(i),
							" cid=" + cities.get(i).getCid());
				} else {
					DbHelp.getInstance(context).save(cities.get(i));
				}
			} else {
				DbHelp.getInstance(context).deleteByWhere(City.class,
						" cid=" + cities.get(i).getCid());
			}
		}
	}

	// 更新区域
	public void updateArea(List<Area> areas) {
		for (int i = 0; i < areas.size(); i++) {
			if ("Y".equalsIgnoreCase(areas.get(i).getVeriosnstatus())) {
				if (DbHelp
						.getInstance(context)
						.findAllByWhere(Area.class,
								" id=" + areas.get(i).getId()).size() > 0) {
					DbHelp.getInstance(context).update(areas.get(i),
							" id=" + areas.get(i).getId());
				} else {
					DbHelp.getInstance(context).save(areas.get(i));
				}
			} else {
				DbHelp.getInstance(context).deleteByWhere(Area.class,
						" id=" + areas.get(i).getId());
			}
		}
	}

	// 获取省份信息
	public List<Province> getProvinceByName(String name) {
		return DbHelp.getInstance(context).findAllByWhere(Province.class,
				" name like '%" + name + "%'");
	}

	// 获取城市信息
	public List<City> getCityByNameAndPid(String pid, String name) {
		return DbHelp.getInstance(context).findAllByWhere(City.class,
				" name like '%" + name + "%' and pid='" + pid + "'");
	}

	// 获取区域信息
	public List<Area> getAreaByNameAndCid(String cid, String name) {
		return DbHelp.getInstance(context).findAllByWhere(Area.class,
				" name like '%" + name + "%' and cid='" + cid + "'");
	}
	public void deleteAll(){
		DbHelp.getInstance(context).deleteAll(Province.class);
		DbHelp.getInstance(context).deleteAll(City.class);
		DbHelp.getInstance(context).deleteAll(Area.class);
	}
	//添加小区消息进数据库
	public void saveAreaNotice(AreaNotice notice){
		DbHelp.getInstance(context).save(notice);
	}
	//更新小区消息数据
	public void updateAreaNotice(AreaNotice notice){
		DbHelp.getInstance(context).update(notice, " id="+"'"+notice.getId()+"'");
	}
	//获取所有的小区信息
	public List<AreaNotice> getAllAreaNotice(){
		int userid=MyApplication.getInstances().getUserid();
		List<AreaNotice> notices=null;
		try {
			notices=DbHelp.getInstance(context).findAllByWhere(AreaNotice.class,"userid="+"'"+userid+"'","time desc");
		} catch (SQLiteException e) {
			Log.e("TAG", e.toString());
		}
		return notices;
	}
	//删除数据库的小区信息
	public void deleteAllNotice(){
		TableInfo table = TableInfo.get(AreaNotice.class);
		DbHelp.getInstance(context).exeSql("alter table "+table.getTableName()+" add userid TEXT");
		//DbHelp.getInstance(context).dropTable(AreaNotice.class);
	}
	//获取未读小区信息数量
	public int getAreaNoticeNum(){
		int userid=MyApplication.getInstances().getUserid();
		TableInfo table = TableInfo.get(AreaNotice.class);
		try {
			DbModel dbModel = DbHelp.getInstance(context).findDbModelBySQL(
					"select count(*) as count from "+table.getTableName()+" where isRead = 0 and userid="+"'"+userid+"'");
			return dbModel.getInt("count");
		} catch (Exception e) {
			return 0;
		}
	}
	//更改本地小区信息状态
	public void changeNoticeState(String readIds){
		TableInfo table = TableInfo.get(AreaNotice.class);
		String sql = "update "+table.getTableName()+" set isRead = 1 where id in("+readIds+")";
		DbModel dbModel = DbHelp.getInstance(context).findDbModelBySQL(sql);
	}
	//获取小区信息的最大ID
	public String getMaxAreaNoticeId(){
		int userid=MyApplication.getInstances().getUserid();
		List<AreaNotice> notices=DbHelp.getInstance(context).findAllByWhere(AreaNotice.class,"userid="+"'"+userid+"'","id desc");
		if(notices.size() > 0)
			return notices.get(0).getId()+"";
		else
			return "0";
	}
	
	//添加离线开门日志
	public void saveOfflineLog(OpenLog openLog){
		DbHelp.getInstance(context).save(openLog);
	}

	//获取所有开门日志
	public List<OpenLog> getAllOpenLog(){
		List<OpenLog> openLog=DbHelp.getInstance(context).findAllByWhere(OpenLog.class,"","opentime asc");
		return openLog;
	}
	
	//清空开门日志
	public void clearOpenLog(){
		DbHelp.getInstance(context).deleteAll(OpenLog.class);
	}
}