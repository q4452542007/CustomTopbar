package mapsoft.com.costomtopbar.fragment;



import android.os.Bundle;

import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by WangChang on 2016/5/15.
 */
public class AffairFragment extends BaseFragment {


    private String title;
    private ImageButton carBtn, disputeBtn, recoverBtn, repairBtn, reportBtn, stopBtn, requestBtn, mapBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_affair, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        /*carBtn = (ImageButton) v.findViewById(R.id.car);
        disputeBtn = (ImageButton) v.findViewById(R.id.dispute);
        recoverBtn = (ImageButton) v.findViewById(R.id.recover);
        repairBtn = (ImageButton) v.findViewById(R.id.repair);
        reportBtn = (ImageButton) v.findViewById(R.id.report);
        stopBtn = (ImageButton) v.findViewById(R.id.stop);
        requestBtn = (ImageButton) v.findViewById(R.id.request);
        mapBtn = (ImageButton) v.findViewById(R.id.map);
*/

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
