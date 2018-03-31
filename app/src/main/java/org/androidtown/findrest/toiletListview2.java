package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-02-04.
 */
public class toiletListview2 {

    private String NameSource;
    private String ANameSource;
    private Float RatingSource;
    private int TagSource;

    public void setTag(int Tag){
        TagSource = Tag;
    }
    public void setName(String name){
        NameSource = name;
    }
    public void setAname(String Aname){
        ANameSource = Aname;
    }
    public void setRating(Float Rating){
        RatingSource = Rating;
    }

    public int getTag(int Tag){
        return this.TagSource;
    }
    public String getName(){
        return this.NameSource;
    }
    public String getAname(){
        return this.ANameSource;
    }
    public Float getRating(){
        return this.RatingSource;
    }
}
