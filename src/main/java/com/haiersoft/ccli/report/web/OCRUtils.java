package com.haiersoft.ccli.report.web;

import com.alibaba.fastjson.JSONObject;
import com.haiersoft.ccli.platform.web.VehicleQueueController;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class OCRUtils {
    private static final Logger logger = LoggerFactory.getLogger(OCRUtils.class);

    public static final String API_KEY_STR = "3HHaLDGWrn7aim103NDugde6";
    public static final String SECRET_KEY_STR = "cfURiYfdOrA1oXf6Hh4ZJ8XGnerKIU2R";
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static String OCRPdf(byte[] bytes,String API_KEY,String SECRET_KEY) throws IOException {
        if (API_KEY == null || API_KEY.trim().length() == 0){
            API_KEY = API_KEY_STR;
        }
        if (SECRET_KEY == null || SECRET_KEY.trim().length() == 0){
            SECRET_KEY = SECRET_KEY_STR;
        }
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String accesstoken = new org.json.JSONObject(response.body().string()).getString("access_token");

        MediaType mediaType2 = MediaType.parse("application/x-www-form-urlencoded");
        // pdf_file 可以通过 getFileContentAsBase64("C:\fakepath\HDMUTAOA06498900B.pdf") 方法获取,如果Content-Type是application/x-www-form-urlencoded时,第二个参数传true
        RequestBody body2 = RequestBody.create(mediaType2, "pdf_file="+getFileContentAsBase64(bytes)+"&cell_contents=false&return_excel=false");
        Request request2 = new Request.Builder()
                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/table?access_token=" + accesstoken)
                .method("POST", body2)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .build();
        Response response2 = HTTP_CLIENT.newCall(request2).execute();
        String string = response2.body().string();
        response2.close();

        //备案号
        String bah = string.substring(string.indexOf("备案号")+3,string.indexOf("备案号")+30);
        bah = bah.substring(0,bah.indexOf("\""));
        bah = bah.replace("\\n","");
        logger.info("报关单备案号==>> "+bah);
        //提运单号
        String tydh = string.substring(string.indexOf("提运单号")+4,string.indexOf("提运单号")+40);
        tydh = tydh.substring(0,tydh.indexOf("\""));
        tydh = tydh.replace("\\n","");
        logger.info("报关单提运单号==>> "+tydh);
        //监管方式
        String jgfs = "";
        int a1= string.indexOf("监管方式");
        if (a1 > 0){
            a1 = a1 + 6;
            int b1 = string.indexOf("监管方式")+30;
            jgfs = string.substring(a1,b1);
            jgfs = jgfs.substring(jgfs.indexOf(")")+1,jgfs.indexOf("\""));
            jgfs = jgfs.replace("\\n","");
            logger.info("报关单监管方式==>> "+jgfs);
        }
        //核注清单号
        String hzqdh = "";
        int a2 = string.indexOf("保税核注清单");
        if (a2 > 0){
            a2 = a2 + 6;
            int b2 = string.indexOf("保税核注清单")+30;
            hzqdh = string.substring(a2,b2);
            hzqdh = hzqdh.substring(0,hzqdh.indexOf("随附单证"));
            if (hzqdh.trim().length() > 9){
                if ("1".equals(hzqdh.substring(8,9))){
                    String hzqdhTemp1 = hzqdh.substring(0,8);
                    String hzqdhTemp2 = hzqdh.substring(9,hzqdh.length());
                    hzqdh = hzqdhTemp1 + "I" + hzqdhTemp2;
                }
            }
        }
        logger.info("报关单保税核注清单==>> "+hzqdh);
        String str = "success,"+bah+","+tydh+","+hzqdh+","+jgfs;
//        String str = "success,T4230W000036,EGLV751300029888,QD423023I000000942,区内物流货物";
        return str;
    }

    static String getFileContentAsBase64(byte[] bytes) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        if (true) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }
}
