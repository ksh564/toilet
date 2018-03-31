package org.androidtown.findrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by 김승훈 on 2017-01-13.
 */
public class ToiletListview1adp extends BaseAdapter implements View.OnClickListener{



    private List<toiletListview> toiletList = null;
    ArrayList<toiletListview> listviewitemlist1 = new ArrayList<toiletListview>();
    private ToiletListview1adp.ListBtnClickListener listBtnClickListener;


    public ToiletListview1adp(ArrayList<toiletListview>asitem){

        this.listviewitemlist1=asitem;

    }

    @Override
    public void onClick(View view) {
        if(this.listBtnClickListener !=null){
            this.listBtnClickListener.onListBtnClick(view,(int)view.getTag());
        }


    }

    public class ViewHolder{

        TextView NameView;
        TextView DistanceView;
        RatingBar RatingView;


    }



    public interface ListBtnClickListener{
        void onListBtnClick(View v, int position);
    }



    @Override
    public int getCount() {
        return listviewitemlist1.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewitemlist1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View Convertview, ViewGroup parent) {
        final  ViewHolder holder;
        final int pos = position;
        final Context context = parent.getContext();

        if(Convertview==null){


            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Convertview = inflater.inflate(R.layout.toiletlist,parent,false);



            holder.NameView=(TextView)Convertview.findViewById(R.id.Toiletname);
            holder.DistanceView=(TextView)Convertview.findViewById(R.id.ToiletDistance);
            holder.RatingView=(RatingBar)Convertview.findViewById(R.id.rating_of_toilet);
            Convertview.setTag(holder);


        }else{
            holder =(ViewHolder)Convertview.getTag();
        }

        holder.NameView.setText(listviewitemlist1.get(position).getName());
        holder.DistanceView.setText(listviewitemlist1.get(position).getDistance().toString());
        holder.RatingView.setRating(listviewitemlist1.get(position).getRating());




        return Convertview;
    }

    public void addItem(String name, Float distance, Float Rating ,int Tag){
        toiletListview item = new toiletListview();
        item.setName(name);
        item.setDistance(distance);
        item.setRating(Rating);
        item.setTag(Tag);

        listviewitemlist1.add(item);

    }


    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());



    }




}
