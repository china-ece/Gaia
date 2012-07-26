package com.chinaece.gaia.http;

import java.util.Collection;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.parsers.AppParser;
import com.chinaece.gaia.parsers.BossTrackParser;
import com.chinaece.gaia.parsers.CalendarParsers;
import com.chinaece.gaia.parsers.ContactParser;
import com.chinaece.gaia.parsers.DocumentParser;
import com.chinaece.gaia.parsers.PendingParser;
import com.chinaece.gaia.parsers.UserParser;
import com.chinaece.gaia.parsers.WeatherParser;
import com.chinaece.gaia.types.AppType;
import com.chinaece.gaia.types.BossTrackingType;
import com.chinaece.gaia.types.CalendarType;
import com.chinaece.gaia.types.ContactType;
import com.chinaece.gaia.types.DocumentType;
import com.chinaece.gaia.types.GaiaType;
import com.chinaece.gaia.types.PendingType;
import com.chinaece.gaia.types.UserType;
import com.chinaece.gaia.types.WeatherType;

public class OAHttpApi extends AbstractHttpAPI implements HttpAPI{
	
	private String url;
	
	@SuppressWarnings("unused")
	private OAHttpApi(){}
	
	public OAHttpApi(String url){
		this.url = url;
	}
	
	public UserType getToken(String user, String password, String domain){
		HttpGet get = createHttpGet(url+"/client/getToken.action", new BasicNameValuePair("user", user), new BasicNameValuePair("pwd", password), new BasicNameValuePair("domain", domain));
		Collection<? extends GaiaType> rst = doRequest(get, new UserParser());
		if(rst != null){
			Collection<UserType> users = (Collection<UserType>)rst;
			return users.iterator().next();
		}
		return null;
	}
	
	public boolean getApps(String token){
		HttpGet get = createHttpGet(url+"/client/getApps.action",new BasicNameValuePair("token",token));
		Collection<? extends GaiaType> rst = doRequest(get, new AppParser(),true);
		if(rst != null){
			Collection<AppType> apps = (Collection<AppType>)rst;
			Gaia.APPLIST = apps;
			return true;
		}
		return false;
	}
	
	public Collection<PendingType> getPending(String token,String appids){
		HttpGet get = createHttpGet(url+"/client/getPendings.action",new BasicNameValuePair("token", token),new BasicNameValuePair("params", appids));
		Collection<? extends GaiaType> rst = doRequest(get, new PendingParser());
		if(rst!=null)
			return (Collection<PendingType>)rst;
		return null;
	}
	
	public DocumentType getDocument(String token,String document){
		HttpGet get = createHttpGet(url+"/client/getDocument.action",new BasicNameValuePair("token",token),new BasicNameValuePair("params",document));
		Collection<?extends GaiaType> rst = doRequest(get, new DocumentParser());
		if(rst!=null)
			return (DocumentType)rst.iterator().next();
		return null;
	}
	
	public Collection<ContactType> getContact(String token,String search){
		HttpGet get = createHttpGet(url+"/client/getContacts.action",new BasicNameValuePair("token",token),new BasicNameValuePair("params",search));
		Collection<?extends GaiaType> rst = doRequest(get, new ContactParser());
		if(rst!=null)
			return (Collection<ContactType>)rst;
		return null;
	}
	
	public Collection<WeatherType> getWeather(){
		HttpGet get = createHttpGet(url+"/client/getWeather.action");
		Collection<? extends GaiaType> rst = doRequest(get, new WeatherParser(),true);
		if(rst != null){
		return(Collection<WeatherType>)rst;
		}
		return null;
	}
	
	public boolean saveDocumnet(String token,String save ){
		HttpPost post = createHttpPost(url+"/client/saveDocument.action",new BasicNameValuePair("token", token),new BasicNameValuePair("params",save ));
		String rst = doRequest(post);
		try {
			if(rst == null)
				return false;
			JSONObject flag = new JSONObject(rst);
			return flag.getBoolean("result");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean submitDocumnet(String token,String submit ){
		HttpPost post = createHttpPost(url+"/client/submitDocument.action",new BasicNameValuePair("token", token),new BasicNameValuePair("params",submit));
		String rst = doRequest(post);
		try {
			if(rst == null)
				return false;
			JSONObject flag = new JSONObject(rst);
			return flag.getBoolean("result");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Collection<BossTrackingType> getBossTrack(){
		HttpPost post = createHttpPost(url+"/client/getBossTracking.action");
		Collection<? extends GaiaType> rst = doRequest(post,new BossTrackParser());
		if(rst != null){
			return(Collection<BossTrackingType>)rst;
		}
		return null;
	}
	
	public Collection<CalendarType> getBossCalendar(String token , String start ,String end){
		HttpGet get = createHttpGet(url+"/client/getBossCalendar.action",new BasicNameValuePair("token", token),new BasicNameValuePair("start", start),new BasicNameValuePair("end", end));
		Collection<? extends GaiaType> rst = doRequest(get,new CalendarParsers());
		if(rst != null){
			return(Collection<CalendarType>)rst;
		}
		return null;
	}
	
	//日程新建
	public boolean newBbuild(String token,String document){
		HttpGet post = createHttpGet(url+"/client/newDocument.action",new BasicNameValuePair("token", token),new BasicNameValuePair("params",document));
		String rst = doRequest(post);
		try {
			if(rst == null)
				return false;
			JSONObject flag = new JSONObject(rst);
			return flag.getBoolean("result");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
