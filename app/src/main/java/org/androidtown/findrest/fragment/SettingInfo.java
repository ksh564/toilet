package org.androidtown.findrest.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.androidtown.findrest.R;

import java.io.IOException;

/**
 * Created by 김승훈 on 2017-03-09.
 */
public class SettingInfo extends AppCompatActivity {

    private NotificationManager notification;
    static boolean btnchecked;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settinginfo);
        btnchecked=false;
        SharedPreferences pref = getSharedPreferences("button",MODE_PRIVATE);
        btnchecked=pref.getBoolean("check",false);

        Toast.makeText(SettingInfo.this,"처음들어올때 과연",Toast.LENGTH_SHORT).show();

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);



        notification  = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Switch sw =(Switch)findViewById(R.id.alertswitch);
        sw.setChecked(btnchecked);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked==true){
                    System.out.println("인스턴스 아이디 확인전"+FirebaseInstanceId.getInstance());
                        btnchecked=true;
                        FirebaseInstanceId.getInstance().getId();
                        Toast.makeText(SettingInfo.this,"인스턴스아이디="+FirebaseInstanceId.getInstance().getId(),Toast.LENGTH_SHORT).show();




                }else{
                    System.out.println("flase=인스턴스 아이디 확인전"+FirebaseInstanceId.getInstance());
                    btnchecked=false;
                    DeleteTokenTask deleteTokenTask=new DeleteTokenTask();
                    deleteTokenTask.execute();
                    Toast.makeText(SettingInfo.this,"인스턴스아이디="+FirebaseInstanceId.getInstance(),Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("button",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("check",btnchecked);
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {

            }
            return null;
        }
    }

}
