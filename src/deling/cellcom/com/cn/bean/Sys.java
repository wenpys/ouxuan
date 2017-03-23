package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * 
 * @author Administrator
 * 
 */
@Root
public class Sys {
	@Element(required = false)
	private String serviceurl;//服务地址
	@Element(required = false)
	private String version;//最新版本
	@Element(required = false)
	private String downurl;//下载地址
	@Element(required = false)
	private String minversion;//最低版本号
	@Element(required = false)
	private String jsonkey;//json加密串
	@Element(required = false)
	private String startgg;//启动广告图片地址
	@Element(required = false)
	private String kfphone;//客服电话
	@Element(required = false)
	private String indexgg;//登陆注册页广告图片地址
	@Element(required = false)
	private String introduce;//升级提示
	@Element(required = false)
	private String token;//认证token
	@Element(required = false)
	private String authstringkey;//authstringkey
	@Element(required = false)
	private String provincedatetime;//省版本号时间
	@Element(required = false)
	private String citydatetime;//市版本号时间
	@Element(required = false)
	private String regiondatetime;//区版本号时间
	@Element(required = false)
	private String sharecontent;//分享内容
	@Element(required = false)
	private String shareurl;//分享地址
	@Element(required = false)
	private String grant_content;//授权分享内容
	@Element(required = false)
	private String grant_url;//授权分享地址
	@Element(required = false)
	private String timer;//定时时间段
	
	
	
	
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getSharecontent() {
		return sharecontent;
	}
	public void setSharecontent(String sharecontent) {
		this.sharecontent = sharecontent;
	}
	public String getShareurl() {
		return shareurl;
	}
	public void setShareurl(String shareurl) {
		this.shareurl = shareurl;
	}
	public String getServiceurl() {
		return serviceurl;
	}
	public void setServiceurl(String serviceurl) {
		this.serviceurl = serviceurl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDownurl() {
		return downurl;
	}
	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getMinversion() {
		return minversion;
	}
	public void setMinversion(String minversion) {
		this.minversion = minversion;
	}
	public String getJsonkey() {
		return jsonkey;
	}
	public void setJsonkey(String jsonkey) {
		this.jsonkey = jsonkey;
	}
	public String getStartgg() {
		return startgg;
	}
	public void setStartgg(String startgg) {
		this.startgg = startgg;
	}
	public String getKfphone() {
		return kfphone;
	}
	public void setKfphone(String kfphone) {
		this.kfphone = kfphone;
	}
	public String getIndexgg() {
		return indexgg;
	}
	public void setIndexgg(String indexgg) {
		this.indexgg = indexgg;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAuthstringkey() {
		return authstringkey;
	}
	public void setAuthstringkey(String authstringkey) {
		this.authstringkey = authstringkey;
	}
	public String getProvincedatetime() {
		return provincedatetime;
	}
	public void setProvincedatetime(String provincedatetime) {
		this.provincedatetime = provincedatetime;
	}
	public String getCitydatetime() {
		return citydatetime;
	}
	public void setCitydatetime(String citydatetime) {
		this.citydatetime = citydatetime;
	}
	public String getRegiondatetime() {
		return regiondatetime;
	}
	public void setRegiondatetime(String regiondatetime) {
		this.regiondatetime = regiondatetime;
	}
	public String getGrant_content() {
		return grant_content;
	}
	public void setGrant_content(String grant_content) {
		this.grant_content = grant_content;
	}
	public String getGrant_url() {
		return grant_url;
	}
	public void setGrant_url(String grant_url) {
		this.grant_url = grant_url;
	}
	
}
