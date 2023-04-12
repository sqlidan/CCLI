package com.haiersoft.ccli.system.sso;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import com.chenay.lh.sso.client.rest.pojo.dto.LhToken;
//import com.chenay.lh.sso.client.rest.pojo.dto.OauthUserProfile;
import com.haiersoft.ccli.common.utils.PropertiesUtil;
import com.haiersoft.ccli.system.entity.User;
import com.haiersoft.ccli.system.service.UserService;

/**
 * @author: 
 * @date: 
 * @description: 通过access_token获取用户信息
 */
@Controller
@RequestMapping("/")
public class SSOAuthUserController {

    private String userInfoUrl=PropertiesUtil.getPropertiesByName("sso.userInfoUrl", "sso"); 
    private String redirectUrl = PropertiesUtil.getPropertiesByName("sso.redirectUrl", "sso"); 

    //private LhToken lhToken=new LhToken();
    
    //接受服务端传回来的access token，由此token去请求服务端的资源（用户信息等）
    @RequestMapping("getUserInfo")
    public String getUserInfo(HttpServletResponse resonse,String accessToken,String refreshToken,Model model) {
    	System.out.println("accessToken is ::: "+ accessToken);
    	System.out.println("refreshToken is ::: "+ refreshToken);
		OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
		try {
			OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(userInfoUrl).setAccessToken(accessToken)
					.buildQueryMessage();

			OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			String body = resourceResponse.getBody();
			System.out.println("SSOAuthUserController ==> 客户端通过accessToken：" + accessToken + "  从服务端获取用户信息为：" + body);
			JSONObject jsonObject = JSON.parseObject(body);
	        String username = jsonObject.getString("username");
	        model.addAttribute("accessToken", accessToken);
	        
			return loginInto(username,accessToken,refreshToken,body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
    }
    
    @Autowired
    UserService userService;
    //登录方法，进行页面跳转
    private String loginInto(String workNo,String accessToken,String refreshToken,String body) throws Exception {		
		//username = "superadmin";wrok
    	//User user = userService.getUserByWorkno("68050");
    	User user = userService.getUserByWorkno(workNo);
    	
    	//如果用户不存在时,返回oops页面
    	if(null==user) {
    		return "system/oops";
    	}
    	
        //单点登录redis处理
		//lhToken.setAccessToken(accessToken);
		//lhToken.setRefreshToken(refreshToken);
		//OauthUserProfile oauthUserProfile = JSON.parseObject(body, OauthUserProfile.class);
		//lhToken.setAttributes(oauthUserProfile);
        //List<NameValuePair> nvps=new ArrayList<>();
		//nvps.add(new BasicNameValuePair("info", JSON.toJSONString(lhToken)));
		// 测试地址
		//String doPost = HttpUtil.doPost("http://10.135.252.115:81/lh-system/saveToken", nvps);
		//System.out.println("doPost "+doPost);
    	
    	//
    	
	    Subject subject = SecurityUtils.getSubject();
	    //增加免密登录功能，使用自定义token
	   // CustomToken token = new CustomToken(user.getLoginName());
	    try{
		   // subject.login(token);
	    }catch(Exception ex)
	    {
	    	
	    	System.out.println(ex.toString());
	    	//return "redirect:/logout";
	    	return "system/login";
	    }

        return "system/indexauth";    //这里是要跳转的页面

    }
    
	/*
	 * @RequestMapping("/logout") public String logout() { String logout=
	 * PropertiesUtil.getPropertiesByName("sso.logout", "sso");
	 * System.out.println("cclisso ~ logout"); Subject subject =
	 * SecurityUtils.getSubject(); subject.logout(); return "redirect:"+logout; }
	 */

}
