package com.haiersoft.ccli.common.utils;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class HttpGo {

    private static final Gson gson = new Gson();

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json");

    private static OkHttpClient HTTPCLIENT;

    @SuppressWarnings("rawtypes")
	private ResponseCallback responseCallback;

    public HttpGo() {
        HTTPCLIENT = new OkHttpClient();
    }

    public String sendRequest(String url, Map<String, String> params) {
        RequestBody body = createRequestBody(params);
        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String sendRequestHeader(String url, String params) {
        RequestBody body = createRequestBody(params);
        // okHttp请求
        Headers headers =null;
        Headers.Builder headerBuilder= new Headers.Builder();
        headerBuilder.set("APPID","7e86aa901e86de01");
/*
        headerBuilder.set("Fmp-Tenant-Data-Node","080013");
*/
        headers = headerBuilder.build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    public String sendRequestOneHeader(String url) {

        // okHttp请求
        Headers headers =null;
        Headers.Builder headerBuilder= new Headers.Builder();
        headerBuilder.set("APPID","7e86aa901e86de01");
/*
        headerBuilder.set("Fmp-Tenant-Data-Node","080013");
*/
        headers = headerBuilder.build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public String sendRequest(String url, String params) {
        RequestBody body = createRequestBody(params);
        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }




    /**
     * 带head的请求
     * @param url
     * @param headersMap
     * @param params
     * @return
     */
    public String sendRequest(String url, Map<String,String> headersMap,String params) {
        //构建请求head中的参数
        Headers headers = null;
        Headers.Builder headerBuilder= new Headers.Builder();
        if(headersMap !=null &&headersMap.size()>0){
            for(String key:headersMap.keySet()){
                //如果参数不是空，就拼起来
                if(headersMap.get(key)!=null){
                    headerBuilder.add(key,headersMap.get(key));
                }
            }
        }
        headers = headerBuilder.build();
        //构建请求body
        RequestBody body = createRequestBody(params);
        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    /**
     * 带head的请求
     * @param url
     * @param headersMap
     * @param params
     * @return
     */
    public String sendRequestHead(String url, Map<String,String> headersMap,String params) {
        //构建请求head中的参数
        Headers headers =null;
        Headers.Builder headerBuilder= new Headers.Builder();
        headerBuilder.set("APPID","7e86aa901e86de01");
        headerBuilder.set("Fmp-Tenant-Data-Node","080013");
        if(headersMap !=null &&headersMap.size()>0){
            for(String key:headersMap.keySet()){
                //如果参数不是空，就拼起来
                if(headersMap.get(key)!=null){
                    headerBuilder.add(key,headersMap.get(key));
                }
            }
        }
        headers = headerBuilder.build();
        //构建请求body
        RequestBody body = createRequestBody(params);
        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .post(body)
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public String getRequest(String url, String params) {
        // okHttp GET请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url+params)
                .get()
                .build();
        Call call = HTTPCLIENT.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    @SuppressWarnings("rawtypes")
	public void sendRequest(String url, String params, final ResponseCallback callback) {

        this.responseCallback = callback;

        // 请求提
        RequestBody body = createRequestBody(params);

        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();

        // 生成新请求
        Call call = HTTPCLIENT.newCall(request);

        // 执行网络请求
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    @SuppressWarnings("rawtypes")
	public void sendRequest(String url, Map<String, String> params, final ResponseCallback callback) {

        this.responseCallback = callback;

        // 请求提
        RequestBody body = createRequestBody(params);

        // okHttp请求
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 生成新请求
        Call call = HTTPCLIENT.newCall(request);

        // 执行网络请求
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                responseCallback.onFailure(e);
            }

            @SuppressWarnings("unchecked")
			@Override
            public void onResponse(Call call, Response response) throws IOException {

                Type type = getSuperclassTypeParameter(responseCallback.getClass());
                String result = response.body().string();

                if (type == String.class) {
                    responseCallback.onSuccess(result);
                } else {
                    responseCallback.onSuccess(gson.fromJson(result, type));
                }

            }
        });

    }

    /**
     * 构建请求
     */
    protected RequestBody createRequestBody(String params) {
        return RequestBody.create(MEDIA_TYPE_JSON, params);
    }

    /**
     * 构建请求
     */
    protected RequestBody createRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }
            builder.add(key, value);
        }
        return builder.build();
    }

    public static Type getSuperclassTypeParameter(Class<?> clazz) {

        Type superclass = clazz.getGenericSuperclass();

        if (superclass instanceof Class)
            throw new RuntimeException("Missing type parameter.");

        ParameterizedType parameterizedType = (ParameterizedType) superclass;

        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public interface ResponseCallback<T> {
        void onFailure(IOException e);

        void onSuccess(T response);
    }
}
