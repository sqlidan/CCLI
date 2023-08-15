package com.haiersoft.ccli.wms.web.preEntry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @Author chenp
 * @Description Http接口工具类
 * @Date 13:27 2020/11/16
 **/
public class HttpUtils {

    /**
     * @return java.lang.String
     * @Author chenp
     * @Description post请求
     * @Date 13:29 2020/11/16
     * @Param [url, nvps]
     **/
    public static String doPost(String url, List<NameValuePair> nvps)
            throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //以post方式请求网页http://www.yshjava.cn
            HttpPost httppost = new HttpPost(url);
            //添加HTTP POST参数
            System.out.println("----------------------------------------");
//	            System.out.println(responseBody);
            System.out.println("----------------------------------------");
            //将POST参数以UTF-8编码并包装成表单实体对象
            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            //打印请求地址
            System.out.println("executing request " + httppost.getRequestLine().getUri());
            //创建响应处理器处理服务器响应内容
//	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //执行请求并获取结果
            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();
            String responseBody = new String(EntityUtils.toString(entity).getBytes("utf-8"), "UTF-8");
//	            String responseBody = httpclient.execute(httppost, responseHandler);
            // responseBody = new String(responseBody.getBytes(),"utf-8");
            return responseBody;
        } finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpclient.getConnectionManager().shutdown();
        }
    }


    /**
     * @return java.lang.String
     * @Author chenp
     * @Description 带请求头的get请求
     * @Date 13:08 2020/11/16
     * @Param [url, param, headers]
     **/
    public static String doGetWithHeads(String url, String param, Map<String, String> headers) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {// 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }


    /**
     * @return java.lang.String
     * @Author chenp
     * @Description 普通get请求
     * @Date 13:28 2020/11/16
     * @Param [url, param, headers]
     **/
    public static String doGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        } finally {// 使用finally块来关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String HttpPostWithJson(String url, String json) {
        String returnValue = "这是默认返回值，接口调用失败";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法

        } catch (Exception e) {
            e.printStackTrace();
            returnValue=e.getMessage();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }


}
