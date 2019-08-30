package com.java.whatsonnews;

import java.io.*;
import java.net.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;



public class RequestNews {

    private String newsRawURL;

    public RequestNews(String url){
        this.newsRawURL=url;
    }

    public JSONObject getNews(){
        int index = newsRawURL.indexOf("?");
        String resultURL = newsRawURL.substring(0,index+1);
        String temp = newsRawURL.substring(index+1);
        try {
            //URLEncode转码会将& ： / = 等一些特殊字符转码,(但是这个字符  只有在作为参数值  时需要转码;例如url中的&具有参数连接的作用，此时就不能被转码)
            String encode = URLEncoder.encode(temp, "utf-8");
            System.out.println(encode);
            encode = encode.replace("%3D",  "=");
            encode = encode.replace("%2F", "/");
            encode = encode.replace("+", "%20");
            encode = encode.replace("%26", "&");
            resultURL += encode;
            System.out.println("转码后的url:"+resultURL);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject parseRequest=null;
        String jsonRequest="";
        String tmp;
        try{
            URL newsAPI = new URL(resultURL);
            URLConnection tc=newsAPI.openConnection();
            BufferedReader br=new BufferedReader(new InputStreamReader(tc.getInputStream()));
            while((tmp=br.readLine())!=null)
                jsonRequest+=tmp;
            //System.out.println(jsonRequest);
        }catch(MalformedURLException e){e.printStackTrace();System.out.println("URL Error!");}
        catch(IOException e){e.printStackTrace();System.out.println("IO Exception Occured!");}
        //System.out.println("获取到的原字符串为:"+jsonRequest);
        parseRequest=JSON.parseObject(jsonRequest);
        return parseRequest;
    }
    public JSONArray getNewsArray(){
        JSONObject parseRequest=this.getNews();
        return parseRequest.getJSONArray("data");
    }
    public static void main(String args[]){
        /*RequestNews rn=new RequestNews("https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2019-07-01&endDate=2019-07-03&words=特朗普&categories=科技");
        JSONObject news=rn.getNews();
        JSONArray newsArray=news.getJSONArray("data");
        System.out.println(((JSONObject)newsArray.get(0)).getString("title"));
        System.out.println(((JSONObject)newsArray.get(0)).getString("category"));
        System.out.println(((JSONObject)newsArray.get(0)).getString("content").substring(0,50)+"...");
        //System.out.println(news.toString());
        //*/
        String test="http://cms-bucket.ws.126.net/2019/08/28/7cd61accae58486ba309aa09e0b69df0.png;http://crawl.ws.126.net/e22a924d50e45a100b772ec744cf71ee.jpg;";
        System.out.println(test.toString().split(";")[0]);
    }
}
