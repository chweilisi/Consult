package com.cheng.consult.ui.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheng.consult.R;
import com.cheng.consult.widget.EditTextWithDel;

public class SearchActivity extends BaseActivity {

    private EditTextWithDel searchInput;
    private TextView searchCancel;
    private ImageButton deleteView;
//    private int searchType = HistorySearch.COLLEGE_SEARCH;
    private int currentFragmentType = -1;//当前片段标记
    private int skipMark;//院校和专业的标记
    private int nColumnNum = 3;
    private TextView searchTypeName;
    private ImageView searchArrowImg;
//    private SearchPopUpWindow searchPopUpWindow;
    private RelativeLayout searchTopLayout;
//    private HotSearchFragment hotSearchFragment = new HotSearchFragment();


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        searchInput = (EditTextWithDel) findViewById(R.id.search_bar_input);
//        searchCancel = (TextView) findViewById(R.id.search_cancel);
//        deleteView = (ImageButton) this.findViewById(R.id.search_bar_del_imageBtn);
//
//        searchTypeName = (TextView) findViewById(R.id.search_type_name);
//        searchArrowImg = (ImageView) findViewById(R.id.search_arrow_img);
//        searchTopLayout = (RelativeLayout) findViewById(R.id.search_top_layout);
//
//        searchTypeName.setOnClickListener(onClickListener);
//        searchArrowImg.setOnClickListener(onClickListener);
//        findViewById(R.id.search_cancel).setOnClickListener(onClickListener);
//        searchInput.setOnEditorActionListener(actionListener);
//        searchInput.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                    searchInput.setCursorVisible(true);
//                }
//                return false;
//            }
//        });
//
//        searchPopUpWindow = new SearchPopUpWindow(this);
//
//        loadHotSearch();

        //弹出软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchInput, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


}
