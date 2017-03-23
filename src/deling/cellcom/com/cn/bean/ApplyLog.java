package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 申请记录
 * @author xpw
 *
 */
@Root
public class ApplyLog {
	@Element(required = false)
	private String id;
	@Element(required = false)
	private String door;	//小区及门栋及房间信息
	@Element(required = false)
	private String type;	//居住身份
	@Element(required = false)
	private String name;	//真实姓名
	@Element(required = false)
	private String state;	//申请状态
	@Element(required = false)
	private String logtime;	//申请时间
	@Element(required=false)
	private String area;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDoor() {
		return door;
	}
	public void setDoor(String door) {
		this.door = door;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLogtime() {
		return logtime;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	
	
}
