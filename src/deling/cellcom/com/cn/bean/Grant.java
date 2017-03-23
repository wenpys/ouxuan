package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 授权
 * @author zsw
 *
 */
@Root
public class Grant {
	@Element(required = false)
	private String phone;	//被授权号码
	@Element(required = false)
	private String name;	//被授权名称
	@Element(required = false)
	private String roomid;//授权房间id
	@Element(required = false)
	private String areaname;//小区名字
	@Element(required = false)
	private String gatename;//门栋名字
	@Element(required = false)
	private String roomname;//房间名字
	@Element(required = false)
	private String valuetime;//有效时间
	@Element(required = false)
	private String starttime;//开始授权时间
	@Element(required = false)
	private String state;	//授权状态
	@Element(required = false)
	private String id;	//授权id
	@Element(required = false)
	private String type;	//授权来源
	@Element(required = false)
	private String identity;	//家属身份
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getGatename() {
		return gatename;
	}
	public void setGatename(String gatename) {
		this.gatename = gatename;
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public String getValuetime() {
		return valuetime;
	}
	public void setValuetime(String valuetime) {
		this.valuetime = valuetime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}		
	
}
