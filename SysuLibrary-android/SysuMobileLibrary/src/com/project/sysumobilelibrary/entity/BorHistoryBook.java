package com.project.sysumobilelibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class BorHistoryBook extends Book {

	private String due_date;
	private String due_time;
	private String no;
	private String ret_date;
	private String ret_time;
	private String sub_library;
	
	public void getFromJSONObject(JSONObject jsonObject) throws JSONException{
		super.author = jsonObject.getString("author");
		super.doc_number = jsonObject.getString("doc_number");
		due_date = jsonObject.getString("due_date");
		due_time = jsonObject.getString("due_time");
		super.name = jsonObject.getString("name");
		no = jsonObject.getString("no");
		ret_date = jsonObject.getString("ret_date");
		ret_time = jsonObject.getString("ret_time");
		sub_library = jsonObject.getString("sub_library");
		super.year = jsonObject.getString("year");
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getRet_date() {
		return ret_date;
	}

	public void setRet_date(String ret_date) {
		this.ret_date = ret_date;
	}

	public String getRet_time() {
		return ret_time;
	}

	public void setRet_time(String ret_time) {
		this.ret_time = ret_time;
	}

	public String getSub_library() {
		return sub_library;
	}

	public void setSub_library(String sub_library) {
		this.sub_library = sub_library;
	}
	


	

}
