package com.cheng.consult.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.app.App;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.view.AskExpertActivity;
import com.cheng.consult.ui.view.ExpertCategoryActivity;
import com.cheng.consult.ui.view.MyConsultQuestionActivity;
import com.cheng.consult.ui.view.MyLoveExpertListActivity;
import com.cheng.consult.ui.view.SearchActivity;
import com.cheng.consult.utils.FileUtils;
import com.cheng.consult.utils.PhotoUtils;
import com.cheng.consult.utils.ToastUtils;
import com.cheng.consult.utils.ToolsUtils;
import com.cheng.consult.widget.BaseDialogue;
import com.cheng.consult.widget.CommonImageLoader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.cheng.consult.app.App.getApplication;

/**
 * Created by cheng on 2017/11/13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{
    private LinearLayout searchView;
    private LinearLayout mQueryExp;
    private LinearLayout mQueryField;
    private LinearLayout mHistoryQuestion;
    private LinearLayout mEssense;
    private Button mQuickAskBtn;
    private LinearLayout mMyLoveExperts;
    private TextView mSearchBar;
    protected BaseDialogue mBaseDialog;
    private String localImagePath;
    private Uri imageUri;
    private File mTakedPhoto;
    private ImageView mStudySelf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefrag_layout, null, false);
        mBaseDialog = new BaseDialogue(getActivity());
        searchView = (LinearLayout)view.findViewById(R.id.homeFragment_search);
        searchView.setOnClickListener(this);
        mQueryExp = (LinearLayout)view.findViewById(R.id.home_expert_icon);
        mQueryExp.setOnClickListener(this);
        mQueryField = (LinearLayout)view.findViewById(R.id.home_field_icon);
        mQueryField.setOnClickListener(this);
        mHistoryQuestion = (LinearLayout)view.findViewById(R.id.home_historyquest_icon);
        mHistoryQuestion.setOnClickListener(this);
        mEssense = (LinearLayout)view.findViewById(R.id.essence_question_icon);
        mEssense.setOnClickListener(this);
        mMyLoveExperts = (LinearLayout)view.findViewById(R.id.home_my_expert_icon);
        mMyLoveExperts.setOnClickListener(this);
        mQuickAskBtn = (Button)view.findViewById(R.id.expert_detail_ask_button);
        mQuickAskBtn.setOnClickListener(this);
        mSearchBar = (TextView)view.findViewById(R.id.search_expert_question);
        mStudySelf = (ImageView)view.findViewById(R.id.study_self_img);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeFragment_search:
            {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_expert_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), ExpertCategoryActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_field_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), ExpertCategoryActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_historyquest_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), MyConsultQuestionActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.home_my_expert_icon:
            {
                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, new ConsultFragment()).commit();
                Intent intent = new Intent(getActivity(), MyLoveExpertListActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }
                break;
            }
            case R.id.expert_detail_ask_button:
            {
                Intent intent = new Intent(getActivity(), AskExpertActivity.class);
                startActivity(intent);
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
                }

                //showFileChooser();
                break;
            }
            default:
                break;
        }

    }

    String filepath = "";
    String filename = "";

    private static final int FILE_SELECT_CODE = 0;

    private List<String> mStringList;
    /** 调用文件选择软件来选择文件 **/
    private void showFileChooser() {
        ListView view = (ListView) getActivity().getLayoutInflater().inflate(R.layout.file_chooser_layout, null);
        if (mStringList == null) {
            mStringList = new ArrayList<>();
            mStringList.add("拍照");
            mStringList.add("从手机上取");
        }

        view.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mStringList));
        view.setOnItemClickListener(this);
        mBaseDialog.setDialogContentView(view).setTitle("上传").setBottomLayoutVisible(false).show();




//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
//                    Constants.REQUEST_CODE_SELECT_FROM_LOCAL);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBaseDialog.dismiss();
        if (position == 0) {
            mTakedPhoto = CommonImageLoader.getInstance().createPhotoFile();
            //startTakePhoto(mTakedPhoto);
            autoObtainCameraPermission();
            //localImagePath = CommonImageLoader.getInstance().takePhoto(getActivity(), Constants.REQUEST_CODE_TAKE_PICTURE).getAbsolutePath();

        } else {
            startPickPhoto();
            //CommonImageLoader.getInstance().pickPhoto(getActivity(), Constants.REQUEST_CODE_SELECT_FROM_LOCAL);
        }
    }

    private void autoObtainCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(getActivity(), "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_TAKE_PICTURE);
            } else {//有权限直接调用系统相机拍照
                if (FileUtils.isExistSDCard()) {
                    imageUri = Uri.fromFile(mTakedPhoto);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(getActivity(), "com.cheng.consult.fileprovider", mTakedPhoto);
                    }
                    PhotoUtils.takePicture(this, imageUri, Constants.REQUEST_CODE_TAKE_PICTURE);
                } else {
                    ToastUtils.showShort(getActivity(), "设备没有SD卡！");
                }
            }
        }
    }


    private void startPickPhoto(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, Constants.REQUEST_CODE_SELECT_FROM_LOCAL);
    }

    private Uri filePhotoUri = null;
    private void startTakePhoto(File file){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            filePhotoUri = FileProvider.getUriForFile((App)getApplication(),"com.cheng.consult.fileprovider", file);
        }else {
            filePhotoUri = Uri.fromFile(file);
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePhotoUri);
        startActivityForResult(intent, Constants.REQUEST_CODE_TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_TAKE_PICTURE:
            {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                            //imageView.setImageBitmap(bitmap);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 24){
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                                mStudySelf.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            //imageView.setImageBitmap(bitmap);
                        }else {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
                            //imageView.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
            }
            case Constants.REQUEST_CODE_SELECT_FROM_LOCAL:
                {
                    if (resultCode == RESULT_OK) {
                        // Get the Uri of the selected file
                        Uri uri = data.getData();
                        //LogUtils.d(TAG, "File Uri: " + uri.toString());

                        // Get the path
                        String path = null;
                        try {
                            path = ToolsUtils.getPath(getContext(), uri);

                            filename = path.substring(path.lastIndexOf("/") + 1);
                            mSearchBar.setText(path);
                        } catch (URISyntaxException e) {
                            //Log.e("TAG", e.toString());
                            //e.printStackTrace();
                            path = "";
                        }
                        filepath = path;
                        //Log.d(TAG, "File Path: " + path);
                        // Get the file instance
                        // File file = new File(path);
                        // Initiate the upload
                    }
                    break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
