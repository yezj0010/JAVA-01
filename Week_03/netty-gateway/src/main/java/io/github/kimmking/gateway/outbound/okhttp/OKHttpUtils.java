package io.github.kimmking.gateway.outbound.okhttp;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * created by DengJin on 2021/1/22 16:49
 */
public class OKHttpUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static String post(String url, Map data){
        return post(url, JSONObject.toJSONString(data));
    }

    public static String post(String url, String data){
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println("result="+result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getResultString(String url){
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            System.out.println("result="+result);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getResultBytes(String url){
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
//            System.out.println("result="+body);
            return body.bytes();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
