package mapsoft.com.costomtopbar.fragment;

import android.os.Build;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

import mapsoft.com.costomtopbar.view.IconCut;

/**
 * @author djl
 * @function
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener{

    private RelativeLayout mLoginlayout;

    private ImageView mUserView,mPwdView;

    private EditText mUserEdit,mPwdEdit,mCurrentEdtView;
    
    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btndelet;

    private Editable mEditable;

    private String mString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {

        mUserView = (ImageView) v.findViewById(R.id.user_view);
        mUserEdit = (EditText) v.findViewById(R.id.user_edit);
        //mUserEdit.setInputType(InputType.TYPE_NULL);  //不显示光标

        mUserEdit.setOnFocusChangeListener(mFocusChangedListener);
        //mUserView.setOnClickListener(this);
        //mUserView.setImageBitmap(IconCut.getInstance(getActivity()).cutLoginUser());
        mPwdView = (ImageView) v.findViewById(R.id.pwd_view);
        mPwdEdit = (EditText) v.findViewById(R.id.pwd_edit);

        mPwdEdit.setOnFocusChangeListener(mFocusChangedListener);
        if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下 danielinbiti
            mUserEdit.setInputType(InputType.TYPE_NULL);
            mPwdEdit.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mUserEdit, false);
                setShowSoftInputOnFocus.invoke(mPwdEdit, false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //mPwdView.setOnClickListener(this);
       // mPwdView.setImageBitmap(IconCut.getInstance(getActivity()).cutLoginPwd());
        btn0 = (Button) v.findViewById(R.id.num0);
        btn0.setOnClickListener(this);
        btn1 = (Button) v.findViewById(R.id.num1);
        btn1.setOnClickListener(this);
        btn2 = (Button) v.findViewById(R.id.num2);
        btn2.setOnClickListener(this);
        btn3 = (Button) v.findViewById(R.id.num3);
        btn3.setOnClickListener(this);
        btn4 = (Button) v.findViewById(R.id.num4);
        btn4.setOnClickListener(this);
        btn5 = (Button) v.findViewById(R.id.num5);
        btn5.setOnClickListener(this);
        btn6 = (Button) v.findViewById(R.id.num6);
        btn6.setOnClickListener(this);
        btn7 = (Button) v.findViewById(R.id.num7);
        btn7.setOnClickListener(this);
        btn8 = (Button) v.findViewById(R.id.num8);
        btn8.setOnClickListener(this);
        btn9 = (Button) v.findViewById(R.id.num9);
        btn9.setOnClickListener(this);
        btndelet = (Button) v.findViewById(R.id.del);
        btndelet.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    


    public static AffairFragment newInstance(String content) {
        AffairFragment fragment = new AffairFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.num0:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("0");
                }else {
                mEditable=mCurrentEdtView.getText();
                mString = mEditable+"0";
                mCurrentEdtView.setText(mString);
                mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num1:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("1");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"1";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num2:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("2");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"2";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num3:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("3");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"3";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num4:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("4");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"4";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num5:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("5");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"5";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num6:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("6");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"6";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num7:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("7");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"7";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num8:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("8");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"8";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;
            case R.id.num9:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText()==null){
                    mCurrentEdtView.setText("9");
                }else {
                    mEditable=mCurrentEdtView.getText();
                    mString = mEditable+"9";
                    mCurrentEdtView.setText(mString);
                    mCurrentEdtView.setSelection(mString.length());
                }
                break;

            case R.id.del:
                if (mCurrentEdtView ==null) {
                    return;
                }
                if (mCurrentEdtView.getText().length()==0){

                }else {
                    //int index = editText.getSelectionStart();
                    //Editable editable = editText.getText();
                    mEditable = mCurrentEdtView.getText();
                    mEditable.delete(mCurrentEdtView.getSelectionStart()-1, mCurrentEdtView.getSelectionStart());

                    /*mCurrentEdtView.setText(mString.substring(0,mString.length()-2));*/
                    mCurrentEdtView.setSelection(mEditable.length());
                }

        }

    }

    private View.OnFocusChangeListener mFocusChangedListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus){
                mCurrentEdtView = (EditText) view;
            }
        }
    };
}
