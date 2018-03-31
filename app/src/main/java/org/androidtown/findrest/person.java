package org.androidtown.findrest;

/**
 * Created by 김승훈 on 2017-03-09.
 */
public class person {
    static String id,email,index_id,id_img,nick;
    static boolean loginstate;
    static int user_sex;

    public person(){

    }
    public person(String index_id,String id,String nick,String email,String id_img,boolean loginstate,int user_sex){
        person.nick = nick;
        person.id =id;
        person.index_id =index_id;
        person.email =email;
        person.id_img =id_img;
        person.loginstate =loginstate;
        person.user_sex=user_sex;

    }

    public static int getUser_sex() {
        return user_sex;
    }

    public static void setUser_sex(int user_sex) {
        person.user_sex = user_sex;
    }

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        person.nick = nick;
    }

    public static boolean isLoginstate() {
        return loginstate;
    }

    public static void setLoginstate(boolean loginstate) {
        person.loginstate = loginstate;
    }

    public static String getId() {
        return id;
    }

    public static String getEmail() {
        return email;
    }

    public static String getIndex_id() {
        return index_id;
    }

    public static void setId(String id) {
        person.id = id;
    }

    public static void setEmail(String email) {
        person.email = email;
    }

    public static void setIndex_id(String index_id) {
        person.index_id = index_id;
    }

    public static String getId_img() {
        return id_img;
    }

    public static void setId_img(String id_img) {
        person.id_img = id_img;
    }
}
