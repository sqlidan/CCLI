package com.haiersoft.ccli.system.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.haiersoft.ccli.common.utils.PropertiesUtil;

/**
 * 登录controller
 *
 * @author ty
 * @date 2015年1月14日
 */
@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private CookieLocaleResolver resolver;

    /**
     * 默认页面
     *
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, String language) {

        if (language == null || language.equals("")) {

            Subject subject = SecurityUtils.getSubject();

            if (subject.isAuthenticated() || subject.isRemembered()) {
                return "system/login";
            }

        } else {

            resolver.removeCookie(response);

            if (language.equals("zh_CN")) {
                resolver.setLocale(request, response, Locale.CHINA);
            } else if (language.equals("en")) {
                resolver.setLocale(request, response, Locale.ENGLISH);
            } else {
                resolver.setLocale(request, response, Locale.CHINA);
            }

        }

        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated() || subject.isRemembered()) {
            return "system/login";
        }

        return "system/login";
    }

    /**
     * 登录失败
     *
     * @param userName
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
        model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
        return "system/login";
    }

    /**
     * 登出
     *
     * @param userName
     * @param model
     * @return
     */
	
	private String ssoLogoutUrl = PropertiesUtil.getPropertiesByName("sso.ssoLogoutUrl", "sso"); 

    @RequestMapping(value = "logout")
    public String logout(Model model) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        //return "system/login";
        return "redirect:"+ssoLogoutUrl;
    }

}
