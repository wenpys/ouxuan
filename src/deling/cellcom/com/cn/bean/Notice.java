package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Root;

/**
 * 小区通知
 * @author xpw
 *
 */
@Root
public class Notice {
	private int id;	//通知id
	private String areaname;//小区名字
	private String title;	//标题
	private String content;	//内容
	private String time;	//发布时间
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
