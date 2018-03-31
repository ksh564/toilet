package org.androidtown.findrest.other;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import org.androidtown.findrest.Album_DTO;
import org.androidtown.findrest.R;
import org.androidtown.findrest.ReviewAdapter;
import org.androidtown.findrest.ReviewDTO;
import org.androidtown.findrest.person;
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
 * Created by 김승훈 on 2017-03-10.
 */
public class reviewpage extends AppCompatActivity implements AbsListView.OnScrollListener,ReviewAdapter.ReviewListItemClickListener{

    public final ArrayList<ReviewDTO> reviewitem = new ArrayList<ReviewDTO>();
    ReviewAdapter reviewAdapter;
    ListView listView;
    static int tag2=0;
    boolean lastitemVisbleFlag =false;
    private boolean mLockListView;
    static int firstnumber,lastnumber;
    private LayoutInflater mInflater;
    person person;
    protected String id_index;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){
            System.out.println("눌리냐");
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    infoJsonParser infoJsonParser = new infoJsonParser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_review);

        person = new person();
        id_index=person.getIndex_id();

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);


        actionBar.setTitle("리뷰");
        reviewAdapter = new ReviewAdapter(reviewitem,this);
        listView = (ListView)findViewById(R.id.all_review);
        listView.setOnScrollListener(this);
        mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView.addFooterView(mInflater.inflate(R.layout.review_footer,null));

        firstnumber=0;
        lastnumber=10;
        listView.setAdapter(reviewAdapter);




        final Bundle intent = getIntent().getExtras();
        if(intent!=null) {

            tag2 = intent.getInt("toilet_num");
            infoJsonParser.execute();
//            Toast.makeText(reviewpage.this, "태그번호는=" + tag2, Toast.LENGTH_LONG).show();

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
            infoJsonParser2 infoJsonParser2 = new infoJsonParser2();
            infoJsonParser2.execute();

//           Toast.makeText(reviewpage.this,"돌아갑니다"+firstnumber+"뒤돌아갑니다"+lastnumber,Toast.LENGTH_SHORT).show();

        }


    }
        // 따봉 클릭
    @Override
    public void onItemClick(View v, int position) {

    }


    class infoJsonParser extends AsyncTask<ArrayList,Void,ArrayList> {


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
            ArrayList<Album_DTO> albumlist = new ArrayList<>();

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/SelectReview3.php?toilet_num="+tag2+"&id_index="+id_index);
                System.out.println("토일렛리뷰 뽑아보자 시작");
                System.out.println("토일렛리뷰 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");

                System.out.println("토일렛리뷰"+test);

                String nicknaume=null, content=null,id=null,rating=null,img1=null,img2=null,img3=null,img4=null,img5=null,img6=null,img7=null,img8=null,img9=null,img10=null, sumrating=null,reviewsum=null,date=null, nickimg=null,Checkdlike=null,like=null,dislike=null,review_index=null;

                System.out.println("토일렛리뷰크기"+test.size());


                for(int k=0; k<test.size(); k++) {


                    JSONObject obj = (JSONObject) test.get(k);

                    nicknaume=(String) obj.get("nickname");

                    content=(String)obj.get("content");

                    id=(String)obj.get("user_id");

                    // 좋아요 업데이트 후

                    review_index=(String)obj.get("review_index");

                    Checkdlike=(String)obj.get("isChecked");

                    like=(String)obj.get("like");

                    dislike=(String)obj.get("dislike");

                    rating=(String)obj.get("rating");

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


                    date =(String)obj.get("day");

                    sumrating=(String)obj.get("sumrating");
                    System.out.println("어디서끊기지?19");

                    ReviewDTO entity = new ReviewDTO();
                    ArrayList<String> setentity = new ArrayList<>();
//                    Album_DTO setentity = new Album_DTO();

                    entity.set_Nickname(nicknaume);

                    entity.set_Content(content);

                    entity.setNickimg(nickimg);

                    entity.setId(id);

                    entity.set_Rating(Float.parseFloat(rating));



                    entity.set_SumRating(Float.parseFloat(sumrating));
                    System.out.println("어디서끊기지?15");
                    entity.setLike(Integer.parseInt(like));
                    System.out.println("어디서끊기지?22");

                    entity.setDislike(Integer.parseInt(dislike));
                    System.out.println("어디서끊기지?23");

                    entity.setReviewindex(Integer.parseInt(review_index));
                    System.out.println("어디서끊기지?24");

                    entity.setIsChecked(Integer.parseInt(Checkdlike));
                    System.out.println("어디서끊기지?25");
                    entity.set_ReviewSum(Integer.parseInt(reviewsum));
                    System.out.println("어디서끊기지?16");
                    entity.set_Date(date);
                    System.out.println("어디서끊기지?17");
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
            listView.setAdapter(reviewAdapter);

            ArrayList<ReviewDTO> lastresult=null;
            try{
                lastresult=result;

            }catch (Exception e){
                e.printStackTrace();
            }

            for(ReviewDTO entity : lastresult){


                reviewAdapter.addItem(entity.get_Nickname(),entity.get_Content(),entity.get_Date(),entity.get_Rating(),entity.getImglist(),entity.getNickimg(),entity.getId(),entity.getReviewindex()
                        ,entity.getLike(),entity.getDislike(),entity.getIsChecked(),0,0);


            }

            reviewAdapter.notifyDataSetChanged();
            mLockListView=false;

        }
    }

    class infoJsonParser2 extends AsyncTask<ArrayList,Void,ArrayList> {


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
            ArrayList<Album_DTO> albumlist = new ArrayList<>();

            try{

                String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/SelectReview2.php?toilet_num="+tag2+"&start_num="+firstnumber+"&last_num="+lastnumber+"&id_index="+id_index);
                System.out.println("토일렛리뷰 뽑아보자 시작");
                System.out.println("토일렛리뷰 뽑아보자"+jsonPage2);
                JSONParser jsonParser = new JSONParser();

                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage2);
                JSONArray test = (JSONArray)jsonObject.get("result");

                System.out.println("토일렛리뷰"+test);

                String nicknaume=null, content=null,rating=null,id=null,img1=null,img2=null,img3=null,img4=null,img5=null,img6=null,img7=null,img8=null,img9=null,img10=null, sumrating=null,reviewsum=null,date=null, nickimg=null,Checkdlike=null,like=null,dislike=null,review_index=null;

                System.out.println("토일렛리뷰크기"+test.size());
                mLockListView = test.size() == 0;

                for(int k=0; k<test.size(); k++) {


                    JSONObject obj = (JSONObject) test.get(k);

                    nicknaume=(String) obj.get("nickname");

                    content=(String)obj.get("content");

                    id=(String)obj.get("user_id");

                    // 좋아요 업데이트 후

                    review_index=(String)obj.get("review_index");

                    Checkdlike=(String)obj.get("isChecked");

                    like=(String)obj.get("like");

                    dislike=(String)obj.get("dislike");

                    rating=(String)obj.get("rating");

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


                    date =(String)obj.get("day");

                    sumrating=(String)obj.get("sumrating");
                    System.out.println("어디서끊기지?19");

                    ReviewDTO entity = new ReviewDTO();

                    ArrayList<String> setentity = new ArrayList<>();
//                    Album_DTO setentity = new Album_DTO();

                    entity.set_Nickname(nicknaume);

                    entity.set_Content(content);

                    entity.setNickimg(nickimg);



                    entity.setId(id);

                    entity.set_Rating(Float.parseFloat(rating));

                    //좋아요 업데이트트
                    entity.setLike(Integer.parseInt(like));
                    System.out.println("어디서끊기지?22");

                    entity.setDislike(Integer.parseInt(dislike));
                    System.out.println("어디서끊기지?23");

                    entity.setReviewindex(Integer.parseInt(review_index));
                    System.out.println("어디서끊기지?24");

                    entity.setIsChecked(Integer.parseInt(Checkdlike));
                    System.out.println("어디서끊기지?25");

                    entity.set_SumRating(Float.parseFloat(sumrating));
                    System.out.println("어디서끊기지?15");
                    entity.set_ReviewSum(Integer.parseInt(reviewsum));
                    System.out.println("어디서끊기지?16");
                    entity.set_Date(date);
                    System.out.println("어디서끊기지?17");

                    System.out.println("이미지1"+img1);
                    if(img1!=null){
                        setentity.add(img1);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지2"+img2);
                    if(img2!=null){
                        setentity.add(img2);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지3"+img3);
                    if(img3!=null){
                        setentity.add(img3);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지4"+img4);
                    if(img4!=null){
                        setentity.add(img4);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지5"+img5);
                    if(img5!=null){
                        setentity.add(img5);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지6"+img6);
                    if(img6!=null){
                        setentity.add(img6);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지7"+img7);
                    if(img7!=null){
                        setentity.add(img7);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지8"+img8);
                    if(img8!=null){
                        setentity.add(img8);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지9"+img9);
                    if(img9!=null){
                        setentity.add(img9);
                    }else{
                        setentity.add("null");
                    }
                    System.out.println("이미지10"+img10);
                    if(img10!=null){
                        setentity.add(img10);
                    }else{
                        setentity.add("null");
                    }
//                    if(img1!=null){
//                        setentity.setImg1(img1);
//                    }
//
//                    if(img2!=null){
//                        setentity.setImg1(img2);
//                    }
//                    if(img3!=null){
//                        setentity.setImg1(img3);
//                    }
//                    if(img4!=null){
//                        setentity.setImg1(img4);
//                    }
//                    if(img5!=null){
//                        setentity.setImg1(img5);
//                    }
//                    if(img6!=null){
//                        setentity.setImg1(img6);
//                    }
//                    if(img7!=null){
//                        setentity.setImg1(img7);
//                    }
//                    if(img8!=null){
//                        setentity.setImg1(img8);
//                    }
//                    if(img9!=null){
//                        setentity.setImg1(img9);
//                    }
//                    if(img10!=null){
//                        setentity.setImg1(img10);
//                    }

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


            ArrayList<ReviewDTO> lastresult=null;
            try{
                lastresult=result;

            }catch (Exception e){
                e.printStackTrace();
            }

            for(ReviewDTO entity : lastresult){
                System.out.println("리뷰어댑터이미지경로"+entity.getNickimg());
                reviewAdapter.addItem(entity.get_Nickname(),entity.get_Content(),entity.get_Date(),entity.get_Rating(),entity.getImglist(),entity.getNickimg(),entity.getId(),entity.getReviewindex()
                        ,entity.getLike(),entity.getDislike(),entity.getIsChecked(),0,0);


            }


            reviewAdapter.notifyDataSetChanged();



        }
    }
}
