package org.androidtown.findrest;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 김승훈 on 2017-01-12.
 */
public class fragment_two extends Fragment implements AdapterView.OnItemClickListener{
    private int a;

    private View view;
    //    public final ArrayList<toiletListview2> ToiletItem = new ArrayList<toiletListview2>();
    public final ArrayList<ToiletDTO> ToiletItem = new ArrayList<ToiletDTO>();
    static ArrayList<ToiletDTO> list2 = null;
    EditText searchengine;
    ImageButton search;
    static  String searchword;
    TextView GPSHELPER;

    ToiletListview2adp adapter2;
    ListView listView, listView2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("log", "onCreateView_2");
//        view = inflater.inflate(R.layout.fragment_two, container, false);
        if(view==null){
//            container.removeAllViews();
        }try{
            view = inflater.inflate(R.layout.fragment_two, container, false);

        }catch (InflateException e){
            e.printStackTrace();
        }





        ArrayList<ToiletDTO> list = null;

        double distance;
        int ParseIsOk = 0;
        int a = 3;
        float d = (float) a;
        search=(ImageButton)view.findViewById(R.id.searchView);
        searchengine=(EditText)view.findViewById(R.id.search_engine);
        GPSHELPER=(TextView)view.findViewById(R.id.gpshelper);

        listView = (ListView) view.findViewById(R.id.listView1);
        listView.setOnItemClickListener(this);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(GPSHELPER.getVisibility()==View.VISIBLE){
                    GPSHELPER.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }


                System.out.println("서치엔진클릭확인");
                searchword=searchengine.getText().toString();
                new ToiletJsonParser2().execute();
            }
        });
        return view;

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


        Intent intent = new Intent(getActivity(), Toilet_info.class);
        intent.putExtra("toilet_name_2", ToiletItem.get(position).getName());
        intent.putExtra("toilet_tag_2", ToiletItem.get(position).getPoi());

        intent.putExtra("toilet_Aname_2", ToiletItem.get(position).getAName());
        intent.putExtra("toilet_xpos_2", ToiletItem.get(position).getXpos());
        intent.putExtra("toilet_ypos_2", ToiletItem.get(position).getYpos());


        startActivity(intent);

    }



    class ToiletJsonParser2 extends AsyncTask<ArrayList, Void, ArrayList> {


        @Override
        protected void onPreExecute() {


            MyLocation myLocation = new MyLocation();


            System.out.println("마이로케이션" + myLocation);


            System.out.println("마이로케이션이란?" + myLocation);

//            System.out.println("마이로케이션.겟로케이션?"+Mylatitude);

        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {

            return getJsonText();
        }


        @Override
        protected void onPostExecute(ArrayList result) {

            System.out.println("온포스트막히는부분1");
            ToiletJsonParser2 parser = new ToiletJsonParser2();
            ArrayList<ToiletDTO> list = null;
            adapter2 = new ToiletListview2adp(ToiletItem);
            listView.setAdapter(adapter2);
            double distance;
            int ParseIsOk = 0;
            int a = 3;
            String addr;
            float d = (float) a;
            if (adapter2.isEmpty() == false) {
                ToiletItem.clear();
                adapter2.notifyDataSetChanged();
            }


            try {
                System.out.println("온포스트막히는부분2");
                list2 = result;
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (ToiletDTO entity : list2) {

                adapter2.addItem(entity.getName(), entity.getAddr(), entity.getRating(), entity.getPoi());

            }
            adapter2.notifyDataSetChanged();

        }


        public ArrayList<ToiletDTO> getJsonText() {

            StringBuffer sb = new StringBuffer();
            ArrayList<ToiletDTO> list = new ArrayList<ToiletDTO>();
            try {

                String encode_searchword= URLEncoder.encode(searchword,"UTF-8");
                String jsonPage = getStringFromUrl("http://ksh564.cafe24.com/signup/getdata_search.php?search="+encode_searchword);

                System.out.println("프래그먼트2jsonpage" + jsonPage);
                JSONParser jsonParser = new JSONParser();


                JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonPage);
                JSONArray test = (JSONArray) jsonObject.get("result");
                System.out.println("프래그먼트2jsonObject" + test);

                String index = null, name = null, Aname = null, ypos = null, xpos = null, Addr = null,avgrating=null;



                for (int k = 0; k < test.size(); k++) {
//
                    JSONObject obj = (JSONObject) test.get(k);
                    xpos = (String) obj.get("x_pos");
                    ypos = (String) obj.get("y_pos");
                    index = (String) obj.get("index");
                    Addr = (String)obj.get("oldaddr");
                    name = (String) obj.get("name");
                    Aname = (String) obj.get("Aname");
                    avgrating=(String)obj.get("avgrating");
                    System.out.println("문제가 예상되는 부분");
//                    Addr = getLocation(Double.valueOf(ypos), Double.valueOf(xpos));
                    System.out.println("문제가 예상되는 부분1");
//                    String mstring = new String(name.getBytes("8859_1"),"utf-8");
//                    System.out.println("프2네임"+mstring);

                    ToiletDTO entity = new ToiletDTO();
                    System.out.println("문제가 예상되는 부분2");
                    entity.setPoi(Integer.parseInt(index));
                    System.out.println("문제가 예상되는 부분3");
                    entity.setXpos(Double.valueOf(xpos));
                    System.out.println("문제가 예상되는 부분4");
                    entity.setYpos(Double.valueOf(ypos));
                    System.out.println("문제가 예상되는 부분5");
                    entity.setName(name);
                    System.out.println("문제가 예상되는 부분6");
                    entity.setAddr(Addr);
                    System.out.println("문제가 예상되는 부분7");
                    entity.setAname(Aname);
                    System.out.println("문제가 예상되는 부분8");
                    entity.setRating(Float.parseFloat(avgrating));

                    list.add(entity);

                }

            } catch (Exception e) {


            }
            System.out.println("리스트갯수" + list.size());
            System.out.println("문제가 예상되는 부분9");
            return list;

        }

        public String getStringFromUrl(String pUrl){
            BufferedReader bufreader2 = null;
            HttpURLConnection urlConnection = null;


            StringBuffer page2 = new StringBuffer();


            try{
                URL url2 = new URL(pUrl);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                System.out.println("Url주소는?"+url2);
                request.setURI(new URI(pUrl));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF-8"));

                String line = null;
//                line=bufreader2.readLine();
//                String ecnoding = new String(line.getBytes("8859_1"),"EUC-KR");

                while((line=in.readLine())!=null){
                    Log.d("토일렛인포버라인" , line);
                    System.out.println("Url주소는?"+url2);
                    page2.append(line);
                    System.out.println("Url주소는?2"+page2);
                }

            }catch (IOException e){
                System.out.println("어디서막히는거야1");
                e.printStackTrace();
            } catch (URISyntaxException e2) {
                System.out.println("어디서막히는거야2");
                e2.printStackTrace();
            }


//            finally {
//                try {
//                    System.out.println("어디서막히는거야3-1");
////                    bufreader2.close();
//                    System.out.println("어디서막히는거야3-2");
////                    urlConnection.disconnect();
//                    System.out.println("어디서막히는거야3-3");
//                }catch (IOException e3){
//                    System.out.println("어디서막히는거야4");
//                    e3.printStackTrace();
//                }
//            }
            System.out.println("Url주소는?2"+page2);
            return page2.toString();
        }
//        public String getStringFromUrl(String pUrl) {
//            BufferedReader bufreader2 = null;
//            HttpURLConnection urlConnection = null;
//
//            StringBuffer page = new StringBuffer();
//
//
//            try {
//                URL url2 = new URL(pUrl);
//                urlConnection = (HttpURLConnection) url2.openConnection();
//
//
//                InputStream contentStream2 = urlConnection.getInputStream();
//                int BUFFERSIZE = 8192;
//
//                bufreader2 = new BufferedReader(new InputStreamReader(contentStream2, "UTF-8"));
//                String line = null;
////                line=bufreader2.readLine();
////                String ecnoding = new String(line.getBytes("8859_1"),"EUC-KR");
//
//                while ((line = bufreader2.readLine()) != null) {
//                    Log.d("line", line);
//                    page.append(line);
//                }
//
//
//            } catch (IOException e) {
//                System.out.println("프래그먼트2에서 막히는부분1");
//                e.printStackTrace();
//            } finally {
//                try {
//                    System.out.println("프래그먼트2에서 막히는부분2");
//                    bufreader2.close();
//                    urlConnection.disconnect();
//                } catch (IOException e) {
//                    System.out.println("프래그먼트2에서 막히는부분3");
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("Url주소는?프래그먼트2" + page);
//            return page.toString();
//        }
    }

    public String getLocation(double lat, double lng) {
        String str = null;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);
        List<Address> addresses;
        try {
            if (geocoder != null) {
                addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && addresses.size() > 0) {
                    str = addresses.get(0).getAddressLine(0).toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;

    }
    @Override
    public void onStart() {
        Log.e("log", "onStart_2");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("log", "onResume_2");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("log", "onPause_2");

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("log", "onStop_2");

        super.onStop();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.e("log", "onDestroyView_2");
//        ViewGroup  parent = (ViewGroup )view.getParent();
//        if(parent!=null){
//            parent.removeView(view);
//        }

    }

    @Override
    public void onDestroy() {
        Log.e("log", "onDestroy_2");
        super.onDestroy();
    }

}
