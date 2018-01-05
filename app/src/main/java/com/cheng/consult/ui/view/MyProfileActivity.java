package com.cheng.consult.ui.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cheng.consult.R;
import com.cheng.consult.ui.common.PostCommonHead;
import com.cheng.consult.ui.common.Urls;
import com.cheng.consult.utils.LogUtils;
import com.cheng.consult.utils.OkHttpUtils;
import com.cheng.consult.utils.PreUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyProfileActivity extends BaseActivity implements View.OnTouchListener {
    private Toolbar mToolbar;
    private EditText mUserName;
    private EditText mPhoneNum;
    private EditText mIndustry;
    private EditText mArea;
    private EditText mEstTime;
    private EditText mCapital;
    private EditText mEmployeeNum;
    private EditText mProduction;
    //private EditText mIsConsulted;
    //private EditText mSaleArea;
    //private EditText mSaleMode;
    private RadioGroup mIsConslutedGroup;
    private RadioButton mConsulted;
    private RadioButton mNoConsulted;
    private boolean mIsConsulted = false;

    private RadioGroup mSaleArea;
    private RadioButton mSaleNative;
    private RadioButton mSaleOversea;
    private String saleArea = "native";

    private CheckBox mSaleAgency;
    private CheckBox mSaleTerminal;
    private CheckBox mSaleOnline;
    private int saleMode = 0;

    //private DatePicker mDatePicker;
    private EditText mEstYear;
    private EditText mEstMonth;

    private List<Integer> mEditTextLists;
    private List<OkHttpUtils.Param> mPostParams;
    private Button mSubmitBtn;
    private PreUtils mPreUtils;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.myfragment_my_profile_tip));

        mPreUtils = PreUtils.getInstance(mContext);

        addEditTextIdToList();

        mUserName = (EditText) findViewById(R.id.user_name);
        //mUserName.setOnTouchListener(this);
        mPhoneNum = (EditText)findViewById(R.id.user_phonenum);
        //mPhoneNum.setOnTouchListener(this);
        mIndustry = (EditText)findViewById(R.id.user_industry);
        //mIndustry.setOnTouchListener(this);
        mArea = (EditText)findViewById(R.id.user_area);
        //mArea.setOnTouchListener(this);
        //mEstTime = (EditText)findViewById(R.id.user_establish_time);
        //mEstTime.setOnTouchListener(this);
        mCapital = (EditText)findViewById(R.id.user_capital);
        //mCapital.setOnTouchListener(this);
        mEmployeeNum = (EditText)findViewById(R.id.user_employeenum);
        //mEmployeeNum.setOnTouchListener(this);
        mProduction = (EditText)findViewById(R.id.user_production);
        //mProduction.setOnTouchListener(this);
        //mIsConsulted = (EditText)findViewById(R.id.user_isconsulted);
        //mIsConsulted.setOnTouchListener(this);
        //mSaleArea = (EditText)findViewById(R.id.user_salearea);
//        mSaleArea.setOnTouchListener(this);
//        mSaleMode = (EditText)findViewById(R.id.user_salemode);
//        mSaleMode.setOnTouchListener(this);

        //成立时间
        mEstYear = (EditText)findViewById(R.id.establish_year);
        mEstMonth = (EditText)findViewById(R.id.establish_mouth);
//        mDatePicker = (DatePicker)findViewById(R.id.user_establish_time);
//        resizePikcer(mDatePicker);//调整datepicker大小
//        Calendar calendar = Calendar.getInstance();
//
//        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
//                    @Override
//                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        //TODO:
//                        int nyear = year;
//                        int nmonth = monthOfYear;
//                        int nday = dayOfMonth;
//                    }
//                });

        //是否咨询过
        mIsConslutedGroup = (RadioGroup)findViewById(R.id.isconslut);
        mIsConslutedGroup.setOnCheckedChangeListener(isConsultCheckListener);
        mConsulted = (RadioButton)findViewById(R.id.consulted);
        mNoConsulted = (RadioButton)findViewById(R.id.noconsulted);

        //销售区域
        mSaleArea = (RadioGroup)findViewById(R.id.user_salearea);
        mSaleArea.setOnCheckedChangeListener(saleareaCheckListener);
        mSaleNative = (RadioButton)findViewById(R.id.salenative);
        mSaleOversea = (RadioButton)findViewById(R.id.saleoversea);

        //销售模式
        mSaleAgency = (CheckBox)findViewById(R.id.agency);
        mSaleAgency.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    saleMode = 1;
                    if(mSaleTerminal.isChecked()){
                        saleMode += 2;
                    }
                    if(mSaleOnline.isChecked()){
                        saleMode += 4;
                    }
                }else{
                    saleMode = 0;
                    if(mSaleTerminal.isChecked()){
                        saleMode += 2;
                    }
                    if(mSaleOnline.isChecked()){
                        saleMode += 4;
                    }
                }
            }
        });
        mSaleTerminal = (CheckBox)findViewById(R.id.terminal);
        mSaleTerminal.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    saleMode = 2;
                    if(mSaleAgency.isChecked()){
                        saleMode += 1;
                    }
                    if(mSaleOnline.isChecked()){
                        saleMode += 4;
                    }
                }else {
                    saleMode = 0;
                    if(mSaleAgency.isChecked()){
                        saleMode += 1;
                    }
                    if(mSaleOnline.isChecked()){
                        saleMode += 4;
                    }
                }
            }
        });
        mSaleOnline = (CheckBox)findViewById(R.id.online);
        mSaleOnline.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    saleMode = 4;
                    if(mSaleAgency.isChecked()){
                        saleMode += 1;
                    }
                    if(mSaleTerminal.isChecked()){
                        saleMode += 2;
                    }
                }else {
                    saleMode = 0;
                    if(mSaleAgency.isChecked()){
                        saleMode += 1;
                    }
                    if(mSaleTerminal.isChecked()){
                        saleMode += 2;
                    }
                }
            }
        });

        //submit user profiles
        mSubmitBtn = (Button)findViewById(R.id.submit);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isProfileDone()){
                    Toast toast = Toast.makeText(mContext, mContext.getResources().getText(R.string.my_profile_error_toast), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    addParams();//include user id
                    saveUserProfile();
                    String url = Urls.HOST_TEST + Urls.USER;
                    OkHttpUtils.Param method = new OkHttpUtils.Param("method", "save");
                    mPostParams.add(method);

                    OkHttpUtils.post(url, null, mPostParams);
                    finish();
                }
            }
        });

    }

    class ProfilePostBean{
        private PostCommonHead.HEAD head;
        private ProfileBody body;

        class ProfileBody{
            private String loginId;//登录id
            private String userId;//企业/用户id
            private String name;//企业名称
            private String phoneNum;//电话
            private String industry;//所属行业
            private String area;//所在区域
            private String stablishDate;//成立日期
            private String registCapital;//注册资金
            private String employeNum;//雇员人数
            private String Production;//主要产品
            private String IsConsulted;//有无咨询经历,0:无；1：有
            private String SalesArea;//销售区域 外销内销
            private String SalesMode;//销售模式 代理商/经销商、终端客户、电商

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        //user profile
        mUserName.setText(mPreUtils.getUserName());
        mPhoneNum.setText(mPreUtils.getUserPhone());
        mIndustry.setText(mPreUtils.getUserIndustry());
        mArea.setText(mPreUtils.getUserArea());

        String est = mPreUtils.getUserEstime();
        String estYear = est.substring(0, est.indexOf("-"));
        //LogUtils.v("MyProfileActivity", "indexof-=" + est.indexOf("-") + " estYear=" + estYear);
        String estMonth = est.substring(est.indexOf("-")+ 1);
        mEstYear.setText(estYear);
        mEstMonth.setText(estMonth);

        mCapital.setText(mPreUtils.getUserCapital());
        mEmployeeNum.setText(mPreUtils.getUserEmployeeNum());
        mProduction.setText(mPreUtils.getUserProduction());

        String isConsult = mPreUtils.getUserIsConsulted();
        if(isConsult.equalsIgnoreCase("true")){
            mConsulted.setChecked(true);
            mNoConsulted.setChecked(false);
        } else {
            mConsulted.setChecked(false);
            mNoConsulted.setChecked(true);
        }

        String saleArea = mPreUtils.getUserSaleArea();
        if(saleArea.equalsIgnoreCase("native")){
            mSaleNative.setChecked(true);
            mSaleOversea.setChecked(false);
        }else {
            mSaleNative.setChecked(false);
            mSaleOversea.setChecked(true);
        }
        setSaleModeUi();
    }

    private void setSaleModeUi(){
        int saleMode = Integer.parseInt(mPreUtils.getUserSaleMode());
        if(1 == saleMode){
            mSaleAgency.setChecked(true);
            mSaleTerminal.setChecked(false);
            mSaleOnline.setChecked(false);
        }
        if(2 == saleMode){
            mSaleAgency.setChecked(false);
            mSaleTerminal.setChecked(true);
            mSaleOnline.setChecked(false);
        }
        if(4 == saleMode){
            mSaleAgency.setChecked(false);
            mSaleTerminal.setChecked(false);
            mSaleOnline.setChecked(true);
        }
        if(3 == saleMode){
            mSaleAgency.setChecked(true);
            mSaleTerminal.setChecked(true);
            mSaleOnline.setChecked(false);
        }
        if(5 == saleMode){
            mSaleAgency.setChecked(true);
            mSaleTerminal.setChecked(false);
            mSaleOnline.setChecked(true);
        }
        if(6 == saleMode){
            mSaleAgency.setChecked(false);
            mSaleTerminal.setChecked(true);
            mSaleOnline.setChecked(true);
        }
        if(7 == saleMode){
            mSaleAgency.setChecked(true);
            mSaleTerminal.setChecked(true);
            mSaleOnline.setChecked(true);
        }
    }

    private void addParams(){
        mPostParams = new ArrayList<>();
        OkHttpUtils.Param param1 = new OkHttpUtils.Param("username", mUserName.getText().toString().trim());
        mPostParams.add(param1);
        OkHttpUtils.Param param2 = new OkHttpUtils.Param("phonenum", mPhoneNum.getText().toString().trim());
        mPostParams.add(param2);
        OkHttpUtils.Param param3 = new OkHttpUtils.Param("area", mArea.getText().toString().trim());
        mPostParams.add(param3);
        OkHttpUtils.Param param4 = new OkHttpUtils.Param("capital", mCapital.getText().toString().trim());
        mPostParams.add(param4);
        OkHttpUtils.Param param5 = new OkHttpUtils.Param("industry", mIndustry.getText().toString().trim());
        mPostParams.add(param5);
        OkHttpUtils.Param param6 = new OkHttpUtils.Param("employee", mEmployeeNum.getText().toString().trim());
        mPostParams.add(param6);
        OkHttpUtils.Param param7 = new OkHttpUtils.Param("production", mProduction.getText().toString().trim());
        mPostParams.add(param7);
        OkHttpUtils.Param param8 = new OkHttpUtils.Param("estime", mEstYear.getText().toString().trim() + "-" + mEstMonth.getText().toString().trim());
        mPostParams.add(param8);
        OkHttpUtils.Param param9 = new OkHttpUtils.Param("saleArea", saleArea);
        mPostParams.add(param9);
        OkHttpUtils.Param param10 = new OkHttpUtils.Param("mIsConsulted", mIsConsulted ? "true" : "false");
        mPostParams.add(param10);
        OkHttpUtils.Param param11 = new OkHttpUtils.Param("saleMode", Integer.toString(saleMode));
        mPostParams.add(param11);
        OkHttpUtils.Param param12 = new OkHttpUtils.Param("Id", String.valueOf(mPreUtils.getUserId()));
        mPostParams.add(param12);

    }

    private void saveUserProfile(){
        mPreUtils.setUserName(mUserName.getText().toString().trim());
        mPreUtils.setUserPhone(mPhoneNum.getText().toString().trim());
        mPreUtils.setUserArea(mArea.getText().toString().trim());
        mPreUtils.setUserCapital(mCapital.getText().toString().trim());
        mPreUtils.setUserIndustry(mIndustry.getText().toString().trim());
        mPreUtils.setUserEmployeeNum(mEmployeeNum.getText().toString().trim());
        mPreUtils.setUserProduction(mProduction.getText().toString().trim());
        mPreUtils.setUserEstime(mEstYear.getText().toString().trim() + "-" + mEstMonth.getText().toString().trim());
        mPreUtils.setUserSaleArea(saleArea);
        mPreUtils.setUserIsConsulted((mIsConsulted ? "true" : "false"));
        mPreUtils.setUserSaleMode(Integer.toString(saleMode));

    }

    private boolean isProfileDone(){
        boolean result = false;
        if((!mUserName.getText().toString().trim().equals("")) && (!mPhoneNum.getText().toString().trim().equals("")) && (!mArea.getText().toString().trim().equals("")) &&
                (!mCapital.getText().toString().trim().equals("")) && (!mIndustry.getText().toString().trim().equals("")) && (!mEmployeeNum.getText().toString().trim().equals("")) &&
                (!mProduction.getText().toString().trim().equals("")) && (!mEstYear.getText().toString().trim().equals("")) && (!mEstMonth.getText().toString().trim().equals("")) &&
                (saleMode != 0)){
            result = true;
        }
        return result;
    }

    /**
     * 调整FrameLayout大小
     * @param tp
     */
    private void resizePikcer(FrameLayout tp){
        List<NumberPicker> npList = findNumberPicker(tp);
        for(NumberPicker np:npList){
            resizeNumberPicker(np);
        }
    }


    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup){
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if(null != viewGroup){
            for(int i = 0;i<viewGroup.getChildCount();i++){
                child = viewGroup.getChildAt(i);
                if(child instanceof NumberPicker){
                    npList.add((NumberPicker)child);
                }
                else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup)child);
                    if(result.size()>0){
                        return result;
                    }
                }
            }
        }
        return npList;
    }




    RadioGroup.OnCheckedChangeListener isConsultCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == mConsulted.getId()){
                mIsConsulted = true;
            }else if(checkedId == mNoConsulted.getId()){
                mIsConsulted = false;
            }
        }
    };

    RadioGroup.OnCheckedChangeListener saleareaCheckListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == mSaleNative.getId()){
                saleArea = "native";
            }else if(checkedId == mSaleOversea.getId()){
                saleArea = "oversea";
            }
        }
    };

    private void addEditTextIdToList(){
        mEditTextLists = new ArrayList<>();
        mEditTextLists.add(R.id.user_name);
        mEditTextLists.add(R.id.user_phonenum);
        mEditTextLists.add(R.id.user_industry);
        mEditTextLists.add(R.id.user_area);
        //mEditTextLists.add(R.id.user_establish_time);
        mEditTextLists.add(R.id.user_capital);
        mEditTextLists.add(R.id.user_employeenum);
        mEditTextLists.add(R.id.user_production);
        //mEditTextLists.add(R.id.user_isconsulted);
        //mEditTextLists.add(R.id.user_salearea);
        //mEditTextLists.add(R.id.user_salemode);

    }

    private boolean isEditText(View view){
        boolean result = false;
        int id = view.getId();
        if(mEditTextLists.contains(id)){
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        //if ((isEditText(v) && canVerticalScroll((EditText) v))) {
//        if(isEditText(v)){
//            v.getParent().requestDisallowInterceptTouchEvent(true);
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                v.getParent().requestDisallowInterceptTouchEvent(false);
//            }
//        }
        //v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        boolean result = false;
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return result;
        }

        if((scrollY > 0) || (scrollY < scrollDifference - 1)){
            result = true;
        }
        return result;
    }
}
