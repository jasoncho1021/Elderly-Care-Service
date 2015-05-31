package com.swdm.jskapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RatingBar;

public class ChecklistResidence extends Fragment {

	public static CheckBox[] Chbx_r_f;
	public static RadioButton[] Rdbtn_r_f;
	public static RatingBar Rbar_r_f;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.residence, container, false);
		
		
		Chbx_r_f = new CheckBox[8];				
		Chbx_r_f[0] = (CheckBox)rootView.findViewById(R.id.Chbx0_r);
		Chbx_r_f[1] = (CheckBox)rootView.findViewById(R.id.Chbx1_r);
		Chbx_r_f[2] = (CheckBox)rootView.findViewById(R.id.Chbx2_r);
		Chbx_r_f[3] = (CheckBox)rootView.findViewById(R.id.Chbx3_r);
		Chbx_r_f[4] = (CheckBox)rootView.findViewById(R.id.Chbx4_r);
		Chbx_r_f[5] = (CheckBox)rootView.findViewById(R.id.Chbx5_r);
		Chbx_r_f[6] = (CheckBox)rootView.findViewById(R.id.Chbx6_r);
		Chbx_r_f[7] = (CheckBox)rootView.findViewById(R.id.Chbx7_r);
		
		Rdbtn_r_f = new RadioButton[5];		
		Rdbtn_r_f[0] = (RadioButton)rootView.findViewById(R.id.Rdbtn0_r);
		Rdbtn_r_f[1] = (RadioButton)rootView.findViewById(R.id.Rdbtn1_r);
		Rdbtn_r_f[2] = (RadioButton)rootView.findViewById(R.id.Rdbtn2_r);
		Rdbtn_r_f[3] = (RadioButton)rootView.findViewById(R.id.Rdbtn3_r);
		Rdbtn_r_f[4] = (RadioButton)rootView.findViewById(R.id.Rdbtn4_r);

		Rbar_r_f = (RatingBar)rootView.findViewById(R.id.ratingBar_r);

		// Setting Module 

		for(int i = 0; i < ChecklistActivity.Chbx_r.length ; i++)
			if(ChecklistActivity.Chbx_r[i] == 1) // The Point thta Fragment access the Activity variable!!
				Chbx_r_f[i].setChecked(true);


		int index = ChecklistActivity.Rdbtn_r;
		Rdbtn_r_f[index].setChecked(true);

		Rbar_r_f.setRating(ChecklistActivity.Rbar_r);
	
		
		return rootView;
	}


}

