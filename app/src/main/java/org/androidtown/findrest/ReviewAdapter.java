package org.androidtown.findrest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.androidtown.findrest.other.Album_Recycler_Adapter;
import org.androidtown.findrest.other.CircleTransform;
import org.androidtown.findrest.other.watch_member;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-04.
 */
public class ReviewAdapter extends BaseAdapter implements View.OnClickListener  {

//    ImageButton thumb_up_pink,thumb_up_white,thumb_down_pink,thumb_down_white;

    ArrayList<ReviewDTO> reviewDTOs = new ArrayList<ReviewDTO>();

    Album_Recycler_Adapter album_recycler_adapter;

    private ReviewListItemClickListener reviewListItemClickListener;

    public ReviewAdapter(ArrayList<ReviewDTO> aritem, ReviewAdapter.ReviewListItemClickListener itemClick) {
        this.reviewDTOs = aritem;
        this.reviewListItemClickListener=itemClick;
    }

    @Override
    public void onClick(View view) {
        if(this.reviewListItemClickListener!=null) {
            this.reviewListItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public class ReViewHolder {
        TextView id;
        RatingBar ratingBar;
        TextView rating;
        TextView Content;
        TextView date;
        ImageButton thumb_up_pink,thumb_up_white,thumb_down_pink,thumb_down_white;
        ImageView pic;
        TextView number_img;
        ImageView nickimg;
        RecyclerView album;
        TextView like,dislike;
    }


    @Override
    public int getCount() {
        return reviewDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View Convertview, ViewGroup parent) {

        final ReViewHolder reViewHolder;
        final Context context = parent.getContext();

        if (Convertview == null) {
            reViewHolder = new ReViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Convertview = inflater.inflate(R.layout.reviewlist, parent, false);

            reViewHolder.thumb_up_pink=(ImageButton)Convertview.findViewById(R.id.new_thumb_Up_black);
            reViewHolder.thumb_down_pink=(ImageButton)Convertview.findViewById(R.id.thumb_down_black);



            reViewHolder.thumb_up_white=(ImageButton)Convertview.findViewById(R.id.thumb_Up_white);
            reViewHolder.thumb_down_white=(ImageButton)Convertview.findViewById(R.id.thumb_down_white);

            reViewHolder.id = (TextView) Convertview.findViewById(R.id.review_nickname);
            reViewHolder.nickimg = (ImageView) Convertview.findViewById(R.id.nickimg);
            reViewHolder.Content = (TextView) Convertview.findViewById(R.id.review_content);
            reViewHolder.date = (TextView) Convertview.findViewById(R.id.review_date);
            reViewHolder.rating = (TextView) Convertview.findViewById(R.id.review_rating);
            reViewHolder.ratingBar = (RatingBar) Convertview.findViewById(R.id.review_ratingbar);
            reViewHolder.number_img = (TextView) Convertview.findViewById(R.id.reviewlist_album_num);
            reViewHolder.album = (RecyclerView) Convertview.findViewById(R.id.horizontal_recycler_albumview);
            reViewHolder.like=(TextView)Convertview.findViewById(R.id.thumb_Up_size);
            reViewHolder.dislike=(TextView)Convertview.findViewById(R.id.thumb_Down_size);



            Convertview.setTag(reViewHolder);
        } else {
            reViewHolder = (ReViewHolder) Convertview.getTag();
        }
        if (reViewHolder.album != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
            reViewHolder.album.setLayoutManager(layoutManager);
            album_recycler_adapter = new Album_Recycler_Adapter(context);
            reViewHolder.album.setAdapter(album_recycler_adapter);

        }
        reViewHolder.id.setText(reviewDTOs.get(position).get_Nickname());
        reViewHolder.id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("프로필이미지 클릭됨");
                Intent intent = new Intent(context,watch_member.class);
                intent.putExtra("user_id",reviewDTOs.get(position).getId());
                context.startActivity(intent);
            }
        });
        reViewHolder.Content.setText(reviewDTOs.get(position).get_Content());
        reViewHolder.date.setText(reviewDTOs.get(position).get_Date());

        reViewHolder.rating.setText(Float.toString(reviewDTOs.get(position).get_Rating()));
        reViewHolder.ratingBar.setRating(reviewDTOs.get(position).get_Rating());
        reViewHolder.like.setText(String.valueOf(reviewDTOs.get(position).getLike()));
        reViewHolder.dislike.setText(String.valueOf(reviewDTOs.get(position).getDislike()));


        album_recycler_adapter.setData(reviewDTOs.get(position).getImglist());

        reViewHolder.thumb_up_pink.setOnClickListener(this);
        reViewHolder.thumb_up_pink.setTag(position);
        reViewHolder.thumb_down_pink.setOnClickListener(this);
        reViewHolder.thumb_down_pink.setTag(position);
        reViewHolder.thumb_up_white.setOnClickListener(this);
        reViewHolder.thumb_up_white.setTag(position);
        reViewHolder.thumb_down_white.setOnClickListener(this);
        reViewHolder.thumb_down_white.setTag(position);


        switch (reviewDTOs.get(position).getLikeani()){

            case 1:
            {YoYo.with(Techniques.RubberBand)
                    .duration(700)
                    .repeat(3)
                    .playOn(Convertview.findViewById(R.id.thumb_Up_white));}
            break;

            case 2:{
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(3)
                        .playOn(Convertview.findViewById(R.id.new_thumb_Up_black));}
            break;
            case 3: {
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(3)
                        .playOn(Convertview.findViewById(R.id.thumb_down_black));
            }     case 4:{
                YoYo.with(Techniques.RubberBand)
                        .duration(700)
                        .repeat(3)
                        .playOn(Convertview.findViewById(R.id.thumb_down_white));}
            break;
        }


//        switch (reviewDTOs.get(position).getDislikeani()){
//
//            case 0: {
//
//            }
//                break;
//            case 3: {
//                YoYo.with(Techniques.RubberBand)
//                        .duration(700)
//                        .repeat(5)
//                        .playOn(Convertview.findViewById(R.id.thumb_down_black));
//            }
//                break;
//            case 4:{
//                YoYo.with(Techniques.RubberBand)
//                        .duration(700)
//                        .repeat(5)
//                        .playOn(Convertview.findViewById(R.id.thumb_down_white));}
//                break;
//        }

        switch (reviewDTOs.get(position).getIsChecked()){
            case 0:
                reViewHolder.thumb_up_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_up_white.setVisibility(View.VISIBLE);
                reViewHolder.thumb_down_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_down_white.setVisibility(View.VISIBLE);
                break;
            case 1:
                reViewHolder.thumb_up_pink.setVisibility(View.VISIBLE);
                reViewHolder.thumb_up_white.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_down_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_down_white.setVisibility(View.VISIBLE);
                reViewHolder.thumb_down_white.setOnClickListener(null);
                break;
            case 2:
                reViewHolder.thumb_up_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_up_white.setVisibility(View.VISIBLE);
                reViewHolder.thumb_down_pink.setVisibility(View.VISIBLE);
                reViewHolder.thumb_down_white.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_up_white.setOnClickListener(null);
                break;
        }

//        reViewHolder.thumb_up_pink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("따봉업색클릭"+position);
//                reViewHolder.thumb_up_pink.setVisibility(View.INVISIBLE);
//                reViewHolder.thumb_up_white.setVisibility(View.VISIBLE);
//            }
//        });
//        reViewHolder.thumb_up_pink.setTag(position);
//        reViewHolder.thumb_down_pink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("따봉다운색클릭");
//                reViewHolder.thumb_down_pink.setVisibility(View.INVISIBLE);
//                reViewHolder.thumb_down_white.setVisibility(View.VISIBLE);
////
//            }
//        });
//        reViewHolder.thumb_down_pink.setTag(position);
//        reViewHolder.thumb_up_white.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                            System.out.println("따봉업무색클릭"+position);
//                reViewHolder.thumb_up_pink.setVisibility(View.VISIBLE);
//                reViewHolder.thumb_up_white.setVisibility(View.INVISIBLE);
//
//            }
//        });
//        reViewHolder.thumb_up_white.setTag(position);
//        reViewHolder.thumb_down_white.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                            System.out.println("따봉다운무색클릭"+position);
//                reViewHolder.thumb_down_pink.setVisibility(View.VISIBLE);
//                reViewHolder.thumb_down_white.setVisibility(View.INVISIBLE);
//            }
//        });
        reViewHolder.thumb_down_white.setTag(position);
        album_recycler_adapter.notifyDataSetChanged();

        reViewHolder.number_img.setText(String.valueOf(reviewDTOs.get(position).getImglist().size()));


//        Glide.with(context).load(reviewDTOs.get(position).get_Img1()).into(reViewHolder.pic);

        Glide.with(context).load(reviewDTOs.get(position).getNickimg())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(reViewHolder.nickimg);
        reViewHolder.nickimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("프로필이미지 클릭됨");
                Intent intent = new Intent(context,watch_member.class);
                intent.putExtra("user_id",reviewDTOs.get(position).getId());
                context.startActivity(intent);
            }
        });

        return Convertview;
    }

    public interface ReviewListItemClickListener{
        void onItemClick(View v, int position);
    }

      public void addItem(String nickname, String Content, String date, Float Rating, ArrayList<String> imgalbum, String nickimg, String id,int reviewindex ,int like,int dislike,int IsChecked,int likeani,int dislikeani) {
        ReviewDTO item = new ReviewDTO();

        item.set_Nickname(nickname);
        item.set_Date(date);
        item.set_Content(Content);
        item.set_Rating(Rating);
        item.setImglist(imgalbum);
        item.setNickimg(nickimg);
        item.setId(id);
        item.setReviewindex(reviewindex);
        item.setLike(like);
        item.setDislike(dislike);
        item.setIsChecked(IsChecked);

          item.setLikeani(likeani);
          item.setDislikeani(dislikeani);
        reviewDTOs.add(item);

    }

//    switch (view.getId()){
//        case R.id.thumb_Up_black:
//
//                thumb_up_pink.setVisibility(View.INVISIBLE);
//                thumb_up_white.setVisibility(View.VISIBLE);
//            System.out.println("따봉업색클릭");
//            break;
//        case R.id.thumb_Up_white:
//                thumb_up_pink.setVisibility(View.VISIBLE);
//                thumb_up_white.setVisibility(View.INVISIBLE);
//            System.out.println("따봉업무색클릭");
//            break;
//        case R.id.thumb_down_black:
//                thumb_down_pink.setVisibility(View.INVISIBLE);
//                thumb_down_white.setVisibility(View.VISIBLE);
//            System.out.println("따봉다운색클릭");
//
//            break;
//        case R.id.thumb_down_white:
//                thumb_down_pink.setVisibility(View.VISIBLE);
//                thumb_down_white.setVisibility(View.INVISIBLE);
//            System.out.println("따봉다운무색클릭");
//            break;
//
//    }


}
