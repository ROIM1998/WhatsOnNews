package com.java.whatsonnews;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class NewsDetail extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.news_detail_list);
        setContentView(R.layout.news_item_detail);
        Bundle bundle=getIntent().getExtras();
        String title=bundle.getString("title");
        String category=bundle.getString("category");
        String content=bundle.getString("content");
        String imagePathAll=bundle.getString("imagePath");
        String[] imagePath=imagePathAll.split(";");
        TextView tvTitle=(TextView)findViewById(R.id.news_detail_title);
        TextView tvCategory=(TextView)findViewById(R.id.news_detail_category);
        content.replaceAll("\u3000"," ");
        String[] contentLines=content.split("\n");



    }
}
