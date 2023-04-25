package com.haiersoft.ccli.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.api.entity.ResponseVo;
import com.haiersoft.ccli.api.util.SHA256withRSA;

@Aspect
@Component
public class ResquestAspect {
	private static final Logger logger = LoggerFactory.getLogger(ResquestAspect.class);
	
	@Pointcut("@annotation(com.haiersoft.ccli.api.aspect.ApiPledgedCheck)")
	public void pointcut() {
	}
	
	//@Around("execution(* com.haiersoft.ccli.api.web.*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        //System.out.println(pjp.getArgs().toString());
        //Object[] args = pjp.getArgs();
        logger.error("接口接收到的参数：");
        logger.error( JSON.toJSONString(pjp.getArgs()) );
      
        logger.error("----------------------------------");
        Object object = pjp.proceed();     

        return object;
    }
    
    @Value("${checkSign}")
    private Boolean checkSign;
	
	@Around("pointcut()")
    public Object checkSing(ProceedingJoinPoint pjp) throws Throwable {

        logger.error("接口接收到的参数：");
        logger.error( JSON.toJSONString(pjp.getArgs()) );
        
        if (!checkSign) {
        	return pjp.proceed();       	
        }
        //验证签名开启
        String strReqParam = JSON.toJSONString(pjp.getArgs());
        JSONArray jsonArray = JSONObject.parseArray(strReqParam);
        JSONObject json = jsonArray.getJSONObject(0);
        String sign = json.getString("signature");
        if(StringUtils.isEmpty(sign)) {
        	return ResponseVo.warn("signature不存在或者为空！");
        }
        String regex = "([\\[\\]{}\"])";
        int index = strReqParam.indexOf(",\"signature\"");
        String doParam = strReqParam.substring(0,index).replaceAll(regex, "");
        System.out.println(doParam.replaceAll(regex, ""));

        logger.error("----------------------------------");

        try {
        	boolean ch = SHA256withRSA.verifySign(doParam, sign);
            if(!ch) {
            	return ResponseVo.warn("签名验证有误，请检查");
            }
        }catch(Exception ex) {
        	ex.printStackTrace();
        	return ResponseVo.warn("签名验证有误，请检查");
        }

        return pjp.proceed();
    }

}
