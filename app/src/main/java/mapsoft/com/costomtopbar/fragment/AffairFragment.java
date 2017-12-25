package mapsoft.com.costomtopbar.fragment;



import android.content.Intent;
import android.os.Bundle;

import android.os.Message;
import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.activity.MessageActivity;
import mapsoft.com.costomtopbar.activity.TabLayoutActivity;
import mapsoft.com.costomtopbar.view.Topbar;

/**
 * Created by WangChang on 2016/5/15.
 */
public class AffairFragment extends BaseFragment {


    private String title;
    private Button carBtn, disputeBtn, recoverBtn, repairBtn, reportBtn, stopBtn, requestBtn, messageBtn, loginBtn, optionBtn;
    private Topbar mTopbar;
    private MainActivity.MyHandler mMyHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affair, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        mMyHandler = new MainActivity.MyHandler((MainActivity) getActivity());
        final Message mMessage =new Message();
        mMessage.what=0x004;

        loginBtn = (Button) v.findViewById(R.id.button_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.toLoginFragment();
            }
        });
        optionBtn = (Button) v.findViewById(R.id.button_option);
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionIntent = new Intent(getActivity(),
                        TabLayoutActivity.class);
                getActivity().startActivity(optionIntent);
            }
        });
        messageBtn = (Button) v.findViewById(R.id.button_message);
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyHandler.sendMessage(mMessage);
                Intent messageIntent = new Intent(getActivity(),
                        MessageActivity.class);
                getActivity().startActivity(messageIntent);
            }
        });
        reportBtn = (Button) v.findViewById(R.id.button_report);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.toReportFragment();
            }
        });

    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static AffairFragment newInstance(String content) {
        AffairFragment fragment = new AffairFragment();
        return fragment;
    }


}
