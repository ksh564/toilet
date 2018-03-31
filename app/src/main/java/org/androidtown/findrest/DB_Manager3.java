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
 * Created by 김승훈 on 2017-02-24.
 */
public class DB_Manager3 {
    private String urlPath;
    private final String event_urlpath = "http://ksh564.cafe24.com/signup/event.php";

    static String event_id;
    static String toilet_name,nick_img,event_cost;
    static String event_nick;
    static Double event_xpos;
    static Double event_ypos;
    static String event_daedline,Content,address;
    static int event_state,user_sex;
    static int toilet_number;

    private ArrayList<String> results;

    public ArrayList<String> enroll_event(String event_id,String event_nick, Double event_xpos, Double event_ypos,String address,String Content,
                                                     String event_daedline, int event_state,int toilet_number,String toilet_name,int user_sex,String nick_img,String event_cost) {


        urlPath = event_urlpath;

        DB_Manager3.event_id = event_id;
        DB_Manager3.event_nick = event_nick;
        DB_Manager3.event_xpos = event_xpos;
        DB_Manager3.event_ypos = event_ypos;
        DB_Manager3.address = address;
        DB_Manager3.Content = Content;
        DB_Manager3.event_daedline = event_daedline;
        DB_Manager3.event_state = event_state;
        DB_Manager3.toilet_number = toilet_number;
        DB_Manager3.toilet_name = toilet_name;
        DB_Manager3.user_sex=user_sex;
        DB_Manager3.nick_img=nick_img;
        DB_Manager3.event_cost=event_cost;

        try {
            results = new enroll_event().execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return results;
    }

    class enroll_event extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            StringBuffer page2 = new StringBuffer();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setRequestMethod("POST");
                System.out.println("디비매니저3_2");
                System.out.println("디비매니저3_2태그" + toilet_number);
                System.out.println("디비매니저3_2태그" + user_sex);
                System.out.println("디비매니저3_2태그" + nick_img);

                String param = URLEncoder.encode("event_id", "UTF-8") + "=" + URLEncoder.encode(event_id, "UTF-8");
                param += "&" + URLEncoder.encode("event_nick", "UTF-8") + "=" + URLEncoder.encode(event_nick, "UTF-8");
                param += "&" + URLEncoder.encode("event_xpos", "UTF-8") + "=" + event_xpos;
                param += "&" + URLEncoder.encode("event_ypos", "UTF-8") + "=" + event_ypos;
                param += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");
                param += "&" + URLEncoder.encode("Content", "UTF-8") + "=" + URLEncoder.encode(Content, "UTF-8");
                param += "&" + URLEncoder.encode("event_daedline", "UTF-8") + "=" + URLEncoder.encode(event_daedline, "UTF-8");
                param += "&" + URLEncoder.encode("event_state", "UTF-8") + "=" + event_state;
                param += "&" + URLEncoder.encode("toilet_number", "UTF-8") + "=" + toilet_number;
                param += "&" + URLEncoder.encode("toilet_name", "UTF-8") + "=" + URLEncoder.encode(toilet_name, "UTF-8");
                param += "&" + URLEncoder.encode("user_sex", "UTF-8") + "=" + user_sex;
                param += "&" + URLEncoder.encode("nick_img", "UTF-8") + "=" + URLEncoder.encode(nick_img, "UTF-8");
                param += "&" + URLEncoder.encode("event_cost", "UTF-8") + "=" + URLEncoder.encode(event_cost, "UTF-8");


                OutputStream outputStream = con.getOutputStream();
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();


                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader
                        (con.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = rd.readLine()) != null) {
                    Log.d("디비매니저3버퍼", line);
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(ArrayList<String> qResults) {
            super.onPostExecute(qResults);
        }
    }
}
