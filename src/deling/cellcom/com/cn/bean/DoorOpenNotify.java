package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 开门通知
 * @author xpw
 *
 */
@Root
public class DoorOpenNotify {
	@Element(required = false)
	private String adid;	//广告id
	@Element(required = false)
	private String ggimgeurl;//广告图片地址
	@Element(required = false)
	private String linkurl;	//广告链接地址
	@Element(required = false)
	private String ggtype;	//广告类型
	@Element(required = false)
	private String content;	//内容
	@Element(required = false)
	private String url;		//7天内使用记录url地址
	
	public String getAdid() {
		return adid;
	}
	public void setAdid(String adid) {
		this.adid = adid;
	}
	public String getGgimgeurl() {
		return ggimgeurl;
	}
	public void setGgimgeurl(String ggimgeurl) {
		this.ggimgeurl = ggimgeurl;
	}
	public String getLinkurl() {
		return linkurl;
	}
	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}
	public String getGgtype() {
		return ggtype;
	}
	public void setGgtype(String ggtype) {
		this.ggtype = ggtype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
