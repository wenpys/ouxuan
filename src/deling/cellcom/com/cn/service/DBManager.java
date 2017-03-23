package deling.cellcom.com.cn.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

public class DBManager {
	public static final String DB_NAME = "ykdatabase.db"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "com.yk.yk_doctor";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME+"/databases/"; // 在手机里存放数据库的位置
	public DBManager() {
		// TODO Auto-generated constructor stub
	}
	// 导如区域及视频点数据
	public void importData(Context context) {
		// 检查 SQLite 数据库文件是否存在
		// if ((new File(DB_PATH + DB_NAME)).exists() == false) {
		// 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
		File f = new File(DB_PATH);
		// 如 database 目录不存在，新建该目录
		if (!f.exists()) {
			f.mkdir();
		}
		try {
			// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
			InputStream is = null;
			is = context.getApplicationContext().getAssets()
					.open("basedata/" + DB_NAME);
			// 输出流
			OutputStream os = new FileOutputStream(DB_PATH+ DB_NAME);
			// 文件写入
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			// 关闭文件流
			os.flush();
			os.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}