package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 钥匙信息
 * @author xpw
 *
 */
@Root
public class Keyinfo implements Serializable{
	@Element(required=false)
	private String id;	//锁id
	@Element(required=false)
	private String lock_id;//开锁密钥
	@Element(required=false)
	private String departid;
	@Element(required=false)
	private String pid;
	@Element(required=false)
	private String userphone;
	@Element(required=false)
	private String gatename;
	@Element(required=false)
	private String areaname;
	@Element(required=false)
	private String valuetime;
	@Element(required=false)
	private String keyname;
	
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	public String getDepartid() {
		return departid;
	}
	public void setDepartid(String departid) {
		this.departid = departid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLock_id() {
		//服务器传回的id是10进制的 需要转为16进制
//		int pwd=0;
//		try {
//			pwd=Integer.valueOf(lock_id);
//		} catch (Exception e) {
//			pwd=305419896;
//		}
		return Integer.toHexString(Integer.valueOf(lock_id));
	}
	public void setLock_id(String lock_id) {
		this.lock_id = lock_id;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getGatename() {
		return gatename;
	}
	public void setGatename(String gatename) {
		this.gatename = gatename;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getValuetime() {
		return valuetime;
	}
	public void setValuetime(String valuetime) {
		this.valuetime = valuetime;
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof Keyinfo){
			Keyinfo info=(Keyinfo) o;
			if(info.getPid().equals(this.getPid())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	@Override
	public int hashCode() {
		return this.getPid().hashCode()*13;
	}

	@Override
	public String toString() {
		return "Keyinfo [id=" + id + ", lock_id=" + lock_id + "]";
	}
}
