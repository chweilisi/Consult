package com.cheng.consult.ui.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.ui.adapter.MyQuestionDetailAdapter;
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
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyQuestionDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Subject mSubject;
    private List<SubjectItem> mQContent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private MyQuestionDetailAdapter mAdapter = null;
    private ImageView mSubjectIcon;
    private TextView mSubjectTitle;
    private TextView mSubjectDes;
    private ListView mListView;
    private String mSubjectData;
    private Button mBtnAskAgain;
    private ImageButton mBtnBottomBarAsk;
    private Gson gson;
    private int mPageIndex = 0;

    protected BaseDialogue mBaseDialog;
    private Uri imageUri;
    private File mTakedPhoto;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_question_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("问题详情");

        mBaseDialog = new BaseDialogue(mContext);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mSubjectTitle = (TextView)findViewById(R.id.tvTitle);
        mSubjectDes = (TextView)findViewById(R.id.tvDesc);

        //获取activity传递数据
        mSubjectData = getIntent().getStringExtra("questiondetail");

        mSubject = gson.fromJson(mSubjectData, Subject.class);
        mQContent = mSubject.getItems();

        //设置问题标题 正文
        mSubjectTitle.setText(mSubject.getTitle());
        mSubjectDes.setText(mSubject.getContent());
        mSubjectDes.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //mQContent = mSubject.getItems();
        if(null != mQContent && mQContent.size() != 0) {
            mAdapter = new MyQuestionDetailAdapter(this, mQContent);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnAnswerItemClickListener(listener);
            mAdapter.notifyDataSetChanged();
        }

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        //set footerview
        setViewerHeaderAndFooter();

        mBtnBottomBarAsk = (ImageButton)findViewById(R.id.btn_question_detail_bottombar_ask);
        mBtnBottomBarAsk.setOnClickListener(this);
    }

    private void setViewerHeaderAndFooter(){
        if(null != mAdapter){
            View footer = LayoutInflater.from(mContext).inflate(R.layout.question_detail_page_ask_again_layout, mRecyclerView, false);
            mBtnAskAgain = (Button)footer.findViewById(R.id.btn_question_detail_answer_again);
            mBtnAskAgain.setOnClickListener(this);
            mAdapter.setFooterView(footer);
            mAdapter.setHeaderView(null);
        }
    }
    private void refreshPage(Subject subject){
        mQContent.addAll(subject.getItems());
        if(null != mQContent && mQContent.size() != 0) {
            mAdapter.notifyDataSetChanged();
            setViewerHeaderAndFooter();
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                mPageIndex = mPageIndex + 1;
                //mPresenter.loadExpertList(mUserId, mExpertCategory, mPageIndex);

                OkHttpUtils.ResultCallback<String> QuestionDetailResultCallback = new OkHttpUtils.ResultCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        PostResponseBodyJson result = gson.fromJson(response, PostResponseBodyJson.class);
                        boolean issuccessed = result.getResultCode().equalsIgnoreCase(Constants.LOGIN_OR_POST_SUCCESS);
                        if(issuccessed){
                            String questionStr = result.getResultJson().trim();
                            Subject tmpSubject = gson.fromJson(questionStr, Subject.class);
                            int size = tmpSubject.getItems().size();
                            if(0 < size){
                                refreshPage(tmpSubject);
                            }
                        }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_PROGRAM)){
                            Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                    + getResources().getString(R.string.question_detail_hint_app_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }else if (result.getResultCode().trim().equalsIgnoreCase(Constants.SYSTEM_ERROR_SERVER)){
                            Toast toast = Toast.makeText(mContext, "ErrorCode = "+ result.getResultCode() + " "
                                    + getResources().getString(R.string.question_detail_hint_server_error) + " " + result.getResultMess(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                };

                //json格式post参数
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateStr = dateFormat.format(date).toString();

                String url = Urls.HOST_TEST + Urls.FORUM;
                PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "subjectView", mApplication.mAppSignature, dateStr, "9000");
                QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mSubject.getSubjectId()), String.valueOf(mPageIndex), "20");
                String strParam = gson.toJson(param);
                OkHttpUtils.postJson(url, QuestionDetailResultCallback, strParam);


            }
        }
    };

    class QuestionDetailPostBean{
        private PostCommonHead.HEAD head;
        private PostBody body;

        public QuestionDetailPostBean(PostCommonHead.HEAD head, String subjectId, String pageNum, String pageSize) {
            this.head = head;
            this.body = new PostBody(subjectId, pageNum, pageSize);
        }

        class PostBody{
            private String subjectId;
            private String pageNum;
            private String pageSize;

            public PostBody(String subjectId, String pageNum, String pageSize) {
                this.subjectId = subjectId;
                this.pageNum = pageNum;
                this.pageSize = pageSize;
            }
        }
    }

    private MyQuestionDetailAdapter.onAnswerItemClickListener listener = new MyQuestionDetailAdapter.onAnswerItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(mContext, AnswerItemDetailActivity.class);
            SubjectItem item = mSubject.getItems().get(position);
            String answerItem = gson.toJson(item);
            intent.putExtra("answer_item", answerItem);
//            intent.putExtra("answer_content", mSubject.getItems().get(position).getContent());
//            intent.putExtra("item_type", mSubject.getItems().get(position).getItemType());
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_question_detail_bottombar_ask:
            {
                //showPictureFrom();
                //break;
            }
            case R.id.btn_question_detail_answer_again:
            {
                Intent intent = new Intent(MyQuestionDetailActivity.this, AskExpertAgainActivity.class);
                intent.putExtra("expertid", mSubject.getExpertId());
                intent.putExtra("subjectid", mSubject.getSubjectId());
                startActivity(intent);
                break;
            }
            case R.id.tvDesc:
            {
                Intent intent = new Intent(mContext, AnswerItemDetailActivity.class);
                String questionDes = mSubject.getContent();
                String filePath = mSubject.getFilePath();
                intent.putExtra("question_description", questionDes);
                intent.putExtra("file_path", filePath);
                startActivity(intent);
                break;
            }
        }
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
//                            Bitmap bitmap = null;
//                            try {
//                                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }
                            if(mTakedPhoto.exists()){
                                uploadImage(mTakedPhoto);
                            }

                        }else {
//                            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
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
                        File tm = new File(path);
                        if(tm.exists()){
                            uploadImage(tm);
                        }
                    } catch (URISyntaxException e) {

                    }
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(File file){

        //json格式post参数
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date).toString();

        String url = Urls.HOST_TEST + Urls.UPLOAD;
        PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "subjectView", mApplication.mAppSignature, dateStr, "9000");
        QuestionDetailPostBean param = new QuestionDetailPostBean(beanHead, String.valueOf(mSubject.getSubjectId()), String.valueOf(mPageIndex), "20");
        String strParam = gson.toJson(param);

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("file", file);
        paramsMap.put("json", strParam);

        OkHttpUtils.uploadFile(url, null, paramsMap);

    }
}
