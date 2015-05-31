package com.swdm.jskapp;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swdm.jskapp.MainActivity.DetailsActivity;

public class MainSchedule extends Fragment {

	Context context;
	Handler mHandler;
	View rootView;
	TextView name;
	ImageButton changeBtn;
	Boolean change;
	Boolean change2;
	int origin;
	int[] notTable = {0,1,2,3,4,5,6,12,18,24,30,36,42};
	ArrayAdapter<String> adapter;
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.schedule, container, false);

		context = getActivity();
		origin = 0;
		change = false;
		change2 = false;		

		
		return rootView;

	}
	
	

	public void onStart() {
		super.onStart();

		//get grid view id from activity_main.xml
		GridView view = (GridView)rootView.findViewById(R.id.grid);
		changeBtn = (ImageButton)rootView.findViewById(R.id.schBtn);

		//android.R.layout.simple_list_item_1
		adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.grid_item, R.id.text,MainActivity.DATA_SET.sch_NAME)
				{

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view =super.getView(position, convertView, parent);

				int curDay;			    
				Calendar c;

				c = Calendar.getInstance();
				curDay = c.get(Calendar.DAY_OF_WEEK); 


				TextView textView = (TextView) view.findViewById(R.id.text);
				for(int i=0; i<6; i++)
				{
					if(i==position)
					{
						if(position == curDay-1 && (1 < curDay && curDay < 7))
						{	
							view.setBackground(getResources().getDrawable(R.drawable.green));
							textView.setTextColor(Color.WHITE);
							continue;
						}

						//BitmapDrawable drawable2 = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_launcher);

						view.setBackground(getResources().getDrawable(R.drawable.blue));
						textView.setTextColor(Color.WHITE);
						//view.setBackgroundColor(Color.YELLOW);
					}

				}


				for(int i=6; i<notTable.length; i++)
					if( notTable[i] == position)
					{
						view.setBackground(getResources().getDrawable(R.drawable.yellow));
						textView.setTextColor(Color.WHITE);
					}


				if(position%6 == curDay-1 && (1 < curDay && curDay < 7) && position != curDay-1)
					textView.setTextColor(Color.GREEN);

				//				TextView textView = (TextView) view.findViewById(android.R.id.text1);
				//
				//				/*YOUR CHOICE OF COLOR*/
				//				textView.setTextSize(14);
				//				textView.setTextColor(Color.WHITE);

				return view;
			}
				};

				view.setAdapter(adapter);


				view.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int pos,long id) 
					{

						String t = parent.getItemAtPosition(pos).toString();


						if(checkTable(pos)) {
							if(change) {

								int select;

								if(t.equalsIgnoreCase(" "))
								{
									select = pos;

								}else {

									int i = 0;		
									while(!(MainActivity.DATA_SET.sch_NAME[i++].equals(t)) && i < MainActivity.DATA_SET.sch_NAME.length);
									select = i-1;

									if(i-1 == MainActivity.DATA_SET.sch_NAME.length )
										return;
								}

								if(change2) {
									origin = select;
									change2= false;

								}
								else {
									String temp = MainActivity.DATA_SET.sch_NAME[select];
									MainActivity.DATA_SET.sch_NAME[select] = MainActivity.DATA_SET.sch_NAME[origin];
									MainActivity.DATA_SET.sch_NAME[origin] = temp;
									change = false;			    	
																	
									// 저장소 스케줄도 바꿔줘야함 및 순서 다 바꿔야함
									int i = 0;
									while(!(MainActivity.DATA_SET.NAME[i++].equals(MainActivity.DATA_SET.sch_NAME[select])));
									int j = 0;
									while(!(MainActivity.DATA_SET.NAME[j++].equals(temp)) && j < 25);
									
									if(j < 25)
									{
									String m_temp = MainActivity.DATA_SET.ID[i-1];
									MainActivity.DATA_SET.ID[i-1] = MainActivity.DATA_SET.ID[j-1];
									MainActivity.DATA_SET.ID[j-1] = m_temp;			
									
									m_temp = MainActivity.DATA_SET.NAME[i-1];
									MainActivity.DATA_SET.NAME[i-1] = MainActivity.DATA_SET.NAME[j-1];
									MainActivity.DATA_SET.NAME[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.BIRTH[i-1];
									MainActivity.DATA_SET.BIRTH[i-1] = MainActivity.DATA_SET.BIRTH[j-1];
									MainActivity.DATA_SET.BIRTH[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.GENDER[i-1];
									MainActivity.DATA_SET.GENDER[i-1] = MainActivity.DATA_SET.GENDER[j-1];
									MainActivity.DATA_SET.GENDER[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.PHONE[i-1];
									MainActivity.DATA_SET.PHONE[i-1] = MainActivity.DATA_SET.PHONE[j-1];
									MainActivity.DATA_SET.PHONE[j-1] = m_temp;
									
									int i_temp = MainActivity.DATA_SET.AGE[i-1];
									MainActivity.DATA_SET.AGE[i-1] = MainActivity.DATA_SET.AGE[j-1];
									MainActivity.DATA_SET.AGE[j-1] = i_temp;
									
									m_temp = MainActivity.DATA_SET.HELPER[i-1];
									MainActivity.DATA_SET.HELPER[i-1] = MainActivity.DATA_SET.HELPER[j-1];
									MainActivity.DATA_SET.HELPER[j-1] = m_temp;
								
									m_temp = MainActivity.DATA_SET.ADDRESS[i-1];
									MainActivity.DATA_SET.ADDRESS[i-1] = MainActivity.DATA_SET.ADDRESS[j-1];
									MainActivity.DATA_SET.ADDRESS[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.LOCATION[i-1];
									MainActivity.DATA_SET.LOCATION[i-1] = MainActivity.DATA_SET.LOCATION[j-1];
									MainActivity.DATA_SET.LOCATION[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.CHECKLIST[i-1];
									MainActivity.DATA_SET.CHECKLIST[i-1] = MainActivity.DATA_SET.CHECKLIST[j-1];
									MainActivity.DATA_SET.CHECKLIST[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.ATTENDANCE[i-1];
									MainActivity.DATA_SET.ATTENDANCE[i-1] = MainActivity.DATA_SET.ATTENDANCE[j-1];
									MainActivity.DATA_SET.ATTENDANCE[j-1] = m_temp;
									
									m_temp = MainActivity.DATA_SET.PICURL[i-1];
									MainActivity.DATA_SET.PICURL[i-1] = MainActivity.DATA_SET.PICURL[j-1];
									MainActivity.DATA_SET.PICURL[j-1] = m_temp;
									
									
																	
									
									Log.i("i-1",MainActivity.DATA_SET.MEETING[i-1]+","+MainActivity.DATA_SET.ID[i-1]);
									Log.i("j-1",MainActivity.DATA_SET.MEETING[j-1]+","+MainActivity.DATA_SET.NAME[j-1]);
									//db로 보내기  "update mobile set checkList='"+result+"' where id='"+ ID +"'"
									String s = "update mobile set meeting='"+ MainActivity.DATA_SET.MEETING[j-1] +"' where id='"+MainActivity.DATA_SET.ID[j-1] +"'";
									String s2= "update mobile set meeting='"+ MainActivity.DATA_SET.MEETING[i-1] +"' where id='"+MainActivity.DATA_SET.ID[i-1] +"'";
									
									informationSet sender = new informationSet(context);
									sender.execute(s,s2);
									
									Log.i("changed","real?");
									}
									
									adapter.notifyDataSetChanged();
								}


							}else {

								if(!t.equalsIgnoreCase(" "))
								{
									Intent intent = new Intent();

									intent.setClass(getActivity(), DetailsActivity.class);

									intent.putExtra("NAME", t);

									startActivity(intent);
								}
							}
						}

						
						return;
					}

				});



				changeBtn.setOnClickListener(new Button.OnClickListener() {

					public void onClick(View v) {

						change = true;
						change2 = true;
					}
				});


				mHandler = new Handler() {
					public void handleMessage(Message msg) {
						if (msg.what == 0){
							Log.d("[sended]:","success");
						}
					}};

	}

	public boolean checkTable (int pos) {
		for(int i=0; i<notTable.length; i++)
			if(notTable[i] == pos)
				return false;

		return true;
	}


	private class informationSet extends AsyncTask<String, Void, Void> {


		public informationSet(Context context) {
			// TODO Auto-generated constructor stub
			super();       
		}

		@SuppressLint("DefaultLocale") @Override
		protected Void doInBackground(String... params) {

			String query = params[0];
			Log.i("query",query);
			String query2 = params[1];
			Log.i("query2",query2);
			DBmanager db=new DBmanager(query);
			db.start3();
			DBmanager db2=new DBmanager(query2);
			db2.start3();
		

			return null;
		}

		protected void onPostExecute(final Void unused) {

		}
	}

}



