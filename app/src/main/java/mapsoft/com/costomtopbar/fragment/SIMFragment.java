package mapsoft.com.costomtopbar.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;


import android.secondbook.com.buttonfragment.R;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/10.
 */

public class SIMFragment extends Fragment {
    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    ListView showView;
    // 声明代表状态名的数组
    String[] statusNames;
    // 声明代表手机状态的集合
    ArrayList<String> statusValues = new ArrayList<String>();
    private Button exitbtn;

    public static SIMFragment newInstance(int type) {
        SIMFragment fragment = new SIMFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TelephonyManager tManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        // 获取各种状态名称的数组
        statusNames = getResources().getStringArray(R.array.statusNames);
        // 获取代表SIM卡状态的数组
        String[] simState = getResources().getStringArray(R.array.simState);
        // 获取代表电话网络类型的数组
        String[] phoneType = getResources().getStringArray(R.array.phoneType);
        // 获取设备编号
        statusValues.add(tManager.getDeviceId());
        // 获取系统平台的版本
        statusValues.add(tManager.getDeviceSoftwareVersion() != null ? tManager
                .getDeviceSoftwareVersion() : this
                .getString(R.string.info_unknow));
        // 获取网络运营商代号
        statusValues.add(tManager.getNetworkOperator());
        // 获取网络运营商名称
        statusValues.add(tManager.getNetworkOperatorName());
        // 获取手机网络类型
        statusValues.add(phoneType[tManager.getPhoneType()]);
        // 获取设备所在位置
        statusValues.add(tManager.getCellLocation() != null ? tManager
                .getCellLocation().toString() : this
                .getString(R.string.info_unknow_site));
        // 获取SIM卡的国别
        statusValues.add(tManager.getSimCountryIso());
        // 获取SIM卡序列号
        statusValues.add(tManager.getSimSerialNumber());
        // 获取SIM卡状态
        statusValues.add(simState[tManager.getSimState()]);
        // 获得ListView对象

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sim_layout, container, false);
        initView(view);
        return view;
    }


    protected void initView(View view) {
        ButtonListener buttoner = new ButtonListener();
        exitbtn = (Button) view.findViewById(R.id.exitButton);
        exitbtn.setOnClickListener(buttoner);
        showView = (ListView) view.findViewById(R.id.show_sim);
        ArrayList<Map<String, String>> status = new ArrayList<Map<String, String>>();
        // 遍历statusValues集合，将statusNames、statusValues
        // 的数据封装到List<Map<String , String>>集合中
        for (int i = 0; i < statusValues.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", statusNames[i]);
            map.put("value", statusValues.get(i));
            status.add(map);
        }
        // 使用SimpleAdapter封装List数据
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), status,
                R.layout.sim_line, new String[] { "name", "value" }, new int[] {
                R.id.name_sim, R.id.value_sim });
        // 为ListView设置Adapter
        showView.setAdapter(adapter);
    }
    class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.exitButton){
                getActivity().finish();
                //System.exit(0);
            }
        }
    }
}

