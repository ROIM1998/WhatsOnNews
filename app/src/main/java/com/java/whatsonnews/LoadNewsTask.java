package com.java.whatsonnews;


import android.os.AsyncTask;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class LoadNewsTask extends AsyncTask<String,Void, JSONObject>{
    private JSONObject container;

    public LoadNewsTask(JSONObject container){
        this.container=container;
    }

    @Override
    protected JSONObject doInBackground(String... params){
        URL apiUrl=null;
        InputStream inputStream=null;
        String newsRawURL=params[0];
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
        parseRequest= JSON.parseObject(jsonRequest);
        return parseRequest;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        container=jsonObject;
    }
}
