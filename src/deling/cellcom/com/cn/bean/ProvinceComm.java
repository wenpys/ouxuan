package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ProvinceComm {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@Element(required = false)
	private List<Province> body;//返回请求的处理结果内容,XML/JSON字符串


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

	public List<Province> getBody() {
		return body;
	}

	public void setBody(List<Province> body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "ProvinceComm [returncode=" + returncode + ", returnmessage="
				+ returnmessage + ", body=" + body + "]";
	}
	
}
