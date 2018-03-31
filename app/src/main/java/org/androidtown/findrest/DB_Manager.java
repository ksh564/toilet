package org.androidtown.findrest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by 김승훈 on 2017-01-18.
 */
public class DB_Manager {

    private String urlPath;
    private final String  signup_user_information_UrlPath ="http://ksh564.cafe24.com/signup/signup_user_information.php";
    private final String signin_UrlPath = "http://ksh564.cafe24.com/signup/signin.php";

    private  String user_id;
    private  String user_nickname;
    private  String user_password;
    private  String user_email;
    private  int user_sex;
    private  ArrayList<String> results;

  public ArrayList<String> signup_user_information(String user_id, String user_nickname, String user_password, String user_email,int user_sex){
      urlPath = signup_user_information_UrlPath;
      this.user_id = user_id;
      this.user_nickname = user_nickname;
      this.user_password = user_password;
      this.user_email = user_email;
      this.user_sex = user_sex;
      try{
          results = new SignupUserInformation().execute().get();
      }catch (InterruptedException e){
          e.printStackTrace();
      }catch (ExecutionException e){
          e.printStackTrace();
      }
      return results;
  }
    class SignupUserInformation extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try{
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                String param = "user_id="+user_id+"&user_nickname="+user_nickname+"&user_password="+user_password+"&user_email="+user_email+"&user_sex="+user_sex;
                Log.d("스트링파람",param);
                OutputStream outputStream = con.getOutputStream();

                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();

                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
                String line = null;
                while((line = rd.readLine())!=null){
                    Log.d("BufferdReader",line);
                }

            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(ArrayList<String> qResults){
            super.onPostExecute(qResults);
        }

    }


}
