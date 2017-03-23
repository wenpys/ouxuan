package deling.cellcom.com.cn.widget.timepickerview.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import cellcom.com.cn.deling.R;
import deling.cellcom.com.cn.utils.ToastUtils;
import deling.cellcom.com.cn.widget.CustomProgressDialog;
import deling.cellcom.com.cn.widget.timepickerview.TimePickerView.Type;
import deling.cellcom.com.cn.widget.timepickerview.adapter.ArrayWheelAdapter;
import deling.cellcom.com.cn.widget.timepickerview.libs.WheelView;
import deling.cellcom.com.cn.widget.timepickerview.listener.OnItemSelectedListener;

public class WheelTime {
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private CustomProgressDialog m_ProgressDialog;
	// private WheelView wv_hours;
	// private WheelView wv_mins;

	private Type type;
	public static final int DEFULT_START_YEAR = 1990;
	public static final int DEFULT_END_YEAR = 2100;
	private int startYear = DEFULT_START_YEAR;
	private int endYear = DEFULT_END_YEAR;
	private Context context;
	private Map<String, ArrayList<String>> mapCities = new HashMap<String, ArrayList<String>>();
	private Map<String, ArrayList<String>> mapAreas = new HashMap<String, ArrayList<String>>();
	private ArrayList<String> provinces = new ArrayList<String>();
	private List<String> pids = new ArrayList<String>();
	private Map<String, ArrayList<String>> mapCids = new HashMap<String, ArrayList<String>>();
	private Map<String, ArrayList<String>> mapareaIds = new HashMap<String, ArrayList<String>>();
	private String provinceName;
	private int select;
	private JSONObject jsonProvince;
	private JSONObject jsonCity;
	private JSONObject jsonArea;
	private boolean isContinue=true;

	public WheelTime(View view) {
		super();
		this.view = view;
		context = view.getContext();
		type = Type.ALL;
		setView(view);
	}

	public WheelTime(View view, Type type) {
		super();
		context = view.getContext();
		this.view = view;
		this.type = type;
		setView(view);
		initData("province");
		initData("city");
		initData("area");
		getJsonData();
	}

	public void setPicker(int year, int month, int day) {
		this.setPicker(year, month, day, 0, 0);
	}

	/**
	 * @param <T>
	 * @Description: TODO 弹出日期时间选择器
	 */
	public <T> void setPicker(int year, int month, int day, int h, int m) {
		// 添加大小月月份并将其转换为list,方便之后的判断
		// 省

		// wv_year.setAdapter(new
		// ArrayWheelAdapter<Province>(pros,pros.size()));
		// // wv_year.setLabel(context.getString(R.string.pickerview_year));//
		// 添加文字
		// wv_year.setCurrentItem(0);// 初始化时显示的数据
		//
		//
		// // 区
		// if(cities.size()>0){
		// String id=cities.get(0).getCid();
		// List<Area>
		// areas=BaseDataManager.getInstance(context).getAreaByCity(id);
		// ArrayList<Area> areass=new ArrayList<Area>();
		// areass.addAll(areas);
		// wv_day = (WheelView) view.findViewById(R.id.day);
		// wv_day.setAdapter(new ArrayWheelAdapter<Area>(areass,areass.size()));
		// wv_day.setCurrentItem(0);
		// }
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_day = (WheelView) view.findViewById(R.id.day);
		wv_month = (WheelView) view.findViewById(R.id.month);
		
		if(provinces.size()<=0||mapCities.size()<=0||mapAreas.size()<=0){
			return;
		}
		
		wv_year.setAdapter(new ArrayWheelAdapter<String>(provinces, provinces.size()));
		wv_year.setCurrentItem(0);

		ArrayList<String> listCity = mapCities.get(pids.get(0));
		wv_month.setAdapter(new ArrayWheelAdapter<String>(listCity, listCity.size()));
		wv_month.setCurrentItem(0);

		ArrayList<String> listArea = mapAreas.get(mapCids.get(pids.get(0)).get(0));
		wv_day.setAdapter(new ArrayWheelAdapter<String>(listArea, listArea.size()));
		wv_day.setCurrentItem(0);
		wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				provinceName = provinces.get(index);
				select = index;
				updataCity(pids.get(index));
			}
		});
		wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				String pid = pids.get(select);
				String cid = mapCids.get(pid).get(index);
				updataArea(cid);
			}
		});

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 6;
		switch (type) {
		case ALL:
			textSize = textSize * 3;
			break;
		case YEAR_MONTH_DAY:
			textSize = textSize * 4;
			// wv_hours.setVisibility(View.GONE);
			// wv_mins.setVisibility(View.GONE);
			break;
		case HOURS_MINS:
			textSize = textSize * 4;
			wv_year.setVisibility(View.GONE);
			wv_month.setVisibility(View.GONE);
			wv_day.setVisibility(View.GONE);
			break;
		case MONTH_DAY_HOUR_MIN:
			textSize = textSize * 3;
			wv_year.setVisibility(View.GONE);
			break;
		case YEAR_MONTH:
			textSize = textSize * 4;
			wv_day.setVisibility(View.GONE);
			// wv_hours.setVisibility(View.GONE);
			// wv_mins.setVisibility(View.GONE);
		}
		wv_day.setTextSize(textSize);
		wv_month.setTextSize(textSize);
		wv_year.setTextSize(textSize);
		// wv_hours.setTextSize(textSize);
		// wv_mins.setTextSize(textSize);

	}

	private void updataCity(String pid) {
		ArrayList<String> cities = mapCities.get(pid);
		wv_month.setAdapter(new ArrayWheelAdapter<String>(cities, cities.size()));
		wv_month.setCurrentItem(0);
		String cid = mapCids.get(pid).get(0);
		updataArea(cid);
	}

	private void updataArea(String cid) {
		ArrayList<String> areas = mapAreas.get(cid);
		wv_day.setAdapter(new ArrayWheelAdapter<String>(areas, areas.size()));
		wv_day.setCurrentItem(0);
	}

	/**
	 * 读取本地的文件的数据
	 */
	private void initData(final String name) {
		// 读取数据
		File file = new File("/sdcard/myFolder/deling/" + name + ".txt");
		if(!file.exists()){
			ToastUtils.show(context, "数据获取异常，请重启客户端!!!");
			isContinue=false;
			return;
		}
		StringBuffer buffer = new StringBuffer();
		try {
			FileInputStream inputStream = new FileInputStream(file);
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = inputStream.read(buf)) != -1) {
				buffer.append(new String(buf, 0, len, "utf-8"));
			}
			inputStream.close();
			if (name.equals("province")) {
				jsonProvince = new JSONObject(buffer.toString());
			} else if (name.equals("city")) {
				jsonCity = new JSONObject(buffer.toString());
			} else {
				jsonArea = new JSONObject(buffer.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 解析文件内容
	private void getJsonData() {
		if(!isContinue){
			return;
		}
		try {
			JSONArray array = jsonProvince.getJSONArray("body");
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				provinces.add(jsonObject.getString("name"));
				String pid = jsonObject.getString("pid");
				pids.add(pid);
				mapCities.put(pid, new ArrayList<String>());
				mapCids.put(pid, new ArrayList<String>());
			}
			JSONArray arrayCity = jsonCity.getJSONArray("body");
			for (int i = 0; i < arrayCity.length(); i++) {
				JSONObject jsonObject = arrayCity.getJSONObject(i);
				String name = jsonObject.getString("name");
				String cid = jsonObject.getString("cid");
				String pid = jsonObject.getString("pid");
				mapCities.get(pid).add(name);
				mapCids.get(pid).add(cid);
				mapAreas.put(cid, new ArrayList<String>());
				mapareaIds.put(cid, new ArrayList<String>());
			}
			JSONArray arrayArea = jsonArea.getJSONArray("body");
			for (int i = 0; i < arrayArea.length(); i++) {
				JSONObject jsonObject = arrayArea.getJSONObject(i);
				String cid = jsonObject.getString("cid");
				String name = jsonObject.getString("name");
				String id = jsonObject.getString("id");
				mapAreas.get(cid).add(name);
				mapareaIds.get(cid).add(id);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_year.setCyclic(cyclic);
		wv_month.setCyclic(cyclic);
		wv_day.setCyclic(cyclic);
		// wv_hours.setCyclic(cyclic);
		// wv_mins.setCyclic(cyclic);
	}

	/**
	 * @return 结果回调
	 */
	public Intent getTime() {
		StringBuffer sb = new StringBuffer();
		String proName = provinces.get(wv_year.getCurrentItem());
		String pid = pids.get(wv_year.getCurrentItem());
		String cityName = mapCities.get(pid).get(wv_month.getCurrentItem());
		String cid = mapCids.get(pid).get(wv_month.getCurrentItem());
		String area = mapAreas.get(cid).get(wv_day.getCurrentItem());
		String areaId = mapareaIds.get(cid).get(wv_day.getCurrentItem());
		sb.append(proName).append("-").append(cityName).append("-")
				.append(area);
		String content = sb.toString();
		Intent intent = new Intent();
		intent.putExtra("content", content);
		intent.putExtra("areaid", areaId);
		return intent;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}
}
