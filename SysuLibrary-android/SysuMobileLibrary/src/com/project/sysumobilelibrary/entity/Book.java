package com.project.sysumobilelibrary.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

public class Book {

	protected String doc_number;
	protected String isbn;
	protected String language;
	protected String name;
	protected String publisher;
	protected String structure;
	protected String summary;
	protected String subject;
	protected String author;
	protected String img_url;
	protected String year;
	
	protected ArrayList<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
	
	public void getFromCursor(Cursor cursor) {
		doc_number = cursor.getString(cursor.getColumnIndex("doc_number"));
		isbn = cursor.getString(cursor.getColumnIndex("isbn"));
		language = cursor.getString(cursor.getColumnIndex("language"));
		name = cursor.getString(cursor.getColumnIndex("name"));
		publisher = cursor.getString(cursor.getColumnIndex("publisher"));
		structure = cursor.getString(cursor.getColumnIndex("structure"));
		summary = cursor.getString(cursor.getColumnIndex("summary"));
		subject = cursor.getString(cursor.getColumnIndex("subject"));
		author = cursor.getString(cursor.getColumnIndex("author"));
		img_url = cursor.getString(cursor.getColumnIndex("img_url"));
		year = cursor.getString(cursor.getColumnIndex("year"));
	}
	
	public void updateFromJSONObject(JSONObject jsonObject) throws JSONException{
		doc_number = jsonObject.getString("doc_number");
		isbn = jsonObject.getString("isbn");
		language = jsonObject.getString("language");
		name = jsonObject.getString("name");
		publisher = jsonObject.getString("publisher");
		structure = jsonObject.getString("structure");
		summary = jsonObject.getString("summary");
		subject = jsonObject.getString("subject");
		author = jsonObject.getString("author");
		img_url = jsonObject.getString("img_url");
		year = jsonObject.getString("year");
	}
	
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public ArrayList<OrderInfo> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(ArrayList<OrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	public String getDoc_number() {
		return doc_number;
	}
	public void setDoc_number(String doc_number) {
		this.doc_number = doc_number;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getStructure() {
		return structure;
	}
	public void setStructure(String structure) {
		this.structure = structure;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	

}
