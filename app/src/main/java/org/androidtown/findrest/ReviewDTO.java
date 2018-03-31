package org.androidtown.findrest;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-04.
 */
public class ReviewDTO {

    private String Nickname;
    private String Content;
    private ArrayList<String> imglist;
    private Float Rating;
    private String Img1;
    private float SumRating;
    private int ReviewSum;
    private String Date;
    private String id;
    private String Nickimg;
    private int IsChecked,like,dislike,reviewindex,likeani,dislikeani;
    private String Toilet_num;
    private String Toilet_name;

    public int getDislikeani() {
        return dislikeani;
    }

    public void setDislikeani(int dislikeani) {
        this.dislikeani = dislikeani;
    }

    public int getLikeani() {
        return likeani;
    }

    public void setLikeani(int likeani) {
        this.likeani = likeani;
    }

    public int getReviewindex() {
        return reviewindex;
    }

    public void setReviewindex(int reviewindex) {
        this.reviewindex = reviewindex;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public int getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(int isChecked) {
        IsChecked = isChecked;
    }

    public String getToilet_name() {
        return Toilet_name;
    }

    public void setToilet_name(String toilet_name) {
        Toilet_name = toilet_name;
    }

    public String getToilet_num() {
        return Toilet_num;
    }

    public void setToilet_num(String toilet_num) {
        Toilet_num = toilet_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickimg() {
        return Nickimg;
    }


    public ArrayList<String> getImglist() {
        return imglist;
    }

    public void setImglist(ArrayList<String> imglist) {
        this.imglist = imglist;
    }

    public void setNickimg(String nickimg) {
        Nickimg = nickimg;
    }

    public String get_Date() {
        return Date;
    }

    public void set_Date(String Date) {
        this.Date = Date;
    }

    public String get_Nickname() {
        return Nickname;
    }

    public void set_Nickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String get_Content() {
        return Content;
    }

    public void set_Content(String Content) {
        this.Content = Content;
    }

    public float get_Rating() {
        return Rating;
    }

    public void set_Rating(float Rating) {
        this.Rating = Rating;
    }

    public String get_Img1() {
        return Img1;
    }

    public void set_Img1(String Img1) {
        this.Img1 = Img1;
    }

    public float get_SumRating() {
        return SumRating;
    }

    public void set_SumRating(float SumRating) {
        this.SumRating = SumRating;
    }

    public int get_ReviewSum() {
        return ReviewSum;
    }

    public void set_ReviewSum(int ReviewSum) {
        this.ReviewSum = ReviewSum;
    }

}
