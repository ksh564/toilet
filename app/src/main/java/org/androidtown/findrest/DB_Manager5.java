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
 * Created by 김승훈 on 2017-04-01.
 */
public class DB_Manager5 {


        private String urlPath;
        private final String signup_user_information_UrlPath = "http://ksh564.cafe24.com/signup/update_event.php";


        static int event_state;
        static String complete_id,user_id,enroll_time;

        private ArrayList<String> results;

        public ArrayList<String> update_event(String  user_id,String  enroll_time,int event_state,String complete_id) {


            urlPath = signup_user_information_UrlPath;
            DB_Manager5.user_id = user_id;
            DB_Manager5.enroll_time = enroll_time;
            DB_Manager5.event_state = event_state;
            DB_Manager5.complete_id = complete_id;

            try {
                results = new update_event().execute().get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return results;
        }

        class update_event extends AsyncTask<Void, Void, ArrayList<String>> {


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
                    System.out.println("디비매니저2_2");
                    System.out.println("디비매니저2_2태그" + user_id);

                    String param = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
                    param += "&" + URLEncoder.encode("enroll_time", "UTF-8") + "=" + URLEncoder.encode(enroll_time, "UTF-8");
                    param += "&" + URLEncoder.encode("event_state", "UTF-8") + "=" + event_state;
                    param += "&" + URLEncoder.encode("complete_id", "UTF-8") + "=" + URLEncoder.encode(complete_id, "UTF-8");

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(param.getBytes());
                    outputStream.flush();
                    outputStream.close();


                    BufferedReader rd = null;
                    rd = new BufferedReader(new InputStreamReader
                            (con.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = rd.readLine()) != null) {
                        Log.d("디비매니저2버퍼", line);
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