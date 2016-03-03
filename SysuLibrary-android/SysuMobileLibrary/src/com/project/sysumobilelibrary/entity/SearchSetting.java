package com.project.sysumobilelibrary.entity;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchSetting {
	
	private final static ArrayList<String> allKeytypes = new ArrayList<String>(Arrays.asList("所有字段","正题名(精确匹配)","题名关键词","著者","主题词","出版社","ISSN","ISBN","索书号","系统号","条形码","用户标签"));
	private final static ArrayList<String> allOps = new ArrayList<String>(Arrays.asList("and", "or","and", "or"));
	private static ArrayList<String> allNums = new ArrayList<String>();
	private final static ArrayList<String> allSubLibrarys = new ArrayList<String>(Arrays.asList("中山大学图书馆","南校区中文流通","南校区外文流通","南校区报刊","特藏部","南校区参考咨询","北校区流通","北校区图书阅览","北校区报刊阅览","东校区流通","东校区阅览","东校区专藏室","东校区法学馆","东校区地库","行政管理中心","珠海校区流通","珠海校区阅览","经管分馆阅览","经管分馆流通"));
	private static ArrayList<String> yearList = new ArrayList<String>();
	private final static ArrayList<String> allFlushs = new ArrayList<String>(Arrays.asList("是", "否","是", "否"));
	
	public SearchSetting() {
		for (int i=1990; i<2020; ++i){
        	yearList.add(i+"");
        }
		for (int i=1; i<=20; ++i){
        	allNums.add(i+"");
        }
		keytype = allKeytypes.get(0);
		num = allNums.get(9);
		op = allOps.get(0);
		flush = allFlushs.get(1);
		
		sub_library = allSubLibrarys.get(0);
	}
	
	private String keytype;
	private String num;
	private String op;
	private String flush;
	private String start_year = "2000";
	private String end_year = "2016";
	private String sub_library;
	
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("keytype:"+keytype);
		str.append("-op:"+op);
		str.append("-start_year:"+start_year);
		str.append("-end_year:"+end_year);
		str.append("-sub_library:"+sub_library);
		return str.toString();
	}
	
	
	
	public String getKeytype() {
		return keytype;
	}
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getStart_year() {
		return start_year;
	}
	public void setStart_year(String start_year) {
		this.start_year = start_year;
	}
	public String getEnd_year() {
		return end_year;
	}
	public void setEnd_year(String end_year) {
		this.end_year = end_year;
	}
	public String getSub_library() {
		return sub_library;
	}
	public void setSub_library(String sub_library) {
		this.sub_library = sub_library;
	}
	public static ArrayList<String> getAllkeytypes() {
		return allKeytypes;
	}
	public static ArrayList<String> getAllops() {
		return allOps;
	}

	public static ArrayList<String> getAllNums() {
		return allNums;
	}
	public static void setAllNums(ArrayList<String> allNums) {
		SearchSetting.allNums = allNums;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public static ArrayList<String> getAllsublibrarys() {
		return allSubLibrarys;
	}
	public static ArrayList<String> getYearList() {
		return yearList;
	}
	public static void setYearList(ArrayList<String> yearList) {
		SearchSetting.yearList = yearList;
	}



	public static ArrayList<String> getAllflushs() {
		return allFlushs;
	}



	public String getFlush() {
		return flush;
	}



	public void setFlush(String flush) {
		this.flush = flush;
	}
	

}
