package com.swdm.jskapp;

import java.util.Calendar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class LocationFragment extends Fragment {

	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private static int curDay;			    
	private static Calendar c;

	private static GoogleMap mMap;
	private static LatLng loc = new LatLng(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude()); // 위치 좌표 설정
	private static CameraPosition cp = new CameraPosition.Builder().target(loc).zoom(16).build();
	private static MarkerOptions marker = new MarkerOptions();


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		view = (RelativeLayout) inflater.inflate(R.layout.location_fragment, container, false);
		// Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map


		setUpMapIfNeeded(); // For setting up the MapFragment


		c = Calendar.getInstance();
		curDay = c.get(Calendar.DAY_OF_WEEK); 

		return view;
	}

	/***** Sets up the map if it is possible to do so *****/
	public static void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			// Check if we were successful in obtaining the map.

			if (mMap != null)
				setUpMap();

		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private static void setUpMap() {
		// For showing a move to my loction button
		//mMap.setMyLocationEnabled(true);
		// For dropping a marker at a point on the Map
		//mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
		// For zooming automatically to the Dropped PIN Location
		marker.title("조재").position(loc);
		//marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.s));
		mMap.addMarker(marker);
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));



		PolylineOptions rectOptions = new PolylineOptions()
		.width(10)
		.color(Color.GREEN); // Closes the polyline.

		
		if( 1 < curDay && curDay < 7 )
		{
			String[] arr;
			int i = curDay - 2;
			int k = 20 + i;

			while(i <= k)
			{
				arr = MainActivity.DATA_SET.LOCATION[i].split(",");
				if(!(arr[0].equals("a")))
				{
					LatLng loca = new LatLng(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]));
					rectOptions.add(loca);
					mMap.addMarker(new MarkerOptions().position(loca).title(MainActivity.DATA_SET.NAME[i])); 
				}
				i+=5;
			}

		}

		// Get back the mutable Polyline
		Polyline polyline = mMap.addPolyline(rectOptions);




		// 그림 지표 넣기
		//		LatLng NEWARK = new LatLng(37.45, 127.13);
		//	GroundOverlayOptions newarkMap = new GroundOverlayOptions();
		//		        newarkMap.image(BitmapDescriptorFactory.fromResource(R.drawable.s))
		//		        .position(NEWARK, 100f, 100f);
		//			mMap.addGroundOverlay(newarkMap);


	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (mMap != null)
			setUpMap();


		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((MapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null)
				setUpMap();
		}
	}

	/**** The mapfragment's id must be removed from the FragmentManager
	 **** or else if the same it is passed on the next time then 
	 **** app will crash ****/
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mMap != null) {
			MainActivity.fragmentManager.beginTransaction()
			.remove(MainActivity.fragmentManager.findFragmentById(R.id.location_map)).commit();
			mMap = null;
		}
	}
}