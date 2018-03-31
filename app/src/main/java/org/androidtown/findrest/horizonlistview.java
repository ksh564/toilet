package org.androidtown.findrest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-02.
 */
public class horizonlistview extends BaseAdapter {

    ArrayList<PhotoListview> Photolistitem = new ArrayList<PhotoListview>();
    public horizonlistview(ArrayList<PhotoListview>aritem){
       this.Photolistitem=aritem;
    }

    private View.OnClickListener deletebutton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button bt = (Button)view;

        }
    };

    public class ViewHolder2{
        ImageView imageView;
        Button deletebtn;
    }

    @Override
    public int getCount() {
        return Photolistitem.size();
    }

    @Override
    public Object getItem(int position) {
        return Photolistitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View ConvertView, ViewGroup parent) {
        final ViewHolder2 holder2;
        final Context context = parent.getContext();

        if(ConvertView==null){

            holder2 = new ViewHolder2();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ConvertView = inflater.inflate(R.layout.toiletlist,parent,false);
        }
        return null;
    }
}
