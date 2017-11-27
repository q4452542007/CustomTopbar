package mapsoft.com.costomtopbar.fragment;


import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mapsoft.com.costomtopbar.db.SharedPreferenceManager;

/**
 * @author djl
 * @function
 */

public class SettingFragment extends Fragment implements View.OnClickListener{

    public static String TABLAYOUT_FRAGMENT = "tab_fragment";

    private Button saveBtn;

    private EditText ipEdit, portEdit, idEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_layout, container, false);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {
        saveBtn = (Button) view.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);
        ipEdit = (EditText) view.findViewById(R.id.edit_ip);
        portEdit = (EditText) view.findViewById(R.id.edit_port);
        idEdit = (EditText) view.findViewById(R.id.edit_id);
    }

    private void initData() {
        ipEdit.setText(SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.IP_SETTING,"0.0.0.0"));
        portEdit.setText(SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.PORT_SETTING,"1000"));
        idEdit.setText(SharedPreferenceManager.getInstance().getString(SharedPreferenceManager.ID_SETTING,"00000"));

    }


    public static SettingFragment newInstance(int type) {
        SettingFragment fragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                SharedPreferenceManager.getInstance().putString(SharedPreferenceManager.IP_SETTING, ipEdit.getText().toString());
                SharedPreferenceManager.getInstance().putString(SharedPreferenceManager.PORT_SETTING,portEdit.getText().toString());
                SharedPreferenceManager.getInstance().putString(SharedPreferenceManager.ID_SETTING, idEdit.getText().toString());
                Toast.makeText(getActivity(),"保存完毕",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
