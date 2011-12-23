package com.chinaece.gaia.http;

import java.util.Collection;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.parsers.AppParser;
import com.chinaece.gaia.parsers.ContactParser;
import com.chinaece.gaia.parsers.DocumentParser;
import com.chinaece.gaia.parsers.PendingParser;
import com.chinaece.gaia.parsers.UserParser;
import com.chinaece.gaia.types.AppType;
import com.chinaece.gaia.types.ContactType;
import com.chinaece.gaia.types.DocumentType;
import com.chinaece.gaia.types.GaiaType;
import com.chinaece.gaia.types.PendingType;
import com.chinaece.gaia.types.UserType;

public class OAHttpApi extends AbstractHttpAPI implements HttpAPI{
	
	private String url;
	
	@SuppressWarnings("unused")
	private OAHttpApi(){}
	
	public OAHttpApi(String url){
		this.url = url;
	}
	
	public boolean getToken(String user, String password, String domain){
		HttpPost post = createHttpPost(url+"/client/getToken.action", new BasicNameValuePair("user", user), new BasicNameValuePair("pwd", password), new BasicNameValuePair("domain", domain));
		Collection<? extends GaiaType> rst = doRequest(post, new UserParser());
		if(rst != null){
			Collection<UserType> users = (Collection<UserType>)rst;
			Gaia.USER = users.iterator().next();
			return true;
		}
		return false;
	}
	
	public boolean getApps(String token){
		HttpPost post = createHttpPost(url+"/client/getApps.action",new BasicNameValuePair("token",token));
		Collection<? extends GaiaType> rst = doRequest(post, new AppParser());
		if(rst != null){
			Collection<AppType> apps = (Collection<AppType>)rst;
			Gaia.APPLIST = apps;
			return true;
		}
		return false;
	}
	
	public Collection<PendingType> getPending(String token,String appids){
		HttpPost post = createHttpPost(url+"/client/getPendings.action",new BasicNameValuePair("token", token),new BasicNameValuePair("params", appids));
		Collection<? extends GaiaType> rst = doRequest(post, new PendingParser());
		if(rst!=null)
			return (Collection<PendingType>)rst;
		return null;
	}
	
	public Collection<DocumentType> getDocument(String token,String document){
		HttpPost post = createHttpPost(url+"/client/getDocument.action",new BasicNameValuePair("token",token),new BasicNameValuePair("params",document));
		Collection<?extends GaiaType> rst = doRequest(post, new DocumentParser());
		if(rst!=null)
			return (Collection<DocumentType>)rst;
		return null;
	}
	
	public Collection<ContactType> getContact(String token,String search){
		HttpPost post = createHttpPost(url+"/client/getContacts.action",new BasicNameValuePair("token",token),new BasicNameValuePair("params",search));
		System.err.println(search);
		Collection<?extends GaiaType> rst = doRequest(post, new ContactParser());
		if(rst!=null)
			return (Collection<ContactType>)rst;
		return null;
	}
}
