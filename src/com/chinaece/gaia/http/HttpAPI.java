package com.chinaece.gaia.http;

import java.util.Collection;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.chinaece.gaia.parsers.GaiaParser;
import com.chinaece.gaia.types.GaiaType;

public interface HttpAPI {
	abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs );
	
	abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs );
	
	abstract public Collection<? extends GaiaType> doRequest(HttpRequestBase req, GaiaParser<? extends GaiaType> parser);
}
