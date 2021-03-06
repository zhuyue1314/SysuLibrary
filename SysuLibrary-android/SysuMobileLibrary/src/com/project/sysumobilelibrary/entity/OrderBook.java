package com.project.sysumobilelibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OrderBook extends Book {

	private String book_num;
	private String book_state;
	private boolean can_delete;
	private String no;
	private String get_place;
	private String meet_date;
	private String sub_library;
	private String order_state;
	private String order_valid;
	private String doc_number;
	
	public void getFromJSONObject(JSONObject jsonObject) throws JSONException{
		book_num = jsonObject.getString("book_num");
		super.doc_number = jsonObject.getString("doc_number");
		book_state = jsonObject.getString("book_state");
		can_delete = jsonObject.getBoolean("can_delete");
		super.name = jsonObject.getString("name");
		no = jsonObject.getString("no");
		get_place = jsonObject.getString("get_place");
		meet_date = jsonObject.getString("meet_date");
		sub_library = jsonObject.getString("sub_library");
		order_state = jsonObject.getString("order_state");
		order_valid = jsonObject.getString("order_valid");
		doc_number = jsonObject.getString("doc_number");
		Log.e("11111111", doc_number);
	}

	public String getBook_num() {
		return book_num;
	}

	public void setBook_num(String book_num) {
		this.book_num = book_num;
	}

	public String getBook_state() {
		return book_state;
	}

	public void setBook_state(String book_state) {
		this.book_state = book_state;
	}

	public boolean isCan_delete() {
		return can_delete;
	}

	public void setCan_delete(boolean can_delete) {
		this.can_delete = can_delete;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getGet_place() {
		return get_place;
	}

	public void setGet_place(String get_place) {
		this.get_place = get_place;
	}

	public String getMeet_date() {
		return meet_date;
	}

	public void setMeet_date(String meet_date) {
		this.meet_date = meet_date;
	}

	public String getSub_library() {
		return sub_library;
	}

	public void setSub_library(String sub_library) {
		this.sub_library = sub_library;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getOrder_valid() {
		return order_valid;
	}

	public void setOrder_valid(String order_valid) {
		this.order_valid = order_valid;
	}

	public String getDoc_number() {
		return doc_number;
	}

	public void setDoc_number(String doc_number) {
		this.doc_number = doc_number;
	}



	

}
