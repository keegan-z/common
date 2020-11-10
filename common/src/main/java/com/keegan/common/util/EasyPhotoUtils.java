package com.keegan.common.util;

import androidx.fragment.app.FragmentActivity;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.builder.AlbumBuilder;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.constant.Capture;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

public class EasyPhotoUtils {

    interface  EasyPhotosListeren {
        void onTakePhotoMultipleCallBack(ArrayList<Photo> resultPhotos);

        void onTakePhotoSingleCallBack(Photo photo);
    }

    private static void takePhotos(FragmentActivity activity, final boolean isMultiple, final EasyPhotosListeren listeren) {
        AlbumBuilder albumBuilder = EasyPhotos.createAlbum(activity, false, GlideEngine.getInstance());//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎]
        if (isMultiple) {//是否多选
            albumBuilder.setCount(9);
        }
        albumBuilder.setCapture(Capture.IMAGE)//默认拍照录像均可（支持Capture.ALL/ Capture.IMAGE/ Capture.VIDEO）
                .enableSingleCheckedBack(true)//设置单选选中返回
                .isCompress(true)//是否压缩
                .setCompressEngine(LubanCompressEngine.getInstance())//设置压缩引擎
                .isCrop(true)//是否剪裁
                .setCompressionQuality(90)// 参数说明：裁剪质量0~100 默认质量90
                .setCircleDimmedLayer(true)//是否圆形裁剪
                .setShowCropFrame(true) //是否显示裁剪框：默认是
                .setShowCropGrid(true) //是否显示裁剪网格：默认是
                .setFreeStyleCropEnabled(false)//是否自由剪裁
                .setAspectRatio(1, 1) //默认1:1
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                        if (null != photos && photos.size() > 0) {
                            if (isMultiple) {
                                listeren.onTakePhotoMultipleCallBack(photos);
                            } else {
                                listeren.onTakePhotoSingleCallBack(photos.get(0));
                            }
                        }
                    }
                });
    }

    private  static void openCameras(FragmentActivity activity, final EasyPhotosListeren listeren) {
        EasyPhotos.createCamera(activity)//参数说明：上下文
                .setCapture(Capture.IMAGE)//默认拍照录像均可（支持Capture.ALL/ Capture.IMAGE/ Capture.VIDEO）
                .enableSingleCheckedBack(true)//设置单选选中返回
                .isCompress(true)//是否压缩
                .setCompressEngine(LubanCompressEngine.getInstance())//设置压缩引擎
                .isCrop(true)//是否剪裁
                .setCompressionQuality(90)// 参数说明：裁剪质量0~100 默认质量90
                .setCircleDimmedLayer(true)//是否圆形裁剪
                .setShowCropFrame(true) //是否显示裁剪框：默认是
                .setShowCropGrid(true) //是否显示裁剪网格：默认是
                .setFreeStyleCropEnabled(false)//是否自由剪裁
                .setAspectRatio(1,1) //默认1:1
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                        if (null != photos && photos.size() > 0) {
                            listeren.onTakePhotoSingleCallBack(photos.get(0));
                        }
                    }
                });

    }

}
