package org.androidtown.findrest;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-02.
 */
public class PhotoListview {

    private String photourl;

    private ArrayList<String>photoArray;

    public void setPhotoArray(ArrayList<String> photoarray){
        photoArray = photoarray;
    }
    public ArrayList<String> getPhotoArray(){
        return this.photoArray;
    }



    public void setPhoto(String photo){
        photourl = photo;
    }
    public String getPhoto(){
        return this.photourl;
    }
}
