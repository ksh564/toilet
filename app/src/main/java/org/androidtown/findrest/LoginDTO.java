package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-03-03.
 */
public class LoginDTO {

    private String id;
    private String nickname;
    private String email;
    private String imgurl;
    private String confirm;
    private String index;
    private int user_sex;

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgurl() {

        return imgurl;
    }

    public String get_confirm(){
        return  confirm;
    }
    public void set_confirm(String confirm){
        this.confirm=confirm;
    }
    public String get_id(){
        return  id;
    }
    public void set_id(String id){
        this.id=id;
    }
    public String get_nickname(){
        return  nickname;
    }
    public void set_nickname(String nickname){
        this.nickname=nickname;
    }
    public String get_email(){
        return  email;
    }
    public void set_email(String email){
        this.email=email;
    }
}
