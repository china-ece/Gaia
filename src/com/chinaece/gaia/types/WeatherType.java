package com.chinaece.gaia.types;

public class WeatherType implements GaiaType{

	private String city;
	private String todayTemp;
	private String tomorrowTemp;
	private String todayWeather;
	private String tomorrowWeather;
	private String todayWind;
	private String tips;	
	
	public String getCity()
	{
		return city;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public String getTodayTemp()
	{
		return todayTemp;
	}
	
	public void setTodayTemp(String todayTemp)
	{
		this.todayTemp = todayTemp;
	}
	
	public String getTomorrowTemp()
	{
		return tomorrowTemp;
	}
	
	public void setTomorrowTemp(String tomorrowTemp)
	{
		this.tomorrowTemp = tomorrowTemp;
	}
	
	public String getTodayWeather()
	{
		return todayWeather;
	}
	
	public void setTodayWeather(String todayWeather)
	{
		this.todayWeather = todayWeather;
	}
	
	public String getTomorrowWeather()
	{
		return tomorrowWeather;
	}
	
	public void setTomorrowWeather(String tomorrowWeather)
	{
		this.tomorrowWeather = tomorrowWeather;
	}
	
	public String getTodayWind()
	{
		return todayWind;
	}
	
	public void setTodayWind(String todayWind)
	{
		this.todayWind = todayWind;
	}
	
	public String getTips()
	{
		return tips;
	}
	
	public void setTips(String tips)
	{
		this.tips = tips;
	}
	}