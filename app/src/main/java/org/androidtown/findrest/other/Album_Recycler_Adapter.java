package org.androidtown.findrest.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.androidtown.findrest.R;
import org.androidtown.findrest.ReviewDTO;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-15.
 */
public class Album_Recycler_Adapter extends RecyclerView.Adapter<Album_Recycler_Adapter.MyViewHolder>  {

    ArrayList<String>albumlist2 = new ArrayList<>();

    ArrayList<ReviewDTO> reviewDTOs = new ArrayList<ReviewDTO>();
    private Context mcontext;
    private  String url_1,url_2,url_3,url_4,url_5,url_6,url_7,url_8,url_9,url_10;
    private int mRowIndex = -1;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mult_imageView,single_imageView,no_imageView;



        public MyViewHolder(View view) {
            super(view);
//            if (getItemCount() == 0) {
//                no_imageView=(ImageView)view.findViewById(R.id.ImgView_noview_album);
//            }else if(getItemCount()==1){
                single_imageView=(ImageView)view.findViewById(R.id.ImgView_single_album);
//            }else{
//                mult_imageView=(ImageView)view.findViewById(R.id.ImgView_album);
//            }

        }
    }


    public void setData(ArrayList<String>albumlist){
        albumlist2=albumlist;
        notifyDataSetChanged();
    }


    public Album_Recycler_Adapter(Context context ){
        this.mcontext=context;


    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        if (getItemCount() == 0) {
//            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_noview, parent, false);
//            return new MyViewHolder(itemview);
//        }else if(getItemCount()==1){

        //4월 16일 수정하려고 한 내용
//        Context context = parent.getContext();
//        View itemview = LayoutInflater.from(context).inflate(R.layout.album_singleview, parent, false);
//           MyViewHolder viewHolder = new MyViewHolder(itemview);
//        return  viewHolder;


        //4월 16일 있었던 내용
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_singleview, parent, false);
            return new MyViewHolder(itemview);



//        }else{
//            LayoutInflater minflater= LayoutInflater.from(parent.getContext());
//            View rootview = minflater.inflate(R.layout.album_view,parent,false);
//            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_view,parent,false);
//            return new MyViewHolder(rootview);
//        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//
//
//        if (getItemCount() == 0) {
//
//            Glide.with(mcontext).load(albumlist2.get(position)).centerCrop().into(holder.no_imageView);
//        }else if(getItemCount()==1){
            System.out.println("가로리사이클러뷰 포지션:"+position);

            Glide.with(mcontext).load(albumlist2.get(position)).centerCrop().into(holder.single_imageView);


        //4월 16일
//            Glide.with(mcontext).load(albumlist2.get(position)).centerCrop().into(holder.single_imageView);
//
//
//        }else{
//            System.out.println("여기서 리사이클러뷰 앨범"+albumlist2);
//            System.out.println("여기서 리사이클러뷰 앨범사이즈"+albumlist2.size());
//            Glide.with(mcontext).load(albumlist2.get(position)).centerCrop().into(holder.mult_imageView);
//
//        }


    }

    @Override
    public int getItemCount() {
            return albumlist2.size();
    }

}
