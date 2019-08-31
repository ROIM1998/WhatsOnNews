package com.java.whatsonnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.java.whatsonnews.News;
import com.java.whatsonnews.R;

import org.w3c.dom.Text;

import java.util.LinkedList;

public class NewsPreviewAdapter extends BaseAdapter {
    private LinkedList<News> mNews;
    private Context mContext;
    private static final int TYPE_NO_IMG=0;
    private static final int TYPE_SMALL_IMG=1;

    public NewsPreviewAdapter(Context mContext,LinkedList<News> mNews){
        this.mContext=mContext;
        this.mNews=mNews;
    }

    @Override
    public int getCount(){
        return mNews.size();
    }

    @Override
    public Object getItem(int position){
        return mNews.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemViewType(int position){
        if(mNews.get(position).getImgPath().equals(";"))
            return TYPE_NO_IMG;
        else
            return TYPE_SMALL_IMG;
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int type=getItemViewType(position);
        ViewHolderSmallImage holderSmallImage=null;
        ViewHolderNoImage holderNoImage=null;
        if(convertView==null){
            switch(type){
                case TYPE_NO_IMG:
                    holderNoImage=new ViewHolderNoImage();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.news_item_no_image,parent,false);
                    holderNoImage.titleView=(TextView)convertView.findViewById(R.id.news_title_no_image);
                    holderNoImage.categoryView=(TextView)convertView.findViewById(R.id.news_category_no_image);
                    holderNoImage.previewView=(TextView)convertView.findViewById(R.id.news_preview_no_image);
                    convertView.setTag(R.id.tag_no_image,holderNoImage);
                    break;
                case TYPE_SMALL_IMG:
                    holderSmallImage=new ViewHolderSmallImage();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.news_item,parent,false);
                    holderSmallImage.titleView=(TextView)convertView.findViewById(R.id.news_title);
                    holderSmallImage.categoryView=(TextView)convertView.findViewById(R.id.news_category);
                    holderSmallImage.previewView=(TextView)convertView.findViewById(R.id.news_preview);
                    holderSmallImage.imgIcon=(ImageView)convertView.findViewById(R.id.news_img);
                    convertView.setTag(R.id.tag_small_image,holderSmallImage);
            }
        }else{
            switch(type){
                case TYPE_NO_IMG:
                    holderNoImage=(ViewHolderNoImage)convertView.getTag(R.id.tag_no_image);
                    break;
                case TYPE_SMALL_IMG:
                    holderSmallImage=(ViewHolderSmallImage)convertView.getTag(R.id.tag_small_image);
                    break;
            }

            News news=mNews.get(position);
            switch(type){
                case TYPE_NO_IMG:
                    if(news!=null){
                        holderNoImage.titleView.setText(news.getTitle());
                        holderNoImage.categoryView.setText(news.getCategory());
                        String content=news.getContent();
                        if(content.length()>50)
                            content=content.substring(0,50)+"...";
                        holderNoImage.previewView.setText(content);
                    }
                    break;
                case TYPE_SMALL_IMG:
                    if(news!=null){
                        holderSmallImage.titleView.setText(news.getTitle());
                        holderSmallImage.categoryView.setText(news.getCategory());
                        String content=news.getContent();
                        if(content.length()>50)
                            content=content.substring(0,50)+"...";
                        holderSmallImage.previewView.setText(content);
                        Glide.with(mContext)
                                .load(news.getImgPath().split(";")[0])
                                .placeholder(R.drawable.ic_launcher_background)
                                .error(R.drawable.ic_launcher_background)
                                .into(holderSmallImage.imgIcon);
                    }
                    break;
            }
        }
        return convertView;
    }

    private static class ViewHolderSmallImage{
        ImageView imgIcon;
        TextView titleView;
        TextView categoryView;
        TextView previewView;
    }

    private static class ViewHolderNoImage{
        TextView titleView;
        TextView categoryView;
        TextView previewView;
    }
}
