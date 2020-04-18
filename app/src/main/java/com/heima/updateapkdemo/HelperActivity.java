package com.heima.updateapkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

public class HelperActivity extends AppCompatActivity {

    private String TAG="HelperActivity=======";
    private TextView progress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        progress = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.start_btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://baicaiyouxuan.oss-cn-shenzhen.aliyuncs.com/baicaiyouxuan.apk";
                new AppUpdateHelper(HelperActivity.this, url).setonDownLoadListener(new AppUpdateHelper.onDownLoadListener() {
                    @Override
                    public void pending() {
                        Log.e(TAG,"pending。。。。");
                    }

                    @Override
                    public void progress(int currentBytes, int totalBytes) {
                        progress.setText("currentBytes="+(currentBytes/(1024*1024))+"M, totalBytes="+(totalBytes/(1024*1024))+"M");
                        progressBar.setProgress(currentBytes);
                        progressBar.setMax(totalBytes);
                        Log.e(TAG,"progress。。。。currentBytes="+currentBytes+",totalBytes="+totalBytes);
                    }

                    @Override
                    public void completed(String apkPath) {
                        Log.e(TAG,"completed。。。。");
                        progress.setText("apk路径="+apkPath);
                    }

                    @Override
                    public void error() {
                        Log.e(TAG,"error。。。。");
                    }
                }).start();
            }
        });

    }

}
