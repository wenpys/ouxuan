package deling.cellcom.com.cn.db;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbHelp {
	private static int version=14;
	private static FinalDb finalDb;
	private static String DataBaseName="deling.db";
	public static String DataBaseTableName1="provincelist";//法律法规类型表
	public static String DataBaseTableName2="citylist";//法律法规搜索关键字表
	
	public static String DataBaseTableName3="arealist";//我的定制
	public static String DataBaseTableName4="wddzsetsignalname";//我的定制
	
	public static String DataBaseTableName5="yujingarea";//我的定制
	public static String DataBaseTableName6="tingkearea";//我的定制
	
	public static String DataBaseTableName7="wddzyujingcomm";//我的定制
	public static String DataBaseTableName8="wddzyujing";//我的定制
	
	
	private DbHelp() {
	}
	public static synchronized FinalDb getInstance(Context context){
		if(finalDb!=null){
			return finalDb;
		}else{
			finalDb=FinalDb.create(context, DataBaseName, true, version, new DbUpdateListener() {
				
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName1);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName2);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName3);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName4);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName5);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName6);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName7);
					db.execSQL("DROP TABLE IF EXISTS "+DataBaseTableName8);
				}
			});
			return finalDb;
		}
	}
}
