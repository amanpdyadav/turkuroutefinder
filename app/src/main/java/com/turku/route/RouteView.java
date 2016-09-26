package com.turku.route;

import java.util.List;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.text.Html;
import java.util.ArrayList;
import android.view.Gravity;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Color;
import java.text.DecimalFormat;
import android.util.TypedValue;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.view.View.OnClickListener;
import com.turku.historydatabase.Address;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import com.turku.historydatabase.DBAdapterAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RouteView extends ActionBarActivity {
	
	JSONArray jarray;
	RouteAdaptor routeAdaptor;
	SetRoute setroute = new SetRoute();
	DecimalFormat df = new DecimalFormat("#.#");
	RouteDetail routedetail = new RouteDetail();

	private TableLayout tablelayout;
	private TextView endpos, startpos;
	private ImageView back, previous, refresh, next;
	private String spos, epos, nexttime, nextdate, timestatus = "true",previousdate,status = "true", url;

	@Override	
	protected void onCreate(final Bundle savedInstanceState) {
		DBAdapterAddress.init(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routelist);

		tablelayout = (TableLayout) findViewById(R.id.TableLayout);
		startpos = (TextView) findViewById(R.id.start_pos);
		endpos = (TextView) findViewById(R.id.end_pos);
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EAAB00")));
        View cView = getLayoutInflater().inflate(R.layout.routeview_title, null);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(cView);		

		previous = (ImageView) findViewById(R.id.action_previous);
		refresh = (ImageView) findViewById(R.id.action_refresh);
		back = (ImageView) findViewById(R.id.routeview_back);
		next = (ImageView) findViewById(R.id.action_next);
		
		spos = getIntent().getStringExtra("spos");
		epos =  getIntent().getStringExtra("epos");		
		url = getIntent().getStringExtra("url");
		
/*********************************************************************************************************************
 * 					EVENT FOR THE BACK BUTTON.  																	 *
 *********************************************************************************************************************/		
		back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				MainActivity.setCurrentDateTime();
				finish();				
			}
		});

/*********************************************************************************************************************
 * 					EVENT FOR THE PREVIOUS BUTTON.  																 *
 *********************************************************************************************************************/			
		previous.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {		
				timestatus = "true";
				url = MainActivity.getURL("previoustime", previousdate);
				tablelayout.removeAllViews();
				startRouteView((JSONObject) (new FetchData().getXMLdataFromurl(url)));
			}
		});

/*********************************************************************************************************************
 * 					EVENT FOR THE REFRESH BUTTON.  																	 *
 *********************************************************************************************************************/			
		refresh.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				url = MainActivity.getURL("setCurrentDateTime()","");
				tablelayout.removeAllViews();
				startRouteView((JSONObject) (new FetchData().getXMLdataFromurl(url)));
			}
		});

/*********************************************************************************************************************
 * 					EVENT FOR THE NEXT BUTTON.  																	 *
 *********************************************************************************************************************/			
		next.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				url = MainActivity.getURL(nexttime,nextdate);
				tablelayout.removeAllViews();	
					startRouteView((JSONObject) (new FetchData().getXMLdataFromurl(url)));
			}
		});
		//This is to start the activity
		startRouteView((JSONObject) (new FetchData().getXMLdataFromurl(url)));
	}

/*********************************************************************************************************************
 * 														THIS IS TO BEGIN ROUTEVIEW.									 */		
public void startRouteView(JSONObject jobj){
		int TotalNumberofRoute = 0;
		String[] parts_spos = spos.split(","),parts_epos = epos.split(",");
		startpos.setText(Html.fromHtml("<b>"+parts_spos[0].toUpperCase()+"</b>"));
		endpos.setText(Html.fromHtml("<b>"+parts_epos[0].toUpperCase()+"</b>"));
		MainActivity.dialog.cancel();
		String start="null", end="null", start_error="null", end_error="null";
		
		try{start_error = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONObject("ERROR").toString();}catch(JSONException e1){}
		try{end_error = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONObject("ERROR").toString();}catch(JSONException e1){}
		try{start = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONObject("LOC").toString();}catch(JSONException e2){}		
		try{end = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONObject("LOC").toString();}catch(JSONException e3){}
		

		try{			
			List<String> listaddress = new ArrayList<String>();
			int locsize = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").length();
			for(int len=0; len<locsize; len++){
				String city = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city");
				if (city.equalsIgnoreCase("turku") || city.equalsIgnoreCase("kaarina") || city.equalsIgnoreCase("naantali") || city.equalsIgnoreCase("raisio")){
					if((jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("number")).equalsIgnoreCase(""))
						listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("name1") +","+
								jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city")
								);
					else
						listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("name1") + " "+
								jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("number") + ","+
								jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city")
								);
				}
			}					
			ShowlistAddress(listaddress, 3);	
			
			listaddress = new ArrayList<String>();
			locsize = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").length();
			for(int len=0; len<locsize; len++){
				String city = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city");
				if (city.equalsIgnoreCase("turku") || city.equalsIgnoreCase("kaarina") || city.equalsIgnoreCase("naantali") || city.equalsIgnoreCase("raisio")){
					if((jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("number")).equalsIgnoreCase(""))
						listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("name1") +","+
								jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city")
								);
					else
					listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("name1") + " "+
							jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("number") + ","+
							jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city")
							);
				}
			}
			ShowlistAddress(listaddress, 4);
		}
		catch(JSONException e4){}
		
		try{
			jarray = jobj.getJSONObject("MTRXML").getJSONArray("ROUTE");
			TotalNumberofRoute = jarray.length();
		}catch(JSONException r){		
			if(!start.equalsIgnoreCase("null")){
				if(!end_error.equalsIgnoreCase("null")){
					try {
						Toast toast = Toast.makeText(getApplicationContext(),AlertDialogueAdapter.nodestination + jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getString("key") + AlertDialogueAdapter.notinMatka, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
						toast.show();
						finish();
					}catch (JSONException ee){}
				}
				else{
					List<String> listaddress = new ArrayList<String>();
					try {
						int locsize = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").length();
						for(int len=0; len<locsize; len++){
							String city = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city");
							if (city.equalsIgnoreCase("turku") || city.equalsIgnoreCase("kaarina") || city.equalsIgnoreCase("naantali") || city.equalsIgnoreCase("raisio")){
								if((jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("number")).equalsIgnoreCase(""))
									listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("name1") +","+
											jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city")
											);
								else
								listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("name1") + " "+
										jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("number") + ","+
										jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(1).getJSONArray("LOC").getJSONObject(len).getString("city")
										);
							}
						}
					}catch (JSONException je){}
					ShowlistAddress(listaddress, 1);					
				}
			}
			
			if(!end.equalsIgnoreCase("null")){
				if(!start_error.equalsIgnoreCase("null")){
					try {
						Toast toast = Toast.makeText(getApplicationContext(), AlertDialogueAdapter.nostarting + jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getString("key") + AlertDialogueAdapter.notinMatka, Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
						toast.show();
						finish();
					}catch (JSONException je){}
				}
				else{
					List<String> listaddress = new ArrayList<String>();
					try {
						int locsize = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").length();
						for (int len = 0; len < locsize; len++) {
							String city = jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city");
							if (city.equalsIgnoreCase("turku") || city.equalsIgnoreCase("kaarina") || city.equalsIgnoreCase("naantali") || city.equalsIgnoreCase("raisio")) {
								if ((jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("number")).equalsIgnoreCase(""))
									listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("name1") + "," +
											jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city")
									);
								else
									listaddress.add(jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("name1") + " " +
											jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("number") + "," +
											jobj.getJSONObject("MTRXML").getJSONArray("GEOCODE").getJSONObject(0).getJSONArray("LOC").getJSONObject(len).getString("city")
									);
							}
						}
					}catch (JSONException je){}
					ShowlistAddress(listaddress, 0);
				}
			}
		}		
		
		/*********************************************************************************************************************
		 * 		This step will call the setup route data and finally to create layout. 										 *	
		*********************************************************************************************************************/		
		startforloop:
		for (int tableroute = 0; tableroute < TotalNumberofRoute; tableroute++) {
			if(status.equalsIgnoreCase("false")) break startforloop;
			routeAdaptor=setroute.setRouteList(jarray, tableroute);
			createLayout(tableroute, jarray);
		}	
}
/* 		This is the end of route list execution.																
*********************************************************************************************************************/	

/*********************************************************************************************************************
 * 		From here the creation of layout begins.  																	 *
 *********************************************************************************************************************/
public void createLayout(int rowIndex, final JSONArray jarray){

	if (DBAdapterAddress.exist(spos) == 0){DBAdapterAddress.addAddress(new Address(spos));}
	if (DBAdapterAddress.exist(epos) == 0){DBAdapterAddress.addAddress(new Address(epos));}
	try{routedetail.ListRouteDeail(jarray.getJSONObject(rowIndex));}
	catch(JSONException e){
//		Toast toast1 = Toast.makeText(RouteView.this, AlertDialogueAdapter.onlyway, Toast.LENGTH_SHORT);  
//		toast1.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
//		toast1.show();
		status = "false";
	}
	Toast toast = Toast.makeText(getApplicationContext(),AlertDialogueAdapter.toast_taplist, Toast.LENGTH_SHORT);  
			toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
			
	ArrayList<String> routeDetail = routedetail.getRouteDetail();
	final ArrayList<String> distanceTime = routedetail.getDistanceTime();
	
	final TableRow tablerow = new TableRow(this);	
	TableRow.LayoutParams tablerowparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f);
	tablerow.setBackgroundColor(Color.parseColor("#EAAB00"));
	tablerow.setId(rowIndex);
	
	LinearLayout depRow = new LinearLayout(this);
	depRow.setOrientation(LinearLayout.VERTICAL);
	/*********************************************************************************************************************
	 * 		This will set the time to start walking.			    											 	 	 */
			TextView deptimeText = new TextView(this);
			deptimeText.setGravity(Gravity.LEFT);
			deptimeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
			deptimeText.setText(Html.fromHtml("<b>"+new StringBuffer(routeAdaptor.getDepartTime()).insert (2, ":")+"</b>"));
	/*********************************************************************************************************************
	 * 		Add the start time to the view.    											 	 							 */
		depRow.addView(deptimeText);

		int busdepval = 0;
		for(int rdetail = 0; rdetail<routeDetail.size();rdetail++){
			if (routeDetail.get(rdetail).equalsIgnoreCase("L") &&  busdepval == 0 && (!routeDetail.get(rdetail+1).equalsIgnoreCase("W"))){
				/*********************************************************************************************************************
				 * 		This will set the bus time at which it will come to the bus stop.									 	 	 */
				TextView busdep = new TextView(this);
				busdep.setGravity(Gravity.LEFT);				
				Log.d("ERROR", routeDetail.get(rdetail+1));
				busdep.setText("("+new StringBuffer(routeDetail.get(rdetail+1)).insert (2, ":")+")");
				busdep.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

				/*********************************************************************************************************************
				 * 		Add the bus time to the view.						    											 	 	 */
				depRow.addView(busdep);
				busdepval=1;
			}
		}
		
		LinearLayout transportdetail = new LinearLayout(this);
		transportdetail.setOrientation(LinearLayout.VERTICAL);
		/*********************************************************************************************************************
		 * 		This will set the total time for the route.			    											 	 	 */
		TextView totaltime = new TextView(this);
		totaltime.setGravity(Gravity.CENTER);
		totaltime.setText(Html.fromHtml("<b>"+new StringBuffer(df.format(Double.parseDouble(routeAdaptor.getTotalTime())))+"MIN</b>"));
		totaltime.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		
		/*********************************************************************************************************************
		 * 		This will set the total distance for the route.		    											 	 	 */
		TextView totalduration = new TextView(this);
		totalduration.setGravity(Gravity.CENTER);
		totalduration.setText(Html.fromHtml("<b>"+(df.format(Double.parseDouble(routeAdaptor.getToatalDuration())/1000))+"KM</b>"));
		totalduration.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
		
		/*********************************************************************************************************************
		 * 		Add the total time and distance to the view.		    											 	 	 */
		transportdetail.addView(totaltime);
		transportdetail.addView(totalduration);		

		LinearLayout routeRow = new LinearLayout(this);
		int pos = routeAdaptor.getTransportList().size();
		
		List<String>	getroutelist = routeAdaptor.getTransportList();
			for (int row = 0; row < pos; row=row+3) {		
				LinearLayout itemtable = new LinearLayout(this);
				itemtable.setOrientation(LinearLayout.VERTICAL);					
					if ((getroutelist.get(row)).equalsIgnoreCase("W")){
						/*********************************************************************************************************************
						 * 		Add the Walking image to the route and finally add it to the view.									 	 	 */
						ImageView itemImage = new ImageView(this);
						itemImage.setImageResource(R.drawable.man);
						itemImage.layout(10, 10, 10, 10);
						itemImage.setMaxHeight(25);
						itemImage.setMaxWidth(25);
						itemImage.setPadding(5, 0, 5, 0);
						itemtable.addView(itemImage);
					}
					else {
						/*********************************************************************************************************************
						 * 		Add the bus image to the route and finally add it to the view.									 	 		 */
						ImageView itemImage = new ImageView(this);
						itemImage.setImageResource(R.drawable.bus1);
						itemImage.layout(10, 10, 10, 10);
						itemImage.setMaxHeight(25);
						itemImage.setMaxWidth(25);	
						itemtable.addView(itemImage);
						/*********************************************************************************************************************
						 * 		Add the bus number to the route and finally add it to the view.									 	 		 */
						TextView itemDesc = new TextView(this);
						itemDesc.setText(Html.fromHtml("<b>&nbsp;<font color='#f00303'>"+getroutelist.get(row+1)+"</font></b>"));						
						itemDesc.layout(10, 10, 10, 10);
						itemDesc.setGravity(Gravity.CENTER);
						itemDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);						
						itemtable.addView(itemDesc);
					}
				/*********************************************************************************************************************
				 * 		Add itemtable layout to the routeRow.															 	 		 */
				routeRow.addView(itemtable);
			}
			/*********************************************************************************************************************
			 * 		Set item table to the center of the screen and add the routeRow to the transport detail layout.	 	 		 */
			routeRow.setLayoutParams(tablerowparams);
			routeRow.setGravity(Gravity.CENTER);
			transportdetail.addView(routeRow);	
			
		LinearLayout ariRow = new LinearLayout(this);
		ariRow.setOrientation(LinearLayout.VERTICAL);
		/*********************************************************************************************************************
		 * 		Add arriving time to the route and finally to the view.											 	 		 */
			TextView aritimeText = new TextView(this);
			aritimeText.setGravity(Gravity.CENTER);
			aritimeText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
			aritimeText.setText(Html.fromHtml("<b>"+new StringBuffer(routeAdaptor.getArriveTime()).insert (2, ":")+"</b>"));
		ariRow.addView(aritimeText);

		int result = 0, index=0;
		for(int rdetail = 0; rdetail<routeDetail.size();rdetail++){
			if (routeDetail.get(rdetail).equalsIgnoreCase("L"))	
				result = 1;			
			if (routeDetail.get(rdetail).equalsIgnoreCase("W"))	
				index = rdetail;
		}
		
		if(result == 1){
			/*********************************************************************************************************************
			 * 		Add the bus arrival time to the route and finally add it to the view.									 	 		 */
				TextView busarival = new TextView(this);
				busarival.setGravity(Gravity.RIGHT);
				
				busarival.setText("("+new StringBuffer(routeDetail.get(index-2)).insert (2, ":")+")");
				busarival.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
				ariRow.addView(busarival);
			}
		/*********************************************************************************************************************
		 * 		Setting the position of elements in the route view.												 	 		 */
		depRow.setLayoutParams(tablerowparams);
		depRow.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		transportdetail.setLayoutParams(tablerowparams);
		transportdetail.setGravity(Gravity.CENTER);
		depRow.setLayoutParams(tablerowparams);
		ariRow.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);		
		
		/*********************************************************************************************************************
		 * 		Add the content of route view to the table row.													 	 		 */
		tablerow.addView(depRow);
		tablerow.addView(transportdetail);
		tablerow.addView(ariRow);		
		
		/*********************************************************************************************************************
		 * 		Adding click listener to the table row.									 	 		 */
		 tablerow.setOnClickListener(new View.OnClickListener() {
	         public void onClick(View view) {
	        	  try{
		        	  routedetail.ListRouteDeail(jarray.getJSONObject(view.getId()));
		        	  Intent nextScreen = new Intent(getApplicationContext(),RouteDetailView.class);
						// Sending data to another Activity
			        	  nextScreen.putStringArrayListExtra("routedetail", routedetail.getRouteDetail());
			        	  nextScreen.putStringArrayListExtra("stopslist", routedetail.getBusstopslist());
			        	  nextScreen.putStringArrayListExtra("getdistancetime", distanceTime);
			        	  nextScreen.putExtra("coordlist", routedetail.getCoord());
			        	  nextScreen.putExtra("spos", getIntent().getStringExtra("spos"));
			        	  nextScreen.putExtra("epos", getIntent().getStringExtra("epos"));
						startActivity(nextScreen);
	        	  }
	        	  catch(JSONException e){
	        		  Intent nextScreen = new Intent(getApplicationContext(),MapActivity.class);
		        	  nextScreen.putExtra("onlywalk", "walk");
		        	  nextScreen.putExtra("spos", getIntent().getStringExtra("spos"));
		        	  nextScreen.putExtra("epos", getIntent().getStringExtra("epos"));
					  startActivity(nextScreen);
	        	  }
	         	}
	         });	
	/*********************************************************************************************************************
	 * 		Finally set the layout params to the table layout and add the table row to the table layout.	 	 		 */
	 TableLayout.LayoutParams rowlayoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT,1f);
	 rowlayoutParams.setMargins(10,10,10,15);
	 tablerow.setLayoutParams(rowlayoutParams);
	 tablelayout.addView(tablerow);

	 if (timestatus.equalsIgnoreCase("true")){
		 previousdate = routeAdaptor.getDapartureDate();
		 timestatus = "false";
	 }
	 nexttime = routeAdaptor.getDepartTime();
	 nextdate = routeAdaptor.getDapartureDate();
	}

public void ShowlistAddress(List<String> listaddress, final int whichfield){
	
	
	DBAdapterAddress.addAddressList(listaddress);
	
	AlertDialog.Builder builderSingle = new AlertDialog.Builder( RouteView.this);
    builderSingle.setIcon(R.drawable.ic_launcher);
    
    if(whichfield == 4 || whichfield == 0)  builderSingle.setTitle(AlertDialogueAdapter.select_startingadd);
    if(whichfield == 1 || whichfield == 3) builderSingle.setTitle(AlertDialogueAdapter.select_endadd);
    
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RouteView.this, android.R.layout.select_dialog_singlechoice);
    arrayAdapter.addAll(listaddress);
    if(arrayAdapter.getCount() == 0) {
    	arrayAdapter.add("No Address :(");
    }
    	
    builderSingle.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	if(arrayAdapter.getItem(which).equalsIgnoreCase("No Address :("))
                		if(whichfield == 4) dialog.dismiss();
                		else finish();
                	else{
	                    if(whichfield == 1){
	                    	MainActivity.totext.setText(arrayAdapter.getItem(which));
	                    	finish();
	                    }
	                    if(whichfield == 0){
	                    	MainActivity.fromtext.setText(arrayAdapter.getItem(which));
	                    	finish();
	                    }
	                    if(whichfield == 3){
	                    	MainActivity.totext.setText(arrayAdapter.getItem(which));
	                    	dialog.dismiss();
	                    	finish();
	                    }
	                    if(whichfield == 4){
	                    	MainActivity.fromtext.setText(arrayAdapter.getItem(which));
	                    	dialog.dismiss();
	                    }
                	}
                }
            });
    builderSingle.show();
 }
}
/* 		This is the end of layout creation.																
*********************************************************************************************************************/	