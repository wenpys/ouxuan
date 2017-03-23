package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class VirtualKey {
	@Element(required=false)
	private String id;
	@Element(required=false)
	private String password;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public VirtualKey() {
		super();
	}
	
}
