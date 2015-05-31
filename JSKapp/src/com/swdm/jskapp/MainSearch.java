package com.swdm.jskapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.swdm.jskapp.MainActivity.DetailsActivity;

public class MainSearch extends Fragment {

   String[] ageOption = new String[]{"50","60","70","80"};
   boolean[] mSelect = { false, false, false, false };

   TextView txtMsg;
   ListView myListView;

   View rootView;
   ArrayAdapter<String> aa;
   private ArrayList<String> list;

   EditText etName;
   CheckBox ageSet;
   boolean isAgeChecked;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


      isAgeChecked = false;

      rootView = inflater.inflate(R.layout.search, container, false);

      list = new ArrayList<String>();

      myListView = (ListView)rootView.findViewById(R.id.list);

      aa = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_activated_1,
            list){

         @Override
         public View getView(int position, View convertView,
               ViewGroup parent) {
            View view =super.getView(position, convertView, parent);

            TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
            textView.setTextColor(Color.BLACK);

            return view;
         }
      };

      myListView.setAdapter(aa);


      etName = (EditText)rootView.findViewById(R.id.etName);
      Button btnCall = (Button)rootView.findViewById(R.id.sendButton);      
      ageSet = (CheckBox)rootView.findViewById(R.id.setAgeBtn);

      //////////////
      ageSet.setOnClickListener(new Button.OnClickListener() {

         public void onClick(View v) { 

            new AlertDialog.Builder(rootView.getContext()) // // //
            .setTitle("나이를 선택하시오")
            .setMultiChoiceItems(ageOption, mSelect,
                  new OnMultiChoiceClickListener() {
               public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                  mSelect[which] = isChecked;
               }
            })
            .setPositiveButton("확인", new DialogInterface.OnClickListener() { 
               public void onClick(DialogInterface dialog, int whichButton) {
                  isAgeChecked = true;
               }
            })
            .setNegativeButton("취소",null)
            .show(); }});



      /////////////////////////////////////

      btnCall.setOnClickListener(new Button.OnClickListener() {

         public void onClick(View v) {

            // 새로 작업 수행할때마다 검색목록 다 지우기
            if(list.size() != 0)
            {   
               int k = list.size()-1;
               while(k >= 0)
                  list.remove(k--);
            }

            // check gender
            RadioGroup ColGroup;
            ColGroup = (RadioGroup)rootView.findViewById(R.id.genderGroup);
            int radioId = ColGroup.getCheckedRadioButtonId();

            if( R.id.maleBtn == radioId) {
               for(int i = 0; i< MainActivity.DATA_SET.GENDER.length; i++)
                  if(MainActivity.DATA_SET.GENDER[i].equals("M"))
                     list.add(MainActivity.DATA_SET.NAME[i]);      
            }
            else if(  R.id.femaleBtn  == radioId) {
               for(int i = 0; i<MainActivity.DATA_SET.GENDER.length; i++)
                  if(MainActivity.DATA_SET.GENDER[i].equals("F"))
                     list.add(MainActivity.DATA_SET.NAME[i]);      
            }      


            if(isAgeChecked) {   
               for (int i = 0; i < mSelect.length; i++) {
                  if(mSelect[i]) {

                     for(int j = 0; j<MainActivity.DATA_SET.NAME.length; j++)
                        if(MainActivity.DATA_SET.AGE[j] <= Integer.parseInt(ageOption[i]))
                           list.add(MainActivity.DATA_SET.NAME[j]);   
                  }
               }
               isAgeChecked = false;
            }


            
            String t = etName.getText().toString();

            int i = 0;
            while(!(MainActivity.DATA_SET.NAME[i++].equals(t)) && i<MainActivity.DATA_SET.NAME.length);

            
            if(i == MainActivity.DATA_SET.NAME.length )
            {
               aa.notifyDataSetChanged();
               return;
            }
            else
            {
               Log.d("[i-1, length]:",Integer.toString(i-1)+"/"+Integer.toString(MainActivity.DATA_SET.NAME.length));
               list.add(MainActivity.DATA_SET.NAME[i-1]);
            }

      
            aa.notifyDataSetChanged();

         }
      });



      myListView.setOnItemClickListener(new OnItemClickListener() {

         public void onItemClick(AdapterView<?> av, View v, int position, long id) {

            detailedFragmentCall(av.getItemAtPosition(position).toString());

         }
      });

      return rootView;
   }



   public void detailedFragmentCall(String t) {

      final android.support.v4.app.FragmentManager fm = getFragmentManager();
      fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

      Intent intent = new Intent();

      // explicitly set the activity context and class
      // associated with the intent (context, class)
      intent.setClass(getActivity(), DetailsActivity.class);

      // pass the current position


      intent.putExtra("NAME", t);

      startActivity(intent);

   }
}

