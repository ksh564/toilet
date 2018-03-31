package org.androidtown.findrest.other;

/**
 * Created by 김승훈 on 2017-03-21.
 */
public class map_poi{
        static double xpoi,ypoi;
        public map_poi(){

        }
        public map_poi(double xpoi,double ypoi){
            map_poi.xpoi=xpoi;
            map_poi.ypoi=ypoi;


        }

    public static double getXpoi() {
        return xpoi;
    }

    public static void setXpoi(double xpoi) {
        map_poi.xpoi = xpoi;
    }

    public static double getYpoi() {
        return ypoi;
    }

    public static void setYpoi(double ypoi) {
        map_poi.ypoi = ypoi;
    }
}
