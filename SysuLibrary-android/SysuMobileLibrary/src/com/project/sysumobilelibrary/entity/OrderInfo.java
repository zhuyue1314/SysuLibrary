package com.project.sysumobilelibrary.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderInfo {

	private String description;
	private String state;
	private String due_date;
	private String due_time;
	private String sub_library;
	private String place;
	private String book_num;
	private String order_num;
	private String bar_code;
	private Boolean can_order;
	
	ArrayList<String> PICKUP = new ArrayList<String>();
	
	public void getFromJSONObject(JSONObject jsonObject) throws JSONException{
		description = jsonObject.getString("description");
		state = jsonObject.getString("state");
		due_date = jsonObject.getString("due_date");
		due_time = jsonObject.getString("due_time");
		sub_library = jsonObject.getString("sub_library");
		place = jsonObject.getString("place");
		book_num = jsonObject.getString("book_num");
		order_num = jsonObject.getString("order_num");
		bar_code = jsonObject.getString("bar_code");
		can_order = jsonObject.getBoolean("can_order");
		
		JSONArray jsonArray = jsonObject.getJSONArray("PICKUP");
		for (int i=0; i<jsonArray.length(); ++i){
			PICKUP.add(jsonArray.getString(i));
		}
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getDue_time() {
		return due_time;
	}
	public void setDue_time(String due_time) {
		this.due_time = due_time;
	}
	public String getSub_library() {
		return sub_library;
	}
	public void setSub_library(String sub_library) {
		this.sub_library = sub_library;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getBook_num() {
		return book_num;
	}
	public void setBook_num(String book_num) {
		this.book_num = book_num;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}
	public Boolean getCan_order() {
		return can_order;
	}
	public void setCan_order(Boolean can_order) {
		this.can_order = can_order;
	}
	public ArrayList<String> getPICKUP() {
		return PICKUP;
	}
	public void setPICKUP(ArrayList<String> pICKUP) {
		PICKUP = pICKUP;
	}

}
