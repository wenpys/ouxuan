package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class SubNotice {
	@Element(required=false)
	private String applystate;
	@Element(required=false)
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getApplystate() {
		return applystate;
	}

	public void setApplystate(String applystate) {
		this.applystate = applystate;
	}
	
}
