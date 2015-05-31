package com.swdm.jskapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

public class ChecklistHealth extends Fragment {

	public static CheckBox[] Chbx_h_f;
	public static RadioButton[] Rdbtn_h_f;
	public static RatingBar Rbar_h_f;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


		View rootView = inflater.inflate(R.layout.health, container, false);

		// Initializing module
		Chbx_h_f = new CheckBox[8];
		Chbx_h_f[0] = (CheckBox)rootView.findViewById(R.id.Chbx0_h);
		Chbx_h_f[1] = (CheckBox)rootView.findViewById(R.id.Chbx1_h);
		Chbx_h_f[2] = (CheckBox)rootView.findViewById(R.id.Chbx2_h);
		Chbx_h_f[3] = (CheckBox)rootView.findViewById(R.id.Chbx3_h);
		Chbx_h_f[4] = (CheckBox)rootView.findViewById(R.id.Chbx4_h);
		Chbx_h_f[5] = (CheckBox)rootView.findViewById(R.id.Chbx5_h);
		Chbx_h_f[6] = (CheckBox)rootView.findViewById(R.id.Chbx6_h);
		Chbx_h_f[7] = (CheckBox)rootView.findViewById(R.id.Chbx7_h);


		Rdbtn_h_f = new RadioButton[5];		
		Rdbtn_h_f[0] = (RadioButton)rootView.findViewById(R.id.Rdbtn0_h);
		Rdbtn_h_f[1] = (RadioButton)rootView.findViewById(R.id.Rdbtn1_h);
		Rdbtn_h_f[2] = (RadioButton)rootView.findViewById(R.id.Rdbtn2_h);
		Rdbtn_h_f[3] = (RadioButton)rootView.findViewById(R.id.Rdbtn3_h);
		Rdbtn_h_f[4] = (RadioButton)rootView.findViewById(R.id.Rdbtn4_h);

		Rbar_h_f = (RatingBar)rootView.findViewById(R.id.ratingBar_h);


		// Setting Module 

		for(int i = 0; i < ChecklistActivity.Chbx_h.length ; i++)
			if(ChecklistActivity.Chbx_h[i] == 1) // The Point thta Fragment access the Activity variable!!
				Chbx_h_f[i].setChecked(true);


		int index = ChecklistActivity.Rdbtn_h;
		Rdbtn_h_f[index].setChecked(true);

		Rbar_h_f.setRating(ChecklistActivity.Rbar_h);


		return rootView;
	}


}

