package org.androidtown.findrest.other;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.daum.mf.map.api.MapView;

import org.androidtown.findrest.DB_Manager3;
import org.androidtown.findrest.MainActivity;
import org.androidtown.findrest.R;
import org.androidtown.findrest.person;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by 김승훈 on 2017-03-21.
 */
public class enroll_event extends AppCompatActivity {

    private DB_Manager3 db_manager3;
    static int tag,event_state,user_sex;
    static String address,toilet_name,user_id,user_nick,user_img;
    static double event_xpos,event_ypos;
    TextView nickname,event_deadline,event_addr,event_addr_name,content_number;
    EditText edit_reward,event_content;
    String result="";
    DecimalFormat df;
    String day,time,append_Day;

    int mYear , mMonth, mDay , mHour ,mMinute;

    private MapView mMapView;
    public String DAUM_MAPS_ANDROID_APP_API_KEY = "15b1197c0f821122fdbe68b7d896e50b";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_event);
        db_manager3 = new DB_Manager3();

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("이벤트 등록");


        //화장실 정보에서 보내온 인텐트 처리
        Bundle intent = getIntent().getExtras();
        tag=intent.getInt("tag_num");
        address=intent.getString("address");
        toilet_name=intent.getString("toilet_name");
        event_xpos=intent.getDouble("event_xpos");
        event_ypos=intent.getDouble("event_ypos");

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.timepicker);

        //각 항목들의 대한 아이디 지정
        nickname=(TextView)findViewById(R.id.enroll_event_id);
        event_deadline=(TextView)findViewById(R.id.enroll_event_input_dayview);
        event_addr=(TextView)findViewById(R.id.enroll_event_address);
        event_addr_name=(TextView)findViewById(R.id.enroll_current_name);
        edit_reward=(EditText)findViewById(R.id.reward_edittext);
        event_content=(EditText)findViewById(R.id.event_content);
        content_number=(TextView)findViewById(R.id.write_opinion_textnum);

        event_addr.setText(address);
        event_addr_name.setText(toilet_name);

//        df = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            df = new DecimalFormat("###,###.####");
//        }

            java.util.Calendar calendar = java.util.Calendar.getInstance();



            mYear = calendar.get((calendar.YEAR));
            mMonth = calendar.get((calendar.MONTH));
            mDay = calendar.get((calendar.DAY_OF_MONTH));
            mHour = calendar.get((calendar.HOUR_OF_DAY));
            mMinute = calendar.get((calendar.MINUTE));






        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        new DatePickerDialog(enroll_event.this,mDateSetListener,mYear,mMonth,mDay).show();

            }
        });

        //사용자 아이디 받는 부분
        person person = new person();

        user_sex=person.getUser_sex();
        user_id=person.getId();
        user_nick=person.getNick();
        user_img=person.getId_img();
        nickname.setText(person.getNick());



//        //돈을 입력 받는 부분에서 소숫점 처리
//        edit_reward.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(!s.toString().equals(result)){     // StackOverflow를 막기위해,
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        result = df.format(Long.parseLong(s.toString().replaceAll(",", "")));   // 에딧텍스트의 값을 변환하여, result에 저장.
//                    }
//                    edit_reward.setText(result);    // 결과 텍스트 셋팅.
//                    edit_reward.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        //컨텐츠 입력 이벤트 처리 하는 부분
        event_content.addTextChangedListener(new TextWatcher() {

            String strcur;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                strcur=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>1000){
                    event_content.setText(strcur);
                    event_content.setSelection(start);
                }else{
                    content_number.setText(String.valueOf(s.length()));
                }

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



   //oncrate 끝
    }
    //각종 이벤트 처리 하는 부분


    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth=monthOfYear;
            mDay=dayOfMonth;
            firstwork();

        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourofDay, int Minute) {
            mHour = hourofDay;
            mMinute=Minute;
            lastwork();
        }
    };
    void  firstwork(){
         day=String.format("%d/%d/%d",mYear,mMonth+1,mDay);
        new TimePickerDialog(enroll_event.this,onTimeSetListener,mHour,mMinute,false).show();
    }
    void lastwork(){
        time=String.format("%d:%d",mHour,mMinute);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(day);
        stringBuilder.append(" ");
        stringBuilder.append(time);

        event_deadline.setText(stringBuilder);
        Toast.makeText(enroll_event.this,stringBuilder,Toast.LENGTH_SHORT).show();
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

        switch (id){
            case R.id.save:

                event_state=0;
                System.out.println("1"+user_id);
                System.out.println("2"+user_nick);
                System.out.println("3"+event_xpos);
                System.out.println("4"+event_ypos);
                System.out.println("5"+address);
                System.out.println("6"+event_content.getText().toString());
                System.out.println("7"+event_deadline.getText().toString());
                System.out.println("8"+event_state);
                System.out.println("9"+tag);


                db_manager3.enroll_event(user_id,user_nick,event_xpos,event_ypos,address,event_content.getText().toString(),event_deadline.getText().toString(),event_state,tag,toilet_name,user_sex,user_img,edit_reward.getText().toString());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.home:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

   // url을 parameter로 받고 GET방식으로 통신하는것
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
}
