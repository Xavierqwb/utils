package com.xavier.utils;

import com.xavier.exception.HttpException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

/**
 * Created by qiuwenbin on 2017/9/2.
 *
 */
public class HttpUtils {

	private final static Logger LOG = LoggerFactory.getLogger("HttpUtils");

	public static class HttpClientProxy {

		// 默认最大连接数
		final static Integer DEFAULT_MAX_TOTAL = 200;

		// 默认每个路由的最大连接数
		final static Integer DEFAULT_MAX_PER_ROUTE = 20;

		private HttpClient httpClient;

		private PoolingHttpClientConnectionManager httpClientConnectionManager;

		public HttpClientProxy() {
			ConnectionSocketFactory connectionSocketFactory = new PlainConnectionSocketFactory();
			LayeredConnectionSocketFactory layeredConnectionSocketFactory = initSSL();
			init(connectionSocketFactory, layeredConnectionSocketFactory);
			httpClient = HttpClients.custom().setConnectionManager(this.httpClientConnectionManager).build();
		}

		public HttpClientProxy(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager) {
			this.httpClientConnectionManager = poolingHttpClientConnectionManager;
		}

		private LayeredConnectionSocketFactory initSSL() {
			LayeredConnectionSocketFactory socketFactory;
			try {
				socketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault());
			} catch (NoSuchAlgorithmException e) {
				LOG.warn("Init SSL failed, message:{}", e.getMessage());
				return SSLConnectionSocketFactory.getSocketFactory();
			}
			return socketFactory;
		}

		private void init(ConnectionSocketFactory connectionSocketFactory,
		                                                LayeredConnectionSocketFactory sslSocketFactory){
			Registry<ConnectionSocketFactory> registry = RegistryBuilder
				.<ConnectionSocketFactory> create().register("http", connectionSocketFactory)
				.register("https", sslSocketFactory).build();
			PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
			httpClientConnectionManager.setMaxTotal(DEFAULT_MAX_TOTAL);
			httpClientConnectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
			this.httpClientConnectionManager = httpClientConnectionManager;
		}

		public HttpClient getClient() {
			HttpClientBuilder builder = HttpClientBuilder.create();
			return builder.setConnectionManager(this.httpClientConnectionManager).build();
		}

		public String doPost(String url, Map<String, String> paramsMap) throws HttpException {
			HttpPost post = new HttpPost(url);
			String result = null;
			List<NameValuePair> paramList = new ArrayList<>();
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

			try {
				if (paramList.size() > 0) {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
					post.setEntity(entity);
				}
				HttpResponse response = httpClient.execute(post);
				if(response != null){
					HttpEntity resEntity = response.getEntity();
					if(resEntity != null){
						result = EntityUtils.toString(resEntity, "UTF-8");
					}
				}
			} catch (IOException e){
				throw new HttpException(e);
			}
			return result;
		}

		public String doGet(String url, Map<String, String> paramsMap) throws HttpException {
			StringBuilder sb = new StringBuilder(url);
			sb.append("?");

			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
			}

			HttpGet get = new HttpGet(sb.toString());
			String result = null;
			try {
				HttpResponse response = httpClient.execute(get);

				if (response != null) {
					HttpEntity entity = response.getEntity();
					if(entity != null){
						result = EntityUtils.toString(entity, "UTF-8");
					}
				}
			} catch (IOException e) {
				throw new HttpException(e);
			}
			return result;
		}
	}
}
