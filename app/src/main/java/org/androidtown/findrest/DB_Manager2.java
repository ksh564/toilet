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
 * Created by 김승훈 on 2017-02-16.
 */
public class DB_Manager2 {

    private String urlPath;
    private final String signup_user_information_UrlPath = "http://ksh564.cafe24.com/signup/register.php";
    private final String signin_UrlPath = "http://ksh564.cafe24.com/signup/signin.php";

    static String toilet_name;
    static String toilet_info;
    static Double toilet_xpos;
    static Double toilet_ypos;
    static String toilet_type;
    static int toilet_sex;
    static int toilet_tag;
    static int toilet_wheel, toilet_diaper, toilet_bidet;
    private ArrayList<String> results;

    public ArrayList<String> signup_user_information(String toilet_name, Double toilet_xpos, Double toilet_ypos,
                                                     String toilet_type, int toilet_sex, int toilet_wheel, int toilet_diaper, int toilet_bidet, String toilet_info, int toilet_tag) {


        urlPath = signup_user_information_UrlPath;
        DB_Manager2.toilet_name = toilet_name;
        DB_Manager2.toilet_tag = toilet_tag;
        DB_Manager2.toilet_xpos = toilet_xpos;
        DB_Manager2.toilet_ypos = toilet_ypos;
        DB_Manager2.toilet_type = toilet_type;
        DB_Manager2.toilet_sex = toilet_sex;
        DB_Manager2.toilet_wheel = toilet_wheel;
        DB_Manager2.toilet_diaper = toilet_diaper;
        DB_Manager2.toilet_bidet = toilet_bidet;
        DB_Manager2.toilet_info = toilet_info;
        try {
            results = new SignupUserInformation().execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return results;
    }

    class SignupUserInformation extends AsyncTask<Void, Void, ArrayList<String>> {


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
                System.out.println("디비매니저2_2태그" + toilet_tag);

                String param = URLEncoder.encode("toilet_tag", "UTF-8") + "=" + toilet_tag;
                param += "&" + URLEncoder.encode("toilet_name", "UTF-8") + "=" + URLEncoder.encode(toilet_name, "UTF-8");
                param += "&" + URLEncoder.encode("toilet_xpos", "UTF-8") + "=" + toilet_xpos;
                param += "&" + URLEncoder.encode("toilet_ypos", "UTF-8") + "=" + toilet_ypos;
                param += "&" + URLEncoder.encode("toilet_type", "UTF-8") + "=" + URLEncoder.encode(toilet_type, "UTF-8");
                param += "&" + URLEncoder.encode("toilet_sex", "UTF-8") + "=" + toilet_sex;
                param += "&" + URLEncoder.encode("toilet_wheel", "UTF-8") + "=" + toilet_wheel;
                param += "&" + URLEncoder.encode("toilet_bidet", "UTF-8") + "=" + toilet_bidet;
                param += "&" + URLEncoder.encode("toilet_diaper", "UTF-8") + "=" + toilet_diaper;
                param += "&" + URLEncoder.encode("toilet_info", "UTF-8") + "=" + toilet_info;


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
