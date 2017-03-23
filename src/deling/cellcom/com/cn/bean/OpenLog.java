package deling.cellcom.com.cn.bean;

import net.tsz.afinal.annotation.sqlite.Id;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
/**
 * 开门日志
 * @author xpw
 *
 */
@Root
public class OpenLog {
	@Id(column="id")
	private int id;
	@Element(required = false)
	private String pid;	//门锁设备序列号
	@Element(required = false)
	private int state;//开门状态
	@Element(required = false)
	private String msg;	//描述
	@Element(required = false)
	private String opentime;	//开门时间
	@Element(required = false)
	private int mode;	//开门方式
	
		
	public OpenLog() {
		super();
	}
	
	public OpenLog(String pid, int state, String msg, String opentime, int mode) {
		super();
		this.pid = pid;
		this.state = state;
		this.msg = msg;
		this.opentime = opentime;
		this.mode = mode;
	}	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOpentime() {
		return opentime;
	}
	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	
}
