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
 * Created by 김승훈 on 2017-04-06.
 */
public class DB_LIKE {

    private String urlPath;
    private final String signup_user_information_UrlPath = "http://ksh564.cafe24.com/signup/update_like.php";


    static int id_index,review_index,like,dislike,like_status;


    private ArrayList<String> results;

    public ArrayList<String> update_like(int id_index,int  review_index,int like,int dislike,int like_status) {


        urlPath = signup_user_information_UrlPath;
        DB_LIKE.id_index = id_index;
        DB_LIKE.review_index = review_index;
        DB_LIKE.like = like;
        DB_LIKE.dislike = dislike;
        DB_LIKE.like_status=like_status;

        try {
            results = new update_like().execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return results;
    }

    class update_like extends AsyncTask<Void, Void, ArrayList<String>> {


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
                System.out.println("라이크매니저");


                String param = URLEncoder.encode("id_index", "UTF-8") + "=" + id_index;
                param += "&" + URLEncoder.encode("review_index", "UTF-8") + "=" + review_index;
                param += "&" + URLEncoder.encode("like", "UTF-8") + "=" + like;
                param += "&" + URLEncoder.encode("dislike", "UTF-8") + "=" + dislike;
                param += "&" + URLEncoder.encode("like_status", "UTF-8") + "=" + like_status;

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(param.getBytes());
                outputStream.flush();
                outputStream.close();


                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader
                        (con.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = rd.readLine()) != null) {
                    Log.d("업데이트라이크", line);
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
