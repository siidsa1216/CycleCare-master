package com.example.cyclecare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class HomeViewPager extends PagerAdapter{

    Context context;

    int images[] = {
            R.drawable.girl_biking,
            R.drawable.qr_illustration,
            R.drawable.man_with_bike
    };

    int headings[] = {
            R.string.heading_one,
            R.string.heading_two,
            R.string.heading_three
    };

    int descriptions[] = {
            R.string.description1,
            R.string.description2,
            R.string.description3
    };

    public HomeViewPager(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.home_slider_layout, container, false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.imageHolder);
        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView description = (TextView) view.findViewById(R.id.description);


        slidetitleimage.setImageResource(images[position]);
        heading.setText(headings[position]);
        description.setText(descriptions[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
