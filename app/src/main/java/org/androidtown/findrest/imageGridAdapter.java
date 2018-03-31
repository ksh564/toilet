package org.androidtown.findrest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 김승훈 on 2017-03-02.
 */
public class imageGridAdapter extends BaseAdapter implements View.OnClickListener {

    private String imgData;
    private String geodata;
    private ArrayList<String> thumsDatalists;
    private ArrayList<String> SelectedPhotos = new ArrayList<String>();
    ArrayList<PhotoListview> Photoitem = new ArrayList<PhotoListview>();

    private ListBtnClickListener listBtnClickListener ;
    Context context = null;

    public imageGridAdapter(ArrayList<String>gridtem ,Context context, imageGridAdapter.ListBtnClickListener listbtn) {

        this.listBtnClickListener = listbtn;
        this.SelectedPhotos = gridtem;
//        thumsDatalists = new ArrayList<String>();
//        SelectedPhotos = new ArrayList<String>();
        this.context=context;
    }

    @Override
    public int getCount() {
        return SelectedPhotos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.imageview,parent,false);
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.viewpager_image);
        System.out.println("비트맵생성 잘 되고 있나1");
//        imageView.setLayoutParams(new GridView.LayoutParams(95, 95));
//        imageView.setAdjustViewBounds(false);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setPadding(2, 2, 2, 2);
        ImageButton button = (ImageButton)convertView.findViewById(R.id.viewpager_icon);
        button.bringToFront();
        button.invalidate();


        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 8;
        Bitmap bmp = BitmapFactory.decodeFile(SelectedPhotos.get(position), bo);
        Bitmap resized = Bitmap.createScaledBitmap(bmp, 320, 240, false);
        System.out.println("비트맵생성 잘 되고 있나"+resized);


        imageView.setImageBitmap(resized);
        imageView.setOnClickListener(this);
        imageView.setTag(position);
        button.setOnClickListener(this);
        button.setTag(position);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        if(this.listBtnClickListener!=null){
            this.listBtnClickListener.onListBtnClick(view,(int)view.getTag());
        }

    }

    public interface ListBtnClickListener{
        void onListBtnClick(View v, int position);
    }

}
