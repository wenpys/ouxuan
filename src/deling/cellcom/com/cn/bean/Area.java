package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Table;
import net.tsz.afinal.annotation.sqlite.Transient;

@Table(name="arealist")
public class Area  implements Serializable {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1L;
	private String id;//id
	private String cid;//城市id
	private String name;//名称
	private String veriosnstatus;//版本状态
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
	public Area(String id, String cid, String name, String veriosnstatus,
			boolean isCheck) {
		super();
		this.id = id;
		this.cid = cid;
		this.name = name;
		this.veriosnstatus = veriosnstatus;
		this.isCheck = isCheck;
	}
	public Area() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}