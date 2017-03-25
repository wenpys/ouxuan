package deling.cellcom.com.cn.bean;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 直播记录
 * @author xpw
 *
 */
public class SvRecord {
	private String name;
	private Date date;
	private int rating;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
