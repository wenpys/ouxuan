package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 可授权房间钥匙信息
 * @author xpw
 *
 */
@Root
public class RoomKey {
	@Element(required=false)
	private String keyname;		//钥匙名字

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	
	
}
