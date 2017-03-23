package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 房号
 * @author xpw
 *
 */
@Root
public class Door {
	@Element(required = false)
	private String id;	//房号id
	@Element(required = false)
	private String name;//房间名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
