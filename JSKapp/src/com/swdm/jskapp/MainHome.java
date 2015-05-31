package com.swdm.jskapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.swdm.jskapp.MainActivity.DetailsActivity;

public class MainHome extends Fragment {

	EditText home_input;
	Button home_enter;
	List<DicType> arrData;
	/**
	 * View UI
	 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.home, container, false);
		
//		// + Data transfer code
//		home_input = (EditText)rootView.findViewById(R.id.home_input);
//		home_enter = (Button)rootView.findViewById(R.id.home_enter);
//		home_enter.setOnClickListener(enterOnClickListener);
//		// + Data transfer code		

		arrData = new ArrayList<DicType>();

		//Data �ʱ�ȭ
		onCreateData();

		//ExpandableListView�� ã�´�.
		ExpandableListView exList = (ExpandableListView)rootView.findViewById(R.id.expandablelist);
		//������ Type�� ������ List Map�� �����Ѵ�.
		List<Map<String, String>> dicType = new ArrayList<Map<String, String>>();
		//������ data�� ������ List Map�� �����Ѵ�. List�ȿ� List�ȿ� Map�� ���¸� �ڴ�.
		List<List<Map<String, String>>> dicArrName = new ArrayList<List<Map<String, String>>>();

		//List�� Map�� key�� �����͸� �����ϱ� ���� �ڵ�
		for(int i=0;i<arrData.size();i++) {
			Map<String, String> type = new HashMap<String, String>();
			//key���� �����͸� ����
			type.put("Type", arrData.get(i).Type);
			//dicType List�� HashMap�� ����
			dicType.add(type);

			List<Map<String, String>> arrName = new ArrayList<Map<String, String>>();
			for(int j=0;j<arrData.get(i).Array.size();j++) {
				Map<String, String> name = new HashMap<String, String>();
				name.put("enName", arrData.get(i).Array.get(j).enName);
				name.put("koName", arrData.get(i).Array.get(j).koName);
				arrName.add(name);
			}
			dicArrName.add(arrName);
		}

		ExpandableListAdapter adapter = new SimpleExpandableListAdapter(
				getActivity(),
				dicType, //ȭ�鿡 �ѷ��� �����͸� ȣ��
				//����� ����Ʈ�並 ȣ��
				android.R.layout.simple_expandable_list_item_1,
				new String[] { "Type" }, //�ѷ��� ���� Hash�� key�� �����ش�.
				new int[] { android.R.id.text1 }, //�ѷ��� TextView�� �ҷ��´�.
				//����� ���� �����͸� ȣ���Ѵ�.
				dicArrName,
				android.R.layout.simple_expandable_list_item_2,
				//�迭�� ����Ͽ� ȣ�� �� �� �ִ�. �� ��� View�� ���� �� �°� �����ؾ� �Ѵ�.
				new String[] { "enName", "koName" },
				//String�� ���� View�� ���� 1:1�̾�� �Ѵ�.
				new int[] { android.R.id.text1, android.R.id.text2 }
				);
		//������ adapter�� List�� �ѷ��ش�.
		exList.setAdapter(adapter);

		
		return rootView;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onCreateData() {
		
//		String[] paragraph = MainActivity.DATA_SET.DESCRIPTION.split("/");
//		
//		String[] p1 = paragraph[0].split(",");
//		String[] p2 = paragraph[1].split(",");
//		String[] p3 = paragraph[2].split(",");
		
	
		
		List<DicName> dic1 = new ArrayList<DicName>();
		dic1.add(new DicName("������ ��������", "2014.6.23"));
		//dic1.add(new DicName("abroad", "�ܱ���"));
		//dic1.add(new DicName("absorptivity", "��� ���"));
		arrData.add(new DicType("���� ����", dic1));

		List<DicName> dic2 = new ArrayList<DicName>();
		dic2.add(new DicName("������ �Ҿƹ��� ���ͺ�", "2014.6.25"));
		//dic2.add(new DicName("dance", "���ߴ�"));
		//dic2.add(new DicName("dancing", "��"));
		//dic2.add(new DicName("deadline", "���� �� ����"));
		arrData.add(new DicType("�ǹ� ����", dic2));

		List<DicName> dic3 = new ArrayList<DicName>();
		dic3.add(new DicName("�ǹ� �౸ȸ ����", "2014.6.20"));
		//dic3.add(new DicName("either", "�� �� �ϳ�"));
		//dic3.add(new DicName("eligible", "���� ����[��] �� �ִ�"));
		//dic3.add(new DicName("effective", "ȿ������"));
		//dic3.add(new DicName("early", "������"));
		arrData.add(new DicType("��� �ҽ�", dic3));
	}
	/**
	 * Click listener
	 */
	
	// + Data transfer code
	OnClickListener enterOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {

			//			String textPassToSchedule = home_input.getText().toString();
			//						
			//			String TabOfSchedule = ((MainActivity)getActivity()).getTabSchedule();
			//
			//			Schedule schedule = (Schedule)getFragmentManager().findFragmentByTag(TabOfSchedule);
			//
			//			schedule.updateTextSchedule(textPassToSchedule);
			//			///
			//			///ViewPager v = ((MainActivity)getActivity()).getViewPager();
			//			//v.setCurrentItem(2);
			//			///
			//			Toast.makeText(getActivity(), 
			//					"text sent to Schedule:\n " + getTag(), 
			//					Toast.LENGTH_LONG).show();
			Intent intent = new Intent();

			// explicitly set the activity context and class
			// associated with the intent (context, class)
			intent.setClass(getActivity(), DetailsActivity.class);

			// pass the current position
			intent.putExtra("NAME", home_input.getText().toString());

			startActivity(intent);

		}
	};
	// + Data transfer code
}
