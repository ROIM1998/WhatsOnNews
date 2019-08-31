package com.java.whatsonnews.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.whatsonnews.R;
import com.java.whatsonnews.fragment.MsgContentFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailAdapter extends BaseAdapter {
    private List<String> words;
    private Context mContext;

    public NewsDetailAdapter(List<String> words,Context mContext){
        this.words=words;
        this.mContext=mContext;
    }

    @Override
    public int getCount(){
        return words.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        convertView= LayoutInflater.from(mContext).inflate(R.layout.news_detail_line,parent,false);
        ImageView iv=(ImageView)convertView.findViewById(R.id.detail_image_line);
        TextView tv=(TextView)convertView.findViewById(R.id.detail_text_line);
        if(words.get(position).startsWith("http")){
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.INVISIBLE);
            Glide.with(mContext)
                    .load(words.get(position))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(iv);
        }
        else
            tv.setText(words.get(position));
        return convertView;
    }
}
