package com.turku.route;

import java.util.List;

public class RouteAdaptor {
    private String totalTime;
	private String departTime;
    private String arriveTime;
	private String dapartureDate;
    private String arrivalDate;
    private String toatalDuration;
    private List<String> routeDetail;
    private List<String> distanceTime;
    private List<String> transportList;
    private List<Double> coordinateList;
    
	public RouteAdaptor(){
    }
    public RouteAdaptor(String dpartTime, String arriveTime, String duration, String rIcon, String rDesc) {
		super();
    }
	public String getDepartTime() {
		return departTime;
	}
	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getDapartureDate() {
		return dapartureDate;
	}
	public void setDapartureDate(String dapartureDate) {
		this.dapartureDate = dapartureDate;
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public String getToatalDuration() {
		return toatalDuration;
	}
	public void setToatalDuration(String toatalDuration) {
		this.toatalDuration = toatalDuration;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public List<String> getTransportList() {
		return transportList;
	}
	public List<String> getDistanceTime() {
		return distanceTime;
	}
	public void setDistanceTime(List<String> distanceTime) {
		this.distanceTime = distanceTime;
	}
	public void setTransportList(List<String> transportList) {
		this.transportList = transportList;
	}
	public List<String> getRouteDetail() {
		return routeDetail;
	}
	public void setRouteDetail(List<String> routeDetail) {
		this.routeDetail = routeDetail;
	}
	public List<Double> getCoordinateList() {
		return coordinateList;
	}
	public void setCoordinateList(List<Double> coordinateList) {
		this.coordinateList = coordinateList;
	}   
}
