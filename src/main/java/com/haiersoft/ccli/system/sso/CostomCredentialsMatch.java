package com.haiersoft.ccli.system.sso;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

public class CostomCredentialsMatch extends HashedCredentialsMatcher {
	
    private Cache<String, AtomicInteger> passwordRetryCache;
    
    public CostomCredentialsMatch (CacheManager cacheManager) {
        this.passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    	
        //增加免密登录功能，使用自定义token
        CustomToken usertoken = (CustomToken) token;
        
        //免密登录,不验证密码
        if (LoginType.NOPASSWD.equals(usertoken.getType())){
            return true;
        }
        
        String loginID = usertoken.getUsername();
        
        AtomicInteger retryTimes = passwordRetryCache.get(loginID);
        if (retryTimes == null) {
            retryTimes = new AtomicInteger(0);
            passwordRetryCache.put(loginID, retryTimes);
        }
        if (retryTimes.incrementAndGet() > 5) {
            throw new ExcessiveAttemptsException();
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches)
            passwordRetryCache.remove(loginID);
 
        return matches;
    }
}
