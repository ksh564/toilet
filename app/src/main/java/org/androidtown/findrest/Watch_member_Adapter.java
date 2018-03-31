package org.androidtown.findrest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

import org.androidtown.findrest.other.Album_Recycler_Adapter;
import org.androidtown.findrest.other.CircleTransform;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-17.
 */
public class Watch_member_Adapter extends BaseAdapter {

    ArrayList<ReviewDTO> reviewDTOs = new ArrayList<ReviewDTO>();

    //사진 리사이클러뷰 어댑터
    Album_Recycler_Adapter album_recycler_adapter;

    private Context mcontext;



    public Watch_member_Adapter(ArrayList<ReviewDTO> aritem , Context context){


        this.reviewDTOs=aritem;
        this.mcontext=context;
        album_recycler_adapter = new Album_Recycler_Adapter(mcontext);

    }




    public class ReViewHolder {
        TextView Toilet_name;
        RatingBar ratingBar;
        TextView rating;
        TextView Content;
        TextView date;
        ImageButton thumb_up_pink,thumb_up_white,thumb_down_pink,thumb_down_white;
        ImageView pic;
        TextView number_img;
        ImageView Toilet_img;
        RecyclerView album;
        TextView Toilet_link;

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
            Convertview = inflater.inflate(R.layout.member_reviewliset, parent, false);

            reViewHolder.thumb_up_pink=(ImageButton)Convertview.findViewById(R.id.thumb_Up_black);
            reViewHolder.thumb_down_pink=(ImageButton)Convertview.findViewById(R.id.thumb_down_black);
            reViewHolder.thumb_up_white=(ImageButton)Convertview.findViewById(R.id.thumb_Up_white);
            reViewHolder.thumb_down_white=(ImageButton)Convertview.findViewById(R.id.thumb_down_white);

            reViewHolder.Toilet_name = (TextView) Convertview.findViewById(R.id.review_nickname);
            reViewHolder.Toilet_img = (ImageView) Convertview.findViewById(R.id.nickimg);
            reViewHolder.Content = (TextView) Convertview.findViewById(R.id.review_content);
            reViewHolder.date = (TextView) Convertview.findViewById(R.id.review_date);
            reViewHolder.rating = (TextView) Convertview.findViewById(R.id.review_rating);
            reViewHolder.ratingBar = (RatingBar) Convertview.findViewById(R.id.review_ratingbar);
            reViewHolder.number_img = (TextView) Convertview.findViewById(R.id.member_albumsize);
            reViewHolder.album = (RecyclerView) Convertview.findViewById(R.id.horizontal_recycler_albumview);
            reViewHolder.Toilet_link=(TextView)Convertview.findViewById(R.id.member_review_link);

            if (reViewHolder.album != null) {
               LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
                reViewHolder.album.setLayoutManager(layoutManager);
//                album_recycler_adapter = new Album_Recycler_Adapter(context);
                reViewHolder.album.setAdapter(album_recycler_adapter);

            }

            Convertview.setTag(reViewHolder);
        } else {
            reViewHolder = (ReViewHolder) Convertview.getTag();
        }
        System.out.println("리스트뷰안화장실이름"+reviewDTOs.get(position).getToilet_name());
        reViewHolder.Toilet_name.setText(reviewDTOs.get(position).getToilet_name());
        reViewHolder.Toilet_name.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        reViewHolder.Content.setText(reviewDTOs.get(position).get_Content());
        reViewHolder.date.setText(reviewDTOs.get(position).get_Date());

        reViewHolder.rating.setText(Float.toString(reviewDTOs.get(position).get_Rating()));
        reViewHolder.ratingBar.setRating(reviewDTOs.get(position).get_Rating());

        album_recycler_adapter.setData(reviewDTOs.get(position).getImglist());
//        album_recycler_adapter.notifyDataSetChanged();

        reViewHolder.thumb_up_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("따봉업색클릭"+position);
                reViewHolder.thumb_up_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_up_white.setVisibility(View.VISIBLE);
            }
        });
        reViewHolder.thumb_up_pink.setTag(position);
        reViewHolder.thumb_down_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("따봉다운색클릭");
                reViewHolder.thumb_down_pink.setVisibility(View.INVISIBLE);
                reViewHolder.thumb_down_white.setVisibility(View.VISIBLE);
//
            }
        });
        reViewHolder.thumb_down_pink.setTag(position);
        reViewHolder.thumb_up_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("따봉업무색클릭"+position);
                reViewHolder.thumb_up_pink.setVisibility(View.VISIBLE);
                reViewHolder.thumb_up_white.setVisibility(View.INVISIBLE);

            }
        });
        reViewHolder.thumb_up_white.setTag(position);
        reViewHolder.thumb_down_white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("따봉다운무색클릭"+position);
                reViewHolder.thumb_down_pink.setVisibility(View.VISIBLE);
                reViewHolder.thumb_down_white.setVisibility(View.INVISIBLE);
            }
        });
        reViewHolder.thumb_down_white.setTag(position);


        reViewHolder.number_img.setText(String.valueOf(reviewDTOs.get(position).getImglist().size()));
        reViewHolder.Toilet_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("리뷰링크 클릭됨");
                Intent intent = new Intent(context,Toilet_info.class);
                intent.putExtra("toilet_tag_2",Integer.parseInt(reviewDTOs.get(position).getToilet_num()));
                context.startActivity(intent);
            }
        });


//        Glide.with(context).load(reviewDTOs.get(position).get_Img1()).into(reViewHolder.pic);

        Glide.with(context).load(reviewDTOs.get(position).get_Img1())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(reViewHolder.Toilet_img);




        return Convertview;
    }

    public void addItem(String toiletname, String Content, String date, Float Rating, ArrayList<String> imgalbum, String toiletimg, String toilet_num) {
        ReviewDTO item = new ReviewDTO();

        item.setToilet_name(toiletname);
        item.set_Date(date);
        item.set_Content(Content);
        item.set_Rating(Rating);
        item.setImglist(imgalbum);
        item.set_Img1(toiletimg);
        item.setToilet_num(toilet_num);

        reviewDTOs.add(item);
    }

}
