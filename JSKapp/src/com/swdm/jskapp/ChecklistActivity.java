package com.swdm.jskapp;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class ChecklistActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * variables for action bar	 */
	
	Context context;


	public static int[] Chbx_h = new int[8];
	public static int Rdbtn_h = 0;
	public static float Rbar_h = 0;

	public static int[] Chbx_r = new int[8];
	public static int Rdbtn_r = 0;
	public static float Rbar_r = 0;

	public static int[] Chbx_e = new int[8];
	public static int Rdbtn_e = 0;
	public static float Rbar_e = 0;


	private ViewPager viewPager;
	private TabsAdapter mAdapter;
	private ActionBar actionBar;
	
	private Handler mHandler;

	// Tab titles
	private String[] tabs = { "Health", "Economy", "Residence" };
	String ID="";
	String result="";
	/**
	 * variable for back button
	 */

	private int Index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		context = this;
		
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (0 == msg.what) {
				
					String RealAddress = (String) msg.obj;
					Log.d("[REAL]:",RealAddress);
				}
			}
		};

		Intent data = getIntent();
	
		Index = data.getIntExtra("index", 0);
		String s = data.getStringExtra("checklist");
		ID = data.getStringExtra("ID");
		String[] array;

		// 01011011/4/2.5,01011010/4/2.5,01011101/4/2.5
		array = s.split(","); // seperate the health, residence, economy check value

		String[] arr_h = array[0].split("/"); // seperate the checkbox, radio, rating of health
		String[] arr_e = array[1].split("/"); // seperate the checkbox, radio, rating of residence
		String[] arr_r = array[2].split("/"); // seperate the checkbox, radio, rating of economy

//Initializing each checklist section with db
		for(int i = 0; i < 8; i++)
			Chbx_h[i] = Integer.parseInt(arr_h[0].substring(i, i+1));

		Rdbtn_h = Integer.parseInt(arr_h[1]);
		Rbar_h = Float.parseFloat(arr_h[2]);


		for(int i = 0; i < 8; i++)
			Chbx_r[i] = Integer.parseInt(arr_r[0].substring(i, i+1));

		Rdbtn_r = Integer.parseInt(arr_r[1]);
		Rbar_r = Float.parseFloat(arr_r[2]);


		for(int i = 0; i < 8; i++)
			Chbx_e[i] = Integer.parseInt(arr_e[0].substring(i, i+1));

		Rdbtn_e = Integer.parseInt(arr_e[1]);
		Rbar_e = Float.parseFloat(arr_e[2]);



		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected

				if(position < 3)
					actionBar.setSelectedNavigationItem(position);
				else
					actionBar.setSelectedNavigationItem(3);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * methods for action bar
	 */

	public ViewPager getViewPager() {
		return viewPager;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view

		int position = tab.getPosition();

		viewPager.setCurrentItem(position);

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	// 여기는 이너클래스로 다 처리했네요? 
	/**
	 * inner class for actions bar
	 */

	public class TabsAdapter extends FragmentPagerAdapter {

		public TabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {

			switch (index) {
			case 2:
				// home fragment
				return new ChecklistResidence();
			case 0:
				// health fragment
				return new ChecklistHealth();
			case 1:
				// economy fragment
				return new ChecklistEconomy();
			}

			return null;
		}

		@Override
		public int getCount() {
			// get item count - equal to number of tabs
			return 3;
		}
	}
	
	private class informationSet extends AsyncTask<Location, Void, Void> {
	
		public informationSet(Context context) {
			super();       
		}

		@SuppressLint("DefaultLocale") @Override
		protected Void doInBackground(Location... params) {
			
			DBmanager db=new DBmanager("update mobile set checkList='"+result+"' where id='"+ ID +"'");
			db.start3();

			return null;
		}

		protected void onPostExecute(final Void unused) {
			finish();
		}
	}

	/**
	 * method for back button
	 */

	@Override
	public void onBackPressed() {


				
		new AlertDialog.Builder(this)
		.setTitle("Terminator")
		.setMessage("Are you sure that you want to quit?")
		.setIcon(R.drawable.ic_launcher)
		.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				// 체크리스트정보 db 전송 
				 result ="";					

				for(int i = 0; i < ChecklistHealth.Chbx_h_f.length ; i++)
					if(ChecklistHealth.Chbx_h_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
						result += "1";
					else
						result += "0";

				result += "/";

				for(int i = 0; i < ChecklistHealth.Rdbtn_h_f.length ; i++)
					if(ChecklistHealth.Rdbtn_h_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
					{
						result += Integer.toString(i);
						break;
					}


				result += "/";

				result += String.valueOf(ChecklistHealth.Rbar_h_f.getRating());

				result += ","; 
				
				// Health ending
				
				for(int i = 0; i < ChecklistEconomy.Chbx_e_f.length ; i++)
					if(ChecklistEconomy.Chbx_e_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
						result += "1";
					else
						result += "0";

				result += "/";

				for(int i = 0; i < ChecklistEconomy.Rdbtn_e_f.length ; i++)
					if(ChecklistEconomy.Rdbtn_e_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
					{
						result += Integer.toString(i);
						break;
					}


				result += "/";

				result += String.valueOf(ChecklistEconomy.Rbar_e_f.getRating());

				result += ","; 
				
				// Economy ending

				for(int i = 0; i < ChecklistResidence.Chbx_r_f.length ; i++)
					if(ChecklistResidence.Chbx_r_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
						result += "1";
					else
						result += "0";

				result += "/";

				for(int i = 0; i < ChecklistResidence.Rdbtn_r_f.length ; i++)
					if(ChecklistResidence.Rdbtn_r_f[i].isChecked()) // The Point thta Fragment access the Activity variable!!
					{
						result += Integer.toString(i);
						break;
					}


				result += "/";

				result += String.valueOf(ChecklistResidence.Rbar_r_f.getRating());

				// Residence ending

			
				Log.d("total",result);
			
				
			
				//db로!
				//MainActivity.DATA_SET.CHECKLIST[Index] = result;
				 (new informationSet(context)).execute(new Location[] {null});
				
			

			}
		})
		.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		})
		.show();


	}
}

