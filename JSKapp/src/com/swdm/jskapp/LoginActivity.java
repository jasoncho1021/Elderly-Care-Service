package com.swdm.jskapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private static final int JSON_ARR = 5;
	public static PersonalInfo DATA_SET = new PersonalInfo();

	public JSONArray arr;
	private Handler mHandler;
	EditText PW, ID;
	ImageButton btnLogin;
	Handler handler;
	TextView registerScreen;
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		(new informationGet(this)).execute(new Location[] {null});
		
		handler = new Handler() {
			public void handleMessage(Message msg) { 
				finish();				
			}
		};

		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		PW = (EditText)findViewById(R.id.loginPw);
		ID = (EditText)findViewById(R.id.loginId);

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				init();
			}
		});

		registerScreen = (TextView) findViewById(R.id.link_to_register);

		// Listening to register new account link
		registerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
			}
		});

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case JSON_ARR:
					arr = (JSONArray) msg.obj;
					break;
				}
			}
		};



	}

//	public void getE() {
//
//		DBmanager dThread = (new DBmanager(mHandler, "SELECT id,password FROM member"));
//		dThread.start2();
//
//	}

	private class informationGet extends AsyncTask<Location, Void, Void> {

		public informationGet(Context context) {
			super();       
		}

		@SuppressLint("DefaultLocale") @Override
		protected Void doInBackground(Location... params) {

			Log.i("[LOGIN]","getE");
			DBmanager dThread = (new DBmanager(mHandler, "SELECT id,password,note FROM member"));
			dThread.start2();

			return null;
		}

		protected void onPostExecute(final Void unused) {
				init();
		}
	}

	public void init() { // Receiving data from the db, and initialzing viewpager

		if(arr.toString().equals("[]")) {

		}
		else if(arr != null) {
			try{

		
				for(int j = 0; j < arr.length(); j++) {
					JSONObject json_data;
					json_data=arr.getJSONObject(j);

					DATA_SET.ID[j] = json_data.getString("id");
					DATA_SET.PW[j] = json_data.getString("password");
					DATA_SET.HELPER[j] = json_data.getString("note");
					
					
					if(DATA_SET.ID[j].equalsIgnoreCase(ID.getText().toString()))
						if(DATA_SET.PW[j].equalsIgnoreCase(PW.getText().toString()))
						{
							
							SharedPreferences sh_Pref = getSharedPreferences("TRIGER_CODE", 0);
							Editor toEdit = sh_Pref.edit();
							toEdit.putString("HELPER", DATA_SET.HELPER[j]);
							toEdit.commit();
							
							//startActivity(new Intent(this, SplashActivity.class));
							Intent i = new Intent(this, MainActivity.class);
							i.putExtra("NOTE", DATA_SET.HELPER[j]); // 쓰레기값
							startActivity(i);
							handler.sendEmptyMessageDelayed(0, 1000);							
						}	
				}
			}catch(JSONException e) {
				Log.e("Log_tag", "Error: JSON object" + e.toString());
			}



		}
	}

}