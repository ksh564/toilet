package org.androidtown.findrest.other;

/**
 * Created by 김승훈 on 2017-04-04.
 */
public class Current_Location {

    static double Xpos,Ypos;

    public Current_Location(){

    }

    public Current_Location(Double Xpos,Double Ypos){
        Current_Location.Xpos=Xpos;
        Current_Location.Ypos=Ypos;

    }

    public static double getXpos() {
        return Xpos;
    }

    public static void setXpos(double xpos) {
        Xpos = xpos;
    }

    public static double getYpos() {
        return Ypos;
    }

    public static void setYpos(double ypos) {
        Ypos = ypos;
    }
}
