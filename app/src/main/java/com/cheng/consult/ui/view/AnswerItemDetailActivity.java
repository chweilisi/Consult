package com.cheng.consult.ui.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.db.table.Subject;
import com.cheng.consult.db.table.SubjectItem;
import com.cheng.consult.ui.common.Constants;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.LogUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Response;

public class AnswerItemDetailActivity extends BaseActivity {

    private TextView mAnswerDetail;
    private String mAnswer;
    private int mItemType = -1;
    private Subject mSubject;
    private SubjectItem mAnswerItem;
    private String mQuestionDes;
    private String mAttachmentPath;
    private ImageView mAttachImg;
    private File mDownloadImgFile;
    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_answer_item_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAttachImg = (ImageView)findViewById(R.id.answer_item_detail_attachment_img);

        mAnswerDetail = (TextView)findViewById(R.id.tv_answer_item_detail_page);

        String answerItem = getIntent().getStringExtra("answer_item");
        if(answerItem != null && !answerItem.isEmpty()){
            mAnswerItem = gson.fromJson(answerItem, SubjectItem.class);

            mItemType = mAnswerItem.getItemType();
            if(0 == mItemType){
                getSupportActionBar().setTitle("问题详情");
            } else {
                getSupportActionBar().setTitle("回答详情");
            }

            mAnswer = mAnswerItem.getContent();
            if(mAnswer != null && !mAnswer.isEmpty()){
                mAnswerDetail.setText(mAnswer);
            }

            mAttachmentPath = mAnswerItem.getFilePath();
            if(null != mAttachmentPath && !mAttachmentPath.isEmpty()){
                int pre = mAttachmentPath.lastIndexOf("/");
                String name = mAttachmentPath.substring(pre + 1);
                mDownloadImgFile = new File(Constants.IMAGE_CACHE_DIR + "take_picture/", name);
                Thread imgThread = new GetAttachmentImage(Urls.ATTACHMENT_BASE_URL, mAttachmentPath);
                imgThread.start();
            }

//            if(null == mAttachmentPath){
//
//            } else {
//                mDownloadImgFile = new File(Constants.IMAGE_CACHE_DIR + "take_picture/", name);
//                OkHttpUtils.ResultCallback<Response> mAttachmentImgCallback = new OkHttpUtils.ResultCallback<Response>() {
//                    @Override
//                    public void onSuccess(Response response) {
//                        InputStream is = null;
//                        byte[] buf = new byte[2048];
//                        int len = 0;
//                        FileOutputStream fos = null;
//                        try {
//                            long total = response.body().contentLength();
//
//                            long current = 0;
//                            is = response.body().byteStream();
//                            fos = new FileOutputStream(mDownloadImgFile);
//                            while ((len = is.read(buf)) != -1) {
//                                current += len;
//                                fos.write(buf, 0, len);
//                            }
//                            fos.flush();
//
//                        } catch (IOException e) {
//
//                        } finally {
//                            try {
//                                if (is != null) {
//                                    is.close();
//                                }
//                                if (fos != null) {
//                                    fos.close();
//                                }
//                            } catch (IOException e) {
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Exception e) {
//
//                    }
//                };
//
//                //json格式post参数
//                Date date = new Date();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateStr = dateFormat.format(date).toString();
//
//                String url = Urls.ATTACHMENT_BASE_URL + mAttachmentPath;
//                PostCommonHead.HEAD beanHead = new PostCommonHead.HEAD("1", "", mApplication.mAppSignature, dateStr, "9000");
//                GetImgAttachmentPostParam postParam = new GetImgAttachmentPostParam(beanHead);
//                String param = gson.toJson(postParam);
//                OkHttpUtils.downloadFile(url, mAttachmentImgCallback, param);
//
//                Thread imgThread = new GetAttachmentImage(Urls.ATTACHMENT_BASE_URL, mAttachmentPath);
//                imgThread.start();
//            }
        }



        //original question item
        mQuestionDes = getIntent().getStringExtra("question_description");
        mAttachmentPath = getIntent().getStringExtra("file_path");

        if(mQuestionDes != null && !mQuestionDes.isEmpty()){
            getSupportActionBar().setTitle("问题详情");
            mAnswerDetail.setText(mQuestionDes);

            if(null != mAttachmentPath && !mAttachmentPath.isEmpty()){
                int pre = mAttachmentPath.lastIndexOf("/");
                String name = mAttachmentPath.substring(pre + 1);
                mDownloadImgFile = new File(Constants.IMAGE_CACHE_DIR + "take_picture/", name);
                Thread imgThread = new GetAttachmentImage(Urls.ATTACHMENT_BASE_URL, mAttachmentPath);
                imgThread.start();
            }
        }

    }

    class GetImgAttachmentPostParam{
        private PostCommonHead.HEAD head;
        private PostBody body;

        public GetImgAttachmentPostParam(PostCommonHead.HEAD head) {
            this.head = head;
            this.body = body;
        }

        class PostBody{

        }

    }

    private class GetAttachmentImage extends Thread {
        private String fileUrl;
        private String fileName;
        public GetAttachmentImage(String fileUrl, String fileName){
            this.fileUrl = fileUrl;
            this.fileName = fileName;
        }

        public void run(){
            if(!mDownloadImgFile.exists()){
                try {
                    URL url = new URL(fileUrl + fileName);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(mDownloadImgFile);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }

                        is.close();
                        fos.close();
                    }
                }catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //set img
            if (mContext != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mDownloadImgFile.exists()){
                            mAttachImg.setVisibility(View.VISIBLE);
                            Bitmap bitmap = BitmapFactory.decodeFile(mDownloadImgFile.getAbsolutePath());
                            mAttachImg.setImageBitmap(bitmap);
                        }

                    }
                });
            }
        }
    }

}
