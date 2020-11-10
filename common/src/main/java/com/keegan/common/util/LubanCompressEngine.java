package com.keegan.common.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.huantansheng.easyphotos.callback.CompressCallback;
import com.huantansheng.easyphotos.engine.CompressEngine;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.utils.system.SystemUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

public class LubanCompressEngine implements CompressEngine {

    //单例
    private static LubanCompressEngine instance = null;

    //单例模式，私有构造方法
    private LubanCompressEngine() {
    }

    //获取单例
    public static LubanCompressEngine getInstance() {
        if (null == instance) {
            synchronized (LubanCompressEngine.class) {
                if (null == instance) {
                    instance = new LubanCompressEngine();
                }
            }
        }
        return instance;
    }

    /**
     * 适配android 10
     *
     * @return
     */
    private String getPath() {
        String path = null;

        if (SystemUtils.beforeAndroidTen()) {
            path = Environment.getExternalStorageDirectory() + "/hl365/compress/";
        } else {
            path = ContextUtil.getContext().getExternalFilesDir("compress").getAbsolutePath();
        }

        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }


    @Override
    public void compress(Context context, final ArrayList<Photo> photos, final CompressCallback callback) {
        callback.onStart();

        ArrayList<String> paths = new ArrayList<>();


        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            if (photo.selectedOriginal) continue;

            if (TextUtils.isEmpty(photo.cropPath)) {
                if (SystemUtils.beforeAndroidTen()) {
                    paths.add(photo.path);
                } else {
                    //android10以上 不剪裁返回的图片路径是uri路径，需要把图片先copy到app私有路径后 再获取copy后的图片路径

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault());
                    String imageName = "IMG_CROP_" + "index" + i + "%s" + getImageSuffix(photo.type);
                    String destinationFileName = String.format(imageName, dateFormat.format(new Date()));

                    String filePath = copyUriToExternalFilesDir(context, parseUri(photo.path), destinationFileName);

                    if (TextUtils.isEmpty(filePath)) {
                        continue;
                    }
                    paths.add(filePath);


                }
            } else {
                paths.add(photo.cropPath);
            }
        }

        if (paths.isEmpty()) {
            callback.onSuccess(photos);
            return;
        }

        Flowable.just(paths)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        return Luban.with(ContextUtil.getContext()).load(list).setTargetDir(getPath()).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) throws Exception {
                        for (int i = 0; i < photos.size(); i++) {
                            Photo photo = photos.get(i);
                            photo.compressPath = files.get(i).getPath();
                        }
                        callback.onSuccess(photos);
                    }
                });

    }

    private Uri parseUri(String path) {
        Uri uri = null;
        if (SystemUtils.beforeAndroidTen()) {
            uri = Uri.fromFile(new File(path));
        } else {
            uri = Uri.parse(path);
        }
        return uri;
    }

    public String copyUriToExternalFilesDir(Context mContext, Uri uri, String fileName) {
        String path = null;
        InputStream inputStream = null;
        File targetFile = null;

        try {
            inputStream = mContext.getContentResolver().openInputStream(uri);
            String tempDir = mContext.getExternalFilesDir("temp").getAbsolutePath();

            if (inputStream != null && tempDir != null) {
                targetFile = new File(tempDir + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(targetFile);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] byteArray = new byte[1024];
                int bytes = bis.read(byteArray);

                while (bytes > 0) {
                    bos.write(byteArray, 0, bytes);
                    bos.flush();
                    bytes = bis.read(byteArray);
                }
                bos.close();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null != targetFile ? targetFile.getAbsolutePath() : "";
    }

    private String getImageSuffix(String type) {
        String defaultSuffix = ".png";
        try {
            int index = type.lastIndexOf("/") + 1;
            if (index > 0) {
                return "." + type.substring(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultSuffix;
        }
        return defaultSuffix;
    }


}
