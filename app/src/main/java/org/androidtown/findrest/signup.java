package org.androidtown.findrest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by 김승훈 on 2017-01-13.
 */
public class signup extends Activity implements View.OnClickListener {

    public int view_id = 0;
    public String symbolvalue;

    public int Idcheckvar=0;
    public int Nickchekvar=0;

    private final String IdCheck_UrlPath = "http://ksh564.cafe24.com/signup/idCheck.php";
    private final String NicknameCheck_UrlPath = "http://ksh564.cafe24.com/signup/nicknameCheck.php";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    static int user_sex;
    private  DB_Manager db_manager;
    private ArrayList<String> results;


    private EditText su_id;
    private EditText su_nickname;
    private EditText su_passWord;
    private EditText su_passWordAgain;
    private EditText su_email;
    private RadioGroup user_sex_radio;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        user_sex_radio=(RadioGroup)findViewById(R.id.user_sex);
        su_id = (EditText)findViewById(R.id.UserId);
        su_id.setFilters(new InputFilter[]{filter});
        su_nickname=(EditText)findViewById(R.id.UserNickname);
        su_passWord=(EditText)findViewById(R.id.UserPassword);
        su_passWordAgain=(EditText)findViewById(R.id.UserPasswordAgain);
        su_email=(EditText)findViewById(R.id.UserEmail);
        Button bto_agreejoin=(Button)findViewById(R.id.SU_SignupBtn);

        su_id.setOnClickListener(this);
        su_nickname.setOnClickListener(this);
        su_passWord.setOnClickListener(this);
        su_passWordAgain.setOnClickListener(this);
        su_email.setOnClickListener(this);
        bto_agreejoin.setOnClickListener(this);

        user_sex_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if(id==R.id.user_sex_male){
                    user_sex=0;

                }else if(id==R.id.user_sex_female){
                    user_sex=1;

                }

            }
        });


        db_manager = new DB_Manager();

        results = new ArrayList<String>();


    }


    @Override
    public void onClick(View view) {

        String a = su_passWord.getText().toString();
        String b = su_passWordAgain.getText().toString();
        String email = su_email.getText().toString();

        switch (view.getId()){
            case R.id.UserId:
                popup(R.id.UserId,"아이디 입력");
                break;
            case R.id.UserNickname:
                popup(R.id.UserNickname,"닉네임 입력");
                break;
            case R.id.UserPassword:
                popup(R.id.UserPassword,"패스워드 입력");
                break;
            case R.id.UserPasswordAgain:
                popup(R.id.UserPasswordAgain,"패스워드 재입력");
                break;
            case R.id.UserEmail:
                popup(R.id.UserEmail,"이메일 입력");
                break;
            case R.id.SU_SignupBtn:
                if(su_id.length()<1){
                    Toast.makeText(getApplicationContext(),"아이디가 공백입니다!",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(su_nickname.length()<1){
                    Toast.makeText(getApplicationContext(),"닉네임이 공백입니다!",Toast.LENGTH_SHORT).show();
                    Log.e("log", "회원가입은 과연!!!"+Idcheckvar);
                    break;
                }

                if(su_email.length()<1){
                    Toast.makeText(getApplicationContext(),"이메일이 공백입니다!",Toast.LENGTH_SHORT).show();
                    break;
                }

                if(Idcheckvar<2){
                    Toast.makeText(getApplicationContext(),"사용하실 수 없는 아이디입니다!",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(Nickchekvar<2){
                    Toast.makeText(getApplicationContext(),"사용하실 수 없는 닉네임입니다!",Toast.LENGTH_SHORT).show();
                    break;
                }


                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(getApplicationContext(), "이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(a.length()<1) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 공백입니다!", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!a.equals(b))
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();

                else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$",a)) {
                    Toast.makeText(getApplicationContext(), "8~20자 영문 대 소문자,숫자,특수문자를 사용하세요.",Toast.LENGTH_LONG).show();

                }
                else {

                    String user_id = su_id.getText().toString();
                    String user_nickname = su_nickname.getText().toString();
                    String user_password = su_passWord.getText().toString();
                    String user_email = su_email.getText().toString();
                    Log.e("log", "회원가입은 과연!!!"+Idcheckvar);

                    db_manager.signup_user_information(user_id,user_nickname,user_password,user_email,user_sex);
                    Intent mainintent = new Intent();
                    mainintent.putExtra("user_id",user_id);

                    setResult(RESULT_OK, mainintent);
                    finish();

                }
                break;

//
        }
    }


    public void popup(final int id, String title){

        view_id = id;
        Context mcontext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            final AlertDialog.Builder aDialog = new AlertDialog.Builder(signup.this);


            aDialog.setTitle(title);
        if(view_id==R.id.UserId){
            final View layout = inflater.inflate(R.layout.popup_signup,(ViewGroup)findViewById(R.id.poptest));
            aDialog.setView(layout);
        } else if(view_id==R.id.UserNickname){
            final View layout = inflater.inflate(R.layout.popup_signup,(ViewGroup)findViewById(R.id.poptest));
            aDialog.setView(layout);

        } else if(view_id==R.id.UserPassword){
            final View layout = inflater.inflate(R.layout.popup_password,(ViewGroup)findViewById(R.id.poptest));
            aDialog.setView(layout);

        } else if(view_id==R.id.UserPasswordAgain){
            final View layout = inflater.inflate(R.layout.popup_password,(ViewGroup)findViewById(R.id.poptest));
            aDialog.setView(layout);

        }else if(view_id==R.id.UserEmail){
            final View layout = inflater.inflate(R.layout.popup_email,(ViewGroup)findViewById(R.id.poptest));
            aDialog.setView(layout);

        }




            aDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                  if(view_id==R.id.UserId) {
                      EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.edittext);
//                      Drawable drawable = edit.getBackground();
//                      drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
//
//                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                          edit.setBackground(drawable);
//                      }else{
//                          edit.setBackgroundDrawable(drawable);
//                      }

                      String user = edit.getText().toString();

                      new IdCheck().execute(user);



                      Log.e("log", "삽질번호"+Idcheckvar);

                      su_id.setText(user);


                  }

                    if(view_id==R.id.UserNickname){
                        EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.edittext);
                        String user = edit.getText().toString();
                        new NicknameCheck().execute(user);
                        su_nickname.setText(user);

                    }

                    if(view_id==R.id.UserPassword){
                        EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.edittext);
                        Log.e("log", "패스워드에디트"+edit);

                        String user = edit.getText().toString();
                        su_passWord.setText(user);

                    }

                    if(view_id==R.id.UserPasswordAgain){
                        EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.edittext);
                        Log.e("log", "패스워드확인에디트"+edit);
                        edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        String user = edit.getText().toString();
                        su_passWordAgain.setText(user);

                    }

                    if(view_id==R.id.UserEmail){
                        EditText edit = (EditText) ((AlertDialog) dialog).findViewById(R.id.edittext);
                        String user = edit.getText().toString();
                        su_email.setText(user);
                    }


                }
            });


            AlertDialog ad = aDialog.create();
//        Button nbutton = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
//        nbutton.setTextColor();
//        Button pbutton = ad.getButton(DialogInterface.BUTTON_POSITIVE);
//        pbutton.setTextColor(0xbaff5787);
            ad.getWindow().setBackgroundDrawable(new ColorDrawable(0xba43e6b6));
                    ad.show();


    }

    private class IdCheck extends AsyncTask<String, String, String>{

        ProgressDialog pdLoading = new ProgressDialog(signup.this);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\t로딩중......");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(IdCheck_UrlPath);
            }catch (MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }
            try{

                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id",params[0]);


                String query = builder.build().getEncodedQuery();
                Log.e("log", "쿼리는?"+query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.e("log", "버퍼쓰기"+writer);
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Log.e("log", "버퍼라이터들어오는거");
            }
            catch (IOException e1){
                e1.printStackTrace();
                return "exception";
            }try{
                int response_code = conn.getResponseCode();
                if(response_code ==HttpURLConnection.HTTP_OK){
                    Log.e("log", "리스폰스응답");
                    InputStream input = conn.getInputStream();
                    Log.e("log", "리스폰스응답");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    Log.e("log", "라인");
                    while ((line=reader.readLine())!=null){
                        result.append(line);
                        Log.e("log", "라인"+line); //line이 하는 일은 php서버단에서 정보가져오는거
                    }
                    Log.e("log", "라인리더"+result);
                    return(result.toString());
                }else{
                    return ("unsuccessful");
                }

            }catch (IOException e){
                e.printStackTrace();
                return "exception";

            }finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {

                Idcheckvar = 1;
                Log.e("log", "onPostExecute"+Idcheckvar);
                Toast.makeText(signup.this, "이미 사용중이거나 탈퇴한 아이디입니다.",Toast.LENGTH_LONG).show();



            }else if(result.equalsIgnoreCase("false")){


                 Idcheckvar = 2;
                Log.e("log", "onPostExecute"+Idcheckvar);
                Toast.makeText(signup.this,"멋진 아이디네요!", Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("exception")|| result.equalsIgnoreCase("unsuccessful")){

                if(result.equalsIgnoreCase("exception")){
                    Toast.makeText(signup.this, "뭔가 잘못되었다, exception이야",Toast.LENGTH_LONG).show();

                }else if(result.equalsIgnoreCase("unsuccessful")){
                    Toast.makeText(signup.this, "뭔가 잘못되었다, unsuccessful이야",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(signup.this, "뭐가 더 잘못됨.",Toast.LENGTH_LONG).show();
            }

        }
    }

    private class NicknameCheck extends AsyncTask<String, String, String>{

        ProgressDialog pdLoading = new ProgressDialog(signup.this);
        HttpURLConnection conn;
        URL url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\t확인중......");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(NicknameCheck_UrlPath);
            }catch (MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }
            try{

                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_nickname",params[0]);


                String query = builder.build().getEncodedQuery();
                Log.e("log", "쿼리는?"+query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                Log.e("log", "버퍼쓰기"+writer);
                writer.write(query);

                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Log.e("log", "버퍼라이터들어오는거");
            }
            catch (IOException e1){
                e1.printStackTrace();
                return "exception";
            }try{
                int response_code = conn.getResponseCode();
                if(response_code ==HttpURLConnection.HTTP_OK){
                    Log.e("log", "리스폰스응답");
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    Log.e("log", "라인");
                    while ((line=reader.readLine())!=null){
                        result.append(line);
                        Log.e("log", "라인"+line); //line이 하는 일은 php서버단에서 정보가져오는거
                    }
                    Log.e("log", "라인리더"+result);
                    return(result.toString());
                }else{
                    return ("unsuccessful");
                }

            }catch (IOException e){
                e.printStackTrace();
                return "exception";

            }finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {

                Nickchekvar = 1;
                Log.e("log", "onPostExecute"+Idcheckvar);
                Toast.makeText(signup.this, "이미 사용중인 닉네임입니다.",Toast.LENGTH_LONG).show();



            }else if(result.equalsIgnoreCase("false")){


                Nickchekvar = 2;
                Log.e("log", "onPostExecute"+Idcheckvar);
                Toast.makeText(signup.this,"멋진 닉네임이네요!.", Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("exception")|| result.equalsIgnoreCase("unsuccessful")){

                if(result.equalsIgnoreCase("exception")){
                    Toast.makeText(signup.this, "뭔가 잘못되었다, exception이야",Toast.LENGTH_LONG).show();

                }else if(result.equalsIgnoreCase("unsuccessful")){
                    Toast.makeText(signup.this, "뭔가 잘못되었다, unsuccessful이야",Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(signup.this, "뭐가 더 잘못됨.",Toast.LENGTH_LONG).show();
            }

        }
    }
    protected InputFilter filter= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end,

                                   Spanned dest, int dstart, int dend) {



            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");

            if (!ps.matcher(source).matches()) {

                return "";

            }

            return null;

        }

    };

}
