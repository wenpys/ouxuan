package deling.cellcom.com.cn.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import deling.cellcom.com.cn.activity.base.FragmentBase;
/**
 * viewpager适配
 * @author Administrator
 *
 */
public class MainAdapter extends FragmentPagerAdapter {

	private List<FragmentBase> fragmentBases ;
	public MainAdapter(FragmentManager fm , List<FragmentBase> fragmentBases) {
		super(fm);
		this.fragmentBases = fragmentBases;
	}
	

	@Override
	public Fragment getItem(int arg0) {
		return fragmentBases.get(arg0);
	}
	@Override
	public int getCount() {
		
		return fragmentBases.size();
	}


}
