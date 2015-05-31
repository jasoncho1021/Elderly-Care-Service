package com.swdm.jskapp;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.swdm.jskapp.MainActivity.DetailsActivity;

public class ElderDetails extends Fragment implements OnClickListener{
	TextView name, email, tel, age, time, date, state, address;
	ImageButton callbtn, smsbtn, checkbtn, findWaybtn;
	ImageButton nfcbtn;
	ImageView pic;
	int index;
	String juso, checklist;

	  
      String imgUrl = "http://sungsoo.my.dude.kr/";
      Bitmap bmImg;
      back task;
      
	
	
	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 
		rootView = inflater.inflate(R.layout.elder_details, container, false);

		name = (TextView) rootView.findViewById(R.id.nameTxt);

		tel = (TextView) rootView.findViewById(R.id.telTxt);


		time = (TextView) rootView.findViewById(R.id.timeTxt);
		state = (TextView) rootView.findViewById(R.id.stateTxt);
		date = (TextView) rootView.findViewById(R.id.dateTxt);

		age = (TextView) rootView.findViewById(R.id.ageTxt);

		address = (TextView) rootView.findViewById(R.id.addressTxt);

		callbtn = (ImageButton) rootView.findViewById(R.id.callBtn);
		smsbtn = (ImageButton) rootView.findViewById(R.id.smsBtn);

		nfcbtn = (ImageButton) rootView.findViewById(R.id.nfcBtn);
		checkbtn = (ImageButton) rootView.findViewById(R.id.checkBtn);
		findWaybtn = (ImageButton) rootView.findViewById(R.id.findWay);

		
		  pic = (ImageView) rootView.findViewById(R.id.imageView1);
		  task = new back();  
		  String temp = MainActivity.DATA_SET.ID[getShownIndex()];
		  
          task.execute(imgUrl+temp+".png");
		

		return rootView;
	}
	
	   private class back extends AsyncTask<String, Integer,Bitmap>{
           
      	 
           @Override
           protected Bitmap doInBackground(String... urls) {
               // TODO Auto-generated method stub
               try{
                   URL myFileUrl = new URL(urls[0]);
                   HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                   conn.setDoInput(true);
                   conn.connect();
                   
                   InputStream is = conn.getInputStream();
                   
                   bmImg = BitmapFactory.decodeStream(is);
                   
                   
               }catch(IOException e){
                   e.printStackTrace();
               }
               return bmImg;
           }
           
           protected void onPostExecute(Bitmap img){
               pic.setImageBitmap(bmImg);
           }
           
       }

	public int getShownIndex() {


		String name = getArguments().getString("NAME");

		int i = 0;		

		//스트럭처 개념으로 해야겠다 아님 모든 배열을 일일이 순서를 바꿔줘야함
		while(!(MainActivity.DATA_SET.NAME[i++].equals(name)));


		return i-1;		  
	}

	public void onStart() {
		super.onStart();

		//emailbtn.setOnClickListener(this);			
		callbtn.setOnClickListener(this);
		smsbtn.setOnClickListener(this);
		checkbtn.setOnClickListener(this);
		findWaybtn.setOnClickListener(this);
		nfcbtn.setOnClickListener(this);

		index = getShownIndex();
		name.setText(MainActivity.DATA_SET.NAME[index]);
		tel.setText(MainActivity.DATA_SET.PHONE[index]);
		age.setText(Integer.toString(MainActivity.DATA_SET.AGE[index]));

		juso = MainActivity.DATA_SET.ADDRESS[index];
		address.setText(juso);

		checklist = MainActivity.DATA_SET.CHECKLIST[index];		

		time.setText(MainActivity.DATA_SET.BIRTH[index]);
		state.setText(MainActivity.DATA_SET.GENDER[index]);
		date.setText(MainActivity.DATA_SET.MEETING[index]);
		
		
	}

	public void onClick(View v) {

		Integer ID = v.getId();
		Intent myIntent;
		Uri uri;

				
		if(R.id.nfcBtn==ID) {
			String loc = getArguments().getString("RealAddress");
			myIntent = new Intent();
			myIntent.putExtra("ID", MainActivity.DATA_SET.ID[getShownIndex()]);
			myIntent.putExtra("loc", loc);
			myIntent.setClass(getActivity(), NfcTag.class);
			startActivity(myIntent);	
		}
		else if(R.id.callBtn==ID) {
			String myData12 = "tel:"+MainActivity.DATA_SET.PHONE[getShownIndex()];
			myIntent = new Intent( android.content.Intent.ACTION_CALL,Uri.parse(myData12));
			startActivity(myIntent);
		}
		else if(R.id.smsBtn==ID) {
			uri = Uri.parse("smsto:"+MainActivity.DATA_SET.PHONE[getShownIndex()]);    
			myIntent = new Intent(Intent.ACTION_SENDTO, uri);    
			myIntent.putExtra("sms_body", "The SMS text");    
			startActivity(myIntent);
		}
		else if ( R.id.checkBtn==ID) {
			Intent intent = new Intent();
			intent.putExtra("ID", MainActivity.DATA_SET.ID[getShownIndex()]);
			intent.putExtra("index", index);
			intent.putExtra("checklist", checklist);
			intent.setClass(getActivity(), ChecklistActivity.class);
			startActivity(intent);						
		}
		else if (R.id.findWay == ID) {
			String loc = getArguments().getString("RealAddress");
			DetailsActivity x = (DetailsActivity)getActivity();
			Uri jri = Uri.parse("http://maps.google.com/maps?f=d&saddr="+loc+"&daddr="+ juso +"&hl=ko"); // juso
			//Uri jri = Uri.parse("http://maps.google.com/maps?f=d&saddr=출발지&daddr="+"도착지주소"+"&hl=ko");address.getText().toString()
			Intent kt = new Intent(Intent.ACTION_VIEW, jri);
			startActivity(kt);
		}

	}
}


