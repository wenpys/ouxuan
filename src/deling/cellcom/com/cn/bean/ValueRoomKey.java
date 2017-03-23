package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 可授权房间
 * @author xpw
 *
 */
@Root
public class ValueRoomKey {
	@Element(required=false)
	private String roomid;		//房间id
	@Element(required=false)
	private String areaname;	//小区名字
	@Element(required=false)
	private String gatename;	//gatename
	@Element(required=false)
	private String roomname;	//房间名字
	@Element(required=false)
	private String door;		//小区及门栋及房间信息
	@Element(required=false)
	private List<RoomKey> keys;	//包含钥匙信息
	
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
	public String getDoor() {
		return door;
	}
	
	public void setDoor(String door) {
		this.door = door;
	}
	
	public List<RoomKey> getKeys() {
		return keys;
	}
	
	public void setKeys(List<RoomKey> keys) {
		keys = keys;
	}
	
}
