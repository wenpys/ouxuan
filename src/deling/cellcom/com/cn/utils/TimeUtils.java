package deling.cellcom.com.cn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_TIME = new SimpleDateFormat(
			"HH:mm");

	private TimeUtils() {
		throw new AssertionError();
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	public static String getNowTime(long timeInMillis) {
		return getTime(timeInMillis, DATE_FORMAT_TIME);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 消息通知格式�?
	 * @param date	yyyy-MM-dd
	 * @param time	HH:mm:ss
	 * @return
	 */
	public static String timeFormat(String date, String time) {
		String now = DATE_FORMAT_DATE.format(new Date(getCurrentTimeInLong()));

		SimpleDateFormat format = new SimpleDateFormat("E");

		int nowYear = Integer.parseInt(now.substring(0, 4));
		int year = Integer.parseInt(date.substring(0, 4));

		int nowMon = Integer.parseInt(now.substring(5, 7));
		int mon = Integer.parseInt(date.substring(5, 7));

		int nowDay = Integer.parseInt(now.substring(8, now.length()));
		int day = Integer.parseInt(date.substring(8, date.length()));

		if (nowYear == year) {
			if (nowMon == mon) {
				if (nowDay - day == 1) {
					return "昨天  ";
				} else if (nowDay == day) {
					return time;

				} else if (nowDay - day <= 3) {
					return format.format(new Date(getCurrentTimeInLong()
							- ((nowDay - day) * 24 * 60 * 60 * 1000)));
				}
			}
		}

		String str = (year + "").substring(2);
		String m;
		String d;
		if (mon < 10) {
			m = "0" + mon;
		} else {
			m = mon + "";
		}
		if (day < 10) {
			d = "0" + day;
		} else {
			d = day + "";
		}

		return str + "/" + m + "/" + d;
	}

	/**
	 * 
	 * @param before
	 *            之前的时�? yyyy-MM-dd HH:mm:ss
	 * @param now
	 *            现在的时�? yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static boolean isFiveMinute(long before, long now) {
		if (now - before >= 1000 * 60 * 5) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 *            传入的时间格式为"yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String handTime(String time) {
		if (time == null || "".equals(time.trim())) {
			return "";
		}
		try {
			Date date = DEFAULT_DATE_FORMAT.parse(time);
			long tm = System.currentTimeMillis();// 当前时间�?
			long tm2 = date.getTime();// 发表动�?�的时间�?
			long d = (tm - tm2) / 1000;// 时间差距 单位�?
			if ((d / (60 * 60 * 24)) > 0) {
				if ((d / (60 * 60 * 24)) < 3) {

					return d / (60 * 60 * 24) + "天前";
				} else
					return time.substring(5, 10);
			} else if ((d / (60 * 60)) > 0) {
				return d / (60 * 60) + "小时�?";
			} else if ((d / 60) > 0) {
				return d / 60 + "分钟�?";
			} else {
				// return d + "秒前";
				return "刚刚";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
