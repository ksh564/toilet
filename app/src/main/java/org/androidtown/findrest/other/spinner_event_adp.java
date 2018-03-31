package org.androidtown.findrest.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.androidtown.findrest.EventAdapter;
import org.androidtown.findrest.R;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-04-05.
 */
public class spinner_event_adp extends BaseAdapter implements View.OnClickListener{



    ArrayList<EventDATA> eventDATAs = new ArrayList<EventDATA>();
    private EventAdapter.ListbtnClick listbtnClick;

    public spinner_event_adp(ArrayList<EventDATA> aritem){
        this.eventDATAs = aritem;
    }
    public interface  ListbtnClick{
        void onListbtnClick(View v, int position);
    }


    @Override
    public void onClick(View view) {
        if(this.listbtnClick!=null){
            this.listbtnClick.onListbtnClick(view,(int)view.getTag());
        }
    }

    public class Event_ReViewHolder {
        ImageView nickimg;
        TextView nickname;
        TextView Toilet_distance;
        TextView Toilet_name;
        TextView Cost;
        TextView date;
        Button state_accept,state_ing,state_done;

    }



    @Override
    public int getCount() {
        return eventDATAs.size();
    }

    @Override
    public Object getItem(int position) {
        return eventDATAs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View Covertview, ViewGroup parent) {


        final Event_ReViewHolder event_reViewHolder;
        final Context context = parent.getContext();

        if(Covertview==null){

            event_reViewHolder = new Event_ReViewHolder();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Covertview=inflater.inflate(R.layout.spinner_event,parent,false);

            event_reViewHolder.nickimg=(ImageView)Covertview.findViewById(R.id.event_nickimg);
            event_reViewHolder.nickname=(TextView)Covertview.findViewById(R.id.event_nickname);
            event_reViewHolder.Cost=(TextView)Covertview.findViewById(R.id.event_cost);
            event_reViewHolder.date=(TextView)Covertview.findViewById(R.id.event_date);
            event_reViewHolder.Toilet_distance=(TextView)Covertview.findViewById(R.id.toilet_distance);
            event_reViewHolder.Toilet_name=(TextView)Covertview.findViewById(R.id.toilet_name);
            event_reViewHolder.state_accept=(Button)Covertview.findViewById(R.id.state_accept);
            event_reViewHolder.state_ing=(Button)Covertview.findViewById(R.id.state_ing);
            event_reViewHolder.state_done=(Button)Covertview.findViewById(R.id.state_done);


            Covertview.setTag(event_reViewHolder);




        }else {

            event_reViewHolder = (Event_ReViewHolder) Covertview.getTag();
        }

        System.out.println("포지션체크"+position);
        System.out.println("이미지체크"+eventDATAs.get(position).getEvent_cost());
        Glide.with(context).load(eventDATAs.get(position).getNick_img())
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(event_reViewHolder.nickimg);
        event_reViewHolder.nickname.setText(eventDATAs.get(position).getEvent_nick());
        event_reViewHolder.Toilet_name.setText(eventDATAs.get(position).getToilet_name());
        event_reViewHolder.Toilet_distance.setText(eventDATAs.get(position).getDistance().toString());
        event_reViewHolder.Cost.setText(eventDATAs.get(position).getEvent_cost());
        System.out.println("어댑터시간체크"+eventDATAs.get(position).getEvent_enroll_time());

        event_reViewHolder.date.setText(eventDATAs.get(position).getEvent_enroll_time());
        switch (eventDATAs.get(position).getEvent_state()){
            case 0:
                event_reViewHolder.state_accept.setVisibility(View.VISIBLE);
                event_reViewHolder.state_ing.setVisibility(View.INVISIBLE);
                event_reViewHolder.state_done.setVisibility(View.INVISIBLE);

                break;
            case 1:
                event_reViewHolder.state_accept.setVisibility(View.INVISIBLE);
                event_reViewHolder.state_ing.setVisibility(View.VISIBLE);
                event_reViewHolder.state_done.setVisibility(View.INVISIBLE);

                break;
            case 2:
                event_reViewHolder.state_accept.setVisibility(View.INVISIBLE);
                event_reViewHolder.state_ing.setVisibility(View.INVISIBLE);
                event_reViewHolder.state_done.setVisibility(View.VISIBLE);

                break;
        }




        return Covertview;
    }

    public void additem(String index,String Nick,String Nick_img ,String Cost,int state,String date,String checktime,String toilet_name,Float toilet_distance){

        EventDATA item = new EventDATA();

        item.setToilet_num(index);
        item.setEvent_nick(Nick);
        item.setNick_img(Nick_img);
        item.setEvent_cost(Cost);
        item.setEvent_state(state);
        item.setEvent_enroll_time(date);
        item.setCheck_time(checktime);
        item.setToilet_name(toilet_name);
        item.setDistance(toilet_distance);


        eventDATAs.add(item);


    }

}
