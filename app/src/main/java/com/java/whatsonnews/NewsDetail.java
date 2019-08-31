package com.java.whatsonnews;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.LinkedList;
import java.util.List;

public class NewsDetail extends Activity{

    private List<String> words=null;
    private Context mContext;
    //private NewsDetailAdapter mAdapter=null;
    private LinearLayout list_words;

    private void setLayout(){
        for(int i=0;i<words.size();i++){
            String word=words.get(i);
            if(word.startsWith("http")){
                ImageView iv=new ImageView(this);
                /*ViewGroup.LayoutParams lp=iv.getLayoutParams();
                lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
                iv.setLayoutParams(lp);*/
                Glide.with(mContext)
                        .load(word)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(iv);
                list_words.addView(iv);
            }
            else{
                TextView tv=new TextView(this);
                /*ViewGroup.LayoutParams lp=tv.getLayoutParams();
                lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.width= ViewGroup.LayoutParams.WRAP_CONTENT;
                tv.setLayoutParams(lp);*/
                System.out.println("调用了文本："+tv.getVisibility());
                tv.setText(word);
                list_words.addView(tv);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_item_detail);
        mContext=NewsDetail.this;
        Bundle bundle=getIntent().getExtras();
        String title=bundle.getString("title");
        String category=bundle.getString("category");
        String content=bundle.getString("content");
        String imagePathAll=bundle.getString("imagePath");
        String[] imagePath=imagePathAll.split(";");
        TextView tvTitle=(TextView)findViewById(R.id.news_detail_title);
        TextView tvCategory=(TextView)findViewById(R.id.news_detail_category);

        tvTitle.setText(title);
        tvCategory.setText(category);

        //content.replaceAll("\u3000"," ");
        String[] contentLines=content.split("\n");
        list_words=(LinearLayout) findViewById(R.id.news_detail);
        words=new LinkedList<String>();
        if(imagePath.length!=0){
            int seperating=Integer.max(contentLines.length/2/imagePath.length,2);
            int i=1,j=0;
            words.add(contentLines[0]);
            while(i<contentLines.length/2&&j<imagePath.length){
                if((i+j)%seperating==0&&j<imagePath.length){
                    words.add(imagePath[j]);
                    j++;
                }else{
                    words.add(contentLines[i]);
                    i++;
                }
            }
            for(;i<contentLines.length;i++)
                words.add(contentLines[i]);
        }else{
            for(int i=0;i<contentLines.length;i++)
                words.add(contentLines[i]);
        }
        setLayout();
        //mAdapter=new NewsDetailAdapter(words,mContext);
        //list_words.setAdapter(mAdapter);
        //.notifyDataSetChanged();
    }
}
