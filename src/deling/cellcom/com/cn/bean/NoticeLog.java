package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class NoticeLog implements Serializable{
	@Element(required=false)
	private String noticeids;

	public String getNoticeids() {
		return noticeids;
	}

	public void setNoticeids(String noticeids) {
		this.noticeids = noticeids;
	}
	
}
