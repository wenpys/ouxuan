package deling.cellcom.com.cn.bean;

import java.io.Serializable;

import org.simpleframework.xml.Root;

/**
 * 个人
 * @author zsw
 *
 */
@Root
public class PersonInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ysxm  ;//医生姓名
	private String ysxb  ;//医生性别
	private String yssf  ;//医生省份
	private String yscs  ;//医生城市
	private String ysqy  ;//医生区域
	private String ysyy  ;//医生医院
	private String ysks  ;//医生科室
	private String yszc  ;//医生职称
	private String ystx  ;//医生头像
	private String ysgrxq;//医生个人详情
	private String yssc  ;//医生擅长
	private String yspjs ;//医生评价�?
	private String ewm   ;//二维�?
	private String shzt  ;//审核状�??
	public String getYsxm() {
		return ysxm;
	}
	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}
	public String getYsxb() {
		return ysxb;
	}
	public void setYsxb(String ysxb) {
		this.ysxb = ysxb;
	}
	public String getYssf() {
		return yssf;
	}
	public void setYssf(String yssf) {
		this.yssf = yssf;
	}
	public String getYscs() {
		return yscs;
	}
	public void setYscs(String yscs) {
		this.yscs = yscs;
	}
	public String getYsqy() {
		return ysqy;
	}
	public void setYsqy(String ysqy) {
		this.ysqy = ysqy;
	}
	public String getYsyy() {
		return ysyy;
	}
	public void setYsyy(String ysyy) {
		this.ysyy = ysyy;
	}
	public String getYsks() {
		return ysks;
	}
	public void setYsks(String ysks) {
		this.ysks = ysks;
	}
	public String getYszc() {
		return yszc;
	}
	public void setYszc(String yszc) {
		this.yszc = yszc;
	}
	public String getYstx() {
		return ystx;
	}
	public void setYstx(String ystx) {
		this.ystx = ystx;
	}
	public String getYsgrxq() {
		return ysgrxq;
	}
	public void setYsgrxq(String ysgrxq) {
		this.ysgrxq = ysgrxq;
	}
	public String getYssc() {
		return yssc;
	}
	public void setYssc(String yssc) {
		this.yssc = yssc;
	}
	public String getYspjs() {
		return yspjs;
	}
	public void setYspjs(String yspjs) {
		this.yspjs = yspjs;
	}
	public String getEwm() {
		return ewm;
	}
	public void setEwm(String ewm) {
		this.ewm = ewm;
	}
	public String getShzt() {
		return shzt;
	}
	public void setShzt(String shzt) {
		this.shzt = shzt;
	}
	

}
