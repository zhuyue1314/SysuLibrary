package com.project.sysumobilelibrary.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.Cursor;

public class LikeBook extends SearchBook implements Cloneable{

	private Long like_time;
	private String note;
	private int index;
	





	public String getFormatLikeTime(){
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss yyyy");
		Date date = new Date(like_time);
		return format.format(date);
	}


	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}


	public Long getLike_time() {
		return like_time;
	}


	public void setLike_time(Long like_time) {
		this.like_time = like_time;
	}
	
	
}
