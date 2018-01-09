package mapsoft.com.costomtopbar.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author djl
 * @function
 */

public class LogFragment extends Fragment {

    private TextView mTextView;
    private StringBuffer sb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement_log_layout, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mTextView = (TextView) view.findViewById(R.id.log_view);
        sb = new StringBuffer();

    }

    public static LogFragment newInstance(int type) {
        LogFragment fragment = new LogFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void logd(String msg) {
        sb.append(msg);
        //解析定位结果，
        String result = sb.toString();
        mTextView.setText(result);
    }
}
