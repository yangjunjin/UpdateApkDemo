package com.heima.updateapkdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private int downloadId;
    private Button startBtn;
    private TextView speedTv;
    private ProgressBar progressBar;
    private boolean firstTime = true;//是否第一次
    private String TAG = "SingleTaskTestActivity=====";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startBtn = findViewById(R.id.start_btn_2);
        speedTv = findViewById(R.id.speed_tv_2);
        progressBar = findViewById(R.id.progressBar_2);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader.setup(MainActivity.this);
                downloadId = createDownloadTask().start();
            }
        });

    }

    String mApkkUrl = "";

    private BaseDownloadTask createDownloadTask() {

        mApkkUrl = "https://baicaiyouxuan.oss-cn-shenzhen.aliyuncs.com/baicaiyouxuan.apk";
        final String path = getExternalCacheDir().getPath();
        File files = new File(path);
        if(!files.exists())files.mkdir();


        File file = new File(path + "/baicaiyouxuan.apk");
        if (file.exists()) file.delete();


        FileDownloader.getImpl().clear(downloadId, mApkkUrl);

        return FileDownloader.getImpl().create(mApkkUrl)
                .setPath(path, true)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    //准备中。。。。。。。
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        File file = new File(path + "/baicaiyouxuan.apk");
                        if (file.exists()) file.delete();
                        Toast.makeText(MainActivity.this, "准备下载", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "pending");
                    }

                    //下载进度。。。。。
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        Log.e(TAG, "progress");
                        if (totalBytes == -1) {
                            progressBar.setIndeterminate(true);
                        } else {
                            progressBar.setMax(totalBytes);
                            progressBar.setProgress(soFarBytes);
                        }


                        if (speedTv != null) {
                            speedTv.setText(String.format("sofar: %fM total: %fM", soFarBytes/(1024*1024.0), totalBytes/(1024*1024.0)));
                        }
                    }

                    //下载完成。。。。。。
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        Log.e(TAG, "completed");
                        speedTv.setText(String.format("totalSize: %fM", task.getSmallFileTotalBytes()/(1024*1024.0)));
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();

                        progressBar.setIndeterminate(false);
                        progressBar.setMax(task.getSmallFileTotalBytes());
                        progressBar.setProgress(task.getSmallFileSoFarBytes());


                        FileDownloader.getImpl().clear(downloadId, mApkkUrl);
                    }

                    //下载失败
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        progressBar.setIndeterminate(false);
                        e.printStackTrace();
                        Log.e(TAG, "error");

                        //重新下载
                        FileDownloader.getImpl().clear(downloadId, mApkkUrl);
                        if (firstTime) {
                            firstTime = false;
                            File file = new File(path + "/baicaiyouxuan.apk");
                            if (file.exists()) file.delete();
                            downloadId = createDownloadTask().start();
                        }
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().clear(downloadId, mApkkUrl);
    }
}
