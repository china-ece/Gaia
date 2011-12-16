package com.chinaece.gaia.types;

public class AppType implements GaiaType{

	private String appid;
	private String name;	
	
	public String getAppid()
	{
		return appid;
	}
	
	public void setAppid(String appid)
	{
		this.appid = appid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
}