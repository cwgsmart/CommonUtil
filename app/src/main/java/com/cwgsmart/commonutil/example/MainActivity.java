package com.cwgsmart.commonutil.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.cwgsmart.commonutil.FilesUtils;
import com.cwgsmart.commonutil.StringUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File filesDir = getFilesDir();
        String subFileName = "hoho";
        File file = new File(filesDir, subFileName);
        File textFile = new File(file, "text.txt");
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String isWriteSuccess = FilesUtils.readByteFromFile(textFile);
        boolean chinesePhone = StringUtils.isChinesePhone("12802551216");
        Log.e(TAG, "isChinese phone " + chinesePhone);

//        boolean dir = FilesUtils.fileExist(filesDir.getAbsolutePath(),"hoho");
        Log.e(TAG, "result = " + isWriteSuccess);
    }
}