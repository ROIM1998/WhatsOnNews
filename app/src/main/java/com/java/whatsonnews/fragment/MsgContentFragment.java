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

import butterknife.BindView;
import butterknife.ButterKnife;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
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

    private List<Map<String,Object>> loadNewsOnline(){
        String url="https://api2.newsminer.net/svc/news/queryNewsList?size=15&startDate=2019-07-29&endDate=2019-08-30";
        if(!name.equals("首页"))
            url+="&categories="+name;
        rn=new RequestNews(url);
        JSONArray newsArray=rn.getNewsArray();
        List<Map<String,Object>> listItem=new ArrayList<Map<String, Object>>();
        System.out.println(newsArray.size());
        for(int i=0;i<Integer.min(10,newsArray.size());i++) {
            JSONObject tmp = (JSONObject) newsArray.get(i);
            Map<String,Object> item=new HashMap<String,Object>();
            item.put("title",tmp.getString("title"));
            item.put("category",tmp.getString("category"));
            String tmpContent=tmp.getString("content");
            String tmpPreview=null;
                        item.put("newsID",tmp.getString("newsID"));
            if(tmpContent.length()<50)
                tmpPreview=tmpContent;
            else
                tmpPreview=tmpContent.substring(0,50)+"...";
            item.put("preview",tmpPreview);
            String imageArray=tmp.getString("image");
            imageArray=imageArray.substring(1,imageArray.length()-1);
            imageArray=imageArray.replaceAll(" ","");
            String[] imgArray=imageArray.split(",");
            String imagePathAll="";
            for(int j=0;j<imgArray.length;j++){
                String imagePath=(String)imgArray[j];
                imagePathAll+=imagePath+";";
            }
            if(imagePathAll.length()==1)
                tmpContent="【没有图片】"+tmpContent;
            item.put("imagePath",imagePathAll);
            item.put("content",tmpContent);
            listItem.add(item);
            tmp.put("imagePath",imagePathAll);
            //saveNewsInDatabase(tmp); 网络运行调试中不进行本地保存，以防磁盘占用
        }
        return listItem;
    }

    private List<Map<String,Object>> loadNewsOffline(){
        List<Map<String,Object>> listItem=new ArrayList<>();
        List<News> queryNews=null;
        if(name=="首页"){
            queryNews=News.listAll(News.class);
        }
        else{
            queryNews=News.findWithQuery(News.class,"Select * from News where category = ?",name);
        }
        for(News news:queryNews){
            Map<String,Object> item=new HashMap<String,Object>();
            item.put("title",news.getTitle());
            item.put("category",news.getCategory());
            String tmpContent=news.getContent();
            String tmpPreview=null;
            if(tmpContent.length()<50)
                tmpPreview=tmpContent;
            else
                tmpPreview=tmpContent.substring(0,50)+"...";
            item.put("content",tmpContent);
            item.put("preview",tmpPreview);
            item.put("imagePath",news.getImgPath());
            item.put("newsID",news.getNewsID());
            listItem.add(item);
        }
        if(listItem.size()==0)
            return null;
        else
            return listItem;
    }

    private List<Map<String,Object>> loadNews(){
        List<Map<String,Object>> listItem=null;
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
        final List<Map<String,Object>> newsList=loadNewsOnline();//loadNews(); 调试网络接口时先不使用本地存储功能
        SimpleAdapter myAdapter = new SimpleAdapter(getContext(), newsList, R.layout.news_item, new String[]{"title", "category", "preview","imagePath"}, new int[]{R.id.news_title, R.id.news_category, R.id.news_preview,R.id.news_img});
        myAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
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
        });
        tvContent.setAdapter(myAdapter);
        tvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle=new Bundle();
                bundle.putString("title",(String)newsList.get(i-1).get("title"));
                bundle.putString("category",(String)newsList.get(i-1).get("category"));
                bundle.putString("content",(String)newsList.get(i-1).get("content"));
                bundle.putString("imagePath",(String)newsList.get(i-1).get("imagePath"));
                bundle.putString("newsID",(String)newsList.get(i-1).get("newsID"));
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
