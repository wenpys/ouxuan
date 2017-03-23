package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class UploadPicInfoBean {
	@Element(required = false)
	private String returncode;//标记
	@Element(required = false)
	private String returnmessage;//返回结果的中文描述
	@ElementList(required = false,type = UpLoadPicBean.class)
	private UpLoadPicBean body = new UpLoadPicBean();//返回请求的处理结果内容,XML/JSON字符串
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
	public UpLoadPicBean getBody() {
		return body;
	}
	public void setBody(UpLoadPicBean body) {
		this.body = body;
	}
}
