package org.androidtown.findrest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.gun0912.tedpermission.PermissionListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

/**
 * Created by 김승훈 on 2017-01-13.
 */
public class signin extends AppCompatActivity {


    HttpURLConnection conn;
    URL url = null;
    person person;
    final int MY_PERMISSION_REQUEST_GPS = 0;
    private final String signin_UrlPath = "http://ksh564.cafe24.com/signup/newlogin.php";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private static boolean loginisset = false;
    static   String user_id ,loginid,loginnick,loginemail,loginimg,loginindex;
    private String loginconfirm;
    static  int loginsex;
    static  String user_password;
    Button Skip,SignUpBtn,Login;

    private  ArrayList<LoginDTO> results;

    static EditText InputId , InputPw;
    private DB_Manager db_manager;

    private CallbackManager callbackManager;
    private LoginButton loginButton;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        setContentView(R.layout.signin);

//        callbackManager = CallbackManager.Factory.create();

        //페이스북 로그인 버튼입니다.
//        loginButton = (LoginButton)findViewById(R.id.facebook_loginbtn);

//        loginButton.setReadPermissions("public_profile", "user_friends","email");
        InputId=(EditText)findViewById(R.id.IdInput);
        InputPw=(EditText)findViewById(R.id.PwInput);

//        new TedPermission(getApplicationContext());
        checkPermission();

//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//                Log.e("토큰",loginResult.getAccessToken().getToken());
//                Log.e("유저아이디",loginResult.getAccessToken().getUserId());
//                Log.e("퍼미션 리스트",loginResult.getAccessToken().getPermissions()+"");
//
//                //loginResult.getAccessToken() 정보를 가지고 유저 정보를 가져올수 있습니다.
//                GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() ,
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(org.json.JSONObject object, GraphResponse response) {
//                                try {
//                                    Log.e("user profile",object.toString());
//                                } catch (Exception e) {
//                                    Log.e("익셉션에러:",e.toString());
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                request.executeAsync();
//
//            }
//
//            @Override
//            public void onCancel() {
//
//              System.out.println("페이스북 로그인 캔슬");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//                System.out.println("페이스북 로그인 에러:"+error);
//            }
//        });

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(),"권한 허가",Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(),"권한 거부\n"+deniedPermissions.toString(),Toast.LENGTH_SHORT).show();

            }
        };





//        new TedPermission(getApplicationContext())
//                .setPermissionListener(permissionListener)
//                .setRationaleMessage("지도 서비스를 이용하기 위해서는 위치 접근 권한이 필요합니다.")
//                .setDeniedMessage("거부하시면 서비스를 이용하지 못합니다.\n이용하시려면 [설정]>[권한]에서 권한을 허용할 수 있습니다.")
//                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
//                .check();

        Skip = (Button)findViewById(R.id.skipbtn);
        SignUpBtn = (Button)findViewById(R.id.SignBtn);
        Login = (Button)findViewById(R.id.loginBtn);




        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_id = InputId.getText().toString();
                user_password = InputPw.getText().toString();

                new Signinlogin().execute();

            }
        });


        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signin.this , MainActivity.class);
                startActivity(intent);

            }
        });
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signin.this, signup.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent,1000);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

        Log.d("RESULT",requestCode+"");
        Log.d("RESULT",resultCode+"");
        Log.d("RESULT",data+"");

        if(requestCode == 1000 && resultCode ==RESULT_OK){
            Toast.makeText(signin.this, "회원가입을 완료했습니다!",Toast.LENGTH_SHORT).show();
            InputId.setText(data.getStringExtra("user_id"));
        }

    }
// gps 퍼미션 구현

    private void checkPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){

                }
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_GPS);
            }else{
                Toast.makeText(this, "Gps Granted",Toast.LENGTH_SHORT).show();
            }
        }


    }
   // gps 퍼미션 결과에 따른 처리과정


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case MY_PERMISSION_REQUEST_GPS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한 허용시
                    Toast.makeText(this, "정상적으로 이용 가능합니다.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "권한사용을 동의해주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

        }

    }

    private class Signinlogin extends AsyncTask<ArrayList, Void, ArrayList> {

        ProgressDialog pdLoading = new ProgressDialog(signin.this);
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
        protected ArrayList doInBackground(ArrayList... arrayLists) {

            return getJsonText();
        }



        @Override
        protected void onPostExecute(ArrayList result) {


            try{
                results=result;
            }catch (Exception e){
                e.printStackTrace();
            }
            for(LoginDTO dto : results){
                loginindex=dto.getIndex();
                loginimg=dto.getImgurl();
                loginid=dto.get_id();
                loginemail=dto.get_email();
                loginnick=dto.get_nickname();
                loginconfirm=dto.get_confirm();
                loginsex=dto.getUser_sex();

            }


            System.out.print("로그인확인"+loginconfirm);

            pdLoading.dismiss();


            if(loginconfirm.equals("true"))
            {
                Intent intent = new Intent(signin.this,MainActivity.class);
                SharedPreferences pref = getSharedPreferences("idbundle",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                loginisset=true;

                editor.putString("idindex",loginindex);
                editor.putBoolean("check",loginisset);

                editor.commit();
                person = new person(loginindex,loginid,loginnick,loginemail,loginimg,loginisset,loginsex);



                startActivity(intent);
                signin.this.finish();


            }else if(loginconfirm.equals("false")){
                System.out.println("로그인컨펌확인"+loginconfirm);
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .repeat(3)
                        .playOn(findViewById(R.id.log_id));
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .repeat(3)
                        .playOn(findViewById(R.id.log_pd));
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .repeat(3)
                        .playOn(findViewById(R.id.IdInput));
                YoYo.with(Techniques.Wobble)
                        .duration(700)
                        .repeat(3)
                        .playOn(findViewById(R.id.PwInput));
                Toast.makeText(signin.this,"아이디 또는 패스워드가 맞지않습니다.", Toast.LENGTH_LONG).show();
            }





        }


    }


    public ArrayList<LoginDTO> getJsonText() {

        StringBuffer sb = new StringBuffer();
        ArrayList<LoginDTO> list = new ArrayList<LoginDTO>();
        try {
            System.out.println("제이슨시작");
            String jsonpage = getStringFromUrl(user_id,user_password);
            System.out.println("로그인주소"+jsonpage);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonpage);
            JSONArray test = (JSONArray) jsonObject.get("dbresult");
            System.out.println("회원가입jsontest"+test);

            String adminid,adminnick,adminemail,adminconfirm,adminimg,adminindex,adminsex;

            JSONObject obj = (JSONObject)test.get(0);

            adminindex=(String)obj.get("index");
            adminid=(String)obj.get("id");
            adminnick=(String)obj.get("nickname");
            adminemail=(String)obj.get("email");
            adminimg=(String)obj.get("profile_img");
            adminsex=(String)obj.get("user_sex");
            if(adminimg!=null){
                adminimg.replaceAll("\\/","/");
            }
            adminconfirm=(String)obj.get("result");

            LoginDTO idset = new LoginDTO();
            idset.setIndex(adminindex);
            idset.set_confirm(adminconfirm);
            idset.setImgurl(adminimg);
            idset.set_id(adminid);
            idset.set_nickname(adminnick);
            idset.set_email(adminemail);
            if(adminsex!=null){
                idset.setUser_sex(Integer.parseInt(adminsex));
            }


            list.add(idset);

//                   return(result.toString());
            return list;

//



        } catch (Exception e) {


        }
        System.out.println("로그인결과물갯수" + list.size());
        return list;

    }


    public String getStringFromUrl(String ...params) {
        try {
            url = new URL(signin_UrlPath);
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
                    .appendQueryParameter("user_id",params[0])
                    .appendQueryParameter("user_password",params[1]);

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

                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line=reader.readLine())!=null){
                    result.append(line);
                    //line이 하는 일은 php서버단에서 정보가져오는거
                }

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



}