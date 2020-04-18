package com.heima.updateapkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.heima.updateapkdemo.appupdate.AndroidUtil;
import com.heima.updateapkdemo.appupdate.AppUpdateHelper;

public class HelperActivity extends AppCompatActivity {

    private String TAG = "HelperActivity=======";
    private TextView progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        progress = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);

        AndroidUtil.applyInstallPermission(HelperActivity.this);

        findViewById(R.id.start_btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AndroidUtil.hasInstallPermission()) {
//                    startDownload();
//                } else {
//                    applyInstallPermission(HelperActivity.this);
//                }
            }
        });
    }
//    public   void applyInstallPermission(@NonNull Activity activity){
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.FOREGROUND_SERVICE,
//                        Manifest.permission.REQUEST_INSTALL_PACKAGES},
//                100);
//    }

    private void startDownload() {
        String url = "https://baicaiyouxuan.oss-cn-shenzhen.aliyuncs.com/baicaiyouxuan.apk";
        new AppUpdateHelper(HelperActivity.this, url).setonDownLoadListener(new AppUpdateHelper.onDownLoadListener() {
            @Override
            public void pending() {
                Log.e(TAG, "pending。。。。");
            }

            @Override
            public void progress(int currentBytes, int totalBytes) {
                progress.setText("currentBytes=" + (currentBytes / (1024 * 1024)) + "M, totalBytes=" + (totalBytes / (1024 * 1024)) + "M");
                progressBar.setProgress(currentBytes);
                progressBar.setMax(totalBytes);
                Log.e(TAG, "progress。。。。currentBytes=" + currentBytes + ",totalBytes=" + totalBytes);
            }

            @Override
            public void completed(String apkPath) {
                Log.e(TAG, "completed。。。。");
                progress.setText("apk路径=" + apkPath);
                String appid="com.heima.updateapkdemo";
                AndroidUtil.installApk(HelperActivity.this,appid,apkPath);
            }

            @Override
            public void error() {
                Log.e(TAG, "error。。。。");
            }
        }).start();
    }


    /**
     * 申请权限返回
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AndroidUtil.PERMISSION_REQUEST_CODE) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                    startDownload();
                    Toast.makeText(HelperActivity.this, "权限申请成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(HelperActivity.this, "权限申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
