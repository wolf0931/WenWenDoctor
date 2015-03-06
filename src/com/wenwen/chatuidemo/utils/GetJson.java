package com.wenwen.chatuidemo.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

/**
 * 类说明
 */
public class GetJson {
	
	/** post */
	public static String postRequest(String sURL, Map<String, String> params)
	{
		HttpClient httpclient = new DefaultHttpClient();
		//超时请求
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

		HttpPost httppost = new HttpPost(sURL);

		ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
		for(Iterator<String> it = params.keySet().iterator();it.hasNext();)
		{
			try
			{
				String sParamKey = it.next();
				String sParam = params.get(sParamKey);
				nameValuePairs.add(new BasicNameValuePair(sParamKey, sParam));
			}
			catch(Exception e)
			{	
				e.printStackTrace();
			}
		}

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"GBK"/*HTTP.UTF_8*/));//设置post参数 并设置编码格式		
			HttpResponse response = httpclient.execute(httppost);
			
			String result = retrieveInputStream(response.getEntity());
			
			return result;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 向api发送get请求，url需按照api要求写，返回从服务器取得的信息。
	 * @param url
	 * @return String
	 */
	public static String getRequest(String url) throws Exception {
		return getRequest(url, new DefaultHttpClient(new BasicHttpParams()));
	}
	
	/**
	 *  
	 * @param url
	 * @param client
	 * @return String
	 */
	protected static String getRequest(String url, DefaultHttpClient client) throws Exception {
		String result = null;
		int statusCode = 0;
		//System.out.println("getRequest--->>"+url);
		HttpGet getMethod = new HttpGet(url);
		
		try {
			//读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
			
			HttpResponse httpResponse = client.execute(getMethod);
			//statusCode == 200 正常
			statusCode = httpResponse.getStatusLine().getStatusCode();
			
			//处理返回的httpResponse信息
			result = retrieveInputStream(httpResponse.getEntity());
		}catch (SocketTimeoutException se) {
			throw se;
		}catch (Exception e) {
			throw e;
		} finally {
			getMethod.abort();
		}
		return result;
	}
	
	/**
	 * 处理httpResponse信息,返回String
	 * @param httpEntity
	 * @return String
	 */
	protected static String retrieveInputStream(HttpEntity httpEntity) {
		Long l = httpEntity.getContentLength();		
		int length = (int) httpEntity.getContentLength();		
		//the number of bytes of the content, or a negative number if unknown. If the content length is known but exceeds Long.MAX_VALUE, a negative number is returned.
		//length==-1，下面这句报错，println needs a message
		//System.out.println("length = "+length);
		if (length <= 0) length = 10000;
		StringBuffer stringBuffer = new StringBuffer(length);
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(httpEntity.getContent(), HTTP.UTF_8);
			char buffer[] = new char[length];
			int count;
			while ((count = inputStreamReader.read(buffer, 0, length - 1)) > 0) {
				stringBuffer.append(buffer, 0, count);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return stringBuffer.toString();
	}
}
