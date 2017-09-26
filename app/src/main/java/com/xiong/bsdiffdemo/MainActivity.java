package com.xiong.bsdiffdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiong.bsdiffdemo.util.ApkUtils;
import com.xiong.bsdiffdemo.util.Diffutils;
import com.xiong.bsdiffdemo.util.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBt1;
    private Button mBt2;
    // 根目录
    private String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

    private int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermission();
    }

    private void initViews() {
        mBt1 = (Button) findViewById(R.id.bt_1);
        mBt2 = (Button) findViewById(R.id.bt_2);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_1:

                // 生成patch包
                Diffutils.generateDiffApk(ApkUtils.extract(this), mPath + "app2.apk", mPath + "app3.patch");
                Toast.makeText(this, "生成patch包成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_2:

                // 复制出base包
                FileUtils.copyFile(ApkUtils.extract(this), mPath + "app1.apk");

                // 合并patch包和base包
                Diffutils.mergeDiffApk(ApkUtils.extract(this), mPath + "app1.apk", mPath + "app3.patch");

                // 安装更新
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //7.0适配
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileProvider", new File(mPath + "app1.apk"));
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(new File(mPath + "app1.apk")), "application/vnd.android.package-archive");
                }
                startActivity(intent);
                break;

        }
    }

    /**
     * 申请权限
     */
    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, REQUEST_CODE);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                mBt1.setEnabled(false);
                mBt2.setEnabled(false);
            }
        }
    }
}
