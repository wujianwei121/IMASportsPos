package com.example.framwork.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.framwork.R;

import java.io.File;

/**
 * Description : 图片加载工具类 使用glide框架封装
 */
public class ImageLoaderUtils {

    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).placeholder(placeholder).skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).skipMemoryCache(false).thumbnail(0.3f)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url, int error, int width, int height) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).override(width, height).skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url, Drawable error, int width, int height) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).override(width, height).skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(error).crossFade().into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(false).thumbnail(0.3f)
                .error(R.drawable.ic_empty_picture)
                .into(imageView);
    }

    public static void displayNone(Context context, ImageView imageView, String url, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).thumbnail(0.3f)
                .error(error)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    public static void displaySmallPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_image_loading)
                .error(R.drawable.ic_empty_picture)
                .thumbnail(0.5f)
                .into(imageView);
    }

    public static void displayBigPhoto(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.color.white)
                .error(R.color.white)
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, int url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .skipMemoryCache(false).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .error(R.drawable.ic_empty_picture)
                .crossFade().into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().thumbnail(0.3f).error(R.color.white)
                .centerCrop().bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }

    public static void displayRound(Context context, ImageView imageView, String url, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate().thumbnail(0.3f).error(error)
                .centerCrop().bitmapTransform(new CropCircleTransformation(context)).into(imageView);
    }
}
