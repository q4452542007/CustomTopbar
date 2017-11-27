package mapsoft.com.costomtopbar.fragment;

import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.secondbook.com.buttonfragment.R;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



/**
 * @author djl
 * @function
 */

public class ReportStationFragment extends Fragment {

    public static String TABLAYOUT_FRAGMENT = "tab_fragment";

    private Button startNotify;
    private Vibrator mVibrator;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportstation_layout, container, false);

        mVibrator =(Vibrator)getActivity().getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        startNotify = (Button)view.findViewById(R.id.notifystart);

        return view;
    }



    public static ReportStationFragment newInstance(int type) {
        ReportStationFragment fragment = new ReportStationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


}
