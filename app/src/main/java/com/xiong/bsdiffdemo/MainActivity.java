package com.xiong.bsdiffdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
                intent.setDataAndType(Uri.fromFile(new File(mPath + "app1.apk")), "application/vnd.android.package-archive");
                startActivity(intent);
                break;

        }
    }
}
