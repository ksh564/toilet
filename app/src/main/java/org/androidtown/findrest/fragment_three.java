package org.androidtown.findrest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import net.daum.mf.map.api.MapView;

import org.androidtown.findrest.other.Current_Location;
import org.androidtown.findrest.other.EventDATA;
import org.androidtown.findrest.other.map_poi;
import org.androidtown.findrest.other.spinner_event_adp;
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
import java.util.concurrent.ExecutionException;

/**
 * Created by 김승훈 on 2017-01-12.
 */
public class fragment_three extends Fragment {

    DB_Manager4 db_manager4;
    private View view;
    private MapView mMapView;
    public String DAUM_MAPS_ANDROID_APP_API_KEY = "15b1197c0f821122fdbe68b7d896e50b";
    static int confirm;
    private String loginconfirm;
    static String token, Mylatitude, Mylongitude;
    public final ArrayList<EventDATA>eventitem = new ArrayList<>();
    static Double myXpos,myYpos,Distance;
    ListView eventlistview;
    LinearLayout First_layout,Text_layout;



    Button BTN;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        map_poi map_poi = new map_poi();
        db_manager4 = new DB_Manager4();

        Current_Location current_location = new Current_Location();


        myXpos=current_location.getXpos();
        myYpos=current_location.getYpos();


        System.out.println("xpos"+myXpos);
        System.out.println("ypos"+myYpos);
        Log.e("log", "onCreateView_3");
        if(view==null){
//            container.removeAllViews();
        }
        try {
            view = inflater.inflate(R.layout.fragment_three, container, false);
        }catch (InflateException e){
            e.printStackTrace();
        }
        First_layout=(LinearLayout)view.findViewById(R.id.First_layout);
        Text_layout=(LinearLayout)view.findViewById(R.id.TextView_layout);

        eventlistview = (ListView)view.findViewById(R.id.event_listview);
        Spinner radius_spinner = (Spinner) view.findViewById(R.id.event_Radius_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        radius_spinner.setAdapter(adapter);
//        radius_spinner.setSelection(0);

        radius_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (position == 0) {

                    Distance=0.1;
                    try {
                        new NewToiletJsonParser().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }else if(position==1){
                    Distance=0.5;
                    try {
                        new NewToiletJsonParser().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }else if(position==2){
                    Distance=1.0;
                    try {
                        new NewToiletJsonParser().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }else{
                    Distance=2.0;
                    try {
                        new NewToiletJsonParser().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




//        BTN=(Button)view.findViewById(R.id.event_btn);
//
//        BTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Double var=1.3;
//
//                Mylatitude=Double.toString(1.123);
//                Mylongitude=Double.toString(1.324);
//
//        token = FirebaseInstanceId.getInstance().getToken();
//                System.out.println("토큰입니다"+token);
//                System.out.println("엑스"+Mylatitude);
//                System.out.println("와이"+Mylongitude);
//
//
//            }
//        });

//
//        MapLayout mapLayout = new MapLayout(getActivity());
//        mMapView = mapLayout.getMapView();
//        mMapView.setDaumMapApiKey(DAUM_MAPS_ANDROID_APP_API_KEY);
//
//        mMapView.setMapViewEventListener(mapViewEventListener);
//        mMapView.setPOIItemEventListener(poiItemEventListener);
//        mMapView.setCurrentLocationEventListener(currentLocationEventListener);
//        mMapView.setMapType(MapView.MapType.Standard);
//
//        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.event_map_view);
//        mapViewContainer.setFocusable(false);
//        mapViewContainer.addView(mapLayout);





        return view;

    }

    class NewToiletJsonParser extends AsyncTask<ArrayList, Void, ArrayList>{




        @Override
        protected ArrayList doInBackground(ArrayList... strs) {
            return getJsonText();
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);

           spinner_event_adp eventAdapter = new spinner_event_adp(eventitem);
            eventlistview.setAdapter(eventAdapter);

            ArrayList<EventDATA> lastresult=null;

             if(eventAdapter.isEmpty()==false){
                 eventitem.clear();
                 eventAdapter.notifyDataSetChanged();
             }

            try{
                lastresult=result;
                System.out.println("이벤트스피너갯수"+lastresult.size());

            }catch (Exception e){
                e.printStackTrace();
            }
            for( EventDATA entity  : lastresult){

                loginconfirm=entity.getEvent_result();

                System.out.println("로그인컨펌"+loginconfirm);

                if(loginconfirm.equals("true")){

                    First_layout.setVisibility(View.INVISIBLE);
                    Text_layout.setVisibility(View.INVISIBLE);
                    eventlistview.setVisibility(View.VISIBLE);

                    eventAdapter.additem(entity.getToilet_num(),entity.getEvent_nick(),entity.getNick_img(),entity.getEvent_cost(),entity.getEvent_state(),entity.getEvent_enroll_time(),entity.getCheck_time(),entity.getToilet_name(),entity.getDistance());


                }else if(loginconfirm.equals("false")){


                    First_layout.setVisibility(View.INVISIBLE);
                    Text_layout.setVisibility(View.VISIBLE);
                    eventlistview.setVisibility(View.INVISIBLE);



                }


            }

            eventAdapter.notifyDataSetChanged();

        }


        public ArrayList<EventDATA> getJsonText() {

            StringBuffer sb = new StringBuffer();
            ArrayList<EventDATA> list = new ArrayList<EventDATA>() ;
            try{

                String jsonPage = getStringFromUrl("http://ksh564.cafe24.com/signup/event_distance.php?my_xpos=" + myXpos + "&my_ypos=" + myYpos + "&distance=" +Distance);
                System.out.println("프래그먼트3page"+jsonPage);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage);
                System.out.println("프래그먼트3page1"+jsonObject);
                JSONArray test = (JSONArray) jsonObject.get("result");
                System.out.println("프래그먼트3page2"+test);

                String index=null,enrolltime=null, nickname=null,nick_img,cost=null,eventsum=null,event_state=null,check_time=null,adminconfirm=null,distance=null,toilet_name=null;


                for(int j=0; j<test.size(); j++) {
                    System.out.println("포문시작");
                    JSONObject obj = (JSONObject) test.get(j);

                    adminconfirm=(String)obj.get("dbresult");
                    System.out.println("adminconfirm:"+adminconfirm);
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
                    distance=(String)obj.get("distance");
                    toilet_name=(String)obj.get("toilet_name");


                    EventDATA entity = new EventDATA();
                    System.out.println("어디서끊기지?1");


                    entity.setEvent_result(adminconfirm);
                    System.out.println("어디서끊기지?2");
                    entity.setIndex(index);
                    System.out.println("어디서끊기지?3");
                    entity.setNick_img(nick_img);
                    System.out.println("어디서끊기지?4");
                    entity.setCheck_time(check_time);
                    System.out.println("어디서끊기지?5");
                    entity.setEvent_nick(nickname);
                    System.out.println("어디서끊기지?6");
                    entity.setEvent_cost(cost);
                    System.out.println("어디서끊기지?7");
                    entity.setEvent_enroll_time(enrolltime);
                    System.out.println("어디서끊기지?8");
                    if(event_state!=null){
                        entity.setEvent_state(Integer.parseInt(event_state));
                    }
                    entity.setToilet_name(toilet_name);
                    if(distance!=null){
                        entity.setDistance(Float.parseFloat(distance));
                    }

                    System.out.println("어디서끊기지?9");

                    list.add(entity);



                }






            }catch (Exception e){


            }
            System.out.println("리스트갯수"+list.size());
            return list;

        }

        public String getStringFromUrl(String pUrl){
            BufferedReader bufreader = null;
            HttpURLConnection urlConnection = null;

            StringBuffer page = new StringBuffer();


            try{
                URL url2 = new URL(pUrl);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                System.out.println("Url주소는?" + url2);
                request.setURI(new URI(pUrl));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                String line=null;
                while((line =in.readLine())!=null){
                    Log.d("line" , line);
                    page.append(line);
                }


            }catch (IOException e) {
                System.out.println("프래그먼트1에서 막히는부분1");
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return page.toString();
        }
    }

// 뷰 제거 하는 부분
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("log", "onDestroyView_fragment_three");
//        ViewGroup  parent = (ViewGroup )view.getParent();
//        if(parent!=null){
//            parent.removeView(view);
//        }
    }

    @Override
    public void onStart() {
        Log.e("log", "onStart_3");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("log", "onResume_3");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("log", "onPause_3");

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("log", "onStop_3");

        super.onStop();
    }


    @Override
    public void onDestroy() {
        Log.e("log", "onDestroy_3");
        super.onDestroy();
    }
}
