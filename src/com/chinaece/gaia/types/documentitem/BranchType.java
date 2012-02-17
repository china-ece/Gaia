package com.chinaece.gaia.types.documentitem;

import java.io.Serializable;
import java.util.ArrayList;

import com.chinaece.gaia.types.GaiaType;

public class BranchType implements GaiaType,Serializable{
	
	private static final long serialVersionUID = -5772724268794155583L;

	private String pathid;
	
	private String name;
	
	private int mode;
	
	private ArrayList<User> listValue = new ArrayList<BranchType.User>();
	
	private String flowtype;
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public String getPathid() {
		return pathid;
	}

	public void setPathid(String pathid) {
		this.pathid = pathid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<User> getPossibleValue() {
		return listValue;
	}

	public void setListValue(ArrayList<User> listValue) {
		this.listValue = listValue;
	}
	
	public void addUser(String displayValue,String dataValue){
		User user = new User();
		user.displayValue = displayValue;
		user.dataValue = dataValue;
		listValue.add(user);
	}
	
	public class User implements Serializable{
		private static final long serialVersionUID = 1586688295051425789L;

		private String displayValue;
		 
		private String dataValue;
		
		public String getDisplayValue() {
			return displayValue;
		}

		public void setDisplayValue(String displayValue) {
			this.displayValue = displayValue;
		}

		public String getDataValue() {
			return dataValue;
		}

		public void setDataValue(String dataValue) {
			this.dataValue = dataValue;
		}

		
	}
}
	
 