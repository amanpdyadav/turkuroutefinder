package com.turku.route;

import android.util.Log;
import android.text.Html;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.view.Gravity;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Color;
import java.text.DecimalFormat;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.LinearLayout;
import android.support.v7.app.ActionBar;
import android.view.View.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;

public class RouteDetailView extends ActionBarActivity{
	private LinearLayout timeplace, detailRow=null, routeRow;
	public TableLayout routedetaillayout;
	private TextView startpos, endpos;	
	private ImageView back, mapbtn;
	ArrayList<String> distTime, stoplist = new ArrayList<String>();
	private TextView title;

	DecimalFormat df = new DecimalFormat("#.#");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routedetail);
				
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EAAB00")));
        View cView = getLayoutInflater().inflate(R.layout.routedetailview_title, null);
		ActionBar ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.setDisplayUseLogoEnabled(false);
		ab.setDisplayHomeAsUpEnabled(false);
		ab.setDisplayShowCustomEnabled(true);
        ab.setCustomView(cView);

        title = (TextView)findViewById(R.id.mapscreen_title);
        title.setText(AlertDialogueAdapter.app_name_route_detail);
        
		routedetaillayout = (TableLayout) findViewById(R.id.RouteDetail);
		mapbtn = (ImageView) findViewById(R.id.routedetailview_map);
		back = (ImageView) findViewById(R.id.routedetailview_back);
		startpos = (TextView) findViewById(R.id.start_pos_detail);
		endpos = (TextView) findViewById(R.id.end_pos_detail);
		
		@SuppressWarnings("unchecked")
		final ArrayList<Double> coordarraylist = (ArrayList<Double>) getIntent().getSerializableExtra("coordlist");
		final ArrayList<String> routearraylist = getIntent().getStringArrayListExtra("routedetail");		
		distTime = getIntent().getStringArrayListExtra("getdistancetime");

		startpos.setText(Html.fromHtml("<b>"+getIntent().getStringExtra("spos").toUpperCase()+"</b>"));
		endpos.setText(Html.fromHtml("<b>"+getIntent().getStringExtra("epos").toUpperCase()+"</b>"));
					
		createLayout(routearraylist);
		
		mapbtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				Intent nextScreen = new Intent(getApplicationContext(),MapActivity.class);
	        	  nextScreen.putExtra("coordlist",coordarraylist);
	        	  nextScreen.putExtra("stopslist",getIntent().getStringArrayListExtra("stopslist"));
	        	  nextScreen.putExtra("spos", getIntent().getStringExtra("spos"));
	        	  nextScreen.putExtra("epos", getIntent().getStringExtra("epos"));
				startActivity(nextScreen);				
			}
		});
		
		back.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				finish();				
			}
		});
	}	
	
	public void createLayout(ArrayList<String> routelist){	
		Toast toast = Toast.makeText(getApplicationContext(),AlertDialogueAdapter.toast_taplist, Toast.LENGTH_SHORT);  
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
		LinearLayout.LayoutParams tablerowparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
		int size = 0, pos=0, count = 0, counter = 1;
		for (int i = 0; i < distTime.size(); i++ ){
				if(distTime.get(i).equalsIgnoreCase("W") || distTime.get(i).equalsIgnoreCase("L")) size++;		
			}		
	    
		for (int row = 0; row < size; row++) {	
			routeRow = new LinearLayout(this);
			routeRow.setOrientation(LinearLayout.HORIZONTAL);
			routeRow.setBackgroundColor(Color.parseColor("#EAAB00"));
			routeRow.setId(row+1);
			
			LinearLayout linearRow = new LinearLayout(this);
			linearRow.setOrientation(LinearLayout.HORIZONTAL);

			if(distTime.get(pos).equalsIgnoreCase("W")){					
				LinearLayout walkingrow = new LinearLayout(this);
				ImageView itemImage = new ImageView(this);
				itemImage.setImageResource(R.drawable.man);
				itemImage.layout(10, 10, 10, 10);	
				itemImage.setMaxHeight(25);
				itemImage.setMaxWidth(25);	
				itemImage.setPadding(0, 15, 0, 0);
				walkingrow.addView(itemImage);			
				

				LinearLayout walklayout = new LinearLayout(this);
				TextView walk = new TextView(this);
				walk.setText(Html.fromHtml("<b>"+AlertDialogueAdapter.walk+"</b>"));
				walk.setGravity(Gravity.CENTER);
				walklayout.addView(walk);
				
				LinearLayout walkinfo = new LinearLayout(this);
				walkinfo.setOrientation(LinearLayout.VERTICAL);
				TextView walkDist = new TextView(this);
				TextView walkTime = new TextView(this);
				
				walkDist.setText(Html.fromHtml("<b>"+distFormat(distTime.get(pos+1))+"</b>"));
				walkDist.setGravity(Gravity.RIGHT);
				walkinfo.addView(walkDist);
				
				walkTime.setText(Html.fromHtml("<b>"+timeFormat(distTime.get(pos+2))+"</b>"));
				walkTime.setGravity(Gravity.RIGHT);
				walkinfo.addView(walkTime);				
				
				walkingrow.setLayoutParams(tablerowparams);
				walkingrow.setGravity(Gravity.LEFT);

				walklayout.setLayoutParams(tablerowparams);
				walklayout.setGravity(Gravity.CENTER);
				
				walkinfo.setLayoutParams(tablerowparams);
				walkinfo.setGravity(Gravity.RIGHT);
				
				routeRow.addView(walkingrow);
				routeRow.addView(walklayout);
				routeRow.addView(walkinfo);
				pos = pos + 3;
			}	
			else if(distTime.get(pos).equalsIgnoreCase("L")){			
				LinearLayout businfo = new LinearLayout(this);				
				businfo.setOrientation(LinearLayout.VERTICAL);
				
				ImageView busImage = new ImageView(this);
				busImage.setImageResource(R.drawable.bus);
				busImage.setMaxHeight(25);
				busImage.setMaxWidth(25);
				LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(80, 50,1.0f);
				imgParams.gravity = Gravity.LEFT|Gravity.CENTER_VERTICAL;
				busImage.setLayoutParams(imgParams);
				businfo.addView(busImage);		
				
				TextView busNumber = new TextView(this);
				busNumber.setText(Html.fromHtml("<b><font color='#f00303' size='40'>"+distTime.get(pos+1)+"</font></b>"));
				busNumber.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				busNumber.setPadding(40, 0, 0, 10);
				businfo.addView(busNumber);				
				
				LinearLayout stop = new LinearLayout(this);				
				stop.setOrientation(LinearLayout.VERTICAL);
				
				ImageView stopImage = new ImageView(this);
				stopImage.setImageResource(R.drawable.busstop);
				stopImage.setMaxHeight(10);
				stopImage.setMaxWidth(25);
				stop.addView(stopImage);		

				TextView stopNumber = new TextView(this);
				stopNumber.setText(Html.fromHtml("<b><font color='#1200D0'>"+distTime.get(pos+2)+"</font></b>"));
				stopNumber.setGravity(Gravity.CENTER);
				stop.addView(stopNumber);
				
				TextView stopName = new TextView(this);
				stopName.setText(Html.fromHtml("<b><font color='#1200D0'>"+distTime.get(pos+3)+"</font></b>"));
				stopName.setGravity(Gravity.CENTER);
				stop.addView(stopName);	

				LinearLayout routeinfo = new LinearLayout(this);
				routeinfo.setOrientation(LinearLayout.VERTICAL);
				
				TextView routeDist = new TextView(this);
				TextView routeTime = new TextView(this);
				
				routeDist.setText(Html.fromHtml("<b>"+distFormat(distTime.get(pos+4))+"</b>"));
				routeDist.setGravity(Gravity.RIGHT);
				routeinfo.addView(routeDist);
				
				routeTime.setText(Html.fromHtml("<b>"+timeFormat(distTime.get(pos+5))+"</b>"));
				routeTime.setGravity(Gravity.RIGHT);
				routeinfo.addView(routeTime);				

				businfo.setLayoutParams(tablerowparams);
				businfo.setGravity(Gravity.LEFT);

				stop.setLayoutParams(tablerowparams);				
				stop.setGravity(Gravity.CENTER);				

				routeinfo.setLayoutParams(tablerowparams);
				routeinfo.setGravity(Gravity.RIGHT);
				
				routeRow.addView(businfo);
				routeRow.addView(stop);
				routeRow.addView(routeinfo);				
				pos = pos + 6;
			}		
			
			timeplace = new LinearLayout(this);
			timeplace.setOrientation(LinearLayout.VERTICAL);
			int status = 1;
			forloop: 
				for (int rownum = count; rownum < routelist.size(); rownum = rownum + 2) {
					detailRow = new LinearLayout(this);
					detailRow.setOrientation(LinearLayout.HORIZONTAL);
					detailRow.setId(counter-1);
					
					TextView routeTime = new TextView(this);
					TextView routeAddress = new TextView(this);

				if (rownum == 0) rownum++;
				
				if ((routelist.get(rownum).equalsIgnoreCase("W") || routelist.get(rownum).equalsIgnoreCase("L")) && counter != size) {
					 count=count+3;
					 counter++;
					break forloop;
				}

				stoplist.add(routelist.get(rownum + 1));
				if(!routelist.get(rownum).equalsIgnoreCase("0000")){
					routeTime.setText(new StringBuffer(routelist.get(rownum)).insert(2, ":"));
					routeTime.setLayoutParams(tablerowparams);
					routeTime.setGravity(Gravity.LEFT);
					
					routeAddress.setText(routelist.get(rownum + 1));
					routeAddress.setLayoutParams(tablerowparams);
					routeAddress.setGravity(Gravity.RIGHT);
//					routeAddress.setPadding(350, 0, 0, 0);
//					Log.d("stop", routelist.get(rownum+1));
					
					detailRow.addView(routeTime);
					detailRow.addView(routeAddress);
					if(status == 1){
						detailRow.setBackgroundColor(Color.parseColor("#FFD800"));
						status  = 0;
					}
					else status = 1;				
				}
				timeplace.addView(detailRow); count = rownum;	
				
				routeRow.setOnClickListener(new View.OnClickListener() {
			         public void onClick(View view) {
			        	 if(routedetaillayout.getChildAt(view.getId()*2-1).getVisibility() != View.GONE){
			        		 routedetaillayout.getChildAt(view.getId()*2-1).setVisibility(View.GONE);
			        	 }
			        	 else{routedetaillayout.getChildAt(view.getId()*2-1).setVisibility(View.VISIBLE);}
			        	}
			         });
			}	
			TableLayout.LayoutParams rowlayoutParams=new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT,1f);
			rowlayoutParams.setMargins(10,10,10,15);
			
			routeRow.setLayoutParams(rowlayoutParams);
			routedetaillayout.addView(routeRow);				

			timeplace.setLayoutParams(rowlayoutParams);
			routedetaillayout.addView(timeplace);
			timeplace.setVisibility(View.GONE);
		}		
}
	
	public String distFormat(String dist){
		double num=0.0;
		try{num = Double.parseDouble(dist);}
		catch(NumberFormatException n){
			Toast.makeText(getApplicationContext(), "Bus is Unknown....!!!", Toast.LENGTH_LONG).show();
			Intent nextScreen = new Intent(getApplicationContext(),MapActivity.class);
        	  nextScreen.putExtra("coordlist",getIntent().getSerializableExtra("coordlist"));
        	  nextScreen.putExtra("spos", getIntent().getStringExtra("spos"));
        	  nextScreen.putExtra("epos", getIntent().getStringExtra("epos"));
        	  finish();
			startActivity(nextScreen);	
		}
		if (num > 1000) return (df.format(num/1000.0)+"KM");
		else return (df.format((int)num)+"M");
	}
	
	public String timeFormat(String dist){		
		return (df.format(Double.parseDouble(dist))+"MIN");
	}
}
