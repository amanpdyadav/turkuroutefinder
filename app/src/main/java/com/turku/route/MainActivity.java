package com.turku.route;

import java.util.List;
import java.util.Locale;

import net.sf.json.JSON;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.os.Build;
import android.os.Bundle;
import android.app.Dialog;
import java.util.ArrayList;
import android.widget.Toast;
import android.view.MenuItem;
import android.widget.Button;
import android.text.Editable;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.ListView;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextWatcher;
import android.text.format.Time;
import android.widget.ImageView;
import android.view.ContextMenu;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.RadioGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.app.ProgressDialog;
import android.widget.LinearLayout;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.location.LocationManager;
import android.content.res.Configuration;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu.ContextMenuInfo;
import android.support.v4.app.ActionBarDrawerToggle;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.turku.historydatabase.Address;
import com.turku.historydatabase.DBAdapterAddress;
import com.turku.historydatabase.DBAdapterHistory;
import com.turku.historydatabase.DBAdapterLanguage;
import com.turku.historydatabase.Language;

public class MainActivity extends ActionBarActivity implements  OnItemClickListener {
	private Button findRoute;
	private RadioGroup radioBtnGroup;
	private DrawerLayout drawerlayout;
	private LinearLayout dateBtn, timeBtn;
	private ListView drawerlist, historylist;	
	private static TextView date, time, title;
	static AutoCompleteTextView totext, fromtext;
	private RadioButton depart_radio, arrive_radio;
	private ImageView swap, history, globe_to, globe_from, lanBtn, drawerBtn;  
	
	private MyAdapter myadapter;
	private List<String> addresslist;
	private ArrayAdapter arrayAdapter;	
	private String connection = "true";
	private ArrayList<String> datalist;
	public static ProgressDialog  dialog;
	String dateString, timeString, focus="true";
	private ActionBarDrawerToggle drawerListener;	

	private static FetchData fetch = new FetchData();	
	private static String spos="", epos="", delete="";
	private ArrayAdapter<String> mAutoCompleteAdapter;
	static final int DATE_DIALOG_ID = 0, TIME_DIALOG_ID = 1;
	public static int cMarginal = 3, wSpeed = 2, rNumber = 3, rOptimize = 1;
	private static int mYear, mMonth, mDay, mHour, mMinute, timemode=1, status = 1, charcounter;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DBAdapterAddress.init(this);
		DBAdapterHistory.init(this);
		DBAdapterLanguage.init(this);
		
		//This will set the language when user install the app for the first time
		if(DBAdapterLanguage.getAllData().size() == 0){
			String val = Locale.getDefault().getLanguage();
        	if(val.equalsIgnoreCase("fi")){DBAdapterLanguage.addAddress(new Language("FIN"));}
        	else if(val.equalsIgnoreCase("sv") || val.equalsIgnoreCase("sv_SE")){DBAdapterLanguage.addAddress(new Language("SWE"));}
        	else{DBAdapterLanguage.addAddress(new Language("ENG"));}
        }
		
		//This line will set the string type either english, finnish or swedish		
		AlertDialogueAdapter stringval = new AlertDialogueAdapter();
		
		//Set the title background color
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EAAB00")));
		
		//Set the custom title view
		View cView = getLayoutInflater().inflate(R.layout.main_activity_title, null);
		android.support.v7.app.ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(cView);		
        
        //set the title name from database
        title = (TextView)findViewById(R.id.app_title);
        title.setText(AlertDialogueAdapter.app_name);
        
        //These will set the language of the app every time the user starts the app
		lanBtn = (ImageView) findViewById(R.id.lan_btn); 		
		List<String> lanGuage = DBAdapterLanguage.getAllData();		
		if(lanGuage.get(0).equals("FIN")){lanBtn.setImageResource(R.drawable.fin);}
		if(lanGuage.get(0).equals("SWE")){lanBtn.setImageResource(R.drawable.swe);}
		if(lanGuage.get(0).equals("ENG")){lanBtn.setImageResource(R.drawable.eng);}		
		lanBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {selectLanguage();}
		});
		
		
		drawerBtn = (ImageView) findViewById(R.id.drawer_btn); 		
		swap = (ImageView) findViewById(R.id.change_img); 	
		findRoute = (Button) findViewById(R.id.find_route);
		findRoute.setText(AlertDialogueAdapter.find_route);
		
		depart_radio = (RadioButton) findViewById(R.id.radio_depart);
		depart_radio.setText(AlertDialogueAdapter.departure_string);
		
		arrive_radio = (RadioButton) findViewById(R.id.radio_arrival);
		arrive_radio.setText(AlertDialogueAdapter.arrival_string);		

		globe_from = (ImageView) findViewById(R.id.globe_from);
		globe_to = (ImageView) findViewById(R.id.globe_to);
		dateBtn = (LinearLayout) findViewById(R.id.set_date);
		timeBtn = (LinearLayout) findViewById(R.id.set_time);
		drawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		radioBtnGroup = (RadioGroup) findViewById(R.id.radio_btngroup);	
		
		DBAdapterAddress.addAddressList(FetchData.getDefaultAddress());
		addresslist = DBAdapterAddress.getAllData();
		datalist = DBAdapterHistory.getAllData();	
		arrayAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.addresslist, datalist);
				
		drawerlist = (ListView) findViewById(R.id.drawerList);
		myadapter = new MyAdapter(this);
		drawerlist.setAdapter(myadapter);
		drawerlist.setOnItemClickListener(this);
		
		//This will hide the history button if the history is empty
		history =  (ImageView) findViewById(R.id.history_btn);
		if(datalist.size() == 0)history.setVisibility(View.GONE);
		
		//Sets the history listview and hide it unless user want to see it
		historylist = (ListView) findViewById(R.id.historylist);
		historylist.setAdapter(arrayAdapter);
		historylist.setVisibility(View.GONE);	
		registerForContextMenu(historylist);
		
		//Sets the adapter of the address suggestion
		mAutoCompleteAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.addresslist , addresslist);
		totext = (AutoCompleteTextView) findViewById(R.id.to_text);
		totext.setAdapter(mAutoCompleteAdapter);
	    totext.setThreshold(1);
		totext.setHint(AlertDialogueAdapter.maEditToHint);
		totext.requestFocus();
		
		fromtext = (AutoCompleteTextView) findViewById(R.id.from_text);	
		fromtext.setAdapter(mAutoCompleteAdapter);
		fromtext.setThreshold(1);
		fromtext.setHint(AlertDialogueAdapter.maEditFromHint);
		fromtext.setText(AlertDialogueAdapter.my_location);

		//Sets current date and time to the fields when app starts
		time = (TextView) findViewById(R.id.set_time_text);
		time.setText(AlertDialogueAdapter.now_string);
		date = (TextView) findViewById(R.id.set_date_text);
		date.setText(AlertDialogueAdapter.today_string);
		setCurrentDateTime();
			


/*********************************************************************************************************************
 * 						Adding async task to from autocomplete textview. 											 *
*********************************************************************************************************************/		
		fromtext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, final int start, int before, int count) {
				if(connection.equalsIgnoreCase("true") && isNetworkAvailable()){	
					if(fromtext.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.my_location_change)) fromtext.setText("");
					if(charcounter<start) charcounter = start; 
					
					startif:
					if(start > 2 && (""+mAutoCompleteAdapter.isEmpty()).equalsIgnoreCase("true")){
						if(start < charcounter) break startif;
						
						DBAdapterAddress.addAddressList(fetch.autocomplete(fetch.validatePosition(s.toString())));
						mAutoCompleteAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.addresslist, DBAdapterAddress.getAllData());
						mAutoCompleteAdapter.notifyDataSetChanged();
						fromtext.setAdapter(mAutoCompleteAdapter);	
						fromtext.showDropDown();		
						
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});

		
/*********************************************************************************************************************
 * 						Adding async task to TO autocomplete textview. 												 *
*********************************************************************************************************************/					
		totext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(connection.equalsIgnoreCase("true") && isNetworkAvailable()){	
						if(totext.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.my_location_change)) totext.setText("");
						if(charcounter<start) charcounter = start;
						
						startif:
						if(start > 2 && (""+mAutoCompleteAdapter.isEmpty()).equalsIgnoreCase("true")){
							if(start < charcounter) break startif;

							DBAdapterAddress.addAddressList(fetch.autocomplete(fetch.validatePosition(s.toString())));
							mAutoCompleteAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.addresslist, DBAdapterAddress.getAllData());
							mAutoCompleteAdapter.notifyDataSetChanged();
							totext.setAdapter(mAutoCompleteAdapter);	
							totext.showDropDown();							
						}
					}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});	


/*********************************************************************************************************************
 * 						Finding the route event invoked by find route button.										 *
*********************************************************************************************************************/			
		findRoute.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				if(isNetworkAvailable()){
					Intent nextScreen = new Intent(MainActivity.this, RouteView.class);
					LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
					String status = "true";
	
					if (fromtext.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.my_location)) {
						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  spos = getCurrentPos();	
						else showGPSDisabledAlertToUser(); 
					}
					else spos = fromtext.getText().toString();
									
					if (totext.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.my_location)) {
						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))  epos = getCurrentPos();	
						else showGPSDisabledAlertToUser();
					}					
					else epos = totext.getText().toString();
					
					if(radioBtnGroup.getCheckedRadioButtonId() == 2131034200)
						timemode = 2;

					try{
						if(spos.equals("") || spos.equalsIgnoreCase(null)){
							status = "false";
							Toast toast = Toast.makeText(MainActivity.this, AlertDialogueAdapter.from_empty, Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
							toast.show();
						}
					}
					catch(NullPointerException n){
						status = "false";
						Toast toast = Toast.makeText(MainActivity.this, AlertDialogueAdapter.from_empty, Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
						toast.show();
					}
					 
					try{
						if(epos.equals("") || epos.equalsIgnoreCase(null)) {
							status = "false";
							Toast toast = Toast.makeText(MainActivity.this, AlertDialogueAdapter.to_empty, Toast.LENGTH_LONG);
							toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
							toast.show();
						}
					}
					catch(NullPointerException n){
						status = "false";
						Toast.makeText(MainActivity.this, AlertDialogueAdapter.to_empty, Toast.LENGTH_LONG).show();
					}
					
					if((!epos.equals("") || !epos.equalsIgnoreCase(null) || !spos.equals("") || !spos.equalsIgnoreCase(null)) && status.equalsIgnoreCase("true")){
						dialog = ProgressDialog.show(MainActivity.this, "", AlertDialogueAdapter.progress,true);
						String url = getURL(spos, epos, mYear+""+pad(mMonth)+""+pad(mDay), pad(mHour)+""+pad(mMinute), timemode+"");	
						nextScreen.putExtra("spos", spos);
						nextScreen.putExtra("epos", epos);
						nextScreen.putExtra("url", url);
						startActivity(nextScreen);
						addHistory("add");
					}
				}
			}
		});			
		
/*********************************************************************************************************************
 * 									History button clicked event.													 *
*********************************************************************************************************************/
		history.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				if(status == 0){
					historylist.setVisibility(view.GONE);
	 				history.setImageResource(R.drawable.down);
	        		status = 1;
	        	 }
	        	 else{
	 				historylist.setVisibility(view.VISIBLE);
	 				history.setImageResource(R.drawable.up);
	        		status = 0;
	        	 }
			}
		});

/*********************************************************************************************************************
 * 									History list clicked event.													 *
*********************************************************************************************************************/		
		historylist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,int position, long id) {
				String[] parts = datalist.get(position).split(" -> ");
				fromtext.setText(parts[0]);
				totext.setText(parts[1]);
				delete=datalist.get(position);
			}
		});


/*********************************************************************************************************************
 * 						Selecting the address from the google map for From position.								 *
*********************************************************************************************************************/			
		globe_from.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent nextScreen = new Intent(MainActivity.this, AddressFromMap.class);
				nextScreen.putExtra("pos", "from");
				startActivity(nextScreen);
			}
		});		
		
/*********************************************************************************************************************
 * 						Selecting the address from the google map for TO position.								 *
*********************************************************************************************************************/			
		globe_to.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent nextScreen = new Intent(MainActivity.this, AddressFromMap.class);
				nextScreen.putExtra("pos", "to");
				startActivity(nextScreen);				
			}
		});
		
/*********************************************************************************************************************
 * 						Adding event to swap the address.				 											 *
*********************************************************************************************************************/	
		swap.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				String ftext = fromtext.getText().toString();
				fromtext.setText(totext.getText().toString());
				totext.setText(ftext);	
				if(focus.equals("true")){
					fromtext.requestFocus();
					focus="false";
				}
				else{
					totext.requestFocus();
					focus = "true";
				}
			}
		});
		
/*********************************************************************************************************************
 * 						Changing the date.																			 *
*********************************************************************************************************************/			
		dateBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {showDialog(DATE_DIALOG_ID);}
		});

/*********************************************************************************************************************
 * 						Changing the time.																			 *
*********************************************************************************************************************/	
		timeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (time.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.now_string)) {showDialog(TIME_DIALOG_ID);} 
				else {setCurrentDateTime();}
			}
		});
		
/*********************************************************************************************************************
 * 									Drawer event invoked for slide form left to right or vice versa.				 *
*********************************************************************************************************************/		
		drawerListener = new ActionBarDrawerToggle(this, drawerlayout, R.drawable.ic_drawer,R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				getSupportActionBar().setTitle(AlertDialogueAdapter.app_name);
			}
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};	
		drawerlayout.setDrawerListener(drawerListener);
		
		//Drawer opener and loser from a button event.		
		drawerBtn.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	if(drawerlayout.isDrawerOpen(GravityCompat.START))
	        		drawerlayout.closeDrawer(Gravity.LEFT);
	        	else
		        	drawerlayout.openDrawer(Gravity.LEFT);
	        }
	    });     
}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
	    return false;//return here true if you want some action to be done from menu button and add the list to menu xml.
	}
	
/*********************************************************************************************************************
 * 						FOR CREATING CONTEXT MENU FOR HISTORY LIST VIEW ITEM.										 *
*********************************************************************************************************************/	
	@Override  
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	    super.onCreateContextMenu(menu, v, menuInfo);  
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    	menu.setHeaderTitle(AlertDialogueAdapter.delete_title);
	        menu.add(0, v.getId(), 0, AlertDialogueAdapter.delete); 
	        menu.add(0, v.getId(), 0, AlertDialogueAdapter.delete_all);
		    delete=datalist.get(info.position);
    }  

/*********************************************************************************************************************
 * 						FOR CREATING DELET MANU FOR ITEM SELECTE IN THE HISTORY LISTVIEW.							 *
*********************************************************************************************************************/	  
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  	    	
        if(item.getTitle()==AlertDialogueAdapter.delete){addHistory("delete");}	
        if(item.getTitle()==AlertDialogueAdapter.delete_all){addHistory("deleteall");}			 
        else {return false;}  
        return true;  
    } 

/*********************************************************************************************************************
 * 						FOR ADDING THE DATA TO THE HISTORY LIST VIEW.												 *
*********************************************************************************************************************/	
  	public void addHistory(String status){
		if (status.equalsIgnoreCase("add")){  		
			if(DBAdapterHistory.exist(spos+" -> "+epos) == 0){DBAdapterHistory.addAddress(new Address(spos+" -> "+epos));}
		}
		if (status.equalsIgnoreCase("delete")){DBAdapterHistory.deleteHistory(delete);}
		if (status.equalsIgnoreCase("deleteall")){DBAdapterHistory.deleteAllHistory();}
		datalist = DBAdapterHistory.getAllData();
    	arrayAdapter = new ArrayAdapter<String>(MainActivity.this,R.layout.addresslist, datalist);
		historylist.setAdapter(arrayAdapter);
    	arrayAdapter.notifyDataSetChanged();
    	if(datalist.size() == 0) history.setVisibility(View.GONE);
    	else history.setVisibility(View.VISIBLE);
  	}

/*********************************************************************************************************************
 * 						FOR SETTING CURRENT DATE AND TIME.															 *
*********************************************************************************************************************/	  	
	public static void setCurrentDateTime(){
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		mYear = today.year;
		mHour = today.hour;
		time.setText(AlertDialogueAdapter.now_string);
		mMonth = today.month+1;
		mDay = today.monthDay;
		date.setText(AlertDialogueAdapter.today_string);
		mMinute = today.minute;
	}

/*********************************************************************************************************************
 * 						FOR UPDATING DATE AND TIME TEXTVIEW.														 *
*********************************************************************************************************************/		
	private void updateDisplay(int choice) {
		if (choice == 0) {
			dateString = pad(mYear) + "." + pad(mMonth) + "." + pad(mDay);
			date.setText(dateString);
		} else {
			timeString = pad(mHour) + ":" + pad(mMinute);
			time.setText(timeString);
		}
	}

/*********************************************************************************************************************
 * 						FOR FORMATTING DATE AND TIME.																 *
*********************************************************************************************************************/	
	private static String pad(int c) {
		if (c >= 10) return String.valueOf(c);
		else return "0" + String.valueOf(c);
	}

/*********************************************************************************************************************
 * 						FOR CREATING ALERT DIALOGUE EITHER FOR CALENDER OR TIME.									 *
*********************************************************************************************************************/	
	@Override
	protected Dialog onCreateDialog(int id) {
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		mYear = today.year;
		mHour = today.hour;
		mMonth = today.month+1;
		mDay = today.monthDay;
		mMinute = today.minute;

		switch (id) {
			case DATE_DIALOG_ID: return new DatePickerDialog(this, mDateSetListener, mYear, mMonth-1, mDay);
			case TIME_DIALOG_ID: return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
		}
		return null;
	}

/*********************************************************************************************************************
 * 						ALERT DIALOGUE FOR DATE PICKER.																 *
*********************************************************************************************************************/	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mYear = year;
			mDay = dayOfMonth;
			mMonth = monthOfYear+1;
			updateDisplay(0);
		}
	};

/*********************************************************************************************************************
 * 						ALERT DIALOGUE FOR TIME PICKER.																 *
*********************************************************************************************************************/	
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay(1);
		}
	};

/*********************************************************************************************************************
 * 						THESE 4 OVER RIDE FUNCTION COMES ALONG WITH DRAWERLISTNER.									 *
*********************************************************************************************************************/	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {return true;}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

/*********************************************************************************************************************
 * 						INVOKING EVENT FROM DRAWER ALERT DIALOGUE.													 *
*********************************************************************************************************************/	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {selectItem(position);}

/*********************************************************************************************************************
 * 						EVENT HANDLER FROM DRAWER ALERT DIALOGUE.													 *
*********************************************************************************************************************/	
	public void selectItem(int position) {
		drawerlist.setItemChecked(position, true);

		if (position==0){
			setTitle(AlertDialogueAdapter.app_name);
			drawerlayout.closeDrawers();
		}
		else
			setTitle(myadapter.getItem(position));
		
		if (position > 0 && position != 2) {
			FragmentManager manager = getFragmentManager();
			AlertDialogView alert = new AlertDialogView();
			Bundle b = new Bundle();
			b.putInt("position", position);
			if(position == 1)
				switch(rOptimize){
					case 1: b.putInt("aposition", 0); break;
					case 2: b.putInt("aposition", 1); break;
					case 3: b.putInt("aposition", 2); break;
					case 4: b.putInt("aposition", 3); break;					
				}
			if(position == 3)
				switch(wSpeed){
					case 1: b.putInt("aposition", 0); break;
					case 2: b.putInt("aposition", 1); break;
					case 3: b.putInt("aposition", 2); break;
					case 4: b.putInt("aposition", 3); break;
					case 5: b.putInt("aposition", 4); break;					
				}
			if(position == 4)
				switch(rNumber){
					case 2: b.putInt("aposition", 0); break;
					case 3: b.putInt("aposition", 1); break;
					case 5: b.putInt("aposition", 2); break;					
				}
			alert.setArguments(b);
			alert.show(manager, "alert_dialog_radio");
		}		
		if (position == 2) {
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(AlertDialogueAdapter.cmarginalTitle);
			final TextView text = new TextView(this);
			LinearLayout linear = new LinearLayout(this);
			linear.setOrientation(1);
			
			final SeekBar seek = new SeekBar(this);
			linear.addView(text);
			linear.addView(seek);

			alert.setView(linear);
			
			text.setText(AlertDialogueAdapter.cmarginal + cMarginal + AlertDialogueAdapter.min);
			seek.setProgress(cMarginal);
			seek.setMax(10);
			seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	            @Override
	            public void onStopTrackingTouch(SeekBar seekBar) {}

	            @Override
	            public void onStartTrackingTouch(SeekBar seekBar) {}

	            @Override
	            public void onProgressChanged(SeekBar seekBar, int progress,
	                    boolean fromUser) {	            		
		                text.setText(AlertDialogueAdapter.cmarginal + progress + AlertDialogueAdapter.min);
	            }
	        });
			
			alert.setPositiveButton(AlertDialogueAdapter.OK,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							if(seek.getProgress() < 3) cMarginal = 3;
							else cMarginal = seek.getProgress();
							seek.getProgress();
							dialog.cancel();
						}
					});
			alert.show();
		}		
	}
	public void setTitle(String title) {
//		MainActivity.title.setText(title);
	}

/*********************************************************************************************************************
 * 						SETTING URL.																				 *
*********************************************************************************************************************/		
	public String getURL(String spos, String epos, String date, String time, String timemode){
		final String url = "http://api.matka.fi/public-lvm/fi/api/?"+
				"from=" + fetch.validatePosition(spos) + 
				"&to=" + fetch.validatePosition(epos) + "&data_source=navici&"+
				"date=" + date +
				"&time=" + time + 
				"&timemode=" + timemode +
				"&show=" + rNumber +
				"&optimize=" + rOptimize +
				"&margin=" + cMarginal +
				"&walkspeed=" + wSpeed +
				"&user=Projektkurs&pass=Reittiopas";
		Log.d("URL", url);
		return url;
	}

/*********************************************************************************************************************
 * 						SETTING URL WITH NEW DATE AND TIME.															 *
*********************************************************************************************************************/	
	public static String getURL(String newtime, String newdate){
		if(newtime.equalsIgnoreCase("setCurrentDateTime()")){
			setCurrentDateTime();
			newtime = pad(mHour)+""+pad(mMinute);
			newdate = mYear+""+pad(mMonth)+""+pad(mDay);
		}
		if(newtime.equalsIgnoreCase("previoustime")){
			mMinute = mMinute-30;
			if (mMinute < 0){
				mMinute = 60 + (mMinute-30);
				mHour = mHour - 1;
				if (mHour < 0){
					mHour = 24 + (mHour-1);
					mDay = mDay - 1;
				}
			}
			newtime = pad(mHour)+""+pad(mMinute);
			newdate = mYear+""+pad(mMonth)+""+pad(mDay);
		}
		final String url = "http://api.matka.fi/public-lvm/fi/api/?"+
				"from=" + fetch.validatePosition(spos) + 
				"&to=" + fetch.validatePosition(epos) + "&data_source=navici&"+
				"date=" + newdate +
				"&time=" + newtime + 
				"&timemode=" + timemode +
				"&show=" + rNumber +
				"&optimize=" + rOptimize +
				"&margin=" + cMarginal +
				"&walkspeed=" + wSpeed +
				"&user=Projektkurs&pass=Reittiopas";
		Log.d("URL", url);
		return url;
	}
	
/*********************************************************************************************************************
 * 						GETTING CURRENT POSITION.										 *
*********************************************************************************************************************/	
	public String getCurrentPos() {
		GPSTracker gpsTracker = new GPSTracker(this);		
		if (gpsTracker.canGetLocation()){return gpsTracker.getTurkuAddress(this);}
		else{gpsTracker.showSettingsAlert();return null;}
	}

/*********************************************************************************************************************
 * 						ALERT DIALOGUE FOR NO INTERNET ACCESS.														 *
*********************************************************************************************************************/		
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
			connection = "true";
			return true;
		}
		else{
			showAlertDialog(MainActivity.this, AlertDialogueAdapter.InternetAlertDialogTitle, AlertDialogueAdapter.InternetAlertDialogMessage, false);
			connection = "false";
			return false;
		}
	}
	
/*********************************************************************************************************************
 * 						ALERT DIALOGUE FOR ENABLING GPS.															 *
*********************************************************************************************************************/		
	private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(AlertDialogueAdapter.GPSAlertDialogMessage)
        .setCancelable(false)
        .setPositiveButton(AlertDialogueAdapter.settings,
         new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
            	if (fromtext.getText().toString().equalsIgnoreCase(AlertDialogueAdapter.my_location)) fromtext.setText("");
            	else totext.setText("");
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	
	/*********************************************************************************************************************
	 * 						SETTING ALERT DIALOGUE.														 *
	*********************************************************************************************************************/		
		@SuppressWarnings("deprecation")
		public void showAlertDialog(Context context, String title, String message, Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	        alertDialog.setTitle(title); 
	        alertDialog.setMessage(message);         
	        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail); 
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
	        alertDialog.show();
	}
	public void selectLanguage(){
		AlertDialog.Builder builderSingle = new AlertDialog.Builder( MainActivity.this);
		List<String> language = new ArrayList<String>();
		language.add("English");
		language.add("Suomi");
		language.add("Svenska");
	    builderSingle.setIcon(R.drawable.ic_launcher);
	    
	    builderSingle.setTitle("Select the Language");
	    
	    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
	    arrayAdapter.addAll(language);
	    builderSingle.setNegativeButton("Cancel",
	            new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    dialog.dismiss();
	                }
	            });

	    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	String val = arrayAdapter.getItem(which);
	                	if(val.equalsIgnoreCase("English")){
	                		DBAdapterLanguage.updateAddressData(new Language("ENG"));
	                	}
	                	if(val.equalsIgnoreCase("Suomi")){
	                		DBAdapterLanguage.updateAddressData(new Language("FIN"));
	                	}
	                	if(val.equalsIgnoreCase("Svenska")){
	                		DBAdapterLanguage.updateAddressData(new Language("SWE"));
	                	}
	                	if (Build.VERSION.SDK_INT >= 11) {
	                		drawerlayout.removeAllViews();
	                	    recreate();
	                	} else {
	                		drawerlayout.removeAllViews();
	                	    Intent intent = getIntent();
	                	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	                	    finish();
	                	    overridePendingTransition(0, 0);
	                	    startActivity(intent);
	                	    overridePendingTransition(0, 0);
	                	}
	                	dialog.dismiss();
	                }
	            });
	    builderSingle.show();
	 }
}//CLOSING THE MAIN ACTIVITY


/*********************************************************************************************************************
 * 						BASE ADAPTOR CLASS.																			 *
*********************************************************************************************************************/	
class MyAdapter extends BaseAdapter {
	private Context context;
	String[] drawerlist_item;

	public MyAdapter(Context context) {
		this.context = context;
		drawerlist_item = AlertDialogueAdapter.drawerlistitem;
	}

	@Override
	public int getCount() {return drawerlist_item.length;}

	@Override
	public String getItem(int position) {return drawerlist_item[position];}

	@Override
	public long getItemId(int position) {return position;}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.drawer_row, parent, false);
		} else {
			row = convertView;
		}
		TextView title = (TextView) row.findViewById(R.id.options_view);
		title.setText(drawerlist_item[position]);
		return row;
	}
}