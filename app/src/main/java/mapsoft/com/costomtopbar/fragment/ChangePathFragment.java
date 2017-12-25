package mapsoft.com.costomtopbar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.secondbook.com.buttonfragment.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import mapsoft.com.costomtopbar.html.ReadHtmlThread;
import static android.view.View.inflate;


public class ChangePathFragment extends BaseFragment implements View.OnClickListener{

    private static final String ARG_PATH = "path";

    private static final String TAG = "ChangePathFragment";
    private ReadHtmlThread mReadHtmlThread;

    private AlertDialog dlgSpecItem;
    private View specItemView;

    private Spinner mSpinner;
    private ArrayList<String> pathList;

    private WebView mWebView;

    private Button switchUpPath;
    private Button switchDownPath;
    private String pathNum;

    private FragmentInteraction2 listener;




    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  //指示系统保留当前的fragment实例,即使是在Activity被重新创建的时候

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path, container, false);
        pathList = new ArrayList<>();
        mWebView = (WebView) view.findViewById(R.id.web_view);
        mWebView.setBackgroundColor(0);
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        String PATH = Environment.getExternalStorageDirectory() + "/";
        File specItemDir = new File(PATH + "path" );
        final File[] files = specItemDir.listFiles();
        pathList.add("请选择线路");
        int i =1;
        for (File file : files) {
            if (file.getName().length() > 3) {
                pathList.add(file.getName().substring(0, file.getName().length() - 5)+"路");
                i++;
            }
        }
        switchUpPath = (Button) view.findViewById(R.id.up);
        switchUpPath.setOnClickListener(this);
        switchDownPath = (Button) view.findViewById(R.id.down);
        switchDownPath.setOnClickListener(this);
        //参数包括( 句柄， 下拉列表显示方式layout（这里采用系统自带的), 下拉列表中的文本id值， 待显示的字符串数组 )；
        // ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner, R.id.textView3333, s1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner, pathList) {
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = inflate(getContext(), R.layout.spinner_item_layout,
                        null);
                TextView label = (TextView) view
                        .findViewById(R.id.spinner_item_label);
                ImageView check = (ImageView) view
                        .findViewById(R.id.spinner_item_checked_image);
                if (position == 0){
                    check.setVisibility(View.GONE);
                }
                label.setText(pathList.get(position));

                if (mSpinner.getSelectedItemPosition() == position) {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.spinner_green));
                    check.setImageResource(R.drawable.select);
                } else {
                    view.setBackgroundColor(getResources().getColor(
                            R.color.spinner_light_green));
                    check.setImageResource(R.drawable.unselect);
                }

                return view;
            }

        };
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    mWebView.loadUrl("file:///sdcard/path/"+pathList.get(position).substring(0,pathList.get(position).length()-1)+".html");
                    pathNum = pathList.get(position).substring(0,pathList.get(position).length()-1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public static ChangePathFragment newInstance(String path) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PATH,path);

        ChangePathFragment fragment = new ChangePathFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Destroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.up:
                listener.sendPath(pathNum,"up");
                break;
            case R.id.down:
                listener.sendPath(pathNum,"down");
                break;
        }
    }

    /**
     * 定义了所有activity必须实现的接口
     */
    public interface FragmentInteraction2
    {
        /**
         * Fragment 向Activity传递指令，这个方法可以根据需求来定义
         * @param
         */
        void sendPath(String pathNum, String direction);
    }

    /**
     * 当FRagmen被加载到activity的时候会被回调
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof FragmentInteraction2)
        {
            listener = (FragmentInteraction2)activity;
        }
        else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }






    @Override
    public void onStop() {
        super.onStop();

    }
}
