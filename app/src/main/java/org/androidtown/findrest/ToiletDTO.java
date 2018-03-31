package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-01-31.
 */
public class ToiletDTO {


    private double xpos;
    private double ypos;
    private String name;
    private String Addr;
    private String Aname;
    private int poi;
    private Float RatingSource;
    private  int toilet_sex;




    public double getXpos(){
        return  xpos;
    }
    public void setXpos(double xpos){
        this.xpos=xpos;
    }

    public double getYpos(){
        return  ypos;

    }
    public void setYpos(double ypos){
        this.ypos=ypos;
    }

    public int get_toilet_sex(){
        return  toilet_sex;
    }
    public void set_toilet_sex(int toilet_sex){
        this.toilet_sex=toilet_sex;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getAddr() {
        return Addr;
    }
    public void setAddr(String Addr){
        this.Addr = Addr;
    }

    public String getAName() {
        return Aname;
    }
    public void setAname(String Aname){
        this.Aname = Aname;
    }

    public int getPoi(){
        return  poi;
    }
    public void setPoi(int poi){
        this.poi = poi;
    }

    public Float getRating(){
        return this.RatingSource;
    }
    public void setRating(Float Rating){
        RatingSource = Rating;
    }

}
