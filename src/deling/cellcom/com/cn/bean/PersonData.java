package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class PersonData {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描�?
	@Element(required = false)
	private PersonInfo body;//返回请求的处理结果内�?,XML/JSON字符�?
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
	public PersonInfo getBody() {
		return body;
	}
	public void setBody(PersonInfo body) {
		this.body = body;
	}


}
