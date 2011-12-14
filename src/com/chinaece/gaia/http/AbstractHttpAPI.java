package com.chinaece.gaia.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.chinaece.gaia.constant.Gaia;
import com.chinaece.gaia.parsers.GaiaParser;
import com.chinaece.gaia.types.GaiaType;

abstract public class AbstractHttpAPI implements HttpAPI{
	
	private static final int TIMEOUT = 5;
	private final HttpClient client;
	public AbstractHttpAPI() {
		client = initClient();
	}
	
	public DefaultHttpClient initClient(){
        final SchemeRegistry supportedSchemes = new SchemeRegistry();
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));
        final HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClientParams.setRedirecting(params, false);
        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
                supportedSchemes);
         return new DefaultHttpClient(ccm, params);
	}
	
	@Override
	public Collection<? extends GaiaType> doRequest(HttpRequestBase req, GaiaParser<? extends GaiaType> parser) {
		try {
			client.getConnectionManager().closeExpiredConnections();
			HttpResponse response = client.execute(req);
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case 200:
				String content = EntityUtils.toString(response.getEntity()).trim();
				if(Gaia.DEBUG)
					Log.d(Gaia.TAG_HTTP, content);
				try{
					JSONObject obj = new JSONObject(content);
					if(obj.getBoolean("status")){
						ArrayList<GaiaType> list = new ArrayList<GaiaType>();
						list.add(parser.parser(obj));
						return list;
					}
					return null;
				}catch (JSONException e) {
					try{
						JSONArray arr = new JSONArray(content);
						return parser.parser(arr);
					}catch (JSONException e1) {
						return null;
					}
				}
			case 404:
				Log.e(Gaia.TAG_HTTP, "wrong http request, wrong address?");
				return null;
			default:
				Log.wtf(Gaia.TAG_HTTP, "unhandled http status code");
				return null;
			}
		} catch (Exception e) {
			req.abort();
			e.printStackTrace();
		}
		return null;
	};

	@Override
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		HttpGet get = new HttpGet(url + "?" + URLEncodedUtils.format(clearHttpParams(nameValuePairs), HTTP.UTF_8));
		if(Gaia.DEBUG)
			Log.d(Gaia.TAG_HTTP, "create http get " + get.getURI());
		return get;
	}

	@Override
	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(clearHttpParams(nameValuePairs)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(Gaia.DEBUG)
			Log.d(Gaia.TAG_HTTP, "create http post " + post);
		return post;
	}
	
	private List<NameValuePair> clearHttpParams(NameValuePair...nameValuePairs){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(NameValuePair pair : nameValuePairs)
			if(pair.getValue() != null && pair.getValue().trim() != "")
				params.add(pair);
		return params;
	}

}
