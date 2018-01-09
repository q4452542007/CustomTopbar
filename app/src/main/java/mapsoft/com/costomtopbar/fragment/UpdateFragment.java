package mapsoft.com.costomtopbar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.secondbook.com.buttonfragment.R;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mapsoft.com.costomtopbar.service.socket.CommonDialog;
import mapsoft.com.costomtopbar.service.socket.update.UpdateService;
import mapsoft.com.costomtopbar.service.socket.update.Util;

/**
 * @author djl
 * @function
 */

public class UpdateFragment extends Fragment {
    public static String TABLAYOUT_FRAGMENT = "tab_fragment";

    private Button updateBtn;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_layout, container, false);
        mContext = getActivity();
        initView(view);
        return view;
    }


    private void initView(View view) {
        updateBtn = (Button) view.findViewById(R.id.check_version);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //说明有新版本,开始下载
                CommonDialog dialog = new CommonDialog(mContext, getString(R.string.update_new_version),
                        getString(R.string.update_title), getString(R.string.update_install),
                        getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                    @Override
                    public void onDialogClick() {
                        Intent intent = new Intent(mContext, UpdateService.class);
                        mContext.startService(intent);
                    }
                });
                dialog.show();
            }
        });
    }

    /*//发送版本检查更新请求
    private void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    //说明有新版本,开始下载
                    CommonDialog dialog = new CommonDialog(mContext, getString(R.string.update_new_version),
                            getString(R.string.update_title), getString(R.string.update_install),
                            getString(R.string.cancel), new CommonDialog.DialogClickListener() {
                        @Override
                        public void onDialogClick() {
                            Intent intent = new Intent(mContext, UpdateService.class);
                            mContext.startService(intent);
                        }
                    });
                    dialog.show();
                } else {
                    //弹出一个toast提示当前已经是最新版本等处理
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }*/


    public static UpdateFragment newInstance(int type) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, type);
        fragment.setArguments(bundle);
        return fragment;
    }


}
