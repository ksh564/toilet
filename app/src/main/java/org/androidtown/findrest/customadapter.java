package org.androidtown.findrest;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by 김승훈 on 2017-03-01.
 */
public class customadapter extends PagerAdapter{

    private LayoutInflater mInflater;

    public customadapter(Context c){
        super();
        mInflater=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        view = mInflater.inflate(R.layout.imageview,null);

        ImageView img = (ImageView)view.findViewById(R.id.viewpager_image);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        super.destroyItem(container, position, object);
    }


    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v==obj;
    }
}
