package com.chinaece.gaia.types;

public class DocumentType implements GaiaType{
	private String name;
	private String desc;
	private String value;
	private String display;
	private String direction;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getDirection(){
		return direction;
	}
	public void setDirection(String direction){
		this.direction = direction;
	}
	
	}
	


