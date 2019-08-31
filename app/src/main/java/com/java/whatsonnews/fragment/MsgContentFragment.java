package com.java.whatsonnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.content.Intent;
import android.os.AsyncTask;

import com.bumptech.glide.Glide;
import com.java.whatsonnews.NewsDetail;
import com.java.whatsonnews.R;
import com.java.whatsonnews.RequestNews;
import com.java.whatsonnews.News;
import com.java.whatsonnews.adapter.NewsPreviewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;


import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;


/**
 * 消息内容页
 */


public class MsgContentFragment extends Fragment {
    @BindView(R.id.news_list)
    PullToRefreshListView tvContent;

    private String name;
    private NewsPreviewAdapter mAdapter;
    private RequestNews rn;

    private class FinishRefresh extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
//          adapter.notifyDataSetChanged();
            tvContent.onRefreshComplete();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        name = bundle.getString("name");
        if (name == null) {
            name = "参数非法";
        }
    }

    private void saveNewsInDatabase(JSONObject tmp){
        News newNews=new News();
        newNews.setCategory(tmp.getString("category"));
        newNews.setContent(tmp.getString("content"));
        newNews.setTitle(tmp.getString("title"));
        newNews.setNewsID(tmp.getString("newsID"));
        newNews.setImgPath(tmp.getString("imagePath"));
        newNews.save();
    }

    private LinkedList<News> loadNewsOnline(){
        String url="https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2019-07-29&endDate=2019-08-30";
        if(!name.equals("首页"))
            url+="&categories="+name;
        rn=new RequestNews(url);
        JSONArray newsArray=rn.getNewsArray();
        LinkedList<News> listItem=new LinkedList<>();
        System.out.println(newsArray.size());
        for(int i=0;i<Integer.min(10,newsArray.size());i++) {
            JSONObject tmp = (JSONObject) newsArray.get(i);
            News item=new News();
            item.setTitle(tmp.getString("title"));
            item.setCategory(tmp.getString("category"));
            item.setContent(tmp.getString("content"));
            item.setNewsID(tmp.getString("newsID"));
            String imageArray=tmp.getString("image");
            imageArray=imageArray.substring(1,imageArray.length()-1);
            imageArray=imageArray.replaceAll(" ","");
            String[] imgArray=imageArray.split(",");
            String imagePathAll="";
            for(int j=0;j<imgArray.length;j++){
                String imagePath=(String)imgArray[j];
                imagePathAll+=imagePath+";";
            }
            item.setImgPath(imagePathAll);
            listItem.add(item);
            tmp.put("imagePath",imagePathAll);
            //saveNewsInDatabase(tmp); 网络运行调试中不进行本地保存，以防磁盘占用
        }
        return listItem;
    }

    private LinkedList<News> loadNewsOffline(){
        List<News> queryNews=null;
        if(name=="首页"){
            queryNews=News.listAll(News.class);
        }
        else{
            queryNews=News.findWithQuery(News.class,"Select * from News where category = ?",name);
        }
        if(queryNews.size()==0)
            return null;
        else
            return (LinkedList<News>) queryNews;
    }

    private List<News> loadNews(){
        List<News> listItem=null;
        listItem=loadNewsOffline();
        if(listItem==null)
            return loadNewsOnline();
        else
            return listItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_msg_content, container, false);
        ButterKnife.bind(this, view);
        final LinkedList<News> newsList=loadNewsOnline();//loadNews(); 调试网络接口时先不使用本地存储功能
        //SimpleAdapter myAdapter = new SimpleAdapter(getContext(), newsList, R.layout.news_item, new String[]{"title", "category", "preview","imagePath"}, new int[]{R.id.news_title, R.id.news_category, R.id.news_preview,R.id.news_img});
        // 为了实现多种预览方式，不使用SimpleAdapter
        /*myAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String s) {
                if(view instanceof ImageView && data instanceof String){
                    ImageView view1=(ImageView)view;
                    String data_new=null;
                    try{
                        data_new=data.toString().split(";")[0];
                        System.out.println("需要访问的URL为："+data_new);
                    }catch(Exception e){data="";}
                    Glide.with(getContext())
                            .load(data_new)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(view1);
                    return true;
                }
                return false;
            }
        });*/
        mAdapter=new NewsPreviewAdapter(getContext(),newsList);
        tvContent.setAdapter(mAdapter);
        tvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                bundle.putString("title",(String)newsList.get(i-1).getTitle());
                bundle.putString("category",(String)newsList.get(i-1).getCategory());
                bundle.putString("content",(String)newsList.get(i-1).getContent());
                bundle.putString("imagePath",(String)newsList.get(i-1).getImgPath());
                bundle.putString("newsID",(String)newsList.get(i-1).getNewsID());
                Intent intent=new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), NewsDetail.class);
                startActivity(intent);
            }
        });

        tvContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(getContext(),"Refreshing!",Toast.LENGTH_SHORT).show();
                new FinishRefresh().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(getContext(),"Find more!",Toast.LENGTH_SHORT).show();
                new FinishRefresh().execute();
            }

        });
        return view;
    }

}
