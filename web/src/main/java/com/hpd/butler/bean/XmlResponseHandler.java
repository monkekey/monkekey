package com.hpd.butler.bean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class XmlResponseHandler{

	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz){
		return new ResponseHandler<T>() {
			public T handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    String str = EntityUtils.toString(entity);
                   return XMLConverUtil.convertToObject(clazz,new String(str.getBytes("ISO8859-1"),"utf-8"));
                } else {
                	//自动释放连接
        			EntityUtils.consume(response.getEntity());
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
                
			}
		};
	}
	
	public static <T> ResponseHandler<T> createResponseHandler2(final Class<T> clazz){
		return new ResponseHandler<T>() {
			public T handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					String str = EntityUtils.toString(entity);
					return XMLConverUtil.convertToObject(clazz,new String(str.getBytes(),"utf-8"));
				} else {
					//自动释放连接
					EntityUtils.consume(response.getEntity());
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
				
			}
		};
	}

}
