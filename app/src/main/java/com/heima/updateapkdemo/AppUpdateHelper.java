package com.heima.updateapkdemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * author : yangjunjin
 * date : 2020/4/18 15:11
 * APP更新辅助的类
 */
public class AppUpdateHelper {

    private String TAG = "AppUpdateHelper";

    private Context mContext;//上下文
    private String mDownLoadUrl = "https://baicaiyouxuan.oss-cn-shenzhen.aliyuncs.com/baicaiyouxuan.apk";//app下载的路径
    private String mFolderPath;//下载apk存放的文件夹
    private String mApkPath;//apk存放的路径
    private int downloadId;//用于清除的标志
    private boolean retryLoad = true;//是否重新下载

    /**
     * 初始化
     *
     * @param context
     * @param apkUrl
     * @return
     */

    public AppUpdateHelper(Context context, String apkUrl) {
        mContext = context;
        mDownLoadUrl = apkUrl;
        mFolderPath = context.getExternalCacheDir().getPath();
        mApkPath = mFolderPath + File.separator + getFileName();
        FileDownloader.setup(mContext);
    }

    private String getFileName() {
        String name = "";
        if (!TextUtils.isEmpty(mDownLoadUrl))
            name = mDownLoadUrl.substring(mDownLoadUrl.lastIndexOf("/"));
        return name;
    }

    /**
     * 开始下载apk
     */
    public void start() {
        //判断文件夹是否存在
        File folderFile = new File(mFolderPath);
        if (!folderFile.exists())
            folderFile.mkdir();

        //判断apk是否存在，如果存在删除
        File apkFile = new File(mApkPath);
        if (apkFile.exists())
            apkFile.delete();

        //每次重新下载需要清除上次的任务
        FileDownloader.getImpl().clear(downloadId, mDownLoadUrl);
        downloadId = createDownloadTask().start();
    }

    private BaseDownloadTask createDownloadTask() {

        return FileDownloader.getImpl().create(mDownLoadUrl)
                .setPath(mFolderPath, true)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    //准备中。。。。。。。
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        if(mListener!=null) mListener.pending();
                        Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "pending");
                    }

                    //下载进度。。。。。
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        if(mListener!=null) mListener.progress(soFarBytes, totalBytes);
                        Log.e(TAG, "progress=" + String.format("sofar: %fM total: %fM", soFarBytes / (1024 * 1024.0), totalBytes / (1024 * 1024.0)));

                        if (totalBytes == -1) {
                            //speedTv.setText(String.format("sofar: %fM total: %fM", soFarBytes / (1024 * 1024.0), totalBytes / (1024 * 1024.0)));
                        }
                    }

                    //下载完成。。。。。。
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        if(mListener!=null) mListener.completed(mApkPath);
                        Log.e(TAG, "completed");
                        Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();
                        //下载完成删除这个任务
                        FileDownloader.getImpl().clear(downloadId, mDownLoadUrl);
                    }

                    //下载失败
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        if(mListener!=null) mListener.error();
                        //下载出错，重新下载
                        Log.e(TAG, "error");
                        FileDownloader.getImpl().clear(downloadId, mDownLoadUrl);
                        try {
                            if (retryLoad) {
                                retryLoad = false;
                                start();
                            }
                            e.printStackTrace();
                        } catch (Exception e1) {
                            e1.getStackTrace();
                        }
                    }

                });
    }


    public onDownLoadListener mListener;

    public AppUpdateHelper setonDownLoadListener(onDownLoadListener listener) {
        this.mListener = listener;
        return this;
    }

    public interface onDownLoadListener {
        void pending();//开始下载

        void progress(int currentBytes, int totalBytes);//下载进度

        void completed(String apkPath);//下载完成

        void error();//下载出错
    }
}
