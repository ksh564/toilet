package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-02-18.
 */
public class ToiletDATA {

    private int toilet_tag;
    private  String toilet_name;
    private  String toilet_info;
    private  Double toilet_xpos;
    private  Double toilet_ypos;
    private  String toilet_type;
    private  int toilet_sex;
    private int toilet_wheel,toilet_diaper,toilet_bidet;


    public int get_toilet_tag(){
        return  toilet_tag;
    }
    public void set_toilet_tag(int toilet_tag){
        this.toilet_tag=toilet_tag;
    }


    public int get_toilet_sex(){
        return  toilet_sex;
    }
    public void set_toilet_sex(int toilet_sex){
        this.toilet_sex=toilet_sex;
    }

    public String get_toilet_name(){
        return  toilet_name;
    }
    public void set_toilet_name(String toilet_name){
        this.toilet_name=toilet_name;
    }

    public String get_toilet_info(){
        return  toilet_info;
    }
    public void set_toilet_info(String toilet_info){this.toilet_info=toilet_info;}

    public Double get_toilet_xpos(){
        return  toilet_xpos;
    }
    public void set_toilet_xpos(Double toilet_xpos){
        this.toilet_xpos=toilet_xpos;
    }

    public Double get_toilet_ypos(){
        return  toilet_ypos;
    }
    public void set_toilet_ypos(Double toilet_ypos){
        this.toilet_ypos=toilet_ypos;
    }

    public String get_toilet_type(){
        return  toilet_type;
    }
    public void set_toilet_type(String toilet_type){
        this.toilet_type=toilet_type;
    }

    public int get_toilet_wheel(){
        return  toilet_wheel;
    }
    public void set_toilet_wheel(int toilet_wheel){
        this.toilet_wheel=toilet_wheel;
    }

    public int get_toilet_diaper(){
        return  toilet_diaper;
    }
    public void set_toilet_diaper(int toilet_diaper){
        this.toilet_diaper=toilet_diaper;
    }

    public int get_toilet_bidet(){
        return  toilet_bidet;
    }
    public void set_toilet_bidet(int toilet_bidet){
        this.toilet_bidet=toilet_bidet;
    }


}
