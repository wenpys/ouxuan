package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class AreaBy {
	@Element(required = false)
	private String areaid;//标记
	@Element(required = false)
	private String name;//返回结果的中文描述
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
