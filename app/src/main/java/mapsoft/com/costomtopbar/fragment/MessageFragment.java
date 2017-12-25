package mapsoft.com.costomtopbar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.activity.MessageActivity;
import mapsoft.com.costomtopbar.activity.TabLayoutActivity;

/**
 * @author djl
 * @function
 */

public class MessageFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affair, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {

       /* loginBtn = (Button) v.findViewById(R.id.button_login);
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
                Intent messageIntent = new Intent(getActivity(),
                        MessageActivity.class);
                getActivity().startActivity(messageIntent);
            }
        });*/

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
