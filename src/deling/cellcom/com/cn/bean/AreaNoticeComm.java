package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;

public class AreaNoticeComm {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@Element(required = false)
	private List<AreaNotice> body;
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
	public List<AreaNotice> getBody() {
		return body;
	}
	public void setBody(List<AreaNotice> body) {
		this.body = body;
	}
	
}
