package com.chinaece.gaia.parsers;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.chinaece.gaia.types.WeatherType;

public class WeatherParser extends AbstractJSONParser<WeatherType>{

	@Override
	public WeatherType parser(JSONObject jsonObj) {
		try {
			WeatherType weather = new WeatherType();
			weather.setCity(jsonObj.getString("city"));
			weather.setTips(jsonObj.getString("tips"));
			weather.setTodayTemp(jsonObj.getString("todayTemp"));
			weather.setTodayWeather(jsonObj.getString("todayWeather"));
			weather.setTodayWind(jsonObj.getString("todayWind"));
			weather.setTomorrowTemp(jsonObj.getString("tomorrowTemp"));
			weather.setTomorrowWeather(jsonObj.getString("tomorrowWeather"));
			return weather;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<WeatherType> parser(JSONArray jsonArray) {
		throw new UnsupportedOperationException("do not call");
	}

}
