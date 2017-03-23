package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class KeyDetailsInfoComm {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@Element(required = false)
	private KeyDetailsInfo body;
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
	public KeyDetailsInfo getBody() {
		return body;
	}
	public void setBody(KeyDetailsInfo body) {
		this.body = body;
	}
	
}
