package com.turku.route;

import android.util.Log;
import android.view.View;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.view.View.OnClickListener;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import android.graphics.drawable.ColorDrawable;

import com.google.android.gms.maps.model.LatLng;
import android.support.v7.app.ActionBarActivity;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapActivity extends ActionBarActivity{
	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	private ImageView back;
	private TextView title;
	private  ArrayList<String> stoplist;	
	private int count = 0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapscreen);
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EAAB00")));
        View cView = getLayoutInflater().inflate(R.layout.mapscreen_title, null);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(cView);

        stoplist = getIntent().getStringArrayListExtra("stopslist");		
        title = (TextView)findViewById(R.id.mapscreen_title);
        title.setText(AlertDialogueAdapter.app_name_map); 
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
	  if (checkPlayServices()) {
	    // Then we're good to go!
			SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			back = (ImageView) findViewById(R.id.mapaddress_back);
			
			map = fm.getMap();
			map.setMyLocationEnabled(true);	
			map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(60.4500,22.2667) , 13.0f) );	
			if(getLocationFromAddress(getIntent().getStringExtra("spos")).latitude != 0.0)
				map.moveCamera( CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(getIntent().getStringExtra("spos")), 13.0f) );	
			else
				map.moveCamera( CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(getIntent().getStringExtra("epos")), 13.0f) );		
			
			try{
				if((getIntent().getStringExtra("onlywalk")).equalsIgnoreCase("walk")){
					Log.d("Lat lng", "Passed walking");
					Log.d("Lat lng", (getLocationFromAddress(getIntent().getStringExtra("spos"))).toString());
					Log.d("Lat lng", (getLocationFromAddress(getIntent().getStringExtra("epos"))).toString());
					
					map.addMarker(new MarkerOptions()
			        .position(getLocationFromAddress(getIntent().getStringExtra("spos")))
			        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
			        .title(getIntent().getStringExtra("spos")));
					
					map.addMarker(new MarkerOptions()
			        .position(getLocationFromAddress(getIntent().getStringExtra("epos")))
			        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
			        .title(getIntent().getStringExtra("epos")));			
				}
			}
			catch(NullPointerException n){}
			
			back.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {finish();}
			});
			
			try{
				ArrayList<Double> coordarraylist = (ArrayList<Double>) getIntent().getSerializableExtra("coordlist");
				mapRoute(coordarraylist);	
			}
			catch(NullPointerException n){}	  
	  }
	}
	
	private boolean checkPlayServices() {
		  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		  if (status != ConnectionResult.SUCCESS) {
		    if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
		      showErrorDialog(status);
		    } else {
		      Toast.makeText(this, AlertDialogueAdapter.device_notsupported, 
		          Toast.LENGTH_LONG).show();
		      finish();
		    }
		    return false;
		  }
		  return true;
		} 

		void showErrorDialog(int code) {
		  GooglePlayServicesUtil.getErrorDialog(code, this, 
		      REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
		}
		
		static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
		static final int REQUEST_CODE_PICK_ACCOUNT = 1002;

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  switch (requestCode) {
		    case REQUEST_CODE_RECOVER_PLAY_SERVICES:
		      if (resultCode == RESULT_CANCELED) {
		        Toast.makeText(this, AlertDialogueAdapter.google_playservices,
		            Toast.LENGTH_SHORT).show();
		        finish();
		      }
		      return;
		    case REQUEST_CODE_PICK_ACCOUNT:
			      if (resultCode == RESULT_OK) {
			        String accountName = data.getStringExtra(
			            AccountManager.KEY_ACCOUNT_NAME);
			        AccountUtils.setAccountName(this, accountName);
			      } else if (resultCode == RESULT_CANCELED) {
			        Toast.makeText(this, AlertDialogueAdapter.recquire_googleaccount, 
			            Toast.LENGTH_SHORT).show();
			        finish();
			      }
			      return;
		  }
		  super.onActivityResult(requestCode, resultCode, data);
		}
		
		private boolean checkUserAccount() {
			  String accountName = AccountUtils.getAccountName(this);
			  if (accountName == null) {
			    // Then the user was not found in the SharedPreferences. Either the
			    // application deliberately removed the account, or the application's
			    // data has been forcefully erased.
			    showAccountPicker();
			    return false;
			  }

			  Account account = AccountUtils.getGoogleAccountByName(this, accountName);
			  if (account == null) {
			    // Then the account has since been removed.
			    AccountUtils.removeAccount(this);
			    showAccountPicker();
			    return false;
			  }

			  return true;
			}

			private void showAccountPicker() {
			  Intent pickAccountIntent = AccountPicker.newChooseAccountIntent(
			      null, null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, 
			      true, null, null, null, null);
			  startActivityForResult(pickAccountIntent, REQUEST_CODE_PICK_ACCOUNT);
			}	
	public void mapRoute(ArrayList<Double> coordlist){	
		int count = 0, status = 0, startpos = 0;
		for (int val = 0; val < coordlist.size(); val++) {
			if (coordlist.get(val) == 0.0 || coordlist.get(val) == 1.0)count++;
		}
		
		for (int repeat = 1; repeat <= count; repeat++) {
			ArrayList<LatLng> points = new ArrayList<LatLng>();
			int result = 0, bus = 0;

			innerloop: for (int j = startpos; j < coordlist.size(); j = j + 2) {
				if (coordlist.get(j) == 0.0) {j++; status = 0; result++;}
				if (coordlist.get(j) == 1.0) {j++; bus++; status = 1; result++;}

				LatLng position = new LatLng(coordlist.get(j),coordlist.get(j + 1));
				points.add(position);
				startpos = j;
				
				if (result == 2) {
					if (status == 1 && bus < 2) status = 0;
					else 
						if (status == 0 && bus < 2) status = 1;					
					startpos = j - 1;
					break innerloop;
				}
			}
			if (status == 0) walkingRoute(points);
			else busRoute(points);
		}
		map.addMarker(new MarkerOptions()
        .position(new LatLng(coordlist.get(1), coordlist.get(2)))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        .title(getIntent().getStringExtra("spos")));
		
		map.addMarker(new MarkerOptions()
        .position(new LatLng(coordlist.get(coordlist.size()-2), coordlist.get(coordlist.size()-1)))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .title(getIntent().getStringExtra("epos")));
	}	
	
	public void busRoute(ArrayList<LatLng> points){
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(5);
		lineOptions.color(Color.RED);
		map.addPolyline(lineOptions);	
		
//		Log.d("Stop list", stoplist.size()+"");
//
//		Log.d("Stop list", stoplist.toString());
//
//		Log.d("count", count+"");
		for(int size=0; size<points.size();size++){
			if(count == stoplist.size())
				count=count-1;
			map.addMarker(new MarkerOptions()
	        .position(points.get(size))
	        .icon(BitmapDescriptorFactory.fromResource(R.drawable.circle))
	        .title(stoplist.get(count))
	        .draggable(false)
	        );
			count++;
		}
		count--;
 }
	public void walkingRoute(ArrayList<LatLng> points){
		PolylineOptions lineOptions = new PolylineOptions();
		lineOptions.addAll(points);
		lineOptions.width(5);
		lineOptions.color(Color.BLUE);
		map.addPolyline(lineOptions);		
 }	
	public LatLng getLocationFromAddress(String strAddress){	
		Geocoder coder = new Geocoder(this);
		List<Address> address;	
		try {
			    address = coder.getFromLocationName(strAddress,5);
			    if (address.size() == 0) { return new LatLng(0.0, 0.0);}
			    Address location = address.get(0);
			    return new LatLng(location.getLatitude(), location.getLongitude());
		    }
	    catch(IOException e){return new LatLng(0.0, 0.0);}
	}
}
