package com.yumi.butler.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class LocalHttpClient {
	private static int maxTotal = 800;
	private HttpClient httpClient;
	private static LocalHttpClient localHttpClient = null;
	private static PoolingHttpClientConnectionManager connManager = null;
	
	public void init(int maxTotal){
		LocalHttpClient.maxTotal = maxTotal;
	}
	
	private LocalHttpClient(){
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(1000*60).build();
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();  
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();  
        registryBuilder.register("http", plainSF);  
        //指定信任密钥存储对象和连接套接字工厂  
        try {  
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接  
            TrustStrategy anyTrustStrategy = new TrustStrategy() {  
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;  
                }  
            };  
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
            registryBuilder.register("https", sslSF);  
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }  
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();  
        
        //设置连接管理器  
        connManager = new PoolingHttpClientConnectionManager(registry);  
        connManager.setMaxTotal(maxTotal);
        connManager.setDefaultMaxPerRoute(connManager.getMaxTotal());
        connManager.setDefaultSocketConfig(socketConfig);
	}
	
	
	public static LocalHttpClient getInstance(){
		if(null!=localHttpClient)
			return localHttpClient;
		localHttpClient = new LocalHttpClient();
		return localHttpClient;
	}
	
	public CloseableHttpResponse execute(HttpUriRequest request){
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != httpClient){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	public <T> T execute(HttpUriRequest request,ResponseHandler<T> responseHandler){
		try {
			CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
			return httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String exe(HttpUriRequest request){
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				return EntityUtils.toString(entity);
			} else {
				System.out.println("http return status error:" + status);
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(null != response){
					response.close();
				}
				if(null != httpClient){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public HttpResponse exe(HttpPost httppost){
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httppost, new BasicHttpContext());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != httpClient){
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}
}
