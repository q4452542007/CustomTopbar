package mapsoft.com.costomtopbar.fragment;

import android.os.Bundle;

import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author djl
 * @function
 */

public class TalkFragment extends BaseFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
    }


    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static TalkFragment newInstance(String content) {
        TalkFragment fragment = new TalkFragment();
        return fragment;
    }
}
