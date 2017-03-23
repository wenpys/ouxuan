package deling.cellcom.com.cn.bean;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Yzm {
	@Element(required = false)
	private String result;//标记
	@Element(required = false)
	private String userid;//返回结果的中文描�?
	@Element(required = false)
	private String token;
	private List<LoginInfo> loginInfos;//返回请求的处理结果内�?,XML/JSON字符�?
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<LoginInfo> getLoginInfos() {
		return loginInfos;
	}
	public void setLoginInfos(List<LoginInfo> loginInfos) {
		this.loginInfos = loginInfos;
	}
}
