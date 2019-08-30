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
        TextView tvContent=(TextView)findViewById(R.id.news_detail_content);
        tvTitle.setText(title);
        tvCategory.setText(category);
        tvContent.setText(content);
        if(imagePath.length!=0){
            ImageView imageView=new ImageView(this);
            System.out.println("添加View: "+imageView.toString());
            //imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            //new LoadImageTask(imageView).execute(imagePath[0]);
            imageView.setImageResource(R.drawable.orange_main);
            linearLayout.addView(imageView);
        }
        else{
            TextView textView=new TextView(this);
            //textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setText("没有图片！");
            linearLayout.addView(textView);
        }
    }
}
