package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class DoorOpenNotifyComm {
	@Element(required = false)
	private String returncode;
	@Element(required = false)
	private String returnmessage;
	private Grant body;

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

	public Grant getBody() {
		return body;
	}

	public void setBody(Grant body) {
		this.body = body;
	}

}
