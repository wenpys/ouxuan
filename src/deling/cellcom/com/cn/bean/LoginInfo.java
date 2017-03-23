package deling.cellcom.com.cn.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class LoginInfo {
	@Element(required = false)
	private String satus;//状态
	@Element(required = false)
	private String sj;//时间

	public String getSatus() {
		return satus;
	}

	public void setSatus(String satus) {
		this.satus = satus;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	@Override
	public boolean equals(Object obj) {
		LoginInfo s = (LoginInfo) obj;
		return satus.equals(s.satus);
	}

	@Override
	public int hashCode() {
		String in = satus;
		return in.hashCode();
	}
}
