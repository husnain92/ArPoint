package com.pk_eagle.arpoint;

/**
 * Created by Eagle on 10/4/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class CustomPager extends PagerAdapter {

    Context context;
//    ImageLoader imageLoader;
String Images[]=new String[3];
    public CustomPager(Context context, String[] imgs){
        this.context = context;
        Images=new String[imgs.length];
        this.Images=imgs;

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.layout1, container, false);
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//        imageLoader = ImageLoader.getInstance();
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);
//        imageView.setImageResource(imageId[position]);
//        imageLoader.displayImage(Images[position], imageView);

        Picasso.with(context)
                .load(Images[position])
                .placeholder(R.drawable.no_img)
                .into(imageView);
//        TextView textView1 = (TextView) viewItem.findViewById(R.id.textView1);
//        textView1.setText("hi");
        ((ViewPager)container).addView(viewItem);

        return viewItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub

        return view == ((View)object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

}
