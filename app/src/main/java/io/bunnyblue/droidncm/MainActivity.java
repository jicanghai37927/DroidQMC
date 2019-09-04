package io.bunnyblue.droidncm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import app.haiyunshan.droidqmc.R;
import io.bunnyblue.droidncm.utils.NcmdumpUtils;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = new File("/sdcard/郑智化 - 大国民.ncm");
        System.out.println(file.getAbsolutePath() + " " + file.exists());
        String newPath = NcmdumpUtils.ncpDump(file);
        System.err.println(newPath);
    }
}
