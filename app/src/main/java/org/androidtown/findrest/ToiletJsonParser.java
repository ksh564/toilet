package org.androidtown.findrest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-01-20.
 */
 class ToiletJsonParser10 extends AsyncTask<ArrayList, Void, ArrayList>{




    @Override
    protected ArrayList doInBackground(ArrayList... strs) {
        return getJsonText();
    }

    @Override
    protected void onPostExecute(ArrayList result) {
        super.onPostExecute(result);
    }







    public ArrayList<ToiletDTO> getJsonText() {

        StringBuffer sb = new StringBuffer();
        ArrayList<ToiletDTO> list = new ArrayList<ToiletDTO>() ;
        try{

            String jsonPage = getStringFromUrl("http://openapi.seoul.go.kr:8088/54645970566b73683137784479417a/json/SearchPublicToiletPOIService/1/1/");

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject =  (JSONObject)jsonParser.parse(jsonPage);
            JSONObject json = (JSONObject) jsonObject.get("SearchPublicToiletPOIService");

            String  name=null;
            Double ypos=null,xpos=null;


            JSONArray array = (JSONArray)json.get("row");

            for(int i=0; i<array.size(); i++) {
                JSONObject entitiy = (JSONObject) array.get(i);

                for(int j=0; j<array.size(); j++) {
                    System.out.println("포문시작");
                    JSONObject obj = (JSONObject) array.get(j);

                    xpos=(Double) obj.get("X_WGS84");
                    System.out.println("xpos:"+xpos);
                    ypos=(Double)obj.get("Y_WGS84");
                    System.out.println("ypos:"+ypos);
                    name=(String)obj.get("FNAME");
                    System.out.println("name:"+name);

                    ToiletDTO entity = new ToiletDTO();
                    entity.setXpos(Double.valueOf(xpos));
                    entity.setYpos(Double.valueOf(ypos));
                    entity.setName(name);

                    list.add(entity);

                }

            }






        }catch (Exception e){


        }
        System.out.println("리스트갯수"+list.size());
        return list;

    }

    public String getStringFromUrl(String pUrl){
        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer();


        try{
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();
            bufreader = new BufferedReader(new InputStreamReader(contentStream , "UTF-8"));
            String line = null;

            while((line =bufreader.readLine())!=null){
                Log.d("line" , line);
                page.append(line);
            }


        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufreader.close();
                urlConnection.disconnect();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return page.toString();
    }

    public static void main(String[] args){

    }
}



