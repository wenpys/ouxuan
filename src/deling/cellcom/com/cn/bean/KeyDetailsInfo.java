package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class KeyDetailsInfo implements Serializable{
	//城市
	@Element(required=false)
	private String city;
	//区域
	@Element(required=false)
	private String area;
	//小区
	@Element(required=false)
	private String gate;
	//房间
	@Element(required=false)
	private String room;
	//房间id
	@Element(required=false)
	private String roomid;
	//电话
	@Element(required=false)
	private String phone;
	//身份
	@Element(required=false)
	private String type;
	//姓名
	@Element(required=false)
	private String name;
	//申请状态
	@Element(required=false)
	private String state;
	//时间
	@Element(required=false)
	private String logtime;
	//不通过的原因
	@Element(required=false)
	private String content;
	
	public String getRoomid() {
		return roomid;
	}
	public void setRoomid(String roomid) {
		this.roomid = roomid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGate() {
		return gate;
	}
	public void setGate(String gate) {
		this.gate = gate;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "KeyDetailsInfo [city=" + city + ", area=" + area + ", gate="
				+ gate + ", room=" + room + ", phone=" + phone + ", type="
				+ type + ", name=" + name + ", state=" + state + ", logtime="
				+ logtime + ", content=" + content + "]";
	}
	
}
