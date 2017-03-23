package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 登录
 * @author zsw
 *
 */
@Root
public class Autho {
	@Element(required = false)
	private String phone;//被授权号码
	@Element(required = false)
	private String name;//被授权名称
	@Element(required = false)
	private String starttime;//开始授权时间
	@Element(required = false)
	private String state;//授权状态
	
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
	
	
}
