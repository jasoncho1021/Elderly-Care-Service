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

public class ChecklistEconomy extends Fragment {

	public static CheckBox[] Chbx_e_f;
	public static RadioButton[] Rdbtn_e_f;
	public static RatingBar Rbar_e_f;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.economy, container, false);
			
			Chbx_e_f = new CheckBox[8];						
			Chbx_e_f[0] = (CheckBox)rootView.findViewById(R.id.Chbx0_e);
			Chbx_e_f[1] = (CheckBox)rootView.findViewById(R.id.Chbx1_e);
			Chbx_e_f[2] = (CheckBox)rootView.findViewById(R.id.Chbx2_e);
			Chbx_e_f[3] = (CheckBox)rootView.findViewById(R.id.Chbx3_e);
			Chbx_e_f[4] = (CheckBox)rootView.findViewById(R.id.Chbx4_e);
			Chbx_e_f[5] = (CheckBox)rootView.findViewById(R.id.Chbx5_e);
			Chbx_e_f[6] = (CheckBox)rootView.findViewById(R.id.Chbx6_e);
			Chbx_e_f[7] = (CheckBox)rootView.findViewById(R.id.Chbx7_e);
		
			Rdbtn_e_f = new RadioButton[5];		
			Rdbtn_e_f[0] = (RadioButton)rootView.findViewById(R.id.Rdbtn0_e);
			Rdbtn_e_f[1] = (RadioButton)rootView.findViewById(R.id.Rdbtn1_e);
			Rdbtn_e_f[2] = (RadioButton)rootView.findViewById(R.id.Rdbtn2_e);
			Rdbtn_e_f[3] = (RadioButton)rootView.findViewById(R.id.Rdbtn3_e);
			Rdbtn_e_f[4] = (RadioButton)rootView.findViewById(R.id.Rdbtn4_e);

			Rbar_e_f = (RatingBar)rootView.findViewById(R.id.ratingBar_e);


			// Setting Module 

			for(int i = 0; i < ChecklistActivity.Chbx_e.length ; i++)
				if(ChecklistActivity.Chbx_e[i] == 1) // The Point thta Fragment access the Activity variable!!
					Chbx_e_f[i].setChecked(true);


			int index = ChecklistActivity.Rdbtn_e;
			Rdbtn_e_f[index].setChecked(true);

			Rbar_e_f.setRating(ChecklistActivity.Rbar_e);
		
		return rootView;
	}

	

}

