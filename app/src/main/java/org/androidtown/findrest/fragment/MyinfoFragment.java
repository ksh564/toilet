package org.androidtown.findrest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidtown.findrest.R;
import org.androidtown.findrest.change.change_nick;
import org.androidtown.findrest.other.CircleTransform;
import org.androidtown.findrest.person;

/**
 * Created by 김승훈 on 2017-03-09.
 */
public class MyinfoFragment extends AppCompatActivity implements View.OnClickListener {

    TextView UpperNick ,Email ,Nick;
    ImageView Profile;
    ViewGroup Change_Nick_layout,Change_Pwd_layout,Chang_Review_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_mody);

        Change_Nick_layout=(RelativeLayout)findViewById(R.id.Change_Nick_layout);
        Change_Pwd_layout =(LinearLayout)findViewById(R.id.Change_Pwd_layout);
        Chang_Review_layout=(LinearLayout)findViewById(R.id.Chang_Review_layout);

        UpperNick = (TextView)findViewById(R.id.Upper_nick);
        Email = (TextView)findViewById(R.id.Upper_email);
        Nick = (TextView)findViewById(R.id.Bottom_nick);
        Profile=(ImageView)findViewById(R.id.information_profile);

        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("내 정보");
        person person = new person();

        UpperNick.setText(org.androidtown.findrest.person.getNick());
        Nick.setText(org.androidtown.findrest.person.getNick());
        Email.setText(org.androidtown.findrest.person.getEmail());

        Change_Nick_layout.setOnClickListener(this);
        Chang_Review_layout.setOnClickListener(this);
        Change_Pwd_layout.setOnClickListener(this);
        Profile.setOnClickListener(this);

        Glide.with(this).load(org.androidtown.findrest.person.getId_img())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(Profile);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Change_Nick_layout:
                startActivity(new Intent(MyinfoFragment.this,change_nick.class));

                break;
            case R.id.Change_Pwd_layout:


                break;
            case R.id.Chang_Review_layout:

                break;
            case R.id.information_profile:
                break;
        }
    }


}
