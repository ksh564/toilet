package org.androidtown.findrest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.findrest.other.EventDATA;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-30.
 */
public class watch_event extends AppCompatActivity {

    private ProgressDialog pdLoading;
    static String user_id, event_state,json_enroll_time,loginNick,after_event_cost,
            after_toilet_name,after_event_id,after_event_nick,after_event_addr,after_event_content,after_event_deadline_time,after_event_enroll_time;
    static int after_event_state,update_event_state;
    Button Event_Accept,Event_Ing,Event_Done,Help_Dis,Help_Done;
    DB_Manager5 db_manager5;
    person person;

    ToiletEventParser toileteventparser = new ToiletEventParser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_event);

        Event_Accept=(Button)findViewById(R.id.event_accept);
        Event_Ing=(Button)findViewById(R.id.event_ing);
        Event_Done=(Button)findViewById(R.id.event_done);
        Help_Dis=(Button)findViewById(R.id.help_disconnect);
        Help_Done=(Button)findViewById(R.id.help_done);
        loginNick=person.getId();



        final Bundle intent = getIntent().getExtras();
        if (intent != null) {

            user_id = intent.getString("user_id");
            json_enroll_time=intent.getString("event_enroll_time");
//            Toast.makeText(watch_event.this, "테스트한다=" + person.getNick() + "이벤트스테이트" + json_enroll_time, Toast.LENGTH_LONG).show();
            toileteventparser.execute();
        }

    }


    class ToiletEventParser extends AsyncTask<ArrayList, Void, ArrayList> {


        @Override
        protected void onPreExecute() {

            pdLoading = new ProgressDialog(watch_event.this);

            pdLoading.setMessage("\t로딩중......");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {


            System.out.println("이벤트 자 시작전");
            return getEventJson();

        }

        @Override
        protected void onPostExecute(ArrayList result) {

            TextView Event_nick = (TextView) findViewById(R.id.watch_event_nick);
            TextView Event_addr = (TextView) findViewById(R.id.watch_event_address);
            TextView Event_content = (TextView) findViewById(R.id.watch_event_content);
            TextView Event_Cost = (TextView)findViewById(R.id.watch_reward_textview);
            TextView Event_enroll_time = (TextView) findViewById(R.id.watch_event_enroll_dayview);
            TextView Event_dead_time = (TextView) findViewById(R.id.watch_event_dead_dayview);

            ArrayList<EventDATA> list = null;

            try {
                list = result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (EventDATA DTO : list) {

                after_event_state=DTO.getEvent_state();
                after_toilet_name=DTO.getToilet_name();
                after_event_nick=DTO.getEvent_nick();
                after_event_addr=DTO.getEvent_addr();
                after_event_cost =DTO.getEvent_cost();
                after_event_content=DTO.getEvent_content();
                after_event_deadline_time=DTO.getEvent_deadline_time();
                after_event_enroll_time=DTO.getEvent_enroll_time();
            }

            Event_Cost.setText(after_event_cost);
            Event_nick.setText(after_event_nick);
            Event_addr.setText(after_event_addr);
            Event_content.setText(after_event_content);
            Event_enroll_time.setText(after_event_enroll_time);
            Event_dead_time.setText(after_event_deadline_time);

            //화장실 케이스 나눠주기

            switch (after_event_state) {
                case 0:

                    Event_Accept.setVisibility(View.VISIBLE);
                    Event_Ing.setVisibility(View.INVISIBLE);
                    Event_Done.setVisibility(View.INVISIBLE);
                    Help_Dis.setVisibility(View.INVISIBLE);
                    Help_Done.setVisibility(View.INVISIBLE);
                    break;
                case 1:

                    if(org.androidtown.findrest.person.getNick().equals(after_event_nick)){
                        Event_Accept.setVisibility(View.INVISIBLE);
                        Event_Ing.setVisibility(View.INVISIBLE);
                        Event_Done.setVisibility(View.INVISIBLE);
                        Help_Dis.setVisibility(View.VISIBLE);
                        Help_Done.setVisibility(View.VISIBLE);

                    }else{
                        Event_Accept.setVisibility(View.INVISIBLE);
                        Event_Ing.setVisibility(View.VISIBLE);
                        Event_Done.setVisibility(View.INVISIBLE);
                        Help_Dis.setVisibility(View.INVISIBLE);
                        Help_Done.setVisibility(View.INVISIBLE);
                    }

                    break;
                case 2:
                        Event_Accept.setVisibility(View.INVISIBLE);
                        Event_Ing.setVisibility(View.INVISIBLE);
                        Event_Done.setVisibility(View.VISIBLE);
                        Help_Dis.setVisibility(View.INVISIBLE);
                        Help_Done.setVisibility(View.INVISIBLE);
                    break;

            }

            Event_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(person.getNick().equals(after_event_nick)){
                        Toast.makeText(watch_event.this,"자신의 이벤트는 수락할 수 없습니다",Toast.LENGTH_SHORT).show();
                    }else{
                        update_event_state=0;
                        Event_Accept.setVisibility(View.INVISIBLE);
                        Event_Ing.setVisibility(View.VISIBLE);
                        Event_Done.setVisibility(View.INVISIBLE);
                        Help_Dis.setVisibility(View.INVISIBLE);
                        Help_Done.setVisibility(View.INVISIBLE);
                        db_manager5=new DB_Manager5();
                        db_manager5.update_event(user_id,json_enroll_time,update_event_state,loginNick);
                        Intent intent = new Intent(watch_event.this, MainActivity.class);
                        startActivity(intent);

                    }


                }
            });
            Help_Dis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update_event_state=1;
                    Event_Accept.setVisibility(View.VISIBLE);
                    Event_Ing.setVisibility(View.INVISIBLE);
                    Event_Done.setVisibility(View.INVISIBLE);
                    Help_Dis.setVisibility(View.INVISIBLE);
                    Help_Done.setVisibility(View.INVISIBLE);
                    db_manager5=new DB_Manager5();
                    db_manager5.update_event(user_id,json_enroll_time,update_event_state,loginNick);
                    Intent intent = new Intent(watch_event.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            Help_Done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    update_event_state=2;
                    Event_Accept.setVisibility(View.INVISIBLE);
                    Event_Ing.setVisibility(View.INVISIBLE);
                    Event_Done.setVisibility(View.VISIBLE);
                    Help_Dis.setVisibility(View.INVISIBLE);
                    Help_Done.setVisibility(View.INVISIBLE);
                    db_manager5=new DB_Manager5();
                    db_manager5.update_event(user_id,json_enroll_time,update_event_state,loginNick);
                    Intent intent = new Intent(watch_event.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            pdLoading.dismiss();
        }
    }

    public String getStringFromUrl(String pUrl) {
        BufferedReader bufreader2 = null;
        HttpURLConnection urlConnection = null;


        StringBuffer page2 = new StringBuffer();

        try {

            URL url = new URL(pUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");

            System.out.println("아이디랑"+user_id+"타임확인"+json_enroll_time);

            String param = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            param += "&" + URLEncoder.encode("event_enroll_time", "UTF-8") + "=" + URLEncoder.encode(json_enroll_time, "UTF-8");

            System.out.println("param확인"+param);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(param.getBytes());
            outputStream.flush();
            outputStream.close();

            BufferedReader in = new BufferedReader(new InputStreamReader
                    (con.getInputStream(), "UTF-8"));

            String line = null;

            while ((line = in.readLine()) != null) {
                Log.d("이벤트라인확인", line);
                page2.append(line);
                System.out.println("Url주소는?2" + page2);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("어디서막히는거야1");
            e.printStackTrace();
        }
        System.out.println("Url주소는?2" + page2);
        return page2.toString();
    }

    public ArrayList<EventDATA> getEventJson() {

        StringBuffer sb = new StringBuffer();
        ArrayList<EventDATA> list = new ArrayList<EventDATA>();
        try {

            String jsonPage2 = getStringFromUrl("http://ksh564.cafe24.com/signup/event_watch.php?");
            System.out.println("이벤트 뽑아보자 시작");
            System.out.println("이벤트 뽑아보자" + jsonPage2);
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonPage2);
            JSONArray test = (JSONArray) jsonObject.get("result");
            System.out.println("이벤트" + test);

            String index, toilet_num,event_cost, toilet_name, event_id, event_nick, event_addr, event_content, event_deadline_time, event_enroll_time, event_state;


            System.out.println("이벤트크기" + test.size());

            for (int k = 0; k < test.size(); k++) {
//                    System.out.println("포문시작");
                JSONObject obj = (JSONObject) test.get(k);
                toilet_name = (String) obj.get("toilet_name");
                event_nick = (String) obj.get("event_nick");
                event_content = (String) obj.get("event_content");
                event_addr = (String) obj.get("event_addr");
                event_cost = (String)obj.get("event_cost");
                event_deadline_time = (String) obj.get("event_deadline_time");
                event_enroll_time = (String) obj.get("event_enroll_time");
                event_state = (String) obj.get("event_state");


                EventDATA entity = new EventDATA();

                entity.setToilet_name(toilet_name);
                entity.setEvent_nick(event_nick);
                entity.setEvent_cost(event_cost);
                entity.setEvent_content(event_content);
                entity.setEvent_addr(event_addr);
                entity.setEvent_deadline_time(event_deadline_time);
                entity.setEvent_enroll_time(event_enroll_time);
                entity.setEvent_state(Integer.parseInt(event_state));

                list.add(entity);

            }

        } catch (Exception e) {
        }
        System.out.println("리스트갯수" + list.size());
        return list;
    }


}
