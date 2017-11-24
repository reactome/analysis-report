package com.example.demo.httpclient;//package com.example.demo.httpclient;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by DengChuan on 2017/10/22.
// */
//@Component
//public class HttpClientDemo {
//    public static String httpGet(String url) {
//        InputStream is;
//        BufferedReader br;
//        StringBuilder sBuilder = null;
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
////            HttpPost httpPost = new HttpPost(url);
//            HttpGet httpGet = new HttpGet(url);
//            //方式一：将参数添加到请求体当中
////            httpPost.setEntity(new StringEntity(arg, "utf-8"));//默认是采用ISO-8859-1 对于中文需要使用utf-8编码
//            //方式二: 如果需要传递多个参数
////            List<NameValuePair> parameters = new ArrayList<>();
////            parameters.add(new BasicNameValuePair("name", "value"));
////            httpPost.setEntity(new UrlEncodedFormEntity(parameters));
//            HttpResponse httpResponse = httpClient.execute(httpGet);
//            //连接成功
//            if (200 == httpResponse.getStatusLine().getStatusCode()) {
//                HttpEntity httpEntity = httpResponse.getEntity();
//                is = httpEntity.getContent();
//                br = new BufferedReader(new InputStreamReader(is));
//                String tempStr;
//                sBuilder = new StringBuilder();
//                while ((tempStr = br.readLine()) != null) {
//                    sBuilder.append(tempStr);
//                }
//                br.close();
//                is.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sBuilder == null ? "" : sBuilder.toString();
//    }
//}
