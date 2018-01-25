package com.cheng.consult.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.FileUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PhotoUtils;
import com.cheng.consult.utils.PreUtils;
import com.cheng.consult.utils.ToastUtils;
import com.cheng.consult.utils.ToolsUtils;
import com.cheng.consult.widget.BaseDialogue;
import com.cheng.consult.widget.CommonImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AskExpertActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EditText mQuestionDes;
    private EditText mQuestionTitle;
    private String qDes;
    private String qTitle;
    private Button mSubmit;
    private List<OkHttpUtils.Param> paramList;
    private Spinner cateSpinner;
    private int questionCate = -1;
    private boolean isFirstSelect = true;
    private String mExpertId;
    private ImageButton mAttach;
    protected BaseDialogue mBaseDialog;
    private Uri imageUri;
    private File mTakedPhoto = null;
    private Gson gson;
    private File mImgFileToAttach = null;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        mExpertId = getIntent().getStringExtra("expertid");
        if(null == mExpertId){
            mExpertId = "-1";
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAttach = (ImageButton)findViewById(R.id.btn_ask_expert_attach);
        mBaseDialog = new BaseDialogue(mContext);
        mAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureFrom();
            }
        });
        cateSpinner = (Spinner)findViewById(R.id.cat_spinner);
        mQuestionDes = (EditText)findViewById(R.id.question_des);
        mQuestionTitle = (EditText)findViewById(R.id.question_title);
        mQuestionDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mQuestionDes, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        mSubmit = (Button)findViewById(R.id.submit);



        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.ResultCallback<String> submitQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };

                qTitle = mQuestionTitle.getText().toString().trim();
                qDes = mQuestionDes.getText().toString().trim();

                if(-1 == questionCate){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_cate_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(qTitle.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_title_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if(qDes.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_des_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //json格式post参数
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateFormat.format(date).toString();

                    PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubject", mApplication.mAppSignature, dateStr, "9000");
                    QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), mExpertId,
                            String.valueOf(questionCate), qTitle, qDes);

                    String param = gson.toJson(postParam);
                    String url = Urls.HOST_TEST + Urls.UPLOAD;


                    HashMap<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("file", mTakedPhoto);
                    paramsMap.put("json", param);

                    OkHttpUtils.uploadFile(url, submitQuestionCallback, paramsMap);


                    //OkHttpUtils.postJson(url, submitQuestionCallback, param);
                    finish();
                }
            }
        });



        List<String> spinnerRes = Arrays.asList(getResources().getStringArray(R.array.spinner_consult_category));

        ArrayAdapter<String> spinnerAadapter = new ArrayAdapter<String>(this,
                R.layout.custom_spiner_text_item, spinnerRes);

        spinnerAadapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);

        cateSpinner.setAdapter(spinnerAadapter);

        cateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isFirstSelect){
                    isFirstSelect = false;
                } else {
                    questionCate = position - 1 + 10;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_cate_error_toast), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }

    class QuestionSubmitParam{
        private PostCommonHead.HEAD head;
        private QuestionBody body;

        public QuestionSubmitParam(PostCommonHead.HEAD head,
                                   String userId, String expertId, String questionCate, String questionTitle, String questionDes) {
            this.head = head;
            this.body = new QuestionBody(userId, expertId, questionCate, questionTitle, questionDes);
        }

        class QuestionBody{
            private String userId;
            private String expertId;
            private String questionCate;
            private String questionTitle;
            private String questionDes;

            public QuestionBody(String userId, String expertId, String questionCate, String questionTitle, String questionDes) {
                this.userId = userId;
                this.expertId = expertId;
                this.questionCate = questionCate;
                this.questionTitle = questionTitle;
                this.questionDes = questionDes;
            }
        }
    }

    private List<String> mStringList;
    private void showPictureFrom() {
        ListView view = (ListView) getLayoutInflater().inflate(R.layout.file_chooser_layout, null);
        if (mStringList == null) {
            mStringList = new ArrayList<>();
            mStringList.add("拍照");
            mStringList.add("从手机上取");
        }

        view.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mStringList));
        view.setOnItemClickListener(this);
        mBaseDialog.setDialogContentView(view).setTitle("上传").setBottomLayoutVisible(false).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBaseDialog.dismiss();
        if (position == 0) {
            mTakedPhoto = CommonImageLoader.getInstance().createPhotoFile();
            DynamicObtainCameraPermissionAndTakePicture();
        } else {
            startPickPhoto();
        }
    }

    private void DynamicObtainCameraPermissionAndTakePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ToastUtils.showShort(mContext, "您已经拒绝过一次");
                }
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_TAKE_PICTURE);
            } else {//有权限直接调用系统相机拍照
                if (FileUtils.isExistSDCard()) {
                    imageUri = Uri.fromFile(mTakedPhoto);
                    //通过FileProvider创建一个content类型的Uri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        imageUri = FileProvider.getUriForFile(mContext, "com.cheng.consult.fileprovider", mTakedPhoto);
                    }
                    PhotoUtils.takePicture(this, imageUri, Constants.REQUEST_CODE_TAKE_PICTURE);
                } else {
                    ToastUtils.showShort(mContext, "设备没有SD卡！");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_TAKE_PICTURE:
            {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        if (data.hasExtra("data")) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 24){
                            if(mTakedPhoto.exists()){
                                //uploadImage(mTakedPhoto);
                            }

                        }else {
                        }
                    }
                }
                break;
            }
            case Constants.REQUEST_CODE_SELECT_FROM_LOCAL:
            {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = ToolsUtils.getPath(mContext, uri);
                        mTakedPhoto = new File(path);
                        if(mTakedPhoto.exists()){
                            //uploadImage(mTakedPhoto);
                        }
//                        File tm = new File(path);
//                        if(tm.exists()){
//                            uploadImage(tm);
//                        }
                    } catch (URISyntaxException e) {

                    }
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(File file){
        OkHttpUtils.ResultCallback<String> uploadImgCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();

        PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubject", mApplication.mAppSignature, dateStr, "9000");
        QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), mExpertId,
                String.valueOf(questionCate), qTitle, qDes);

        String param = gson.toJson(postParam);
        String url = Urls.HOST_TEST + Urls.UPLOAD;

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("file", file);
        paramsMap.put("json", param);

        OkHttpUtils.uploadFile(url, uploadImgCallback, paramsMap);

    }

}
