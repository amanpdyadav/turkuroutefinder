package com.turku.route;

import org.json.JSONArray;

import org.json.JSONException;
import java.util.List;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class SetRoute{
	
	RouteAdaptor routeAdaptor;
	JSONArray jarray;
	Integer tableroute;
	DecimalFormat df = new DecimalFormat("#.#");
	
public SetRoute(){}

public SetRoute(JSONArray jarray, Integer tableroute){	
	this.jarray = jarray;
	this.tableroute = tableroute;
}
/*********************************************************************************************************************
 * 		This is class is responsible for creating the list of data for calculating the route.						 *
*********************************************************************************************************************/
public RouteAdaptor setRouteList(JSONArray jarray, Integer tableroute){		
	
	int TotalNumberofTrnsport=0,line = 0, walk = 0;
	String status = "null", walkstatus = "true" , busstatus = "true";	
	List<String> transportList = new ArrayList<String>();
	routeAdaptor = new RouteAdaptor();

	try {
		routeAdaptor.setDepartTime(jarray.getJSONObject(tableroute).getJSONArray("POINT").getJSONObject(0).getJSONObject("DEPARTURE").getString("time"));
		routeAdaptor.setArriveTime(jarray.getJSONObject(tableroute).getJSONArray("POINT").getJSONObject(1).getJSONObject("DEPARTURE").getString("time"));
		routeAdaptor.setDapartureDate(jarray.getJSONObject(tableroute).getJSONArray("POINT").getJSONObject(0).getJSONObject("DEPARTURE").getString("date"));
		routeAdaptor.setArrivalDate(jarray.getJSONObject(tableroute).getJSONArray("POINT").getJSONObject(1).getJSONObject("DEPARTURE").getString("date"));
		routeAdaptor.setToatalDuration(jarray.getJSONObject(tableroute).getJSONObject("LENGTH").getString("dist"));
		routeAdaptor.setTotalTime(jarray.getJSONObject(tableroute).getJSONObject("LENGTH").getString("time"));
	}catch (JSONException je){}
	
	/*********************************************************************************************************************
	 * 		This will check if the only way to the destination is by walking.  											 */
	try{
		TotalNumberofTrnsport = jarray.getJSONObject(tableroute).getJSONArray("WALK").length();
	  }
	 catch(JSONException e){		  
  		transportList.add("W");
		 try {
			 transportList.add(df.format((Double.parseDouble(jarray.getJSONObject(tableroute).getJSONObject("WALK").getJSONObject("LENGTH").getString("dist"))) / 1000) + "KM");
			 transportList.add((int) Double.parseDouble(jarray.getJSONObject(tableroute).getJSONObject("WALK").getJSONObject("LENGTH").getString("time")) + "MIN");
			 status = jarray.getJSONObject(tableroute).getJSONObject("WALK").getJSONObject("LENGTH").getString("time");
		 }catch (JSONException je){}
	  }
	/* 		The end of setting walking route only by means of waliking
	*********************************************************************************************************************/
	/*********************************************************************************************************************
	 * 		The beginning of setting transportation number;  											 				*/
	if (status.equalsIgnoreCase("null")){
		try{
				TotalNumberofTrnsport =  TotalNumberofTrnsport +  jarray.getJSONObject(tableroute).getJSONArray("LINE").length();
		}
		catch(JSONException e){
				TotalNumberofTrnsport =  TotalNumberofTrnsport + 1;
		}
	}
	/* The end of setting transportation number.
	*********************************************************************************************************************/
	
	/*********************************************************************************************************************
	 *This will set the route if there are multiple way of reaching the destination meaning includes both bus and walking.
	 *					THIS IS THE MAIN PLACE TO BE CAREFUL															*/
	if (status.equalsIgnoreCase("null")){
	 for (int addlist = 0; addlist < TotalNumberofTrnsport; addlist++){
		 int walk_len=0;
		 try {
			 walk_len =jarray.getJSONObject(tableroute).getJSONArray("WALK").length();
		 }catch (JSONException je){}
		if (walkstatus.equalsIgnoreCase("true") && walk < walk_len){
			try {
				transportList.add("W");
				transportList.add(df.format((Double.parseDouble(jarray.getJSONObject(tableroute).getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("dist"))) / 1000) + "KM");
				transportList.add((int) Double.parseDouble(jarray.getJSONObject(tableroute).getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("time")) + "MIN");
				walk++;
				walkstatus = "flase";
				busstatus = "true";
			}catch (JSONException je){}
		}

		/*********************************************************************************************************************
		 * 		If the route contains only one bus to our destination.	  										 			 */
		try{
			jarray.getJSONObject(tableroute).getJSONObject("LINE");//This is just to make sure the error occurs at first place.
			if(busstatus.equalsIgnoreCase("true") && line == 0){
			transportList.add("L");
			transportList.add(jarray.getJSONObject(tableroute).getJSONObject("LINE").getString("code"));
			transportList.add((int)Double.parseDouble(jarray.getJSONObject(tableroute).getJSONObject("LINE").getJSONObject("LENGTH").getString("time"))+"MIN");
			line++;
			walkstatus = "true";
			}				
		}		

		/*********************************************************************************************************************
		 * 		If the route contains only multiple bus to our destination  											 	 */
		catch(JSONException e){
			/*********************************************************************************************************************
			 * 		If there is a no need of walking while changing the bus.    											 	 */
			int line_len=0;
			try {
				line_len = jarray.getJSONObject(tableroute).getJSONArray("LINE").length();
			}catch (JSONException je){}
			if(line < line_len){
				try {
					int stopsize = jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getJSONArray("STOP").length();
					if (busstatus.equalsIgnoreCase("true") && (line + 1) < jarray.getJSONObject(tableroute).getJSONArray("LINE").length() &&
							jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getJSONArray("STOP").getJSONObject(stopsize - 1).getString("code").equalsIgnoreCase(
									jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line + 1).getJSONArray("STOP").getJSONObject(0).getString("code"))) {
//					Log.d("TAG", "Passed similarity");
						transportList.add("L");
						transportList.add(jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getString("code"));
						transportList.add(jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getJSONObject("LENGTH").getString("time").charAt(0) + "MIN");
						line++;
					}
					/*********************************************************************************************************************
					 * 		If there is a need of walking while changing the bus.    											 	 */
					else if (busstatus.equalsIgnoreCase("true") && line < jarray.getJSONObject(tableroute).getJSONArray("LINE").length()) {
//					Log.d("TAG", "Failed similarity");
						transportList.add("L");
						transportList.add(jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getString("code"));
						transportList.add(jarray.getJSONObject(tableroute).getJSONArray("LINE").getJSONObject(line).getJSONObject("LENGTH").getString("time").charAt(0) + "MIN");
						line++;
						busstatus = "false";
						walkstatus = "true";
					}
				}catch (JSONException je){}
			}	
		 }
	  }
	}
	/*********************************************************************************************************************
	 * 		Finally this line will add all the list of routes info into the arraylist.									 *
	 *********************************************************************************************************************/
	routeAdaptor.setTransportList(transportList);	
	return routeAdaptor;
 }	
}
