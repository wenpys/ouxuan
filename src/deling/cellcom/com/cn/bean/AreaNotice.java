package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import org.simpleframework.xml.Element;

public class AreaNotice implements Serializable{
	/**
	 * 
	 */
	@Id(column="autoid")
	private int autoid;
	@Element(required=false)
	private int id;
	@Element(required=false)
	private String areaname;
	@Element(required=false)
	private String title;
	@Element(required=false)
	private String content;
	@Element(required=false)
	private String time;
	@Element(required=false)
	private boolean isRead=false;
	@Element(required=false)
	private String userid;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getAutoid() {
		return autoid;
	}
	public void setAutoid(int autoid) {
		this.autoid = autoid;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	} 
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public AreaNotice(int id, String areaname, String title, String content,
			String time, boolean isRead,String userid) {
		super();
		this.id = id;
		this.areaname = areaname;
		this.title = title;
		this.content = content;
		this.time = time;
		this.isRead = isRead;
		this.userid=userid;
	}
	
	public AreaNotice() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "AreaNotice [id=" + id + ", areaname=" + areaname + ", title="
				+ title + ", content=" + content + ", time=" + time
				+ ", isRead=" + isRead +", userid=" + userid +"]";
	}
	
}
