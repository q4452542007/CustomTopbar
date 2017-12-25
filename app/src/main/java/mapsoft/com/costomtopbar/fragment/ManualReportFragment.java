package mapsoft.com.costomtopbar.fragment;

import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mapsoft.com.costomtopbar.activity.MainActivity;
import mapsoft.com.costomtopbar.constant.Constant;

/**
 * @author djl
 * @function
 */

public class ManualReportFragment extends BaseFragment implements View.OnClickListener{

    private Button btn_in,btn_out,btn_next;
    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manul_report_layout, container, false);
        initView(view);
        mMainActivity = (MainActivity) getActivity();
        return view;
    }

    private void initView(View view) {
        btn_in = (Button) view.findViewById(R.id.in);
        btn_in.setOnClickListener(this);
        btn_out = (Button) view.findViewById(R.id.out);
        btn_out.setOnClickListener(this);
        btn_next = (Button) view.findViewById(R.id.next);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static ManualReportFragment newInstance(String content) {
        ManualReportFragment fragment = new ManualReportFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.in:
                mMainActivity.manulReport(Constant.IN_STATION);
                break;
            case R.id.next:
                mMainActivity.manulReport(Constant.NEXT_STATION);
                break;
            case R.id.out:
                mMainActivity.manulReport(Constant.OUT_STATION);
                break;
        }
    }
}
