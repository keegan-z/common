package com.keegan.common.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.keegan.common.R;

public class GlideUtil {

    public static void loadImage(ImageView imageView, Object path) {
        RequestOptions options = new RequestOptions()
                .error(R.drawable.icon_default)
                .placeholder(R.drawable.icon_default);

        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

        Glide.with(ContextUtil.getContext())
                .load(path)
                .apply(options)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);

    }

    /**
     * 加载圆角图片
     *
     * @param imageView
     * @param path
     * @param corners
     */
    public static void loadRoundImage(ImageView imageView, Object path, int corners) {

        RequestOptions options = new RequestOptions()
                .error(R.drawable.icon_default)
                .transform(new CenterCrop(), new RoundedCorners(corners))
                .placeholder(R.drawable.icon_default);

        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        Glide.with(ContextUtil.getContext())
                .load(path)
                .apply(options)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);


    }

    /**
     * 加载圆形图片
     *
     * @param imageView
     * @param path      * @param scaleType  -1:默认；0:CenterCrop；1：FitCenter；
     */
    public static void loadCirclemage(ImageView imageView, Object path) {
        RequestOptions options = new RequestOptions()
                .transform(new CenterCrop(), new CircleCrop());
        /*   .placeholder(R.drawable.icon_club_detail_head);*/

        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

        Glide.with(ContextUtil.getContext())
                .load(path)
                .apply(options)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .into(imageView);

    }


}
