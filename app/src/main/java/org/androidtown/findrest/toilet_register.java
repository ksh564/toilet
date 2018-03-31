package org.androidtown.findrest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-02-15.
 */
public class toilet_register extends AppCompatActivity {

    static int toilet;
    boolean mInitSpinner;
    protected int tag;

    static   String toilet_name;
    static  String toilet_info;
    static  Double toilet_xpos;
    static  Double toilet_ypos;
    static  String toilet_type;
    static  int toilet_sex ,toilet_tag;
    static  boolean toilet_wheel,toilet_daiper,toilet_bidet;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private  DB_Manager2 db_manager;
    private ArrayList<String> results;


    static int radio_bidet,radio_wheel,radio_diaper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_toilet);
        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);

        db_manager = new DB_Manager2();





        TextView toilet_register_name = (TextView)findViewById(R.id.toilet_register_name);
//        TextView toilet_register_ypos = (TextView)findViewById(R.id.toilet_register_ypos);
//        TextView toilet_register_xpos = (TextView)findViewById(R.id.toilet_register_xpos);
//        EditText toilet_register_type =(EditText)findViewById(R.id.toilet_register_type);
        final Bundle intent = getIntent().getExtras();



        if(intent!=null){

        System.out.println("태그값을 보여줘!!!"+intent.getInt("tag2"));


            toilet_tag=intent.getInt("tag2");
            toilet_name=intent.getString("tag2_name");
//            toilet_type=intent.getString("tag2_type");
            toilet_xpos=intent.getDouble("tag2_xpos");
            toilet_ypos=intent.getDouble("tag2_ypos");


            toilet_register_name.setText(toilet_name);
//            toilet_register_xpos.setText(toilet_xpos.toString());
//            toilet_register_ypos.setText(toilet_ypos.toString());
//            toilet_register_type.setText(toilet_type);


        }


        setSupportActionBar(mtoolbar);



        results = new ArrayList<String>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);
        actionBar.setTitle("화장실등록");

        Spinner spinner = (Spinner)findViewById(R.id.toilet_spinner);
        Spinner aname_spinner = (Spinner)findViewById(R.id.toilet_aname_spinner);
        spinner.setPrompt("화장실을 선택하세요");

        final ArrayAdapter<CharSequence> aname_adapter = ArrayAdapter.createFromResource(this, R.array.Aname,android.R.layout.simple_spinner_item);

        aname_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        aname_spinner.setAdapter(aname_adapter);

        aname_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(position==0){
                    toilet_type="공중화장실";
                }
                else if(position==1){
                    toilet_type="개방화장실";
                }else if(position==2){
                    toilet_type="비개방화장실";
                }else if(position==3){
                    toilet_type="공공청사화장실";
                }else{
                    toilet_type="지하철화장실";
                }

//                Toast.makeText(toilet_register.this,aname_adapter.getItem(position)+"가 선택되었습니다.그리고"+toilet_type,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.toilet,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if(mInitSpinner==false){
                    mInitSpinner=true;
                    return;
                }


                if(position==0){
                    toilet_sex=0;

                }
                else if(position==1){
                    toilet_sex=1;
                }else if(position==2){
                    toilet_sex=2;
                }else if(position==3){
                    toilet_sex=3;
                }

//                Toast.makeText(toilet_register.this,adapter.getItem(position)+"가 선택되었습니다.그리고"+toilet_sex,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RadioButton wheelbutton_yes = (RadioButton)findViewById(R.id.radio_wheel_ok);
        RadioButton wheelbutton_no = (RadioButton)findViewById(R.id.radio_wheel_no);
        RadioButton diaperbutton_yes = (RadioButton)findViewById(R.id.radio_diaper_ok);
        RadioButton diaperbutton_no = (RadioButton)findViewById(R.id.radio_diaper_no);
        RadioButton bidetbutton_yes = (RadioButton)findViewById(R.id.radio_bidet_ok);
        RadioButton bidetbutton_no = (RadioButton)findViewById(R.id.radio_bidet_no);

        RadioGroup wheelgroup = (RadioGroup)findViewById(R.id.radio_wheel);
        RadioGroup diapergroup = (RadioGroup)findViewById(R.id.radio_diaper);
        RadioGroup bidetgroup = (RadioGroup)findViewById(R.id.radio_bidet);

        wheelgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if(id==R.id.radio_wheel_ok){
                    radio_wheel=1;
//                    Toast.makeText(toilet_register.this,"라디오휠"+radio_wheel+"입니다",Toast.LENGTH_SHORT).show();
                }else if(id==R.id.radio_wheel_no){
                    radio_wheel=0;
//                    Toast.makeText(toilet_register.this,"라디오휠"+radio_wheel+"입니다",Toast.LENGTH_SHORT).show();
                }

            }
        });
        diapergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if(id==R.id.radio_diaper_ok){
                    radio_diaper=1;
//                    Toast.makeText(toilet_register.this,"라디오다이퍼"+radio_diaper+"입니다",Toast.LENGTH_SHORT).show();
                }else if(id==R.id.radio_diaper_no){
                    radio_diaper=0;
//                    Toast.makeText(toilet_register.this,"라디오다이퍼"+radio_diaper+"입니다",Toast.LENGTH_SHORT).show();
                }

            }
        });
        bidetgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if(id==R.id.radio_bidet_ok){
                    radio_bidet=1;
//                    Toast.makeText(toilet_register.this,"라디오비데"+radio_bidet+"입니다",Toast.LENGTH_SHORT).show();
                }else if(id==R.id.radio_bidet_no){
                    radio_bidet=0;
//                    Toast.makeText(toilet_register.this,"라디오비데"+radio_bidet+"입니다",Toast.LENGTH_SHORT).show();
                }

            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.complete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.complete){


            EditText toilet_register_info = (EditText)findViewById(R.id.toilet_register_info);
            System.out.println("디비매니저2시작전_1");
            toilet_info=toilet_register_info.getText().toString();
            System.out.println("디비매니저2시작전_2");
            db_manager.signup_user_information(toilet_name,toilet_xpos,toilet_ypos,toilet_type,toilet_sex,radio_wheel,radio_diaper,radio_bidet,toilet_info,toilet_tag);
            System.out.println("디비매니저2시작전_3");
            Intent intent = new Intent(toilet_register.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"정보수정 완료료",Toast.LENGTH_SHORT).show();
//            db_manager.signup_user_information()

            return  true;
        }    else if(id==android.R.id.home){
            System.out.println("눌리냐");
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
