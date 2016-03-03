package com.project.sysumobilelibrary.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	
	private String username;
	private String password;
	private String token;
	private String name;
	private String address;
	private String email;
	private String addr_start;
	private String addr_end;
	private String postalcode;
	private String phone1;
	private String phone2;
	private String phone3;
	private String phone4;
	private String state;
	private String type;
	private String number;
	private String valid_date;
	private String borrow_num;
	private String borrow_history;
	private String order_num;
	private String cash;
	private String debt;
	
	public void updateFromJSONObject(JSONObject jsonObject) throws JSONException{
		username = jsonObject.getString("number");
		token = jsonObject.getString("token");
		name = jsonObject.getString("name");
		address = jsonObject.getString("address");
		email = jsonObject.getString("email");
		addr_start = jsonObject.getString("addr_start");
		addr_end = jsonObject.getString("addr_end");
		postalcode = jsonObject.getString("postalcode");
		phone1 = jsonObject.getString("phone1");
		phone2 = jsonObject.getString("phone2");
		phone3 = jsonObject.getString("phone3");
		phone4 = jsonObject.getString("phone4");
		state = jsonObject.getString("state");
		type = jsonObject.getString("type");
		number = jsonObject.getString("number");
		valid_date = jsonObject.getString("valid_date");
		borrow_num = jsonObject.getString("borrow_num");
		borrow_history = jsonObject.getString("borrow_history");
		order_num = jsonObject.getString("order_num");
		cash = jsonObject.getString("cash");
		debt = jsonObject.getString("debt");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}













	public void setPassword(String password) {
		this.password = password;
	}













	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddr_start() {
		return addr_start;
	}
	public void setAddr_start(String addr_start) {
		this.addr_start = addr_start;
	}
	public String getAddr_end() {
		return addr_end;
	}
	public void setAddr_end(String addr_end) {
		this.addr_end = addr_end;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getPhone3() {
		return phone3;
	}
	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}
	public String getPhone4() {
		return phone4;
	}
	public void setPhone4(String phone4) {
		this.phone4 = phone4;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getValid_date() {
		return valid_date;
	}
	public void setValid_date(String valid_date) {
		this.valid_date = valid_date;
	}
	public String getBorrow_num() {
		return borrow_num;
	}
	public void setBorrow_num(String borrow_num) {
		this.borrow_num = borrow_num;
	}
	public String getBorrow_history() {
		return borrow_history;
	}
	public void setBorrow_history(String borrow_history) {
		this.borrow_history = borrow_history;
	}
	public String getOrder_num() {
		return order_num;
	}
	public void setOrder_num(String order_num) {
		this.order_num = order_num;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	public String getDebt() {
		return debt;
	}
	public void setDebt(String debt) {
		this.debt = debt;
	}



}
