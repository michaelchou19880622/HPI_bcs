package com.hpicorp.bcs.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import com.hpicorp.bcs.entities.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientUtil {
	
	private static final Object INIT_FLAG = "INIT_FLAG";
	
	private static CloseableHttpClient httpClient;
	
	private static int timeout = 3;

	private HttpClientUtil() {}
	
	public static HttpClient generateClient() throws KeyManagementException, NoSuchAlgorithmException {
		synchronized (INIT_FLAG) {
			if(httpClient == null){
				SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

				// set up a TrustManager that trusts everything
				sslContext.init(null, new TrustManager[] { new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						log.info("getAcceptedIssuers => {}", Calendar.getInstance().getTime());
						return null;
					}
					
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
						log.info("checkClientTrusted => {}", Calendar.getInstance().getTime());
					}
					
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
						log.info("checkServerTrusted => {}", Calendar.getInstance().getTime());
					}
				} }, new SecureRandom());
				
				SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
				
				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					    .register("http", PlainConnectionSocketFactory.getSocketFactory())
					    .register("https", sslsf)
					    .build();
				
				PoolingHttpClientConnectionManager pm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				pm.setMaxTotal(1200);
				pm.setDefaultMaxPerRoute(300);
				
				RequestConfig defaultRequestConfig = RequestConfig.custom()
					    .setSocketTimeout(timeout  * 1000)
					    .setConnectTimeout(timeout  * 1000)
					    .setConnectionRequestTimeout(timeout  * 1000)
					    .build();
				
				httpClient = HttpClients.custom().setConnectionManager(pm).setDefaultRequestConfig(defaultRequestConfig).build();
			}
		}
		
		return httpClient; 
	}
	
	public static ResponseDTO execute(HttpUriRequest request, int errCount) {
		ResponseDTO dto = null;
		try {
			dto = executeAction(request);
		} catch (Exception e) {
			errCount += 1;
			log.error("『 bcs 』http client execute error count => {}, url => {}, error => {}", errCount, request.getURI(), e);
			try {
				dto = executeAction(request);
			} catch (Exception e2) {
				errCount += 1;
				log.error("『 bcs 』http client execute error count => {}, url => {}, error => {}", errCount, request.getURI(), e2);
				try {
					dto = executeAction(request);
				} catch (Exception e3) {
					errCount += 1;
					log.error("『 bcs 』http client execute error count => {}, url => {}, error => {}", errCount, request.getURI(), e3);
					try {
						dto = executeAction(request);
					} catch (Exception e4) {
						log.error("『 bcs 』http client execute error count over three times, url => {}, error => {}", request.getURI(), e4);
						dto = new ResponseDTO(400, "");
					}
				}
			}
		}
		return dto;
	}
	
	public static ResponseDTO executeAction(HttpUriRequest request) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException {
		Integer statusCode;
		InputStream inputStream = null;
		StringWriter writer = new StringWriter();
		HttpClient client = generateClient();
		HttpResponse rsp =  client.execute(request);
		statusCode = rsp.getStatusLine().getStatusCode();
		HttpEntity rspEntity = rsp.getEntity();
		inputStream = rspEntity.getContent();
		IOUtils.copy(inputStream, writer, "utf-8");
		EntityUtils.consume(rspEntity);
		return new ResponseDTO(statusCode, writer.toString());
	}

	public static ResponseDTO postJSON(String url, String body) {
		Header[] headers = { new BasicHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE) };
		return post(url, headers, new StringEntity(body, Charset.forName("utf-8")));
	}
	
	public static ResponseDTO postJSON(String url, String body, Header[] headers) {
		return post(url, headers, new StringEntity(body, Charset.forName("utf-8")));
	}
	
	public static ResponseDTO get(String url, Header[] headers) {
		HttpGet requestGet = new HttpGet(url);
		requestGet.setHeaders(headers);
		return execute(requestGet, 0);
	}
	
	public static ResponseDTO post(String url, Header[] headers, HttpEntity body) {
		HttpPost requestPost = new HttpPost(url);
		requestPost.setHeaders(headers);
		requestPost.setEntity(body);
		return execute(requestPost, 0);
	}
	
	public static ResponseDTO put(String url, Header[] headers, String body) {
		HttpPut requestPut = new HttpPut(url);
		requestPut.setHeaders(headers);
		requestPut.setEntity(new StringEntity(body, Charset.forName("utf-8")));
		return execute(requestPut, 0);
	}

	
	public static ResponseDTO delete(String url, Header[] headers) {
		HttpDelete requestDelete = new HttpDelete(url);
		requestDelete.setHeaders(headers);
		return execute(requestDelete, 0);
	}
	
	public static HttpEntity toUrlEncodeForm(Map<String, String> map) {
		try {
			List <NameValuePair> nvps = new ArrayList <>();
			for(Entry<String, String> entry: map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			return new UrlEncodedFormEntity(nvps, "utf-8");
		} catch (Exception e) {
			log.error("Convert To URL Encode Form Error", e);
		}
		return null;
	}
}
