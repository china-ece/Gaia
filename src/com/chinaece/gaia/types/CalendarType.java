package com.chinaece.gaia.types;

import java.io.Serializable;

public class CalendarType implements GaiaType,Serializable{
	
	private static final long serialVersionUID = -5772724268794155583L;
	
	private String affair;
	
	public String getAffair() {
		return affair;
	}

	public void setAffair(String affair) {
		this.affair = affair;
	}

	public String getContent1() {
		return content1;
	}

	public void setContent1(String content1) {
		this.content1 = content1;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	private String content1;
	
	private String starttime;
	
	private String endtime;

}
