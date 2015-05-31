package com.swdm.jskapp;

import com.swdm.jskapp.LocationFragment;
import com.swdm.jskapp.MainHome;
import com.swdm.jskapp.MainSchedule;
import com.swdm.jskapp.MainSearch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// home activity
			return new MainHome();
		case 1:
			// schedule activity
			return new MainSchedule();
		case 2:
			// search activity
			return new MainSearch();
		case 3:
			return new LocationFragment();
		}

		return null;
	}


	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}

