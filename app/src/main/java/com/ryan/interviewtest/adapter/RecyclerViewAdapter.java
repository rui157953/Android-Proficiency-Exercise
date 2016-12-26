package com.ryan.interviewtest.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryan.interviewtest.view.MyIndicator;
import com.ryan.interviewtest.R;
import com.ryan.interviewtest.model.ResultsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Ryan.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public static final int TYPE_PURE_TEXT = 0;
    public static final int TYPE_TEXT_WITH_IMAGES = 1;
    public static final int TYPE_FOOTER = 2;

    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<ResultsBean> mBeanList;
    private OnItemClickListener mItemClickListener;
    private boolean canLoadMore = true;

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener == null) return;
            //注意这里使用getTag方法获取数据
            mItemClickListener.onItemClick(v,(int)v.getTag());

    }

    public interface OnItemClickListener{
        void onItemClick(View view ,int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public RecyclerViewAdapter(Context context, List<ResultsBean> beanList) {
        mContext = context;
        mBeanList = beanList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position +1 == getItemCount()){
            type = TYPE_FOOTER;
        }else if (mBeanList.get(position).getImages()!=null){
            type = TYPE_TEXT_WITH_IMAGES;
        }else {
            type = TYPE_PURE_TEXT;
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PURE_TEXT){
            View view = mLayoutInflater.inflate(R.layout.list_pure_text_item,null);
            view.setOnClickListener(this);
            return new PureTextViewHolder(view);
        }else if (viewType == TYPE_TEXT_WITH_IMAGES){
            View view = mLayoutInflater.inflate(R.layout.list_with_images_item,null);
            view.setOnClickListener(this);
            return new TextWithImagesViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer_view, null);
            view.setOnClickListener(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        holder.itemView.setTag(position);
        switch (getItemViewType(position)){
            case TYPE_PURE_TEXT:
                PureTextViewHolder pureTextViewHolder = (PureTextViewHolder) holder;
                pureTextViewHolder.describer.setText( mBeanList.get(position).getDesc());
                pureTextViewHolder.who.setText(mBeanList.get(position).getWho());
                try {
                    date = format.parse(mBeanList.get(position).getPublishedAt());
                } catch (ParseException e) {
//                e.printStackTrace();
                }
                pureTextViewHolder.time.setText(format.format(date));
            break;
            case TYPE_TEXT_WITH_IMAGES:
                final TextWithImagesViewHolder textWithImagesViewHolder = (TextWithImagesViewHolder) holder;
                textWithImagesViewHolder.describer.setText(mBeanList.get(position).getDesc());
                textWithImagesViewHolder.who.setText(mBeanList.get(position).getWho());
                try {
                    date = format.parse(mBeanList.get(position).getPublishedAt());
                } catch (ParseException e) {
//                e.printStackTrace();
                }
                textWithImagesViewHolder.time.setText(format.format(date));
                final ImagesViewPagerAdapter imagesViewPagerAdapter = new ImagesViewPagerAdapter(mContext, mBeanList.get(position).getImages());
                textWithImagesViewHolder.viewPager.setAdapter(imagesViewPagerAdapter);
                textWithImagesViewHolder.myIndicator.initIndicator(textWithImagesViewHolder.viewPager);
            break;
            case TYPE_FOOTER:
                FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
                if (canLoadMore) {
                    footerViewHolder.footer.setText(mContext.getResources().getString(R.string.load_more));
                }else {
                    footerViewHolder.footer.setText(mContext.getResources().getString(R.string.no_more));
                }
            break;
        }
    }

    @Override
    public int getItemCount() {
        if (mBeanList.size()<=0){
            return 0;
        }else {
            return mBeanList.size()+1;
        }
    }



    public static class PureTextViewHolder extends RecyclerView.ViewHolder{
        private TextView describer, who,time;

        public PureTextViewHolder(View itemView) {
            super(itemView);
            describer = (TextView) itemView.findViewById(R.id.item_describer);
            who = (TextView) itemView.findViewById(R.id.item_who);
            time = (TextView) itemView.findViewById(R.id.item_time);

        }

    }

    public static class TextWithImagesViewHolder extends RecyclerView.ViewHolder{
        private ViewPager viewPager;
        private TextView describer, who,time;
        private MyIndicator myIndicator;

        public TextWithImagesViewHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.item_view_pager);
            describer = (TextView) itemView.findViewById(R.id.item_describer);
            who = (TextView) itemView.findViewById(R.id.item_who);
            time = (TextView) itemView.findViewById(R.id.item_time);
            myIndicator = (MyIndicator) itemView.findViewById(R.id.vpi);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView footer;
        public FooterViewHolder(View view) {
            super(view);
            footer = (TextView) view.findViewById(R.id.footer_tv);
        }
    }

}

