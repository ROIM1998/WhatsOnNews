package com.java.whatsonnews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoadImageTask extends AsyncTask<String,Void,Bitmap>{
    private ImageView imageView;

    public LoadImageTask(ImageView imageview){
        this.imageView=imageview;
    }

    @Override
    protected Bitmap doInBackground(String... params){
        URL imageUrl=null;
        Bitmap bitmap=null;
        InputStream inputStream=null;
        try{
            imageUrl=new URL(params[0]);
            HttpURLConnection conn=(HttpURLConnection)imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            inputStream=conn.getInputStream();
            bitmap=BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }catch(Exception e){e.printStackTrace();}
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }
}
