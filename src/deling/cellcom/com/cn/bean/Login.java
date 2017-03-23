package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Root;

/**
 * 登录
 * @author xpw
 *
 */
@Root
public class Login {
	private int userid;//用户id
	private String imgurl;//头像地址
	private int usertype;//用户类型
	private int keynum;//钥匙数量
	private int keystate;//钥匙状态
	private String token;//钥匙状态
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public int getKeynum() {
		return keynum;
	}
	public void setKeynum(int keynum) {
		this.keynum = keynum;
	}
	public int getKeystate() {
		return keystate;
	}
	public void setKeystate(int keystate) {
		this.keystate = keystate;
	}
	
	
}
