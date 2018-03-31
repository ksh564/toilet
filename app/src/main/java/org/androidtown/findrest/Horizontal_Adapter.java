package org.androidtown.findrest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 김승훈 on 2017-03-08.
 */
public class Horizontal_Adapter extends RecyclerView.Adapter<Horizontal_Adapter.MyViewHolder> {

    private List<String> horizontalList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtView;
        public MyViewHolder(View view) {
            super(view);
            if(getItemCount()==10){
                txtView=(TextView)view.findViewById(R.id.txtView);
            }else{

            }




        }
    }
    public Horizontal_Adapter(List<String> horizontalList) {
        this.horizontalList = horizontalList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(getItemCount()==10){

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item, parent, false);
            return new MyViewHolder(itemView);
        }else{

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.imageview, parent, false);

            return new MyViewHolder(itemView);
        }



    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       if(getItemCount()==10){
           holder.txtView.setText(horizontalList.get(position));
       }else{

       }



    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
