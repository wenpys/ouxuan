package deling.cellcom.com.cn.utils;

import java.io.File;

/**
 * 监测文件夹下的文件是否存在
 * @author Administrator
 *
 */
public class FileUtils {
	public static boolean checkFile(String type){

		File path=new File("/sdcard/myFolder/");
		if(!path.exists()){
			return false;
		}
		path=new File("/sdcard/myFolder/deling/");
		if(!path.exists()){
			return false;
		}
		path=new File("/sdcard/myFolder/deling/"+type+".txt");
		if(!path.exists()){
			return false;
		}
		return true;
	}
}
