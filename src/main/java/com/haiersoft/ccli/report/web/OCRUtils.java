package com.haiersoft.ccli.report.web;

import okhttp3.*;
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

//    @RequestMapping(value = "OCRPdf", method = RequestMethod.POST)
//    @org.springframework.web.bind.annotation.ResponseBody
//    public Map<String,String> OCRPdf(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IOException {
//        byte[] bytes = file.getBytes();
//        Map<String,String> result =  OCRUtils.OCRPdf(bytes);
//        return result;
//    }

    public static final String API_KEY = "3HHaLDGWrn7aim103NDugde6";
    public static final String SECRET_KEY = "cfURiYfdOrA1oXf6Hh4ZJ8XGnerKIU2R";
    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static Map<String,String> OCRPdf(byte[] bytes) throws IOException {
        Map<String,String> result = new HashMap<>();
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
        System.out.println("bah==>> "+bah);
        //提运单号
        String tydh = string.substring(string.indexOf("提运单号")+4,string.indexOf("提运单号")+40);
        tydh = tydh.substring(0,tydh.indexOf("\""));
        tydh = tydh.replace("\\n","");
        System.out.println("tydh==>> "+tydh);
        //核注清单号
        String hzqdh = "";
        int a = string.indexOf("核注清单号");
        if (a > 0){
            a = a + 5;
            int b = string.indexOf("核注清单号")+50;
            hzqdh = string.substring(a,b);
            hzqdh = hzqdh.substring(0,hzqdh.indexOf("\""));
            hzqdh = hzqdh.replace("\\n","");
        }
        System.out.println("hzqdh==>> "+hzqdh);

        result.put("bah",bah);
        result.put("tydh",tydh);
        result.put("hzqdh",hzqdh);
        return result;
    }

    static String getFileContentAsBase64(byte[] bytes) throws IOException {
        String base64 = Base64.getEncoder().encodeToString(bytes);
        if (true) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }
}
