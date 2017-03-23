package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;

public class Village {
	@Element(required=false)
	//数据库ID
	private int id;
	@Element(required=false)
	//门栋id
	private String gateid;
	@Element(required=false)
	//门栋名称
	private String gatename;
	@Element(required=false)
	//小区id
	private String areaid;
	@Element(required=false)
	//小区名称
	private String areaname;
	@Element(required=false)
	//区域id
	private String regionid;
	@Element(required=false)
	//区域名称
	private String regionname;
	@Element(required=false)
	//城市id
	private String cityid;
	@Element(required=false)
	//小区名称
	private String cityname;
	@Element(required=false)
	//省份id
	private String provinceid;
	@Element(required=false)
	//省份名称
	private String provincename;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGateid() {
		return gateid;
	}
	public void setGateid(String gateid) {
		this.gateid = gateid;
	}
	public String getGatename() {
		return gatename;
	}
	public void setGatename(String gatename) {
		this.gatename = gatename;
	}
	public String getAreaid() {
		return areaid;
	}
	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getRegionid() {
		return regionid;
	}
	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}
	public String getRegionname() {
		return regionname;
	}
	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getProvinceid() {
		return provinceid;
	}
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	public String getProvincename() {
		return provincename;
	}
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	@Override
	public String toString() {
		return "Village, gateid=" + gateid + ", gatename="
				+ gatename + ", areaid=" + areaid + ", areaname=" + areaname
				+ ", regionid=" + regionid + ", regionname=" + regionname
				+ ", cityid=" + cityid + ", cityname=" + cityname
				+ ", provinceid=" + provinceid + ", provincename="
				+ provincename + "]";
	}
	
}
