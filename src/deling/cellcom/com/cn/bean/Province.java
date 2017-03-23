package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name="provincelist")
public class Province implements Serializable{

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	private String id;//id
	private String pid;//省id
	private String name;//省名
	private String veriosnstatus;//版本信息
	@Transient
	private boolean isCheck;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVeriosnstatus() {
		return veriosnstatus;
	}
	public void setVeriosnstatus(String veriosnstatus) {
		this.veriosnstatus = veriosnstatus;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public Province(String id, String pid, String name, String veriosnstatus,
			boolean isCheck) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.veriosnstatus = veriosnstatus;
		this.isCheck = isCheck;
	}
	public Province() {
		super();
	}
	@Override
	public String toString() {
		return "[id=" + id + ", pid=" + pid + ", name=" + name
				+ ", veriosnstatus=" + veriosnstatus + ", isCheck=" + isCheck
				+ "]";
	}
	
}