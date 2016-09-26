package com.turku.route;

import java.util.ArrayList;

import com.turku.historydatabase.DBAdapterLanguage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RouteDetail {
	ArrayList<String> routeDetail, busstopslist;
	private ArrayList<Double> coord;
    ArrayList<String> distanceTime;
	
	public RouteDetail() { }	
	public void ListRouteDeail(JSONObject route) {
		this.coord = new ArrayList<Double>();
		this.routeDetail  = new ArrayList<String>();
		this.distanceTime = new ArrayList<String>();
		this.busstopslist = new ArrayList<String>();
		String status = "null";
		try{
			route.getJSONArray("LINE").toString();
			lineRoute(route, "true");
		}
		catch(JSONException e){	
			try {
				status = route.getJSONObject("LINE").toString();				
			}catch (JSONException je){}
			if(!status.equalsIgnoreCase("null")){
			lineRoute(route, "false");
			}
		}
		if (status.equalsIgnoreCase("null")){
			try{
				route.getJSONArray("WALK");
			}
			catch(JSONException w){
				walkingRoute(route, "false");	
			}
		}
	}	
	/*********************************************************************************************************************
	 * 		This will check if the only way to the destination is by walking.  											 */
	public void walkingRoute(JSONObject route, String status){
	
		this.coord.add(0.0);
		this.routeDetail.add("W");
		this.distanceTime.add("W");
		try {
			this.distanceTime.add(route.getJSONObject("WALK").getJSONObject("LENGTH").getString("dist"));
			this.distanceTime.add(route.getJSONObject("WALK").getJSONObject("LENGTH").getString("time"));
		}catch (JSONException je){}
		try {
			JSONArray maplocarray = route.getJSONObject("WALK").getJSONArray("MAPLOC");
			for (int val = 0; val < maplocarray.length(); val++) {
				this.coord.add(Double.parseDouble(maplocarray.getJSONObject(val).getString("y")));
				this.coord.add(Double.parseDouble(maplocarray.getJSONObject(val).getString("x")));
				try {
					if (getStreetName(maplocarray.getJSONObject(val)).length() > 0) {
						this.routeDetail.add(maplocarray.getJSONObject(val).getJSONObject("DEPARTURE").getString("time"));
						this.routeDetail.add(getStreetName(maplocarray.getJSONObject(val)));
					}
				} catch (JSONException e) {
					//Log.d("TAG", "No name for this");
				}
			}
		}catch (JSONException je){}
//		}catch(JSONException e){
////			Toast toast = Toast.makeText(MainActivity.this,AlertDialogueAdapter.nostarting + jobj.getJSONArray("GEOCODE").getJSONObject(0).getString("key") + AlertDialogueAdapter.notinMatka, Toast.LENGTH_SHORT);  
////			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
////			toast.show();
//		}
	}
	/*********************************************************************************************************************
	 * 							If there is only one bus to the destination.  											 */		
	public void lineRoute(JSONObject route, String status){
		int TotalNumberofTrnsport=0, walk=0, bus = 0;
		String walkstatus = "true", busstatus = "true", maploc="true";
		
		if (status.equalsIgnoreCase("false")){
			try {
				TotalNumberofTrnsport=route.getJSONArray("WALK").length() +1;
			}catch (JSONException je){}
		}
		else if (status.equalsIgnoreCase("true")){
			try {
				TotalNumberofTrnsport=route.getJSONArray("WALK").length() + route.getJSONArray("LINE").length();				
			}catch (JSONException je){}	
		}
//		Log.d("TAG", "No of transport :" + TotalNumberofTrnsport);
		for (int val = 0; val< TotalNumberofTrnsport; val++){
			int walk_len = 0, line_len = 0;
			try {
				walk_len = route.getJSONArray("WALK").length();
				line_len = route.getJSONArray("LINE").length();
			}catch (JSONException je){}
			if (walk < walk_len && walkstatus.equalsIgnoreCase("true")){
				try{
					
					JSONObject jobj = route.getJSONArray("WALK").getJSONObject(walk);
					String result = jobj.keys().toString().contains("MAPLOC")+"";
					if(result.equalsIgnoreCase("false")){
						this.routeDetail.add("W");
						this.routeDetail.add("0000");
						this.routeDetail.add("NO");
						this.distanceTime.add("W");
						this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("dist"));
						this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("time"));
						walk++;
						walkstatus = "false";
						busstatus = "true";
					}
				}
				catch(JSONException map){
					//If exeception occurs dont do anything but goto next line.
				 }
			}

			if (walkstatus.equalsIgnoreCase("true") && walk < walk_len && maploc.equalsIgnoreCase("true")){
				try{
					setwalkingdata(route.getJSONArray("WALK").getJSONObject(walk).getJSONArray("MAPLOC"));
					this.distanceTime.add("W");
					this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("dist"));
					this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("time"));
					walk++;
					walkstatus = "false";
					busstatus = "true";
				}
				catch(JSONException e){
					try {
						setwalkingdata(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("MAPLOC"));
						this.distanceTime.add("W");
						this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("dist"));
						this.distanceTime.add(route.getJSONArray("WALK").getJSONObject(walk).getJSONObject("LENGTH").getString("time"));
						walk++;
						walkstatus = "false";
						busstatus = "true";
					}catch (JSONException je){}
				}
			}
			/*********************************************************************************************************************
			 * 		If there is only one bus in the route.    															 	 	 */
			else if (status.equalsIgnoreCase("false") && busstatus.equalsIgnoreCase("true") && bus < 1){
				try {
					setbusdata(route.getJSONObject("LINE"));
					bus++;
					walkstatus = "true";
					busstatus = "false";
				}catch (JSONException je){}
			}
			/*********************************************************************************************************************
			 * 		If there is a multiple bus line in the route. 		    											 	 	 */									
			else if(status.equalsIgnoreCase("true") && bus < line_len &&  busstatus.equalsIgnoreCase("true")){
				try {
					int stopsize = route.getJSONArray("LINE").getJSONObject(bus).getJSONArray("STOP").length();
					if (busstatus.equalsIgnoreCase("true") && (bus + 1) < route.getJSONArray("LINE").length() &&
							route.getJSONArray("LINE").getJSONObject(bus).getJSONArray("STOP").getJSONObject(stopsize - 1).getString("code").equalsIgnoreCase(
									route.getJSONArray("LINE").getJSONObject(bus + 1).getJSONArray("STOP").getJSONObject(0).getString("code"))) {
						setbusdata(route.getJSONArray("LINE").getJSONObject(bus));
						bus++;
					}
					/*********************************************************************************************************************
					 * 		If there is a need of walking while changing the bus.    											 	 	 */
					else if (busstatus.equalsIgnoreCase("true") && bus < route.getJSONArray("LINE").length()) {
						setbusdata(route.getJSONArray("LINE").getJSONObject(bus));
						bus++;
						busstatus = "false";
						walkstatus = "true";
					}
				}catch (JSONException je){}
			}
		}	
	}
	/*********************************************************************************************************************
	 * 		This will set the walking data from the route that contains one bus route.  								 */
	public void setwalkingdata(JSONArray route){
		this.coord.add(0.0);
		this.routeDetail.add("W");
		for (int val = 0; val < route.length(); val++) {
			try {
				this.coord.add(Double.parseDouble(route.getJSONObject(val).getString("y")));
				this.coord.add(Double.parseDouble(route.getJSONObject(val).getString("x")));
			}catch (JSONException je){}
			try {
				if (getStreetName(route.getJSONObject(val)).length() > 0) {
					this.routeDetail.add(route.getJSONObject(val).getJSONObject("DEPARTURE").getString("time"));
					this.routeDetail.add(getStreetName(route.getJSONObject(val)));
				}
			} catch (JSONException e) {
				this.routeDetail.add("0000");
				this.routeDetail.add("NO");
			}
		}
	}
	/*********************************************************************************************************************
	 * 		This will check if the only way to the destination is by walking.  											 */	
	public void setwalkingdata(JSONObject route){
		this.coord.add(0.0);
		this.routeDetail.add("W");
		try{
			this.coord.add(Double.parseDouble(route.getString("y")));
			this.coord.add(Double.parseDouble(route.getString("x")));
			if (getStreetName(route).length() > 0) {
				this.routeDetail.add(route.getJSONObject("DEPARTURE").getString("time"));
				this.routeDetail.add(getStreetName(route));
			}
		}
		catch(JSONException e){
			this.routeDetail.add("0000");
			this.routeDetail.add("NO");
		}
	}
	/*********************************************************************************************************************
	 * 		This will set the line to the list of route detail and coordinates. 										 */
	public void setbusdata(JSONObject route){
		this.coord.add(1.0);
		this.routeDetail.add("L");
//		this.routeDetail.add(route.getString("id"));
//		this.routeDetail.add(route.getString("code"));
		this.distanceTime.add("L");
		try {
			this.distanceTime.add(route.getString("code"));
		}catch (JSONException je){}
		int stop_len=0;
		try {
			stop_len = route.getJSONArray("STOP").length();
		}catch (JSONException je){}
		for(int val = 0; val < stop_len;val++){
			try {
				this.coord.add(Double.parseDouble(route.getJSONArray("STOP").getJSONObject(val).getString("y")));
				this.coord.add(Double.parseDouble(route.getJSONArray("STOP").getJSONObject(val).getString("x")));
			}catch (JSONException je){}
			try {
				if (getStreetName(route.getJSONArray("STOP").getJSONObject(val)).length() > 0) {
					if (val == 0) {
						this.distanceTime.add(route.getJSONArray("STOP").getJSONObject(val).getString("code"));
						this.distanceTime.add(getStreetName(route.getJSONArray("STOP").getJSONObject(val)));
					}

					this.routeDetail.add(route.getJSONArray("STOP").getJSONObject(val).getJSONObject("DEPARTURE").getString("time"));
					this.routeDetail.add(getStreetName(route.getJSONArray("STOP").getJSONObject(val)));
					this.busstopslist.add(getStreetName(route.getJSONArray("STOP").getJSONObject(val)));
				}
			}
			catch(JSONException e){
				//Log.d("TAG", "No name for this");
			}
		}
		try {
			this.distanceTime.add(route.getJSONObject("LENGTH").getString("dist"));
			this.distanceTime.add(route.getJSONObject("LENGTH").getString("time"));
		}catch (JSONException je){}
	}
	public ArrayList<Double> getCoord() {
		return coord;
	}
	public void setCoord(ArrayList<Double> coord) {
		this.coord = coord;
	}
	public ArrayList<String> getRouteDetail() {
		return routeDetail;
	}
	public void setRouteDetail(ArrayList<String> routeDetail) {
		this.routeDetail = routeDetail;
	}
	public ArrayList<String> getBusstopslist() {
		return busstopslist;
	}
	public void setBusstopslist(ArrayList<String> busstopslist) {
		this.busstopslist = busstopslist;
	}
	public ArrayList<String> getDistanceTime() {
		return distanceTime;
	}
	public void distanceTime(ArrayList<String> distanceTime) {
		this.distanceTime = distanceTime;
	}
	private String getStreetName(JSONObject name){
		int index = 0;
		if(DBAdapterLanguage.getAllData().get(0).equalsIgnoreCase("SWE")) index = 1;
		try {
			return name.getJSONArray("NAME").getJSONObject(index).getString("val");
		}catch (JSONException e){
			try {
				return name.getJSONObject("NAME").getString("val");
			}catch (JSONException je){return "";}
		}

	}
}
