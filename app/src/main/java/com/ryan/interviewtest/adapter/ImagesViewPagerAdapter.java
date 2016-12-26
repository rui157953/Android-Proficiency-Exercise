package com.ryan.interviewtest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ryan.interviewtest.view.MyIndicator;
import com.ryan.interviewtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan.
 */
public class ImagesViewPagerAdapter extends PagerAdapter implements MyIndicator.IndicatorListener{

    private Context mContext;
    private List<String> originalImages;
    private List<String> newImages;
    private LayoutInflater mLayoutInflater;

    public ImagesViewPagerAdapter(Context context, List<String> originalImages) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.originalImages = originalImages;
        newImages = new ArrayList<>();
        if (originalImages.size()<2){
            newImages.addAll(originalImages);
        }else {
            newImages.addAll(originalImages);
            newImages.addAll(originalImages);
            newImages.addAll(originalImages);
        }
    }

    @Override
    public int getCorrectCount() {
        return originalImages.size();
    }

    @Override
    public int getCount() {
        return newImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SimpleDraweeView view = (SimpleDraweeView) mLayoutInflater.inflate(R.layout.imageview, null);
        Uri uri = Uri.parse(newImages.get(position));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        view.setController(controller);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,Object object) {
        container.removeView((SimpleDraweeView) object);
    }
}
