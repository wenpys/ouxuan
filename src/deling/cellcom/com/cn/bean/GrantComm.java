package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class GrantComm {
	@Element(required = false)
	private String returncode;
	@Element(required = false)
	private String returnmessage;
	private List<Grant> body;

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

	public List<Grant> getBody() {
		return body;
	}

	public void setBody(List<Grant> body) {
		this.body = body;
	}

}
