package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class VillageComm {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@Element(required = false)
	private Village body;
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public String getReturnmessage() {
		return returnmessage;
	}
	public void setReturnmessage(String returnmessage) {
		this.returnmessage = returnmessage;
	}
	public Village getBody() {
		return body;
	}
	public void setBody(Village body) {
		this.body = body;
	}
	
}
