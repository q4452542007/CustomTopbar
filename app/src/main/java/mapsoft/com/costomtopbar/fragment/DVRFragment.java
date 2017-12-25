package mapsoft.com.costomtopbar.fragment;

import android.hardware.Camera;
import android.kpocom.Gpioctljni;
import android.media.MediaRecorder;
import android.os.Bundle;

import android.secondbook.com.buttonfragment.R;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class DVRFragment extends BaseFragment implements View.OnClickListener,SurfaceHolder.Callback {

    private static final String ARG_TV = "option";
    private static final String TAG = "DVR";

    // UI相关
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button okButton, photoButton, videoButton, browseButton;
    private Spinner systemSpinner, channelSpinner;

    private boolean isOnPause = false;
    // 功能相关
    private MediaRecorder mediaRecorder;
    private String videoFile = null;
    private boolean isRecord = false; // 判断是否录像

    private Button switchCamera;
    private Button checkCamera;
    private Button exit;
    private int index;
    private Camera cam;
    private SurfaceView surfaceview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dvr_layout, container, false);
        switchCamera = (Button)view.findViewById(R.id.switchCamera);
        surfaceview = (SurfaceView) view.findViewById(R.id.surface);
        surfaceview.getHolder().addCallback(this);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ret = Gpioctljni.switchCameraIndex(index);
                index = 1 - index;
                if (ret == -1) {
                    index = 1 - index;
                    Toast.makeText(getActivity(), "摄像头未打开或异常，无法操作", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "切换到" + index + "摄像头", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkCamera = (Button)view.findViewById(R.id.checkCamera);
        checkCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ret = Gpioctljni.getCameraStatus(0x01);
                if(ret == -1){
                    Toast.makeText(getActivity(), "无法操作摄像头", Toast.LENGTH_SHORT).show();
                }
                else if((ret&0x00000080) > 0){
                    Toast.makeText(getActivity(), "未检测到摄像头", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "摄像头已插入", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //System.exit(0);
                MainActivity.this.finish();
            }
        });*/
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static DVRFragment newInstance(String content) {
        DVRFragment fragment = new DVRFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            cam = Camera.open();// 打开摄像头
            Gpioctljni.switchCameraIndex(index);
            cam.setPreviewDisplay(holder);
            cam.startPreview();

        } catch (Exception e) {
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            cam = Camera.open();// 打开摄像头
            Gpioctljni.switchCameraIndex(index);
            cam.setPreviewDisplay(holder);
            cam.startPreview();

        } catch (Exception e) {
        }

    }

}

