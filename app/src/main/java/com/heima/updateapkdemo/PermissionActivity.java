package com.heima.updateapkdemo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;


/**
 * 动态权限的申请
 */
public class PermissionActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 1;
    private final String[] permissionManifest = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);
        ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
//        if (!permissionCheck()) {
//            if (Build.VERSION.SDK_INT >= 23) {
//                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
//            }
//        }
        Log.e("权限申请===",permissionCheck()+"");
    }

    /**
     * 查询是否有权限
     * @return
     */
    private boolean permissionCheck() {
        //小于23，不需要权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        int permissionCheck = PackageManager.PERMISSION_GRANTED;//0
        String permission;
        for (int i = 0; i < permissionManifest.length; i++) {
            permission = permissionManifest[i];
            //ermissionChecker.checkSelfPermission(this, permission)==0就表示有这个权限
            //PackageManager.PERMISSION_GRANTED=0权限授予
            //PackageManager.PERMISSION_DENIED=-1//没有权限
            if (PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
                return false;
            }
        }
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 权限申请的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("TAG",grantResults[0]+","+shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "获取到相机权限, 打开相机", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "获取相机权限失败", Toast.LENGTH_SHORT).show();
                }

//                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(PermissionActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(PermissionActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
//                }
            }
        }

    }

}

