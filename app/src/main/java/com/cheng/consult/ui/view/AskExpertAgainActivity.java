package com.cheng.consult.ui.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.PostResponseBodyJson;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.FileUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PhotoUtils;
import com.cheng.consult.utils.ToastUtils;
import com.cheng.consult.utils.ToolsUtils;
import com.cheng.consult.widget.BaseDialogue;
import com.cheng.consult.widget.CommonImageLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AskExpertAgainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private EditText mQuestionDes;
    private Button mSubmit;
    private String qDes;
    private int mExpertId;
    private Long mSubjectId;
    private ImageButton mAttach;
    protected BaseDialogue mBaseDialog;
    private Uri imageUri;
    private File mTakedPhoto;
    private Gson gson;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_ask_expert_again;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        mAttach = (ImageButton)findViewById(R.id.btn_ask_expert_again_attach);
        mBaseDialog = new BaseDialogue(mContext);
        mAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureFrom();
            }
        });

        mExpertId = getIntent().getIntExtra("expertid", -1);
        mSubjectId = getIntent().getLongExtra("subjectid", -1);

        mQuestionDes = (EditText)findViewById(R.id.question_des);
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



        //final Gson gson=  new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtils.ResultCallback<String> submitQuestionCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                        boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                        if(issuccessed){
                            Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.submit_success), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(mContext, "提交失败，请检查网络", Toast.LENGTH_LONG).show();
                    }
                };

                qDes = mQuestionDes.getText().toString().trim();

                if(qDes.isEmpty()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_question_des_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //json格式post参数
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateStr = dateFormat.format(date).toString();

                    PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubjectItem", mApplication.mAppSignature, dateStr, "9000");
                    QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), String.valueOf(mSubjectId), "0", qDes, "0", "20");

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
    }

    class QuestionSubmitParam{
        private PostCommonHead.HEAD head;
        private QuestionBody body;

        public QuestionSubmitParam(PostCommonHead.HEAD head,
                                   String relationId, String subjectId, String isType, String questionDes, String pageNum, String pageSize) {
            this.head = head;
            this.body = new QuestionBody(relationId, subjectId, isType, questionDes, pageNum, pageSize);
        }

        class QuestionBody{
            private String relationId;//追问就是用户id，专家回答就是专家id
            private String subjectId;
            private String isType;
            private String questionDes;
            private String pageNum;
            private String pageSize;

            public QuestionBody(String relationId, String subjectId, String isType, String questionDes, String pageNum, String pageSize) {

                this.relationId = relationId;
                this.subjectId = subjectId;
                this.isType = isType;
                this.questionDes = questionDes;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
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

        PostCommonHead.HEAD postHead = new PostCommonHead.HEAD("1", "saveSubjectItem", mApplication.mAppSignature, dateStr, "9000");
        QuestionSubmitParam postParam = new QuestionSubmitParam(postHead, String.valueOf(mApplication.mUserId), String.valueOf(mSubjectId), "0", qDes, "0", "20");

        String param = gson.toJson(postParam);
        String url = Urls.HOST_TEST + Urls.UPLOAD;

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("file", file);
        paramsMap.put("json", param);

        OkHttpUtils.uploadFile(url, uploadImgCallback, paramsMap);

    }

}
