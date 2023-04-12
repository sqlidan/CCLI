package com.haiersoft.ccli.system.sso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.common.utils.PropertiesUtil;

@Controller
@RequestMapping("/cclisso")
public class SSOAuthTokenController {

	/**
	 * @author:
	 * @date:
	 * @description: 服务端回调方法 1.服务端回调,传回code值 2.根据code值，调用服务端服务,根据code获取access_token
	 *               3.拿到access_token重定向到客户端的服务 /oauth-client/getUserInfo 在该服务中
	 *               再调用服务端获取用户信息
	 */

	private String clientId= PropertiesUtil.getPropertiesByName("sso.clientId", "sso"); 
	private String clientSecret= PropertiesUtil.getPropertiesByName("sso.clientSecret", "sso"); 
	private String accessTokenUrl = PropertiesUtil.getPropertiesByName("sso.accessTokenUrl", "sso"); 
	private String redirectUrl = PropertiesUtil.getPropertiesByName("sso.redirectUrl", "sso"); 

	//@Value("${response_type}")
	//private String response_type;

	// 接受客户端返回的code，提交申请access token的请求
	@RequestMapping("/callbackCode")
	public Object toLogin(HttpServletRequest request) throws OAuthProblemException {

		String code = request.getParameter("code");

		System.out.println("==> 服务端回调，获取的code：" + code);
		
		String result = getTokenByHttpPost(code);
		JSONObject jsonObject = JSON.parseObject(result);
        String accessToken  = jsonObject.getString("access_token");
        String refreshToken = jsonObject.getString("refresh_token");
		
		return "redirect:/getUserInfo?accessToken=" + accessToken+"&refreshToken="+refreshToken;


	}
	
	private String getTokenByOAuthClientRequest(String code) throws OAuthProblemException {
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

		try {
			
		//	HttpPost request=new HttpPost(accessTokenUrl);
		//	System.out.println("保存的access_token="+access_token);

			OAuthClientRequest accessTokenRequest = OAuthClientRequest.tokenLocation(accessTokenUrl)
					.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(clientId).setClientSecret(clientSecret)
					.setCode(code).setRedirectURI(redirectUrl)
					.buildQueryMessage();
			System.out.println(oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST,OAuthJSONAccessTokenResponse.class));
			// 去服务端请求access token，并返回响应
			//OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);
			OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST,OAuthJSONAccessTokenResponse.class);

			// 获取服务端返回过来的access token
			String accessToken = oAuthResponse.getAccessToken();
			// 查看access token是否过期
			Long expiresIn = oAuthResponse.getExpiresIn();
			System.out.println(
					"==> 客户端根据 code值 " + code + " 到服务端获取的access_token为：" + accessToken + " 过期时间为：" + expiresIn);

			System.out.println("==> 拿到access_token然后重定向到 客户端 /oauth-client/getUserInfo服务,传过去accessToken");



		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	private String getTokenByHttpPost(String code) {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		String result = "";
        HttpPost httpPost = new HttpPost(accessTokenUrl);       // 拼接参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("code", code));
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        list.add(new BasicNameValuePair("client_id", clientId));
        list.add(new BasicNameValuePair("client_secret", clientSecret));
        list.add(new BasicNameValuePair("redirect_uri", redirectUrl));
        System.out.println("==== 提交参数 ======" + list);
        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            System.out.println("========HttpResponseProxy：========" + statusCode);
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                System.out.println("========接口返回=======" + result);
            }
            EntityUtils.consume(entity);
	          
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;  
	}
	

}
