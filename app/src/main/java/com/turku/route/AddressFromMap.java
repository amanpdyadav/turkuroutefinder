package com.turku.route;

import java.util.List;
import java.util.Locale;
import android.os.Bundle;
import android.view.View;
import android.view.Gravity;
import android.widget.Toast;
import android.graphics.Color;
import android.content.Intent;
import android.widget.TextView;
import android.content.Context;
import android.location.Address;
import android.widget.ImageView;
import android.location.Geocoder;
import android.app.PendingIntent;
import android.accounts.AccountManager;
import android.support.v7.app.ActionBar;
import android.location.LocationManager;
import android.content.SharedPreferences;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.GoogleMap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

public class AddressFromMap extends ActionBarActivity{

	GoogleMap map;
	ImageView img;
	private TextView title;
	PendingIntent pendingIntent;
	LocationManager locationManager;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.mapscreen);		
		
		ActionBar ab = getSupportActionBar();
		ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EAAB00")));	
        View cView = getLayoutInflater().inflate(R.layout.mapaddress_title, null);
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(cView);
        title = (TextView)findViewById(R.id.mapscreen_title);
        title.setText(AlertDialogueAdapter.app_name_select_address);
	}
	
	@Override
	protected void onResume() {
	  super.onResume();
//	  if (checkPlayServices() && checkUserAccount()) {
	  if (checkPlayServices()) {
			SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
	        img = (ImageView) findViewById(R.id.mapaddress_back);
	        
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			final String pos = getIntent().getStringExtra("pos");
			
			map = fm.getMap();
			map.setMyLocationEnabled(true);	
			map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(60.4500,22.2667) , 13.0f) );
			Toast toast = Toast.makeText(getApplicationContext(),AlertDialogueAdapter.toastForMapAddress, Toast.LENGTH_SHORT);  
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
			map.setOnMapLongClickListener(new OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng point) {
					map.clear();
					map.animateCamera(CameraUpdateFactory.newLatLng(point));
					try {
						Geocoder geo = new Geocoder(AddressFromMap.this.getApplicationContext(),Locale.getDefault());
						List<Address> addresses = geo.getFromLocation(point.latitude,point.longitude, 1);
						if (addresses.isEmpty()) {Toast.makeText(getApplicationContext(), AlertDialogueAdapter.waiting_for_location,Toast.LENGTH_LONG).show();} 
						else {
							if (addresses.size() > 0) {
								Address address = addresses.get(0);
								String addressLine = address.getAddressLine(0);
								String locality = (address.getAddressLine(1).split(" "))[1];

								map.addMarker(new MarkerOptions().position(point).title(addressLine + "," + locality).draggable(true));
								if (pos.equalsIgnoreCase("to")) MainActivity.totext.setText(addressLine + "," + locality);
								else MainActivity.fromtext.setText(addressLine + "," + locality);
							}
						}
					} catch (Exception e) {e.printStackTrace();}
				}
			});
			map.setOnMarkerDragListener(new OnMarkerDragListener() {
				
				@Override
				public void onMarkerDragStart(Marker arg0) {}
				
				@Override
				public void onMarkerDragEnd(Marker marker) {
					LatLng point = marker.getPosition();
					map.clear();
					map.animateCamera(CameraUpdateFactory.newLatLng(point));
					try {
						Geocoder geo = new Geocoder(AddressFromMap.this.getApplicationContext(),Locale.getDefault());
						List<Address> addresses = geo.getFromLocation(point.latitude,point.longitude, 1);
						if (addresses.isEmpty()) {Toast.makeText(getApplicationContext(), AlertDialogueAdapter.waiting_for_location,Toast.LENGTH_LONG).show();} 
						else {
							if (addresses.size() > 0) {
								Address address = addresses.get(0);
								String addressLine = address.getAddressLine(0);
								String locality = (address.getAddressLine(1).split(" "))[1];

								map.addMarker(new MarkerOptions().position(point).title(addressLine + "," + locality).draggable(true));
								if (pos.equalsIgnoreCase("to")) MainActivity.totext.setText(addressLine + "," + locality);
								else MainActivity.fromtext.setText(addressLine + "," + locality);
							}
						}
					} catch (Exception e) {e.printStackTrace();}
				}
				
				@Override
				public void onMarkerDrag(Marker arg0) {}
			});
			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {			
				@Override
				public void onInfoWindowClick(Marker arg0) {finish();}
			});
			
			img.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {finish();}
			});
	  }
	}
	private boolean checkPlayServices() {
		  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		  if (status != ConnectionResult.SUCCESS) {
		    if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
		      showErrorDialog(status);
		    } else {
		      Toast.makeText(this, AlertDialogueAdapter.device_notsupported, Toast.LENGTH_LONG).show();
		      finish();
		    } return false;
		  }return true;
		} 

		void showErrorDialog(int code) {
		  GooglePlayServicesUtil.getErrorDialog(code, this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
		}
		
		static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;
		static final int REQUEST_CODE_PICK_ACCOUNT = 1002;

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  switch (requestCode) {
		    case REQUEST_CODE_RECOVER_PLAY_SERVICES:
		      if (resultCode == RESULT_CANCELED) {
		        Toast.makeText(this, AlertDialogueAdapter.google_playservices, Toast.LENGTH_SHORT).show();
		        finish();
		      }return;
		    case REQUEST_CODE_PICK_ACCOUNT:
			      if (resultCode == RESULT_OK) {
			        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			        AccountUtils.setAccountName(this, accountName);
			      } else if (resultCode == RESULT_CANCELED) {
			        Toast.makeText(this, AlertDialogueAdapter.recquire_googleaccount, Toast.LENGTH_SHORT).show();
			        finish();
			      } return;
		  } super.onActivityResult(requestCode, resultCode, data);
		}
		
//		private boolean checkUserAccount() {
//			  String accountName = AccountUtils.getAccountName(this);
//			  if (accountName == null) {
//			    // Then the user was not found in the SharedPreferences. Either the
//			    // application deliberately removed the account, or the application's
//			    // data has been forcefully erased.
//			    showAccountPicker();
//			    return false;
//			  }
//
//			  Account account = AccountUtils.getGoogleAccountByName(this, accountName);
//			  if (account == null) {
//			    // Then the account has since been removed.
//			    AccountUtils.removeAccount(this);
//			    showAccountPicker();
//			    return false;
//			  } return true;
//			}
//
//			private void showAccountPicker() {
//			  Intent pickAccountIntent = AccountPicker.newChooseAccountIntent(
//			      null, null, new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, 
//			      true, null, null, null, null);
//			  startActivityForResult(pickAccountIntent, REQUEST_CODE_PICK_ACCOUNT);
//			}
}
