package org.androidtown.findrest.other;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidtown.findrest.Album_DTO;
import org.androidtown.findrest.R;
import org.androidtown.findrest.ReviewDTO;
import org.androidtown.findrest.Watch_member_Adapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-16.
 */
public class watch_member extends AppCompatActivity implements AbsListView.OnScrollListener  {

    public final ArrayList<ReviewDTO> reviewitem = new ArrayList<ReviewDTO>();
    Watch_member_Adapter watch_member_adapter;
    ListView listView;
    ScrollView scrollView;
    static  String getvalue_id;
    static  String profileurl,reviewnum;
    boolean lastitemVisbleFlag =false;
    private boolean mLockListView;
    static int firstnumber,lastnumber;
    private LayoutInflater mInflater;
    private String default_toiletimg= "http://ksh564.cafe24.com/signup/uploads/toilet1.jpg";
    ReviewJsonParser_First reviewJsonParser_first =new ReviewJsonParser_First();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_member);
//
//        firstnumber=0;
//        lastnumber=10;

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);
        actionBar.setTitle("화장실정보");

        scrollView = (ScrollView)findViewById(R.id.Watch_member_scroll);
        watch_member_adapter = new Watch_member_Adapter(reviewitem,this);
        listView = (ListView)findViewById(R.id.watch_member_listview);
        listView.setOnScrollListener(this);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
//        listView.setAdapter(reviewAdapter);
        final Bundle intent = getIntent().getExtras();
        if(intent!=null) {


            getvalue_id = intent.getString("user_id");
//            Toast.makeText(watch_member.this, "아이디는=" + getvalue_id, Toast.LENGTH_LONG).show();
            information_json information_json = new information_json();

            information_json.execute();
            reviewJsonParser_first.execute();

//            infoJsonParser.execute();


        }

    }

    public String getStringFromUrl(String pUrl){
        BufferedReader bufreader2 = null;
        HttpURLConnection urlConnection = null;


        StringBuffer page2 = new StringBuffer();

        try{
            URL url2 = new URL(pUrl);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            System.out.println("Url주소는?"+url2);
            request.setURI(new URI(pUrl));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));

            String line = null;

            while((line=in.readLine())!=null){
                Log.d("토일렛인포버라인" , line);
                System.out.println("Url주소는?"+url2);
                page2.append(line);
                System.out.println("Url주소는?2"+page2);
            }
        }catch (IOException e){
            System.out.println("어디서막히는거야1");
            e.printStackTrace();
        } catch (URISyntaxException e2) {
            System.out.println("어디서막히는거야2");
            e2.printStackTrace();
        }
        System.out.println("Url주소는?2"+page2);
        return page2.toString();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int count = totalItemCount - visibleItemCount;
        if(firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false)
        {   firstnumber+=10;
            lastnumber+=10;
//            infoJsonParser2 infoJsonParser2 = new infoJsonParser2();
//            infoJsonParser2.execute();
        }


    }
    class information_json extends AsyncTask<ArrayList,Void,ArrayList> {
        ProgressDialog pdLoading = new ProgressDialog(watch_member.this);

        @Override
        protected void onPreExecute() {

            pdLoading.setMessage("\t로딩중......");
            pdLoading.setCancelable(false);
            pdLoading.show();
            mLockListView = true;
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {
            return getJson();
        }
        public ArrayList<String> getJson(){

            ArrayList<String> img = new ArrayList<>();
            ArrayList<Album_DTO> albumlist = new ArrayList<>();

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/watch_member_information.php?user_id="+getvalue_id);
                System.out.println("회원보기 시작");
                System.out.println("회원보기 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("dbresult");
                System.out.println("회원보기"+test);

                String nickname=null,date=null, nickimg=null;

                System.out.println("회원보기끊기는거확인전");
                    JSONObject obj = (JSONObject) test.get(0);
                System.out.println("회원보기끊기는거확인1"+obj);
                    nickname=(String) obj.get("nickname");
                System.out.println("회원보기끊기는거확인2"+nickname);
                TextView Nickname = (TextView)findViewById(R.id.watch_member_nickname);
                    Nickname.setText(nickname);

                    nickimg=(String)obj.get("profile_img");
                    if(nickimg!=null){
                        nickimg.replaceAll("\\/","/");
                    }
                    System.out.println("프로필이미지확인"+nickimg);
                    profileurl=nickimg;


                System.out.println("프로필이미지확인2"+nickimg);
                    date =(String)obj.get("join_day");

                    System.out.println("어디서끊기지?19");


            }catch (Exception e){
            }

            System.out.println("프로필이미지"+img.size());
            return img;
        }
        @Override
        protected void onPostExecute(ArrayList result) {

            ImageView Profile = (ImageView)findViewById(R.id.watch_member_profile);
            Glide.with(watch_member.this).load(profileurl)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(watch_member.this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Profile);
            pdLoading.dismiss();
        }
    }



    class ReviewJsonParser_First extends AsyncTask<ArrayList,Void,ArrayList> {

        @Override
        protected void onPreExecute() {
            mLockListView = true;
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {
            return getJson();
        }
        public ArrayList<ReviewDTO> getJson(){
            ArrayList<ReviewDTO> list = new ArrayList<>();

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/watch_member.php?user_id="+getvalue_id);
                System.out.println("멤버리뷰 뽑아보자 시작");
                System.out.println("멤버리뷰 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");

                System.out.println("멤버리뷰"+test);

                String toiletname=null,toiletnum=null, content=null,rating=null,img1=null,img2=null,img3=null,img4=null,img5=null,img6=null,img7=null,img8=null,img9=null,img10=null, sumrating=null,reviewsum=null,date=null, nickimg=null;

                System.out.println("멤버리뷰"+test.size());


                for(int k=0; k<test.size(); k++) {


                    JSONObject obj = (JSONObject) test.get(k);

                    toiletname=(String) obj.get("toiletname");
                    System.out.println("화장실이름"+toiletname);

                    content=(String)obj.get("content");

                    rating=(String)obj.get("rating");

                    toiletnum=(String)obj.get("toiletnum");

                    img1=(String)obj.get("img1");
                    if(img1!=null){
                        img1.replaceAll("\\/","/");
                    }
                    img2=(String)obj.get("img2");
                    if(img2!=null){
                        img2.replaceAll("\\/","/");
                    }
                    img3=(String)obj.get("img3");
                    if(img3!=null){
                        img3.replaceAll("\\/","/");
                    }
                    img4=(String)obj.get("img4");
                    if(img4!=null){
                        img4.replaceAll("\\/","/");
                    }
                    img5=(String)obj.get("img5");
                    if(img5!=null){
                        img5.replaceAll("\\/","/");
                    }
                    img6=(String)obj.get("img6");
                    if(img6!=null){
                        img6.replaceAll("\\/","/");
                    }
                    img7=(String)obj.get("img7");
                    if(img7!=null){
                        img7.replaceAll("\\/","/");
                    }
                    img8=(String)obj.get("img8");
                    if(img8!=null){
                        img8.replaceAll("\\/","/");
                    }
                    img9=(String)obj.get("img9");
                    if(img9!=null){
                        img9.replaceAll("\\/","/");
                    }
                    img10=(String)obj.get("img10");
                    if(img10!=null){
                        img10.replaceAll("\\/","/");
                    }
                    nickimg=(String)obj.get("nickimg");
                    if(nickimg!=null){
                        nickimg.replaceAll("\\/","/");
                    }
                    System.out.println("프로필이미지확인"+nickimg);

                    reviewsum=(String)obj.get("reviewsum");

                    reviewnum=reviewsum;

                    date =(String)obj.get("day");

                    sumrating=(String)obj.get("sumrating");
                    System.out.println("어디서끊기지?19");

                    ReviewDTO entity = new ReviewDTO();
                    ArrayList<String> setentity = new ArrayList<>();

                    entity.setToilet_name(toiletname);

                    entity.set_Content(content);

                    entity.setNickimg(nickimg);

                    entity.set_Rating(Float.parseFloat(rating));

                    entity.setToilet_num(toiletnum);

                    entity.set_SumRating(Float.parseFloat(sumrating));
                    System.out.println("어디서끊기지?15");
                    entity.set_ReviewSum(Integer.parseInt(reviewsum));
                    System.out.println("어디서끊기지?16");
                    entity.set_Date(date);
                    System.out.println("어디서끊기지?17");
                    System.out.println("이미지1"+img1);
                    if(img1!=null){
                        setentity.add(img1);
                        entity.set_Img1(img1);
                    }else{
                        entity.set_Img1(default_toiletimg);
                    }
                    System.out.println("이미지2"+img2);
                    if(img2!=null){
                        setentity.add(img2);
                    }
                    System.out.println("이미지3"+img3);
                    if(img3!=null){
                        setentity.add(img3);
                    }
                    System.out.println("이미지4"+img4);
                    if(img4!=null){
                        setentity.add(img4);
                    }
                    System.out.println("이미지5"+img5);
                    if(img5!=null){
                        setentity.add(img5);
                    }
                    System.out.println("이미지6"+img6);
                    if(img6!=null){
                        setentity.add(img6);
                    }
                    System.out.println("이미지7"+img7);
                    if(img7!=null){
                        setentity.add(img7);
                    }
                    System.out.println("이미지8"+img8);
                    if(img8!=null){
                        setentity.add(img8);
                    }
                    System.out.println("이미지9"+img9);
                    if(img9!=null){
                        setentity.add(img9);
                    }
                    System.out.println("이미지10"+img10);
                    if(img10!=null){
                        setentity.add(img10);
                    }
                    System.out.println("이미지 처리끝 마무리");


                    System.out.println("이미지 처리끝 마무리1");
                    entity.setImglist(setentity);


                    list.add(entity);

                }

            }catch (Exception e){
            }

            System.out.println("리뷰갯수"+list.size());
            return list;
        }
        @Override
        protected void onPostExecute(ArrayList result) {
            ReviewDTO item;
            listView.setAdapter(watch_member_adapter);
            TextView reviewnumber = (TextView)findViewById(R.id.watch_member_reviewsum);
            reviewnumber.setText(reviewnum);

            ArrayList<ReviewDTO> lastresult=null;
            try{
                lastresult=result;

            }catch (Exception e){
                e.printStackTrace();
            }

            for(ReviewDTO entity : lastresult){

                watch_member_adapter.addItem(entity.getToilet_name(),entity.get_Content(),entity.get_Date(),entity.get_Rating(),entity.getImglist(),entity.get_Img1(),entity.getToilet_num());
            }

            watch_member_adapter.notifyDataSetChanged();
            mLockListView=false;

        }
    }



    // 페이징 하는 리스트뷰
//    class infoJsonParser2 extends AsyncTask<ArrayList,Void,ArrayList> {
//
//
//        @Override
//        protected void onPreExecute() {
//            mLockListView = true;
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList doInBackground(ArrayList... strs) {
//            return getJson();
//        }
//        public ArrayList<ReviewDTO> getJson(){
//            ArrayList<ReviewDTO> list = new ArrayList<>();
//            ArrayList<Album_DTO> albumlist = new ArrayList<>();
//
//            try{
//
//                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/SelectReview2.php?toilet_num="+tag2+"&start_num="+firstnumber+"&last_num="+lastnumber);
//                System.out.println("토일렛리뷰 뽑아보자 시작");
//                System.out.println("토일렛리뷰 뽑아보자"+jsonPage2);
//                JSONParser jsonParser = new JSONParser();
//
//                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
//                JSONArray test = (JSONArray)jsonObject.get("result");
//
//                System.out.println("토일렛리뷰"+test);
//
//                String nicknaume=null, content=null,rating=null,img1=null,img2=null,img3=null,img4=null,img5=null,img6=null,img7=null,img8=null,img9=null,img10=null, sumrating=null,reviewsum=null,date=null, nickimg=null;
//
//                System.out.println("토일렛리뷰크기"+test.size());
//                mLockListView = test.size() == 0;
//
//                for(int k=0; k<test.size(); k++) {
//
//                    JSONObject obj = (JSONObject) test.get(k);
//
//                    nicknaume=(String) obj.get("nickname");
//
//                    content=(String)obj.get("content");
//
//                    rating=(String)obj.get("rating");
//
//                    img1=(String)obj.get("img1");
//                    if(img1!=null){
//                        img1.replaceAll("\\/","/");
//                    }
//                    img2=(String)obj.get("img2");
//                    if(img2!=null){
//                        img2.replaceAll("\\/","/");
//                    }
//                    img3=(String)obj.get("img3");
//                    if(img3!=null){
//                        img3.replaceAll("\\/","/");
//                    }
//                    img4=(String)obj.get("img4");
//                    if(img4!=null){
//                        img4.replaceAll("\\/","/");
//                    }
//                    img5=(String)obj.get("img5");
//                    if(img5!=null){
//                        img5.replaceAll("\\/","/");
//                    }
//                    img6=(String)obj.get("img6");
//                    if(img6!=null){
//                        img6.replaceAll("\\/","/");
//                    }
//                    img7=(String)obj.get("img7");
//                    if(img7!=null){
//                        img7.replaceAll("\\/","/");
//                    }
//                    img8=(String)obj.get("img8");
//                    if(img8!=null){
//                        img8.replaceAll("\\/","/");
//                    }
//                    img9=(String)obj.get("img9");
//                    if(img9!=null){
//                        img9.replaceAll("\\/","/");
//                    }
//                    img10=(String)obj.get("img10");
//                    if(img10!=null){
//                        img10.replaceAll("\\/","/");
//                    }
//                    nickimg=(String)obj.get("nickimg");
//                    if(nickimg!=null){
//                        nickimg.replaceAll("\\/","/");
//                    }
//                    System.out.println("프로필이미지확인"+nickimg);
//
//                    reviewsum=(String)obj.get("reviewsum");
//
//
//                    date =(String)obj.get("day");
//
//                    sumrating=(String)obj.get("sumrating");
//                    System.out.println("어디서끊기지?19");
//
//                    ReviewDTO entity = new ReviewDTO();
//
//                    ArrayList<String> setentity = new ArrayList<>();
//
//                    entity.set_Nickname(nicknaume);
//
//                    entity.set_Content(content);
//
//                    entity.setNickimg(nickimg);
//
//                    entity.set_Rating(Float.parseFloat(rating));
//                    entity.set_SumRating(Float.parseFloat(sumrating));
//                    System.out.println("어디서끊기지?15");
//                    entity.set_ReviewSum(Integer.parseInt(reviewsum));
//                    System.out.println("어디서끊기지?16");
//                    entity.set_Date(date);
//                    System.out.println("어디서끊기지?17");
//
//                    System.out.println("이미지1"+img1);
//                    if(img1!=null){
//                        setentity.add(img1);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지2"+img2);
//                    if(img2!=null){
//                        setentity.add(img2);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지3"+img3);
//                    if(img3!=null){
//                        setentity.add(img3);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지4"+img4);
//                    if(img4!=null){
//                        setentity.add(img4);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지5"+img5);
//                    if(img5!=null){
//                        setentity.add(img5);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지6"+img6);
//                    if(img6!=null){
//                        setentity.add(img6);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지7"+img7);
//                    if(img7!=null){
//                        setentity.add(img7);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지8"+img8);
//                    if(img8!=null){
//                        setentity.add(img8);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지9"+img9);
//                    if(img9!=null){
//                        setentity.add(img9);
//                    }else{
//                        setentity.add("null");
//                    }
//                    System.out.println("이미지10"+img10);
//                    if(img10!=null){
//                        setentity.add(img10);
//                    }else{
//                        setentity.add("null");
//                    }
//
//                    entity.setImglist(setentity);
//
//                    list.add(entity);
//
//                }
//
//            }catch (Exception e){
//            }
//            System.out.println("리뷰갯수"+list.size());
//            return list;
//        }
//        @Override
//        protected void onPostExecute(ArrayList result) {
//            ReviewDTO item;
//            ArrayList<ReviewDTO> lastresult=null;
//            try{
//                lastresult=result;
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            for(ReviewDTO entity : lastresult){
//                System.out.println("리뷰어댑터이미지경로"+entity.getNickimg());
//                reviewAdapter.addItem(entity.get_Nickname(),entity.get_Content(),entity.get_Date(),entity.get_Rating(),entity.getImglist(),entity.getNickimg(),entity.getId());
//
//            }
//
//            reviewAdapter.notifyDataSetChanged();
//        }
//    }
}
