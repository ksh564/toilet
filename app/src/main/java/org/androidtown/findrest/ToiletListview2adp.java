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

/**
 * Created by 김승훈 on 2017-02-04.
 */
public class ToiletListview2adp extends BaseAdapter implements View.OnClickListener{

    //Filterable

    private List<toiletListview> toiletList = null;
//    ArrayList<toiletListview2> listviewitemlist2 = new ArrayList<toiletListview2>();
    ArrayList<ToiletDTO> listviewitemlist2 = new ArrayList<ToiletDTO>();

//    ArrayList<ToiletDTO> filtereditemlist2 = new ArrayList<ToiletDTO>();

//    Filter listFilter;
    private ToiletListview2adp.ListBtnClickListenr listBtnClickListener;

    public ToiletListview2adp(ArrayList<ToiletDTO>aritem){

       this.listviewitemlist2 = aritem;


    }

//    @Override
//    public Filter getFilter() {
//        if(listFilter==null){
//            listFilter = new ListFilter();
//        }
//        return listFilter;
//    }

    public interface ListBtnClickListenr{
        void onListBtnClick(View v, int position);
    }

    @Override
    public int getCount() {
        return listviewitemlist2.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewitemlist2.get(position);
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
            Convertview = inflater.inflate(R.layout.toiletlist2,parent,false);



            holder.NameView=(TextView)Convertview.findViewById(R.id.Toiletname);
            holder.AddrView=(TextView)Convertview.findViewById(R.id.ToiletAname);
            holder.RatingView=(RatingBar)Convertview.findViewById(R.id.Toilet_Rating);
            Convertview.setTag(holder);


        }else{
            holder =(ViewHolder)Convertview.getTag();
        }

        holder.NameView.setText(listviewitemlist2.get(position).getName());
        holder.AddrView.setText(listviewitemlist2.get(position).getAddr());
        holder.RatingView.setRating(listviewitemlist2.get(position).getRating());




        return Convertview;
    }

    @Override
    public void onClick(View view) {

        if(this.listBtnClickListener !=null){
            this.listBtnClickListener.onListBtnClick(view,(int)view.getTag());
        }

    }

    public class ViewHolder{

        TextView NameView;
        TextView AddrView;
        RatingBar RatingView;


    }








    public void addItem(String name, String Addr, Float Rating,int Tag){
//        toiletListview2 item2 = new toiletListview2();
       ToiletDTO item2 = new ToiletDTO();

        item2.setName(name);
        item2.setAddr(Addr);
        item2.setRating(Rating);
        item2.setPoi(Tag);

        listviewitemlist2.add(item2);

    }
//
//    private  class  ListFilter extends Filter {
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            FilterResults results = new FilterResults();
//
//            if(constraint ==null || constraint.length() == 0){
//                results.values = listviewitemlist2 ;
//                results.count = listviewitemlist2.size();
//            }else {
//                ArrayList<ToiletDTO>itemList = new ArrayList<ToiletDTO>();
//                for (ToiletDTO item : listviewitemlist2){
//                    if (item.getName().toUpperCase().contains(constraint.toString().toUpperCase())||
//                            item.getAddr().toUpperCase().contains(constraint.toString().toUpperCase()))
//                    {
//                     itemList.add(item);
//                    }
//                }
//                results.values = itemList;
//                results.count = itemList.size();
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            filtereditemlist2 = (ArrayList<ToiletDTO>) results.values;
//            if(results.count>0){
//                notifyDataSetChanged();
//            }else {
//                notifyDataSetInvalidated();
//            }
//
//        }
//    }
//


}
