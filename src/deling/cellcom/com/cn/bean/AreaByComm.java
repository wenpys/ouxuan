package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;

public class AreaByComm {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@Element(required = false)
	private List<AreaBy> body;//返回请求的处理结果内容,XML/JSON字符串
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
	public List<AreaBy> getBody() {
		return body;
	}
	public void setBody(List<AreaBy> body) {
		this.body = body;
	}
	
}
