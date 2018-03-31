package org.androidtown.findrest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapReverseGeoCoder;

import org.androidtown.findrest.other.EventDATA;
import org.androidtown.findrest.other.enroll_event;
import org.androidtown.findrest.other.reviewpage;
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
import java.util.List;
import java.util.Locale;


/**
 * Created by 김승훈 on 2017-02-07.
 */
public class Toilet_info extends AppCompatActivity implements AdapterView.OnItemClickListener,ReviewAdapter.ReviewListItemClickListener{

    static int Checker,like,dislike;
    static int tag=0,user_sex;
    protected double x_pos ,y_pos;
    public  String DAUM_MAPS_ANDROID_APP_API_KEY = "15b1197c0f821122fdbe68b7d896e50b";
    protected String Aname,ToiletName,Toilet_info,id_index;
    static String Toilet_add;
    ScrollView scrollView;
    TextView address;
    TextView countreple,counterevent;
    private ProgressDialog pdLoading;
    ReviewAdapter reviewAdapter;
    EventAdapter eventAdapter;
    ListView listView0,listView,listView2,listView3,eventlistview,eventlistview0,eventlistview2,eventlistview3;

    static String eventcount,reviewcount;
    static int toilet = 0;
    static int wheel = 0;
    static int bidet = 0;
    static int diaper = 0;
    person person;

    ImageButton thumb_up_pink,thumb_up_white,thumb_down_pink,thumb_down_white, enroll_EVENT;
    public final ArrayList<ReviewDTO> reviewitem = new ArrayList<ReviewDTO>();
    public final ArrayList<EventDATA>eventitem = new ArrayList<>();
    ToiletJsonParser2 toiletJsonParser2 = new ToiletJsonParser2();
    infoJsonParser infoJsonParser = new infoJsonParser();
    eventjsonParser eventjsonParser=new eventjsonParser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        Window window = getWindow();
//        window.setContentView(R.layout.toilet_information);
        setContentView(R.layout.toilet_information);
        person = new person();
        id_index=person.getIndex_id();
        user_sex=person.getUser_sex();
        ImageView reply_image= (ImageView)findViewById(R.id.reply_image);
        reviewAdapter = new ReviewAdapter(reviewitem,this);
        eventAdapter = new EventAdapter(eventitem);

        listView0 = (ListView)findViewById(R.id.toilet_information_list0);
        listView = (ListView)findViewById(R.id.toilet_information_list);
        listView2 = (ListView)findViewById(R.id.toilet_information_list2);
        listView3 = (ListView)findViewById(R.id.toilet_information_list3);

        eventlistview0 = (ListView)findViewById(R.id.event_listview0);
        eventlistview = (ListView)findViewById(R.id.event_listview);
        eventlistview2 = (ListView)findViewById(R.id.event_listview2);
        eventlistview3 = (ListView)findViewById(R.id.event_listview3);







        scrollView = (ScrollView)findViewById(R.id.information_scroll);


        enroll_EVENT=(ImageButton)findViewById(R.id.enroll_event_btn);

        enroll_EVENT.setColorFilter(Color.parseColor("#000000"));
//
//        thumb_up_pink=(ImageButton)findViewById(R.id.thumb_Up_black);
//
//        thumb_up_pink.setColorFilter(Color.parseColor("#43e6b6"));
//        thumb_down_pink=(ImageButton)findViewById(R.id.thumb_down_black);
//        thumb_up_white=(ImageButton)findViewById(R.id.thumb_Up_white);
//        thumb_down_white=(ImageButton)findViewById(R.id.thumb_down_white);


        enroll_EVENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(org.androidtown.findrest.Toilet_info.this,enroll_event.class);
                intent.putExtra("tag_num",tag);
                intent.putExtra("address",address.getText().toString());
                intent.putExtra("toilet_name",ToiletName);
                intent.putExtra("event_xpos",x_pos);
                intent.putExtra("event_ypos",y_pos);
                startActivity(intent);
            }
        });


        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setListViewHeightBasedOnItems(listView);

        reply_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(org.androidtown.findrest.person.isLoginstate()){
                   Intent intent = new Intent(Toilet_info.this,write_opinion.class);
                   intent.putExtra("tag_num",tag);
                   intent.putExtra("toilet_name",ToiletName);
                   startActivity(intent);

               }else{
                   Toast.makeText(Toilet_info.this,"로그인 후에 이용할 수 있습니다.",Toast.LENGTH_SHORT).show();
               }

            }
        });






        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);
        actionBar.setTitle("화장실정보");

        final Bundle intent = getIntent().getExtras();
        if(intent!=null) {

            tag = intent.getInt("toilet_tag_2");

//            Toast.makeText(Toilet_info.this, "태그번호는=" + tag, Toast.LENGTH_LONG).show();
            toiletJsonParser2.execute();
            infoJsonParser.execute();
            eventjsonParser.execute();
        }







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if(id==R.id.save){

            Intent intent = new Intent(Toilet_info.this,toilet_register.class);

            intent.putExtra("tag2",tag);
            intent.putExtra("tag2_name",ToiletName);
            intent.putExtra("tag2_type",Aname);
            intent.putExtra("tag2_xpos",x_pos);
            intent.putExtra("tag2_ypos",y_pos);




//            Toast.makeText(this,"xpos값"+x_pos,Toast.LENGTH_SHORT).show();
            startActivity(intent);


            return  true;
        }

         else if(id==android.R.id.home){
            System.out.println("눌리냐");
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
// 이벤트 아이템 클릭 메소드
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(org.androidtown.findrest.Toilet_info.this,watch_event.class);
        System.out.println("이벤트클릭이되고있습니다.");
        intent.putExtra("event_enroll_time",eventitem.get(position).getCheck_time());
        intent.putExtra("user_id",eventitem.get(position).getEvent_nick());
        startActivity(intent);

    }


    // 리뷰 따봉 버튼 클릭
    @Override
    public void onItemClick(View v, int position) {
        switch (v.getId()) {


            //따봉업취소
            case R.id.new_thumb_Up_black: {

                System.out.println("누구껀지 체크"+reviewitem.get(position).getId());
                System.out.println("나는 누군지??"+person.getNick());

                if(org.androidtown.findrest.person.getId().equals(reviewitem.get(position).getId())){
                    Toast.makeText(Toilet_info.this,"자신의 리뷰에는 추천하실 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else{

                    reviewitem.get(position).setLikeani(1);
                reviewitem.get(position).setIsChecked(0);
                like = reviewitem.get(position).getLike();
                like -= 1;
                reviewitem.get(position).setLike(like);
                reviewAdapter.notifyDataSetChanged();
                Checker = 0;
                DB_LIKE db_like = new DB_LIKE();
                db_like.update_like(Integer.parseInt(person.getIndex_id()), reviewitem.get(position).getReviewindex(), reviewitem.get(position).getLike(), reviewitem.get(position).getDislike(), Checker);
                System.out.println("따봉업취소클릭이 됩니다");
                }
                break;
            }
            //따봉업
            case R.id.thumb_Up_white: {
                if(org.androidtown.findrest.person.getId().equals(reviewitem.get(position).getId())){
                    Toast.makeText(Toilet_info.this,"자신의 리뷰에는 추천하실 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    reviewitem.get(position).setLikeani(2);
                    reviewitem.get(position).setIsChecked(1);
                    like = reviewitem.get(position).getLike();
                    like += 1;
                    reviewitem.get(position).setLike(like);
                    reviewAdapter.notifyDataSetChanged();
                    Checker = 1;
                    DB_LIKE db_like = new DB_LIKE();
                    db_like.update_like(Integer.parseInt(person.getIndex_id()), reviewitem.get(position).getReviewindex(), reviewitem.get(position).getLike(), reviewitem.get(position).getDislike(), Checker);
                    System.out.println("따봉업클릭이 됩니다");
                }
            }
                break;


            //따봉다운취소
            case R.id.thumb_down_black: {
                // 1이 취소
                reviewitem.get(position).setLikeani(4);
                reviewitem.get(position).setIsChecked(0);
                dislike = reviewitem.get(position).getDislike();
                dislike -= 1;
                reviewitem.get(position).setDislike(dislike);
                reviewAdapter.notifyDataSetChanged();
                Checker = 0;
                DB_LIKE db_like = new DB_LIKE();
                db_like.update_like(Integer.parseInt(person.getIndex_id()),reviewitem.get(position).getReviewindex(),reviewitem.get(position).getLike(),reviewitem.get(position).getDislike(),Checker);
                System.out.println("따봉다운취소클릭이 됩니다");
            }
                break;


            //따봉다운
            case R.id.thumb_down_white: {
                if(org.androidtown.findrest.person.getNick().equals(reviewitem.get(position).getId())){
                    Toast.makeText(Toilet_info.this,"자신의 리뷰에는 비추천하실 수 없습니다.",Toast.LENGTH_SHORT).show();
                }else {
                    reviewitem.get(position).setLikeani(3);
                    reviewitem.get(position).setIsChecked(2);

                    dislike = reviewitem.get(position).getDislike();
                    dislike += 1;
                    reviewitem.get(position).setDislike(dislike);
                    reviewAdapter.notifyDataSetChanged();
                    Checker = 2;
                    DB_LIKE db_like = new DB_LIKE();
                    db_like.update_like(Integer.parseInt(person.getIndex_id()), reviewitem.get(position).getReviewindex(), reviewitem.get(position).getLike(), reviewitem.get(position).getDislike(), Checker);
                    System.out.println("따봉다운클릭이 됩니다");
                }
            }
                break;

        }

    }

    class like extends  AsyncTask<ArrayList,Void,ArrayList>{

        @Override
        protected ArrayList doInBackground(ArrayList... arrayLists) {
            return null;
        }
    }


    class eventjsonParser extends AsyncTask<ArrayList,Void,ArrayList>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {
            return getJson();
        }
        public ArrayList<EventDATA> getJson(){
            ArrayList<EventDATA> list = new ArrayList<>();
            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/SelectEvent.php?toilet_num="+tag+"&toilet_sex="+user_sex);
                System.out.println("토일렛리뷰 뽑아보자 시작");
                System.out.println("토일렛리뷰 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");

                System.out.println("토일렛리뷰"+test);

                String index=null,enrolltime=null, nickname=null,nick_img,cost=null,eventsum=null,event_state=null,check_time;

                System.out.println("토일렛리뷰크기"+test.size());

                for(int k=0; k<test.size(); k++) {
//                    System.out.println("포문시작");

                    JSONObject obj = (JSONObject) test.get(k);
                    eventsum=(String)obj.get("event_sum");
                    enrolltime=(String) obj.get("day");
                    index=(String) obj.get("toilet_num");
                    nickname=(String)obj.get("nick_name");
                    nick_img=(String)obj.get("nick_img");
                    if(nick_img!=null){
                        nick_img.replaceAll("\\/","/");
                    }
                    check_time=(String)obj.get("check_time");
                    cost=(String)obj.get("event_cost");
                    event_state=(String)obj.get("event_state");




                    //안드로이드 클라이언트에서 파싱할때 /옆에 \가 붙기 때문에 치환을 해줘야 한다.
                    eventcount=eventsum;

                    System.out.println("어디서끊기지?19");

                    EventDATA entity = new EventDATA();
                    System.out.println("어디서끊기지?20");

                    System.out.println("어디서끊기지?21");


                    entity.setIndex(index);

                    entity.setNick_img(nick_img);

                    entity.setCheck_time(check_time);

                    entity.setEvent_nick(nickname);

                    entity.setEvent_cost(cost);

                    entity.setEvent_enroll_time(enrolltime);

                    if(event_state!=null){
                        entity.setEvent_state(Integer.parseInt(event_state));
                    }

                    if(eventcount.equals("0")){

                    }else{
                        list.add(entity);
                    }

                }

            }catch (Exception e){
            }
            System.out.println("이벤트갯수"+list.size());
            return list;
        }
        @Override
        protected void onPostExecute(ArrayList result) {
            ReviewDTO item;



            ArrayList<EventDATA> lastresult=null;
            try{
                lastresult=result;

            }catch (Exception e){
                e.printStackTrace();
            }

            switch (lastresult.size()){
                case 0:
                    eventlistview.setVisibility(View.GONE);

                    eventlistview2.setVisibility(View.GONE);
                    eventlistview3.setVisibility(View.GONE);

                    eventlistview.setEnabled(false);
                    eventlistview2.setEnabled(false);
                    eventlistview3.setEnabled(false);


                    eventlistview0.setOnItemClickListener(Toilet_info.this);


                    eventlistview0.setAdapter(eventAdapter);
                    for(EventDATA entity : lastresult){

                        System.out.println("현재시각체크"+entity.getEvent_enroll_time());
                        eventAdapter.additem(entity.getToilet_num(),entity.getEvent_nick(),entity.getNick_img(),entity.getEvent_cost(),entity.getEvent_state(),entity.getEvent_enroll_time(),entity.getCheck_time());

                    }
                    eventAdapter.notifyDataSetChanged();
                   break;
                case 1:
                    eventlistview0.setVisibility(View.GONE);

                    eventlistview2.setVisibility(View.GONE);
                    eventlistview3.setVisibility(View.GONE);

                    eventlistview0.setEnabled(false);
                    eventlistview2.setEnabled(false);
                    eventlistview3.setEnabled(false);

;                     eventlistview.setOnItemClickListener(Toilet_info.this);

                    eventlistview.setAdapter(eventAdapter);
                    for(EventDATA entity : lastresult){

                        System.out.println("현재시각체크"+entity.getEvent_enroll_time());
                        eventAdapter.additem(entity.getToilet_num(),entity.getEvent_nick(),entity.getNick_img(),entity.getEvent_cost(),entity.getEvent_state(),entity.getEvent_enroll_time(),entity.getCheck_time());

                    }
                    eventAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    eventlistview0.setVisibility(View.GONE);

                    eventlistview.setVisibility(View.GONE);
                    eventlistview3.setVisibility(View.GONE);

                    eventlistview0.setEnabled(false);
                    eventlistview.setEnabled(false);
                    eventlistview3.setEnabled(false);
                    eventlistview2.setOnItemClickListener(Toilet_info.this);

                    eventlistview2.setAdapter(eventAdapter);
                    for(EventDATA entity : lastresult){

                        System.out.println("현재시각체크"+entity.getEvent_enroll_time());
                        eventAdapter.additem(entity.getToilet_num(),entity.getEvent_nick(),entity.getNick_img(),entity.getEvent_cost(),entity.getEvent_state(),entity.getEvent_enroll_time(),entity.getCheck_time());

                    }
                    eventAdapter.notifyDataSetChanged();

                    break;
                case 3:
                    eventlistview0.setVisibility(View.GONE);
                    eventlistview.setVisibility(View.GONE);
                    eventlistview2.setVisibility(View.GONE);

                    eventlistview0.setEnabled(false);
                    eventlistview.setEnabled(false);
                    eventlistview2.setEnabled(false);

                    eventlistview3.setOnItemClickListener(Toilet_info.this);
                    eventlistview3.setAdapter(eventAdapter);
                    for(EventDATA entity : lastresult){

                        System.out.println("현재시각체크"+entity.getEvent_enroll_time());
                        eventAdapter.additem(entity.getToilet_num(),entity.getEvent_nick(),entity.getNick_img(),entity.getEvent_cost(),entity.getEvent_state(),entity.getEvent_enroll_time(),entity.getCheck_time());

                    }
                    eventAdapter.notifyDataSetChanged();
                    break;
            }




        }
    }

    class infoJsonParser extends AsyncTask<ArrayList,Void,ArrayList>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {
            return getJson();
        }
        public ArrayList<ReviewDTO> getJson(){
            ArrayList<ReviewDTO> list = new ArrayList<>();
            ArrayList<Album_DTO> albumlist = new ArrayList<>();

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/selectReview.php?toilet_num="+tag+"&id_index="+id_index);
                System.out.println("토일렛리뷰 뽑아보자 시작");
                System.out.println("토일렛리뷰 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");

                System.out.println("토일렛리뷰"+test);

                String nicknaume=null,id, content=null,rating=null,img1=null,img2=null,img3=null,img4=null,img5=null,img6=null,img7=null,img8=null,img9=null,img10=null, sumrating=null,
                        reviewsum=null,date=null,nickimg=null,Checkdlike=null,like=null,dislike=null,review_index=null;

                System.out.println("토일렛리뷰크기"+test.size());

                for(int k=0; k<test.size(); k++) {
//                    System.out.println("포문시작");review_index

                    JSONObject obj = (JSONObject) test.get(k);

                    nicknaume=(String) obj.get("nickname");

                    content=(String)obj.get("content");

                    id=(String)obj.get("user_id");

                    rating=(String)obj.get("rating");

                    //따봉후에 생긴것
                    review_index=(String)obj.get("review_index");

                    Checkdlike=(String)obj.get("isChecked");
                    like=(String)obj.get("like");
                    dislike=(String)obj.get("dislike");

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
                    //안드로이드 클라이언트에서 파싱할때 /옆에 \가 붙기 때문에 치환을 해줘야 한다.

                    nickimg=(String)obj.get("nickimg");
                    if(nickimg!=null){
                        nickimg.replaceAll("\\/","/");
                    }



                    reviewsum=(String)obj.get("reviewsum");
                    reviewcount=reviewsum;

                    date =(String)obj.get("day");

                  sumrating=(String)obj.get("sumrating");
                  System.out.println("어디서끊기지?19");

                    ReviewDTO entity = new ReviewDTO();
                    System.out.println("어디서끊기지?20");
                    ArrayList<String> setentity = new ArrayList<>();
//                    Album_DTO setentity = new Album_DTO();
                    System.out.println("어디서끊기지?21");
                    System.out.println("따봉?15");
                    entity.set_Nickname(nicknaume);
                    System.out.println("따봉?16");
                    entity.set_Content(content);
                    System.out.println("따봉?17");
                    entity.setId(id);
                    System.out.println("따봉?18");
                    entity.setNickimg(nickimg);
                    System.out.println("따봉?19");
                    entity.set_Rating(Float.parseFloat(rating));
                    System.out.println("따봉?20");
                    entity.set_SumRating(Float.parseFloat(sumrating));
                    System.out.println("따봉?21");

                    // 따봉후에 생긴거
                    entity.setLike(Integer.parseInt(like));
                    System.out.println("어디서끊기지?22");

                    entity.setDislike(Integer.parseInt(dislike));
                    System.out.println("어디서끊기지?23");

                    entity.setReviewindex(Integer.parseInt(review_index));
                    System.out.println("어디서끊기지?24");

                    entity.setIsChecked(Integer.parseInt(Checkdlike));
                    System.out.println("어디서끊기지?25");


                    System.out.println("이미지1"+img1);
                    if(img1!=null){
                        setentity.add(img1);
                    }
                    System.out.println("이미지2"+img1);
                    if(img2!=null){
                        setentity.add(img2);
                    }
                    System.out.println("이미지3"+img1);
                    if(img3!=null){
                        setentity.add(img3);
                    }
                    System.out.println("이미지4"+img1);
                    if(img4!=null){
                        setentity.add(img4);
                    }
                    System.out.println("이미지5"+img1);
                    if(img5!=null){
                        setentity.add(img5);
                    }
                    System.out.println("이미지6"+img1);
                    if(img6!=null){
                        setentity.add(img6);
                    }
                    System.out.println("이미지7"+img1);
                    if(img7!=null){
                        setentity.add(img7);
                    }
                    System.out.println("이미지8"+img1);
                    if(img8!=null){
                        setentity.add(img8);
                    }
                    System.out.println("이미지9"+img1);
                    if(img9!=null){
                        setentity.add(img9);
                    }
                    System.out.println("이미지10"+img1);
                    if(img10!=null){
                        setentity.add(img10);
                    }
                    System.out.println("이미지 처리끝 마무리");
//                    albumlist.add(setentity);
                    System.out.println("이미지 처리끝 마무리 앨범리스트 갯수확인"+albumlist.size());
                    entity.setImglist(setentity);

                    System.out.println("어디서끊기지?15");
                    entity.set_ReviewSum(Integer.parseInt(reviewsum));
                    System.out.println("어디서끊기지?16");
                    entity.set_Date(date);
                    System.out.println("어디서끊기지?17");

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





            ArrayList<ReviewDTO> lastresult=null;
            try{
                lastresult=result;

            }catch (Exception e){
                e.printStackTrace();
            }

            switch (lastresult.size()){
                case 0: {
                    listView.setVisibility(View.GONE);
                    listView2.setVisibility(View.GONE);
                    listView3.setVisibility(View.GONE);

                    listView.setEnabled(false);
                    listView2.setEnabled(false);
                    listView3.setEnabled(false);

                    final View header = getLayoutInflater().inflate(R.layout.opinion_list_header, null, false);
                    View footer = getLayoutInflater().inflate(R.layout.opinion_list_footer, null, false);
                    countreple = (TextView) header.findViewById(R.id.count);
                    listView0.addHeaderView(header);
                    listView0.addFooterView(footer);

                    Button button = (Button) footer.findViewById(R.id.add);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("푸터클릭!!!");
                            Intent intent = new Intent(Toilet_info.this, reviewpage.class);
                            intent.putExtra("toilet_num", tag);
                            startActivity(intent);
                        }
                    });

                    listView0.setAdapter(reviewAdapter);


                    for (ReviewDTO entity : lastresult) {
                        System.out.println("리뷰전" + entity.getNickimg());

                        reviewAdapter.addItem(entity.get_Nickname(), entity.get_Content(), entity.get_Date(), entity.get_Rating(), entity.getImglist()
                                , entity.getNickimg(), entity.getId(), entity.getReviewindex()
                                , entity.getLike(), entity.getDislike(), entity.getIsChecked(),0,0);

                    }
                }
                    break;

                case 1:
                {


                    listView0.setVisibility(View.GONE);
                    listView2.setVisibility(View.GONE);
                    listView3.setVisibility(View.GONE);

                    listView0.setEnabled(false);
                    listView2.setEnabled(false);
                    listView3.setEnabled(false);

                    final View header = getLayoutInflater().inflate(R.layout.opinion_list_header,null,false);
                    View footer = getLayoutInflater().inflate(R.layout.opinion_list_footer,null,false);
                    countreple = (TextView)header.findViewById(R.id.count);
                    listView.addHeaderView(header);
                    listView.addFooterView(footer);

                    Button button = (Button)footer.findViewById(R.id.add);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("푸터클릭!!!");
                            Intent intent = new Intent(Toilet_info.this,reviewpage.class);
                            intent.putExtra("toilet_num",tag);
                            startActivity(intent);
                        }
                    });

                    listView.setAdapter(reviewAdapter);


                    for(ReviewDTO entity : lastresult){
                        System.out.println("리뷰전"+entity.getNickimg());

                        reviewAdapter.addItem(entity.get_Nickname(),entity.get_Content(),entity.get_Date(),entity.get_Rating(),entity.getImglist()
                                ,entity.getNickimg(),entity.getId(),entity.getReviewindex()
                                ,entity.getLike(),entity.getDislike(),entity.getIsChecked(),0,0);

                    }
                }
                    break;

                case 2: {
                    listView0.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    listView3.setVisibility(View.GONE);

                    listView0.setEnabled(false);
                    listView.setEnabled(false);
                    listView3.setEnabled(false);

                    final View header = getLayoutInflater().inflate(R.layout.opinion_list_header, null, false);
                    View footer = getLayoutInflater().inflate(R.layout.opinion_list_footer, null, false);
                    countreple = (TextView) header.findViewById(R.id.count);
                    listView2.addHeaderView(header);
                    listView2.addFooterView(footer);

                    Button button = (Button) footer.findViewById(R.id.add);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("푸터클릭!!!");
                            Intent intent = new Intent(Toilet_info.this, reviewpage.class);
                            intent.putExtra("toilet_num", tag);
                            startActivity(intent);
                        }
                    });

                    listView2.setAdapter(reviewAdapter);


                    for (ReviewDTO entity : lastresult) {
                        System.out.println("리뷰전" + entity.getNickimg());

                        reviewAdapter.addItem(entity.get_Nickname(), entity.get_Content(), entity.get_Date(), entity.get_Rating(), entity.getImglist()
                                , entity.getNickimg(), entity.getId(), entity.getReviewindex()
                                , entity.getLike(), entity.getDislike(), entity.getIsChecked(),0,0);

                    }
                }
                    break;

                case 3: {
                    listView0.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    listView2.setVisibility(View.GONE);

                    listView0.setEnabled(false);
                    listView.setEnabled(false);
                    listView2.setEnabled(false);

                    final View header = getLayoutInflater().inflate(R.layout.opinion_list_header, null, false);
                    View footer = getLayoutInflater().inflate(R.layout.opinion_list_footer, null, false);
                    countreple = (TextView) header.findViewById(R.id.count);
                    listView3.addHeaderView(header);
                    listView3.addFooterView(footer);

                    Button button = (Button) footer.findViewById(R.id.add);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("푸터클릭!!!");
                            Intent intent = new Intent(Toilet_info.this, reviewpage.class);
                            intent.putExtra("toilet_num", tag);
                            startActivity(intent);
                        }
                    });

                    listView3.setAdapter(reviewAdapter);


                    for (ReviewDTO entity : lastresult) {
                        System.out.println("리뷰전" + entity.getNickimg());

                        reviewAdapter.addItem(entity.get_Nickname(), entity.get_Content(), entity.get_Date(), entity.get_Rating(), entity.getImglist()
                                , entity.getNickimg(), entity.getId(), entity.getReviewindex()
                                , entity.getLike(), entity.getDislike(), entity.getIsChecked(),0,0);

                    }
                }
                    break;
            }


            countreple.setText(reviewcount);

        }
    }





    class ToiletJsonParser2 extends AsyncTask<ArrayList, Void, ArrayList> {





        @Override
        protected void onPreExecute() {

              pdLoading = new ProgressDialog(Toilet_info.this);

              pdLoading.setMessage("\t로딩중......");
              pdLoading.setCancelable(false);
              pdLoading.show();
        }
        @Override
        protected ArrayList doInBackground(ArrayList... strs) {



            System.out.println("토일렛정보 뽑아보자 시작전");
            return getJsonText2();

        }

        @Override
        protected void onPostExecute(ArrayList result) {



            ImageView Toiletinfo_mantoilet = (ImageView)findViewById(R.id.info_mantoiletimg);
            ImageView Toiletinfo_mantoilet_no =(ImageView)findViewById(R.id.info_man_notoiletimg);

            ImageView Toiletinfo_womantoilet = (ImageView)findViewById(R.id.info_womantoiletimg);
            ImageView Toiletinfo_womantoilet_no =(ImageView)findViewById(R.id.info_woman_notoiletimg);

            ImageView Toiletinfo_toilet = (ImageView)findViewById(R.id.info_toiletimg);
            ImageView Toiletinfo_unisextoilet = (ImageView)findViewById(R.id.info_unisextoiletimg);

            ImageView Toiletinfo_wheelchair_Ok = (ImageView)findViewById(R.id.wheelok);
            ImageView Toiletinfo_wheelchair_No = (ImageView)findViewById(R.id.wheelno);

            ImageView Toiletinfo_bidet_Ok = (ImageView)findViewById(R.id.bidet_ok);
            ImageView Toiletinfo_bidet_No = (ImageView)findViewById(R.id.bidet_no);

            ImageView Toiletinfo_Diaper_Ok = (ImageView)findViewById(R.id.diaper_ok);
            ImageView Toiletinfo_Diaper_No = (ImageView)findViewById(R.id.diaper_no);

            TextView Toilet_Name = (TextView)findViewById(R.id.inform_title);
            TextView Toilet_Aname = (TextView)findViewById(R.id.toilet_info_aname);
            TextView Toilet_infomation = (TextView)findViewById(R.id.inform_informaiton);
            address = (TextView)findViewById(R.id.toilet_info_add);
            ToiletJsonParser2 parser = new ToiletJsonParser2();
            ArrayList<ToiletDATA> list = null;

            try{
                list=result;
            }catch (Exception e){
                e.printStackTrace();
            }

            for(ToiletDATA DTO : list){


                wheel=DTO.get_toilet_wheel();

                toilet=DTO.get_toilet_sex();
                System.out.println("파스후데이터타입"+toilet);
                diaper=DTO.get_toilet_diaper();
                bidet=DTO.get_toilet_bidet();
                Aname=DTO.get_toilet_type();
                ToiletName=DTO.get_toilet_name();
                Toilet_info=DTO.get_toilet_info();
                x_pos=DTO.get_toilet_xpos();
                y_pos=DTO.get_toilet_ypos();


            }

            //화장실 케이스 나눠주기

            switch (toilet) {
                case 0:

                    Toiletinfo_toilet.setVisibility(View.VISIBLE);
                    Toiletinfo_unisextoilet.setVisibility(View.INVISIBLE);
                break;
                case 1:

                    Toiletinfo_mantoilet.setVisibility(View.VISIBLE);
                    Toiletinfo_mantoilet_no.setVisibility(View.INVISIBLE);

                    break;
                case 2:

                    Toiletinfo_womantoilet_no.setVisibility(View.INVISIBLE);
                    Toiletinfo_womantoilet.setVisibility(View.VISIBLE);

                    break;
                case 3:
                    Toiletinfo_toilet.setVisibility(View.INVISIBLE);
                    Toiletinfo_unisextoilet.setVisibility(View.VISIBLE);

                    break;
            }

            if(wheel==1){
                Toiletinfo_wheelchair_Ok.setVisibility(View.VISIBLE);
                Toiletinfo_wheelchair_No.setVisibility(View.INVISIBLE);


            }else if (wheel==0){

                Toiletinfo_wheelchair_Ok.setVisibility(View.INVISIBLE);
                Toiletinfo_wheelchair_No.setVisibility(View.VISIBLE);

            }

            if(bidet==1){

                Toiletinfo_bidet_Ok.setVisibility(View.VISIBLE);
                Toiletinfo_bidet_No.setVisibility(View.INVISIBLE);

            }else if (bidet==0){
                Toiletinfo_bidet_Ok.setVisibility(View.INVISIBLE);
                Toiletinfo_bidet_No.setVisibility(View.VISIBLE);

            }

            if(diaper==1){

                Toiletinfo_Diaper_Ok.setVisibility(View.VISIBLE);
                Toiletinfo_Diaper_No.setVisibility(View.INVISIBLE);

            }else if (diaper==0){

                Toiletinfo_Diaper_Ok.setVisibility(View.INVISIBLE);
                Toiletinfo_Diaper_No.setVisibility(View.VISIBLE);


            }





            MapReverseGeoCoder.ReverseGeoCodingResultListener reverseGeoCodingResultListener = new MapReverseGeoCoder.ReverseGeoCodingResultListener() {
                @Override
                public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String Adrress) {
                    Toilet_add=Adrress;
                    address.setText(Toilet_add);
                }

                @Override
                public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

                }
            };

            getLocation(y_pos,x_pos);

            Toilet_Name.setText(ToiletName);
            Toilet_Aname.setText(Aname);
            Toilet_infomation.setText(Toilet_info);



            pdLoading.dismiss();

        }




        public ArrayList<ToiletDATA> getJsonText2() {

            StringBuffer sb = new StringBuffer();
            ArrayList<ToiletDATA> list = new ArrayList<ToiletDATA>() ;

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/getdata10.php?index="+tag);
                System.out.println("토일렛정보 뽑아보자 시작");
                System.out.println("토일렛정보 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");
                System.out.println("토일렛정보"+test);

                String index=null, name=null ,Aname=null, ypos=null,xpos=null,sex=null,wheel=null,bidet=null,diaper=null,info=null;


                System.out.println("토일렛정보크기"+test.size());

                for(int k=0; k<test.size(); k++) {
//                    System.out.println("포문시작");
                    JSONObject obj = (JSONObject) test.get(k);
                    xpos=(String) obj.get("x_pos");
                    ypos=(String)obj.get("y_pos");
                    index=(String)obj.get("index");
                    name=(String)obj.get("name");
                    Aname=(String)obj.get("Aname");
                    sex=(String)obj.get("sex");
                    info=(String)obj.get("info");
                    diaper=(String)obj.get("diaper");
                    bidet=(String)obj.get("bidet");
                    wheel=(String)obj.get("wheel");


//                    String mstring = new String(name.getBytes("8859_1"),"utf-8");
//                    System.out.println("프2네임"+mstring);

                    ToiletDATA entity = new ToiletDATA();
                    entity.set_toilet_tag(Integer.parseInt(index));
                    entity.set_toilet_xpos(Double.valueOf(xpos));
                    entity.set_toilet_ypos(Double.valueOf(ypos));
                    entity.set_toilet_name(name);
                    entity.set_toilet_type(Aname);
                    entity.set_toilet_sex(Integer.parseInt(sex));
                    entity.set_toilet_diaper(Integer.parseInt(diaper));
                    entity.set_toilet_wheel(Integer.parseInt(wheel));
                    entity.set_toilet_bidet(Integer.parseInt(bidet));
                    System.out.println("인포확인"+info);
                    if(info!=null){
                       entity.set_toilet_info(info);
                   }



                    list.add(entity);

                }

            }catch (Exception e){
            }
            System.out.println("리스트갯수"+list.size());
            return list;

        }


    }
    public void getLocation(double lat, double lng){
        String str = null;
        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
        List<Address> addresses;
        try{
            if(geocoder!=null){
                addresses = geocoder.getFromLocation(lat,lng,1);
                if(addresses!=null&&addresses.size()>0){
                    str=addresses.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        address.setText(str);

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
//                line=bufreader2.readLine();
//                String ecnoding = new String(line.getBytes("8859_1"),"EUC-KR");

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

    public void setListViewHeightBasedOnItems(ListView listView) {

        // Get list adpter of listview;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)  return;

        int numberOfItems = listAdapter.getCount();

        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() *  (numberOfItems - 1);

        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    @Override
    protected void onResume() {
        Log.e("log", "토일렛인포리쥼");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.e("log", "토일렛인포스탑");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("log", "onDestroy");
        super.onDestroy();
    }
}
