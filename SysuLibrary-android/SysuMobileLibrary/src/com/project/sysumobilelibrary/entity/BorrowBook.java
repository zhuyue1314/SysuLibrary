package com.project.sysumobilelibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class BorrowBook extends Book {

	private String due_date;
	private String select_no;
	private String sub_library;
	private String no;
	
	public void getFromJSONObject(JSONObject jsonObject) throws JSONException{
		super.author = jsonObject.getString("author");
		super.doc_number = jsonObject.getString("doc_number");
		due_date = jsonObject.getString("due_date");
		super.name = jsonObject.getString("name");
		select_no = jsonObject.getString("select_no");
		sub_library = jsonObject.getString("sub_library");
		super.year = jsonObject.getString("year");
		no = jsonObject.getString("no");
	}
	
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getSelect_no() {
		return select_no;
	}
	public void setSelect_no(String select_no) {
		this.select_no = select_no;
	}

	public String getSub_library() {
		return sub_library;
	}

	public void setSub_library(String sub_library) {
		this.sub_library = sub_library;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	

}
