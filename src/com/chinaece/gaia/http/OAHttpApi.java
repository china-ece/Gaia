package com.chinaece.gaia.http;

import java.util.Collection;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.parsers.UserParser;
import com.chinaece.gaia.types.GaiaType;
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
}
