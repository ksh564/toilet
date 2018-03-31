package org.androidtown.findrest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.androidtown.findrest.other.Current_Location;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

/**
 * Created by 김승훈 on 2017-01-12.
 */
public class fragment_one extends Fragment implements AdapterView.OnItemClickListener {


    static double Mylatitude, Mylongitude, distance;
    static ArrayList resultlist;
    private LocationManager locationManager;
    private MapView mMapView;
    static double my_xpos, my_ypos, mydistance;
    static int confirm,user_sex;
    org.androidtown.findrest.other.map_poi map_poi;
    DB_Manager4 db_manager4;

    static String token,strmylat,strymylong,user_id;

    static MapLayout mapLayout;
    static MapPoint mMapPoint;
    float v = 10;
    public String DAUM_MAPS_ANDROID_APP_API_KEY = "15b1197c0f821122fdbe68b7d896e50b";

    private View view;
    public final ArrayList<toiletListview> ToiletItem = new ArrayList<toiletListview>();
    //    ToiletListview1adp adapter;
    ListView listView;
    static ListView listView2;
    static ToiletListview1adp adapter2;
    static ArrayList<ToiletDTO> list2 = null;
    static ToiletDTO entity;
    static ArrayList<ToiletDTO> list = null;
    Current_Location current_location;

    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.d(gps, "쥐피에스 권한세팅");
        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(getActivity());
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용,GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다." +
                    "\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            return;
                        }
                    }).create().show();
            return false;


        } else {
            return true;
        }


    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("log", "onCreateView_1");
        if(view==null){
//            container.removeAllViews();
        }try{
            view = inflater.inflate(R.layout.fragment_one, container, false);

        }catch (InflateException e){
            e.printStackTrace();
        }

        confirm=13;

        Bundle arguments = new Bundle();
        arguments.putInt("test",confirm);

        

        db_manager4 = new DB_Manager4();
        MapLayout mapLayout = new MapLayout(getActivity());
        chkGpsService();
        mMapView = mapLayout.getMapView();

        mMapView.setDaumMapApiKey(DAUM_MAPS_ANDROID_APP_API_KEY);

        mMapView.setMapViewEventListener(mapViewEventListener);
        mMapView.setPOIItemEventListener(poiItemEventListener);
        mMapView.setCurrentLocationEventListener(currentLocationEventListener);
        mMapView.setMapType(MapView.MapType.Standard);


        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.setFocusable(false);
        mapViewContainer.addView(mapLayout);

        //아이디 세팅

        person person = new person();
        user_id = org.androidtown.findrest.person.getId();
        user_sex=person.getUser_sex();

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);


        Spinner radius_spinner = (Spinner) view.findViewById(R.id.radius_spinner);


        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        radius_spinner.setAdapter(adapter);
        radius_spinner.setSelection(0);

        radius_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int ParseIsOk = 0;
                int a = 3;
                float d = (float) a;




               if (position == 0) {

                    if (mMapPoint != null) {
                        mMapView.removeAllPOIItems();


                        my_xpos = mMapPoint.getMapPointGeoCoord().longitude;
                        my_ypos = mMapPoint.getMapPointGeoCoord().latitude;

                        mydistance = 0.1;
//                        setUpMapIfNeeded();
                        try {
                            new ToiletJsonParser().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 200));

                    } else {
                        Toast.makeText(getActivity(), "현재 위치를 불러오는 중입니다, 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                    }


                } else if (position == 1) {

                    if (mMapPoint != null) {
                        mMapView.removeAllPOIItems();
                        my_xpos = mMapPoint.getMapPointGeoCoord().longitude;
                        my_ypos = mMapPoint.getMapPointGeoCoord().latitude;
                        mydistance = 0.5;
                        try {
                            new ToiletJsonParser().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 1000));
                    } else {
                        Toast.makeText(getActivity(), "현재 위치를 불러오는 중입니다, 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else if (position == 2) {
                    if (mMapPoint != null) {
                        mMapView.removeAllPOIItems();
                        my_xpos = mMapPoint.getMapPointGeoCoord().longitude;
                        my_ypos = mMapPoint.getMapPointGeoCoord().latitude;
                        mydistance = 1.0;
                        try {
                            new ToiletJsonParser().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 2000));
                    } else {
                        Toast.makeText(getActivity(), "현재 위치를 불러오는 중입니다, 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    if (mMapPoint != null) {
                        mMapView.removeAllPOIItems();
                        my_xpos = mMapPoint.getMapPointGeoCoord().longitude;
                        my_ypos = mMapPoint.getMapPointGeoCoord().latitude;
                        mydistance = 2.0;
                        try {
                            new ToiletJsonParser().execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 4000));
                    } else {
                        Toast.makeText(getActivity(), "현재 위치를 불러오는 중입니다, 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        int a = 3;
        float c = (float) a;
        float d = (float) a;
        int b1 = 4;
        int c1 = 13;
        float b = (float) b1;
        float c2 = (float) c1;

        return view;
    }


    private void setUpMapIfNeeded() {

        if (mMapView != null) {
            setUpMap();

        }

    }

    private void setUpMap() {


        System.out.println("셋업맵에들어갔다.");


        try {
            new ToiletJsonParser().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    MapView.POIItemEventListener poiItemEventListener = new MapView.POIItemEventListener() {


        @Override
        public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

            int tag;
            tag = mapPOIItem.getTag();
            Intent intent = new Intent(getActivity(), Toilet_info.class);
            intent.putExtra("toilet_tag_2", tag);
//            intent.putExtra("Toilet_name", mapPOIItem.getItemName());
//            intent.putExtra("Toilet_ypos", mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude);
//            intent.putExtra("Toilet_xpos", mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude);
//            intent.putExtra("Aname", mapPOIItem.getUserObject().toString());

            startActivity(intent);

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
            //deprecated
        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

//            int tag;
//            tag = mapPOIItem.getTag();
//            Intent intent = new Intent(getActivity(), Toilet_info.class);
//            intent.putExtra("toilet_tag_2", tag);
////            intent.putExtra("Toilet_name", mapPOIItem.getItemName());
////            intent.putExtra("Toilet_ypos", mapPOIItem.getMapPoint().getMapPointGeoCoord().latitude);
////            intent.putExtra("Toilet_xpos", mapPOIItem.getMapPoint().getMapPointGeoCoord().longitude);
////            intent.putExtra("Aname", mapPOIItem.getUserObject().toString());
//
//            startActivity(intent);


        }

        @Override
        public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

        }
    };

    MapView.CurrentLocationEventListener currentLocationEventListener = new MapView.CurrentLocationEventListener() {
        @Override
        public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

            mMapPoint = mapPoint;
             // ypoi = latitude 위도
            Mylatitude = mapPoint.getMapPointGeoCoord().latitude;
            // xpoi=longtitude   경도
            Mylongitude = mapPoint.getMapPointGeoCoord().longitude;

            current_location = new Current_Location(Mylongitude,Mylatitude);

            map_poi.setXpoi(Mylatitude);
            map_poi.setYpoi(Mylongitude);
            token = FirebaseInstanceId.getInstance().getToken();



            if(token!=null){

                if(person.isLoginstate()){
                    strmylat=Double.toString(Mylatitude);
                    strymylong=Double.toString(Mylongitude);
                    System.out.println("위도:"+strmylat+"경도:"+strymylong);

                    db_manager4.update_token(token,strymylong,strmylat,user_id,user_sex);

                }
            }




        }

        @Override
        public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

        }

        @Override
        public void onCurrentLocationUpdateFailed(MapView mapView) {

        }

        @Override
        public void onCurrentLocationUpdateCancelled(MapView mapView) {

        }
    };


    MapView.MapViewEventListener mapViewEventListener = new MapView.MapViewEventListener() {


        @Override
        public void onMapViewInitialized(MapView mapView) {
            Log.e("log", "onMapViewInitialized");
//            int meter=1000;
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


//            mMapView.setCurrentLocationRadius(meter); //현위치 마커를 중심으로 그릴 원의 반경을 지정
//            mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.49238614627172,126.90983237468618),true);

        }

        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewCenterPointMoved");


        }

        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {
            Log.e("log", "onMapViewZoomLevelChanged");

        }

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewSingleTapped");

        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewDoubleTapped");

        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewLongPressed");

        }

        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewDragStarted");
        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewDragEnded");
        }

        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
            Log.e("log", "onMapViewMoveFinished");

        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("클릭이되고있습니다.");
        Intent intent = new Intent(getActivity(), Toilet_info.class);
        intent.putExtra("toilet_tag_2", ToiletItem.get(position).getTag());
        System.out.println("클릭이되고있습니다." + ToiletItem.get(position).getTag());
        startActivity(intent);

    }


    class ToiletJsonParser extends AsyncTask<ArrayList, Void, ArrayList> {

        ProgressDialog pdLoading = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {

            pdLoading.setMessage("\t로딩중......");
            pdLoading.setCancelable(false);
            pdLoading.show();

            MyLocation myLocation = new MyLocation();

            System.out.println("마이로케이션" + myLocation);

            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {

                    Mylatitude = location.getLatitude();
                    Mylongitude = location.getLongitude();
//                    System.out.println("값 저장 전 내위도"+Mylatitude);
//                    System.out.println("값 저장 전 내경도"+Mylongitude);
                }
            };


            System.out.println("마이로케이션이란?" + myLocation);
//            System.out.println("마이로케이션이란?"+Mylatitude);
            myLocation.getLocation(getActivity(), locationResult);
            System.out.println("마이로케이션.겟로케이션?" + myLocation.getLocation(getActivity(), locationResult));
//            System.out.println("마이로케이션.겟로케이션?"+Mylatitude);

        }

        @Override
        protected ArrayList doInBackground(ArrayList... strs) {

            return getJsonText();
        }


        @Override
        protected void onPostExecute(ArrayList result) {


            ToiletJsonParser parser = new ToiletJsonParser();


            ToiletListview1adp adapter2 = new ToiletListview1adp(ToiletItem);
            listView.setAdapter(adapter2);

            int ParseIsOk = 0;
            int a = 3;
            float d = (float) 2.7;
            try {
                list = result;
                resultlist = result;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (adapter2.isEmpty() == false) {
                ToiletItem.clear();
                adapter2.notifyDataSetChanged();
            }
            for (ToiletDTO entity2 : list) {

                MapPOIItem options = new MapPOIItem();

                options.setItemName(entity2.getName());
                options.setTag(entity2.getPoi());
                options.setUserObject(entity2.getAName());

                options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity2.getYpos(), entity2.getXpos()));

//                           if(entity2.getAName().equals("민간개방화장실")||equals("지하철"))
                if (entity2.get_toilet_sex() == 0) {
                    options.setMarkerType(MapPOIItem.MarkerType.CustomImage);

                    options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    options.setCustomImageResourceId(R.mipmap.marker_uni);

                } else if (entity2.get_toilet_sex() == 1) {
                    options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    options.setCustomImageResourceId(R.mipmap.marker_mann);
                    //남자전용
                } else if (entity2.get_toilet_sex() == 2) {
                    options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    options.setCustomImageResourceId(R.mipmap.marker_woman);
                    //여자전용
                } else if(entity2.get_toilet_sex()==3) {
                    //남녀공용
                    options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    options.setCustomImageResourceId(R.mipmap.marker_uni);
                }else{
                    options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    options.setCustomImageResourceId(R.drawable.quest_mark);
                }

                options.setCustomImageAutoscale(false);
                options.setCustomImageAnchor(0.5f, 1.0f);

                Location locationA = new Location("내현재위치");
                locationA.setLatitude(Mylatitude);
                locationA.setLongitude(Mylongitude);
                Location locationB = new Location("마커위치");
                locationB.setLatitude(entity2.getYpos());
                locationB.setLongitude(entity2.getXpos());
                distance = locationA.distanceTo(locationB);
                float c = (float) distance;
//                System.out.println("플롯자리수 변환전"+c);
                Float num = Float.parseFloat(String.format("%.1f", c));

                mMapView.addPOIItem(options);
                adapter2.addItem(entity2.getName(), num, entity2.getRating(), entity2.getPoi());
            }
            adapter2.notifyDataSetChanged();
            if (adapter2.getCount() == 0) {

                Toast.makeText(getActivity(), "해당 범위 내의 화장실이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

            pdLoading.dismiss();


//            for (ToiletDTO entity : list) {
//
//                MapPOIItem options = new MapPOIItem();
//
//                options.setItemName(entity.getName());
//                options.setTag(entity.getPoi());
//                options.setUserObject(entity.getAName());
//
//                options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity.getYpos(), entity.getXpos()));
//                options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                options.setCustomImageResourceId(R.drawable.toilet_marker);
//                options.setCustomImageAutoscale(false);
//                options.setCustomImageAnchor(0.5f, 1.0f);
//
//                Location locationA = new Location("내현재위치");
//                locationA.setLatitude(Mylatitude);
//                locationA.setLongitude(Mylongitude);
//                Location locationB = new Location("마커위치");
//                locationB.setLatitude(entity.getYpos());
//                locationB.setLongitude(entity.getXpos());
//                distance = locationA.distanceTo(locationB);
////                System.out.println("디스턴스"+distance);
//                float c = (float) distance;
////                System.out.println("플롯자리수 변환전"+c);
//                Float num = Float.parseFloat(String.format("%.1f", c));
////                System.out.println("플롯자리수 변환후"+num);
//
////                calDistance(Mylatitude,Mylongitude,)
//                mMapView.addPOIItem(options);

//
////             끝
//            }

//            Spinner radius_spinner = (Spinner) view.findViewById(R.id.radius_spinner);
//
//
//            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.radius, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//            radius_spinner.setAdapter(adapter);
//
//            radius_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                    int ParseIsOk = 0;
//                    int a = 3;
//                    float d = (float) a;
//
//                  ToiletListview1adp adapter = new ToiletListview1adp();
//                    listView.setAdapter(adapter);
//
////                    MapCircle circle1 = new MapCircle(MapPoint.mapPointWithGeoCoord(mMapPoint.getMapPointGeoCoord().latitude,mMapPoint.getMapPointGeoCoord().longitude),50, Color.argb(128,255,0,0),Color.argb(128,0,255,0));
////                    circle1.setTag(1);
//
//                        if( position==0){
//
//                    }
//
//                    else if (position == 1) {
//
//
//                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 100));
//                        mMapView.removeAllPOIItems();
//                            for(ToiletDTO entity2 :list){
//
//                            MapPOIItem options = new MapPOIItem();
//
//                            options.setItemName(entity2.getName());
//                            options.setTag(entity2.getPoi());
//                            options.setUserObject(entity2.getAName());
//
//                            options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity2.getYpos(), entity2.getXpos()));
//                            options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                            options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                            options.setCustomImageResourceId(R.drawable.toilet_marker);
//                            options.setCustomImageAutoscale(false);
//                            options.setCustomImageAnchor(0.5f, 1.0f);
//
//                            Location locationA = new Location("내현재위치");
//                            locationA.setLatitude(Mylatitude);
//                            locationA.setLongitude(Mylongitude);
//                            Location locationB = new Location("마커위치");
//                            locationB.setLatitude(entity2.getYpos());
//                            locationB.setLongitude(entity2.getXpos());
//                            distance = locationA.distanceTo(locationB);
//                            float c = (float) distance;
////                System.out.println("플롯자리수 변환전"+c);
//                Float num = Float.parseFloat(String.format("%.1f", c));
////                System.out.println("플롯자리수 변환후"+num);
////                System.out.println("디스턴스"+distance);
//
//                            if(distance<100){
//                                mMapView.addPOIItem(options);
//                                adapter.addItem(entity2.getName(), num, d);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        if(adapter.getCount()==0){
//
//                            Toast.makeText(getActivity(), "해당 범위 내의 화장실이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else if (position == 2) {
//                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 500));
//                        mMapView.removeAllPOIItems();
//                        for(ToiletDTO entity2 :list){
//
//                            MapPOIItem options = new MapPOIItem();
//
//                            options.setItemName(entity2.getName());
//                            options.setTag(entity2.getPoi());
//                            options.setUserObject(entity2.getAName());
//
//                            options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity2.getYpos(), entity2.getXpos()));
//                            options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                            options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                            options.setCustomImageResourceId(R.drawable.toilet_marker);
//                            options.setCustomImageAutoscale(false);
//                            options.setCustomImageAnchor(0.5f, 1.0f);
//
//                            Location locationA = new Location("내현재위치");
//                            locationA.setLatitude(Mylatitude);
//                            locationA.setLongitude(Mylongitude);
//                            Location locationB = new Location("마커위치");
//                            locationB.setLatitude(entity2.getYpos());
//                            locationB.setLongitude(entity2.getXpos());
//                            distance = locationA.distanceTo(locationB);
////                System.out.println("디스턴스"+distance);
//                            float c = (float) distance;
//
////                System.out.println("플롯자리수 변환전"+c);
//                            Float num = Float.parseFloat(String.format("%.1f", c));
//
//                            if(distance<500){
//                                mMapView.addPOIItem(options);
//                                adapter.addItem(entity2.getName(), num, d);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        if(adapter.getCount()==0){
//
//                            Toast.makeText(getActivity(), "해당 범위 내의 화장실이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        }
////                        mMapView.removeCircle(circle1);
//                    } else if (position == 3) {
//                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 1000));
//                        mMapView.removeAllPOIItems();
//                        for(ToiletDTO entity2 :list){
//
//                            MapPOIItem options = new MapPOIItem();
//
//                            options.setItemName(entity2.getName());
//                            System.out.println("마커의이름"+entity2.getName());
//                            options.setTag(entity2.getPoi());
//                            System.out.println("마커의인덱스"+entity2.getPoi());
//                            options.setUserObject(entity2.getAName());
//
//                            options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity2.getYpos(), entity2.getXpos()));
//                            options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                            options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                            options.setCustomImageResourceId(R.drawable.toilet_marker);
//                            options.setCustomImageAutoscale(false);
//                            options.setCustomImageAnchor(0.5f, 1.0f);
//
//                            Location locationA = new Location("내현재위치");
//                            locationA.setLatitude(Mylatitude);
//                            locationA.setLongitude(Mylongitude);
//                            Location locationB = new Location("마커위치");
//                            locationB.setLatitude(entity2.getYpos());
//                            locationB.setLongitude(entity2.getXpos());
//                            distance = locationA.distanceTo(locationB);
////                System.out.println("디스턴스"+distance);
//                            float c = (float) distance;
//
////                System.out.println("플롯자리수 변환전"+c);
//                            Float num = Float.parseFloat(String.format("%.1f", c));
//                            if(distance<1000){
//                                mMapView.addPOIItem(options);
//                                adapter.addItem(entity2.getName(), num, d);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                        if(adapter.getCount()==0){
//
//                            Toast.makeText(getActivity(), "해당 범위 내의 화장실이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        mMapView.moveCamera(CameraUpdateFactory.newMapPointAndDiameter(mMapPoint, 2000));
//                        mMapView.removeAllPOIItems();
//                       for(ToiletDTO entity2 :list){
//
//                           MapPOIItem options = new MapPOIItem();
//
//                           options.setItemName(entity2.getName());
//                           options.setTag(entity2.getPoi());
//                           options.setUserObject(entity2.getAName());
//
//                           options.setMapPoint(MapPoint.mapPointWithGeoCoord(entity2.getYpos(), entity2.getXpos()));
//
////                           if(entity2.getAName().equals("민간개방화장실")||equals("지하철"))
//                           if(entity2.get_toilet_sex()==1){
//                               options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                               options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                               options.setCustomImageResourceId(R.drawable.man_toilet_icon);
//                               //남자전용
//                           }else if(entity2.get_toilet_sex()==2){
//                               options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                               options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                               options.setCustomImageResourceId(R.drawable.woman_toilet_icon);
//                               //여자전용
//                           }else{
//                               //남녀공용
//                               options.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//                               options.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
//                               options.setCustomImageResourceId(R.drawable.toilet_unisex_icon);
//                           }
//
//                           options.setCustomImageAutoscale(false);
//                           options.setCustomImageAnchor(0.5f, 1.0f);
//
//                           Location locationA = new Location("내현재위치");
//                           locationA.setLatitude(Mylatitude);
//                           locationA.setLongitude(Mylongitude);
//                           Location locationB = new Location("마커위치");
//                           locationB.setLatitude(entity2.getYpos());
//                           locationB.setLongitude(entity2.getXpos());
//                           distance = locationA.distanceTo(locationB);
//                           float c = (float) distance;
////                System.out.println("플롯자리수 변환전"+c);
//                           Float num = Float.parseFloat(String.format("%.1f", c));
////                System.out.println("플롯자리수 변환후"+num);
////                System.out.println("디스턴스"+distance);
////                System.out.println("디스턴스"+distance);
//
//                           if(distance<2000){
//                               mMapView.addPOIItem(options);
//                               adapter.addItem(entity2.getName(), num, d);
//
//                           }
//
//                       }
//                        adapter.notifyDataSetChanged();
//                        if(adapter.getCount()==0){
//
//                            Toast.makeText(getActivity(), "해당 범위 내의 화장실이 존재 하지 않습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        }

        public ArrayList<ToiletDTO> getJsonText() {

            StringBuffer sb = new StringBuffer();
            ArrayList<ToiletDTO> list = new ArrayList<ToiletDTO>();
            try {

//                String jsonPage = getStringFromUrl("http://openapi.seoul.go.kr:8088/54645970566b73683137784479417a/json/SearchPublicToiletPOIService/1/200/");
                String jsonPage = getStringFromUrl("http://ksh564.cafe24.com/signup/distance2.php?my_xpos=" + my_xpos + "&my_ypos=" + my_ypos + "&distance=" + mydistance);

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonPage);
               JSONArray test = (JSONArray) jsonObject.get("result");
                System.out.println("프래그먼트1jsonObject" + test);

                String name = null, Aname = null;
                Double new_x = null, new_y = null;
                String xpos = null, ypos = null, sex = null, index = null,avgrating=null;


                for (int k = 0; k <test.size(); k++) {

                    JSONObject obj = (JSONObject) test.get(k);
                    Aname = (String) obj.get("Aname");
                    name = (String) obj.get("name");
                    index = (String) obj.get("index");
                    xpos = (String) obj.get("x_pos");
                    sex = (String) obj.get("sex");
                    avgrating=(String)obj.get("avgrating");
                    ypos = (String) obj.get("y_pos");


                    ToiletDTO entity = new ToiletDTO();
                    entity.setXpos(Double.valueOf(xpos));
                    entity.setYpos(Double.valueOf(ypos));
                    entity.setRating(Float.parseFloat(avgrating));
                    entity.setPoi(Integer.valueOf(index));
                    entity.set_toilet_sex(Integer.valueOf(sex));
                    entity.setName(name);
                    entity.setAname(Aname);


                    list.add(entity);

                }

            } catch (Exception e) {


            }
            System.out.println("프래그먼트1리스트갯수" + list.size());
            return list;

        }
    }

    public String getStringFromUrl(String pUrl) {
        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer();
        StringBuffer page2 = new StringBuffer();


        try {

            //페이징 전 통신 방식
            URL url2 = new URL(pUrl);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            System.out.println("Url주소는?" + url2);
            request.setURI(new URI(pUrl));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

            String line = null;
//                line=bufreader2.readLine();
//                String ecnoding = new String(line.getBytes("8859_1"),"EUC-KR");

            while ((line = in.readLine()) != null) {
                Log.d("토일렛인포버라인", line);
                System.out.println("Url주소는?" + url2);
                page2.append(line);
                System.out.println("Url주소는?2" + page2);
            }


        } catch (IOException e) {
            System.out.println("프래그먼트1에서 막히는부분1");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        return page2.toString();
    }

    Comparator<Double> cmasc = new Comparator<Double>() {
        @Override
        public int compare(Double i1, Double i2) {
            return i1.compareTo(i2);
        }
    };

    public double calDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

        return dist;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg) {
        return deg * Math.PI / 180d;
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return rad * 180d / Math.PI;
    }


    @Override
    public void onStart() {
        Log.e("log", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("log", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("log", "onPause");

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("log", "onStop");

        super.onStop();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        Log.e("log", "onDestroyView");
//        ViewGroup  parent = (ViewGroup )view.getParent();
//        if(parent!=null){
//            parent.removeView(view);
//        }

    }

    @Override
    public void onDestroy() {
        Log.e("log", "onDestroy");
        super.onDestroy();
    }
}
