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
public class OptionFragment extends BaseFragment implements View.OnClickListener{

    private static final String ARG_TV = "option";

    private String content;

    private ImageButton checkBtn,changeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);
        /*checkBtn = (ImageButton) view.findViewById(R.id.checkbtn);
        changeBtn = (ImageButton) view.findViewById(R.id.changebtn);
        checkBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);*/
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static OptionFragment newInstance(String content) {
        OptionFragment fragment = new OptionFragment();
        return fragment;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.checkbtn:
                Intent gps_intent = new Intent(getActivity(),
                        TabLayoutActivity.class);
                getActivity().startActivity(gps_intent);
                break;*/

            /*case R.id.changebtn:
                MainActivity mainActivity = (MainActivity ) getActivity();
                mainActivity.setPathBtn();
                break;*/

            default:
                break;
        }
        // 不要忘记提交

    }

}
