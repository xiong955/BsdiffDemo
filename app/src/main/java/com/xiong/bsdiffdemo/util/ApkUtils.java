package com.xiong.bsdiffdemo.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @author: xiong
 * @time: 2017/09/26
 * @说明: 获取base基包地址
 */

public class ApkUtils {
    public static String extract(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationContext().getApplicationInfo();
        String apkPath = applicationInfo.sourceDir;
        return apkPath;
    }

}
