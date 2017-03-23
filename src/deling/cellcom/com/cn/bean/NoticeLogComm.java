package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class NoticeLogComm implements Serializable{
	@Element(required = false)
	private String returncode;
	@Element(required = false)
	private String returnmessage;
	@Element(required = false)
	private NoticeLog body;
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
	public NoticeLog getBody() {
		return body;
	}
	public void setBody(NoticeLog body) {
		this.body = body;
	}
	
}
