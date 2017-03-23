package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 门栋
 * @author xpw
 *
 */
@Root
public class Gate {
	@Element(required = false)
	private String id;	//门栋id
	@Element(required = false)
	private String name;//名称
	
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
