package com.cheng.consult.widget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.cheng.consult.app.App;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.utils.FileUtils;
import com.cheng.consult.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cheng.consult.app.App.getApplication;

/**
 * Created by cheng on 2018/1/19.
 */

public class CommonImageLoader {
    public static final String TAG = "CommonImageLoader";
    public static final String CURRENT_POSITION = "current_position";
    public static final String PREVIEW_FROM = "preview_from";
    public static final String PREVIEW_DELETE = "preview_delete";
    public static final String PREVIEW_SELECT = "preview_select";
    public static final String IS_ORIGIN = "is_origin";
    public static final String PREVIEW_BASE = "preview_base";

    private static CommonImageLoader instance;
    private ImageLoader mImageLoader;
    /**
     * 最大的选择图片的个数
     */
    private int mMaxSelectedCount=8;

    /**
     * 已选择的图片信息
     */
    private List<ImageItem> mSelectedImages = new ArrayList<>();


    private List<ImageFolder> mImageFolders = new ArrayList<>();
    private int mCurrentImageFolderPosition = 0;

    public ImageFolder getCurrentImageFolder() {
        return getImageFolders().get(getCurrentImageFolderPosition());
    }

    public static CommonImageLoader getInstance() {
        if (instance == null) {
            synchronized (CommonImageLoader.class) {
                if (instance == null) {
                    instance = new CommonImageLoader();
                }
            }
        }
        return instance;
    }


    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            LogUtils.e(TAG,"图片加载器为空，所以new 一个新的");
            mImageLoader = new GlideImageLoader();
        }
        return mImageLoader;
    }

    private void setMaxSelectedCount(int maxSelectedCount) {
        mMaxSelectedCount = maxSelectedCount;
    }

    public int getMaxSelectedCount() {
        return mMaxSelectedCount;
    }


    public List<ImageItem> getSelectedImages() {
        return mSelectedImages;
    }

    public void setSelectedImages(List<ImageItem> selectedImages) {
        mSelectedImages = selectedImages;
    }


    public void setImageFolders(List<ImageFolder> imageFolders) {
        mImageFolders = imageFolders;
    }

    public List<ImageFolder> getImageFolders() {
        return mImageFolders;
    }


    public void setCurrentImageFolderPosition(int currentImageFolderPosition) {
        mCurrentImageFolderPosition = currentImageFolderPosition;
    }

    private int getCurrentImageFolderPosition() {
        return mCurrentImageFolderPosition;
    }


    public void clearAllData() {
        if (mSelectedImages.size() > 0) {
            mSelectedImages.clear();
        }
        if (mImageFolders.size() > 0) {
            mImageFolders.clear();
        }
        setImageLoader(null);
    }

    public void initStanderConfig(int maxSelectedCount) {
        clearAllData();
        setMaxSelectedCount(maxSelectedCount);
        setImageLoader(new GlideImageLoader());
    }


    public void pickPhoto(Activity activity, int requestCode) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public File createPhotoFile(){
        File dir;
        if (FileUtils.isExistSDCard()) {
            dir = FileUtils.newDir(Constants.IMAGE_CACHE_DIR + "take_picture/");
        } else {
            dir = Environment.getDataDirectory();
        }
        File file=null;
        if (dir != null) {
            try {
                file = new File(dir, System.currentTimeMillis() + ".jpg");
                if(!file.exists()){
                    file.createNewFile();
                }
            }catch (IOException e){

            }
        }
        return file;
    }

    public File takePhoto(Activity activity, int requestCodeTakePicture) {

        File dir;
        if (FileUtils.isExistSDCard()) {
            dir = FileUtils.newDir(Constants.IMAGE_CACHE_DIR + "take_picture/");
        } else {
            dir = Environment.getDataDirectory();
        }
        File file=null;
        if (dir != null) {
            try {
                file = new File(dir, System.currentTimeMillis() + ".jpg");
                if(!file.exists()){
                    file.createNewFile();
                }
            }catch (IOException e){

            }

            //file = FileUtils.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            fileUri = FileProvider.getUriForFile((App)getApplication(),"com.cheng.consult.fileprovider", file);
        }else {
            fileUri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, requestCodeTakePicture);
        return file;
    }
}
