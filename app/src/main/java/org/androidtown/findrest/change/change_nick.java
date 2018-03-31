package org.androidtown.findrest.change;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import org.androidtown.findrest.R;
import org.androidtown.findrest.fragment.MyinfoFragment;
import org.androidtown.findrest.person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 김승훈 on 2017-03-10.
 */
public class change_nick extends AppCompatActivity {
    person person;
    private final String UrlPath = "http://ksh564.cafe24.com/signup/Upload_Nick.php";
    EditText nickname;
    static String ChangeIdTag,ChangeNickname;
    String results;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_nick_layout);

        nickname = (EditText)findViewById(R.id.change_nick_nick);
        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);
        actionBar.setTitle("내 정보");

        person= new person();
        nickname.setText(org.androidtown.findrest.person.getNick());



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



            new Change_information(org.androidtown.findrest.person.getIndex_id(),nickname.getText().toString()).execute();

            return  true;
        }  else if(id==android.R.id.home){
            System.out.println("눌리냐");
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class Change_information extends AsyncTask<Void,Void,String>{
        String index,nick;

         Change_information(String index,String nick){
             this.index=index;
             this.nick=nick;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(UrlPath);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                String param = URLEncoder.encode("Index", "UTF-8") + "=" +  URLEncoder.encode(index, "UTF-8");
                System.out.println("아이디바꾸는패럼확인1"+index);
                param += "&" + URLEncoder.encode("NickName", "UTF-8") + "=" + URLEncoder.encode(nick, "UTF-8");
                System.out.println("아이디바꾸는패럼확인2"+nick);
                System.out.println("아이디바꾸는패럼확인3"+param);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();

                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader
                        (con.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = rd.readLine()) != null) {
                    Log.d("체인지닉네임", line);
                    results=line;
                }



            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("체인지닉네임무슨일!!"+result);

            if(result.equals("update_ok")){
                Toast.makeText(change_nick.this,"닉네임이 변경 되었습니다.",Toast.LENGTH_SHORT).show();
                org.androidtown.findrest.person.setNick(nickname.getText().toString());
                Intent intent = new Intent(change_nick.this,MyinfoFragment.class);
                startActivity(intent);
            }else{
                Toast.makeText(change_nick.this,"변경 과정에서 오류가 생겼습니다.",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
