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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by 김승훈 on 2017-03-28.
 */
public class DB_Manager4 {
    private String urlPath;
    private final String  signup_user_information_UrlPath ="http://ksh564.cafe24.com/fcm/update_token.php";

    private  String token;
    private  String xpos;
    private  String ypos;
    private  String user_id;
    private int user_sex;
    private ArrayList<String> results;

    public ArrayList<String> update_token(String token, String xpos, String ypos,String user_id,int user_sex){
        urlPath = signup_user_information_UrlPath;
        this.token = token;
        this.xpos = xpos;
        this.ypos = ypos;
        this.user_id=user_id;
        this.user_sex=user_sex;

        try{
            results = new update_token().execute().get();
        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        return results;
    }
    class update_token extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try{
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");

                String param = URLEncoder.encode("Token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                param += "&" + URLEncoder.encode("xpos", "UTF-8") + "=" + URLEncoder.encode(xpos, "UTF-8");
                param += "&" + URLEncoder.encode("ypos", "UTF-8") + "=" +URLEncoder.encode(ypos, "UTF-8");
                param += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" +URLEncoder.encode(user_id, "UTF-8");
                param += "&" + URLEncoder.encode("user_sex", "UTF-8") + "=" + user_sex;



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
