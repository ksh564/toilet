package org.androidtown.findrest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yongbeam.y_photopicker.util.photopicker.PhotoPagerActivity;
import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;
import com.yongbeam.y_photopicker.util.photopicker.utils.YPhotoPickerIntent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-01.
 */
public class write_opinion extends AppCompatActivity implements imageGridAdapter.ListBtnClickListener {

    TextView messageText;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String upLoadServerUri = null;
    final String uploadFilepath = "storage/emulated/0/";
    final String uploadFileName = "test";

    static ArrayList shi;
    TextView photonumber ,textnumber;

    final int MY_PERMISSION_REQUEST_STORAGE = 0;
    final int INTENT_PHOTO = 10;
    static String user_id, user_nick, user_nickimg, user_email,toilet_name;
    static int toilet_num;
    private boolean isSetimage = false;
    EditText inputContent;
    RatingBar inputRating;

    ArrayList selectedPhotos = new ArrayList<>();
    final imageGridAdapter gridAdapter = new imageGridAdapter(selectedPhotos, this, this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wirte_opinion);


        Bundle intent = getIntent().getExtras();
        toilet_num = intent.getInt("tag_num");
        toilet_name = intent.getString("toilet_name");


        dialog = new ProgressDialog(write_opinion.this);
        textnumber = (TextView)findViewById(R.id.write_opinion_textnum);
        inputContent = (EditText) findViewById(R.id.opinion_edit);
        inputContent.addTextChangedListener(new TextWatcher() {
            String strcur;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                    strcur=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>1000){
                    inputContent.setText(strcur);
                    inputContent.setSelection(start);
                }else{
                    textnumber.setText(String.valueOf(s.length()));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputRating = (RatingBar) findViewById(R.id.opinion_rating);
        LayerDrawable stars = (LayerDrawable) inputRating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter( Color.parseColor("#43e6b6"), PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1).setColorFilter( Color.parseColor("#43e6b6"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter( Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//        DrawableCompat.setTint(stars, Color.parseColor("#43e6b6"));
//        messageText = (TextView)findViewById(R.id.uploadtest);
//        messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");

        upLoadServerUri = "http://ksh564.cafe24.com/signup/UploadToServer.php";
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.A)

        SharedPreferences prefs = getSharedPreferences("idbundle", MODE_PRIVATE);

        person person = new person();
        user_id = org.androidtown.findrest.person.getId();
        user_email = org.androidtown.findrest.person.getEmail();
        user_nick = org.androidtown.findrest.person.getNick();
        user_nickimg = org.androidtown.findrest.person.getId_img();

//        Toast.makeText(write_opinion.this, "인텐트"+toilet_num+toilet_name, Toast.LENGTH_SHORT).show();

        photonumber=(TextView)findViewById(R.id.write_opinion_photonum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.new_backbutton);
        actionBar.setTitle("리뷰 하기");

        ImageButton gallerybutton = (ImageButton) findViewById(R.id.insert_gallery_icon);


        gallerybutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });


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
        switch (id) {

            case android.R.id.home:
                System.out.println("눌리냐");
                finish();
                return true;

            case R.id.save:

                if (inputContent.length() < 1) {
                    Toast.makeText(getApplicationContext(), "내용이 공백입니다!", Toast.LENGTH_SHORT).show();
                    break;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                            messageText.setText("uploading started....");
                            }
                        });


                        if (isSetimage) {
                            uploadFile(selectedPhotos);
                        } else {
                            uploadFile(shi);

                        }


                    }
                }).start();
                Intent intent = new Intent(write_opinion.this, MainActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Read/Write external Storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_STORAGE);
        } else {
            YPhotoPickerIntent intent = new YPhotoPickerIntent(write_opinion.this);
            intent.setMaxSelectCount(10);
            intent.setShowCamera(false);
            intent.setShowGif(false);
            intent.setSelectCheckBox(true);
            intent.setMaxGrideItemCount(4);
            System.out.println("카메라문제점 찾기1");
            System.out.println("카메라문제점 찾기2+인텐트리절트" + INTENT_PHOTO);
            startActivityForResult(intent, INTENT_PHOTO);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //권한 허용시
                    YPhotoPickerIntent intent = new YPhotoPickerIntent(write_opinion.this);
                    intent.setMaxSelectCount(10);
                    intent.setShowCamera(false);
                    intent.setShowGif(false);
                    intent.setSelectCheckBox(true);
                    intent.setMaxGrideItemCount(4);
                    startActivityForResult(intent, INTENT_PHOTO);
                } else {
                    Toast.makeText(write_opinion.this, "권한사용을 동의해주셔야 이용이 가능합니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ArrayList<String> photos = null;

        System.out.println("카메라문제점 찾기3+인텐트리절트" + INTENT_PHOTO);
        System.out.println("카메라문제점 찾기4+리절트오케이?" + RESULT_OK);

        int i = 0;
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_PHOTO) {
                photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                Log.e("log", "Photos:" + photos + "photo.size()" + photos.size());
                photonumber.setText(String.valueOf(photos.size()));
                GridView gridView = (GridView) findViewById(R.id.gridview);
                gridView.setAdapter(gridAdapter);
                isSetimage = true;


                if (photos != null) {
                    selectedPhotos.addAll(photos);
                    Log.e("log", "photos selectedphotos" + selectedPhotos);
                }
                Intent startactivity = new Intent(write_opinion.this, PhotoPagerActivity.class);
                startactivity.putStringArrayListExtra("photos", selectedPhotos);
                startActivity(startactivity);

            }
        }
    }

    @Override
    public void onListBtnClick(View v, int position) {
        System.out.println("그리드뷰클릭되나 확인합니다");

        switch (v.getId()) {
            case R.id.viewpager_image:
                selectedPhotos.remove(position);
                gridAdapter.notifyDataSetChanged();
                photonumber.setText(String.valueOf(selectedPhotos.size()));
                break;

        }


    }

    public int uploadFile(ArrayList sourceFileUri) {
        ArrayList fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        System.out.println("파일업로드전파일경로" + sourceFileUri);


//        if(isSetimage){
//            dialog.dismiss();
//            Log.e("uploadFile", "Source File not exist :"
//                    +uploadFilepath + "" + uploadFileName);
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    messageText.setText("Source File not exist :"
//                            +uploadFilepath + "" + uploadFileName);
//
//                }
//            });
//            return 0;

//         if(isSetimage){
        try {

            URL url = new URL(upLoadServerUri);

//                conn = (HttpURLConnection)url.openConnection();
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//                conn.setUseCaches(false);
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Connection", "Keep-Alive");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


//                    System.out.println("파일업로드후리퀘스트프로퍼티" + fileName.get(i).toString());


            if (isSetimage) {


            }


            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                    conn.setRequestProperty("uploaded_file_"+i, fileName.get(i).toString());

            dos = new DataOutputStream(conn.getOutputStream());
            int i = 0;
            if (isSetimage) {
            for (i = 0; i < sourceFileUri.size(); i++) {
                File sourceFile = new File(sourceFileUri.get(i).toString());


                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition:form-data; name=\"uploaded_file_" + i + "\";filename=\"" + fileName.get(i).toString() + "\"" + lineEnd);
                    System.out.println("uploaded_file_" + i);

                    dos.writeBytes(lineEnd);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while ((bytesRead > 0)) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dos.writeBytes(lineEnd);

                }
            }

            String encoded_id = URLEncoder.encode(user_id, "UTF-8");
            String encoded_nick = URLEncoder.encode(user_nick, "UTF-8");
            String encoded_nickimg = URLEncoder.encode(user_nickimg, "UTF-8");
            String encoded_content = URLEncoder.encode(inputContent.getText().toString(), "UTF-8");
            String encoded_toilet_name = URLEncoder.encode(toilet_name,"UTF-8");


            if (isSetimage) {
                String encoded_isSetImage = URLEncoder.encode("true", "UTF-8");
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition:form-data; name=\"isSetImage\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(encoded_isSetImage + lineEnd);

            } else {
                String encoded_isSetImage = URLEncoder.encode("false", "UTF-8");
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition:form-data; name=\"isSetImage\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(encoded_isSetImage + lineEnd);

            }
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"id\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(encoded_id + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"nickname\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(encoded_nick + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"nickimg\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(encoded_nickimg + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"content\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(encoded_content + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"toiletnum\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(toilet_num + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"toiletrating\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(inputRating.getRating() + lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition:form-data; name=\"toiletname\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(encoded_toilet_name + lineEnd);


            //이미지가 있으면 이미지 사이즈를 받아 넘겨주는 post
            if(isSetimage){
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition:form-data; name=\"imgsize\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(sourceFileUri.size() + lineEnd);
            }



            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(line);

                    System.out.println("파일 전송 결과 : " + sb.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (serverResponseCode == 200) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                + uploadFileName;

//                            messageText.setText(msg);
                        Toast.makeText(write_opinion.this, "File Upload Complete.",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
//                fileInputStream.close();
            dos.flush();
            dos.close();


        } catch (FileNotFoundException ex) {
            dialog.dismiss();
            ex.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                        messageText.setText("MalformedURLException Exception : check script url.");
                    Toast.makeText(write_opinion.this, "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            dialog.dismiss();
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                        messageText.setText("Got Exception : see logcat ");
                    Toast.makeText(write_opinion.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();

                }
            });

        }

//        }
        dialog.dismiss();
        return serverResponseCode;
    }
}
