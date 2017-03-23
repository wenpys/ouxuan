package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;
/**
 * 
 * @author zsw	城市bean
 *
 */
@Table(name="citylist")
public class City implements Serializable{

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	private String id;//id
	private String cid;//城市id
	private String pid;//省id
	private String name;//城市名
	private String veriosnstatus;//版本信息学
	@Transient
	private boolean isCheck;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
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

	public City(String id, String cid, String pid, String name,
			String veriosnstatus, boolean isCheck) {
		super();
		this.id = id;
		this.cid = cid;
		this.pid = pid;
		this.name = name;
		this.veriosnstatus = veriosnstatus;
		this.isCheck = isCheck;
	}
	@Override
	public String toString() {
		return "City [id=" + id + ", cid=" + cid + ", pid=" + pid + ", name="
				+ name + ", veriosnstatus=" + veriosnstatus + ", isCheck="
				+ isCheck + "]";
	}
	public City() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}