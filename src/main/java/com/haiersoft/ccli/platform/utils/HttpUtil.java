package com.haiersoft.ccli.platform.utils;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.mozilla.javascript.tools.idswitch.FileBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * wwj 2018/8/22 17:06
 */
@Component
public class HttpUtil{

	private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
//
//	public String doPost(String url, List<NameValuePair> nvps) throws Exception {
//		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//		try {
//			HttpPost httppost = new HttpPost(url);
//			// 添加HTTP POST参数
//			logger.info("----------------------------------------");
//			logger.info("请求网址" + httppost.getRequestLine().getUri());
//			logger.info("请求参数" + JSONObject.toJSONString(nvps));
//			logger.info("----------------------------------------");
//			// 将POST参数以UTF-8编码并包装成表单实体对象
//			httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
//			// 执行请求并获取结果
//			HttpResponse httpResponse = httpclient.execute(httppost);
//			HttpEntity entity = httpResponse.getEntity();
//			String responseBody = new String(EntityUtils.toString(entity).getBytes("utf-8"));
//			return responseBody;
//		} finally {
//			httpclient.close();
//		}
//	}
//
//	public String doGet(String url) throws Exception {
//		return doGet(url, new ArrayList<NameValuePair>());
//	}
//
//	public String doGet(String url, List<NameValuePair> nvps) throws Exception {
//		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//		try {
//			URIBuilder uriBuilder = new URIBuilder(url);
//			if (Detect.notEmpty(nvps)) {
//				uriBuilder.setParameters(nvps);
//			}
//			// 根据带参数的URI对象构建GET请求对象
//			HttpGet httpGet = new HttpGet(uriBuilder.build());
//			// 添加HTTP POST参数
//			logger.info("----------------------------------------");
//			logger.info("请求网址" + httpGet.getURI());
//			logger.info("请求参数" + JSONObject.toJSONString(nvps));
//			logger.info("----------------------------------------");
//			// 将POST参数以UTF-8编码并包装成表单实体对象
//			// 执行请求并获取结果
//			HttpResponse httpResponse = httpclient.execute(httpGet);
//			HttpEntity entity = httpResponse.getEntity();
//			String responseBody = new String(EntityUtils.toString(entity).getBytes("utf-8"));
//			return responseBody;
//		} finally {
//			httpclient.close();
//		}
//	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	@Async
	public void doPost(String url, String json) throws Exception{

		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		StringEntity s = new StringEntity(json,"UTF-8");
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");// 发送json数据需要设置contentType
		post.setEntity(s);
		HttpResponse res = httpclient.execute(post);

		if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = res.getEntity();
			String responseBody = new String(EntityUtils.toString(entity).getBytes("utf-8"));
			//return responseBody;
		}
		//return "";
	}

//	public static String postRequest(String url, HttpServletRequest request) throws Exception {
//		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
//		HttpPost post = new HttpPost(url);
//		HttpUriRequest request2 = HttpRequest;
//		HttpResponse res = httpclient.
//
//	}

	public static String doPostUploadFile(String url, MultipartFile file,String fileParamName) {
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		String result = "";
		try {
			String fileName = file.getOriginalFilename();
			HttpPost httpPost = new HttpPost(url);

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("utf-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody(fileParamName, file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity reponseEntity = response.getEntity();
			if (reponseEntity != null) {
				result = EntityUtils.toString(reponseEntity, Charset.forName("utf-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;

	}
}
