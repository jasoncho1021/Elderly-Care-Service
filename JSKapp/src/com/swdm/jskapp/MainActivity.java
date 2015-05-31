package com.swdm.jskapp;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, SensorEventListener {

	final static String ACTION_SENT = "ACTION_MESSAGE_SENT";
	final static String ACTION_DELIVERY = "ACTION_MESSAGE_DELIVERY";

	private String Helper;
	public static FragmentManager fragmentManager;

	public static PersonalInfo DATA_SET = new PersonalInfo();
	public JSONArray arr;
	/**
	 * variables for action barb
	 */
	//public static DATA_SET dataSet;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	/**
	 * variables for tab
	 */

	private String TabSchedule;

	/**
	 * variable for back button 
	 */

	private boolean bExit = false;

	// Tab titles
	private String[] tabs = { "HOME", "SCHEDUE", "SEARCH", "TRAVEL"};

	/**
	 * variables for sensor
	 */

	private long lastTime;
	private float speed;
	private float lastX;
	private float lastY;
	private float lastZ;
	private float x, y, z;

	private static final int SHAKE_THRESHOLD = 3000;
	@SuppressWarnings("deprecation")
	private static final int DATA_X = SensorManager.DATA_X;
	@SuppressWarnings("deprecation")
	private static final int DATA_Y = SensorManager.DATA_Y;
	@SuppressWarnings("deprecation")
	private static final int DATA_Z = SensorManager.DATA_Z;

	private SensorManager sensorManager;
	private Sensor accelerormeterSensor;


	private LocationManager mLocationMgr;
	private Handler mHandler;

	public static Location mLastLocation;
	public static boolean flag = true;
	private boolean mGeocoderAvailable;
	private static final int UPDATE_LASTLATLNG = 4;
	private static final int LAST_UP = 3;
	private static final int UPDATE_LATLNG = 2;
	private static final int UPDATE_ADDRESS = 1;
	private static final int JSON_ARR = 5;
	
	

	private static final int SECONDS_TO_UP = 10000;
	private static final int METERS_TO_UP = 10;
	private static final int MINUTES_TO_STALE = 1000 * 60 * 2;

	static public String RealLocation, RealTime, RealAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get helper name which was saved at the login activity
		SharedPreferences sh_Pref = getSharedPreferences("TRIGER_CODE", 0);
		Helper = sh_Pref.getString("HELPER", "nullk");
		
		// get info from the db
	      (new informationGet(this)).execute(new Location[] {null});
	      
	      // try to set notification data from member db
	      DATA_SET.DESCRIPTION = getIntent().getStringExtra("NOTE");

	      
	      fragmentManager = getFragmentManager();		

	    
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_ADDRESS:
					RealAddress = (String) msg.obj;
					break;
				case UPDATE_LATLNG:
					RealLocation = (String) msg.obj;
					break;
				case LAST_UP:
					RealTime = (String) msg.obj;
					break;
				case JSON_ARR:
					arr = (JSONArray) msg.obj;
					break;
				}
			}
		};

		mGeocoderAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();
		mLocationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


		// GpsService();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
//		registerReceiver(mSentBR, new IntentFilter(ACTION_SENT));
//		registerReceiver(mDeliveryBR, new IntentFilter(ACTION_DELIVERY));
	}



	public void init() { // Receiving data from the db, and initialzing viewpager

		if(arr.toString().equals("[]")) {

		}
		else if(arr != null) {


			try {
				int k = 0;
				String[] array;
				
				for(int i = 0; i<25; i++)
				{
					DATA_SET.ID[i] = " ";
					DATA_SET.NAME[i] = " ";
					DATA_SET.BIRTH[i] = " ";
					DATA_SET.GENDER[i] = " ";
					DATA_SET.PHONE[i] = " ";
					DATA_SET.AGE[i] = 0;
					DATA_SET.HELPER[i] = " ";
					DATA_SET.MEETING[i] = " ";
					DATA_SET.ADDRESS[i] = " ";
					DATA_SET.LOCATION[i] = "a,b";
					DATA_SET.CHECKLIST[i] = " ";
					DATA_SET.ATTENDANCE[i] = " ";
					DATA_SET.PICURL[i] = " ";					
				}
				
				for(int j = 0; j < arr.length(); j++) {
					JSONObject json_data;

					

					json_data = arr.getJSONObject(j);

					array = json_data.getString("meeting").split("_");
					
					Log.e("[HELPER]",Helper);

					if(json_data.getString("helper").contains(Helper))
					{

						if(array[1].equals("1"))
						{
							if(array[0].equalsIgnoreCase("M"))
								k = 0;
							else if(array[0].equalsIgnoreCase("T"))
								k = 1;
							else if(array[0].equalsIgnoreCase("W"))
								k = 2;
							else if(array[0].equalsIgnoreCase("TH"))
								k = 3;
							else if(array[0].equalsIgnoreCase("F"))
								k = 4;
						}
						else if(array[1].equals("2"))
						{
							if(array[0].equalsIgnoreCase("M"))
								k = 5;
							else if(array[0].equalsIgnoreCase("T"))
								k = 6;
							else if(array[0].equalsIgnoreCase("W"))
								k = 7;
							else if(array[0].equalsIgnoreCase("TH"))
								k = 8;
							else if(array[0].equalsIgnoreCase("F"))
								k = 9;
						}
						else if(array[1].equals("3"))
						{
							if(array[0].equalsIgnoreCase("M"))
								k = 10;
							else if(array[0].equalsIgnoreCase("T"))
								k = 11;
							else if(array[0].equalsIgnoreCase("W"))
								k = 12;
							else if(array[0].equalsIgnoreCase("TH"))
								k = 13;
							else if(array[0].equalsIgnoreCase("F"))
								k = 14;
						}
						else if(array[1].equals("4"))
						{
							if(array[0].equalsIgnoreCase("M"))
								k = 15;
							else if(array[0].equalsIgnoreCase("T"))
								k = 16;
							else if(array[0].equalsIgnoreCase("W"))
								k = 17;
							else if(array[0].equalsIgnoreCase("TH"))
								k = 18;
							else if(array[0].equalsIgnoreCase("F"))
								k = 19;
						}
						else if(array[1].equals("5"))
						{
							if(array[0].equalsIgnoreCase("M"))
								k = 20;
							else if(array[0].equalsIgnoreCase("T"))
								k = 21;
							else if(array[0].equalsIgnoreCase("W"))
								k = 22;
							else if(array[0].equalsIgnoreCase("TH"))
								k = 23;
							else if(array[0].equalsIgnoreCase("F"))
								k = 24;
						}



						DATA_SET.ID[k] = json_data.getString("id");
						DATA_SET.NAME[k] = json_data.getString("name");
						DATA_SET.BIRTH[k] = json_data.getString("birth");
						DATA_SET.GENDER[k] = json_data.getString("sex");
						DATA_SET.PHONE[k] = json_data.getString("phone");
						DATA_SET.AGE[k] = json_data.getInt("age");
						DATA_SET.HELPER[k] = json_data.getString("helper");
						DATA_SET.MEETING[k] = json_data.getString("meeting");
						DATA_SET.ADDRESS[k] = json_data.getString("address");
						DATA_SET.LOCATION[k] = json_data.getString("location");
						DATA_SET.CHECKLIST[k] = json_data.getString("checkList");
						DATA_SET.ATTENDANCE[k] = json_data.getString("attendance");
						DATA_SET.PICURL[k] = json_data.getString("pictureURL");

					}
				}
				
				for(int i=0; i<25; i++)
					Log.i(Integer.toString(i),DATA_SET.MEETING[i]);

				// Initializing time table
				DATA_SET.sch_NAME[0] ="TIME";
				DATA_SET.sch_NAME[1] ="MON";
				DATA_SET.sch_NAME[2] ="TUE";
				DATA_SET.sch_NAME[3] ="WED";
				DATA_SET.sch_NAME[4] ="THU";
				DATA_SET.sch_NAME[5] ="FRI";
				DATA_SET.sch_NAME[6] ="9";
				DATA_SET.sch_NAME[12] ="10";
				DATA_SET.sch_NAME[18] ="11";
				DATA_SET.sch_NAME[24] ="12";
				DATA_SET.sch_NAME[30] ="13";
				DATA_SET.sch_NAME[36] ="14";
				DATA_SET.sch_NAME[42] ="15";

				int j = 0;
				int i = 7;
				for( ; i < 12; i++)
					DATA_SET.sch_NAME[i] = DATA_SET.NAME[j++];
				for( i =13 ; i < 18; i++)
					DATA_SET.sch_NAME[i] = DATA_SET.NAME[j++];	
				for( i =19 ; i < 24; i++)
					DATA_SET.sch_NAME[i] = DATA_SET.NAME[j++];
				for( i = 25; i < 30; i++)
				    DATA_SET.sch_NAME[i] = " ";
				for( i = 31; i < 36; i++)
					DATA_SET.sch_NAME[i] = DATA_SET.NAME[j++];
				for( i = 37; i < 42; i++)
					DATA_SET.sch_NAME[i] = DATA_SET.NAME[j++];
				for( i = 43; i < 48; i++)
					DATA_SET.sch_NAME[i] = " ";


			}catch(JSONException e) {
				Log.e("Log_tag", "Error: JSON object" + e.toString());
			}
		}


		// Adapter Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

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

				actionBar.setSelectedNavigationItem(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

		});

	}


	//	public void getData() {
	//		DBmanager my =new DBmanager();
	//		JSONArray arr=my.requestQuery("SELECT id, password, name FROM mobile");
	//		try {
	//			if(arr.toString().equals("[]")) {
	//
	//			}
	//			else if(arr != null) {
	//				String s = "";
	//
	//				try {
	//					for(int i = 0; i < arr.length(); i++) {
	//						JSONObject json_data;
	//
	//						json_data=arr.getJSONObject(i);
	//						String idTmp = json_data.getString("id");
	//						String passTmp = json_data.getString("password");
	//						String nameTmp = json_data.getString("name");
	//
	//						s = s + idTmp + " " + passTmp + " " + nameTmp + "\n";
	//					}
	//
	//					//data.setText(s);
	//
	//				}catch(JSONException e) {
	//					Log.e("Log_tag", "Error: JSON object" + e.toString());
	//				}
	//			}
	//		}catch(Exception e) {
	//			Log.e("Log_tag", "Error: NULL data" + e.toString());
	//		}
	//	}
	//	

	//		public void setData() {
	//			DBmanager db = new DBmanager("INSERT INTO mobile VALUES ('sg')");
	//			//UPDATE mobile SET meeting=(' ') WHERE name='sungsoo';
	//			db.run();
	//	
	//			// if running as thread
	//			//Thread thread = new Thread(db);
	//			//thread.start();		
	//		}


	/**
	 * methods for action bar
	 */

	public ViewPager getViewPager() {
		return viewPager;
	}

	// + Data transfer code 
	public void setTabSchedule(String t) {
		TabSchedule = t;
	}

	public String getTabSchedule() {
		return TabSchedule;
	}
	// + Data transfer code finish

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

	/**
	 * method when back button is pressed 
	 */

	@Override
	public void onBackPressed() {
		if (bExit)
			finish();
		else {
			Toast.makeText(this, "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			bExit = true;
			new Handler().postDelayed(new Runnable() { // mainthread
				@Override
				public void run() {
					bExit = false;
				}
			}, 3 * 1000);
		}
	}

	/**
	 * inner class for viewing elder's details
	 */

	public static class DetailsActivity extends FragmentActivity { 

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);


			if (savedInstanceState == null) {
				// During initial setup, plug in the details fragment.

				// create fragment
				ElderDetails details = new ElderDetails();

				// get and set the position input by user (i.e., "index")
				// which is the construction arguments for this fragment
				getIntent().putExtra("RealAddress", RealAddress);
				details.setArguments(getIntent().getExtras());  //   

				getSupportFragmentManager().beginTransaction().add(android.R.id.content, details).commit(); //占쎈뮞占쎄문占쎈퓠 add占쎈립占쎈뼄?占쎄퉱 占쎈만占쎈뼒�뜮袁る뼒占쎈빍繹먲옙 add
			}
		}
	}

	/**
	 * methods for sensor
	 */

	@Override
	protected void onResume() {
		super.onResume();
		setup();
	}

	private void setup() {
		Location newLocation = null;
		mLocationMgr.removeUpdates(listener);
		//mLatLng.setText(R.string.unknown);
		newLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER, R.string.no_gps_support);

		// If gps location doesn't work, try network location
		if (newLocation == null) {
			newLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER, R.string.no_network_support);
		}

		if (newLocation != null) {
			updateUILocation(getBestLocation(newLocation, mLastLocation));
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (accelerormeterSensor != null)
			sensorManager.registerListener(this, accelerormeterSensor,
					SensorManager.SENSOR_DELAY_GAME);


	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (sensorManager != null)
			sensorManager.unregisterListener(this);
		mLocationMgr.removeUpdates(listener);
	}

	protected Location getBestLocation(Location newLocation, Location currentBestLocation) {
		if (currentBestLocation == null) {
			return newLocation;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isNewerThanStale = timeDelta > MINUTES_TO_STALE;
		boolean isOlderThanStale = timeDelta < -MINUTES_TO_STALE;
		boolean isNewer = timeDelta > 0;

		if (isNewerThanStale) {
			return newLocation;
		} else if (isOlderThanStale) {
			return currentBestLocation;
		}

		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return newLocation;
		} else if (isNewer && !isLessAccurate) {
			return newLocation;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return newLocation;
		}
		return currentBestLocation;
	}

	private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
		Location location = null;
		if (mLocationMgr.isProviderEnabled(provider)) {
			mLocationMgr.requestLocationUpdates(provider, SECONDS_TO_UP, METERS_TO_UP, listener);
			location = mLocationMgr.getLastKnownLocation(provider);
		} else {
			Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}


	private void updateUILocation(Location location) {
		Message.obtain(mHandler, UPDATE_LATLNG, location.getLatitude() + ", " + location.getLongitude()).sendToTarget();
		if (mLastLocation != null) {
			Message.obtain(mHandler, UPDATE_LASTLATLNG, mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude()).sendToTarget();        	
		}
		mLastLocation = location;
		Date now = new Date();
		Message.obtain(mHandler, LAST_UP, now.toString()).sendToTarget();


		if (mGeocoderAvailable) doReverseGeocoding(location);
	}

	private void doReverseGeocoding(Location location) {
		(new ReverseGeocode(this)).execute(new Location[] {location});
	}

	private final LocationListener listener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateUILocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {}

		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};


	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}



	private class informationGet extends AsyncTask<Location, Void, Void> {

		public informationGet(Context context) {
			super();       
		}

		@SuppressLint("DefaultLocale") @Override
		protected Void doInBackground(Location... params) {

			Log.i("[MAIN]","getE");
			DBmanager dThread = (new DBmanager(mHandler, "SELECT id,name,birth,sex,phone,age,helper,meeting,address,location,checkList,attendance,pictureURL FROM mobile"));
			dThread.start2();

			return null;
		}

		protected void onPostExecute(final Void unused) {
			init();
		}
	}

	private class ReverseGeocode extends AsyncTask<Location, Void, Void> {

		public ReverseGeocode(Context context) {
			super();       
		}

		@SuppressLint("DefaultLocale") @Override
		protected Void doInBackground(Location... params) {


			Location loc = params[0];
			String indiStr = " ";
			try {
				String address = String
						.format(Locale.KOREAN,"http://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false&language="
								+ getApplicationContext().getResources().getConfiguration().locale.getLanguage(),
								loc.getLatitude(), loc.getLongitude());

				HttpGet httpGet = new HttpGet(address);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;
				StringBuilder stringBuilder = new StringBuilder();


				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();


				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}

				//System.out.println(stringBuilder.toString());

				JSONObject jsonObject = new JSONObject(stringBuilder.toString());


				if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
					JSONArray results = jsonObject.getJSONArray("results");
					JSONObject result = results.getJSONObject(0);
					indiStr = new String(result.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");
					//Log.d("LOCATION:",indiStr);
				}


			} catch (IOException e) {
				e.printStackTrace();
				// Update address field with the exception.

				Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message.obtain(mHandler, UPDATE_ADDRESS, indiStr).sendToTarget();

			return null;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long currentTime = System.currentTimeMillis();
			long gabOfTime = (currentTime - lastTime);
			if (gabOfTime > 100) {
				lastTime = currentTime;
				x = event.values[SensorManager.DATA_X];
				y = event.values[SensorManager.DATA_Y];
				z = event.values[SensorManager.DATA_Z];

				speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

				// event condition
				if (speed > SHAKE_THRESHOLD) {

					//String myData = "tel:010-3022-0560";
					//Intent myIntent = new Intent( android.content.Intent.ACTION_CALL,Uri.parse(myData));
					//startActivity(myIntent);
					
					//Thread th1 = new Thread ( new Runnable() { public void run() { sendSMS("응급상황발생주소:\n"+RealAddress);}});
					//th1.start();
					
					//sendSMS("응급상황발생주소:\n"+RealAddress);
				Intent intent = new Intent( Intent.ACTION_SENDTO, Uri.parse("smsto:5551234"));
				intent.putExtra("sms_body","응급상황발생주소:\n"+RealAddress);
					startActivity(intent);

				}

				lastX = event.values[DATA_X];
				lastY = event.values[DATA_Y];
				lastZ = event.values[DATA_Z];
			}

		}

	}

	/**
	 * method for asking GPS service
	 */

	private boolean GpsService() {

		String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		Log.d(gps, "ask GPS");  

		if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
			// 
			AlertDialog.Builder gsDialog = new AlertDialog.Builder(this); 
			gsDialog.setTitle("GPS");   
			gsDialog.setMessage("GPS켜시겠습니까"); 
			gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
				public void onClick(DialogInterface dialog, int which) { 
					// 
					Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS); 
					intent.addCategory(Intent.CATEGORY_DEFAULT); 
					startActivity(intent); 
				} 
			})
			.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create().show();
			return false;

		} else { 
			return true; 
		} 
	}
	
	public void sendSMS (String loc) {

		SmsManager sms = SmsManager.getDefault();

		PendingIntent SentIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SENT), 0);
		PendingIntent DeliveryIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_DELIVERY), 0);

		sms.sendTextMessage("01030220560", null, loc, SentIntent, DeliveryIntent);
	}

	// 송신 확인
	BroadcastReceiver mSentBR = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(getResultCode() == Activity.RESULT_OK) 
			{	
				Log.e("[loc_SMS]","메시지 송신 성공");
				
					unregisterReceiver(mSentBR);
					unregisterReceiver(mDeliveryBR);
					
					Message.obtain(mHandler, 0, 0).sendToTarget();
					Log.d("[loc]:","STOP");
			}
			else
				Log.e("[loc_SMS]","메시지 송신 실패");
		}
	};

	BroadcastReceiver mDeliveryBR = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(getResultCode() == Activity.RESULT_OK)
				Log.e("[loc_SMS]","메시지 수신 성공");				   
			else
				Log.e("[loc_SMS]","메시지 수신 성공");
		}
	};

}

