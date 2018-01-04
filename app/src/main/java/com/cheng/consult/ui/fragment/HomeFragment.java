package com.cheng.consult.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.view.ExpertCategoryActivity;
import com.cheng.consult.ui.view.MyConsultQuestionActivity;
import com.cheng.consult.ui.view.MyLoveExpertListActivity;
import com.cheng.consult.ui.view.SearchActivity;
import com.cheng.consult.utils.ToolsUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by cheng on 2017/11/13.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private LinearLayout searchView;
    private LinearLayout mQueryExp;
    private LinearLayout mQueryField;
    private LinearLayout mHistoryQuestion;
    private LinearLayout mEssense;
    private Button mQuickAskBtn;
    private LinearLayout mMyLoveExperts;
    private TextView mSearchBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homefrag_layout, null, false);
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
//                Intent intent = new Intent(getActivity(), AskExpertActivity.class);
//                startActivity(intent);
//                if (getContext() instanceof Activity) {
//                    ((Activity) getContext()).overridePendingTransition(R.anim.slide_alpha_in, R.anim.slide_alpha_out);
//                }

                showFileChooser();
                break;
            }
            default:
                break;
        }

    }

    String filepath = "";
    String filename = "";

    private static final int FILE_SELECT_CODE = 0;

    /** 调用文件选择软件来选择文件 **/
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
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
        super.onActivityResult(requestCode, resultCode, data);
    }



}
