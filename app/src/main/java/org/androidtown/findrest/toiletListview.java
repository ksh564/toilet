package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-01-13.
 */
public class toiletListview {

    private String NameSource;
    private Float DistanceSource;
    private Float RatingSource;
    private int TagSource;


    public void setName(String name){
        NameSource = name;
    }
    public String getName(){
        return this.NameSource;
    }
    public void setDistance(Float Distance){
        DistanceSource = Distance;
    }
    public void setRating(Float Rating){
        RatingSource = Rating;
    }

    public void setTag(int Tag){
        TagSource = Tag;
    }
    public int getTag(){
        return this.TagSource;
    }


    public Float getDistance(){
        return this.DistanceSource;
    }
    public Float getRating(){
        return this.RatingSource;
    }

}
