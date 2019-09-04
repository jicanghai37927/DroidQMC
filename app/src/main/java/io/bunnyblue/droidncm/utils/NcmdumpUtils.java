package io.bunnyblue.droidncm.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import multithreads.SingleTask;

public class NcmdumpUtils {

    public static String ncpDump(File file) {
        String targetFile = "";

        if (TextUtils.isEmpty(targetFile)) {
            File outPath = new File(Environment.getExternalStorageDirectory(), "Music");
            outPath.mkdirs();

            outPath = new File(outPath, file.getName() + ".mp3");
            try {
                File out = SingleTask.decode(file, outPath);
                targetFile = (out == null || !out.exists())? targetFile: out.getAbsolutePath();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return targetFile;
    }
}
